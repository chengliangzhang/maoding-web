package com.maoding.dynamicForm.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.dynamicForm.dto.FormDTO;
import com.maoding.dynamicForm.dto.FormQueryDTO;
import com.maoding.dynamicForm.dto.SaveDynamicAuditDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.dynamicForm.service.DynamicFormFieldValueService;
import com.maoding.dynamicForm.service.DynamicFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    /**
     * 作者：FYT
     * 日期：2018/9/14
     * 描述：审批表 启用/停用
     * @return
     * @throws Exception
     */
    @RequestMapping("/startOrStopDynamicForm")
    @ResponseBody
    public AjaxMessage startOrStopDynamicForm(@RequestBody SaveDynamicFormDTO dto) throws Exception{
        return AjaxMessage.succeed(dynamicFormService.startOrStopDynamicForm(dto));
    }

    /**
     * 作者：FYT
     * 日期：2018/9/14
     * 描述：审批表 删除
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteDynamicForm")
    @ResponseBody
    public AjaxMessage deleteDynamicForm (SaveDynamicFormDTO dto) throws  Exception{
        return AjaxMessage.succeed(dynamicFormService.deleteDynamicForm(dto));
    }


    /**
     * 描述     查询动态表单应显示的控件及相应属性
     * 日期     2018/9/13
     * @author  张成亮
     * @return  动态表单内各控件的位置、名称等信息
     * @param   query
     **/
    @RequestMapping(value = "/getFormDetail", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getFormDetail(@RequestBody FormQueryDTO query) throws Exception {
        updateCurrentUserInfo(query);
        FormDTO detail = dynamicFormService.getFormDetail(query);
        return AjaxMessage.succeed(detail);
    }

}
