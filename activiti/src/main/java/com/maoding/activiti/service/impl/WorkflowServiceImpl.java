package com.maoding.activiti.service.impl;

import com.maoding.activiti.dto.*;
import com.maoding.activiti.service.WorkflowService;
import com.maoding.core.base.dto.CoreEditDTO;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.ProcessTypeConst;
import com.maoding.core.util.*;
import com.maoding.user.dto.UserDTO;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static final String DEFAULT_FLOW_TASK_KEY = "defaultFlow";
    public static final String FLOW_ELEMENT_KEY_START = "start";
    public static final String FLOW_ELEMENT_KEY_END = "end";
    public static final String ID_SPLIT = "_";
    public static final String ID_PREFIX_PROCESS = "p" + ID_SPLIT;
    public static final String ID_PREFIX_TASK = "t" + ID_SPLIT;


    @Autowired
    private RepositoryService repositoryService;

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
     * 描述       加载流程进行编辑
     *           根据companyId,key,type生成流程key，查找指定流程
     *           找到则加载此流程，未找到则创建新流程
     *           如果找到流程，加载流程时，srcDeployId，startDigitCondition无效
     *           如果未找到流程，且指定了srcDeployId时
     *              复制srcDeployId流程，不判断流程模板和新流程是否为相同类型
     * 日期       2018/8/2
     * @param   deploymentPrepareRequest 包含组织编号、类型、流程名称、分支条件等信息
     * @return  新建或修改后的流程信息
     * @author  张成亮
     **/
    @Override
    public DeploymentDTO prepareDeployment(DeploymentPrepareDTO deploymentPrepareRequest) {
        DeploymentDTO deploymentDTO;

        //查找已有流程
        Process process = getProcessByKey(getDeploymentKey(deploymentPrepareRequest));
        //如果是已有流程，转换已有流程
        if (process != null){
            deploymentDTO = toDeploymentDTO(process);
        } else {
            //如果是新流程
            //如果指定流程模板，复制流程
            if (StringUtils.isNotEmpty(deploymentPrepareRequest.getSrcDeployId())) {
                process = getProcessByKey(deploymentPrepareRequest.getSrcDeployId());
            }
            //如果找到模板，设定模板参数，否则创建流程
            if (process != null) {
                //设定模板参数
                process.setName(deploymentPrepareRequest.getName());
                process.setId(getDeploymentKey(deploymentPrepareRequest));
                deploymentDTO = toDeploymentDTO(process);
            } else {
                deploymentDTO = BeanUtils.createFrom(deploymentPrepareRequest, DeploymentDTO.class);
                //如果是条件流程，转换数字条件为编辑信息
                if (isConditionType(deploymentPrepareRequest)) {
                    DigitConditionEditDTO condition = deploymentPrepareRequest.getStartDigitCondition();
                    deploymentDTO.setVarKey(condition.getVarKey());
                    deploymentDTO.setFlowTaskListMap(toFlowTaskListMap(condition));
                }
            }
        }
        return deploymentDTO;
    }

    //转换数字条件为用户任务编辑信息
    private Map<String,List<FlowTaskDTO>> toFlowTaskListMap(DigitConditionEditDTO digitConditionEditRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(digitConditionEditRequest),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(digitConditionEditRequest.getVarKey()),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(digitConditionEditRequest.getPointList()),log);

        Map<String,List<FlowTaskDTO>> taskListMap = new HashMap<>();
        taskListMap.put(DEFAULT_FLOW_TASK_KEY,toFlowTaskList(getDefaultFlowTaskListInfo()));
        List<Long> pointList = digitConditionEditRequest.getPointList();
        pointList.forEach(point -> taskListMap.put(point.toString(),toFlowTaskList(getDefaultFlowTaskListInfo())));
        return taskListMap;
    }

    private List<FlowTaskDTO> toFlowTaskList(List<FlowTaskEditDTO> flowTaskEditList){
        return BeanUtils.createListFrom(flowTaskEditList,FlowTaskDTO.class);
    }

    //查找已有流程
    private Process getProcessByKey(String deploymentKey){
        //查找最后发布的流程版本
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(deploymentKey)
                .latestVersion()
                .singleResult();
        if (pd == null){
            return null;
        }

        //查找流程定义model
        BpmnModel bm = repositoryService.getBpmnModel(pd.getId());
        if (bm == null){
            return null;
        }
        
        //查找流程定义
        return bm.getProcessById(deploymentKey);
    }

    /**
     * 描述       创建或修改一个流程
     *           根据companyId,key,type生成流程key
     *           不判断流程是否已存在
     *           不根据流程类型更改组、人员设置
     *           如果新流程是条件流程，且taskListMap包含defaultFlow之外的值，从中获取节点信息和用户任务信息
     *           如果新流程不是条件流程，taskListMap内非defaultFlow的值无效
     * 日期       2018/7/30
     * @author  张成亮
     * @param   deploymentEditRequest 包含名称、流程内用户任务的编辑信息
     * @return  新建或修改后的流程信息
     **/
    @Override
    public DeploymentDTO changeDeployment(DeploymentEditDTO deploymentEditRequest) {
        Process process = createProcess(deploymentEditRequest);
        BpmnModel model = createModel(process);
        repositoryService.createDeployment()
                .addBpmnModel(getDeploymentKey(deploymentEditRequest) + ".bpmn",model)
                .name(deploymentEditRequest.getName())
                .deploy();

        process = getProcessByKey(getDeploymentKey(deploymentEditRequest));

        return toDeploymentDTO(process);
    }

    //转换工作流引擎内流程模型对象为DeploymentDTO
    private DeploymentDTO toDeploymentDTO(Process process){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(process),log);

        //复制同名属性
        DeploymentDTO deploymentDTO = BeanUtils.createFrom(process,DeploymentDTO.class);

        //获取start节点
        StartEvent startElement = (StartEvent) process.getFlowElement(FLOW_ELEMENT_KEY_START);
        List<SequenceFlow> sequenceList = startElement.getOutgoingFlows();

        //获取用户任务及条件节点，如果不是条件流程，用户任务序列保存在默认路径内
        Map<String,List<FlowTaskDTO>> taskListMap = new HashMap<>();
        if (isConditionType(process)){
            sequenceList.forEach(sequence ->{
                //获取一条路径
                String condition = sequence.getConditionExpression();
                List<FlowTaskDTO> taskList = listFlowTask(process,sequence);

                //获取节点值并保存路径，默认路径节点值为空
                Long point = getPointFromCondition(condition);
                String mapKey = (isDefaultFlow(point)) ? DEFAULT_FLOW_TASK_KEY : point.toString();
                taskListMap.put(mapKey,taskList);

                //保存条件名称到结果
                if (needSaveKey(deploymentDTO)){
                    String key = getKeyFromCondition(condition);
                    if (StringUtils.isNotEmpty(key)) {
                        deploymentDTO.setVarKey(key);
                    }
                }
            });
        } else {
            //获取默认路径
            SequenceFlow sequence = ObjectUtils.getFirst(sequenceList);
            List<FlowTaskDTO> taskList = listFlowTask(process,sequence);
            taskListMap.put(DEFAULT_FLOW_TASK_KEY,taskList);
        }
        deploymentDTO.setFlowTaskListMap(taskListMap);

        return deploymentDTO;
    }

    //判断是否需要保存条件名称
    private boolean needSaveKey(DeploymentDTO deploymentDTO){
        return StringUtils.isEmpty(deploymentDTO.getVarKey());
    }

    //判断condition路径是否默认路径
    private boolean isDefaultFlow(Long point){
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
        TraceUtils.check(ObjectUtils.isNotEmpty(userTask),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(userTask.getOutgoingFlows()),log);

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
        return BeanUtils.createFrom(userTask,FlowTaskDTO.class);
    }

    //判断是否是创建流程申请
    private boolean isCreateRequest(DeploymentEditDTO deploymentEditRequest){
        return StringUtils.isEmpty(deploymentEditRequest.getId());
    }

    //创建一个新流程定义
    private Process createProcess(List<FlowElement> flowElementList,DeploymentEditDTO otherInfo){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(otherInfo),log);

        Process process = BeanUtils.createFrom(otherInfo,Process.class);
        flowElementList.forEach(process::addFlowElement);
        process.setId(getDeploymentKey(otherInfo));

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
    private Process createProcess(DeploymentEditDTO deploymentEditRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(deploymentEditRequest),log);

        List<FlowElement> flowElementList = new ArrayList<>();

        //添加开始终止节点
        StartEvent startEvent = appendStartEvent(flowElementList);
        EndEvent endEvent = appendEndEvent(flowElementList);

        //添加流程启动时的数字式的审批条件
        if (isConditionType(deploymentEditRequest)){
            //添加相应审批条件下的用户任务和连接线
            //提取数字条件，并对数字条件节点进行排序
            List<Long> pointList = toOrderedPointList(deploymentEditRequest.getFlowTaskEditListMap());

            //添加条件及条件内的用户任务和连线
            for (int i=0; i<=pointList.size(); i++){
                //获取此数字条件节点判断字符串
                String condition = getCondition(pointList,deploymentEditRequest.getVarKey(),i);

                //添加从起点开始的用户任务和连接线
                String key = (i == 0) ? DEFAULT_FLOW_TASK_KEY : pointList.get(i-1).toString();

                TraceUtils.check(deploymentEditRequest.getFlowTaskEditListMap() != null);
                List<FlowTaskEditDTO> taskEditList = deploymentEditRequest.getFlowTaskEditListMap().get(key);

                UserTask userTask = appendUserTask(flowElementList,startEvent,endEvent,taskEditList,condition);

                //添加最后一个用户任务到终点的连接线
                appendSequence(flowElementList,userTask,endEvent,null);
            }
        } else {
            //如果是自由流程或固定流程，添加默认关键字标注的用户任务序列
            List<FlowTaskEditDTO> taskEditList = (deploymentEditRequest.getFlowTaskEditListMap() != null) ?
                    deploymentEditRequest.getFlowTaskEditListMap().get(DEFAULT_FLOW_TASK_KEY) : null;
            UserTask userTask = appendUserTask(flowElementList,startEvent,endEvent,taskEditList,null);
            //添加最后一个用户任务到终点的连接线
            appendSequence(flowElementList,userTask,endEvent,null);
        }

        //创建流程
        return createProcess(flowElementList,deploymentEditRequest);
    }

    //从用户任务编辑信息中提取数字式条件序列
    private List<Long> toOrderedPointList(Map<String, List<FlowTaskEditDTO>> taskListMap){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(taskListMap),log);

        List<Long> pointList = new ArrayList<>();
        taskListMap.forEach((key, value) -> {
            if (StringUtils.isNotSame(key, DEFAULT_FLOW_TASK_KEY)) {
                pointList.add(DigitUtils.parseLong(key));
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
    private Long getPointFromCondition(String condition){
        String s = StringUtils.left(condition,"}");
        if (StringUtils.contains(s,">=")){
            return DigitUtils.parseLong(StringUtils.lastRight(s,">="));
        } else {
            //是默认条件分支
            return null;
        }
    }

    //获取第n段条件的数字条件表达式
    private String getCondition(List<Long> pointList, String key, int n){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(pointList),log);
        TraceUtils.check(StringUtils.isNotEmpty(key),log);
        TraceUtils.check((pointList.size() >= n) && (n >= 0),log);

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

    //判断是否条件流程，其他类型参数
    private boolean isConditionType(Process process){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(process),log);

        String processId = process.getId();
        String typeId = StringUtils.lastRight(processId,ID_SPLIT);
        Integer type = DigitUtils.parseInt(typeId);
        return isConditionType(type);
    }

    //判断是否条件流程，其他类型参数
    private boolean isConditionType(DeploymentPrepareDTO deploymentPrepareRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(deploymentPrepareRequest),log);

        return isConditionType(deploymentPrepareRequest.getType())
                && (deploymentPrepareRequest.getStartDigitCondition() != null)
                && (StringUtils.isNotEmpty(deploymentPrepareRequest.getStartDigitCondition().getVarKey()))
                && (ObjectUtils.isNotEmpty(deploymentPrepareRequest.getStartDigitCondition().getPointList()));
    }


    //是否条件流程，其他类型参数
    private boolean isConditionType(DeploymentEditDTO deploymentEditRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(deploymentEditRequest),log);

        return isConditionType(deploymentEditRequest.getType())
                && (StringUtils.isNotEmpty(deploymentEditRequest.getVarKey()));
    }


    //通过输入参数组合成流程key：p_companyId_key_type, id不可以以数字开始，因此添加p_前缀
    private <T> String getDeploymentKey(String companyId, String key, T type){
        return ID_PREFIX_PROCESS + companyId + ID_SPLIT
                + key + ID_SPLIT
                + type.toString();
    }
    
    //通过输入参数组合成流程, 兼容的其他参数类型
    private String getDeploymentKey(DeploymentEditDTO deploymentEditRequest){
        return getDeploymentKey(deploymentEditRequest.getCurrentCompanyId(),deploymentEditRequest.getKey(),deploymentEditRequest.getType());
    }
    private String getDeploymentKey(DeploymentPrepareDTO deployPrepareRequest){
        return getDeploymentKey(deployPrepareRequest.getCurrentCompanyId(),deployPrepareRequest.getKey(),deployPrepareRequest.getType());
    }

    //从流程引擎获取指定流程
    private Deployment getDeployment(DeploymentEditDTO deploymentEditRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(deploymentEditRequest),log);

        List<Deployment> deploymentList = repositoryService.createDeploymentQuery()
                .deploymentId(deploymentEditRequest.getId())
                .list();
        return ObjectUtils.getFirst(deploymentList);
    }

    //创建并添加起始节点
    private StartEvent appendStartEvent(List<FlowElement> flowElementList) {
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),log);

        //创建并添加起始节点
        StartEvent startEvent = new StartEvent();
        startEvent.setId(FLOW_ELEMENT_KEY_START);
        flowElementList.add(startEvent);
        return startEvent;
    }

    //创建并添加终止节点
    private EndEvent appendEndEvent(List<FlowElement> flowElementList) {
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),log);

        //创建并添加终止节点
        EndEvent endEvent = new EndEvent();
        endEvent.setId(FLOW_ELEMENT_KEY_END);
        flowElementList.add(endEvent);
        return endEvent;
    }

    //创建任务连接线
    private SequenceFlow appendSequence(List<FlowElement> flowElementList,FlowElement flowElement1,FlowElement flowElement2,String condition){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElement1),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElement2),log);

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
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(prevFlowElement),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(prevFlowElement.getId()),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(flowTaskInfo),log);

        //创建并添加用户任务节点
        UserTask userTask = BeanUtils.createFrom(flowTaskInfo,UserTask.class);
        if (StringUtils.isEmpty(userTask.getId())){
            userTask.setId(ID_PREFIX_TASK + StringUtils.getUUID());
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
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(prevFlowElement),log);

        UserTask userTask;
        //如果存在用户任务，添加用户任务
        if (ObjectUtils.isNotEmpty(flowTaskInfoList)){
            //在第一次使用传入的flowElement,其后使用创建的userTask作为上一个节点
            for (FlowTaskEditDTO flowTaskInfo : flowTaskInfoList){
                prevFlowElement = appendUserTask(flowElementList,prevFlowElement,endEvent,flowTaskInfo,condition);
                //第一次主线连线使用传入的condition，其后使用isPass=1
                condition = "${isPass=='1'}";
            }
            userTask = (UserTask) prevFlowElement;
        } else {
            //如果不存在用户任务，添加一个默认的用户任务
            userTask = appendUserTask(flowElementList,prevFlowElement,endEvent,getDefaultFlowTaskInfo(),condition);
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
        defaultFlowTaskInfo.setId(ID_PREFIX_TASK + StringUtils.getUUID());
        defaultFlowTaskInfo.setName(defaultFlowTaskInfo.getId());
        return defaultFlowTaskInfo;
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
    public void deleteDeploy(DeploymentQueryDTO deleteRequest) {
        List<Deployment> list = repositoryService.createDeploymentQuery()
                .processDefinitionKeyLike(ID_PREFIX_PROCESS + deleteRequest.getCurrentCompanyId() + ID_SPLIT + deleteRequest.getKey() + "%")
                .list();
        list.forEach(deployment ->
            repositoryService.deleteDeployment(deployment.getId())
        );
    }

    /**
     * 描述     查询流程，并返回分类结果
     * 日期     2018/8/2
     *
     * @param query 流程查询条件
     * @return 分类流程，key为流程组名称，list为流程列表
     * @author 张成亮
     **/
    @Override
    public Map<String, List<DeploymentSimpleDTO>> listDeploymentWithKey(DeploymentQueryDTO query) {
        Map<String,List<DeploymentSimpleDTO>> result = new HashMap<>();

        //行政审批包含两种，因此都需要过滤出来
        query.setKey(ProcessTypeConst.PROCESS_TYPE_LEAVE);
        List<DeploymentSimpleDTO> leaveList = listDeployment(query);
        query.setKey(ProcessTypeConst.PROCESS_TYPE_ON_BUSINESS);
        List<DeploymentSimpleDTO> onBusinessList = listDeployment(query);
        List<DeploymentSimpleDTO> normalList = new ArrayList<>();
        normalList.addAll(leaveList);
        normalList.addAll(onBusinessList);
        result.put("行政审批",normalList);

        //财务审批
        query.setKey(ProcessTypeConst.PROCESS_TYPE_FINANCE);
        result.put("财务审批",listDeployment(query));

        //项目审批
        query.setKey(ProcessTypeConst.PROCESS_TYPE_PROJECT);
        result.put("项目审批",listDeployment(query));

        return result;
    }

    /**
     * @param query 流程查询器
     * @return 流程列表
     * @author 张成亮
     * @date 2018/7/30
     * @description 查询流程
     **/
    @Override
    public List<DeploymentSimpleDTO> listDeployment(DeploymentQueryDTO query) {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKeyLike(ID_PREFIX_PROCESS + query.getCurrentCompanyId() + ID_SPLIT + query.getKey() + "%")
                .latestVersion()
                .active()
                .list();
        return toDeploymentSimpleDTOList(list);
    }

    //转换流程部署信息列表到卯丁流程信息列表
    private List<DeploymentSimpleDTO> toDeploymentSimpleDTOList(List<ProcessDefinition> srcList){
        List<DeploymentSimpleDTO> dstList = new ArrayList<>();
        srcList.forEach(src ->
            dstList.add(toDeploymentSimpleDTO(src))
        );
        return dstList;
    }

    //转换流程部署信息到卯丁流程信息
    private DeploymentSimpleDTO toDeploymentSimpleDTO(ProcessDefinition src){
        DeploymentSimpleDTO dto = BeanUtils.createFrom(src,DeploymentSimpleDTO.class);
        String key = src.getKey();
        dto.setId(key);
        dto.setType(DigitUtils.parseInt(StringUtils.lastRight(key,ID_SPLIT)));
        dto.setKey(StringUtils.getContent(key,-2,ID_SPLIT));
        return dto;
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
        return (int) repositoryService.createProcessDefinitionQuery()
                .processDefinitionKeyLike(ID_PREFIX_PROCESS + query.getCurrentCompanyId() + ID_SPLIT + query.getKey() + "%")
                .latestVersion()
                .active()
                .count();
    }

    /**
     * @param query 流程查询器
     * @return 流程分页数据
     * @author 张成亮
     * @date 2018/7/30
     * @description 分页获取流程列表
     **/
    @Override
    public CorePageDTO<DeploymentSimpleDTO> listPageDeployment(DeploymentQueryDTO query) {
        ProcessDefinitionQuery pdQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKeyLike(ID_PREFIX_PROCESS + query.getCurrentCompanyId() + ID_SPLIT + query.getKey() + "%")
                .latestVersion()
                .active();
        List<ProcessDefinition> list = pdQuery.listPage(query.getPageIndex()*query.getPageSize(),query.getPageSize());

        CorePageDTO<DeploymentSimpleDTO> result = new CorePageDTO<>();
        result.setPageIndex(query.getPageIndex());
        result.setPageSize(query.getPageSize());
        result.setCount((int)pdQuery.count());
        result.setList(toDeploymentSimpleDTOList(list));
        return result;
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
