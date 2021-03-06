package com.maoding.process.service.impl;

import com.maoding.activiti.dto.*;
import com.maoding.activiti.service.WorkflowService;
import com.maoding.commonModule.dto.AuditCopyDataDTO;
import com.maoding.commonModule.service.AuditCopyService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.ExpenseConst;
import com.maoding.core.constant.ProcessTypeConst;
import com.maoding.core.constant.ProjectCostConst;
import com.maoding.core.util.*;
import com.maoding.dynamicForm.dao.DynamicFormFieldDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.service.DynamicFormGroupService;
import com.maoding.exception.CustomException;
import com.maoding.financial.dto.AuditBaseDTO;
import com.maoding.financial.dto.AuditDTO;
import com.maoding.financial.dto.AuditEditDTO;
import com.maoding.financial.dto.SaveExpMainDTO;
import com.maoding.financial.entity.ExpMainEntity;
import com.maoding.financial.service.ExpAuditService;
import com.maoding.financial.service.ExpMainService;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dto.CompanyUserAppDTO;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.process.dao.ProcessInstanceRelationDao;
import com.maoding.process.dao.ProcessTypeDao;
import com.maoding.process.dto.ActivitiDTO;
import com.maoding.process.dto.TaskDTO;
import com.maoding.process.dto.UserTaskNodeDTO;
import com.maoding.process.entity.ProcessInstanceRelationEntity;
import com.maoding.process.entity.ProcessTypeEntity;
import com.maoding.process.service.ProcessService;
import com.maoding.projectcost.dto.ProjectCostPointDetailDTO;
import com.maoding.projectcost.service.ProjectCostService;
import com.maoding.user.service.UserAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * activiti Service
 * 流程定义，部署，启动,任务 处理接口实现
 */
@Service("processService")
public class ProcessServiceImpl extends NewBaseService implements ProcessService {

    @Autowired
    private ProcessInstanceRelationDao processInstanceRelationDao;

    @Autowired
    private ExpAuditService expAuditService;

    @Autowired
    private ExpMainService expMainService;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private ProcessTypeDao processTypeDao;

    @Autowired
    private UserAttachService userAttachService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ProjectCostService projectCostService;

    @Autowired
    private AuditCopyService auditCopyService;

    @Autowired
    private DynamicFormGroupService dynamicFormGroupService;

    @Autowired
    private DynamicFormFieldDao dynamicFormFieldDao;

    private static final String auditIdKey = "auditId";

    @Override
    public String startProcessInstance(ActivitiDTO dto) throws Exception {
        String processInstanceId = null;
        //获取流程的key值，确定启动哪个流程
        ProcessTypeEntity processType = processTypeDao.getCurrentProcessType(dto.getCurrentCompanyId(),dto.getTargetType());
        String processKey = getProcessKey(processType);
        if(ProcessTypeConst.PROCESS_TYPE_FREE.equals(processKey)
                && (!dto.getParam().containsKey("approveUser") || StringUtil.isNullOrEmpty(dto.getParam().get("approveUser")))){
            return null;
        }

        if(processKey != null){
            //启动流程
            WorkActionDTO workAction = new WorkActionDTO();
            workAction.setResultMap(dto.getParam());
            workAction.setKey(processKey);
            workAction.setBusinessKey(dto.getBusinessKey());
            workAction.setCompanyUserId(dto.getCompanyUserId());
            workAction.setResultMap(dto.getParam());
            workAction.setCurrentCompanyId(dto.getCurrentCompanyId());
            processInstanceId = workflowService.startProcess(workAction).getId();
            //保存审核记录
            saveAudit(dto.getBusinessKey(),dto,true);

            //保存流程实例与业务表的关系
            ProcessInstanceRelationEntity instanceRelation = new ProcessInstanceRelationEntity(dto.getBusinessKey(),processInstanceId,dto.getTargetType());
            //保存流程实例内的流程模板信息
            if (processType != null) {
                instanceRelation.setProcessTypeId(processType.getId());
                instanceRelation.setConditionFieldId(processType.getConditionFieldId());
                instanceRelation.setFinanceFieldId(processType.getFinanceFieldId());
            }

            processInstanceRelationDao.insert(instanceRelation);
            return processInstanceId;
        }
        return null;
    }

    @Override
    public boolean isNeedStartProcess(ActivitiDTO dto) throws Exception {
        //获取流程的key值，确定启动哪个流程
        String processKey = this.getProcessKey(dto.getTargetType(),dto.getCurrentCompanyId());
        if(ProcessTypeConst.PROCESS_TYPE_FREE.equals(processKey)
                && (!dto.getParam().containsKey("approveUser") || StringUtil.isNullOrEmpty(dto.getParam().get("approveUser")))){
            return false;
        }
        return true;
    }

    
    //通过companyId和key查找process信息
    private ProcessDefineDTO getProcessDefineByCompanyIdAndKey(String companyId,String key){
        ProcessDefineQueryDTO query = new ProcessDefineQueryDTO();
        query.setCurrentCompanyId(companyId);
        query.setKey(key);
        query.setNeedConditionFieldInfo(1);
        return processTypeDao.getProcessDefine(query);
    }

