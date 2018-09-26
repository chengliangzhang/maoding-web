package com.maoding.dynamicForm.controller;

import com.maoding.commonModule.dto.SaveAuditCopyDTO;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.BeanUtils;
import com.maoding.dynamicForm.dto.*;
import com.maoding.dynamicForm.service.DynamicFormFieldValueService;
import com.maoding.dynamicForm.service.DynamicFormGroupService;
import com.maoding.dynamicForm.service.DynamicFormService;
import com.maoding.financial.dto.QueryAuditDTO;
import com.maoding.process.service.ProcessService;
import com.maoding.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 接口：iWork/dynamicForm/insertDynamicForm
     * 参数：SaveDynamicFormDTO
     */
    @RequestMapping(value = "/saveDynamicForm", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage insertDynamicForm(@RequestBody SaveDynamicFormDTO dto) throws Exception{
        updateCurrentUserInfo(dto);
        return AjaxMessage.succeed(dynamicFormService.insertDynamicForm(dto));
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

    /**
     * 作者：FYT
     * 日期：2018/9/18
     * 描述：后台管理-审批管理-操作，对调分组seq排序对调(交换seq值) （如：行政审批 与 财务审批 位置对调）
     * 接口：iWork/dynamicForm/updateDynamicFormSeq
     * 参数：FormGroupEditDTO  对调（dynamicFromId1，seq1，dynamicFromId2，seq2）
     */
    @RequestMapping(value = "/updateDynamicFormSeq", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage updateDynamicFormSeq(@RequestBody FormGroupEditDTO dto) throws Exception {

        return AjaxMessage.succeed(dynamicFormService.updateDynamicFormSeq(dto));
    }

    /**
     * 描述       获取动态表单列表，以列表或分组形式给出
     *              可代替iWork/workflow/listProcessDefine接口
     * 日期       2018/9/19
     * @author   张成亮
     * @param    query 动态表单列表查询条件
     *              如果未指定accountId，currentCompanyId，则使用当前用户信息
     *              id 动态表单编号，如果不为空则只返回编号相同的表单
     *              useGroup 是否按群组汇总，0-否，1/null-是
     * @return   动态表单列表，或动态表单分组-动态表单列表
     **/
    @RequestMapping(value = "/listForm", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage listForm(@RequestBody FormQueryDTO query) throws Exception {
        updateCurrentUserInfo(query);

        AjaxMessage ajaxMessage = AjaxMessage.succeed("查询成功");
        if (query.getUseGroup() != null && query.getUseGroup() == 0){
            List<FormDTO> formList = dynamicFormService.listForm(query);
            ajaxMessage.setData(formList);
        } else {
            FormGroupQueryDTO groupQuery = BeanUtils.createFrom(query,FormGroupQueryDTO.class);
            groupQuery.setIsIncludeForm(1);
            groupQuery.setNeedCC(1);
            List<FormGroupDTO> groupList = dynamicFormGroupService.listFormGroup(groupQuery);
            ajaxMessage.setData(groupList);
        }
        return ajaxMessage;
    }


    /**
     * 描述       以分组形式给出启用的动态表单模板
     * 日期       2018/9/19
     * @author   张成亮
     * @param    query 动态表单列表查询条件
     *              如果未指定accountId，currentCompanyId，则使用当前用户信息
     * @return   动态表单列表，或动态表单分组-动态表单列表
     **/
    @RequestMapping(value = "/listActiveForm", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage listActiveForm(@RequestBody FormQueryDTO query) throws Exception {
        updateCurrentUserInfo(query);
        FormGroupQueryDTO groupQuery = BeanUtils.createFrom(query,FormGroupQueryDTO.class);
        groupQuery.setIsIncludeForm(1);
        groupQuery.setStatus(1);
        groupQuery.setNeedCC(0);
        groupQuery.setNotIncludeGroupName("项目审批");
        List<FormGroupDTO> groupList = dynamicFormGroupService.listFormGroup(groupQuery);
        return AjaxMessage.succeed(groupList);
    }


    /**
     * 动态表单编辑时，下拉框，选择系统默认的数据，点击按钮查询系统中默认的数据的接口
     * selectType = 1，报销， 2 = 费用，3 = 请假类型
     */
    @RequestMapping(value = "/listSystemDefaultSelect", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage listSystemDefaultSelect(@RequestBody FormFieldQueryDTO query) throws Exception {
        updateCurrentUserInfo(query);
        return AjaxMessage.succeed(dynamicFormService.listSystemDefaultSelect(query));
    }

    /**
     * 动态表单编辑时，审批控件，请求数据
     */
    @RequestMapping(value = "/listAuditType", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage listAuditType(@RequestBody QueryAuditDTO query) throws Exception {
        updateCurrentUserInfo(query);
        return AjaxMessage.succeed(dynamicFormService.listAuditType(query));
    }
}
