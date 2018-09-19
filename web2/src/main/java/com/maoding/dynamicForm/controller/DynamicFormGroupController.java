package com.maoding.dynamicForm.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.dynamicForm.dto.FormGroupEditDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.dynamicForm.service.DynamicFormGroupService;
import com.maoding.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("iWork/dynamicFormGroup")
public class DynamicFormGroupController  extends BaseController {

    @Autowired
    private DynamicFormGroupService dynamicFormGroupService;

    @Autowired
    private ProcessTypeService processTypeService;


    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }



    /**
     * 描述     添加审批群组
     *          由于原有审批管理的查询接口在此类内实现，因此保留一个接口，保持一致性
     *          但根据目前的设计，审批信息使用动态表单表来存储，因此在动态表单服务内进行具体实现，并在动态表单内也提供相同功能的接口
     * 日期     2018/9/14
     * @author  张成亮
     * @return  新创建的群组
     * @param   request 添加群组请求
     *                  name 群组名称
     **/
    @RequestMapping(value = "/saveDynamicFormGroup", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage addProcessGroup(@RequestBody FormGroupEditDTO request) throws Exception {
        updateCurrentUserInfo(request);
        dynamicFormGroupService.saveDynamicFormGroup(request);
        return AjaxMessage.succeed(null);
    }

    /**
     * 删除分组
     */
    @RequestMapping(value = "/deleteDynamicFormGroup", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage deleteDynamicFormGroup(@RequestBody FormGroupEditDTO request) throws Exception {
        updateCurrentUserInfo(request);
        dynamicFormGroupService.deleteDynamicFormGroup(request);
        return AjaxMessage.succeed(null);
    }

    /**
     * 作者：FYT
     * 日期：2018/9/19
     * 描述：后台管理-审批管理-操作-查询当前公司的分组（查询现有分组，返回list）ss
     * 接口：iWork/dynamicForm/selectDynamicFormGroupList
     * 参数：
     */
    @RequestMapping(value = "/selectDynamicFormGroupList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage selectDynamicFormGroupList(@RequestBody FormGroupEditDTO dto) throws Exception {
        updateCurrentUserInfo(dto);
        return AjaxMessage.succeed(dynamicFormGroupService.listFormGroupByCompanyId(dto));
    }

    /**
     * 作者：FYT
     * 日期：2018/9/19
     * 描述：后台管理-审批管理-操作-动态表移动到其他分组
     * 接口：iWork/dynamicForm/updateProcessTypeFormType
     * 参数：FormGroupEditDTO  动态表单id,分组formType
     */
    @RequestMapping(value = "/updateProcessTypeFormType", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage updateProcessTypeFormType(@RequestBody SaveDynamicFormDTO dto) throws Exception {
        return AjaxMessage.succeed(processTypeService.updateProcessTypeFormType(dto));
    }
}
