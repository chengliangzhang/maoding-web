package com.maoding.workflow.controller;

import com.maoding.activiti.dto.DeploymentDTO;
import com.maoding.activiti.dto.DeploymentEditDTO;
import com.maoding.activiti.dto.WorkActionDTO;
import com.maoding.activiti.dto.WorkTaskDTO;
import com.maoding.activiti.service.WorkflowService;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.base.dto.CoreEditDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/31
 * @description : 工作流接口
 */
@Controller
@RequestMapping("/iWork/workflow")
public class WorkFlowController extends BaseController {
    /** 工作流服务接口 */
    @Autowired
    private WorkflowService workflowService;

    /**
     * 描述       创建流程
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/createDeployment")
    public AjaxMessage createDeployment(@RequestBody DeploymentEditDTO deploymentEditRequest) throws Exception {
        updateCurrentEditRequest(deploymentEditRequest);
        DeploymentDTO deployment = workflowService.changeDeployment(deploymentEditRequest);
        return AjaxMessage.succeed("创建成功").setData(deployment);
    }

    /**
     * 描述       更改数字条件
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/changeDigitCondition")
    public AjaxMessage changeDigitCondition(@RequestBody DeploymentEditDTO deploymentEditRequest) throws Exception {
        updateCurrentEditRequest(deploymentEditRequest);
        DeploymentDTO deployment = workflowService.changeDeployment(deploymentEditRequest);
        return AjaxMessage.succeed("修改成功").setData(deployment);
    }
    
    /**
     * 描述       更改审批流程任务节点
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/changeFlowTask")
    public AjaxMessage changeFlowTask(@RequestBody DeploymentEditDTO deploymentEditRequest) throws Exception {
        updateCurrentEditRequest(deploymentEditRequest);
        DeploymentDTO deployment = workflowService.changeDeployment(deploymentEditRequest);
        return AjaxMessage.succeed("修改成功").setData(deployment);
    }

    /**
     * 描述       删除流程
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/deleteDeployment")
    public AjaxMessage deleteDeployment(@RequestBody CoreEditDTO deleteRequest) throws Exception {
        updateCurrentEditRequest(deleteRequest);
        workflowService.deleteDeploy(deleteRequest);
        return AjaxMessage.succeed("删除成功");
    }

    /**
     * 描述       启动流程，id应设置为流程编号
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/startDeployment")
    public AjaxMessage startDeployment(@RequestBody WorkActionDTO startRequest) throws Exception {
        updateCurrentEditRequest(startRequest);
        WorkTaskDTO workTask = workflowService.startDeployment(startRequest);
        return AjaxMessage.succeed("删除成功").setData(workTask);
    }

    /**
     * 描述       完成执行任务，id应设置为执行任务编号
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/completeWorkTask")
    public AjaxMessage completeWorkTask(@RequestBody WorkActionDTO completeRequest) throws Exception {
        updateCurrentEditRequest(completeRequest);
        workflowService.completeWorkTask(completeRequest);
        return AjaxMessage.succeed("完成成功");
    }

    //补充当前公司编号、当前用户编号
    private void updateCurrentEditRequest(CoreEditDTO editDTO){
        if (StringUtils.isEmpty(editDTO.getCurrentCompanyId())){
            editDTO.setCurrentCompanyId(currentCompanyId);
        }
        if (StringUtils.isEmpty(editDTO.getAccountId())){
            editDTO.setAccountId(currentUserId);
        }
    }
}
