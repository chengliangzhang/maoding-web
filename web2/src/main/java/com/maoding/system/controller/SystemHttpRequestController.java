package com.maoding.system.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.bean.Sms;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.MD5Helper;
import com.maoding.core.util.SecurityCodeUtil;
import com.maoding.core.util.StringUtil;
import com.maoding.system.service.SystemService;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.entity.AccountEntity;
import com.maoding.user.service.AccountService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：SystemController
 * 类描述：系统公共controller
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午11:50:16
 */
@Controller
@RequestMapping("/iWork/SystemHttpRequestController")
public class SystemHttpRequestController extends BaseController{
	
	@Autowired
	private SmsSender smsSender;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SystemService systemService;

	@Value("${admin.server.url}")
	private String adminSserverUrl;

	@RequestMapping("/login")
	public String workLogin() {
		return "views/login/login";
	}
	@RequestMapping("/workRegister")
	public String workRegister() {
		return "work/views/register/workRegister";
	}
	@RequestMapping("/workForgetLoginPwd")
	public String workForgetLoginPwdStep() {
		return "work/views/forget/workForgetLoginPwd";
	}

	

	/**
	 * 方法描述：获取验证码
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-上午11:52:49
	 * @param cellphone
	 * @return
	 */
	@RequestMapping(value = "/securityCode/{cellphone}", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage sendSecurityCode(@PathVariable("cellphone") String cellphone){
		
		String securityCode=SecurityCodeUtil.createSecurityCode();
		//验证手机号是否注册，在账号输入框失去焦点时验证，在此不做验证
		if(StringUtil.isNumeric(cellphone)&&cellphone.length()==11){
			Sms sms=new Sms();
			sms.addMobile(cellphone);
			sms.setMsg(StringUtil.format(SystemParameters.SEND_CODE_MSG, securityCode));
			log.debug("短信发送结果::{}",smsSender.send(sms));
			saveCodeToSession(cellphone,securityCode);
			return this.ajaxResponseSuccess();
		}else{
			return ajaxResponseError("发送验证码失败");
		}
	}
	
	/**
	 * 方法描述：检查验证码 作 者：TangY 日 期：2016年7月7日-下午7:22:46
	 * 
	 * @param cellphone
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/validateCode/{cellphone}/{code}", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage validateCode(
			@PathVariable("cellphone") String cellphone,
			@PathVariable("code") String code) {
		if (!checkCode(cellphone, code)) {
			return ajaxResponseError("短信验证码有误，请重新输入").setCode("3");
		}
		return ajaxResponseSuccess();
	}


	/**
	 * 方法描述：个人账号注册
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:25:03
	 * @param dto
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	@ResponseBody
//	public AjaxMessage register(@RequestBody AccountDTO dto) throws Exception{
//		if(null==dto.getPassword()||"".equals(dto.getPassword())){
//			return ajaxResponseError("密码不能为空！");
//		}
//		dto.setPassword(MD5Helper.getMD5For32(dto.getPassword()));
//		AjaxMessage ajax=accountService.register(dto);
//		dto = (AccountDTO)ajax.getData();
//		if(!StringUtil.isNullOrEmpty(dto.getId())){
//			Map<String,Object> m = systemService.getUserSessionObjOfWork(dto,this.getHttpServletRequest());
//			setSession(m);
//			return ajaxResponseSuccess("注册成功");
//		}
//		return ajaxResponseError("注册失败");
//	}
	
	
	/**
	 * 方法描述：登录（用户登录 ）
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:26:23
	 * @param dto
	 * @return
	 */
//	@RequestMapping(value = "/login_bak", method = RequestMethod.POST)
//	@ResponseBody
//	public  AjaxMessage login_bak(@RequestBody AccountDTO dto) throws Exception{
//
//		if(null==dto.getPassword()||"".equals(dto.getPassword())){
//			return ajaxResponseError("密码不能为空！");
//		}
//		dto.setPassword(MD5Helper.getMD5For32(dto.getPassword()));
//
//		AjaxMessage ajax = systemService.login(dto);
//		if("0".equals(ajax.getCode())){
//			Map<String,Object> m = systemService.getUserSessionObjOfWork((AccountDTO)ajax.getData(),this.getHttpServletRequest());
//			setSession(m);
//		}
//		return ajax;
//	}

	/**
	 * 方法描述：登录（用户登录 ）shiro
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:26:23
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public  AjaxMessage login(@RequestBody AccountDTO dto) throws Exception{

		if(null==dto.getPassword()||"".equals(dto.getPassword())){
			return ajaxResponseError("密码不能为空！");
		}
		dto.setPassword(MD5Helper.getMD5For32(dto.getPassword()));

		//创建用户名和密码的令牌
		UsernamePasswordToken token = new UsernamePasswordToken(dto.getCellphone(),dto.getPassword());
		//记录该令牌，如果不记录则类似购物车功能不能使用。
		//token.setRememberMe(true);
		//subject理解成权限对象。类似user
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
		} catch (UnknownAccountException ex) {//用户名没有找到。
			return this.ajaxResponseError("帐号或密码错误");
		} catch (IncorrectCredentialsException ex) {//用户名密码不匹配。
			return this.ajaxResponseError("帐号或密码错误");
		}catch (AuthenticationException e) {//其他的登录错误
			return this.ajaxResponseError("帐号或密码错误");
		}
		//验证是否成功登录的方法
		if (subject.isAuthenticated()) {
			return this.ajaxResponseSuccess().setInfo("登录成功").setData(dto);
		}
		return this.ajaxResponseError("登录失败");
	}


	@RequestMapping(value = "/storeUser", method = RequestMethod.POST)
	@ResponseBody
	public  AjaxMessage storeUser() throws Exception{

		String userId = this.getFromSession("userId", String.class);
		this.set("userMarkId",userId);
		String adminServerUrl = adminSserverUrl;
		return new AjaxMessage().setCode("0").setData(adminServerUrl);
	}
	
	/**
	 * 方法描述：设置session
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午5:29:12
	 * @param m
	 */
//	public void setSession(Map<String,Object> m){
//		this.getHttpServletRequest().getSession().setAttribute("user", m.get("user"));
//		this.getHttpServletRequest().getSession().setAttribute("userId", m.get("userId"));
//		//当前组织， 切换组织的时候，companyId需要更换
//		this.getHttpServletRequest().getSession().setAttribute("companyId", m.get("companyId"));
//		this.getHttpServletRequest().getSession().setAttribute("company", m.get("company"));
//		this.getHttpServletRequest().getSession().setAttribute("roleCompany", m.get("roleCompany"));
//		this.getHttpServletRequest().getSession().setAttribute("orgType", "company");
//		this.getHttpServletRequest().getSession().setAttribute("fastdfsUrl", fastdfsUrl);
//	}
	

	/**
	 * 方法描述：忘记密码
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午4:13:09
	 * @param accountDto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage forgotPassword(@RequestBody AccountDTO accountDto)throws Exception{
		accountDto.setPassword(MD5Helper.getMD5For32(accountDto.getPassword()));
	   return systemService.forgotPassword(accountDto);
	}


	/**
	 * 方法描述：链接验证邮箱
	 * 作        者：MaoSF
	 * 日        期：2016年6月27日-下午3:58:38
	 * @param u（用户id）
	 * @param e（邮箱）
	 * @param code（验证码）
	 * @return
	 */
	@RequestMapping(value="/email-activate/verify-mail/{u}/{e}/{code}",method = RequestMethod.GET)
	public ModelAndView verifyMail(
			@PathVariable("u") String u,
			@PathVariable("e") String e,
			@PathVariable("code") String code)throws Exception{
		Map<String,Object> model = new HashMap<String,Object>(); ;
		//注册前验证是否已经注册过
		if(StringUtil.isNullOrEmpty(e)){
			model.put("code","1");
		}

		if( null !=accountService.getAccountByCellphoneOrEmail(e)){
			model.put("code","1");
		}
		AccountEntity user = accountService.selectById(u);
		if(user!=null){
			String emailCode = user.getEmialCode();
			if(emailCode !=null && !emailCode.equals("")){
				String _email = emailCode.split("-")[0];
				String _code = emailCode.split("-")[1];
				if(e.equals(_email) && code.equals(_code)){
					user.setEmail(e);
					user.setEmialCode("");
					accountService.updateById(user);
					model.put("code","0");
					model.put("email",e);
				}
				else{
					model.put("code","1");;
				}
			}
		}

		return new ModelAndView("work/views/personal_center/bindEmailSuccess",model);
	}

	/**
	 * 方法描述：退出，注销登录(shiro)
	 * 作        者：MaoSF
	 * 日        期：2015年12月16日-下午3:15:03
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout() throws Exception{
		SecurityUtils.getSubject().logout();
		return "views/home/login";
	}

	/**
	 * 方法描述：忘记密码（发送验证码）
	 * 作        者：MaoSF
	 * 日        期：2015年12月16日-下午3:15:03
	 * @return
	 */
	@RequestMapping(value = "/sendSecurityCodeAndValidateCellphone/{cellphone}", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage sendSecurityCodeAndValidateCellphone(@PathVariable("cellphone") String cellphone) throws Exception{
		if(null == accountService.getAccountDtoByCellphoneOrEmail(cellphone)){
			return this.ajaxResponseError("手机号不存在");
		}else{
			return this.sendSecurityCode(cellphone);
		}
	}


}
