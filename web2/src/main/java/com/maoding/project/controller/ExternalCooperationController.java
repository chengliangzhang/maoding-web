package com.maoding.project.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.dto.PartnerQueryDTO;
import com.maoding.org.service.OrganizationService;
import com.maoding.project.dto.ProjectPartnerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 外部组织
 * Created by wrb on 2017/5/9.
 */
@Controller
@RequestMapping("/iWork/cooperation")
public class ExternalCooperationController extends BaseController {

    @Autowired
    private OrganizationService organizationService;

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }

    /**
     * 方法描述：外部合组织列表
     * 作者：wrb
     * 日期：2017/5/9
     * @param:
     * @return:
     */
    @RequestMapping(value = "/getProjectPartnerList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectPartnerList(@RequestBody PartnerQueryDTO dto) throws Exception {

        List<ProjectPartnerDTO> list = organizationService.getProjectPartnerList(dto);
        return this.ajaxResponseSuccess().setData(list);
    }

    /**
     * 方法描述：解除关系
     * 作者：wrb
     * 日期：2017/5/9
     * @param:
     * @return:
     */
    @RequestMapping(value = "/relieveRelationship/{id}",method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage relieveRelationship(@PathVariable String id) throws Exception{

        return organizationService.relieveRelationship(id);
    }

    /**
     * 方法描述：重新发送短信通知
     * 作者：wrb
     * 日期：2017/5/9
     * @param:
     * @return:
     */
    @RequestMapping(value = "/resendSMS/{id}",method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage resendSMS(@PathVariable String id) throws Exception{

        return organizationService.resendSMS(id,this.currentUserId,this.currentCompanyId);
    }


}
