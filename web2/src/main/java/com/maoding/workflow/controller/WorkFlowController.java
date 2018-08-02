package com.maoding.workflow.controller;

import com.maoding.activiti.dto.*;
import com.maoding.activiti.service.WorkflowService;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.base.dto.CoreEditDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     *           根据companyId,key,type生成流程key
     *           不判断流程是否已存在
     *           如果srcDeployId不为空，从指定的流程模板进行复制，
     *              复制流程时，不判断流程模板和新流程是否为相同类型
     *           不根据流程类型更改组、人员设置
     *           如果新流程是条件流程，且taskListMap包含defaultFlow之外的值，从中获取节点信息和用户任务信息
     *           如果新流程不是条件流程，taskListMap内非defaultFlow的值无效
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/createDeployment")
    @ResponseBody
    @Deprecated
    public AjaxMessage createDeployment(@RequestBody DeploymentEditDTO deploymentEditRequest) throws Exception {
        updateCurrentEditRequest(deploymentEditRequest);
        DeploymentDTO deployment = workflowService.changeDeployment(deploymentEditRequest);
        return AjaxMessage.succeed("创建成功").setData(deployment);
    }

    /**
     * 描述       加载流程进行编辑
     *           根据companyId,key,type生成流程key，查找指定流程
     *           找到则加载此流程，未找到则创建新流程
     *           如果找到流程，加载流程时，srcDeployId，taskListMap无效
     * 日期       2018/8/2
     * @author   张成亮
     **/
    @RequestMapping("/prepareDeployment")
    @ResponseBody
    public AjaxMessage prepareDeployment(@RequestBody DeploymentPrepareDTO deploymentPrepareRequest) throws Exception {
        updateCurrentEditRequest(deploymentPrepareRequest);
        DeploymentDTO deployment = workflowService.prepareDeployment(deploymentPrepareRequest);
        return AjaxMessage.succeed("加载成功").setData(deployment);
    }

    /**
     * 描述       更改数字条件
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/changeDigitCondition")
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
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
