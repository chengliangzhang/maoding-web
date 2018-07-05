package com.maoding.user.controller;

import com.maoding.core.annotation.isLogin;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.component.mail.MailSender;
import com.maoding.core.component.mail.bean.Mail;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.MD5Helper;
import com.maoding.core.util.SecurityCodeUtil;
import com.maoding.core.util.StringUtil;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.service.CompanyUserService;
import com.maoding.system.service.DataDictionaryService;
import com.maoding.system.service.SystemService;
import com.maoding.user.dto.*;
import com.maoding.user.entity.AccountEntity;
import com.maoding.user.service.AccountService;
import com.maoding.user.service.UserAttachService;
import com.maoding.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/iWork/personal")
public class UserController extends BaseController {


    private final String th1 = "431X272";//头像缩略图大小
    private final String th2 = "180X240";//头像缩略图大小
    private final String th3 = "245X155";
    @Autowired
    public UserAttachService userAttachService;
    @Autowired
    public UserService userService;
    @Autowired
    public AccountService accountService;
    @Autowired
    public SystemService systemService;
    @Value("${person}")
    private String personUrl;
    @Value("${webscoket.url}")
    private String scoketUrl;
    @Autowired
    private MailSender mailSender;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 个人中心(v2.0)
     */
    @RequestMapping("/center")
    public String center(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        model.addAttribute("fastdfsUrl", fastdfsUrl);
        model.addAttribute("forwardType", model.getOrDefault("forwardType", null));
        return "views/personal/personalCenter";
    }

