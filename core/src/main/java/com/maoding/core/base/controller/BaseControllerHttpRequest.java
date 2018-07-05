package com.maoding.core.base.controller;

import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.SystemParameters;
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

/**
 * 深圳市设计同道技术有限公司
 * 类    名：BaseController
 * 类描述：基础Controller抽象类
 * 作    者：Chenxj
 * 日    期：2015年6月23日-下午5:07:54
 */
public abstract class BaseControllerHttpRequest {
	protected final Logger log=LoggerFactory.getLogger(getClass());
	
	@Value("${server.url}")
	protected String serverUrl;
	
	@Value("${fastdfs.url}")
	protected String fastdfsUrl;

	protected String currentUserId;
	protected String currentCompanyId;

	@Autowired
	private HttpServletRequest httpServletRequest;


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

	protected void saveToSession(String key,Object value){
		httpServletRequest.getSession().setAttribute(key, value);
	}
	protected Object getFromSession(String key){
		return httpServletRequest.getSession().getAttribute(key);
	}
	/**
	 * 方法描述：获取验证验证，把手机号当做为key值
	 * 作        者：MaoSF
	 * 日        期：2016年6月15日-下午5:34:28
	 * @param key
	 * @param code
	 */
	protected void saveCodeToSession(String key,String code){
		httpServletRequest.getSession().setAttribute(key, code);
		httpServletRequest.getSession().setAttribute(key+"-timeout", System.currentTimeMillis());
	}
	
	protected boolean checkCode(String key,String code){
		String codeLocal=getFromSession(key, String.class);
		if(codeLocal==null){
			return false;
		}
		long timeout=System.currentTimeMillis()-getFromSession(key+"-timeout", long.class)-SystemParameters.SECURITY_CODE_10_MAX_LIVE_TIME;
		if(timeout<0 && code.equalsIgnoreCase(codeLocal)){
			return true;
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	protected <T>T getFromSession(String key,Class<T>clazz){
		return (T) httpServletRequest.getSession().getAttribute(key);
	}
	/**
	 * 方法描述：是否有角色
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:11
	 * @param role
	 * @return
	 */
	protected boolean hasRole(String role){
	//	return httpServletRequest.getSession().getAttribute("role").hasRole(role);
		return false;
	}
	/**
	 * 方法描述：是否包含所有角色
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:19
	 * @param roles
	 * @return
	 */
/*	protected boolean hasAllRoles(String...roles){
		Set<String>rs=new HashSet<String>();
		for(String r:roles){
			rs.add(r);
		}
		return SecurityUtils.getSubject().hasAllRoles(rs);
	}*/
	/**
	 * 方法描述：是否包含所有角色
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:21
	 * @param roles
	 * @return
	 */
	/*protected boolean hasAllRoles(Collection<String>roles){
		return SecurityUtils.getSubject().hasAllRoles(roles);
	}*/
//	/**
//	 * 方法描述：是否包含某个角色
//	 * 作        者：Chenxj
//	 * 日        期：2015年12月18日-下午3:34:23
//	 * @param roles
//	 * @return
//	 */
//	protected boolean hasRoles(String...roles){
//		for(String r:roles){
//			if(hasRole(r)){
//				return true;
//			}
//		}
//		return false;
//	}
//	/**
//	 * 方法描述：是否包含某个角色
//	 * 作        者：Chenxj
//	 * 日        期：2015年12月18日-下午3:34:26
//	 * @param roles
//	 * @return
//	 */
//	protected boolean hasRoles(Collection<String>roles) {
//		for(String r:roles){
//			if(hasRole(r)){
//				return true;
//			}
//		}
//		return false;
//	}
	/**
	 * 方法描述：是否用权限
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:30
	 * @param permission
	 * @return
	 */
/*	protected boolean hasPermission(String permission){
		return SecurityUtils.getSubject().isPermitted(permission);
	}*/
	/**
	 * 方法描述：是否包含某个权限
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:32
	 * @param permissions
	 * @return
	 */
	/*protected boolean hasPermissions(String...permissions){
		for(String permission:permissions){
			if(SecurityUtils.getSubject().isPermitted(permission)){
				return true;
			}
		}
		return false;
	}*/
	/**
	 * 方法描述：是否包含某个权限
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:36
	 * @param permissions
	 * @return
	 */
	/*protected boolean hasPermissions(Collection<String>permissions){
		for(String permission:permissions){
			if(hasPermission(permission)){
				return true;
			}
		}
		return false;
	}*/
	/**
	 * 方法描述：是否包含全部权限
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:39
	 * @param permissions
	 * @return
	 */
	/*protected boolean hasAllPermissions(String...permissions){
		return SecurityUtils.getSubject().isPermittedAll(permissions);
	}*/
	/**
	 * 方法描述：是否包含全部权限
	 * 作        者：Chenxj
	 * 日        期：2015年12月18日-下午3:34:42
	 * @param permissions
	 * @return
	 */
	/*protected boolean hasAllPermissions(Collection<String>permissions){
		return SecurityUtils.getSubject().isPermittedAll((String[]) permissions.toArray());
	}*/
	protected AjaxMessage ajaxResponseError(String error){
		AjaxMessage m=new AjaxMessage();
		m.setCode("1");
		m.setInfo(error);
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
		m.setInfo(error.getMessage());
		return m;
	}
	
	protected AjaxMessage ajaxResponseLoginExceptionError(){
		AjaxMessage m=new AjaxMessage();
		m.setCode(SystemParameters.SESSION_TIMEOUT_CODE);
		m.setInfo("登录状态已超时！");
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
		m.setInfo(success);
		return m;
	}
//	@ExceptionHandler
//	public void exceptionHandler(Exception e) throws Exception{
//		log.error("请求处理出现异常", e);
//		throw e;
//	}
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}
	
	 
	@ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception ex, HttpServletRequest request) throws Exception {
        if (isAjaxRequest(request)) {
            return handleRequestJsonException(ex, request);
        } else {
            throw ex;
        }
    }

    private boolean isAjaxRequest(HttpServletRequest httpServletRequest) {
        return ("XMLHttpRequest").equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"));
    }

    private AjaxMessage handleRequestJsonException(Exception ex, HttpServletRequest request) {
    	if (ex instanceof LoginException) {
    		return ajaxResponseLoginExceptionError();
    	} else {
            return ajaxResponseExceptionError(ex);
    	}
    }
}
