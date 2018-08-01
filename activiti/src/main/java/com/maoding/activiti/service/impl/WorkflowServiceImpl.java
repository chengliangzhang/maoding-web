package com.maoding.activiti.service.impl;

import com.maoding.activiti.dto.*;
import com.maoding.activiti.service.WorkflowService;
import com.maoding.core.base.dto.CoreEditDTO;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.user.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/26
 * @description :
 */
@Service("workflowServiceImpl")
public class WorkflowServiceImpl extends NewBaseService implements WorkflowService {
    /**
     * @param deployment            指定的流程，可以为空
     * @param deploymentEditRequest 包含数字条件、修改任务的流程编辑信息，可以为空
     * @return 流程编辑信息
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取一个流程的编辑器
     **/
    @Deprecated
    @Override
    public DeploymentEditDTO createDeploymentEdit(DeploymentDTO deployment, DeploymentEditDTO deploymentEditRequest) {
        return null;
    }

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     创建或修改一个流程
     * @param   deploymentEditRequest 包含名称、数字条件、修改任务的流程编辑信息，除名称外，各属性可以为空
     *                                如果id为空，则是创建流程，如果srcDeployId不为空，需要从模板流程复制流程到新流程中
     *                                否则，如果id不为空，则是修改流程，srcDeployId无效
     * @return  新建或修改后的流程信息
     **/
    @Override
    public DeploymentDTO changeDeployment(DeploymentEditDTO deploymentEditRequest) {
        return null;
    }

    /**
     * @return 流程查询信息
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取一个流程查询器
     **/
    @Override
    public DeploymentQueryDTO createDeploymentQuery() {
        return null;
    }

    /**
     * @param deploymentEdit 编辑内容
     * @author 张成亮
     * @date 2018/7/30
     * @description 保存流程
     **/
    @Override
    public void saveDeploy(DeploymentEditDTO deploymentEdit) {

    }

    /**
     * @param deploymentEdit 删除内容
     * @author 张成亮
     * @date 2018/7/30
     * @description 删除流程
     **/
    @Deprecated
    @Override
    public void deleteDeploy(DeploymentEditDTO deploymentEdit) {

    }

    /**
     * @param deleteRequest 删除申请
     * @author 张成亮
     * @date 2018/7/30
     * @description 删除流程
     **/
    @Override
    public void deleteDeploy(CoreEditDTO deleteRequest) {

    }

    /**
     * @param query 流程查询器
     * @return 流程列表
     * @author 张成亮
     * @date 2018/7/30
     * @description 查询流程
     **/
    @Override
    public List<DeploymentDTO> listDeployment(DeploymentQueryDTO query) {
        return null;
    }

    /**
     * @param query 流程查询器
     * @return 流程个数
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取流程总个数
     **/
    @Override
    public int countDeployment(DeploymentQueryDTO query) {
        return 0;
    }

    /**
     * @param query 流程查询器
     * @return 流程分页数据
     * @author 张成亮
     * @date 2018/7/30
     * @description 分页获取流程列表
     **/
    @Override
    public CorePageDTO<DeploymentDTO> listPageDeployment(DeploymentQueryDTO query) {
        return null;
    }

    /**
     * @param deploymentEdit 指定的流程编辑器，不能为空
     * @param flowTask       指定的流程任务，可以为空
     * @return 流程任务编辑器
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取一个流程任务的编辑器
     **/
    @Override
    public FlowTaskEditDTO createFlowTaskEdit(DeploymentEditDTO deploymentEdit, FlowTaskDTO flowTask) {
        return null;
    }

    /**
     * @return 流程任务查询信息
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取一个流程任务的查询器
     **/
    @Override
    public FlowTaskQueryDTO createFlowTaskQuery() {
        return null;
    }

    /**
     * @param query 流程任务查询器
     * @return 流程任务列表
     * @author 张成亮
     * @date 2018/7/30
     * @description 查询流程任务
     **/
    @Override
    public List<FlowTaskDTO> listFlowTask(FlowTaskQueryDTO query) {
        return null;
    }

