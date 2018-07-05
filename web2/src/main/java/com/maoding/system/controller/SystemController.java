package com.maoding.system.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.common.service.UploadService;
import com.maoding.core.component.sms.SmsSender;
import com.maoding.core.component.sms.bean.Sms;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.MD5Helper;
import com.maoding.core.util.SecurityCodeUtil;
import com.maoding.core.util.StringUtil;
import com.maoding.message.service.UpdateNotifyService;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.dto.CompanyUserLiteDTO;
import com.maoding.org.dto.LoginAdminDTO;
import com.maoding.org.dto.TeamOperaterDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.CompanyUserAuditService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.org.service.TeamOperaterService;
import com.maoding.role.service.RoleUserService;
import com.maoding.system.service.DataDictionaryService;
import com.maoding.system.service.SystemService;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.dto.AppUseDTO;
import com.maoding.user.dto.RegisterCompanyDTO;
import com.maoding.user.dto.ShareInvateDTO;
import com.maoding.user.entity.AccountEntity;
import com.maoding.user.service.AccountService;
import com.maoding.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：SystemController
 * 类描述：系统公共controller
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午11:50:16
 */
@Controller
@RequestMapping("/iWork/sys")
public class SystemController extends BaseController{
	
	@Autowired
	private SmsSender smsSender;

	@Autowired
	private CompanyUserService companyUserService;

	@Autowired
	private TeamOperaterService teamOperaterService;

	@Autowired
	private CompanyUserAuditService companyUserAuditService;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private SystemService systemService;

	@Autowired
	private DataDictionaryService dataDictionaryService;

	@Autowired
	public UserService userService;

	@Autowired
	public UploadService uploadService;

	@Autowired
	public CompanyService companyService;

	@Autowired
	public RoleUserService roleUserService;

	@Autowired
	private UpdateNotifyService notifyService;

//    @Autowired
//    private ProjectTaskManagerService projectTaskManagerService;

	@Value("${admin.server.url}")
	private String adminSserverUrl;
	@Value("${helpDoc}")
	protected String helpDocUrl;
	@Value("${cdn.url}")
	protected String cdnUrl;


	/**
	 * 跳转到登录界面
	 * @return
	 * @throws Exception
     */
	@RequestMapping("/login")
	public String login()  throws Exception{
		if(SecurityUtils.getSubject().isAuthenticated()){
			/*systemService.getCurrUserInfoOfWork(model,this.getSession());
			List<DataDictionaryDTO> projectTypeList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_Type);//项目类别
			model.put("projectTypeList",projectTypeList);
			return "views/home/workbench";*/
			return "redirect:/iWork/home/workbench";
		}
		return "views/home/login";
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
        //return "redirect:/iWork/sys/login";
		return "redirect:/";
    }

