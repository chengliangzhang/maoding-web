package com.maoding.admin.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.bean.Sms;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.MD5Helper;
import com.maoding.core.util.SecurityCodeUtil;
import com.maoding.core.util.StringUtil;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.dto.LoginAdminDTO;
import com.maoding.org.dto.TeamOperaterDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.CompanyUserAuditService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.org.service.TeamOperaterService;
import com.maoding.system.service.SystemService;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.dto.RegisterCompanyDTO;
import com.maoding.user.dto.ShareInvateDTO;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：SystemController
 * 类描述：系统公共controller
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午11:50:16
 */
@Controller
@RequestMapping("/iAdmin/sys")
public class AdminSystemController extends BaseController{
	
	@Autowired
	private SmsSender smsSender;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SystemService systemService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyUserService companyUserService;

	@Autowired
	private CompanyUserAuditService companyUserAuditService;

	@Autowired
	private TeamOperaterService teamOperaterService;

	@Value("${server.url}")
	protected String serverUrl;

	@RequestMapping("/adminLogin")
	public String adminLoginStep1() {
		return "admin/views/login/adminLogin";
	}

	@RequestMapping("/adminRegister")
	public String adminRegister() {

		return "admin/views/register/adminRegister";
	}

	/**
	 * 管理员－重置密码－界面
	 * @return
     */
	@RequestMapping(value = "/adminForgetManagerPwd/{companyId}/{userId}", method = RequestMethod.GET)
	public String adminForgetManagerPwd(@PathVariable String companyId,@PathVariable String userId, ModelMap model) {
		CompanyEntity companyEntity = companyService.selectById(companyId);
		model.addAttribute("companyName",companyEntity.getCompanyName());
		model.addAttribute("userId",userId);
		return "admin/views/forget/adminForgetManagerPwd";
	}

	/**
	 * 分享邀请 界面
	 * @return
     */
	@RequestMapping(value = "/shareInvitation/{companyId}/{userId}", method = RequestMethod.GET)
	public String shareInvitation(@PathVariable("companyId") String companyId,@PathVariable("userId") String userId,ModelMap model) throws Exception {
		CompanyDTO companyInfo = companyService.getCompanyById(companyId);
		CompanyUserEntity userInfo = companyUserService.getCompanyUserByUserIdAndCompanyId(userId,companyId);
		model.addAttribute("companyInfo",companyInfo);
		model.addAttribute("userInfo",userInfo);
		model.addAttribute("fastdfsUrl",fastdfsUrl);

		return "admin/views/register/shareRegister";
	}

	/**
	 * 分享邀请 界面2
	 * @return
	 */
	@RequestMapping(value = "/shareInvitation/{companyId}", method = RequestMethod.GET)
	public String shareInvitation2(@PathVariable("companyId") String companyId,ModelMap model) throws Exception {
		CompanyDTO companyInfo = companyService.getCompanyById(companyId);
		model.addAttribute("companyInfo",companyInfo);
		model.addAttribute("fastdfsUrl",fastdfsUrl);

		return "admin/views/register/shareRegister";
	}

	@RequestMapping("/adminForgetLoginPwdStep")
	public String adminForgetLoginPwdStep() {
		return "forget/adminForgetLoginPwdStep";
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
			@PathVariable("code") String code) throws Exception{
		if (!checkCode(cellphone, code)) {
			return ajaxResponseError("短信验证码有误，请重新输入").setCode("3");
		}
		AccountDTO  account= accountService.getAccountDtoByCellphoneOrEmail(cellphone);
		if(account==null || "1".equals(account.getStatus())){
			//需要填写姓名和密码
			return ajaxResponseSuccess().setData(true);
		}
		//不需要填写姓名和密码
		return ajaxResponseSuccess().setData(false);
	}
	
