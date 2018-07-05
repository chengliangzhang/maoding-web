package com.maoding.admin.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.system.service.SystemService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：HomeController
 * 类描述：admin首页controller
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午11:50:16
 */
@Controller
@RequestMapping("/iAdmin/home")
public class AdminHomeController extends BaseController{
	
	@Autowired
	private SystemService systemService;


	@RequestMapping("/adminbench")
	public String adminbench(ModelMap model) throws Exception {
		//获取当前用户基本信息
		systemService.getCurrUserInfoOfAdmin(model, this.getSession());
		return "views/admin/adminbench";
	}
	@RequestMapping(value = "/toHelp/{id}", method = RequestMethod.GET)
	public String toHelp(ModelMap model,@PathVariable("id") String id) throws Exception{
		model.put("menuId",id);
		systemService.getCurrUserInfoOfWork(model,this.getSession());
		return "admin/views/adminHelp/adminNewcomersInstruction";
	}


}
