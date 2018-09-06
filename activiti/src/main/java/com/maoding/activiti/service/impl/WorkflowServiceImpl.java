package com.maoding.activiti.service.impl;

import com.maoding.activiti.dto.*;
import com.maoding.activiti.service.WorkflowService;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.ProcessTypeConst;
import com.maoding.core.util.*;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/26
 * @description :
 */
@Service("workflowServiceImpl")
public class WorkflowServiceImpl extends NewBaseService implements WorkflowService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormService formService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private  ProcessEngine processEngine;

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
    @Override
    public ProcessDefineDetailDTO prepareProcessDefine(ProcessDetailPrepareDTO prepareRequest) {
        ProcessDefineDetailDTO processDefineDetail;

        //补充未填写字段
        TraceUtils.check(StringUtils.isNotEmpty(prepareRequest.getKey()),"!key不可以为空");
        //name字段
        if (StringUtils.isEmpty(prepareRequest.getName())){
            prepareRequest.setName(ProcessTypeConst.nameMap.get(prepareRequest.getKey()));
        }


        //生成新流程或返回已有流程
        if (isEditConditionType(prepareRequest)){
            //如果是设置条件分支，生成新流程
            processDefineDetail = BeanUtils.createFrom(prepareRequest, ProcessDefineDetailDTO.class);
            DigitConditionEditDTO condition = prepareRequest.getStartDigitCondition();
            processDefineDetail.setFlowTaskGroupList(toOrderedFlowTaskGroupList(toFlowTaskGroupList(condition),prepareRequest.getKey()));
        } else if (!isFreeType(prepareRequest)){
            //否则如果不是自由流程，读取流程或生成新流程
            //查找已有流程
            Process process = getProcessByKey(getProcessDefineKey(prepareRequest),prepareRequest.getCurrentCompanyId());
            //如果是已有流程，转换已有流程
            if (process != null){
                processDefineDetail = toProcessDefineDetailDTO(process);
            } else {
                //如果是新流程
                //如果指定流程模板，复制流程
                if (StringUtils.isNotEmpty(prepareRequest.getSrcProcessDefineId())) {
                    process = getProcessByKey(prepareRequest.getSrcProcessDefineId(),prepareRequest.getCurrentCompanyId());
                }
                //如果找到模板，设定模板参数，否则创建流程
                if (process != null) {
                    //设定模板参数
                    process.setName(prepareRequest.getName());
                    process.setId(getProcessDefineKey(prepareRequest));
                    processDefineDetail = toProcessDefineDetailDTO(process);
                    processDefineDetail = updateProcessDefineDetailDTO(processDefineDetail,process);
                } else {
                    processDefineDetail = BeanUtils.createFrom(prepareRequest, ProcessDefineDetailDTO.class);
                    //添加默认路径
                    processDefineDetail.setFlowTaskGroupList(toFlowTaskGroupList(null));
                }
            }
        } else {
            //如果是自由流程，返回空，需要保证id正确
            processDefineDetail = BeanUtils.createFrom(prepareRequest, ProcessDefineDetailDTO.class);
            processDefineDetail.setId(getProcessDefineKey(prepareRequest.getCurrentCompanyId(),prepareRequest.getKey(),prepareRequest.getType()));
        }


        //添加单位并返回
        processDefineDetail.setUnit(ProcessTypeConst.unitMap.get(prepareRequest.getKey()));
        return processDefineDetail;
    }

    //判断是否自由流程
    private boolean isFreeType(ProcessDetailPrepareDTO prepareRequest){
        return (prepareRequest == null) || (isFreeType(prepareRequest.getType()));
    }

    //更新流程定义内的组名称和用户名称
    private ProcessDefineDetailDTO updateProcessDefineDetailDTO(ProcessDefineDetailDTO processDefineDetailDTO, Process process){
        //添加可以启动的组和用户名称
        if (ObjectUtils.isNotEmpty(process.getCandidateStarterGroups())){
            List<FlowGroupDTO> groupList = new ArrayList<>();
            for (String groupId : process.getCandidateStarterGroups()) {
                groupList.add(new FlowGroupDTO(groupId,getGroupName(groupId)));
            }
            processDefineDetailDTO.setCandidateStarterGroupList(groupList);
        }
        if (ObjectUtils.isNotEmpty(process.getCandidateStarterUsers())){
            List<FlowUserDTO> userList = new ArrayList<>();
            for (String companyUserId : process.getCandidateStarterUsers()) {
                FlowUserDTO user = new FlowUserDTO();
                user.setId(companyUserId);
                userList.add(user);
            }
            processDefineDetailDTO.setCandidateStarterUserList(userList);
        }
        return processDefineDetailDTO;
    }



    //判断是否是设置条件分支
    private boolean isEditConditionType(ProcessDetailPrepareDTO prepareRequest){
        return (prepareRequest != null)
                && (isConditionType(prepareRequest.getType()))
                && (ObjectUtils.isNotEmpty(prepareRequest.getStartDigitCondition()));
    }

    //转换数字条件为用户任务编辑信息
    @Deprecated
    private Map<String,List<FlowTaskDTO>> toFlowTaskListMap(DigitConditionEditDTO digitConditionEditRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(digitConditionEditRequest),"!参数错误");
        TraceUtils.check(ObjectUtils.isNotEmpty(digitConditionEditRequest.getPointList()),"!参数错误");

        Map<String,List<FlowTaskDTO>> taskListMap = new HashMap<>();
        taskListMap.put(ProcessTypeConst.DEFAULT_FLOW_TASK_KEY,toFlowTaskList(getDefaultFlowTaskListInfo()));
        List<Double> pointList = digitConditionEditRequest.getPointList();
        pointList.forEach(point -> taskListMap.put(point.toString(),toFlowTaskList(getDefaultFlowTaskListInfo())));
        return taskListMap;
    }

    //转换数字条件为用户任务信息
    private List<FlowTaskGroupDTO> toFlowTaskGroupList(DigitConditionEditDTO digitConditionEditRequest) {
        List<FlowTaskGroupDTO> taskGroupList = new ArrayList<>();
        //添加默认路径
        FlowTaskGroupDTO prevGroup = new FlowTaskGroupDTO(ProcessTypeConst.DEFAULT_FLOW_TASK_KEY,null);
        taskGroupList.add(prevGroup);

        //添加条件分支路径
        if (isValid(digitConditionEditRequest)) {
            List<Double> pointList = digitConditionEditRequest.getPointList().stream()
                    .sorted()
                    .collect(Collectors.toList());
            for (Double point : pointList) {
                FlowTaskGroupDTO taskGroup = new FlowTaskGroupDTO(point.toString(),null);
                taskGroupList.add(taskGroup);
            }
        }
        return taskGroupList;
    }

    //条件分支是否有效
    private boolean isValid(DigitConditionEditDTO digitConditionEditRequest){
        return (digitConditionEditRequest != null)
                && (ObjectUtils.isNotEmpty(digitConditionEditRequest));
    }

    private List<FlowTaskDTO> toFlowTaskList(List<FlowTaskEditDTO> flowTaskEditList){
        return BeanUtils.createListFrom(flowTaskEditList,FlowTaskDTO.class);
    }

    //查找已有流程
    private Process getProcessByKey(String processDefineKey,String currentCompanyId){
        //查找最后发布的流程版本
        ProcessDefinitionQuery pdQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefineKey)
                .processDefinitionTenantId(currentCompanyId)
                .latestVersion()
                .orderByProcessDefinitionVersion().desc();
        List<ProcessDefinition> pdList = pdQuery.list();
        ProcessDefinition pd = ObjectUtils.getFirst(pdList);
        if (pd == null){
            return null;
        }

        //查找流程定义model
        BpmnModel bm = repositoryService.getBpmnModel(pd.getId());
        if (bm == null){
            return null;
        }
        
        //查找流程定义
        return bm.getProcessById(processDefineKey);
    }

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
    @Override
    public ProcessDefineDetailDTO changeProcessDefine(ProcessDefineDetailEditDTO editRequest) {
        ProcessDefineDetailDTO processDefineDetail;
        if (!isFreeType(editRequest)) {
            Process process = createProcess(editRequest);
            BpmnModel model = createModel(process);
            new BpmnAutoLayout(model).execute();
            repositoryService.createDeployment()
                    .addBpmnModel(getProcessDefineKey(editRequest) + ".bpmn", model)
                    .name(editRequest.getName())
                    .tenantId(editRequest.getCurrentCompanyId())
                    .deploy();

            process = getProcessByKey(getProcessDefineKey(editRequest),editRequest.getCurrentCompanyId());

            processDefineDetail = toProcessDefineDetailDTO(process);
        } else {
            processDefineDetail = BeanUtils.createFrom(editRequest,ProcessDefineDetailDTO.class);
            processDefineDetail.setId(getProcessDefineKey(editRequest.getCurrentCompanyId(),editRequest.getKey(),editRequest.getType()));
        }

        //设置单位并返回
        processDefineDetail.setUnit(ProcessTypeConst.unitMap.get(editRequest.getKey()));
        return processDefineDetail;
    }

    //判断是否自由流程
    private boolean isFreeType(ProcessDefineDetailEditDTO editRequest){
        return (editRequest == null) || (isFreeType(editRequest.getType()));
    }

    //转换工作流引擎内流程模型对象为ProcessDefineDetailDTO
    private ProcessDefineDetailDTO toProcessDefineDetailDTO(Process process){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(process),"!参数错误");

        //复制同名属性
        ProcessDefineDetailDTO processDefineDetailDTO = BeanUtils.createFrom(process,ProcessDefineDetailDTO.class);

        //获取start节点
        StartEvent startElement = (StartEvent) process.getFlowElement(ProcessTypeConst.FLOW_ELEMENT_KEY_START);
        List<SequenceFlow> sequenceList = startElement.getOutgoingFlows();

        //获取用户任务及条件节点，如果不是条件流程，用户任务序列保存在默认路径内
        Map<String,List<FlowTaskDTO>> taskListMap = new HashMap<>();
        List<FlowTaskGroupDTO> taskGroupList = new ArrayList<>();
        if (isConditionType(process)){
            sequenceList.forEach(sequence ->{
                //获取一条路径
                String condition = sequence.getConditionExpression();
                List<FlowTaskDTO> taskList = listFlowTask(process,sequence);

                //获取节点值并保存路径，默认路径节点值为空
                Double point = getPointFromCondition(condition);
                String groupName = (isDefaultFlow(point)) ? ProcessTypeConst.DEFAULT_FLOW_TASK_KEY : point.toString();

                //保存路径到taskGroup
                FlowTaskGroupDTO taskGroup = new FlowTaskGroupDTO();
                taskGroup.setName(groupName);
                taskGroup.setFlowTaskList(taskList);
                taskGroupList.add(taskGroup);

                //维持兼容性
                taskListMap.put(groupName,taskList);
            });
        } else {
            //获取默认路径
            SequenceFlow sequence = ObjectUtils.getFirst(sequenceList);
            List<FlowTaskDTO> taskList = listFlowTask(process,sequence);
            //保存路径到taskGroup
            FlowTaskGroupDTO taskGroup = new FlowTaskGroupDTO();
            taskGroup.setName(ProcessTypeConst.DEFAULT_FLOW_TASK_KEY);
            taskGroup.setFlowTaskList(taskList);
            taskGroupList.add(taskGroup);

            //维持兼容性
            taskListMap.put(ProcessTypeConst.DEFAULT_FLOW_TASK_KEY,taskList);
        }
        String id = process.getId();
        String key = StringUtils.getContent(id,-2,ProcessTypeConst.ID_SPLIT);
        Integer type = DigitUtils.parseInt(StringUtils.lastRight(id,ProcessTypeConst.ID_SPLIT));

        processDefineDetailDTO.setType(type);
        processDefineDetailDTO.setKey(key);
        processDefineDetailDTO.setFlowTaskGroupList(toOrderedFlowTaskGroupList(taskGroupList,key));

        processDefineDetailDTO = updateProcessDefineDetailDTO(processDefineDetailDTO,process);

        return processDefineDetailDTO;
    }

    //把taskListMap转换为排好序的路径序列
    private List<FlowTaskGroupDTO> toOrderedFlowTaskGroupList(List<FlowTaskGroupDTO> taskGroupList,String key){
        List<FlowTaskGroupDTO> dstList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(taskGroupList)){
            //添加默认路径
            FlowTaskGroupDTO prevGroup = null;
            for (FlowTaskGroupDTO taskGroup : taskGroupList){
                if (StringUtils.isSame(ProcessTypeConst.DEFAULT_FLOW_TASK_KEY,taskGroup.getName())){
                    prevGroup = taskGroup;
                    dstList.add(taskGroup);
                }
            }


            Double point = null;

            for (int i=0;i<taskGroupList.size();i++){
                FlowTaskGroupDTO taskGroup = taskGroupList.get(i);
                if (isRemainMin(taskGroupList,taskGroup,point)){
                    point = DigitUtils.parseDouble(taskGroup.getName());
                    String unit = ProcessTypeConst.unitMap.get(key);
                    String title = point + unit + "<=" + ProcessTypeConst.titleMap.get(key);

                    taskGroup.setMinValue(point);
                    taskGroup.setTitle(title);
                    if (prevGroup != null) {
                        prevGroup.setMaxValue(point);
                        String prevTitle = StringUtils.isEmpty(prevGroup.getTitle()) ? ProcessTypeConst.titleMap.get(key) : prevGroup.getTitle();
                        prevGroup.setTitle(prevTitle + "<" + point + unit);
                    }
                    prevGroup = taskGroup;
                    if(i==taskGroupList.size()-1){//最后一个
                        title = ProcessTypeConst.titleMap.get(key)+">=" + point + unit;
                        taskGroup.setTitle(title);
                    }
                    dstList.add(taskGroup);
                }
            }
        }
        return dstList;
    }

    //判断是否是余下来的最小节点路径
    private boolean isRemainMin(List<FlowTaskGroupDTO> taskGroupList, FlowTaskGroupDTO taskGroup, Double point){
        boolean isMin = true;
        if (ObjectUtils.isEmpty(taskGroupList) || ObjectUtils.isEmpty(taskGroup) || StringUtils.isSame(ProcessTypeConst.DEFAULT_FLOW_TASK_KEY,taskGroup.getName())){
            isMin = false;
        } else {
            double taskPoint = DigitUtils.parseDouble(taskGroup.getName());
            for (FlowTaskGroupDTO tg : taskGroupList) {
                if (StringUtils.isNotSame(ProcessTypeConst.DEFAULT_FLOW_TASK_KEY,tg.getName())){
                    double tgPoint = DigitUtils.parseDouble(tg.getName());
                    if (((point == null) || (point < tgPoint))
                        && (tgPoint < taskPoint)){
                        isMin = false;
                        break;
                    }
                }
            }
        }
        return isMin;
    }

    //判断condition路径是否默认路径
    private boolean isDefaultFlow(Double point){
        return (point == null);
    }

    //获取路径上的所有用户任务
    private List<FlowTaskDTO> listFlowTask(Process process,SequenceFlow sequence){
        List<FlowTaskDTO> taskList = new ArrayList<>();

        FlowElement nextElement = process.getFlowElement(sequence.getTargetRef());
        while (isUserTask(nextElement)){
            UserTask nextUserTask = (UserTask)nextElement;
            taskList.add(toFlowTask(nextUserTask));
            nextElement = getNextUserTask(process,nextUserTask);
        }
        return taskList;
    }

    //是否UserTask
    private boolean isUserTask(FlowElement element){
        return (element != null) && (element instanceof UserTask);
    }

    //获取下一个UserTask
    private UserTask getNextUserTask(Process process,UserTask userTask){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(userTask),"!参数错误");
        TraceUtils.check(ObjectUtils.isNotEmpty(userTask.getOutgoingFlows()),"!参数错误");

        List<SequenceFlow> nextSequenceList = userTask.getOutgoingFlows();
        for (SequenceFlow nextSequence : nextSequenceList) {
            FlowElement nextElement = process.getFlowElement(nextSequence.getTargetRef());
            if (isUserTask(nextElement)) {
                return (UserTask) nextElement;
            }
        }
        return null;
    }

    private FlowTaskDTO toFlowTask(UserTask userTask){
        FlowTaskDTO task = BeanUtils.createFrom(userTask,FlowTaskDTO.class);
        if (ObjectUtils.isNotEmpty(userTask.getCandidateGroups())){
            List<FlowGroupDTO> groupList = new ArrayList<>();
            for (String groupId : userTask.getCandidateGroups()) {
                groupList.add(new FlowGroupDTO(groupId,getGroupName(groupId)));
            }
            task.setCandidateGroupList(groupList);
        }
        if (ObjectUtils.isNotEmpty(userTask.getCandidateUsers())){
            List<FlowUserDTO> userList = new ArrayList<>();
            for (String companyUserId : userTask.getCandidateUsers()) {
                FlowUserDTO user = new FlowUserDTO();
                user.setId(companyUserId);
                userList.add(user);
            }
            task.setCandidateUserList(userList);
        }
        if (ObjectUtils.isNotEmpty(userTask.getAssignee())){
            String companyUserId = userTask.getAssignee();
            FlowUserDTO user = new FlowUserDTO();
            user.setId(companyUserId);
            task.setAssigneeUser(user);
        }
        return task;
    }


    /**
     * 流程挂起
     * 用于单据撤销
     */
    @Override
    public void suspendProcessInstanceById(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }




    //获取群组名称
    private String getGroupName(String groupId){
        Group group = identityService.createGroupQuery()
                .groupId(groupId)
                .singleResult();
        return (group != null) ? group.getName() : null;
    }

    //判断是否是创建流程申请
    private boolean isCreateRequest(ProcessDefineDetailEditDTO deploymentEditRequest){
        return StringUtils.isEmpty(deploymentEditRequest.getId());
    }

    //创建一个新流程定义
    private Process createProcess(List<FlowElement> flowElementList,ProcessDefineDetailEditDTO otherInfo){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),"!参数错误");
        TraceUtils.check(ObjectUtils.isNotEmpty(otherInfo),"!参数错误");

        Process process = BeanUtils.createFrom(otherInfo,Process.class);
        flowElementList.forEach(process::addFlowElement);
        process.setId(getProcessDefineKey(otherInfo));

        //添加启动组和用户信息
        if (ObjectUtils.isNotEmpty(otherInfo.getCandidateStarterGroupList())){
            process.setCandidateStarterGroups(new ArrayList<>());
            for (FlowGroupDTO g : otherInfo.getCandidateStarterGroupList()) {
                process.getCandidateStarterGroups().add(g.getId());
            }
        }

        if (ObjectUtils.isNotEmpty(otherInfo.getCandidateStarterUserList())){
            process.setCandidateStarterUsers(new ArrayList<>());
            for (FlowUserDTO u : otherInfo.getCandidateStarterUserList()) {
                process.getCandidateStarterUsers().add(u.getId());
            }
        }

        return process;
    }

    //创建一个流程模型
    private BpmnModel createModel(Process process){
        BpmnModel model = new BpmnModel();
        model.addProcess(process);
//        new BpmnAutoLayout(model).execute();
        return model;
    }

    //创建一个新流程
    private Process createProcess(ProcessDefineDetailEditDTO editRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(editRequest),"!参数错误");

        List<FlowElement> flowElementList = new ArrayList<>();

        //获取输入
        //原始代码使用map做输入参数，现在改为list,为加快开发速度，将其转换为原有的map参数
        Map<String,List<FlowTaskEditDTO>> editListMap = toListMap(editRequest.getFlowTaskGroupList());

        //同步流程类型
       // editRequest.setType(syncProcessType(editRequest));


        //添加开始终止节点
        StartEvent startEvent = appendStartEvent(flowElementList);
        EndEvent endEvent = appendEndEvent(flowElementList);

        //添加流程启动时的数字式的审批条件
        if (isConditionType(editRequest)){
            //添加相应审批条件下的用户任务和连接线
            //提取数字条件，并对数字条件节点进行排序
            List<Double> pointList = toOrderedPointList(editListMap);

            //添加条件及条件内的用户任务和连线
            for (int i=0; i<=pointList.size(); i++){
                //获取此数字条件节点判断字符串
                TraceUtils.check(StringUtils.isNotEmpty(editRequest.getKey()),"!key不能为空");
                String condition = getCondition(pointList,editRequest.getKey(),i);

                //添加从起点开始的用户任务和连接线
                String key = (i == 0) ? ProcessTypeConst.DEFAULT_FLOW_TASK_KEY : pointList.get(i-1).toString();

                List<FlowTaskEditDTO> taskEditList = editListMap.get(key);

                //如果此路径为空，直接添加从起始节点到终止节点的连线，否则建立用户任务节点，每个用户任务添加从上一节点到此节点的连线，并添加上一个用户任务节点到结束节点的连线
                if (ObjectUtils.isNotEmpty(taskEditList)) {
                    //添加用户任务路径
                    UserTask userTask = appendUserTask(flowElementList, startEvent, endEvent, taskEditList, condition);

                    //添加最后一个用户任务到终点的连接线
                    appendSequence(flowElementList, userTask, endEvent, null);
                } else {
                    //添加从起始节点到终止节点的路径
                    appendSequence(flowElementList, startEvent, endEvent, condition);
                }
            }
        } else {
            //如果是自由流程或固定流程，添加默认关键字标注的用户任务序列
            List<FlowTaskEditDTO> taskEditList = (editListMap != null) ?
                    editListMap.get(ProcessTypeConst.DEFAULT_FLOW_TASK_KEY) : null;
            //如果此路径为空，直接添加从起始节点到终止节点的连线，否则建立用户任务节点，每个用户任务添加从上一节点到此节点的连线，并添加上一个用户任务节点到结束节点的连线
            if (ObjectUtils.isNotEmpty(taskEditList)) {
                //添加用户任务路径
                UserTask userTask = appendUserTask(flowElementList,startEvent,endEvent,taskEditList,null);
                //添加最后一个用户任务到终点的连接线
                appendSequence(flowElementList, userTask, endEvent, null);
            } else {
                //添加从起始节点到终止节点的路径
                appendSequence(flowElementList, startEvent, endEvent, null);
            }
        }

        //创建流程
        return createProcess(flowElementList,editRequest);
    }

    //转换
    private Map<String,List<FlowTaskEditDTO>> toListMap(List<FlowTaskGroupEditDTO> flowTaskGroupEditList){
        Map<String,List<FlowTaskEditDTO>> dstMap = new HashMap<>();
        if (ObjectUtils.isNotEmpty(flowTaskGroupEditList)) {
            flowTaskGroupEditList.forEach(tg -> {
                if (StringUtils.isEmpty(tg.getName())) {
                    tg.setName(ProcessTypeConst.DEFAULT_FLOW_TASK_KEY);
                }
                if (ObjectUtils.isNotEmpty(tg.getFlowTaskList())){
                    for (FlowTaskEditDTO t : tg.getFlowTaskList()) {
                        if (ObjectUtils.isNotEmpty(t.getCandidateGroupList())){
                            t.setCandidateGroups(new ArrayList<>());
                            for (FlowGroupDTO g : t.getCandidateGroupList()) {
                                t.getCandidateGroups().add(g.getId());
                            }
                        }
                        if (ObjectUtils.isNotEmpty(t.getCandidateUserList())){
                            t.setCandidateUsers(new ArrayList<>());
                            for (FlowUserDTO u : t.getCandidateUserList()) {
                                t.getCandidateUsers().add(u.getId());
                            }
                        }
                        if (ObjectUtils.isNotEmpty(t.getAssigneeUser())){
                            t.setAssignee(t.getAssigneeUser().getId());
                        }
                    }
                }
                dstMap.put(tg.getName(), tg.getFlowTaskList());
            });
        }
        return dstMap;
    }

    //从用户任务编辑信息中提取数字式条件序列
    private List<Double> toOrderedPointList(Map<String, List<FlowTaskEditDTO>> taskListMap){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(taskListMap),"!参数错误");

        List<Double> pointList = new ArrayList<>();
        taskListMap.forEach((key, value) -> {
            if (StringUtils.isNotSame(key, ProcessTypeConst.DEFAULT_FLOW_TASK_KEY)) {
                pointList.add(DigitUtils.parseDouble(key));
            }
        });

        return pointList.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    //根据表达式获取关键字名称
    private String getKeyFromCondition(String condition){
        String s;
        if (StringUtils.contains(condition,">=")){
            s = StringUtils.left(condition,">=");
            String cs = StringUtils.lastRight(s," and ");
            if (StringUtils.isEmpty(cs)){
                cs = StringUtils.right(s,"{");
            }
            s = cs;
        } else {
            s = StringUtils.lastRight(condition,">");
            s = StringUtils.left(s,"}");
        }
        return s;
    }

    //根据表达式获取数字条件节点
    private Double getPointFromCondition(String condition){
        String s = StringUtils.left(condition,"}");
        if (StringUtils.contains(s,">=")){
            return DigitUtils.parseDouble(StringUtils.lastRight(s,">="));
        } else {
            //是默认条件分支
            return null;
        }
    }

    //获取第n段条件的数字条件表达式
    private String getCondition(List<Double> pointList, String key, int n){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(pointList),"!参数错误");
        TraceUtils.check(StringUtils.isNotEmpty(key),"!参数错误");
        TraceUtils.check((pointList.size() >= n) && (n >= 0),"!参数错误");

        //组装 ${max > key and key >= min}
        StringBuilder conditionBuilder = new StringBuilder("${");

        if (pointList.size() > n) {
            conditionBuilder.append(pointList.get(n))
                    .append(">")
                    .append(key);
        }

        if ((pointList.size() > n) && (n > 0)){
            conditionBuilder.append(" and ");
        }

        if (n > 0){
            conditionBuilder.append(key)
                    .append(">=")
                    .append(pointList.get(n-1));
        }
        conditionBuilder.append("}");
        return conditionBuilder.toString();
    }

    //是否条件流程类型
    private boolean isConditionType(Integer type){
        return ProcessTypeConst.PROCESS_TYPE_CONDITION.equals(type);
    }

    //是否自由流程类型
    private boolean isFreeType(Integer type){
        return ProcessTypeConst.TYPE_FREE.equals(type) || ProcessTypeConst.TYPE_NOT_PROCESS.equals(type);
    }


    //判断是否条件流程，其他类型参数
    private boolean isConditionType(Process process){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(process),"!参数错误");

        String processId = process.getId();
        String typeId = StringUtils.lastRight(processId,ProcessTypeConst.ID_SPLIT);
        Integer type = DigitUtils.parseInt(typeId);
        return isConditionType(type);
    }

    //判断是否条件流程，其他类型参数
    private boolean isConditionType(ProcessDetailPrepareDTO deploymentPrepareRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(deploymentPrepareRequest),"!参数错误");

        return (deploymentPrepareRequest.getStartDigitCondition() != null)
                && (ObjectUtils.isNotEmpty(deploymentPrepareRequest.getStartDigitCondition().getPointList()));
    }


    //是否条件流程，其他类型参数
    private boolean isConditionType(ProcessDefineDetailEditDTO deploymentEditRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(deploymentEditRequest),"!参数错误");

        return isConditionType(deploymentEditRequest.getType());
    }


    //通过输入参数组合成流程key：p_companyId_key_type, id不可以以数字开始，因此添加p_前缀
    private <T> String getProcessDefineKey(String companyId, String key, T type){
        return ProcessTypeConst.ID_PREFIX_PROCESS + companyId + ProcessTypeConst.ID_SPLIT
                + key + ProcessTypeConst.ID_SPLIT
                + type.toString();
    }
    
    //通过输入参数组合成流程, 兼容的其他参数类型
    private String getProcessDefineKey(ProcessDefineDetailEditDTO deploymentEditRequest){
        return getProcessDefineKey(deploymentEditRequest.getCurrentCompanyId(),deploymentEditRequest.getKey(),deploymentEditRequest.getType());
    }
    private String getProcessDefineKey(ProcessDetailPrepareDTO deployPrepareRequest){
        return getProcessDefineKey(deployPrepareRequest.getCurrentCompanyId(),deployPrepareRequest.getKey(),deployPrepareRequest.getType());
    }

    //创建并添加起始节点
    private StartEvent appendStartEvent(List<FlowElement> flowElementList) {
        //创建并添加起始节点
        StartEvent startEvent = new StartEvent();
        startEvent.setId(ProcessTypeConst.FLOW_ELEMENT_KEY_START);
        flowElementList.add(startEvent);
        return startEvent;
    }

    //创建并添加终止节点
    private EndEvent appendEndEvent(List<FlowElement> flowElementList) {
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),"!参数错误");

        //创建并添加终止节点
        EndEvent endEvent = new EndEvent();
        endEvent.setId(ProcessTypeConst.FLOW_ELEMENT_KEY_END);
        flowElementList.add(endEvent);
        return endEvent;
    }

    //创建任务连接线
    private SequenceFlow appendSequence(List<FlowElement> flowElementList,FlowElement flowElement1,FlowElement flowElement2,String condition){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),"!参数错误");
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElement1),"!参数错误");
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElement2),"!参数错误");

        //添加连接线
        SequenceFlow sequence = new SequenceFlow();
        sequence.setSourceRef(flowElement1.getId());
        sequence.setTargetRef(flowElement2.getId());
        if (StringUtils.isNotEmpty(condition)){
            sequence.setConditionExpression(condition);
        }
        flowElementList.add(sequence);
        return sequence;
    }

    //在一个节点后创建并添加用户任务节点及连接线
    private UserTask appendUserTask(List<FlowElement> flowElementList,FlowElement prevFlowElement,EndEvent endEvent,FlowTaskEditDTO flowTaskInfo,String condition){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),"!参数错误");
        TraceUtils.check(ObjectUtils.isNotEmpty(prevFlowElement),"!参数错误");
        TraceUtils.check(ObjectUtils.isNotEmpty(prevFlowElement.getId()),"!参数错误");
        TraceUtils.check(ObjectUtils.isNotEmpty(flowTaskInfo),"!参数错误");

        //创建并添加用户任务节点
        UserTask userTask = BeanUtils.createFrom(flowTaskInfo,UserTask.class);
        if (StringUtils.isEmpty(userTask.getId())){
            userTask.setId(ProcessTypeConst.ID_PREFIX_TASK + StringUtils.getUUID());
        }
        flowElementList.add(userTask);

        //创建并添加连接线
        appendSequence(flowElementList,prevFlowElement,userTask,condition);
        //如果上一个节点是用户任务，添加一条到结束节点的连线
        if (isUserTask(prevFlowElement)){
            appendSequence(flowElementList,prevFlowElement,endEvent,"${isPass=='0'}");
        }

        return userTask;
    }

    //在一个节点后添加用户任务节点序列及连接线，返回最后添加的用户任务
    private UserTask appendUserTask(List<FlowElement> flowElementList,FlowElement prevFlowElement,EndEvent endEvent,List<FlowTaskEditDTO> flowTaskInfoList,String condition){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),"!参数错误");
        TraceUtils.check(ObjectUtils.isNotEmpty(prevFlowElement),"!参数错误");

        UserTask userTask = null;
        //如果存在用户任务，添加用户任务
        if (ObjectUtils.isNotEmpty(flowTaskInfoList)){
            //在第一次使用传入的flowElement,其后使用创建的userTask作为上一个节点
            for (FlowTaskEditDTO flowTaskInfo : flowTaskInfoList){
                prevFlowElement = appendUserTask(flowElementList,prevFlowElement,endEvent,flowTaskInfo,condition);
                //第一次主线连线使用传入的condition，其后使用isPass=1
                condition = "${isPass=='1'}";
            }
            userTask = (UserTask) prevFlowElement;
        }
        return userTask;
    }

    //获取一个默认用户任务路径
    private List<FlowTaskEditDTO> getDefaultFlowTaskListInfo(){
        List<FlowTaskEditDTO> defaultFlowTaskListInfo = new ArrayList<>();
        defaultFlowTaskListInfo.add(getDefaultFlowTaskInfo());
        return defaultFlowTaskListInfo;
    }

    //获取一个默认用户任务信息
    private FlowTaskEditDTO getDefaultFlowTaskInfo(){
        FlowTaskEditDTO defaultFlowTaskInfo = new FlowTaskEditDTO();
        defaultFlowTaskInfo.setId(ProcessTypeConst.ID_PREFIX_TASK + StringUtils.getUUID());
        defaultFlowTaskInfo.setName(defaultFlowTaskInfo.getId());
        return defaultFlowTaskInfo;
    }

    /**
     * 描述       删除流程
     * 日期       2018/7/31
     * @author   张成亮
     * @param    deleteRequest 更改信息
     *              如果未指定id，则根据currentCompanyId,key生成部分流程编号，只要编号符合这部分的流程都将被删除
     **/
    @Override
    public void deleteProcessDefine(ProcessDefineQueryDTO deleteRequest) {
        TraceUtils.check(deleteRequest != null);
        //删除已定义流程
        TraceUtils.check(StringUtils.isNotEmpty(deleteRequest.getCurrentCompanyId()),"!currentCompanyId不能为空");
        TraceUtils.check(StringUtils.isNotEmpty(deleteRequest.getKey()),"!key不能为空");
        List<Deployment> list = repositoryService.createDeploymentQuery()
                .processDefinitionKeyLike(ProcessTypeConst.ID_PREFIX_PROCESS
                        + deleteRequest.getCurrentCompanyId()
                        + ProcessTypeConst.ID_SPLIT
                        + deleteRequest.getKey() + "%")
                .list();
        list.forEach(deployment ->
            repositoryService.deleteDeployment(deployment.getId())
        );

    }


    /**
     * 描述       查询流程定义，返回列表
     * 日期       2018/8/2
     * @author   张成亮
     * @param    query 流程查询条件
     * @return   流程列表
     **/
    @Override
    public List<ProcessDefineDTO> listProcessDefine(ProcessDefineQueryDTO query) {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKeyLike(ProcessTypeConst.ID_PREFIX_PROCESS + query.getCurrentCompanyId() + ProcessTypeConst.ID_SPLIT + query.getKey() + "%")
                .latestVersion()
                .active()
                .list();
        return toProcessDefineDTOList(list);
    }

    //转换流程部署信息列表到卯丁流程信息列表
    private List<ProcessDefineDTO> toProcessDefineDTOList(List<ProcessDefinition> srcList){
        List<ProcessDefineDTO> dstList = new ArrayList<>();
        srcList.forEach(src ->
            dstList.add(toProcessDefineDTO(src))
        );
        return dstList;
    }

    //转换流程部署信息到卯丁流程信息
    private ProcessDefineDTO toProcessDefineDTO(ProcessDefinition src){
        ProcessDefineDTO dto = BeanUtils.createFrom(src,ProcessDefineDTO.class);
        String key = src.getKey();
        dto.setId(key);
        dto.setType(DigitUtils.parseInt(StringUtils.lastRight(key,ProcessTypeConst.ID_SPLIT)));
        dto.setKey(StringUtils.getContent(key,-2,ProcessTypeConst.ID_SPLIT));
        dto.setDocumentation(src.getDescription());
        return dto;
    }

    /**
     * 描述       查询符合条件的流程定义个数
     * 日期       2018/8/2
     * @author   张成亮
     * @param    query 流程查询条件
     * @return   流程定义个数
     **/
    @Override
    public int countProcessDefine(ProcessDefineQueryDTO query) {
        return (int) repositoryService.createProcessDefinitionQuery()
                .processDefinitionKeyLike(ProcessTypeConst.ID_PREFIX_PROCESS + query.getCurrentCompanyId() + ProcessTypeConst.ID_SPLIT + query.getKey() + "%")
                .latestVersion()
                .active()
                .count();
    }

    /**
     * 描述       查询符合条件的流程定义，返回分页结果
     * 日期       2018/8/2
     * @author   张成亮
     * @param    query 流程查询条件
     * @return   流程定义分页信息
     **/
    @Override
    public CorePageDTO<ProcessDefineDTO> listPageProcessDefine(ProcessDefineQueryDTO query) {
        ProcessDefinitionQuery pdQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKeyLike(ProcessTypeConst.ID_PREFIX_PROCESS + query.getCurrentCompanyId() + ProcessTypeConst.ID_SPLIT + query.getKey() + "%")
                .latestVersion()
                .active();
        List<ProcessDefinition> list = pdQuery.listPage(query.getPageIndex()*query.getPageSize(),query.getPageSize());

        CorePageDTO<ProcessDefineDTO> result = new CorePageDTO<>();
        result.setPageIndex(query.getPageIndex());
        result.setPageSize(query.getPageSize());
        result.setTotal((int)pdQuery.count());
        result.setData(toProcessDefineDTOList(list));
        return result;
    }

    /**
     * 描述     查询流程用到的用户
     * 日期     2018/8/3
     * @author   张成亮
     * @param    query 查询条件
     *              如果未指定accountId，currentCompanyId，则使用当前用户信息
     *              如果指定了idList，id无效
     *              如果同时指定了多个条件，各条件之间是“与”的关系
     * @return   符合条件的用户列表
     **/