	/**
	 * 方法描述：个人账号注册
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:25:03
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage register(@RequestBody AccountDTO dto) throws Exception{
		dto.setPassword(MD5Helper.getMD5For32(dto.getPassword()));
		return accountService.register(dto);
	}
	
	/**
	 * 方法描述：企业注册
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:25:22
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/registerCompany", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage registerCompany(@RequestBody RegisterCompanyDTO dto) throws Exception{
        if(null==dto.getAdminPassword()||"".equals(dto.getAdminPassword())){
        	return ajaxResponseError("团队管理密码不能为空！");
        }           
        AjaxMessage ajax = accountService.registerCompany(dto);
		if(ajax.getCode().equals("0")){
			AccountDTO accountDTO = (AccountDTO)ajax.getData();
			//创建用户名和密码的令牌
			UsernamePasswordToken token = new UsernamePasswordToken(accountDTO.getCellphone(),accountDTO.getPassword());
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
				Map<String,Object> m = systemService.getUserSessionObjOfAdmin((AccountDTO)ajax.getData());
				setSession(m);
				return this.ajaxResponseSuccess().setInfo("登录成功").setData(dto);
			}
			return this.ajaxResponseError("注册成功，自动登录失败");
		}
		return ajax;
	}
	

	/**
	 * 方法描述：设置session
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午5:29:12
	 * @param m
	 */
	public void setSession(Map<String,Object> m){
		this.getSession().setAttribute("user", m.get("user"));
		this.getSession().setAttribute("userId", m.get("userId"));
		//当前组织， 切换组织的时候，companyId需要更换
		this.getSession().setAttribute("adminCompanyId", m.get("companyId"));
		this.getSession().setAttribute("adminCompany", m.get("company"));
		this.getSession().setAttribute("companyId", m.get("companyId"));
		this.getSession().setAttribute("company", m.get("company"));
		this.getSession().setAttribute("roleCompany", m.get("roleCompany"));
		this.getSession().setAttribute("role", m.get("role"));
		this.getSession().setAttribute("orgType", "company");
		this.getSession().setAttribute("fastdfsUrl", fastdfsUrl);
	}

	
	/**
	 * 方法描述：企业登录（第一步：用户账号登录，第二步：验证管理员密码）【此方法验证管理员密码】
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:26:23
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/loginAdmin", method = RequestMethod.POST)
	@ResponseBody
	public  AjaxMessage loginAdmin(@RequestBody LoginAdminDTO dto) throws Exception{
		dto.setAdminPassword(MD5Helper.getMD5For32(dto.getAdminPassword()));
		AjaxMessage ajax = systemService.loginAdmin(dto);
		if("0".equals(ajax.getCode())){
			Map<String,Object> m= (Map<String,Object>)ajax.getData();
//			this.getSession().setAttribute("userId",  m.get("userId"));
//			this.getSession().setAttribute("user",  m.get("user"));
			//admin登录成功，把当前组织id保存到session中（adminCompanyId）
			this.getSession().setAttribute("adminCompanyId", m.get("companyId"));
			this.getSession().setAttribute("adminCompany", m.get("company"));

			//当前组织， 切换组织的时候，companyId需要更换
			this.getSession().setAttribute("companyId", m.get("companyId"));
			this.getSession().setAttribute("company", m.get("company"));

		}
		return ajax; 
	}
	

	/*******************分享邀请注册*******************/
	@RequestMapping(value = "/shareInvateRegister", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage shareInvateRegister(@RequestBody ShareInvateDTO dto)throws Exception{
		if(!checkCode(dto.getCellphone(),dto.getCode())){
			return ajaxResponseError("短信验证码有误，请重新输入");
		}
		return companyUserAuditService.shareInvateRegister(dto);
	}

	/**
	 * 方法描述：退出，注销登录
	 * 作        者：MaoSF
	 * 日        期：2015年12月16日-下午3:15:03
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout() throws Exception{
		/*this.getSession().removeAttribute("user");
		this.getSession().removeAttribute("userId");
		this.getSession().removeAttribute("companyId");
		this.getSession().removeAttribute("company");
		this.getSession().removeAttribute("roleCompany");
		this.getSession().removeAttribute("orgType");*/
		this.getSession().removeAttribute("adminCompanyId");
		this.getSession().removeAttribute("adminCompany");
		SecurityUtils.getSubject().logout();
		return "views/home/login";
	}



	/**
	 * 方法描述：忘记密码（发送验证码）
	 * 作        者：MaoSF
	 * 日        期：2015年12月16日-下午3:15:03
	 * @return
	 */
	@RequestMapping(value = "/sendSecurityCodeAndValidateCellphone/{userId}/{cellphone}", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage sendSecurityCodeAndValidateCellphone(@PathVariable("userId") String userId,@PathVariable("cellphone") String cellphone) throws Exception{
		if (StringUtil.isNullOrEmpty(userId)) {
			userId = this.getFromSession("userId",String.class);
		}
		if(null == accountService.getAccountDtoByCellphoneOrEmail(cellphone)){
			return this.ajaxResponseError("手机号不存在");
		}else{
			AccountDTO accountDTO = accountService.getAccountById(userId);
			if(accountDTO.getCellphone().equals(cellphone))
			{
				return this.sendSecurityCode(cellphone);
			}
			return this.ajaxResponseError("手机号与当前组织不匹配");
		}
	}

	/**
	 * 方法描述：修改管理员密码
	 * 作        者：MaoSF
	 * 日        期：2016年4月26日-下午2:24:31
	 * @return
	 */
	@RequestMapping(value="/updateAdminPassword", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage updateAdminPassword(@RequestBody TeamOperaterDTO dto) throws Exception{
		dto.setAdminPassword(MD5Helper.getMD5For32(dto.getAdminPassword()));
		return teamOperaterService.updateAdminPassword(dto);
	}

	/**
	 * 方法描述：获取公司邀请链接
	 * 作        者：Chenxj
	 * 日        期：2016年4月19日-上午11:36:44
	 *
	 * @param map{userId,token}
	 * @return link
	 */
	@RequestMapping("/invitation_link")
	@ResponseBody
	public AjaxMessage invitationLink(@RequestBody Map<String, String> map) {
		String link = serverUrl + "/iAdmin/sys/shareInvitation/" + map.get("companyId") + "/" + map.get("accountId");
		return this.ajaxResponseSuccess().setData(link);
	}

}
