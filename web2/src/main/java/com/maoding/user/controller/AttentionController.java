package com.maoding.user.controller;

import com.maoding.core.base.controller.BaseController;

import com.maoding.core.bean.AjaxMessage;
import com.maoding.user.dto.AttentionDTO;
import com.maoding.user.service.AttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 方法描述：关注
 * 作   者：wrb
 * 日   期：2017年01月06日-下午17:29:00
 * @param param
 * @return
 * @throws Exception
 */
@Controller
@RequestMapping("/iWork/attention")
public class AttentionController extends  BaseController{


	@Autowired
	private AttentionService attentionService;


	@ModelAttribute
	public void before(){
		this.currentUserId = this.getFromSession("userId",String.class);
		this.currentCompanyId =this.getFromSession("companyId",String.class);
	}

	/**
	 * 方法描述：添加项目关注记录
	 * 作   者：wrb
	 * 日   期：2017年01月06日-下午17:29:00
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/attention",method=RequestMethod.POST)
	@ResponseBody
	public AjaxMessage addAttention(@RequestBody AttentionDTO dto) throws Exception{
		if(null==dto.getCompanyId() || "".equals(dto.getCompanyId())){
			dto.setCompanyId(this.currentCompanyId);
		}
		dto.setCreateBy(this.currentUserId);
		return attentionService.addAttention(dto);
	}

	/**
	 * 方法描述：添加项目关注记录
	 * 作   者：wrb
	 * 日   期：2017年01月06日-下午17:29:00
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/attention/{id}",method=RequestMethod.GET)
	@ResponseBody
	public AjaxMessage delAttention(@PathVariable String id) throws Exception{
		return attentionService.delAttention(id);
	}

}