    //复制已有流程属性
    private void copyProperty(ProcessDefineDTO orig,ProcessDefineDetailDTO dest){
        if ((orig != null) && (dest != null)){
            if (dest.getId() == null){
                dest.setId(orig.getId());
            }
            if (dest.getDocumentation() == null){
                dest.setDocumentation(orig.getDocumentation());
            }
            if (dest.getType() == null){
                dest.setType(orig.getType());
            }
            if (StringUtils.isEmpty(dest.getName())){
                dest.setName(orig.getName());
            }
            if (StringUtils.isEmpty(dest.getConditionFieldId())){
                dest.setConditionFieldId(orig.getConditionFieldId());
            }
            if (StringUtils.isEmpty(dest.getUnit())){
                dest.setUnit(orig.getVarName());
            }
        }
        
    }
    
    @Override
    public ProcessDefineDetailDTO prepareProcessDefine(ProcessDetailPrepareDTO prepareRequest) throws Exception {
        //查找已有的流程设置
        TraceUtils.check(StringUtils.isNotEmpty(prepareRequest.getCurrentCompanyId()),"!currentCompanyId不能为空");
        TraceUtils.check(StringUtils.isNotEmpty(prepareRequest.getKey()),"!key不能为空");
        ProcessDefineDTO process = getProcessDefineByCompanyIdAndKey(prepareRequest.getCurrentCompanyId(),prepareRequest.getKey());

        //查找可以设置的条件列表
        FormFieldQueryDTO fieldQuery = BeanUtils.createFrom(prepareRequest,FormFieldQueryDTO.class);
        fieldQuery.setFormId(prepareRequest.getKey());
        fieldQuery.setToCondition(1);
        List<DynamicFormFieldDTO> fieldList = dynamicFormFieldDao.listFormField(fieldQuery);
        List<ConditionDTO> conditionList = convertToConditionList(fieldList);


        //如果没有设置type字段等信息，从数据库内读取流程属性，并补充相应信息
        if (isNeedFill(prepareRequest)) {
            if (ObjectUtils.isNotEmpty(process)){
                if (prepareRequest.getType() == null){
                    prepareRequest.setType(process.getType());
                }
                if (StringUtils.isEmpty(prepareRequest.getName())){
                    prepareRequest.setName(process.getName());
                }
                if (StringUtils.isEmpty(prepareRequest.getConditionFieldId())){
                    prepareRequest.setConditionFieldId(process.getConditionFieldId());
                }
                if (StringUtils.isEmpty(prepareRequest.getVarName()) && StringUtils.isEmpty(prepareRequest.getVarUnit())){
                    ConditionDTO firstCondition = ObjectUtils.getFirst(conditionList);
                    if (firstCondition != null) {
                        prepareRequest.setVarName(firstCondition.getName());
                        prepareRequest.setVarUnit(firstCondition.getUnit());
                    } else {
                        prepareRequest.setVarName(process.getVarName());
                        prepareRequest.setVarUnit(process.getVarUnit());
                    }
                }
            }
        }

        //从流程引擎中读取流程定义
        ProcessDefineDetailDTO processDefineDetail  = this.workflowService.prepareProcessDefine(prepareRequest);

        if(processDefineDetail!=null){
            //复制已有流程属性
            copyProperty(process,processDefineDetail);

            //设置可以设置的条件列表
            processDefineDetail.setOptionalConditionList(conditionList);

            //重新组织一下数据，设置人员头像
            this.setUserInfo(processDefineDetail);
            //添加变量名和单位
            processDefineDetail.setName(prepareRequest.getVarName());
            processDefineDetail.setUnit(prepareRequest.getVarUnit());
        }
        return processDefineDetail;
    }

    //转换为conditionList
    private List<ConditionDTO> convertToConditionList(List<DynamicFormFieldDTO> fieldList){
        List<ConditionDTO> conditionList = new ArrayList<>();
        for (DynamicFormFieldDTO field : fieldList) {
            ConditionDTO condition = BeanUtils.createFrom(field,ConditionDTO.class);
            condition.setName(field.getFieldTitle());
            condition.setUnit(field.getFieldUnit());
            conditionList.add(condition);
        }
        return conditionList;
    }