    /**
     * 个人中心(v2.0)
     */
    @RequestMapping("/center/{forwordType}")
    public String centerBy(RedirectAttributes model, @PathVariable String forwordType) throws Exception {
        String dataAction;
        switch (forwordType) {
            case "1":
                dataAction = "account";
                break;
            case "2":
                dataAction = "safety";
                break;
            case "3":
                dataAction = "message";
                break;
            default:
                dataAction = "account";
                break;
        }
        model.addFlashAttribute("forwardType", dataAction);
        return "redirect:/iWork/personal/center";
    }

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
    }

    /**
     * 方法描述：头像上传原图
     * 作        者：MaoSF
     * 日        期：2016年7月13日-下午7:56:32
     */
    @RequestMapping(value = "/uploadHeadOriginalImg", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage uploadHeadOriginalImg(MultipartFile file) throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("fastdfsUrl", fastdfsUrl);
        param.put("personUrl", personUrl);
        param.put("th1", th1);
        return userService.uploadHeadOriginalImg(file, param);
    }

    /**
     * 方法描述：保存头像信息
     * 作        者：TangY
     * 日        期：2016年7月13日-下午7:56:32
     */
    @RequestMapping(value = "/saveOrUpdateUserAttach", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateUserAttach(@RequestBody Map<String, Object> param) throws Exception {
        param.put("userId", this.currentUserId);
        param.put("fastdfsUrl", fastdfsUrl);
        return userService.saveOrUpdateUserAttach(param);
    }


    /**
     * 方法描述：个人中心－获取账号信息
     * 作    者：wangrb
     * 日    期：2016年7月11日-下午9:38:13
     */
    @RequestMapping(value = {"/userInfo", "/userInfo/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    @isLogin
    public AjaxMessage getUserInfo(@PathVariable Map<String, Object> map) throws Exception {
        //userService.get
        //账号信息
        String id = (String) map.get("id");
        if (null == id || "".equals(id)) {
            id = this.currentUserId;
        }
        UserDTO userDto = userService.getUserById(id);
        AccountDTO accountDTO = accountService.getAccountById(id);
        if (null == accountDTO) {
            return new AjaxMessage().setCode("1").setInfo("获取账号信息错误!");
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BaseDTO.copyFields(accountDTO, userInfoDTO);
        userInfoDTO.setPassword(null);
        userInfoDTO.setOldPassword(null);
        if (userDto != null) {
            userInfoDTO.setBirthday(userDto.getBirthday());
            userInfoDTO.setSex(userDto.getSex());
            userInfoDTO.setUserName(userDto.getUserName());
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", accountDTO.getId());
        param.put("attachType", "5");
        List<UserAttachDTO> list = userAttachService.getAttachByTypeToDTO(param);
        if (list != null && list.size() > 0) {
            userInfoDTO.setHeadImg(list.get(0).getFileGroup() + "/" + list.get(0).getAttachPath());
        }
        CompanyDTO companyDTO = this.getFromSession("company", CompanyDTO.class);
        if (companyDTO != null) {
            userInfoDTO.setCompanyName(companyDTO.getCompanyName());
        }
        userInfoDTO.setMajorName(userDto.getMajorName());
        userInfoDTO.setMajor(userDto.getMajor());
        return new AjaxMessage().setCode("0").setData(userInfoDTO);
    }

    /**
     * 方法描述：添加或修改用户信息
     * 作        者：TangY
     * 日        期：2016年7月11日-下午5:58:40
     */
    @RequestMapping(value = {"userInfo", "userInfo/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrupdateUser(@RequestBody AccountDTO dto) throws Exception {
        return (AjaxMessage) userService.saveOrUpdateUser(dto);
    }

    /**
     * 方法描述：上传用户附件   只保存缩略图
     * 作        者：wangrb
     * 日        期：2015年11月17日-下午5:18:09
     * @return (code, msg, UserAttachEntity)
     */
    @RequestMapping(value = "/uploadAttach", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage uploadAttach(@PathVariable("file") MultipartFile file, @PathVariable("type") String type) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("type", type);
        param.put("userId", this.currentUserId);
        param.put("companyId", this.currentCompanyId);
        param.put("personUrl", personUrl);
        return (AjaxMessage) userAttachService.saveUserAttach(file, param);
    }

    /**
     * 方法描述：上传头像
     * 作        者：MaoSF
     * 日        期：2016年7月13日-下午6:57:39
     */
    @RequestMapping(value = "/uploadHeadImg", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadHeadImg(@RequestBody Map<String, Object> param) throws Exception {
        param.put("userId", this.currentUserId);
        param.put("th1", th1);
        return userService.uploadHeadImg(param);
    }

    /**
     * 方法描述：修改密码
     * 作        者：TangY
     * 日        期：2016年7月8日-下午4:11:25
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage changePassword(@RequestBody AccountDTO accountDto) throws Exception {
        accountDto.setId(this.currentUserId);
        accountDto.setPassword(MD5Helper.getMD5For32(accountDto.getPassword()));
        accountDto.setOldPassword(MD5Helper.getMD5For32(accountDto.getOldPassword()));
        return systemService.changePassword(accountDto);
    }


    /**
     * 方法描述：修改手机号
     * 作        者：TangY
     * 日        期：2016年7月8日-下午4:13:09
     */
    @RequestMapping(value = "/changeCellphone", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage changeCellphone(@RequestBody AccountDTO accountDto) throws Exception {
        accountDto.setId(this.currentUserId);
        if (!checkCode(accountDto.getCellphone(), accountDto.getCode())) {
            return ajaxResponseError("短信验证码有误，请重新输入");
        }
        return systemService.changeCellphone(accountDto);
    }

    /**
     * 方法描述：绑定邮箱
     * 作        者：TangY
     * 日        期：2016年7月8日-下午4:15:15
     */
    @RequestMapping(value = "/bindMailbox", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage bindMailbox(@RequestBody AccountDTO accountDto) throws Exception {
        accountDto.setId(this.currentUserId);
        return systemService.bindMailbox(accountDto);
    }

    /**
     * 方法描述：发送邮件
     * 作        者：TangY
     * 日        期：2016年7月8日-下午5:13:58
     */
    @RequestMapping(value = "/sendBindMail", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage sendBindMail(@RequestBody Map<String, Object> map) throws Exception {
        Mail mail = new Mail();
        String toMail = (String) map.get("toMail");
        AccountDTO accountDTO = accountService.getAccountDtoByCellphoneOrEmail(toMail);
        if (accountDTO != null) {
            return ajaxResponseError("此邮箱已被占用");
        }
        String userId = (String) getFromSession("userId");
        AccountEntity user = accountService.selectById(userId);
        String securityCode = SecurityCodeUtil.createSecurityCode();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String dateStr = sdf.format(new Date());
        String bodyStr = "<h4>" + user.getUserName() + "：您好！</h4>";
        bodyStr += "<p style='text-indent:2em;'>您正在进行\"卯丁\"邮箱验证操作，请点击下面链接进行绑定邮箱验证：</p><br/>";
        String url = this.serverUrl + "/iWork/sys/email-activate/verify-mail/" + user.getId() + "/" + toMail + "/" + securityCode;
        bodyStr += "<p style='text-indent:2em;font-size:14px;'><a href='" + url + "'>" + url + "</a></p><br/>";
        bodyStr += "<p style='text-indent:2em;'>如果你没有使用过\"卯丁\"，请忽略此邮件。</p><br/>";

        bodyStr += "<p style='text-indent:2em;'>" + dateStr + "</p><hr/>";
        bodyStr += "<span style='margin-left:100px;position:absolute;margin-top:50px;'>";

        mail.setTo(toMail);
        mail.setSubject("卯丁邮箱验证");
        mail.setHtmlBody(StringUtil.format(bodyStr, user.getId(), toMail, securityCode));
        boolean isOK = mailSender.sendMail(mail);
        //boolean isOK=true;
        if (isOK) {
            user.setEmialCode(toMail + "-" + securityCode);
            accountService.updateById(user);
            return ajaxResponseSuccess("邮件发送成功");
        } else {
            return ajaxResponseError("邮件发送失败");
        }
    }

    /**
     * 方法描述：appUse
     * 作        者：TangY
     * 日        期：2016年7月11日-下午5:58:40
     */
    @RequestMapping(value = {"saveAppUse",}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrupdateAppUse(@RequestBody AppUseDTO dto) throws Exception {
        return (AjaxMessage) userService.saveOrUpdateAppUse(dto);
    }

//	@Autowired
//	private MessageProducer messageProducer;
//
//	/**
//	 * 方法描述：appUse
//	 * 作        者：TangY
//	 * 日        期：2016年7月11日-下午5:58:40
//	 * @param messageMap
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value={ "sendMessage","/sendMessage/{id}", },method=RequestMethod.POST)
//	@ResponseBody
//	public AjaxMessage sendMessage(@RequestBody Map<String,Object> messageMap) throws Exception{
//		CompanyUserEntity companyUserEntity=companyUserService.getCompanyUserByUserIdAndCompanyId(this.currentUserId,this.currentCompanyId);
//		messageMap.put("messSender",companyUserEntity.getId());
//		messageProducer.sendSystemMessage(systemMessageDestination,messageMap);
//		return ajaxResponseSuccess("消息发送成功");
//	}
    //====================================================admin=============================================================


    @RequestMapping("index")
    public String test() {
        return "index";
    }

    /**
     * 方法描述：头像上传原图
     * 作        者：MaoSF
     * 日        期：2016年7月13日-下午7:56:32
     */
    @RequestMapping(value = "/uploadHeadOriginalImgAdmin", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage uploadHeadOriginalImgAdmin(MultipartFile file) throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("fastdfsUrl", fastdfsUrl);
        param.put("th1", th1);
        return userService.uploadHeadOriginalImg(file, param);
    }

    /**
     * 方法描述：上传头像
     * 作        者：MaoSF
     * 日        期：2016年7月13日-下午6:57:39
     */
    @RequestMapping(value = "/uploadHeadImgAdmin", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadHeadImgAdmin(@RequestBody Map<String, Object> param) throws Exception {
        param.put("userId", this.currentUserId);
        param.put("th1", th1);
        return userService.uploadHeadImg(param);
    }

    /**
     * 方法描述：获取专业
     * 作    者 : MaoSF
     * 日    期 : 2017/05/23
     */
    @RequestMapping(value = "/getMajor", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMajor() throws Exception {
        return AjaxMessage.succeed(this.dataDictionaryService.getSubDataByCode(SystemParameters.USER_MAJOR));
    }
}
