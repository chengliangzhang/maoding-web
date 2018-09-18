package com.maoding.dynamicForm.controller;

import com.maoding.commonModule.dto.SaveAuditCopyDTO;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.dynamicForm.dto.*;
import com.maoding.dynamicForm.service.DynamicFormFieldValueService;
import com.maoding.dynamicForm.service.DynamicFormGroupService;
import com.maoding.dynamicForm.service.DynamicFormService;
import com.maoding.process.service.ProcessService;
import com.maoding.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    DynamicFormGroupService dynamicFormGroupService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private ProcessService processService;


    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }

    /**
     * 作者：FYT
     * 日期：2018/9/13
     * 描述：保存审核表单模板
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/insertDynamicForm", method = RequestMethod.POST)
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
    @RequestMapping(value = "/saveAuditDetail", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveAuditDetail(@RequestBody SaveDynamicAuditDTO dto) throws Exception{
        updateCurrentUserInfo(dto);
        return AjaxMessage.succeed(dynamicFormFieldValueService.saveAuditDetail(dto));
    }

    /**
     * 作者：FYT
     * 日期：2018/9/14
     * 描述：是否启用动态表单 Status： 1启用，0禁用
     * 参数：（id(processType_Id)）
     * 接口：iWork/dynamicForm/updateStatusDynamicForm
     */
    @RequestMapping(value = "/updateStatusDynamicForm", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage updateStatusDynamicForm(@RequestBody SaveDynamicFormDTO dto) throws Exception{
        return AjaxMessage.succeed(processTypeService.updateStatusDynamicForm(dto));
    }

    /**
     * 作者：FYT
     * 日期：2018/9/14
     * 描述：动态表单模板 删除
     * 参数：（id(processType_Id)）
     * 接口：iWork/dynamicForm/deleteDynamicForm
     */
    @RequestMapping(value = "/deleteDynamicForm", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage deleteDynamicForm (@RequestBody SaveDynamicFormDTO dto) throws  Exception{
        updateCurrentUserInfo(dto);
        return AjaxMessage.succeed(processTypeService.deleteDynamicForm(dto));
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

    /**
     * 描述       准备用于编辑的动态表单，包括可以使用的控件等信息
     * 日期       2018/9/18
     * @author   张成亮
     **/
    @RequestMapping(value = "/prepareFormToEdit", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage prepareFormToEdit(@RequestBody FormEditDTO request) throws Exception {
        updateCurrentUserInfo(request);
        FormWithOptionalDTO detail = dynamicFormService.prepareFormToEdit(request);
        return AjaxMessage.succeed(detail);
    }


    /**
     * 作者：FYT
     * 日期：2018/9/17
     * 描述：后台管理-审批管理-操作，seq排序对调(交换seq值) 和
     * 接口：iWork/dynamicForm/setDynamicFormSeq
     * 参数：FormGroupEditDTO  对调（dynamicFromId1，seq1，dynamicFromId2，seq2）
     */
    @RequestMapping(value = "/setDynamicFormSeq", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage setDynamicFormSeq(@RequestBody FormGroupEditDTO dto) throws Exception {

        return AjaxMessage.succeed(dynamicFormService.setDynamicFormSeq(dto));
    }


    /**
     * 添加知会人
     */
    @RequestMapping(value = "/saveAuditCopy", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveAuditCopy(@RequestBody SaveAuditCopyDTO dto) throws Exception {
        return AjaxMessage.succeed(processTypeService.saveAuditCopy(dto));
    }

}
