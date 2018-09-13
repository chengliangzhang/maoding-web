package com.maoding.dynamicForm.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.dynamicForm.dto.SaveDynamicAuditDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.dynamicForm.service.DynamicFormFieldValueService;
import com.maoding.dynamicForm.service.DynamicFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 作者：FYT
 * 日期：2018/9/13
 */
@Controller
@RequestMapping("iWork/dynamicForm")
public class DynamicFormController extends BaseController {

    @Autowired
    DynamicFormService dynamicFormService;
    @Autowired
    DynamicFormFieldValueService dynamicFormFieldValueService;


    /**
     * 作者：FYT
     * 日期：2018/9/13
     * 描述：保存审核表单模板
     * @return
     * @throws Exception
     */
    @RequestMapping("/insertDynamicForm")
    @ResponseBody
    public AjaxMessage insertDynamicForm(@RequestBody SaveDynamicFormDTO dto) throws Exception{
        updateCurrentUserInfo(dto);
        return AjaxMessage.succeed(dynamicFormService.insertDynamicForm(dto));
    }

    /**
     * 作者：FYT
     * 日期：2018/9/13
     * 描述：保存审核表单内容
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveAuditDetail")
    @ResponseBody
    public AjaxMessage saveAuditDetail(@RequestBody SaveDynamicAuditDTO dto) throws Exception{
        return AjaxMessage.succeed(dynamicFormFieldValueService.saveAuditDetail(dto));
    }
}
