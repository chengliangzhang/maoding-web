package com.maoding.activiti.service.impl;

import com.maoding.activiti.dto.*;
import com.maoding.activiti.service.WorkflowService;
import com.maoding.core.base.dto.CoreEditDTO;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.ProcessTypeConst;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.util.ObjectUtils;
import com.maoding.core.util.StringUtils;
import com.maoding.core.util.TraceUtils;
import com.maoding.user.dto.UserDTO;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
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
        if (isCreateRequest(deploymentEditRequest)){
            Process process = createDeployment(deploymentEditRequest);
            return toDeploymentDTO(process);
        } else {
            return null;
        }
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
                Integer point = getPointFromCondition(condition);
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
    private boolean isDefaultFlow(Integer point){
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
    private Process createDeployment(DeploymentEditDTO deploymentEditRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(deploymentEditRequest),log);

        List<FlowElement> flowElementList = new ArrayList<>();
        //如果模板为空
        if (StringUtils.isEmpty(deploymentEditRequest.getSrcDeployId())) {
            //添加开始终止节点
            StartEvent startEvent = appendStartEvent(flowElementList);
            EndEvent endEvent = appendEndEvent(flowElementList);

            //添加流程启动时的数字式的审批条件
            if (isCondition(deploymentEditRequest)){
                //添加相应审批条件下的用户任务和连接线
                //获取数字式审批条件
                DigitConditionEditDTO startDigitCondition = deploymentEditRequest.getStartDigitCondition();
                //对数字条件节点进行排序
                List<Long> pointList = startDigitCondition.getPointList()
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());

                //添加条件及条件内的用户任务和连线
                for (int i=0; i<=pointList.size(); i++){
                    //获取此数字条件节点判断字符串
                    String condition = getCondition(pointList,startDigitCondition.getVarKey(),i);
                    //添加从起点开始的用户任务和连接线
                    String key = (i == pointList.size()) ? DEFAULT_FLOW_TASK_KEY : pointList.get(i).toString();
                    List<FlowTaskEditDTO> taskEditList = (deploymentEditRequest.getFlowTaskEditListMap() != null) ?
                            deploymentEditRequest.getFlowTaskEditListMap().get(key) : null;
                    UserTask userTask = appendUserTask(flowElementList,startEvent,taskEditList,condition);
                    //添加最后一个用户任务到终点的连接线
                    appendSequence(flowElementList,userTask,endEvent,null);
                }
            } else {
                //如果是自由流程或固定流程，添加默认关键字标注的用户任务序列
                List<FlowTaskEditDTO> taskEditList = (deploymentEditRequest.getFlowTaskEditListMap() != null) ?
                        deploymentEditRequest.getFlowTaskEditListMap().get(DEFAULT_FLOW_TASK_KEY) : null;
                UserTask userTask = appendUserTask(flowElementList,startEvent,taskEditList,null);
                //添加最后一个用户任务到终点的连接线
                appendSequence(flowElementList,userTask,endEvent,null);
            }
        }

        //创建流程
        Process process = createProcess(flowElementList,deploymentEditRequest);
        BpmnModel model = createModel(process);
        repositoryService.createDeployment()
            .addBpmnModel(getDeploymentKey(deploymentEditRequest) + ".bpmn",model)
            .name(deploymentEditRequest.getName())
            .deploy();
        return process;
    }

    //根据表达式获取关键字名称
    private String getKeyFromCondition(String condition){
        String[] sArray = condition.split(" and ");
        if (sArray.length <= 1){
            //不是中间节点
            return null;
        }
        String[] sArray2 = sArray[0].split(">=");
        return sArray2[0];
    }

    //根据表达式获取数字条件节点
    private Integer getPointFromCondition(String condition){
        String s = condition.replace("${","").replace("}","");
        if (s.contains(">=")){
            return Integer.valueOf(s.substring(s.indexOf(">=")));
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

    //判断是否条件流程
    private boolean isConditionType(Process process){
        String processId = process.getId();
        String typeId = StringUtils.lastRight(processId,ID_SPLIT);
        Integer type = Integer.valueOf(typeId);
        return isConditionType(type);
    }


    //是否条件流程
    private boolean isCondition(DeploymentEditDTO deploymentEditRequest){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(deploymentEditRequest),log);

        return isConditionType(deploymentEditRequest.getType())
                && (deploymentEditRequest.getStartDigitCondition() != null)
                && (StringUtils.isNotEmpty(deploymentEditRequest.getStartDigitCondition().getVarKey()))
                && (ObjectUtils.isNotEmpty(deploymentEditRequest.getStartDigitCondition().getPointList()));
    }

    //是否条件流程类型
    private boolean isConditionType(Integer type){
        return ProcessTypeConst.PROCESS_TYPE_CONDITION.equals(type);
    }

    //通过输入参数组合成流程key：p_companyId_key_type, id不可以以数字开始，因此添加p_前缀
    private String getDeploymentKey(DeploymentEditDTO deploymentEditRequest){
        return ID_PREFIX_PROCESS + deploymentEditRequest.getCurrentCompanyId() + ID_SPLIT
                + deploymentEditRequest.getKey() + ID_SPLIT
                + deploymentEditRequest.getType();
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
    private UserTask appendUserTask(List<FlowElement> flowElementList,FlowElement prevFlowElement,FlowTaskEditDTO flowTaskInfo,String condition){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(prevFlowElement),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(flowTaskInfo),log);

        //创建并添加用户任务节点
        UserTask userTask = BeanUtils.createFrom(flowTaskInfo,UserTask.class);
        flowElementList.add(userTask);

        //创建并添加连接线
        appendSequence(flowElementList,prevFlowElement,userTask,condition);
        return userTask;
    }

    //在一个节点后添加用户任务节点序列及连接线，返回最后添加的用户任务
    private UserTask appendUserTask(List<FlowElement> flowElementList,FlowElement prevFlowElement,List<FlowTaskEditDTO> flowTaskInfoList,String condition){
        //检查参数
        TraceUtils.check(ObjectUtils.isNotEmpty(flowElementList),log);
        TraceUtils.check(ObjectUtils.isNotEmpty(prevFlowElement),log);

        //UserTask也是FlowElement,因此可以使用UserTask代替prevFlowElement传递第一个参数
        UserTask userTask = BeanUtils.createFrom(prevFlowElement,UserTask.class);

        //如果存在用户任务，添加用户任务
        if (ObjectUtils.isNotEmpty(flowTaskInfoList)){
            //在第一次使用传入的flowElement,其后使用创建的userTask作为上一个节点
            for (FlowTaskEditDTO flowTaskInfo : flowTaskInfoList){
                userTask = appendUserTask(flowElementList,userTask,flowTaskInfo,condition);
                //第一次使用传入的condition，其后无需condition
                condition = null;
            }
        } else {
            //如果不存在用户任务，添加一个默认的用户任务
            userTask = appendUserTask(flowElementList,userTask,getDefaultFlowTaskInfo(),condition);
        }
        return userTask;
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
