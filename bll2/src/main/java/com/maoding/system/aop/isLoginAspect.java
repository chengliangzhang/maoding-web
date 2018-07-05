package com.maoding.system.aop;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.util.StringUtil;
/**深圳市设计同道技术有限公司
 * 类    名：AuthorityCheckableAOP
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年8月31日-上午9:59:19
 */
@Aspect
public class isLoginAspect {
	private final Logger log=LoggerFactory.getLogger(getClass());
	
	@Pointcut("execution(@com.maoding.core.annotation.isLogin * com.maoding..controller.*.*(..))")
	public void isLogin(){};
	
	@Around("isLogin()")
	public Object check_Login(ProceedingJoinPoint call) throws Throwable{
		boolean passable=false;
		BaseController bc=(BaseController) call.getTarget();
		if(bc.getSession().getAttribute("user")!=null){
			passable=true;
		}
		if(passable){
//				log.debug("拦截到[{}]类方法::[{}({})]执行",bc.getClass(),ms.getMethod().getName(),StringUtil.join(",", call.getArgs()));
			return call.proceed();
		}else{
			log.debug("用户鉴权失败，令牌过期或无效::[{}]",StringUtil.join(",", call.getArgs()));
			throw new LoginException();
			//response.sendRedirect("home/views/login/login.html");
		}
	}

}
