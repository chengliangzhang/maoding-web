package com.maoding.process.service;


import com.maoding.activiti.dto.*;
import com.maoding.financial.dto.AuditDTO;
import com.maoding.financial.dto.AuditEditDTO;
import com.maoding.financial.dto.SaveExpMainDTO;
import com.maoding.process.dto.ActivitiDTO;
import com.maoding.process.dto.TaskDTO;
import com.maoding.process.dto.UserTaskNodeDTO;

import java.util.List;
import java.util.Map;

public interface ProcessService {

    /**
     * 启动流程实例
     * @param dto -> processKey:act_re_procdef 中的key_
     * @param dto -> businessKey:业务表中的id（比如中报销审批启动流程，businessKey = expMain 表中的id)
     * @param dto -> param:启动流程的时候，携带的参数，可以是空
     */
    String startProcessInstance(ActivitiDTO dto) throws Exception;

    /**
     * 是否需要启动流程
     */
    boolean isNeedStartProcess(ActivitiDTO dto) throws Exception;

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
    ProcessDefineDetailDTO prepareProcessDefine(ProcessDetailPrepareDTO prepareRequest) throws Exception;

    ProcessDefineDetailDTO changeProcessDefine(ProcessDefineDetailEditDTO editRequest);

    void deleteProcessDefine(ProcessDefineQueryDTO deleteRequest);

    /**
     * 描述       查询所有流程定义，分组返回列表，组名为中文
     * 日期       2018/8/2
     * @author   张成亮
     * @param    query 流程查询条件
     * @return   分组流程列表
     **/
    List<ProcessDefineGroupDTO> listProcessDefineWithGroup(ProcessDefineQueryDTO query) throws Exception;

    /**
     * 描述       查询流程定义
     * 日期       2018/9/13
     * @author   张成亮
     **/
    List<ProcessDefineDTO> listProcessDefine(ProcessDefineQueryDTO query);

    /**
     * 任务签收
     */
    void claimTask(TaskDTO dto) throws Exception;

    /**
     * 任务完成(直接完成activiti中的任务，id 为ru_task 中的id)
     */
    void completeTask(TaskDTO dto) throws Exception;

    /**
     * 任务完成(直接完成审核表中的任务（audit），id 为exp_audit 中的id)
     */
    boolean completeTask2(TaskDTO dto) throws Exception;

    /**
     * id:审批记录的id
     */
    Map<String,Object> getCurrentProcess(AuditEditDTO dto);

    Map<String,Object> getCurrentTaskUser(AuditEditDTO dto, List<AuditDTO> auditList, String value) throws Exception;

    List<UserTaskNodeDTO> getUserListForAudit(AuditEditDTO dto);

    /**
     * 流程挂起
     * 用于单据撤销
     */
    int suspendProcess(SaveExpMainDTO dto);
}
