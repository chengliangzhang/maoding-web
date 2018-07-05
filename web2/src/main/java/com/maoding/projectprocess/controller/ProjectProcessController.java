package com.maoding.projectprocess.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.project.dto.ProjectProcessDTO;
import com.maoding.project.dto.SaveProjectProcessDTO;
import com.maoding.project.service.ProjectProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Created by Wuwq on 2016/10/27.
 */
@Controller
@RequestMapping("iWork/projectProcess")
public class ProjectProcessController extends BaseController {


    @Autowired
    private ProjectProcessService projectProcessService;


    @ModelAttribute
    public void before(){
        this.currentUserId = this.getFromSession("userId",String.class);
        this.currentCompanyId =this.getFromSession("companyId",String.class);
    }

    /**新建或更新流程*/
    @RequestMapping(value={ "/saveOrUpdateProcess"},method= RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateProcess(@RequestBody ProjectProcessDTO dto) throws Exception{
        dto.setCompanyId(this.currentCompanyId);
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        return projectProcessService.saveOrUpdateProcess(dto);
    }

    /**新业务逻辑
     * 新建或更新流程
     */
    @RequestMapping(value={ "/saveOrUpdateProcessNew"},method= RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateProcessNew(@RequestBody ProjectProcessDTO dto) throws Exception{
        dto.setCompanyId(this.currentCompanyId);
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        return projectProcessService.saveOrUpdateProcessNew(dto);
    }

    /**获取指定任务下的流程*/
    @RequestMapping(value={ "/getProcessesByTask/{taskManageId}"},method= RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getProcessesByTask(@PathVariable("taskManageId") String taskManageId) throws Exception{
        AjaxMessage ajaxMessage = projectProcessService.getProcessesByTask(taskManageId);
        return ajaxMessage;
    }

}
