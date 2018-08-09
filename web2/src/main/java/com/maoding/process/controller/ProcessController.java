package com.maoding.process.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.process.dto.ProcessEditDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.service.BusinessProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/iWork/process")
public class ProcessController extends BaseController{

    @Autowired
    private BusinessProcessService businessProcessService;


    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
    }


    /**
     * 方法描述：获取组织的项目收支流程
     * 作者：MaoSF
     * 日期：2016/11/30
     */
    @RequestMapping(value = "/getProcessByCompany", method = RequestMethod.POST)
    @ResponseBody
   // @RequiresPermissions(value = {RoleConst.ADMIN_NOTICE}, logical = Logical.OR)
    public AjaxMessage getProcessByCompany(@RequestBody QueryProcessDTO query) throws Exception {
        query.setCurrentCompanyId(this.currentCompanyId);
        return AjaxMessage.succeed(businessProcessService.getProcessByCompany(query));
    }

    /**
     * 方法描述：保存收支流程
     * 作者：MaoSF
     * 日期：2016/11/30
     */
    @RequestMapping(value = "/saveProcess", method = RequestMethod.POST)
    @ResponseBody
    // @RequiresPermissions(value = {RoleConst.ADMIN_NOTICE}, logical = Logical.OR)
    public AjaxMessage saveProcess(@RequestBody ProcessEditDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        int data = businessProcessService.saveProcess(dto);
        if(data>0){
            return AjaxMessage.succeed("操作成功");
        }
        return AjaxMessage.error("操作失败");
    }

    /**
     * 方法描述：获取流程的节点信息
     * 作者：MaoSF
     * 日期：2016/11/30
     */
    @RequestMapping(value = "/listProcessNode", method = RequestMethod.POST)
    @ResponseBody
    // @RequiresPermissions(value = {RoleConst.ADMIN_NOTICE}, logical = Logical.OR)
    public AjaxMessage listProcessNode(@RequestBody QueryProcessDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        return AjaxMessage.succeed( businessProcessService.listProcessNode(dto));
    }

    /**
     * 方法描述：删除项目收支流程
     * 作者：MaoSF
     * 日期：2016/11/30
     */
    @RequestMapping(value = "/deleteProcessForProjectPay", method = RequestMethod.POST)
    @ResponseBody
    // @RequiresPermissions(value = {RoleConst.ADMIN_NOTICE}, logical = Logical.OR)
    public AjaxMessage deleteProcessForProjectPay(@RequestBody ProcessEditDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        int data = businessProcessService.deleteProcessForProjectPay(dto);
        if(data>0){
            return AjaxMessage.succeed("操作成功");
        }
        return AjaxMessage.error("操作失败");
    }

    /**
     * 方法描述：选择，取消流程
     * 作者：MaoSF
     * 日期：2016/11/30
     */
    @RequestMapping(value = "/selectedProcessForProjectPay", method = RequestMethod.POST)
    @ResponseBody
    // @RequiresPermissions(value = {RoleConst.ADMIN_NOTICE}, logical = Logical.OR)
    public AjaxMessage selectedProcessForProjectPay(@RequestBody ProcessEditDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        int data = businessProcessService.selectedProcessForProjectPay(dto);
        if(data>0){
            return AjaxMessage.succeed("操作成功");
        }
        return AjaxMessage.error("操作失败");
    }

    /**
     * 方法描述：选择，取消流程节点中的状态
     * 作者：MaoSF
     * 日期：2016/11/30
     */
    @RequestMapping(value = "/selectedProcessNodeStatus", method = RequestMethod.POST)
    @ResponseBody
    // @RequiresPermissions(value = {RoleConst.ADMIN_NOTICE}, logical = Logical.OR)
    public AjaxMessage selectedProcessNodeStatus(@RequestBody ProcessEditDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        int data = businessProcessService.selectedProcessNodeStatus(dto);
        if(data>0){
            return AjaxMessage.succeed("操作成功");
        }
        return AjaxMessage.error("操作失败");
    }

}