    //准备流程时的申请数据需要从数据库中补全
    private boolean isNeedFill(ProcessDetailPrepareDTO prepareRequest){
        return (prepareRequest.getType() == null)
                || (StringUtils.isEmpty(prepareRequest.getName()))
                || (StringUtils.isEmpty(prepareRequest.getConditionFieldId()))
                || (StringUtils.isEmpty(prepareRequest.getVarName()))
                || (StringUtils.isEmpty(prepareRequest.getVarUnit()));
    }

    //保存流程时的申请数据需要从数据库中补全
    private boolean isNeedFill(ProcessDefineDetailEditDTO editRequest){
        return (editRequest.getType() == null)
                || (StringUtils.isEmpty(editRequest.getName()))
                || (StringUtils.isEmpty(editRequest.getVarName()))
                || (StringUtils.isEmpty(editRequest.getVarUnit()));
    }

    private void setUserInfo(ProcessDefineDetailDTO processDefineDetail) {
        //重新组织一下数据，设置人员头像
        if(processDefineDetail!=null) {
            if (ObjectUtils.isNotEmpty(processDefineDetail.getFlowTaskGroupList())) {
                processDefineDetail.getFlowTaskGroupList().stream().forEach(task -> {
                    if (ObjectUtils.isNotEmpty(task.getFlowTaskList())) {
                        task.getFlowTaskList().stream().forEach(t -> {
                            if (t.getAssigneeUser() != null && StringUtils.isNotEmpty(t.getAssigneeUser().getId())) {
                                t.setAssigneeUser(this.getFlowUser(t.getAssigneeUser().getId()));
                            }
                            if (ObjectUtils.isNotEmpty(t.getCandidateUserList())) {
                                t.getCandidateUserList().stream().forEach(user -> {
                                    if (StringUtils.isNotEmpty(user.getId())) {
                                        this.getFlowUser(user);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public ProcessDefineDetailDTO changeProcessDefine(ProcessDefineDetailEditDTO editRequest) {
        //查找已有的流程设置
        TraceUtils.check(StringUtils.isNotEmpty(editRequest.getCurrentCompanyId()),"!currentCompanyId不能为空");
        TraceUtils.check(StringUtils.isNotEmpty(editRequest.getKey()),"!key不能为空");
        ProcessDefineDTO process = getProcessDefineByCompanyIdAndKey(editRequest.getCurrentCompanyId(),editRequest.getKey());

        if (isNeedFill(editRequest)){
            if (ObjectUtils.isNotEmpty(process)){
                if (editRequest.getType() == null){
                    editRequest.setType(process.getType());
                }
                if (StringUtils.isEmpty(editRequest.getName())){
                    editRequest.setName(process.getName());
                }
                if (StringUtils.isEmpty(editRequest.getConditionFieldId())){
                    editRequest.setConditionFieldId(process.getConditionFieldId());
                }
                if (StringUtils.isEmpty(editRequest.getVarName())){
                    editRequest.setVarName(process.getVarName());
                }
                if (StringUtils.isEmpty(editRequest.getVarUnit())) {
                    editRequest.setVarUnit(process.getVarUnit());
                }
            }
        }

        ProcessDefineDetailDTO processDefineDetail = workflowService.changeProcessDefine(editRequest);
        process = updateProcessType(editRequest);
        if(processDefineDetail!=null){
            //复制已有流程属性
            copyProperty(process,processDefineDetail);

            //重新组织一下数据，设置人员头像
            this.setUserInfo(processDefineDetail);
        }
        return processDefineDetail;
    }

    @Override
    public void deleteProcessDefine(ProcessDefineQueryDTO deleteRequest) {
        this.workflowService.deleteProcessDefine(deleteRequest);
        ProcessDefineDetailEditDTO request = BeanUtils.createFrom(deleteRequest,ProcessDefineDetailEditDTO.class);
        int type;
        if(ProcessTypeConst.PROCESS_TYPE_PROJECT_PAY_APPLY.equals(deleteRequest.getKey())){
            type = ProcessTypeConst.TYPE_NOT_PROCESS;
        } else {
            type = ProcessTypeConst.TYPE_FREE;
        }
        request.setType(type);

        //把类型库内流程设定为自由流程
        updateProcessType(request);
    }

    private ProcessDefineDTO updateProcessType(ProcessDefineDetailEditDTO editRequest){
        TraceUtils.check(editRequest != null);

        //计算要保存到数据库内的流程类型
        int type = getRealType(DigitUtils.parseInt(editRequest.getType()),editRequest.getKey(),editRequest.getConditionFieldId());

        //从数据库内读取当前的流程信息
        String companyId = editRequest.getCurrentCompanyId();
        String key = editRequest.getKey();
        TraceUtils.check(StringUtils.isNotEmpty(companyId),"!currentCompanyId不能为空");
        TraceUtils.check(StringUtils.isNotEmpty(key),"!key不能为空");
        ProcessTypeEntity typeEntity = processTypeDao
                .getCurrentProcessTypeByFormId(editRequest.getCurrentCompanyId(),editRequest.getKey());

        //如果数据库内没有流程信息，新建一条，否则更改已有的流程信息
        boolean found;
        if (typeEntity == null) {
            typeEntity = new ProcessTypeEntity();
            typeEntity.initEntity();
            typeEntity.setCreateBy(editRequest.getAccountId());
            found = false;
        } else {
            typeEntity.resetUpdateDate();
            typeEntity.setUpdateBy(editRequest.getAccountId());
            found = true;
        }

        //设置其他属性
        //所属组织
        typeEntity.setCompanyId(companyId);
        //设置此类型为启用状态
        typeEntity.setStatus(1);
        //设置此类型为未删除状态
        typeEntity.setDeleted(0);
        //设置流程关键字
        typeEntity.setTargetType(key);
        //设置类型
        typeEntity.setType(type);
        //设置条件字段，如果conditionFieldId为空，设置conditionFieldId为第一个条件，并且进行提示
        if ((DigitUtils.parseInt(editRequest.getType()) == ProcessTypeConst.PROCESS_TYPE_CONDITION) && (StringUtils.isEmpty(editRequest.getConditionFieldId()))) {
            //设置流程为条件流程，且没有指定conditionFieldId
            //查找可以设置的条件列表
            FormFieldQueryDTO fieldQuery = BeanUtils.createFrom(editRequest,FormFieldQueryDTO.class);
            fieldQuery.setFormId(editRequest.getKey());
            fieldQuery.setToCondition(1);
            List<DynamicFormFieldDTO> fieldList = dynamicFormFieldDao.listFormField(fieldQuery);
            if (ObjectUtils.isNotEmpty(fieldList)){
                DynamicFormFieldDTO firstField = ObjectUtils.getFirst(fieldList);
                if (firstField != null){
                    typeEntity.setConditionFieldId(firstField.getId());
                    TraceUtils.check(false,"!conditionFieldId在设置条件流程时不能为空，已设置conditionFieldId为默认");
                } else {
                    TraceUtils.check(false,"!conditionFieldId在设置条件流程时不能为空，且无法找到默认的conditionFieldId");
                }
            }
        } else {
            //正常设置条件字段
            typeEntity.setConditionFieldId(editRequest.getConditionFieldId());
        }

        if (found) {
            processTypeDao.updateById(typeEntity);
        } else {
            processTypeDao.insert(typeEntity);
        }

        return BeanUtils.createFrom(typeEntity,ProcessDefineDTO.class);
    }

    //检查并设置有效流程
    private int getRealType(int type, String key, String conditionFieldId){
        //如果不是有效流程类型
        if (isNotInvalid(type,conditionFieldId)){
            //对于付款申请，如果没有流程，则默认为无流程，其他的默认为自由流程
            if(ProcessTypeConst.PROCESS_TYPE_PROJECT_PAY_APPLY.equals(key)){
                type = ProcessTypeConst.TYPE_NOT_PROCESS;
            } else {
                type = ProcessTypeConst.TYPE_FREE;
            }
        }
        return type;
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


    //是否条件流程类型
    private boolean isConditionType(Integer type){
        return ProcessTypeConst.PROCESS_TYPE_CONDITION.equals(type);
    }

    private boolean isNotInvalid(int type, String conditionFieldId){
        return (type < ProcessTypeConst.TYPE_NOT_PROCESS)
                || (type > ProcessTypeConst.PROCESS_TYPE_CONDITION)
                || ((type == ProcessTypeConst.PROCESS_TYPE_CONDITION) && StringUtils.isEmpty(conditionFieldId));
    }

    //根据用户编号获取用户信息
    private FlowUserDTO getFlowUser(String companyUserId){
        String name = getUserName(companyUserId);
        String img = getUserHeadImg(companyUserId);
        return new FlowUserDTO(companyUserId,name,img);
    }

    //根据用户编号获取用户信息
    private void getFlowUser(FlowUserDTO user){
        String name = getUserName(user.getId());
        String img = getUserHeadImg(user.getId());
        user.setImgUrl(img);
        user.setName(name);
    }

    //获取用户头像
    private String getUserHeadImg(String companyUserId){
        String url = null;
        CompanyUserEntity companyUser = companyUserDao.selectById(companyUserId);
        if (companyUser != null){
            try {
                url = userAttachService.getHeadImgUrl(companyUser.getUserId());
            } catch (Exception e) {
                TraceUtils.check(false,"!参数错误");
            }
        }
        return url;
    }

    //获取用户名称
    private String getUserName(String companyUserId){
        return this.companyUserDao.getUserName(companyUserId);
    }
    /**
     *
     * @param businessKey:主业务id（比如报销单id）
     * @param dto：从前端传递过来的基础参数
     * @param isPass ：当前审批是否通过
     * @description ：完成该任务后，如果不是结束流程，则存在下一个任务，为下一个审批人增加一条审批记录(为了兼容原有的做法，并且不改动原来的查询)
     * 如果审核通过，并且已经结束流程，则把审批的主记录设置为审批通过状态
     * 如果没有审批通过，则把当前记录设置为退回状态
     */
    private boolean saveAudit(String businessKey, AuditBaseDTO dto, boolean isPass) throws Exception{
        boolean isContinueAudit = false;
        List<FlowTaskDTO> taskList = null;
        if(isPass){
            taskList = workflowService.listWorkTask(businessKey);
            //处理 getVariables ，创建下一个人要处理的任务，并且返回任务的id，可能是多个任务的id。把返回的任务id 放入 dto.getVariables() 中进行保存
            for(FlowTaskDTO task:taskList){
                // todo 处理审核记录
                SaveExpMainDTO saveExpMain = new SaveExpMainDTO();
                saveExpMain.setCompanyUserId(dto.getCurrentCompanyUserId());
                saveExpMain.setAuditPerson(task.getAssigneeUser().getId());
                saveExpMain.setAppOrgId(dto.getAppOrgId());
                saveExpMain.setAccountId(dto.getAccountId());
                saveExpMain.setId(businessKey);
                saveExpMain.setCurrentCompanyId(dto.getCurrentCompanyId());
                String auditId = this.expAuditService.saveAudit(saveExpMain);
                Map<String,Object> map = new HashMap<>();
                map.put(auditIdKey,auditId);
                workflowService.setTaskVariables(task.getId(),map);
                isContinueAudit = true;
            }
        }

        //没有任务，说明流程已经结束
        if(isPass && CollectionUtils.isEmpty(taskList)){
            ExpMainEntity exp = this.expMainService.selectById(businessKey);
            exp.setId(businessKey);
            exp.setApproveStatus("1");//审批通过的状态
            this.expMainService.updateById(exp);
            if(ExpenseConst.TYPE_PROJECT_PAY_APPLY.equals(exp.getType())){
                //推送任务
                ProjectCostPointDetailDTO projectCostPointDetailDTO = BeanUtils.createFrom(dto,ProjectCostPointDetailDTO.class);
                projectCostPointDetailDTO.setMainId(businessKey);
                projectCostPointDetailDTO.setFeeStatus(ProjectCostConst.FEE_STATUS_APPROVE);
                projectCostService.completeProjectFeeApply(projectCostPointDetailDTO);
            }
            this.expMainService.sendMessageForAudit(businessKey,dto.getCurrentCompanyId(),exp.getCompanyUserId(),exp.getType(),dto.getAccountId(),null,ProjectCostConst.FEE_STATUS_APPROVE+"");
        }
        if(!isPass){
            ExpMainEntity exp = this.expMainService.selectById(businessKey);
            exp.setId(businessKey);
            exp.setApproveStatus("2");//退回的状态
            this.expMainService.updateById(exp);
            if(ExpenseConst.TYPE_PROJECT_PAY_APPLY.equals(exp.getType())){
                //推送任务
                ProjectCostPointDetailDTO projectCostPointDetailDTO =  BeanUtils.createFrom(dto,ProjectCostPointDetailDTO.class);
                projectCostPointDetailDTO.setMainId(businessKey);
                projectCostPointDetailDTO.setFeeStatus(ProjectCostConst.FEE_STATUS_NOT_APPROVE);
                projectCostService.completeProjectFeeApply(projectCostPointDetailDTO);
            }
            this.expMainService.sendMessageForAudit(businessKey,dto.getCurrentCompanyId(),exp.getCompanyUserId(),exp.getType(),dto.getAccountId(),null,ProjectCostConst.FEE_STATUS_APPROVE+"");
        }
        return isContinueAudit;
    }

    /**
     * 根据类型获取流程的Key值
     */
    private String getProcessKey(String targetType,String companyId){
        //如果是自由流程，则启动系统中的默认的流程
        ProcessTypeEntity processType = this.processTypeDao.getCurrentProcessType(companyId,targetType);
        return getProcessKey(processType);
    }

    private String getProcessKey(ProcessTypeEntity processType ){
        //如果是自由流程，则启动系统中的默认的流程
        if(processType==null || ProcessTypeConst.TYPE_FREE == processType.getType()){
            return ProcessTypeConst.PROCESS_TYPE_FREE;
        }
        return "p_"+processType.getCompanyId()+"_"+processType.getTargetType()+"_"+processType.getType();
    }

    /**
     * 描述       查询所有流程定义，分组返回列表，组名为中文
     * 日期       2018/8/2
     * @author   张成亮
     * @param    query 流程查询条件
     * @return   分组流程列表
     **/
    @Override
    public List<ProcessDefineGroupDTO> listProcessDefineWithGroup(ProcessDefineQueryDTO query) throws Exception{
        //先做初始化
        dynamicFormGroupService.initDynamicFormGroup(query.getCurrentCompanyId());
        List<ProcessDefineGroupDTO> groupList = processTypeDao.listProcessDefineWithGroup(query);
        if (ObjectUtils.isNotEmpty(groupList)){
           // groupList.forEach(group->updateType(group.getProcessDefineList(),query.getCurrentCompanyId())); 不需要，不能更改id
            groupList.forEach(group->{
                group.getProcessDefineList().stream().forEach(processType->{
                    List<AuditCopyDataDTO> list = auditCopyService.listAuditCopy(processType.getId());
                    processType.setCopyList(list);
                });
            });
        }
        return groupList;
    }

    /**
     * 描述       查询流程定义
     * 日期       2018/9/13
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<ProcessDefineDTO> listProcessDefine(ProcessDefineQueryDTO query) {
        List<ProcessDefineDTO> list = processTypeDao.listProcessDefine(query);
        updateType(list,query.getCurrentCompanyId());
        return list;
    }


    //更新列表内的流程type值
    private void updateType(List<ProcessDefineDTO> pdList,String companyId){
        if (ObjectUtils.isNotEmpty(pdList)) {
            pdList.forEach(pd -> {
                ProcessTypeEntity type = processTypeDao.getCurrentProcessType(companyId, pd.getKey());
                if (type != null) {
                    pd.setType(type.getType());
                    pd.setId(StringUtils.lastLeft(pd.getId(), ProcessTypeConst.ID_SPLIT)
                            + ProcessTypeConst.ID_SPLIT
                            + pd.getType());
                }
            });
        }
    }

    /**
     * 签收任务
     */
    @Override
    public void claimTask(TaskDTO dto) throws Exception {
        WorkActionDTO actionDTO = new WorkActionDTO();
        actionDTO.setCompanyUserId(dto.getCompanyUserId());
        actionDTO.setId(dto.getTaskId());
        workflowService.claimWorkTask(actionDTO);

    }

    /**
     * 完成任务
     */
    @Override
    public void completeTask(TaskDTO dto) throws Exception {
      //  claimTask(dto);
        Map<String, Object> variable =  workflowService.getTaskVariables(dto.getTaskId());
        //处理当前任务 对应 myTask 表中的任务 先获取，再处理，myTask表中的id保存在activiti对应的任务的variable对应的myTaskId中
        if(variable.containsKey(auditIdKey) && !StringUtil.isNullOrEmpty(auditIdKey)){
            SaveExpMainDTO saveExpMain = new SaveExpMainDTO();
            saveExpMain.setId(dto.getBusinessKey());
            saveExpMain.setCompanyUserId(dto.getCompanyUserId());
            saveExpMain.setVersionNum(dto.getVersionNum());
            expAuditService.completeAudit(saveExpMain);
        }
        //以便在我的任务列表中，能获取到activiti中的任务。
        WorkActionDTO workAction = new WorkActionDTO();
        workAction.setId(dto.getTaskId());
        workAction.setResultMap(dto.getVariables());
        workflowService.completeWorkTask(workAction);
        //保存我的任务（当前任务完成，如果下一个节点不是结束节点，系统会自动为下一个节点产生一个任务，系统需要保存一条相应的审核记录中）
        this.saveAudit(dto.getBusinessKey(),dto,false);

    }


    /**
     * 完成任务
     */
    @Override
    public boolean completeTask2(TaskDTO dto) throws Exception {
        //  claimTask(dto);

        List<FlowTaskDTO> list = workflowService.listWorkTaskVariableValueEquals(auditIdKey,dto.getId());
        for(FlowTaskDTO task:list){
            //查询当前流程是否是自由流程
            String processKey = workflowService.getProcessKeyByTaskId(task.getId());
            WorkActionDTO workAction = new WorkActionDTO();
            workAction.setId(task.getId());
            //如果是自由流程，如果前端没有传递审批人，则直接结束流程
            if(ProcessTypeConst.PROCESS_TYPE_FREE.equals(processKey) && StringUtil.isNullOrEmpty(dto.getNextCompanyUserId())){
                workAction.setIsPass(ProcessTypeConst.NOT_PASS);
            }else if(StringUtil.isNullOrEmpty(dto.getApproveStatus()) || ProcessTypeConst.PASS.equals(dto.getApproveStatus())){
                workAction.setIsPass(ProcessTypeConst.PASS);
            }else {
                workAction.setIsPass(ProcessTypeConst.NOT_PASS);
            }
            dto.getVariables().put("approveUser",dto.getNextCompanyUserId());
            workAction.setResultMap(dto.getVariables());
            workAction.setCompanyUserId(dto.getCurrentCompanyUserId());
            if(task.getAssigneeUser()==null || task.getAssigneeUser().getId()==null){
                workAction.getResultMap().put("isNotSign","1");
            }
            workflowService.completeWorkTask(workAction);
            //保存我的任务（当前任务完成，如果下一个节点不是结束节点，系统会自动为下一个节点产生一个任务，系统需要保存一条相应的审核记录中）
            return this.saveAudit(dto.getBusinessKey(),dto, ProcessTypeConst.PASS.equals(dto.getApproveStatus()));
        }
        return false;
    }

    /**
     *
     * @param dto:mainId:审批主记录的id
     * @param dto:targetType 审批类型
     * @return 返回到前端的标识，1：代表是自由流程，需要前端传递审批人 0：代表不是自由流程，不需要前端传递审批人
     */
    /**
     *
     * @param dto:mainId:审批主记录的id
     * @param dto:targetType 审批类型
     * @return 返回到前端的标识，1：代表是自由流程，需要前端传递审批人 0：代表不是自由流程，不需要前端传递审批人,2:无流程
     */
    @Override
    public  Map<String,Object>  getCurrentProcess(AuditEditDTO dto) {
        ProcessDefDTO processDefine = null;
        Map<String,Object> result = new HashMap<>();
        result.put("processFlag","1");//返回到前端的标识，1：代表是自由流程，需要前端传递审批人
        result.put("conditionList",new ArrayList<>());//首先默认返回个空数组
        result.put("processType",ProcessTypeConst.TYPE_FREE);
        if(StringUtil.isNullOrEmpty(dto.getMainId())){
            ProcessTypeEntity processType = this.processTypeDao.getCurrentProcessType(dto.getAppOrgId(),dto.getAuditType());
            //如果是费用申请，特殊情况，如果是无流程的情况下。是不需要审批的，processFlag = 2 告诉前端不需要传递人员
            if(ProcessTypeConst.PROCESS_TYPE_PROJECT_PAY_APPLY.equals(dto.getAuditType())
                    && (processType==null || processType.getType()== ProcessTypeConst.TYPE_NOT_PROCESS)){
                result.put("processFlag","2");//返回到前端的标识，2:无流程
                result.put("processType",ProcessTypeConst.TYPE_NOT_PROCESS);
            }
            if(!"2".equals((String)result.get("processFlag"))){
                if(!(processType==null || ProcessTypeConst.TYPE_FREE == processType.getType())){
                    processDefine = workflowService.getProcessDefineIdByProcessKey(this.getProcessKey(dto.getAuditType(),dto.getAppOrgId()),dto.getAppOrgId());
                }
            }
        }else {
            ProcessInstanceRelationEntity instanceRelation = processInstanceRelationDao.getProcessInstanceRelation(dto.getMainId());
            if(instanceRelation!=null){
                processDefine = workflowService.getProcessDefineIdByProcessInstanceId(instanceRelation.getProcessInstanceId());
            }
        }
        if(processDefine!=null && !StringUtil.isNullOrEmpty(processDefine.getId()) && !processDefine.getKey().contains(ProcessTypeConst.PROCESS_TYPE_FREE)){
            List<UserTaskNodeDTO> userList = new ArrayList<>();
            ProcessDetailPrepareDTO detailPrepareDTO = new ProcessDetailPrepareDTO();
            detailPrepareDTO.setSrcProcessDefineId(processDefine.getId());
            List<UserTaskDTO> userTaskList = this.workflowService.listFlowTaskUser(detailPrepareDTO);
            //todo 重新封装
            userList = this.getUserList(userTaskList);
            result.put("processFlag","0");//代表不是自由流程，不需要前端传递审批人
            result.put("conditionList",userList);
            String key = processDefine.getKey();
            result.put("processType",key.substring(key.lastIndexOf("_")+1));
        }
        return result;
    }


    @Override
    public Map<String,Object> getCurrentTaskUser(AuditEditDTO dto, List<AuditDTO> auditList, String value) throws Exception{
        Map<String,Object>  result = getCurrentProcess(dto);
        String processType = result.get("processType").toString();
        if("0".equals(result.get("processFlag")) && !StringUtils.isEmpty(value)){//必须要判断type！=null，一下是用于处理已经产生的审批单的审批记录的数据
            List<UserTaskNodeDTO> taskList = (List<UserTaskNodeDTO>)result.get("conditionList");
            List<CompanyUserAppDTO> userList = this.getUserList(taskList,value,processType);
            int index = 0;
            if(!CollectionUtils.isEmpty(auditList)){//理论上是不会为空的
                index = auditList.size()-1;
            }else {
                auditList = new ArrayList<>();
            }
            for(int i = index;i<userList.size();i++){
                AuditDTO audit = new AuditDTO();
                BaseDTO.copyFields(userList.get(i),audit);
                audit.setApproveStatus("0");//待审批状态
                auditList.add(audit);
            }
        }
        return result;
    }


   private List<CompanyUserAppDTO> getUserList(List<UserTaskNodeDTO> taskList,String value,String processType){
       if(!CollectionUtils.isEmpty(taskList)) {
           if(ProcessTypeConst.TYPE_FIXED== Integer.parseInt(processType)){//固定流程的情况下
               return taskList.get(0).getUserList();
           }else {//是条件流程的情况下
               if(value==null){
                   throw new CustomException("参数错误");
               }
               for(UserTaskNodeDTO t:taskList){
                   if(isCurrentBrunchProcess(t,value)){
                       return t.getUserList();
                   }
               }
           }
       }
       return null;
   }

    private boolean isCurrentBrunchProcess(UserTaskNodeDTO t,String value){
        double v = Double.parseDouble(value);
        double max = (StringUtil.isNullOrEmpty(t.getMax()) || "null".equals(t.getMax()))?-1:Double.parseDouble(t.getMax());
        double min = (StringUtil.isNullOrEmpty(t.getMin()) || "null".equals(t.getMin()))?0:Double.parseDouble(t.getMin());
        if (max == -1) {
            //这里处理最大的值的情况下
            if (v >= min) {
                return true;
            }
        } else {
            if (v >= min && v < max) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<UserTaskNodeDTO> getUserListForAudit(AuditEditDTO dto) {
        List<UserTaskNodeDTO> userList = new ArrayList<>();
        ProcessDefDTO processDefine = null;
        if(StringUtil.isNullOrEmpty(dto.getMainId())){
            ProcessTypeEntity processType = this.processTypeDao.getCurrentProcessType(dto.getAppOrgId(),dto.getAuditType());
            if(processType==null || ProcessTypeConst.TYPE_FREE == processType.getType()){
                return userList;
            }else {
                processDefine = workflowService.getProcessDefineIdByProcessKey(this.getProcessKey(dto.getAuditType(),dto.getAppOrgId()),dto.getAppOrgId());
            }
        }else {
            ProcessInstanceRelationEntity instanceRelation = processInstanceRelationDao.getProcessInstanceRelation(dto.getMainId());
            if(instanceRelation!=null){
                processDefine = workflowService.getProcessDefineIdByProcessInstanceId(instanceRelation.getProcessInstanceId());
            }
        }
        if(processDefine!=null && !StringUtil.isNullOrEmpty(processDefine.getId()) && !processDefine.getKey().equals(ProcessTypeConst.PROCESS_TYPE_FREE)){
            ProcessDetailPrepareDTO detailPrepareDTO = new ProcessDetailPrepareDTO();
            detailPrepareDTO.setSrcProcessDefineId(processDefine.getId());
            List<UserTaskDTO> userTaskList = this.workflowService.listFlowTaskUser(detailPrepareDTO);
            //todo 重新封装
            userList = this.getUserList(userTaskList);
        }
        return userList;
    }

    @Override
    public int suspendProcess(SaveExpMainDTO dto) {
        //查询流程实例与审核单的关联关系
        ProcessInstanceRelationEntity instanceRelation = processInstanceRelationDao.getProcessInstanceRelation(dto.getId());
        if(instanceRelation!=null){
            workflowService.suspendProcessInstanceById(instanceRelation.getProcessInstanceId());
        }
        //对报审核单据进行状态设置
        ExpMainEntity entity = new ExpMainEntity();
        entity.setId(dto.getId());
        entity.setApproveStatus(ProjectCostConst.FEE_STATUS_REPEAL.toString());
        return this.expMainService.updateById(entity);
    }


    /**
     * 重新组织数据，根据id，把用户的信息获取，返回到前端
     */
    private List<UserTaskNodeDTO> getUserList(List<UserTaskDTO> userTaskList){
        List<UserTaskNodeDTO> list = new ArrayList<>();
        for (UserTaskDTO u : userTaskList) {
            UserTaskNodeDTO taskNode = new UserTaskNodeDTO();
            taskNode.setMax(getStringOrDefault(u.getMax(),null));
            taskNode.setMin(getStringOrDefault(u.getMin(),"0"));
            List<CompanyUserAppDTO> userList = new ArrayList<>();
            for (String assign : u.getAssignList()) {
                userList.add(companyUserDao.getCompanyUserDataById(assign));
            }
            taskNode.setUserList(userList);
            list.add(taskNode);
        }
        return list;
    }

    //返回值或默认值
    //由于数据有可能为字符串"null",增加了字符串"null"的判断
    private String getStringOrDefault(String value,String defaultValue){
        if ("null".equals(value)){
            return defaultValue;
        }
        return StringUtils.getStringOrDefault(value,defaultValue);
    }



}