//    @Override
//    public List<UserDTO> listUser(UserQueryDTO query) {
//        return null;
//    }

    /**
     * 描述     查询流程用到的群组
     * 日期     2018/8/3
     *
     * @param query 查询条件
     *              如果未指定accountId，currentCompanyId，则使用当前用户信息
     *              如果指定了idList，id无效
     *              如果同时指定了多个条件，各条件之间是“与”的关系
     * @return 符合条件的群组列表
     * @author 张成亮
     **/
    @Override
    public List<FlowGroupDTO> listGroup(GroupQueryDTO query) {
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


    @Override
    public List<FlowTaskDTO> listWorkTask(String businessKey) {
        List<FlowTaskDTO> taskList = new ArrayList<>();
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .list();

        //处理 getVariables ，创建下一个人要处理的任务，并且返回任务的id，可能是多个任务的id。把返回的任务id 放入 dto.getVariables() 中进行保存
        list.stream().forEach(task->{
            List<IdentityLink> userList = taskService.getIdentityLinksForTask(task.getId());
            userList.stream().forEach(u->{
                String userIdStr = null;
                if("assignee".equals(u.getType())){
                    userIdStr = u.getUserId();
                }
                if("candidate".equals(u.getType())){
                    userIdStr = u.getUserId();
                }
                String[] userIds = userIdStr.split(",");
                for(String userId:userIds){
                    FlowTaskDTO flowTask = new FlowTaskDTO();
                    flowTask.setId(task.getId());
                    flowTask.setName(task.getName());
                    FlowUserDTO assignee = new FlowUserDTO();
                    assignee.setId(userId);
                    flowTask.setAssigneeUser(assignee);
                    taskList.add(flowTask);
                }
            });
        });
        return taskList;
    }

    @Override
    public List<FlowTaskDTO> listWorkTaskVariableValueEquals(String name, String value) {
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()
                .processVariableValueEquals(name,value)
                .list();
        List<FlowTaskDTO> taskList = new ArrayList<>();
        list.stream().forEach(task->{
            FlowTaskDTO flowTask = new FlowTaskDTO();
            flowTask.setId(task.getId());
            flowTask.setName(task.getName());
            FlowUserDTO assignee = new FlowUserDTO();
            assignee.setId(task.getAssignee());
            flowTask.setAssigneeUser(assignee);
            taskList.add(flowTask);
        });
        return taskList;

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
     * 描述       启动流程
     * 日期       2018/8/1
     *
     * @param workTask
     * @author 张成亮
     */
    @Override
    public WorkTaskDTO startProcess(WorkActionDTO workTask) {
        ProcessInstance processInstance = null;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(workTask.getCompanyUserId());
            if(ProcessTypeConst.PROCESS_TYPE_FREE.equals(workTask.getKey())){
                processInstance = runtimeService.startProcessInstanceByKey(workTask.getKey(),workTask.getBusinessKey(), workTask.getResultMap());
            }else {
                processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(workTask.getKey(),workTask.getBusinessKey(), workTask.getResultMap(),workTask.getCurrentCompanyId());
            }
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
        WorkTaskDTO workTaskDTO = new WorkTaskDTO();
        workTaskDTO.setId(processInstance.getId());
        return workTaskDTO;
    }

    /**
     * @param workTask 当前任务
     * @author 张成亮
     * @date 2018/7/30
     * @description 结束当前任务
     **/
    @Override
    public void completeWorkTask(WorkActionDTO workTask) {
        //如果还未认领，则直接认领，并完成
        if(workTask.getResultMap().containsKey("isNotSign")){
            this.claimWorkTask(workTask);
        }
       //处理完成的动作
        Map<String,Object> variables = new HashMap<>();
        variables.put("isPass",workTask.getIsPass());
        variables.putAll(workTask.getResultMap());
        taskService.complete(workTask.getId(), variables);
    }

    /**
     * 获取任务的参数
     * @param taskId 任务id
     */
    @Override
    public Map<String, Object> getTaskVariables(String taskId) {
        return taskService.getVariables(taskId);
    }

    /**
     * 设置任务的参数
     * @param taskId 任务id
     */
    @Override
    public void setTaskVariables(String taskId, Map<String, Object> variables) {
        taskService.setVariables(taskId,variables);
    }

    /**
     * @param workTask 当前任务
     * @author 张成亮
     * @date 2018/7/30
     * @description 认领当前任务, 如果任务不是会签的话，认领任务将会使任务从其他人任务列表内消失
     **/
    @Override
    public void claimWorkTask(WorkActionDTO workTask) {
        taskService.claim(workTask.getId(), workTask.getCompanyUserId());
    }

    /**
     * @param taskId 当前任务Id
     * @author MaoSF
     * @date 2018/8/02
     * @description 根据当前任务的id获取流程的key
     **/
    @Override
    public String getProcessKeyByTaskId(String taskId) {
        String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
        return getProcessKeyByProcessInstanceId(processInstanceId);
    }
	
	/**
     * @param processInstanceId 当前流程实例id
     * @author MaoSF
     * @date 2018/8/02
     * @description 根据当前流程实例id获取流程的key
     **/
    @Override
    public String getProcessKeyByProcessInstanceId(String processInstanceId) {
        return getProcessByProcessInstanceId(processInstanceId).getKey();
    }

    @Override
    public String getProcessDefineIdByProcessInstanceId(String processInstanceId) {
        return getProcessByProcessInstanceId(processInstanceId).getId();
    }

    public String saveFreeProcess() throws Exception {
        // 1. Build up the model from scratch
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        model.addProcess(process);
        process.setId(ProcessTypeConst.PROCESS_TYPE_FREE);

        process.addFlowElement(createStartEvent());
        process.addFlowElement(createEndEvent());
        String fromTaskId = "task1";
        String toTaskId = null;
        process.addFlowElement(createUserTask(fromTaskId, fromTaskId, "${approveUser}"));
        process.addFlowElement(createSequenceFlow("start", fromTaskId,null));
        for(int i=2;i<=50;i++){
            toTaskId =  "task"+i;
            process.addFlowElement(createUserTask(toTaskId, toTaskId, "${approveUser}"));
            process.addFlowElement(createSequenceFlow(fromTaskId, toTaskId,"${isPass=='1'}"));
            process.addFlowElement(createSequenceFlow(fromTaskId, "end","${isPass=='0'}"));
            fromTaskId = toTaskId;
        }
        process.addFlowElement(createSequenceFlow(fromTaskId, "end",null));

        // 2. Generate graphical information
        new BpmnAutoLayout(model).execute();

        // 3. 部署流程
        Deployment deployment = processEngine.getRepositoryService().createDeployment()
                .addBpmnModel("free_process.bpmn", model).name("free_process deployment")
                .deploy();

        return deployment.getId();
    }

    protected UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
    }

    //任务节点-组
    protected UserTask createGroupTask(String id, String name, String candidateGroup) {
        List<String> candidateGroups=new ArrayList<String>();
        candidateGroups.add(candidateGroup);
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setCandidateGroups(candidateGroups);
        return userTask;

    }

    protected SequenceFlow createSequenceFlow(String from, String to,String conditionExpression) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        if(!StringUtil.isNullOrEmpty(conditionExpression)){
            flow.setConditionExpression(conditionExpression);
        }
        return flow;
    }

    protected StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        return startEvent;
    }

    protected EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        return endEvent;
    }


    //排他网关
    protected ExclusiveGateway createExclusiveGateway(String id,String name) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(id);
        exclusiveGateway.setName(name);
        return exclusiveGateway;
    }


    @Override
    public String getProcessDefineIdByProcessKey(String processKey,String companyId) {
        ProcessDefinitionQuery processDefinitionQuery = this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .processDefinitionTenantId(companyId);
        if(processDefinitionQuery!=null){
            List<ProcessDefinition> processDefinitionList = processDefinitionQuery.active().orderByProcessDefinitionVersion().desc().list();//latestVersion().singleResult();
            if(!CollectionUtils.isEmpty(processDefinitionList)){
                return processDefinitionList.get(0).getId();
            }
        }
        return null;
    }


    private ProcessDefDTO getProcessByProcessInstanceId(String processInstanceId){
        ProcessDefDTO def = new ProcessDefDTO();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if(processInstance!=null){
            def.setId(processInstance.getProcessDefinitionId());
            def.setKey(processInstance.getProcessDefinitionKey());
        }else {
            HistoricProcessInstance processInstance2 = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if(processInstance2!=null){
                def.setId(processInstance2.getProcessDefinitionId());
                def.setKey(processInstance2.getProcessDefinitionKey());
            }
        }
        return def;
    }

    @Override
    public  List<UserTaskDTO> listFlowTaskUser(ProcessDetailPrepareDTO query) {
        List<UserTaskDTO> list = new ArrayList<>();
        BpmnModel model = repositoryService.getBpmnModel(query.getSrcProcessDefineId());
        if(model != null) {
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            StartEvent start = null;
            for(FlowElement e : flowElements) {
                if(e instanceof StartEvent){
                    //接下来获取分支
                    start = (StartEvent)e;
                    break;
                }
            }
            List<SequenceFlow> sequenceFlows =  start.getOutgoingFlows();
            sequenceFlows.stream().forEach(s->{
                UserTaskDTO userTask = new UserTaskDTO();
                Map<String,Double> condition = getPointMapFromCondition(s.getConditionExpression());
                if(condition!=null){
                    userTask.setMax(condition.get("max")+"");
                    userTask.setMin(condition.get("min")+"");
                }
                getUserTask(flowElements,s.getTargetRef(),userTask);
                list.add(userTask);
            });
        }
        return list;
    }

    public void  getUserTask( Collection<FlowElement> flowElements,String refId,UserTaskDTO userTask){
        flowElements.stream().forEach(f->{
            if(refId.equals(f.getId())){
                if(f instanceof UserTask){
                    UserTask t = (UserTask)f;
                    if(!StringUtil.isNullOrEmpty(t.getAssignee())){
                        userTask.getAssignList().add(t.getAssignee());
                    }else if(!CollectionUtils.isEmpty(t.getCandidateUsers())){
                        userTask.getAssignList().addAll(t.getCandidateUsers());
                    }
                    List<SequenceFlow> sequenceFlows =  t.getOutgoingFlows();
                    sequenceFlows.stream().forEach(s->{
                        getUserTask(flowElements,s.getTargetRef(),userTask);
                    });
                }
            }
        });
    }

    //根据表达式获取数字条件节点
    private Map<String,Double> getPointMapFromCondition(String conditionStr){
        Map<String,Double> result = new HashMap<>();
        if(StringUtils.isNotEmpty(conditionStr)){
        //    conditionStr= conditionStr.replaceAll(" || ", " or ").replaceAll(" && ", " and ");
            String split = null;
            if(conditionStr.contains("and")){
                split = "and";
            }
            if(conditionStr.contains("or")){
                split = "or";
            }
            String[] conditions = new String[1];
            if(StringUtil.isNullOrEmpty(split)){
                conditions[0] = conditionStr;
            }else {
                conditions = conditionStr.split(split);
            }

            for(String condition:conditions){
                String s = getCondition(condition);
                if(s.contains(">=")){
                    result.put("min",DigitUtils.parseDouble(StringUtils.lastRight(s,">=").trim()));
                }else   if(s.contains(">")){
                    result.put("max",DigitUtils.parseDouble(StringUtils.left(s,">").trim()));
                }else {
                    result.put("max",DigitUtils.parseDouble(StringUtils.lastRight(s,">").trim()));
                }
            }
        }
        return result;
    }

    private static String getCondition( String condition ){
        String s = condition;
        if(condition.contains("${")){
            s = StringUtils.right(condition,"${");
        }
        if(condition.contains("}")){
            s  = StringUtils.left(s,"}");
        }
        return s;
    }
}
