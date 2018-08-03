package com.maoding.core.base.controller;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.StringUtils;
import com.maoding.exception.CustomException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：BaseController
 * 类描述：基础Controller抽象类
 * 作    者：Chenxj
 * 日    期：2015年6月23日-下午5:07:54
 */
public abstract class BaseController {
	protected final Logger log=LoggerFactory.getLogger(getClass());
	
	@Value("${server.url}")
	protected String serverUrl;
	
	@Value("${fastdfs.url}")
	protected String fastdfsUrl;

	protected String currentUserId;
	protected String currentCompanyId;
	protected String currentCompanyUserId;


	@Autowired
	private JedisPool jedisPool;

	public void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public Jedis getJedis() {
		return jedisPool.getResource();
	}

	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.get(key);
		} finally {
			returnResource(jedis);
		}
	}

	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key, value);
		} finally {
			returnResource(jedis);
		}
	}

	public Long incr(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.incr(key);
		} finally {
			returnResource(jedis);
		}
	}

	public Session getSession(){
		return SecurityUtils.getSubject().getSession();
	}

	protected void saveToSession(String key,Object value){
		SecurityUtils.getSubject().getSession().setAttribute(key, value);
	}
	protected Object getFromSession(String key){
		return SecurityUtils.getSubject().getSession().getAttribute(key);
	}
	/**
	 * 方法描述：获取验证验证，把手机号当做为key值
	 * 作        者：MaoSF
	 * 日        期：2016年6月15日-下午5:34:28
	 * @param key
	 * @param code
	 */
	protected void saveCodeToSession(String key,String code){
		SecurityUtils.getSubject().getSession().setAttribute(key, code);
		SecurityUtils.getSubject().getSession().setAttribute(key+"-timeout", System.currentTimeMillis());
	}
	
	protected boolean checkCode(String key,String code){
		String codeLocal=getFromSession(key, String.class);
		if(codeLocal==null){
			return false;
		}
		long timeout=System.currentTimeMillis()-getFromSession(key+"-timeout", long.class)-SystemParameters.SECURITY_CODE_10_MAX_LIVE_TIME;
		return timeout < 0 && code.equalsIgnoreCase(codeLocal);
	}

	protected <T>T getFromSession(String key,Class<T>clazz){
		return (T) SecurityUtils.getSubject().getSession().getAttribute(key);
	}

	/**
	 * 方法描述：是否有角色
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:11
	 * @param role
	 * @return
	 */
	protected boolean hasRole(String role){
		return SecurityUtils.getSubject().hasRole(role);
	}
	/**
	 * 方法描述：是否包含所有角色
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:19
	 * @param roles
	 * @return
	 */
	protected boolean hasAllRoles(String...roles){
		Set<String> rs=new HashSet<String>();
		for(String r:roles){
			rs.add(r);
		}
		return SecurityUtils.getSubject().hasAllRoles(rs);
	}
	/**
	 * 方法描述：是否包含所有角色
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:21
	 * @param roles
	 * @return
	 */
	protected boolean hasAllRoles(Collection<String>roles){
		return SecurityUtils.getSubject().hasAllRoles(roles);
	}
	/**
	 * 方法描述：是否包含某个角色
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:23
	 * @param roles
	 * @return
	 */
	protected boolean hasRoles(String...roles){
		for(String r:roles){
			if(hasRole(r)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 方法描述：是否包含某个角色
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:26
	 * @param roles
	 * @return
	 */
	protected boolean hasRoles(Collection<String>roles) {
		for(String r:roles){
			if(hasRole(r)){
				return true;
			}
		}
		return false;
	}




	protected AjaxMessage ajaxResponseError(String error){
		AjaxMessage m=new AjaxMessage();
		m.setCode("1");
		m.setInfo(error);
		m.setData(error);
		return m;
	}

	protected AjaxMessage ajaxResponseError(Object error){
		AjaxMessage m=new AjaxMessage();
		m.setCode("1");
		m.setInfo(error);
		return m;
	}
	
	/**
	 * 方法描述：异常处理返回
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-上午10:27:17
	 * @param error
	 * @return
	 */
	protected AjaxMessage ajaxResponseExceptionError(Exception error){
		AjaxMessage m=new AjaxMessage();
		m.setCode(SystemParameters.EXCEPTION_CODE);
		m.setInfo("系统异常");
		return m;
	}
	
	protected AjaxMessage ajaxResponseLoginExceptionError(){
		AjaxMessage m=new AjaxMessage();
		m.setCode(SystemParameters.SESSION_TIMEOUT_CODE);
		m.setInfo("登录状态已超时！");
		return m;
	}

	protected AjaxMessage ajaxResponseUnauthorizedException(){
		AjaxMessage m=new AjaxMessage();
		m.setCode("1");
		m.setInfo("对不起，您没有权限操作！");
		return m;
	}

	protected AjaxMessage ajaxResponseSuccess(){
		AjaxMessage m=new AjaxMessage();
		m.setCode("0");
		return m;
	}
	protected AjaxMessage ajaxResponseSuccess(Object success){
		AjaxMessage m=new AjaxMessage();
		m.setCode("0");
		if (success instanceof String) {
			m.setData(success);
			m.setInfo(success);
		} else {
			m.setData(success);
		}
		return m;
	}

	 
	@ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception ex, HttpServletRequest request) throws Exception {
        log.error(request.toString());
        ex.printStackTrace();
		if (isAjaxRequest(request)) {
			log.info("不是预期的HttpServletRequest");
			if (ex instanceof LoginException) {
				log.error("登录异常:",ex);
				return ajaxResponseLoginExceptionError();
			} else if(ex instanceof UnauthorizedException){
				log.warn("权限校验未通过");
				return ajaxResponseUnauthorizedException();
			} else if(ex instanceof CustomException){
				return AjaxMessage.error(ex.getMessage());
			} else{
				log.info("其他情况");
				if (ex != null){
					log.error("存在异常:",ex);
				}
				return ajaxResponseExceptionError(ex);
			}
        }
		if(ex instanceof UnauthorizedException) {
			log.warn("权限校验未通过");
			return ajaxResponseUnauthorizedException();
		}
		if(ex instanceof NullPointerException) {
			log.error("空指针异常:",ex);
			return  AjaxMessage.failed("系统异常");
		}
		if (ex != null) {
			log.error("发生异常:", ex);
		}
		return  AjaxMessage.failed("数据异常");
    }

    private boolean isAjaxRequest(HttpServletRequest httpServletRequest) {
        return ("XMLHttpRequest").equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"));
    }

    private AjaxMessage handleRequestJsonException(Exception ex, HttpServletRequest request) {
    	if (ex instanceof LoginException) {
    		return ajaxResponseLoginExceptionError();
    	} else if(ex instanceof UnauthorizedException){
			return ajaxResponseUnauthorizedException();
		} else{
            return ajaxResponseExceptionError(ex);
    	}
    }

	//补充当前公司编号、当前用户编号
	protected void updateCurrentUserInfo(BaseDTO editDTO){
		if (StringUtils.isEmpty(editDTO.getCurrentCompanyId())){
			editDTO.setCurrentCompanyId(currentCompanyId);
		}
		if (StringUtils.isEmpty(editDTO.getAccountId())){
			editDTO.setAccountId(currentUserId);
		}
	}
}
