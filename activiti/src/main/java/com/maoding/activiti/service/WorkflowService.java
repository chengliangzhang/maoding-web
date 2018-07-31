package com.maoding.activiti.service;

import com.maoding.activiti.dto.*;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.user.dto.UserDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/26
 * @description :
 */
public interface WorkflowService {
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取一个流程的编辑器
     * @param   deployment 指定的流程，可以为空
     * @return  流程编辑信息
     **/
    DeploymentEditDTO createDeploymentEdit(DeploymentDTO deployment);
    
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     保存流程
     * @param   deploymentEdit 编辑内容
     **/
    void saveDeploy(DeploymentEditDTO deploymentEdit);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     删除流程
     * @param   deploymentEdit 删除内容
     **/
    void deleteDeploy(DeploymentEditDTO deploymentEdit);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     挂起流程
     * @param   deploymentEdit 挂起内容
     **/
    void suspendDeploy(DeploymentEditDTO deploymentEdit);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取一个流程查询器
     * @return  流程查询信息
     **/
    DeploymentQueryDTO createDeploymentQuery();
    
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     查询流程
     * @param   query 流程查询器
     * @return  流程列表
     **/
    List<DeploymentDTO> listDeployment(DeploymentQueryDTO query);
    
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取流程总个数
     * @param   query 流程查询器
     * @return  流程个数
     **/
    int countDeployment(DeploymentQueryDTO query);
    
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     分页获取流程列表
     * @param   query 流程查询器
     * @return  流程分页数据
     **/
    CorePageDTO<DeploymentDTO> listPageDeployment(DeploymentQueryDTO query);
    
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取一个流程任务的编辑器
     * @param   deploymentEdit 指定的流程编辑器，不能为空
     * @param   flowTask 指定的流程任务，可以为空
     * @return  流程任务编辑器
     **/
    FlowTaskEditDTO createFlowTaskEdit(DeploymentEditDTO deploymentEdit, FlowTaskDTO flowTask);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     保存流程任务
     * @param   flowTaskEdit 编辑内容
     **/
    void saveFlowTask(FlowTaskEditDTO flowTaskEdit);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     删除流程任务
     * @param   flowTaskEdit 删除内容
     **/
    void deleteFlowTask(FlowTaskEditDTO flowTaskEdit);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取一个流程任务的查询器
     * @return  流程任务查询信息
     **/
    FlowTaskQueryDTO createFlowTaskQuery();

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     查询流程任务
     * @param   query 流程任务查询器
     * @return  流程任务列表
     **/
    List<FlowTaskDTO> listFlowTask(FlowTaskQueryDTO query);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取一个流程路径编辑器
     * @param   sequence 指定的流程路径，可以为空
     * @return  流程路径编辑器
     **/
    FlowSequenceEditDTO createFlowSequenceEdit(DeploymentEditDTO deploymentEdit, FlowSequenceDTO sequence);


    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     保存流程路径
     * @param   flowSequenceEdit 编辑内容
     **/
    void saveFlowSequence(FlowSequenceEditDTO flowSequenceEdit);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     删除流程路径
     * @param   flowSequenceEdit 删除内容
     **/
    void deleteFlowSequence(FlowSequenceEditDTO flowSequenceEdit);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取一个流程路径查询器
     * @return  流程路径查询器
     **/
    FlowSequenceQueryDTO createFlowSequenceQuery();

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     查询流程路径
     * @param   query 流程路径查询器
     * @return  流程路径列表
     **/
    List<FlowSequenceDTO> listFlowSequence(FlowSequenceQueryDTO query);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取一个流程网关编辑器
     * @param   gateWay 指定的流程网关，可以为空
     * @return  流程路径编辑器
     **/
    FlowGateWayEditDTO createFlowGateWayEdit(DeploymentEditDTO deploymentEdit, FlowGateWayDTO gateWay);


    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     保存流程网关
     * @param   flowGateWayEdit 编辑内容
     **/
    void saveFlowGateWay(FlowGateWayEditDTO flowGateWayEdit);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     删除流程网关
     * @param   flowGateWayEdit 删除内容
     **/
    void deleteFlowGateWay(FlowGateWayEditDTO flowGateWayEdit);
    
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取一个流程网关查询器
     * @return  流程网关查询器
     **/
    FlowGateWayQueryDTO createFlowGateWayQuery();

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     查询流程网关
     * @param   query 流程网关查询器
     * @return  流程网关列表
     **/
    List<FlowGateWayDTO> listFlowGateWay(FlowGateWayQueryDTO query);
    
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取一个流程用户查询器
     * @return  流程用户查询器
     **/
    UserQueryDTO createUserQuery();

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     查询用户
     * @param   query 用户查询器
     * @return  用户列表
     **/
    List<UserDTO> listUser(UserQueryDTO query);
    
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取一个正在当前流程任务查询器
     * @return  当前流程任务查询器
     **/
    WorkTaskQueryDTO createWorkTaskQuery();
    
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     查询当前任务
     * @param   query 当前任务查询器
     * @return  当前任务列表
     **/
    List<WorkTaskDTO> listWorkTask(WorkTaskQueryDTO query);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     获取当前任务总个数
     * @param   query 当前任务查询器
     * @return  当前任务个数
     **/
    int countWorkTask(WorkTaskQueryDTO query);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     分页获取当前任务列表
     * @param   query 当前任务查询器
     * @return  当前任务分页数据
     **/
    CorePageDTO<WorkTaskDTO> listPageWorkTask(WorkTaskQueryDTO query);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     启动流程
     * @param   deployment 流程
     * @return  当前流程任务
     **/
    WorkTaskDTO startDeployment(DeploymentDTO deployment);

    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     结束当前任务
     * @param   workTask 当前任务
     **/
    void completeWorkTask(WorkActionDTO workTask);
    
    /**
     * @author  张成亮
     * @date    2018/7/30
     * @description     认领当前任务,如果任务不是会签的话，认领任务将会使任务从其他人任务列表内消失
     * @param   workTask 当前任务
     **/
    void claimWorkTask(WorkActionDTO workTask);
}
