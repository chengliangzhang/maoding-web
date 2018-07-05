package com.maoding.system.controller;


import com.maoding.core.base.controller.BaseController;
import com.maoding.core.base.dto.BasePageDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.system.dto.UpdateDTO;
import com.maoding.system.dto.UpdateQueryDTO;
import com.maoding.system.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：WelcomeController
 * 类描述：系统公共controller
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午11:50:16
 */
@Controller
@RequestMapping("/")
public class WelcomeController extends BaseController{
	@Autowired
	VersionService versionService;

	@RequestMapping("/")
	public String workLogin() {
		return "views/login/login";
	}



	/** 系统升级日志界面 */
	@RequestMapping("/logUpdatePage")
	public String logUpdatePage(){

		return "views/log/logUpdateList";
	}
	/** 系统升级日志 */
	@RequestMapping(value = "/listUpdateHistory", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage listUpdateHistory(@RequestBody UpdateQueryDTO query){
		BasePageDTO<UpdateDTO> result = versionService.listUpdateHistory(query);
		return AjaxMessage.succeed(result);
	}

}
