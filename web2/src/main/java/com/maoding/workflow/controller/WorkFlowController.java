package com.maoding.workflow.controller;

import com.maoding.activiti.dto.*;
import com.maoding.activiti.service.WorkflowService;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.base.dto.CoreEditDTO;
import com.maoding.core.base.dto.CoreQueryDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

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
        updateCurrentUserInfo(deploymentPrepareRequest);
        DeploymentDTO deployment = workflowService.prepareDeployment(deploymentPrepareRequest);
        return AjaxMessage.succeed("加载成功").setData(deployment);
    }

    /**
     * 描述       列出全部流程
     * 日期       2018/8/2
     * @author   张成亮
     **/
    @RequestMapping("/listDeployment")
    @ResponseBody
    public AjaxMessage listDeployment(@RequestBody DeploymentQueryDTO query) throws Exception {
        updateCurrentUserInfo(query);
        Map<String,List<DeploymentSimpleDTO>> result = workflowService.listDeploymentWithKey(query);
        return AjaxMessage.succeed("查询成功").setData(result);
    }

    /**
     * 描述       更改审批流程任务节点
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/changeFlowTask")
    @ResponseBody
    public AjaxMessage changeFlowTask(@RequestBody DeploymentEditDTO deploymentEditRequest) throws Exception {
        updateCurrentUserInfo(deploymentEditRequest);
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
    public AjaxMessage deleteDeployment(@RequestBody DeploymentQueryDTO deleteRequest) throws Exception {
        updateCurrentUserInfo(deleteRequest);
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
        updateCurrentUserInfo(startRequest);
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
        updateCurrentUserInfo(completeRequest);
        workflowService.completeWorkTask(completeRequest);
        return AjaxMessage.succeed("完成成功");
    }

    //补充当前公司编号、当前用户编号
    private void updateCurrentUserInfo(CoreEditDTO editDTO){
        if (StringUtils.isEmpty(editDTO.getCurrentCompanyId())){
            editDTO.setCurrentCompanyId(currentCompanyId);
        }
        if (StringUtils.isEmpty(editDTO.getAccountId())){
            editDTO.setAccountId(currentUserId);
        }
    }

    private void updateCurrentUserInfo(CoreQueryDTO query){
        if (StringUtils.isEmpty(query.getCurrentCompanyId())){
            query.setCurrentCompanyId(currentCompanyId);
        }
        if (StringUtils.isEmpty(query.getAccountId())){
            query.setAccountId(currentUserId);
        }
    }
}
