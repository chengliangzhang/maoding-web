package com.maoding.process.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.process.dto.ProcessEditDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/iWork/process")
public class ProcessController extends BaseController{

    @Autowired
    private ProcessService processService;


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
        return AjaxMessage.succeed(processService.getProcessByCompany(query));
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
        int data = processService.saveProcess(dto);
        if(data>0){
            return AjaxMessage.succeed("操作成功");
        }
        return AjaxMessage.error("操作失败");
    }

}