	/**
	 * 帮助
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/helpCenter")
	public String helpCenter()  throws Exception{
		return "views/help/helpCenter";
	}
	@RequestMapping("/register")
	public String register(ModelMap model) {
		model.put("cdnUrl", cdnUrl);
		return "views/home/register";
	}

	@RequestMapping("/forgetLoginPwd")
	public String forgetLoginPwd() {

		return "views/home/forgetLoginPwd";
	}

	/**
	 * 方法描述：获取验证码
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-上午11:52:49
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/securityCode", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage sendSecurityCode(@RequestBody Map<String,Object> paraMap){
		String cellphone=paraMap.get("cellphone").toString();
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
	 * @param paraMap
	 * @return
	 */
	@RequestMapping(value = "/validateCode", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage validateCode(
			@RequestBody Map<String,Object> paraMap) {

		if(null==paraMap.get("cellphone").toString()){
			return ajaxResponseError("手机号码不能为空！");
		}
		if(null==paraMap.get("code").toString()){
			return ajaxResponseError("验证码不能为空！");
		}
		String cellphone=paraMap.get("cellphone").toString();
		String code=paraMap.get("code").toString();
		if (!checkCode(cellphone, code)) {
			return ajaxResponseError("短信验证码有误，请重新输入").setCode("3");
		}
		return ajaxResponseSuccess();
	}


	private AjaxMessage checkCode(AccountDTO dto){
		if(StringUtil.isNullOrEmpty(dto.getCellphone())){
			return ajaxResponseError("手机号码不能为空！");
		}
		if(StringUtil.isNullOrEmpty(dto.getCode())){
			return ajaxResponseError("验证码不能为空！");
		}
		if (!checkCode(dto.getCellphone(), dto.getCode())) {
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
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage register(HttpServletRequest request,@RequestBody AccountDTO dto) throws Exception{

	    AjaxMessage ajaxMessage=this.checkCode(dto);

		if(!"0".equals(ajaxMessage.getCode())){
			return  ajaxMessage;
		}

		if(null==dto.getPassword()||"".equals(dto.getPassword())){
			return ajaxResponseError("密码不能为空！");
		}
		if(null==dto.getCellphone()||"".equals(dto.getCellphone())){
			return ajaxResponseError("手机号码不能为空！");
		}
		//此处备份，为了登录，由于登录密码在login方法中加密
		AccountDTO loginDTO = new AccountDTO();
		BaseDTO.copyFields(dto,loginDTO);

		dto.setPassword(MD5Helper.getMD5For32(dto.getPassword()));
		AjaxMessage ajax=accountService.register(dto);
		dto = (AccountDTO)ajax.getData();
		if(!StringUtil.isNullOrEmpty(dto.getId())){
			this.login(request,loginDTO);
			return ajaxResponseSuccess("注册成功，欢迎进入卯丁！");
		}
		return ajaxResponseError("注册失败");
	}

	/**
	 * 方法描述：登录（用户登录 ）shiro
	 * 作        者：MaoSF
	 * 日        期：2016年7月7日-下午6:26:23
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public  AjaxMessage login(HttpServletRequest request,@RequestBody AccountDTO dto) throws Exception{

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
			//验证是否成功登录的方法
			if (subject.isAuthenticated()) {
				SavedRequest savedRequest = WebUtils.getSavedRequest(request);
				String redirectUrl = "";
				if (savedRequest != null)
				{
					redirectUrl = savedRequest.getRequestUrl();
					if(!StringUtil.isNullOrEmpty(redirectUrl)){
						if(redirectUrl.indexOf("/maoding")>-1){
							redirectUrl=redirectUrl.replaceAll("/maoding","");
						}
					}
				}
				return this.ajaxResponseSuccess().setInfo("登录成功").setData(redirectUrl);
			}
		} catch (UnknownAccountException ex) {//用户名没有找到。
			return this.ajaxResponseError("手机号或密码错误");
		} catch (IncorrectCredentialsException ex) {//用户名密码不匹配。
			return this.ajaxResponseError("手机号或密码错误");
		}catch (AuthenticationException e) {//其他的登录错误
			return this.ajaxResponseError("手机号或密码错误");
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

		return new ModelAndView("views/personal/bindEmailSuccess",model);
	}

	/**
	 * 方法描述：忘记密码（发送验证码）
	 * 作        者：MaoSF
	 * 日        期：2015年12月16日-下午3:15:03
	 * @return
	 */
	@RequestMapping(value = "/sendSecurityCodeAndValidateCellphone", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage sendSecurityCodeAndValidateCellphone(@RequestBody Map<String,Object> paraMap) throws Exception{
		if(null == accountService.getAccountDtoByCellphoneOrEmail(paraMap.get("cellphone").toString())){
			return this.ajaxResponseError("手机号不存在");
		}else{
			return this.sendSecurityCode(paraMap);
		}
	}

	@RequestMapping("/fromApp")
	public String fromApp() throws Exception {
		return "work/views/fromApp/formApp";
	}
	/**
	 * 方法描述：appUse
	 * 作        者：TangY
	 * 日        期：2016年7月11日-下午5:58:40
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={ "saveAppUse", },method=RequestMethod.POST)
	@ResponseBody
	public AjaxMessage saveOrupdateAppUse(@RequestBody AppUseDTO dto) throws Exception{
		return (AjaxMessage) userService.saveOrUpdateAppUse(dto);
	}
	@RequestMapping("/saveAppUseSuccess")
	public String saveAppUseSuccess() throws Exception {
		return "work/views/fromApp/formAppSuccess";
	}


	@RequestMapping("file/downloadPersonalDoc")
	public void downloadPersonalDoc(HttpServletResponse response) throws UnsupportedEncodingException {
		String fileName = "卯丁个人操作手册V1.0.pdf";
		//获取网站部署路径(通过ServletContext对象)，用于确定下载文件位置，从而实现下载

		uploadService.downLoadFile(response,fileName,helpDocUrl);

	}
	@RequestMapping("file/downloadManaDoc")
	public void downloadManaDoc(HttpServletResponse response) throws UnsupportedEncodingException {
		String fileName = "卯丁管理员使用手册V1.0.pdf";
		//获取网站部署路径(通过ServletContext对象)，用于确定下载文件位置，从而实现下载
		uploadService.downLoadFile(response,fileName,helpDocUrl);

	}

	/********************************一下方法数据迁移（请勿调用）******************************************/
	@RequestMapping(value = "/initCompanyRole", method = RequestMethod.POST)
	@ResponseBody
	public void initCompanyRole() throws Exception{
		this.companyService.initCompanyRole();
	}

//    @RequestMapping(value = "/initTaskManageProcess", method = RequestMethod.POST)
//    @ResponseBody
//    public void initTaskManageProcess() throws Exception{
//        this.projectTaskManagerService.initTaskManageProcess();
//    }

	@RequestMapping(value = "/initRolePermission", method = RequestMethod.POST)
	@ResponseBody
	public void initRolePermission() throws Exception{
		this.companyService.initRolePermission();
	}

	@RequestMapping(value = "/initRoleUserPermission", method = RequestMethod.POST)
	@ResponseBody
	public void initRoleUserPermission() throws Exception{
		this.roleUserService.initRoleUserPermission();
	}

	/*************************************end**********************************************************/

	@RequestMapping(value = "/createImg", method = RequestMethod.POST)
	@ResponseBody
	public void createImg() throws Exception{
		this.uploadService.createQrcode("https://www.baidu.com","company/");
	}


	/**
	 * 方法描述：获取当前用户的登录信息
	 * 作        者：TangY
	 * 日        期：2016年7月7日-上午11:52:49
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/getCurrUserOfWork", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage getCurrUserOfWork()throws Exception{
		Map<String,Object> model=new HashMap<String,Object>();
		try {
			Map<String, Object> resultMap = systemService.getCurrUserOfWork(model, this.getSession());
			if (resultMap != null) {
				return this.ajaxResponseSuccess().setData(resultMap);
			} else {
				SecurityUtils.getSubject().logout();
				return ajaxResponseError("需要重新登录");
			}
		} catch (Exception e){
			SecurityUtils.getSubject().logout();
			return ajaxResponseError("需要重新登录");
		}
		
	}

	//======================================================admin============================================================
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
	@RequestMapping(value = "/forgetAdminLoginPwd/{companyId}/{userId}", method = RequestMethod.GET)
	public String forgetAdminLoginPwd(@PathVariable String companyId,@PathVariable String userId, ModelMap model) {
		CompanyEntity companyEntity = companyService.selectById(companyId);
		model.addAttribute("companyName",companyEntity.getCompanyName());
		model.addAttribute("userId",userId);
		return "views/admin/forgetAdminLoginPwd";
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

		return "views/home/shareRegister";
	}
	/**
	 * 个人信息分享界面
	 * @return
	 */
	@RequestMapping(value = "/sharePersonalInfo/{companyId}/{userId}", method = RequestMethod.GET)
	public String sharePersonalInfo(@PathVariable("companyId") String companyId,@PathVariable("userId") String userId,ModelMap model) throws Exception {
		CompanyDTO companyInfo = companyService.getCompanyById(companyId);
		CompanyUserLiteDTO userInfo = companyUserService.getCompanyUserLiteDTO(userId,companyId);
		model.addAttribute("companyInfo",companyInfo);
		model.addAttribute("userInfo",userInfo);
		model.addAttribute("fastdfsUrl",fastdfsUrl);
		model.addAttribute("serverUrl",adminSserverUrl);

		return "views/home/sharePersonalInfo";
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
	@RequestMapping(value = "/registerAdmin", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage registerAdmin(@RequestBody AccountDTO dto) throws Exception{
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
				return this.ajaxResponseError("手机号或密码错误");
			} catch (IncorrectCredentialsException ex) {//用户名密码不匹配。
				return this.ajaxResponseError("手机号或密码错误");
			}catch (AuthenticationException e) {//其他的登录错误
				return this.ajaxResponseError("手机号或密码错误");
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
	@RequestMapping(value = "/loginAdmin/{id}", method = RequestMethod.POST)
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
	@RequestMapping("/logoutAdmin")
	public String logoutAdmin() throws Exception{
		/*this.getSession().removeAttribute("user");
		this.getSession().removeAttribute("userId");
		this.getSession().removeAttribute("companyId");
		this.getSession().removeAttribute("company");
		this.getSession().removeAttribute("roleCompany");
		this.getSession().removeAttribute("orgType");*/
		this.getSession().removeAttribute("adminCompanyId");
		this.getSession().removeAttribute("adminCompany");
		SecurityUtils.getSubject().logout();
		return "views/admin/loginToAdmin";
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

	@RequestMapping(value = "/createNotify", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage createNotify(@RequestParam("days") Integer days) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE,days);
		return notifyService.createNotify(c.getTime());
	}

	@RequestMapping(value = "/notify", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage executeNotify() throws Exception {
		return notifyService.getNotify(currentCompanyId,currentUserId);
	}

	@RequestMapping(value = "/completeNotify", method = RequestMethod.GET)
	@ResponseBody
	public AjaxMessage completeNotify() throws Exception {
		return notifyService.completeNotify(currentCompanyId);
	}
}
