package com.maoding.workflow.controller;

import com.maoding.activiti.dto.DeploymentEditDTO;
import com.maoding.activiti.dto.WorkActionDTO;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.base.dto.CoreEditDTO;
import com.maoding.core.bean.AjaxMessage;
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
    /**
     * 描述       创建流程
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/createDeployment")
    public AjaxMessage createDeployment(@RequestBody DeploymentEditDTO deploymentEditRequest) throws Exception {
        return null;
    }

    /**
     * 描述       更改数字条件
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/changeDigitCondition")
    public AjaxMessage changeDigitCondition(@RequestBody DeploymentEditDTO deploymentEditRequest) throws Exception {
        return null;
    }
    
    /**
     * 描述       更改审批流程任务节点
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/changeFlowTask")
    public AjaxMessage changeFlowTask(@RequestBody DeploymentEditDTO deploymentEditRequest) throws Exception {
        return null;
    }

    /**
     * 描述       删除流程
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/deleteDeployment")
    public AjaxMessage deleteDeployment(@RequestBody CoreEditDTO deleteRequest) throws Exception {
        return null;
    }

    /**
     * 描述       启动流程，id应设置为流程编号
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/startDeployment")
    public AjaxMessage startDeployment(@RequestBody WorkActionDTO startRequest) throws Exception {
        return null;
    }

    /**
     * 描述       完成执行任务，id应设置为执行任务编号
     * 日期       2018/7/31
     * @author   张成亮
     **/
    @RequestMapping("/completeWorkTask")
    public AjaxMessage completeWorkTask(@RequestBody WorkActionDTO completeRequest) throws Exception {
        return null;
    }
}