    /**
     * @param deploymentEdit
     * @param sequence       指定的流程路径，可以为空  @return  流程路径编辑器
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取一个流程路径编辑器
     **/
    @Override
    public FlowSequenceEditDTO createFlowSequenceEdit(DeploymentEditDTO deploymentEdit, FlowSequenceDTO sequence) {
        return null;
    }

    /**
     * @return 流程路径查询器
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取一个流程路径查询器
     **/
    @Override
    public FlowSequenceQueryDTO createFlowSequenceQuery() {
        return null;
    }

    /**
     * @param query 流程路径查询器
     * @return 流程路径列表
     * @author 张成亮
     * @date 2018/7/30
     * @description 查询流程路径
     **/
    @Override
    public List<FlowSequenceDTO> listFlowSequence(FlowSequenceQueryDTO query) {
        return null;
    }

    /**
     * @param deploymentEdit
     * @param gateWay        指定的流程网关，可以为空  @return  流程路径编辑器
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取一个流程网关编辑器
     **/
    @Override
    public FlowGateWayEditDTO createFlowGateWayEdit(DeploymentEditDTO deploymentEdit, FlowGateWayDTO gateWay) {
        return null;
    }


   /**
     * @return 流程网关查询器
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取一个流程网关查询器
     **/
    @Override
    public FlowGateWayQueryDTO createFlowGateWayQuery() {
        return null;
    }

    /**
     * @param query 流程网关查询器
     * @return 流程网关列表
     * @author 张成亮
     * @date 2018/7/30
     * @description 查询流程网关
     **/
    @Override
    public List<FlowGateWayDTO> listFlowGateWay(FlowGateWayQueryDTO query) {
        return null;
    }

    /**
     * @return 流程用户查询器
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取一个流程用户查询器
     **/
    @Override
    public UserQueryDTO createUserQuery() {
        return null;
    }

    /**
     * @param query 用户查询器
     * @return 用户列表
     * @author 张成亮
     * @date 2018/7/30
     * @description 查询用户
     **/
    @Override
    public List<UserDTO> listUser(UserQueryDTO query) {
        return null;
    }

    /**
     * @return 当前流程任务查询器
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取一个正在当前流程任务查询器
     **/
    @Override
    public WorkTaskQueryDTO createWorkTaskQuery() {
        return null;
    }

    /**
     * @param query 当前任务查询器
     * @return 当前任务列表
     * @author 张成亮
     * @date 2018/7/30
     * @description 查询当前任务
     **/
    @Override
    public List<WorkTaskDTO> listWorkTask(WorkTaskQueryDTO query) {
        return null;
    }

    /**
     * @param query 当前任务查询器
     * @return 当前任务个数
     * @author 张成亮
     * @date 2018/7/30
     * @description 获取当前任务总个数
     **/
    @Override
    public int countWorkTask(WorkTaskQueryDTO query) {
        return 0;
    }

    /**
     * @param query 当前任务查询器
     * @return 当前任务分页数据
     * @author 张成亮
     * @date 2018/7/30
     * @description 分页获取当前任务列表
     **/
    @Override
    public CorePageDTO<WorkTaskDTO> listPageWorkTask(WorkTaskQueryDTO query) {
        return null;
    }

    /**
     * @param deployment 流程
     * @return 当前流程任务
     * @author 张成亮
     * @date 2018/7/30
     * @description 启动流程
     **/
    @Deprecated
    @Override
    public WorkTaskDTO startDeployment(DeploymentDTO deployment) {
        return null;
    }

    /**
     * 描述       启动流程
     * 日期       2018/8/1
     *
     * @param workTask
     * @author 张成亮
     */
    @Override
    public WorkTaskDTO startDeployment(WorkActionDTO workTask) {
        return null;
    }

    /**
     * @param workTask 当前任务
     * @author 张成亮
     * @date 2018/7/30
     * @description 结束当前任务
     **/
    @Override
    public void completeWorkTask(WorkActionDTO workTask) {

    }

    /**
     * @param workTask 当前任务
     * @author 张成亮
     * @date 2018/7/30
     * @description 认领当前任务, 如果任务不是会签的话，认领任务将会使任务从其他人任务列表内消失
     **/
    @Override
    public void claimWorkTask(WorkActionDTO workTask) {

    }
}
