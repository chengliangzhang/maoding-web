package com.maoding.admin.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtil;
import com.maoding.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/iAdmin/personal")
public class AdminUserController extends  BaseController{

	@Value("${person}")
	private String personUrl;	
	
	@Value("${apache.url}")
	private String apacheUrl;

	@Autowired
    public UserService userService;


	private final String th1="431X272";//头像缩略图大小
	private final String th2="180X240";//头像缩略图大小
	private final String th3="245X155";

	@ModelAttribute
	public void before() throws Exception{
		this.currentUserId = getFromSession("userId",String.class);
		this.currentCompanyId = getFromSession("adminCompanyId",String.class);
		if(StringUtil.isNullOrEmpty(this.currentUserId) || StringUtil.isNullOrEmpty(this.currentCompanyId)){
			throw new LoginException();
		}
	}

	@RequestMapping("index")
	public String test(){
		return "index";
	}
	
	/**
	 * 方法描述：头像上传原图
	 * 作        者：MaoSF
	 * 日        期：2016年7月13日-下午7:56:32
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadHeadOriginalImg",method=RequestMethod.POST)
	@ResponseBody
	public AjaxMessage uploadHeadOriginalImg(MultipartFile file) throws Exception{
		Map<String,String> param = new HashMap<String, String>();
		param.put("fastdfsUrl", fastdfsUrl);
		param.put("th1", th1);
		return userService.uploadHeadOriginalImg(file, param);
	}
	
	/**
	 * 方法描述：上传头像
	 * 作        者：MaoSF
	 * 日        期：2016年7月13日-下午6:57:39
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/uploadHeadImg",method=RequestMethod.POST)
	@ResponseBody
	public Object uploadHeadImg(@RequestBody Map<String,Object> param) throws Exception{
		param.put("userId",  this.currentUserId);
		param.put("th1",  th1);
		return  userService.uploadHeadImg(param);
	}




}
