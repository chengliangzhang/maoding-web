package com.maoding.dynamicForm.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.dto.SaveDynamicAuditDTO;
import com.maoding.dynamicForm.service.DynamicFormFieldValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 使用动态表单 处理审批单
 */
@Controller
@RequestMapping("iWork/dynamicAudit")
public class DynamicAuditController extends BaseController {

    @Autowired
    DynamicFormFieldValueService dynamicFormFieldValueService;


    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }

    /**
     * 作者：FYT
     * 日期：2018/9/13
     * 描述：保存审核表单内容
     * 接口：iWork/dynamicForm/saveAuditDetail
     * 参数：SaveDynamicFormDTO
     */
    @RequestMapping(value = "/saveDynamicAudit", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveAuditDetail(@RequestBody SaveDynamicAuditDTO dto) throws Exception{
        updateCurrentUserInfo(dto);
        return AjaxMessage.succeed(dynamicFormFieldValueService.saveAuditDetail(dto));
    }

    /**
     * 作者：MaoSF
     * 日期：2018/9/13
     * 描述：保存审核表单内容
     * 接口：iWork/dynamicForm/initDynamicAudit
     * 参数：SaveDynamicFormDTO
     */
    @RequestMapping(value = "/initDynamicAudit", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage initDynamicAudit(@RequestBody FormFieldQueryDTO dto) throws Exception{
        updateCurrentUserInfo(dto);
        return AjaxMessage.succeed(dynamicFormFieldValueService.initDynamicAudit(dto));
    }

}
