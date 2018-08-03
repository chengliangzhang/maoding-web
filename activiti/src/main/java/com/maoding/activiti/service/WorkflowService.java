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
     * 描述       加载流程，准备进行编辑
     * 日期       2018/8/2
     * @author   张成亮
     * @param    prepareRequest 加载信息
     *              如果type为ProcessTypeConst.PROCESS_TYPE_CONDITION(3)，且startDigitCondition不为空
     *                  根据startDigitCondition创建多个分支
     *              否则，根据companyId,key,type生成流程编号，查找指定流程定义
     *                  如果找到流程定义，则加载此流程
     *                      加载流程时，srcProcessDefineId无效
     *                  否则，如果未找到流程定义，则创建新流程
     *                      创建流程时，如果指定了srcProcessDefineId时，则复制srcProcessDefineId流程
     *                          复制srcProcessDefineId流程时，不判断流程模板和新流程是否为相同类型
     *                      否则，如果未指定srcProcessDefineId
     *                          创建单分支，一个默认审批节点的流程
     *              调用此接口时，如果创建了一个流程，不会存储流程定义到数据库
     * @return  查找到的或创建的流程定义信息
     **/
    ProcessDefineDetailDTO prepareProcessDefine(ProcessDetailPrepareDTO prepareRequest);


    /**
     * 描述       创建或修改一个流程，并保存到数据库
     *           调用此接口时，会存储流程定义到数据库
     * 日期       2018/7/31
     * @author   张成亮
     * @param    editRequest 更改信息
     *              根据companyId,key,type生成流程编号
     *              如果type为ProcessTypeConst.PROCESS_TYPE_CONDITION(3)
     *                  使用taskListMap中defaultFlow之外的分组名，作为条件节点信息
     *                  使用key值作为条件节点变量名
     *              否则，如果type不为ProcessTypeConst.PROCESS_TYPE_CONDITION(3)
     *                  taskListMap中defaultFlow之外的分组无效
     *              存储流程定义到数据库时
     *                 不会判断数据库内是否已经存在同编号流程，如果存在，会生成新版本
     *                 不会根据type值更改组、人员等设置
     *              taskListMap需要包含所有节点信息，无论是否曾被修改
     * @return   保存后的流程定义
     **/
    ProcessDefineDetailDTO changeProcessDefine(ProcessDefineDetailEditDTO editRequest);

    /**
     * 描述       删除流程
     * 日期       2018/7/31
     * @author   张成亮
     * @param    deleteRequest 更改信息
     *              如果未指定id，则根据currentCompanyId,key生成部分流程编号，只要编号符合这部分的流程都将被删除
     **/
    void deleteProcessDefine(ProcessDefineQueryDTO deleteRequest);

    /**
     * 描述       查询所有流程定义，分组返回列表，组名为中文
     * 日期       2018/8/2
     * @author   张成亮
     * @param    query 流程查询条件
     * @return   分组流程列表
     **/
    List<ProcessDefineGroupDTO> listProcessDefineWithGroup(ProcessDefineQueryDTO query);

    /**
     * 描述       查询符合条件的流程定义，返回列表
     * 日期       2018/8/2
     * @author   张成亮
     * @param    query 流程查询条件
     * @return   流程列表
     **/
    List<ProcessDefineDTO> listProcessDefine(ProcessDefineQueryDTO query);

    /**
     * 描述       查询符合条件的流程定义个数
     * 日期       2018/8/2
     * @author   张成亮
     * @param    query 流程查询条件
     * @return   流程定义个数
     **/
    int countProcessDefine(ProcessDefineQueryDTO query);

    /**
     * 描述       查询符合条件的流程定义，返回分页结果
     * 日期       2018/8/2
     * @author   张成亮
     * @param    query 流程查询条件
     * @return   流程定义分页信息
     **/
    CorePageDTO<ProcessDefineDTO> listPageProcessDefine(ProcessDefineQueryDTO query);

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
     * 描述       启动流程
     * 日期       2018/8/1
     * @author   张成亮
     **/
    WorkTaskDTO startProcess(WorkActionDTO workTask);


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
