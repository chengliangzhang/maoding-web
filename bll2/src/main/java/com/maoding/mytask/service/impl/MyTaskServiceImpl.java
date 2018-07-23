package com.maoding.mytask.service.impl;

import com.maoding.conllaboration.SyncCmd;
import com.maoding.conllaboration.service.CollaborationService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.BaseShowDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.MyTaskRole;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.BeanUtilsEx;
import com.maoding.core.util.CommonUtil;
import com.maoding.core.util.StringUtil;
import com.maoding.deliver.dao.DeliverDao;
import com.maoding.deliver.entity.DeliverEntity;
import com.maoding.dynamic.service.DynamicService;
import com.maoding.financial.dao.ExpMainDao;
import com.maoding.financial.dto.ExpMainDTO;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import com.maoding.mytask.dao.MyTaskDao;
import com.maoding.mytask.dto.*;
import com.maoding.mytask.entity.MyTaskEntity;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyRelationAuditDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyRelationAuditEntity;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dao.ProjectProcessNodeDao;
import com.maoding.project.dto.DeliverEditDTO;
import com.maoding.project.dto.ProjectSkyDriverQueryDTO;
import com.maoding.project.dto.ResponseEditDTO;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectProcessNodeEntity;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import com.maoding.project.service.ProjectProcessService;
import com.maoding.project.service.ProjectService;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.projectcost.dao.ProjectCostPaymentDetailDao;
import com.maoding.projectcost.dao.ProjectCostPointDao;
import com.maoding.projectcost.dao.ProjectCostPointDetailDao;
import com.maoding.projectcost.dto.ProjectCostPaymentDetailDTO;
import com.maoding.projectcost.dto.ProjectCostPointDataForMyTaskDTO;
import com.maoding.projectcost.entity.ProjectCostEntity;
import com.maoding.projectcost.entity.ProjectCostPaymentDetailEntity;
import com.maoding.projectcost.entity.ProjectCostPointDetailEntity;
import com.maoding.projectcost.entity.ProjectCostPointEntity;
import com.maoding.projectcost.service.ProjectCostService;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.projectmember.service.ProjectMemberService;
import com.maoding.role.service.PermissionService;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.dto.ProjectTaskDataDTO;
import com.maoding.task.dto.TaskDescDTO;
import com.maoding.task.entity.ProjectTaskEntity;
import com.maoding.task.service.ProjectTaskService;
import com.maoding.user.dto.UserQueryDTO;
import com.maoding.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：MyTaskServiceImpl
 * 类描述：我的任务ServiceImpl
 * 作    者：MaoSF
 * 日    期：2016年11月30日-下午3:10:45
 */
@Service("myTaskService")
@Transactional(rollbackFor=Exception.class)
public class MyTaskServiceImpl extends GenericService<MyTaskEntity> implements MyTaskService {

    @Autowired
    private MyTaskDao myTaskDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private DeliverDao deliverDao;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ProjectTaskDao projectTaskDao;

    @Autowired
    private ProjectProcessNodeDao projectProcessNodeDao;

    @Autowired
    private ExpMainDao expMainDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private ProjectCostPaymentDetailDao projectCostPaymentDetailDao;

    @Autowired
    private ProjectCostPointDetailDao projectCostPointDetailDao;

    @Autowired
    private ProjectCostPointDao projectCostPointDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CompanyRelationAuditDao companyRelationAuditDao;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ProjectProcessService projectProcessService;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ProjectCostService projectCostService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private CollaborationService collaborationService;

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private CompanyService companyService;


    private String sperate = "<br/>";

    /**
     * 方法描述：根据参数查询我的任务（companyId,companyUserId)
     * 作者：MaoSF
     * 日期：2016/12/1
     * @param:
     * @return:
     */
    @Override
    public List<MyTaskEntity> getMyTaskByParamter(Map<String, Object> param) throws Exception {
        if (StringUtil.isNullOrEmpty(param.get("status"))) {
            param.put("status", "0");
        }
        return this.myTaskDao.getMyTaskByParam(param);
    }

    /**
     * 方法描述：根据参数查询我的任务（companyId,companyUserId)
     * 作者：MaoSF
     * 日期：2016/12/1
     * @param:
     * @return:
     */
    @Override

    public Map<String, Object> getMyTaskByParam(Map<String, Object> param) throws Exception {

        String companyId = (String) param.get("companyId");
        if (StringUtil.isNullOrEmpty(companyId)) {
            param.clear();
            param.put("data", new ArrayList<>());
            param.put("total", "0");
            return param;
        }
        Object pageIndex = param.get("pageIndex");
        if (null != pageIndex) {
            int page = (Integer) pageIndex;
            int pageSize = (Integer) param.get("pageSize");
            param.put("startPage", page * pageSize);
            param.put("endPage", pageSize);
        }
        if (StringUtil.isNullOrEmpty(param.get("status"))) {
            param.put("status", "0");
        }
        if (!StringUtil.isNullOrEmpty(param.get("viewType"))) {
            List<String> typeList = new ArrayList<String>();
            String viewType = (String) param.get("viewType");

            if ("1".equals(viewType)) {
                typeList.add("1");
                typeList.add("14");
            }
            if ("2".equals(viewType)) {
                typeList.add("2");
                typeList.add("13");
                typeList.add("15");
                typeList.add("22");
            }
            if ("3".equals(viewType)) {
                typeList.add("3");
            }
            if ("4".equals(viewType)) {
                typeList.add("4");
                typeList.add("5");
                typeList.add("6");
                typeList.add("7");
                typeList.add("16");
                typeList.add("18");
                typeList.add("20");
            }
            if ("5".equals(viewType)) {
                typeList.add("8");
                typeList.add("9");
                typeList.add("10");
                typeList.add("17");
                typeList.add("19");
                typeList.add("21");
            }
            if ("6".equals(viewType)) {
                typeList.add("11");
                typeList.add("12");
            }
            if (!CollectionUtils.isEmpty(typeList)) {
                param.put("typeList", typeList);
            }
        }
        List<MyTaskDTO> data = this.myTaskDao.getMyTaskListByParam(param);
        //重新组装数据
        this.convertMyTask(data);
        int total = this.myTaskDao.getMyTaskByParamCount(param);
        param.clear();
        param.put("data", data);
        param.put("total", total);
        if (null != pageIndex)
            param.put("pageIndex", (Integer) pageIndex);
        return param;
    }

    private void convertMyTask(List<MyTaskDTO> data) throws Exception {
        for (MyTaskDTO dto : data) {
            convertMyTask(dto, null, dto.getCompanyId());
        }
    }

    /**
     * 方法描述：myTaskEntity重新查找数据组装成dto
     * 作者：MaoSF
     * 日期：2017/1/6
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask(MyTaskDTO entity, String accountId, String companyId) throws Exception {
        int taskType = entity.getTaskType();

        //暂时未处理
        switch (taskType) {
            case 1:
            case 2:
                return convertMyTask1(entity);
            case 3:
                return convertMyTask3(entity);
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return convertMyTask4(entity);
            case 10:
            case 20:
            case 21:
                return convertMyTask10(entity);
            case 11:
                return convertMyTask11(entity);
            case 13:
                return convertMyTask13(entity);
            case 12:
                return entity;
            case 14:
                return convertMyTask14(entity);
            case 15:
                return convertMyTask15(entity);
            case 16:
            case 17:
            case 18:
            case 19:
                return convertMyTask16(entity);
            case 22:
                return convertMyTask22(entity);
            default:
                return null;
        }
    }

    /**
     * 方法描述：根据参数查询我的任务（companyId,companyUserId，projectId)
     * 作者：MaoSF
     * 日期：2016/12/1
     * @param:
     * @return:
     */
    @Override
    public List<MyTaskEntity> getMyTaskByProjectId(String projectId, String companyId, String userId) throws Exception {
        CompanyUserEntity companyUser = this.companyUserDao.getCompanyUserByUserIdAndCompanyId(userId, companyId);
        if (companyUser != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("handlerId", companyUser.getId());
            param.put("companyId", companyId);
            param.put("projectId", projectId);
            param.put("status", "0");
            return this.myTaskDao.getMyTaskByParam(param);
        }
        return null;
    }

    /**
     * 方法描述：任务经营人，任务技术负责人
     * 作者：MaoSF
     * 日期：2017/1/9
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask1(MyTaskDTO dto) throws Exception {

        ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
        if (projectEntity != null) {
            if (!dto.getCompanyId().equals(projectEntity.getCompanyId())) {
                dto.setTaskMemo("合作设计项目");
            }
            dto.setTaskTitle(projectEntity.getProjectName());
            if (dto.getTaskType() == 1) {
                dto.setTaskContent("你被指定为项目经营负责人");
            }
            if (dto.getTaskType() == 2) {
                dto.setTaskContent("你被指定为项目设计负责人");
            }
        }
        return dto;
    }

    /**
     * 方法描述：任务负责人的任务，设计人提交流程，项目经营人,配置权限进入的界面(taskType=3,4,5)
     * 作者：MaoSF
     * 日期：2017/1/9
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask3(MyTaskDTO dto) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
        if (projectEntity != null) {
            ProjectTaskDataDTO taskEntity = this.projectTaskDao.getProjectTaskById(dto.getParam1(), dto.getCompanyId());
            dto.setTaskTitle(projectEntity.getProjectName());
            String taskMemo = "";
            if (taskEntity != null) {
                dto.setTaskTitle2(taskEntity.getTaskName());
                String startTime = taskEntity.getStartTime();
                String endTime = taskEntity.getEndTime();
                this.projectService.setProjectTaskState(taskEntity);
                if (!StringUtil.isNullOrEmpty(startTime)) {
                    taskMemo += startTime + "~" + endTime;
                    if (!StringUtil.isNullOrEmpty(taskEntity.getStateHtml())) {
                        taskMemo += taskEntity.getStateHtml();
                    }
                }
                dto.setDescription(taskEntity.getTaskRemark());
            }
            dto.setTaskMemo(taskMemo);
            dto.setTaskContent("你被指定为该任务的" + dto.getTaskContent() + "人");
        }
        return dto;
    }

    /**
     * 方法描述：项目经营人收款，付款确认(taskType=4，6)
     * 作者：MaoSF
     * 日期：2017/1/9
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask4(MyTaskDTO dto) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
        if (projectEntity != null) {
            dto.setTaskTitle(projectEntity.getProjectName());
            ProjectCostPointDetailEntity pointDetailEntity = this.projectCostPointDetailDao.selectById(dto.getTargetId());
            if (pointDetailEntity != null) {
                ProjectCostPointEntity pointEntity = this.projectCostPointDao.selectById(pointDetailEntity.getPointId());
                dto.setTaskContent(pointEntity.getFeeDescription() + sperate + "金额：" + StringUtil.getRealData(pointDetailEntity.getFee()) + "万元，需要你确认付款。");
                dto.setDescription(pointDetailEntity.getInvoice());
                dto.setFee(pointDetailEntity.getFee().toString());
                double sumFee = this.projectCostPaymentDetailDao.getSumFee(pointDetailEntity.getId());
                dto.setPaymentFee(sumFee + "");
                dto.setNotReceiveFee(StringUtil.getRealData(pointDetailEntity.getFee().subtract(new BigDecimal(sumFee + ""))));
            }
        }

        return dto;
    }

    /**
     * 方法描述：type=10，合同回款，财务到款操作
     * 作者：MaoSF
     * 日期：2017/1/9
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask10(MyTaskDTO dto) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
        if (projectEntity != null) {
            dto.setTaskTitle(projectEntity.getProjectName());
            ProjectCostPointDetailEntity pointDetailEntity = this.projectCostPointDetailDao.selectById(dto.getTargetId());
            if (pointDetailEntity != null) {
                ProjectCostPointEntity pointEntity = this.projectCostPointDao.selectById(pointDetailEntity.getPointId());
                if (dto.getTaskType() == 20) {//其他费用－付款
                    dto.setTaskContent(pointEntity.getFeeDescription() + sperate + "金额：" + StringUtil.getRealData(pointDetailEntity.getFee()) + "万元，开始付款，请进行付款操作。");
                } else {
                    dto.setTaskContent(pointEntity.getFeeDescription() + sperate + "金额：" + StringUtil.getRealData(pointDetailEntity.getFee()) + "万元，开始收款，请进行到账操作。");
                }
                dto.setDescription(pointDetailEntity.getInvoice());
                dto.setFee(pointDetailEntity.getFee().toString());
                double sumFee = this.projectCostPaymentDetailDao.getSumFee(pointDetailEntity.getId());
                dto.setPaymentFee(sumFee + "");
                dto.setNotReceiveFee(StringUtil.getRealData(pointDetailEntity.getFee().subtract(new BigDecimal(sumFee + ""))));
            }
        }
        return dto;
    }


    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2017/1/9
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask11(MyTaskDTO dto) throws Exception {
        dto.setTaskMemo(expMainDao.selectById(dto.getTargetId()).getExpNo());
        return dto;
    }

    /**
     * 方法描述：任务负责人的任务，设计人提交流程，项目经营人,配置权限进入的界面(taskType=1,2)
     * 作者：MaoSF
     * 日期：2017/1/9
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask13(MyTaskDTO dto) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
        if (projectEntity != null) {
            dto.setTaskTitle(projectEntity.getProjectName());
            String taskMemo = "";
            if (!dto.getCompanyId().equals(projectEntity.getCompanyId())) {
                taskMemo = "合作设计项目 ";
            }
            String taskName = this.projectTaskDao.getTaskParentName(dto.getTargetId());
            if (taskName != null) {
                taskMemo += taskName;
            }
            ProjectTaskEntity taskEntity = this.projectTaskDao.selectById(dto.getTargetId());
            if (taskEntity != null) {
                dto.setDescription(taskEntity.getTaskRemark());
            }
            dto.setTaskMemo(taskMemo);
            dto.setTaskContent("你被指定为项目任务负责人");
        }

        return dto;
    }

    /**
     * 方法描述：经营负责人指定设计负责人的任务
     * 作者：MaoSF
     * 日期：2017/1/9
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask14(MyTaskDTO dto) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
        if (projectEntity != null) {
            dto.setTaskTitle(projectEntity.getProjectName());
            String taskMemo = "";
            if (!dto.getCompanyId().equals(projectEntity.getCompanyId())) {
                taskMemo = "合作设计项目 ";
            }
            String taskName = this.projectTaskDao.getTaskParentName(dto.getTargetId());
            if (taskName != null) {
                taskMemo += taskName;
            }
            dto.setTaskMemo(taskMemo);
            dto.setTaskContent("你被指定为经营负责人，请指定设计负责人");
        }

        return dto;
    }

    /**
     * 方法描述：安排任务负责人
     * 作者：MaoSF
     * 日期：2017/1/9
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask15(MyTaskDTO dto) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
        if (projectEntity != null) {
            dto.setTaskTitle(projectEntity.getProjectName());
            String taskMemo = "";
            if (!dto.getCompanyId().equals(projectEntity.getCompanyId())) {
                taskMemo = "合作设计项目 ";
            }
            String taskName = this.projectTaskDao.getTaskParentName(dto.getTargetId());
            if (taskName != null) {
                taskMemo += taskName;
            }
            dto.setTaskMemo(taskMemo);
            dto.setTaskContent("请指定任务负责人");
        }

        return dto;
    }


    /**
     * 方法描述：技术审查费，合作设计费，财务--到款，付款
     * 作者：MaoSF
     * 日期：2017/1/9
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask16(MyTaskDTO dto) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
        if (projectEntity != null) {
            dto.setTaskTitle(projectEntity.getProjectName());
            ProjectCostPaymentDetailEntity paymentDetailEntity = this.projectCostPaymentDetailDao.selectById(dto.getTargetId());
            if (paymentDetailEntity != null) {
                ProjectCostPointEntity pointEntity = null;
                String pointId = dto.getParam1();
                if (StringUtil.isNullOrEmpty(dto.getParam1())) {//防止旧的数据没有设置pointID到param1字段中，
                    ProjectCostPointDetailEntity pointDetailEntity = this.projectCostPointDetailDao.selectById(paymentDetailEntity.getPointDetailId());
                    pointId = pointDetailEntity == null ? null : pointDetailEntity.getPointId();
                }
                pointEntity = this.projectCostPointDao.selectById(pointId);//在 保存的时候，把pointID保存在param1.方便查询
                if (pointEntity != null) {
                    String payType = "付款";
                    if (dto.getTaskType() == 17 || dto.getTaskType() == 19) {
                        payType = "到账";
                    }
                    dto.setTaskContent(pointEntity.getFeeDescription() + sperate + "金额：" + StringUtil.getRealData(paymentDetailEntity.getFee()) + "万元，已" + payType + "，请进行相关操作。");
                    dto.setFee(paymentDetailEntity.getFee().toString());
                }
            }
        }

        return dto;
    }


    /**
     * 方法描述：type=22，设计任务完成后，给设计负责人推送的任务
     * 作者：MaoSF
     * 日期：2017/1/9
     * @param:
     * @return:
     */
    private MyTaskDTO convertMyTask22(MyTaskDTO dto) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
        ProjectTaskEntity taskEntity = this.projectTaskDao.selectById(dto.getTargetId());
        if (projectEntity != null && taskEntity != null) {
            dto.setTaskTitle(projectEntity.getProjectName());
            List<ProjectTaskEntity> taskList = this.projectTaskDao.getProjectTaskByPid(dto.getTargetId());
            String taskContent = "";
            if (!CollectionUtils.isEmpty(taskList)) {
                for (ProjectTaskEntity task : taskList) {
                    taskContent += "“" + task.getTaskName() + "”,";
                }
            }
            if (!StringUtil.isNullOrEmpty(taskContent)) {
                dto.setTaskContent("设计任务" + taskContent.substring(0, taskContent.length() - 1) + "已全部完成，是否确认" + taskEntity.getTaskName() + "已完成。");
            } else {
                dto.setTaskContent("设计任务" + taskEntity.getTaskName() + "已完成。");
            }
        }
        return dto;
    }

    /**
     * 方法描述：技术审查费付款确认，合作技术费付款确认（taskType=4 or 6）
     * 作者：MaoSF
     * 日期：2016/12/21
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage saveMyTask(String targetId, int taskType, String companyId,String createBy,String currentCompanyId) throws Exception {
        ProjectCostPointDetailEntity detailEntity = this.projectCostPointDetailDao.selectById(targetId);
        if (detailEntity != null) {
            if (taskType == 4 || taskType == 6) {
                this.saveMyTaskFor4Or6(targetId, taskType, companyId, detailEntity.getProjectId(),createBy,currentCompanyId);
            }

            if (taskType == 10 || taskType == 20 || taskType == 21) {
                return saveMyTask(targetId, companyId, taskType, null, detailEntity.getProjectId(),createBy,currentCompanyId);
            }
        }
        return null;
    }

    /**
     * 方法描述：taskType = 4 or 6
     * 作者：MaoSF
     * 日期：2017/5/2
     * @param:
     * @return:
     */
    private void saveMyTaskFor4Or6(String targetId, int taskType, String companyId, String projectId,String createBy,String currentCompanyId) throws Exception {
        List<ProjectMemberEntity> managerList = this.getProjectOperatorManager(projectId, companyId);
        if (!CollectionUtils.isEmpty(managerList)) {
            String companyUserIds = "";
            for(ProjectMemberEntity managerEntity:managerList){
                if(!companyUserIds.contains(managerEntity.getCompanyUserId())){
                    companyUserIds+=managerEntity.getCompanyUserId()+",";
                    MyTaskEntity taskEntity = this.getMyTaskEntity(targetId, taskType);
                    taskEntity.setId(StringUtil.buildUUID());
                    taskEntity.setCompanyId(companyId);
                    taskEntity.setTaskType(taskType);
                    taskEntity.setHandlerId(managerEntity.getCompanyUserId());
                    taskEntity.setCreateBy(createBy);
                    taskEntity.setSendCompanyId(currentCompanyId);
                    taskEntity.setCreateDate(new Date());
                    this.myTaskDao.insert(taskEntity);
                    MessageEntity m = this.getMessage(taskEntity);
                    if(m!=null){
                        m.setSendCompanyId(currentCompanyId);
                        this.messageService.sendMessage(m);
                    }
                }
            }
        }
    }


    private List<ProjectMemberEntity> getProjectOperatorManager(String projectId, String companyId) throws Exception {
        List<ProjectMemberEntity> list = new ArrayList<>();
        ProjectMemberEntity member = this.projectMemberService.getOperatorManager(projectId, companyId);
        ProjectMemberEntity assistant = this.projectMemberService.getOperatorAssistant(projectId, companyId);
        if(member!=null){
            list.add(member);
        }
        if(assistant!=null){
            list.add(assistant);
        }
        return list;
    }

    /**
     * 方法描述：保存我的任务(合同回款发起，给财务任务,合作设计费，技术审查费：经营负责人确认付款后，给财务人员推送到款，付款的任务)
     * filed1:为技术审查费，合作设计费，财务到款，付款的保存的maoding_web_project_cost_point中的id，方便查询
     * 作者：MaoSF
     * 日期：2017/3/6
     */
    private AjaxMessage saveMyTask(String targetId, String companyId, int taskType, String pointId, String projectId,String createBy,String currentCompanyId) throws Exception {
        String type = null;
        companyId = companyService.getFinancialHandleCompanyId(companyId);
        List<CompanyUserTableDTO> companyUserList = null;
        if(taskType==SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAY
                || taskType==SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAY
                || taskType==SystemParameters.OTHER_FEE_FOR_PAY){
            companyUserList = this.companyUserService.getFinancialManager(companyId);
            type = MyTaskRole.FINANCE_PAY;
        }
        if(taskType==SystemParameters.CONTRACT_FEE_PAYMENT_CONFIRM
                || taskType==SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAID
                || taskType==SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAID
                || taskType==SystemParameters.OTHER_FEE_FOR_PAID){
            companyUserList = this.companyUserService.getFinancialManagerForReceive(companyId);
            type = MyTaskRole.FINANCE_RECEIVE;
        }
        MyTaskEntity taskEntity = this.getMyTaskEntity(targetId, taskType);
        //插入空的数据，用于新增财务人员，展现财务任务
        taskEntity.setId(StringUtil.buildUUID());
        taskEntity.setProjectId(projectId);
        taskEntity.setCompanyId(companyId);
        taskEntity.setTaskType(taskType);
        taskEntity.setParam1(pointId);//保存所在收款节点，便于后面查询
        taskEntity.setParam3(type);
        taskEntity.setCreateBy(createBy);
        taskEntity.setSendCompanyId(currentCompanyId);
        taskEntity.setCreateDate(new Date());
        this.myTaskDao.insert(taskEntity);
        if (!CollectionUtils.isEmpty(companyUserList)) {
            for (CompanyUserTableDTO dto : companyUserList) {
                taskEntity.setHandlerId(dto.getId());
                MessageEntity m = this.getMessage(taskEntity);
                if(m!=null){
                    m.setSendCompanyId(currentCompanyId);
                    this.messageService.sendMessage(m);
                }
            }
        }
        return AjaxMessage.succeed(companyId);
    }

    /**
     * 方法描述：保存我的任务（直接推送给人的）（1.设置流程后，发送给设计人。2.报销单审核后，推送审核任务。）
     * 作者：MaoSF
     * 日期：2016/12/21
     */
    @Override
    public AjaxMessage saveMyTask(String targetId, int taskType, String companyId, String companyUserId,String createBy,String currentCompanyId,boolean isAgreeAndTrans) throws Exception {
       //推送消息，所有在下面设置false
        return saveMyTask(targetId, taskType, companyId, companyUserId, false,createBy,currentCompanyId);
    }

    private void sendMessageForMyTask(MyTaskEntity taskEntity,String currentCompanyId) {
        MessageEntity msg = getMessage(taskEntity);
        if(msg==null) return;
        msg.setSendCompanyId(currentCompanyId);
        if (msg.getMessageType() == SystemParameters.MESSAGE_TYPE_5 || msg.getMessageType() == SystemParameters.MESSAGE_TYPE_6) {
            List<String> taskList = projectTaskDao.getProjectTaskOfToCompany(taskEntity.getTargetId(), taskEntity.getCompanyId());
            if ((taskList != null) && (taskList.size() > 0)) {
                for (String taskId : taskList) {
                    msg.setTargetId(taskId);
                    messageService.sendMessage(msg);
                }
            } else {
                messageService.sendMessage(msg);
            }
        } else {
            messageService.sendMessage(msg);
        }
    }

    /**
     * 方法描述：保存我的任务（直接推送给人的）
     * 作者：MaoSF
     * 日期：2016/12/21
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage saveMyTask(String targetId, int taskType, String companyId, String companyUserId, boolean isSendMessage,String createBy,String currentCompanyId) throws Exception {
        if (taskType == SystemParameters.PRODUCT_TASK_DESIGN) {//目前屏蔽技术负责人的任务
            return null;
        }
        MyTaskEntity taskEntity = this.getMyTaskEntity(targetId, taskType);
        if (null == taskEntity) {
            taskEntity = new MyTaskEntity();
        }
        taskEntity.setTargetId(targetId);
        taskEntity.setTaskType(taskType);
        taskEntity.setCompanyId(companyId);
        taskEntity.setHandlerId(companyUserId);
        taskEntity.setId(StringUtil.buildUUID());
        taskEntity.setCreateBy(createBy);
        taskEntity.setSendCompanyId(currentCompanyId);
        taskEntity.setCreateDate(new Date());
        this.myTaskDao.insert(taskEntity);
        if (isSendMessage) {
            sendMessageForMyTask(taskEntity,currentCompanyId);
        }
        saveProductTask(taskEntity,taskType,createBy,currentCompanyId);
        return null;
    }

    /**
     * 方法描述：推送生产安排的任务，如果已经存在生产安排，则不推送
     * 作者：MaoSF
     * 日期：2017/6/30
     */
    private void saveProductTask(MyTaskEntity taskEntity,int taskType,String createBy,String currentCompanyId) throws Exception{
        if(taskType==SystemParameters.PRODUCT_TASK_RESPONSE){//生产安排的任务，任务负责人的任务
            Map<String,Object> map = new HashMap<>();
            map.put("projectId",taskEntity.getProjectId());
            map.put("handlerId",taskEntity.getHandlerId());
            map.put("taskType",SystemParameters.PRODUCT_TASK);
            if(CollectionUtils.isEmpty(this.getMyTaskByParamter(map))){ //不存在有效的生产安排个人任务
                //添加生产安排的个人任务
                this.ignoreMyTask(taskEntity.getTargetId(),SystemParameters.PRODUCT_TASK,null);
                MyTaskEntity newProductTask = new MyTaskEntity(); //使用新建的对象代替原有对象，使输入的个人任务可以继续被激活
                BeanUtilsEx.copyProperties(taskEntity,newProductTask);
                newProductTask.setId(StringUtil.buildUUID());
                newProductTask.setTaskType(SystemParameters.PRODUCT_TASK);
                newProductTask.setCreateBy(createBy);
                newProductTask.setSendCompanyId(currentCompanyId);
                this.myTaskDao.insert(newProductTask);
            }
        }
    }
    /**
     * 方法描述：保存我的任务（更换系统中某特定职务的人之后，把所有的任务移交给新人）
     * 作者：MaoSF
     * 日期：2016/12/21
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage saveMyTask(MyTaskEntity entity, boolean isSendMessage) throws Exception {
        entity.setId(StringUtil.buildUUID());
        this.myTaskDao.insert(entity);
        if (isSendMessage) {
            this.sendMessageForMyTask(entity,entity.getSendCompanyId());
        }
        return null;
    }


    /************************任务类型(1.签发：经营负责人,2.生产安排（项目设计负责人）.
     * 3.设计（设计，校对，审核），
     * 4.付款（技术审查费-确认付款款（经营负责人）），5.付款（技术审查费-确认付款款（企业负责人）），6.付款（合作设计费-付款确认（经营负责人）），7.付款（合作设计费-付款确认（企业负责人）），
     * 8.到款（技术审查费-确认到款），9.到款（合作设计费-到款确认）10.到款（合同回款-到款确认）
     11.报销单审核,12.同意邀请合作伙伴)***********************/

    private MyTaskEntity getMyTaskEntity(String targetId, int taskType) throws Exception {
        switch (taskType) {
            case 1:
            case 2:
            case 13:
            case 14:
            case 15:
            case 22:
                return taskEntity1(targetId);
            case 3:
                return taskEntity3(targetId);
            case 4:
            case 5:
            case 6:
            case 7:
                return taskEntity4(targetId);
            case 8:
            case 9:
            case 10:
            case 20:
            case 21:
                return taskEntity8(targetId);
            case 11:
            case 23:
                return taskEntity11(targetId,taskType);
            case 12:
                return taskEntity12(targetId);
            case 16:
            case 17:
            case 18:
            case 19:
                return taskEntity16(targetId);
            default:
                return null;
        }
    }


    /**
     * 方法描述：签发任务（项目立项，签发时候给经营负责人推送任务）2.生产安排（项目设计负责人）22,安排经营负责人（targetId:projectId or taskId)
     * 作者：MaoSF
     * 日期：2016/12/22
     * @param:type=1 or type = 2 or type =13 or type=14
     * @return:
     */
    private MyTaskEntity taskEntity1(String targetId) {
        MyTaskEntity myTask = new MyTaskEntity();
        ProjectEntity project = this.projectDao.selectById(targetId);
        if (project != null) {
            myTask.setProjectId(project.getId());
            myTask.setTargetId(targetId);
            return myTask;
        } else {
            ProjectTaskEntity projectTaskEntity = this.projectTaskDao.selectById(targetId);
            myTask.setProjectId(projectTaskEntity.getProjectId());
            myTask.setTargetId(targetId);
            return myTask;
        }
    }


    /**
     * 方法描述：targetId(为流程节点的id)（新建流程时候，对设计人发送任务） 比如：（设计：卯丁科技大厦 方案设计），设计，校对，审定
     * 作者：MaoSF
     * 日期：2016/12/21
     * @param:
     * @return:
     */
    private MyTaskEntity taskEntity3(String targetId) {
        MyTaskEntity myTask = new MyTaskEntity();
        ProjectProcessNodeEntity projectProcessNode = this.projectProcessNodeDao.selectById(targetId);
        ProjectTaskEntity projectTask = this.projectTaskDao.selectById(projectProcessNode.getProcessId());//目前兼容一下。后期移除process，taskId直接关联到node记录中
        if (projectTask != null) {
            ProjectEntity project = this.projectDao.selectById(projectTask.getProjectId());
            if (project != null) {
                myTask.setProjectId(project.getId());
                myTask.setTaskTitle(project.getProjectName());
                myTask.setTargetId(targetId);
                myTask.setTaskContent(projectProcessNode.getNodeName());
                myTask.setParam1(projectTask.getId());//保存所在任务的id，便于后面查询
                return myTask;
            }
        }
        return null;
    }

    /**
     * 方法描述：付款（技术审查费-确认付款款（经营负责人））
     * 作者：MaoSF
     * 日期：2016/12/22
     * @param:gargetId为maoding_web_project_cost_detail表中的id
     * @return: type=4 or type =5; type = 6 or type = 7
     */
    private MyTaskEntity taskEntity4(String targetId) {
        MyTaskEntity myTask = new MyTaskEntity();
        ProjectCostPointDetailEntity pointDetailEntity = this.projectCostPointDetailDao.selectById(targetId);
        if (pointDetailEntity != null) {
            ProjectCostPointEntity pointEntity = this.projectCostPointDao.selectById(pointDetailEntity.getPointId());
            if (pointEntity != null) {
                myTask.setProjectId(pointEntity.getProjectId());
                myTask.setTargetId(targetId);
                myTask.setTaskContent(pointEntity.getFeeDescription() + sperate + "金额：" + StringUtil.getRealData(pointDetailEntity.getFee()) + "万元");
                return myTask;
            }
        }
        return null;
    }


    /**
     * 方法描述：付款（技术审查费-确认付款款（经营负责人））
     * 作者：MaoSF
     * 日期：2016/12/22
     * @param:gargetId为maoding_web_project_cost_detail表中的id
     * @return: type=8 or type =9; type = 10
     */
    private MyTaskEntity taskEntity8(String targetId) {
        MyTaskEntity myTask = new MyTaskEntity();
        ProjectCostPointDetailEntity pointDetailEntity = projectCostPointDetailDao.selectById(targetId);
        if (pointDetailEntity != null) {
            ProjectCostPointEntity pointEntity = this.projectCostPointDao.selectById(pointDetailEntity.getPointId());
            if (pointEntity != null) {
                myTask.setProjectId(pointEntity.getProjectId());
                myTask.setTargetId(targetId);
                myTask.setTaskContent(pointEntity.getFeeDescription() + sperate + "金额：" + StringUtil.getRealData(pointDetailEntity.getFee()) + "，收到账项后请及时确认");
                return myTask;
            }
        }
        return null;
    }


    /**
     * 方法描述：targetId(为报销单的id)
     * 作者：MaoSF
     * 日期：2016/12/21
     * @param:
     * @return:
     */
    private MyTaskEntity taskEntity11(String targetId,Integer type) {
        MyTaskEntity myTask = new MyTaskEntity();
        ExpMainDTO expMainDTO = this.expMainDao.getByMainIdForMyTask(targetId);
        if (expMainDTO != null) {
            if(type == SystemParameters.EXP_AUDIT){
                myTask.setTaskTitle(expMainDTO.getUserName() + "的报销单");
            }else {
                myTask.setTaskTitle(expMainDTO.getUserName() + "的费用申请");
            }
            myTask.setTaskContent(expMainDTO.getExpTypeName() + " 总金额：" + StringUtil.getRealData(expMainDTO.getExpSumAmount()) + "元");
            return myTask;
        }
        return null;
    }


    /**
     * 方法描述：申请加入成为合作伙伴
     * 作者：MaoSF
     * 日期：2016/12/21
     */
    private MyTaskEntity taskEntity12(String targetId) {
        MyTaskEntity myTask = new MyTaskEntity();
        CompanyRelationAuditEntity companyRelationAuditEntity = this.companyRelationAuditDao.selectById(targetId);
        if (null != companyRelationAuditEntity) {
            CompanyEntity companyEntity = this.companyDao.selectById(companyRelationAuditEntity.getOrgId());
            myTask.setTaskTitle(companyEntity.getCompanyName());
            myTask.setTaskContent("事业合伙人申请");
            return myTask;
        }
        return null;
    }

    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2016/12/21
     */
    private MyTaskEntity taskEntity16(String targetId) {
        MyTaskEntity myTask = new MyTaskEntity();
        myTask.setTargetId(targetId);
        ProjectCostPaymentDetailEntity projectCostPaymentDetailEntity = this.projectCostPaymentDetailDao.selectById(targetId);
        if (projectCostPaymentDetailEntity != null) {
            myTask.setProjectId(projectCostPaymentDetailEntity.getProjectId());
        }
        return myTask;
    }


    /**
     * 方法描述：根据targetId修改状态
     * 作者：MaoSF
     * 日期：2016/12/1
     */
    @Override
    public int updateStatesByTargetId(Map<String, Object> paraMap) {
        return myTaskDao.updateStatesByTargetId(paraMap);
    }


    /**
     * 方法描述：忽略我的任务
     * 作者：MaoSF
     * 日期：2016/12/21
     */
    @Override
    public AjaxMessage ignoreMyTask(String targetId, int taskType, String companyUserId) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("targetId",targetId);
        map.put("taskType",taskType);
        map.put("handlerId",companyUserId);
        this.myTaskDao.deleteMyTask(map);
        return AjaxMessage.succeed(null);
    }

    /**
     * 忽略 一个项目在某个公司下的所有任务（删除任务使用）
     */
    @Override
    public AjaxMessage ignoreMyTask(String projectId, Integer taskType, String companyId ,String companyUserId) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("taskType",taskType);
        map.put("companyId",companyId);
        map.put("handlerId",companyUserId);
        this.myTaskDao.deleteMyTask(map);
        return AjaxMessage.succeed(null);
    }

    /**
     * 方法描述：忽略我的任务（删除了该节点后，任务全部忽略）
     * 作者：MaoSF
     * 日期：2016/12/21
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage ignoreMyTask(String targetId) throws Exception {
        this.myTaskDao.deleteProjectTask(targetId);//此处做兼容处理，把param4设置为1
        return null;
    }

    /**
     * 方法描述：忽略我的任务（用于忽略分解任务的个人任务）
     * 作者：Zhangchengliang
     * 日期：2017/7/24
     *
     * @param projectId 任务负责人所在项目ID
     * @param companyId 任务负责人所在组织ID
     * @param handlerId 任务负责人雇员ID
     */
    @Override
    public AjaxMessage ignoreMyTaskForResponsible(String projectId, String companyId, String handlerId) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("companyId",companyId);
        map.put("handlerId",handlerId);
        map.put("taskType",SystemParameters.PRODUCT_TASK);
        this.myTaskDao.deleteMyTask(map);
        return AjaxMessage.succeed(null);
    }


    /**
     * 方法描述：处理我的任务
     * 作者：MaoSF
     * 日期：2016/12/21
     */
    @Override
    public AjaxMessage handleMyTask(HandleMyTaskDTO dto) throws Exception {
//        try{
            MyTaskEntity myTaskEntity = this.selectById(dto.getId());
            if(myTaskEntity!=null && "!".equals(myTaskEntity.getParam4())){
                return AjaxMessage.failed("任务已被完成或已失效");
            }
            if (null != myTaskEntity) {
                String status = dto.getStatus();
                String currentUserId = dto.getAccountId();
                String result = dto.getResult();
                int taskType = myTaskEntity.getTaskType();
                dto.setProjectId(myTaskEntity.getProjectId());
                switch (taskType) {
                    case 1:
                    case 2:
                        return handleType1(myTaskEntity, status, currentUserId);
                    case 13:
                        return handleType13(myTaskEntity, dto);
                    case 3:
                        return handleType3(myTaskEntity, status, currentUserId);
                    case 4:
                    case 6:
                        return handleType4(myTaskEntity, result, currentUserId);//todo 此接口需要走流程
                    case 22: //确认签发完成
                        return handleType22(myTaskEntity, result, currentUserId);
                    case 10:
                    case 20:
                    case 21:
                        return handleType10(myTaskEntity, result, status, currentUserId, dto.getPaidDate());
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                        return handleType16(myTaskEntity, result, status, currentUserId, dto.getPaidDate());
                    case MyTaskEntity.DELIVER_CONFIRM_FINISH:
                        handleMyTaskDeliverResponse(myTaskEntity,dto);
                        return AjaxMessage.succeed(null);
                    default:
                        return null;
                }
            }
//        }catch (Exception e){
//            e.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//        }
        return AjaxMessage.failed("操作失败");
    }

    /**
     * @author  张成亮
     * @date    2018/7/19
     * @description     处理交付任务
     * @param   deliver 交付任务
     **/
    private void handleMyTaskDeliver(DeliverEntity deliver, DeliverEditDTO request){
        if (isTrue(request.getIsFinished())){
            completeMyTaskDeliver(deliver);
        } else {
            deliver.setStatus("0");
            deliverDao.updateById(deliver);
        }
    }


    /**
     * @author  张成亮
     * @date    2018/7/18
     * @description     处理交付负责人任务
     * @param   myTask 要处理的个人任务
     * @param   param 处理任务时的参数
     **/
    private void handleMyTaskDeliverResponse(MyTaskEntity myTask, HandleMyTaskDTO param){
        if (isComplete(param)){
            completeMyTaskDeliverResponse(myTask,param);
        }
    }

    //完成负责人的交付任务
    private void completeMyTaskDeliverResponse(MyTaskEntity myTask, HandleMyTaskDTO param){
        //标记此所有任务完成
        finishMyTask(myTask);

        //如果已经此交付目录没有负责人要从事的交付任务了
        MyTaskQueryDTO query = new MyTaskQueryDTO();
        query.setCompanyId(myTask.getCompanyId());
        query.setTaskId(myTask.getTargetId());
        query.setMyTaskType(MyTaskEntity.DELIVER_CONFIRM_FINISH);
        query.setStatus(0);
        List<MyTaskEntity> list = myTaskDao.listEntityByQuery(query);
        if (ObjectUtils.isEmpty(list) && !com.maoding.core.util.StringUtils.isEmpty(myTask.getTargetId())) {
            //标记交付任务完成
            DeliverEntity deliver = deliverDao.selectById(myTask.getTargetId());
            if (deliver != null) {
                completeMyTaskDeliver(deliver);
            }
        }
    }

    //完成总的交付任务
    private void completeMyTaskDeliver(DeliverEntity deliver){
        //标记交付任务完成
        deliver.setStatus("1");
        deliverDao.updateById(deliver);

        //标记所有交付任务的所有子任务完成 （包括所有属于此交付任务的负责人确认任务和执行交付任务，targetId等于此交付任务的id)
        MyTaskEntity changed = new MyTaskEntity();
        changed.setStatus("1"); //完成标志
        MyTaskQueryDTO changedQuery = new MyTaskQueryDTO();
        changedQuery.setCompanyId(deliver.getCompanyId());
        changedQuery.setTaskId(deliver.getId());
        myTaskDao.updateByQuery(changed,changedQuery);
    }

    //判断处理任务参数是激活还是完成
    private boolean isComplete(HandleMyTaskDTO param){
        return "1".equals(param.getCompletion());
    }

    /**
     * 方法描述：处理type =1,2,3,13（完成，开始按钮操作，直接改变状态）
     * 作者：MaoSF
     * 日期：2017/1/17
     * @param:
     * @return:
     */
    private AjaxMessage handleType1(MyTaskEntity myTask, String status, String accountId) throws Exception {
//        //TODO 更新projectMemberStatus状态，便于统计
//        updateProjectMemberStatus(myTask);
        return AjaxMessage.succeed("操作成功");
    }

    /**
     * 方法描述：处理type =3（完成，开始按钮操作，直接改变状态）
     * 作者：MaoSF
     * 日期：2017/1/17
     * @param:
     * @return:
     */
    private AjaxMessage handleType3(MyTaskEntity myTask, String status, String accountId) throws Exception {
        AjaxMessage ajaxMessage = this.projectProcessService.completeProjectProcessNode(myTask.getProjectId(), myTask.getCompanyId(), myTask.getTargetId(), myTask.getParam1(), accountId);
        if (ajaxMessage != null && "0".equals(ajaxMessage.getCode())) {
            this.finishMyTask(myTask.getId());
        }
        return ajaxMessage;
    }


    private AjaxMessage finishMyTask(String id){
        return finishMyTask(id,"1");
    }

    private AjaxMessage finishMyTask(String id,String param2){
        if(StringUtil.isNullOrEmpty(id)){
            return AjaxMessage.error("操作失败");
        }
        MyTaskEntity entity = new MyTaskEntity();
        entity.setId(id);
        entity.setStatus("1");
        if(StringUtil.isNullOrEmpty(param2)){
            entity.setParam2("1");
        }else {
            entity.setParam2(param2);
        }
        entity.setCompleteDate(new Date());
        myTaskDao.updateById(entity);
        return AjaxMessage.succeed(null);
    }

    /**
     * 方法描述：处理任务完成
     * 作者：MaoSF
     * 日期：2017/3/12
     */
    private AjaxMessage handleType13(MyTaskEntity myTask, HandleMyTaskDTO dto) throws Exception {
        dto.setProjectId(myTask.getProjectId());
        //1.判断是否存在设校审，并且是是否完成
        AjaxMessage ajaxMessage = this.projectTaskService.completeProductTask(dto);
        if (ajaxMessage != null && "0".equals(ajaxMessage.getCode())) {
            //处理我的任务
            this.finishMyTask(myTask.getId());
            //判断，当前人所负责的任务是否全部完成，如果完成，则把生产安排给完成
            if(CollectionUtils.isEmpty(projectTaskDao.getMyResponsibleTask(myTask.getProjectId(),myTask.getCompanyId(),myTask.getHandlerId(),"1"))){
                //重新使用myTask对象,把生产安排的任务设置为完成状态
                myTask.setStatus("1");
                myTask.setTargetId(null);
                myTask.setTaskType(SystemParameters.PRODUCT_TASK);
                myTaskDao.updateStatesByTargetId(myTask);
            }

          //  updateProjectMemberStatus(myTask);
            return AjaxMessage.succeed("操作成功");
        }
        return ajaxMessage;
    }

    /**
     * 方法描述：经营负责人处理技术审查费（合作设计费）付款确认 type=4，6
     * 作者：MaoSF
     * 日期：2017/1/17
     * @param:
     * @return:
     */
    private AjaxMessage handleType4(MyTaskEntity myTask, String result, String accountId) throws Exception {

        //新增记录,调用费控模块的接口，新增收款（付款）记录
        ProjectCostPaymentDetailDTO detailDTO = new ProjectCostPaymentDetailDTO();
        detailDTO.setPointDetailId(myTask.getTargetId());
        detailDTO.setCurrentCompanyUserId(myTask.getHandlerId());
        detailDTO.setFee(new BigDecimal(result));
        detailDTO.setAccountId(accountId);
        detailDTO.setCurrentCompanyId(myTask.getCompanyId());
        detailDTO.setTaskType(myTask.getTaskType());
        AjaxMessage ajaxMessage = this.projectCostService.saveCostPaymentDetail(detailDTO);
        if ("1".equals(ajaxMessage.getCode())) {//如果失败，则返回
            return ajaxMessage;
        }

        //2。给双方财务推送付款，到款的任务
        Map<String, Object> data = (Map<String, Object>) ajaxMessage.getData();
        if (data != null) {
            ProjectCostEntity costEntity = this.projectCostService.selectById(data.get("costId"));
            String pointId = (String) data.get("pointId");
            //技术审查费推送任务
            if ("2".equals(costEntity.getType())) {
                ajaxMessage = this.saveMyTask((String) data.get("paymentDetailId"), costEntity.getToCompanyId(), SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAID, pointId, costEntity.getProjectId(),accountId,myTask.getCompanyId());
                String handCompanyId = (String)ajaxMessage.getData();
                if(!companyService.getFinancialHandleCompanyId(costEntity.getFromCompanyId()).equals(handCompanyId)){
                     this.saveMyTask((String) data.get("paymentDetailId"), costEntity.getFromCompanyId(), SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAY, pointId, costEntity.getProjectId(),accountId,myTask.getCompanyId());
                }
            }
            //合作设计费推送任务
            if ("3".equals(costEntity.getType())) {
                String handleFromCompanyId = companyService.getFinancialHandleCompanyId(costEntity.getFromCompanyId());
                String handleToCompanyId = companyService.getFinancialHandleCompanyId(costEntity.getToCompanyId());
                if(handleFromCompanyId.equals(handleToCompanyId)){
                    if(handleToCompanyId.equals(costEntity.getToCompanyId())){
                        this.saveMyTask((String) data.get("paymentDetailId"), handleToCompanyId, SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAID, pointId, costEntity.getProjectId(), accountId, myTask.getCompanyId());
                    }else {
                        this.saveMyTask((String) data.get("paymentDetailId"), handleFromCompanyId, SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAY, pointId, costEntity.getProjectId(),accountId,myTask.getCompanyId());
                    }
                }else {
                    this.saveMyTask((String) data.get("paymentDetailId"), handleFromCompanyId, SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAY, pointId, costEntity.getProjectId(),accountId,myTask.getCompanyId());
                    this.saveMyTask((String) data.get("paymentDetailId"), handleToCompanyId, SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAID, pointId, costEntity.getProjectId(), accountId, myTask.getCompanyId());
                }
            }
        }
        //查询是否已经收款完全
        double sumFee = this.projectCostPaymentDetailDao.getSumFee(myTask.getTargetId());
        ProjectCostPointDetailEntity pointDetail = this.projectCostPointDetailDao.selectById(myTask.getTargetId());
        //如果已经收款完成，则把任务取消
        if (pointDetail != null && CommonUtil.doubleCompare(pointDetail.getFee().doubleValue(), sumFee) <= 0) {
            finishMyTask(myTask);
        }
        return AjaxMessage.succeed("操作成功");
    }


    /**
     * 方法描述：合同回款，其他费用财务到款、付款确认 type=10，20,21
     * 作者：MaoSF
     * 日期：2017/1/17
     */
    private AjaxMessage handleType10(MyTaskEntity myTask, String result, String status, String accountId, String paidDate) throws Exception {
        //验证身份
        AjaxMessage ajaxMessage = validateIdentity(myTask,accountId);
        if(ajaxMessage!=null){
            return ajaxMessage;
        }
        CompanyUserEntity handler = companyUserDao.getCompanyUserByUserIdAndCompanyId(accountId,myTask.getCompanyId());
        if(handler==null){
            return AjaxMessage.failed("参数错误");
        }
        //新增记录,调用
        ProjectCostPaymentDetailDTO detailDTO = new ProjectCostPaymentDetailDTO();
        detailDTO.setPointDetailId(myTask.getTargetId());
        if (null != myTask && myTask.getTaskType() == 20) {//其他费用－付款
            detailDTO.setPayDate(paidDate);
        } else {
            detailDTO.setPaidDate(paidDate);
        }
        detailDTO.setCurrentCompanyUserId(handler.getId());
        detailDTO.setFee(new BigDecimal(result));
        detailDTO.setCurrentCompanyId(myTask.getCompanyId());
        detailDTO.setAccountId(accountId);
        detailDTO.setTaskType(myTask.getTaskType());
        ajaxMessage = this.projectCostService.saveCostPaymentDetail(detailDTO);
        if ("1".equals(ajaxMessage.getCode())) {//如果失败，则返回
            return ajaxMessage;
        }

        //查询是否已经收款完全
        double sumFee = this.projectCostPaymentDetailDao.getSumFee(myTask.getTargetId());
        ProjectCostPointDetailEntity pointDetail = this.projectCostPointDetailDao.selectById(myTask.getTargetId());
        //如果已经收款完成，则把任务取消
        if (pointDetail != null && CommonUtil.doubleCompare(pointDetail.getFee().doubleValue(), sumFee) <= 0) {
            myTask.setHandlerId(handler.getId());
            finishMyTask(myTask);
        }

        return AjaxMessage.succeed("操作成功");

    }

    private AjaxMessage validateIdentity(MyTaskEntity myTask,String accountId){
        switch (myTask.getTaskType()){
            case SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAY:
            case SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAY:
            case SystemParameters.OTHER_FEE_FOR_PAY:
                if (!permissionService.isFinancial(myTask.getCompanyId(),accountId)) {
                    return AjaxMessage.failed("你无权限操作");
                }
                return null;
            case SystemParameters.CONTRACT_FEE_PAYMENT_CONFIRM:
            case SystemParameters.TECHNICAL_REVIEW_FEE_FOR_PAID:
            case SystemParameters.COOPERATIVE_DESIGN_FEE_FOR_PAID:
            case SystemParameters.OTHER_FEE_FOR_PAID:
                if (!permissionService.isFinancialReceive(myTask.getCompanyId(),accountId)) {
                    return AjaxMessage.failed("你无权限操作");
                }
                return null;
            default:return null;
        }
    }

    /**
     * 方法描述：结束我的任务，合并原有的重复代码生成
     * 设置任务状态为1，并把相关任务的状态设置为2
     * 作者：ZCL
     * 日期：2017/5/5
     */
    @Override
    public void finishMyTask(MyTaskEntity myTask) {
        //3.处理我的任务
        this.finishMyTask(myTask.getId());

        //如果不是确认交付完成任务
        if (!isDeliverResponseType(myTask)) {
            //忽略其他人员的任务
            Map<String, Object> map = new HashMap<>();
            map.put("param4", "1");
            map.put("targetId", myTask.getTargetId());
            map.put("projectId", myTask.getProjectId());
            map.put("companyId", myTask.getCompanyId());
            map.put("taskType", myTask.getTaskType());
            this.updateStatesByTargetId(map);
        } else {
            //否则，是确认交付完成任务
            //同时完成同负责人、同签发任务的上传任务
            finishDeliverUpload(myTask);
            //如果是最后一个确认交付完成任务
            if (isLastDeliverResponseType(myTask)) {
                //通告交付任务创建者上传任务已完成
                notifyDeliverFinished(myTask);
            }
        }
    }

    //判断是否确认交付完成任务
    private boolean isDeliverResponseType(MyTaskEntity myTask){
        return Integer.valueOf(MyTaskEntity.DELIVER_CONFIRM_FINISH)
                .equals(myTask.getTaskType());
    }

    //判断是否最后一个确认交付完成任务
    private boolean isLastDeliverResponseType(MyTaskEntity myTask){
        //查询同项目、同公司、targetId相同，类型为确认交付完成任务，且状态为未完成的任务
        Map<String, Object> query = new HashMap<>();
        query.put("projectId",myTask.getProjectId());
        query.put("companyId",myTask.getCompanyId());
        query.put("targetId",myTask.getTargetId());
        query.put("taskType",MyTaskEntity.DELIVER_CONFIRM_FINISH);
        query.put("status","0");
        List<MyTaskEntity> list = myTaskDao.getMyTaskByParam(query);
        return ObjectUtils.isEmpty(list);
    }

    //完成同负责人、同签发任务的上传任务
    private void finishDeliverUpload(MyTaskEntity myTask){
        //查询同项目、同公司、targetId相同，类型为上传任务，且状态为未完成的任务
        Map<String, Object> query = new HashMap<>();
        query.put("projectId",myTask.getProjectId());
        query.put("companyId",myTask.getCompanyId());
        query.put("targetId",myTask.getTargetId());
        query.put("taskType",MyTaskEntity.DELIVER_EXECUTE);
        query.put("status","0");
        List<MyTaskEntity> list = myTaskDao.getMyTaskByParam(query);
        if (!ObjectUtils.isEmpty(list)){
            for (MyTaskEntity entity : list) {
                finishMyTask(entity.getId());
            }
        }
    }

    //通告交付任务创建者上传任务已完成
    private void notifyDeliverFinished(MyTaskEntity myTask){
        MessageEntity message = getMessage(myTask);
        messageService.sendMessage(message);
    }


    /**
     * 方法描述：财务到款确认 type=16-19（合作设计费，技术审查费，双方财务付款到款操作）
     * 作者：MaoSF
     * 日期：2017/1/17
     * @param:
     * @return:
     */
    private AjaxMessage handleType16(MyTaskEntity myTask, String result, String status, String accountId, String paidDate) throws Exception {
        //验证身份
        AjaxMessage ajaxMessage = validateIdentity(myTask,accountId);
        if(ajaxMessage!=null){
            return ajaxMessage;
        }

        CompanyUserEntity handler = companyUserDao.getCompanyUserByUserIdAndCompanyId(accountId,myTask.getCompanyId());
        if(handler==null){
            return AjaxMessage.failed("参数错误");
        }

        ProjectCostPaymentDetailDTO detailDTO = new ProjectCostPaymentDetailDTO();
        detailDTO.setId(myTask.getTargetId());

        detailDTO.setCurrentCompanyUserId(handler.getId());
        if (myTask.getTaskType() == 16 || myTask.getTaskType() == 18) {
            detailDTO.setPayDate(paidDate);
        }
        if (myTask.getTaskType() == 17 || myTask.getTaskType() == 19) {
            detailDTO.setPaidDate(paidDate);
        }
        detailDTO.setProjectId(myTask.getProjectId());
        detailDTO.setCurrentCompanyId(myTask.getCompanyId());
        detailDTO.setAccountId(accountId);
        detailDTO.setTaskType(myTask.getTaskType());
        ajaxMessage = this.projectCostService.saveCostPaymentDetail(detailDTO);
        if ("1".equals(ajaxMessage.getCode())) {//如果失败，则返回
            return ajaxMessage;
        }
        //2.处理我的任务
        myTask.setHandlerId(handler.getId());
        this.finishMyTask(myTask);
        return AjaxMessage.succeed("操作成功");
    }


    /**
     * 方法描述：完成签发任务
     * 作者：ZCL
     * 日期：2017/5/21
     */
    private AjaxMessage handleType22(MyTaskEntity myTask, String status, String accountId) throws Exception {
        //// TODO: 2017/5/21 完成签发任务
        //保留原始数据
        MyTaskEntity origin = new MyTaskEntity();
        BeanUtilsEx.copyProperties(myTask,origin);
        //处理我的任务
        this.finishMyTask(myTask);
        this.projectTaskService.handleProjectTaskCompleteDate(myTask.getProjectId(),myTask.getCompanyId(), accountId);
        //给经营负责人发送消息
      //  messageService.sendMessage(origin,myTask,null,myTask.getProjectId(),myTask.getCompanyId(),accountId);
        return AjaxMessage.succeed("操作成功");
    }


    private MessageEntity getMessage(MyTaskEntity task) {
        CompanyUserEntity userEntity = this.companyUserDao.selectById(task.getHandlerId());
        if (userEntity == null || userEntity.getUserId().equals(task.getCreateBy())) {
            return null;
        }
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setProjectId(task.getProjectId());
        messageEntity.setCompanyId(task.getCompanyId());
        messageEntity.setTargetId(task.getTargetId());
        messageEntity.setParam1(task.getParam1());
        messageEntity.setUserId(userEntity.getUserId());
        messageEntity.setCreateBy(task.getCreateBy());
        messageEntity.setCreateDate(task.getCreateDate());
        messageEntity.setSendCompanyId(task.getCompanyId());
        switch (task.getTaskType()) {
            case 1:
            case 2:
                getMessageType1(messageEntity, task.getTaskType());
                break;
            case 3:
                messageEntity.setTargetId(task.getParam1());//保存任务的id到 targetId中
                messageEntity.setParam1(task.getTargetId());
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_10);
                break;
            case 13:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_7);
                break;
            case 4:
            case 5:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_11);
                break;
            case 6:
            case 7:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_14);
                break;
            case 8:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_12);
                break;
            case 9:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_15);
                break;
            case 10:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_17);
                break;
            case 11:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_19);
                break;
            case 23:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_222);
                break;
            case 16:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_23);
                break;
            case 17:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_24);
                break;
            case 18:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_25);
                break;
            case 19:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_26);
                break;
            case 20:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_31);
                break;
            case 21:
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_32);
                break;
            case MyTaskEntity.DELIVER_CONFIRM_FINISH:
                messageEntity.setUserId(task.getCreateBy());
                messageEntity.setSendCompanyId(task.getSendCompanyId());
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_DELIVER_FINISHED);
                break;
        }
        return messageEntity;
    }

    private void getMessageType1(MessageEntity messageEntity, int taskType) {
        int messageType = 0;
        ProjectEntity projectEntity = this.projectDao.selectById(messageEntity.getProjectId());
        if (1 == taskType) {//移交经营负责人
            if (projectEntity.getCompanyId().equals(messageEntity.getCompanyId())) {
                messageType = SystemParameters.MESSAGE_TYPE_301;
            } else if (!StringUtil.isNullOrEmpty(projectEntity.getCompanyBid())
                    && !projectEntity.getCompanyId().equals(projectEntity.getCompanyBid())
                    && projectEntity.getCompanyBid().equals(messageEntity.getCompanyId())) {
                messageType = SystemParameters.MESSAGE_TYPE_1;
            } else {
                messageType = SystemParameters.MESSAGE_TYPE_5;
            }

        } else {
            if (projectEntity.getCompanyId().equals(messageEntity.getCompanyId())) {
                messageType = SystemParameters.MESSAGE_TYPE_4;
            } else if (!StringUtil.isNullOrEmpty(projectEntity.getCompanyBid())
                    && !projectEntity.getCompanyId().equals(projectEntity.getCompanyBid())
                    && projectEntity.getCompanyBid().equals(messageEntity.getCompanyId())) {
                messageType = SystemParameters.MESSAGE_TYPE_2;
            } else {
                messageType = SystemParameters.MESSAGE_TYPE_6;
            }
        }
        messageEntity.setMessageType(messageType);
        messageEntity.setMessageContent(projectEntity.getProjectName());
    }


    /**
     * 作用：激活已完成的任务
     * 作者：ZCL
     * 日期：2017-5-20
     * @param dto 激活申请
     * @return 0：正常激活，-1：无法激活
     */
    @Override
    public String activeMyTask(MyTaskActiveRequestDTO dto) throws Exception {
        //取出个人任务信息
        MyTaskEntity myTask = myTaskDao.selectById(dto.getMyTaskId());
        if (myTask == null) return "数据错误";

        //取相应的生产任务信息
        ProjectTaskEntity pTask = null;
        if (myTask.getTaskType() == 13) {
            pTask = projectTaskDao.selectById(myTask.getTargetId());
        } else if (myTask.getTaskType() == 3) {
            pTask = projectTaskDao.selectById(myTask.getParam1());
        }
        if (pTask == null) return "数据错误";

        //判断父任务是否完成，如果完成则不允许继续操作
        ProjectTaskEntity parent = projectTaskDao.selectById(pTask.getTaskPid());
        if (parent != null) {
            if (parent.getCompleteDate() != null) return "父任务已完成";
        }

        //获取此任务的任务负责人
        ProjectMemberEntity taskResponsible = this.projectMemberService.getProjectMember(pTask.getProjectId(), pTask.getCompanyId(), ProjectMemberType.PROJECT_TASK_RESPONSIBLE, pTask.getId());
        if (taskResponsible == null) return "数据错误";
        String taskResponsiblerId = taskResponsible.getCompanyUserId();

        if ((myTask.getTaskType() == 13) && (dto.getCurrentCompanyUserId().equals(taskResponsiblerId))) { //如果申请时任务负责人要激活生产任务
            projectTaskDao.resetTaskCompleteStatus(pTask.getId());

            //生产安排任务
            this.saveProductTask(myTask,myTask.getTaskType(),dto.getUserId(),dto.getCompanyId());

            //设置修改过的数据
            ProjectTaskEntity targetTask = new ProjectTaskEntity();
            BeanUtilsEx.copyProperties(pTask,targetTask);
            targetTask.setCompleteDate(null);
            //保存项目动态
            dynamicService.addDynamic(pTask,targetTask,dto.getCompanyId(),dto.getUserId());
            //如果是生产的根任务激活，如果存在
            if (pTask.getTaskType() == 2 && !StringUtil.isNullOrEmpty(pTask.getTaskPid())) {
                this.ignoreMyTask(pTask.getTaskPid(), SystemParameters.TASK_COMPLETE, null);
            }
        } else if ((myTask.getTaskType() == 3) && (pTask.getCompleteDate() == null)) { //如果要激活的是设计、校验、审核，且任务尚未完成
            //保留原始数据
            ProjectProcessNodeEntity originNode = projectProcessNodeDao.selectById(myTask.getTargetId());
            projectTaskDao.resetProcessNodeCompleteStatus(myTask.getTargetId());
            //设置修改过的数据
            ProjectProcessNodeEntity targetNode = new ProjectProcessNodeEntity();
            BeanUtilsEx.copyProperties(originNode,targetNode);
            targetNode.setCompleteTime(null);
            //保存项目动态
            dynamicService.addDynamic(originNode,targetNode,dto.getCompanyId(),dto.getUserId());
        } else if ((myTask.getTaskType() == 3) && (pTask.getCompleteDate() != null)) {//如果要激活的是设计、校验、审核，且任务已经完成
            // to do 如果任务负责人和申请人是同一人，先恢复生产任务,再激活设计任务
            return "任务负责人已确认完成，如需激活请联系任务负责人";
        } else { //其他情况
            return "激活失败";
        }

        //激活个人任务
        myTask.setParam2("0");
        myTask.setStatus("0");
        myTaskDao.updateById(myTask);

        //通知协同
        this.collaborationService.pushSyncCMD_PT(pTask.getProjectId(),pTask.getTaskPath(),SyncCmd.PT2);

        return "激活成功";
    }

    /** 作用：激活已完成的任务
     * 作者：ZCL
     * 日期：2017-5-20
            * @param dto 激活申请
     * @return 0：正常激活，-1：无法激活
     */
    @Override
    public String activeMyTask2(MyTaskActiveRequestDTO dto) throws Exception{

        Map<String,Object> map = new HashMap<>();
        map.put("ignoreStatus","1");
        map.put("status","1");
        map.put("handlerId",dto.getCurrentCompanyUserId());
        map.put("targetId",dto.getTaskId());
        map.put("taskType","13");
        List<MyTaskEntity> list = this.getMyTaskByParamter(map);
        if(CollectionUtils.isEmpty(list)){
            return "数据错误";
        }
        dto.setMyTaskId(list.get(0).getId());
        return activeMyTask(dto);
    }
    /**
     * 方法描述：查询任务列表(按照项目分组)
     * 作者：MaoSF
     * 日期：2017/05/04
     */
    @Override
    public List<MyTaskListDTO> getMyTaskList(Map<String, Object> param) throws Exception {

        if (null != param.get("pageIndex")) {
            int page = (Integer) param.get("pageIndex");
            int pageSize = (Integer) param.get("pageSize");
            param.put("startPage", page * pageSize);
            param.put("endPage", pageSize);
        }
        List<MyTaskListDTO> list = this.myTaskDao.getMyTaskList(param);
        for(MyTaskListDTO dto:list){
            dto.setMyTaskList(convertMyTask2(dto.getTaskList()));
            dto.setTaskList(null);
        }
        return list;
    }

    /**
     * 方法描述：查询任务列表
     * 作者：MaoSF
     * 日期：2017/05/04
     *
     * @param param
     */
    @Override
    public List<MyTaskListDTO> getMyTaskList2(Map<String, Object> param) throws Exception {
        initParam(param);
        List<MyTaskListDTO> list = this.myTaskDao.getMyTaskList2(param);
        return list;
    }

    @Override
    public Map<String,Object> getMyTaskList4(Map<String, Object> param) throws Exception {
        initParam(param);
//        if(StringUtil.isNullOrEmpty(param.get("status"))){
//            param.put("status","0");
//        }
        List<MyTaskList2DTO> list = this.myTaskDao.getMyTaskList4(param);
        int total = myTaskDao.getMyTaskByParamCount(param);
        for(MyTaskList2DTO dto:list){
            if(dto.getType()>0){
                dto.setTaskTypeText(projectTaskDao.getStateText(dto.getTaskState(),dto.getPlanStartTime(),dto.getPlanEndTime(),dto.getCompleteDate()));
            }
            dto.setRole(this.getRole(dto));
            dto.setTaskName(this.getTaskName(dto.getTaskType(),dto.getProjectName(),dto.getTaskName(),dto.getTaskContent()));
            getMyTaskDesc(dto);

            //为交付执行任务填充相关目录编号和目录名
            if (isDeliverType(dto.getTaskType())) {
                BaseShowDTO dir = getDirInfo(dto.getTargetId());
                dto.setTargetId(dir.getId());
            }
        }

        long time4 = System.currentTimeMillis();
        param.clear();
        param.put("total",total);
        param.put("data", list);
        return param;
    }
    public String getTaskName(int taskType,String projectName,String taskName,String taskContent) {
        //暂时未处理
        switch (taskType) {
            case 1:
                return "任务签发"+" - "+projectName;
            case 3://（设计，校对，审核）任务完成
                return taskContent+" - "+taskName;
            case 4:
                return "技术审查费 - 付款金额确认";
            case 6:
                return "合作设计费 - 付款金额确认";
            case 10:
                return "合同回款"+" - "+"到账确认";
            case 11:
            case 23:
                return taskName;
            case 12: // 任务负责人，生产安排任务
                return "生产安排 - "+projectName;
            case 13: //任务负责人所负责的任务完成，13完成，同时触发12完成
                return "生产安排 - "+taskName;
            case 14:
                return "设置设计负责人";
            case 15:
                return "设置任务负责人";
            case 16:
                return "技术审查费 - 确认付款日期";
            case 17:
                return "技术审查费 - 确认到账日期";
            case 18:
                return "合作设计费 - 确认付款日期";
            case 19:
                return "合作设计费 - 付款确认";
            case 20:
                return "其他费用 - 付款确认";
            case 21:
                return "其他费用 - 到账确认";
            case 22://所有设计任务已完成，给组织的设计负责人推送任务
                return "审批任务 - "+taskName;
            case MyTaskEntity.DELIVER_CONFIRM_FINISH:
                return "交付确认 - " + taskName;
            case MyTaskEntity.DELIVER_EXECUTE:
                return "交付执行 - " + taskName;
            default:
                return null;
        }
    }

    /**
     * @author  张成亮
     * @date    2018/7/19
     * @description     查找交付任务的相关目录信息
     * @param   deliverId 交付任务的编号
     * @return  目录信息
     **/
    private BaseShowDTO getDirInfo(String deliverId){
        BaseShowDTO result = new BaseShowDTO("","");
        if (!com.maoding.core.util.StringUtils.isEmpty(deliverId)) {
            //查找与交付任务相关的目录
            ProjectSkyDriverQueryDTO query = new ProjectSkyDriverQueryDTO();
            query.setDeliverId(deliverId);
            ProjectSkyDriveEntity dir = projectSkyDriverService.getEntityByQuery(query);
            if (dir != null) {
                result.setId(dir.getId());
                result.setName(dir.getFileName());
            }
        }
        return result;
    }

    /**
     * 方法描述：myTaskEntity重新查找数据组装成dto
     * 作者：GuoZB
     * 日期：2017/1/6
     */
    private void getMyTaskDesc(MyTaskList2DTO dto) throws Exception {
        int taskType = dto.getTaskType();
        if(taskType == 1){
            ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
            if (projectEntity.getCompanyId().equals(dto.getCompanyId())) {//立项方
                dto.setDescription("进行"+dto.getProjectName()+"的“"+this.projectTaskDao.getIssueTaskName(dto.getProjectId(), dto.getCompanyId(), 1,null)+"”等的签发工作");
            } else {//合作方
                dto.setDescription("进行"+dto.getProjectName()+"的“"+this.projectTaskDao.getIssueTaskName(dto.getProjectId(), dto.getCompanyId(), 2,null)+"”等的签发工作");
            }
        } else if (isDeliverType(taskType)) {
            dto.setDescription(dto.getTaskContent());
        }
    }

    //是否交付任务（有两个类型）
    private boolean isDeliverType(int taskType){
        return (MyTaskEntity.DELIVER_CONFIRM_FINISH == taskType)
                || (MyTaskEntity.DELIVER_EXECUTE == taskType);
    }

    public String getRole(MyTaskList2DTO dto) {
        int taskType = dto.getTaskType();
        String taskContent = dto.getTaskContent();
        //暂时未处理
        switch (taskType) {
            case 1:
            case 14:
            case 4:
            case 6:
                ProjectMemberEntity member = projectMemberService.getOperatorManager(dto.getProjectId(),dto.getCompanyId());
                if(member!=null && !StringUtil.isNullOrEmpty(dto.getHandlerId()) && member.getCompanyUserId().equals(dto.getHandlerId())){
                    return "经营负责人";
                }
                return "经营助理";
            case 3:
                return taskContent + "人";
            case 11:
            case 23:
                return "审批";
            case 12:
            case 13:
                return "任务负责人";
            case 10:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                return "财务人员";
            case 15:
            case 22:
                ProjectMemberEntity design = projectMemberService.getDesignManager(dto.getProjectId(),dto.getCompanyId());
                if(design!=null && !StringUtil.isNullOrEmpty(dto.getHandlerId()) && design.getCompanyUserId().equals(dto.getHandlerId())){
                    return "设计负责人";
                }
                return "设计助理";
            case MyTaskEntity.DELIVER_CONFIRM_FINISH:
                return "任务负责人";
            case MyTaskEntity.DELIVER_EXECUTE:
                return "任务执行人";
            default:
                return null;
        }
    }

    private void initParam(Map<String, Object> param){
        if (null != param.get("pageIndex")) {
            int page = (Integer) param.get("pageIndex");
            int pageSize = (Integer) param.get("pageSize");
            param.put("startPage", page * pageSize);
            param.put("endPage", pageSize);
        }
        //当前成员是否是财务成员
        if(permissionService.isFinancial((String)param.get("companyId"),(String)param.get("accountId"))){
            param.put("isHandler","1");
        }
        if(permissionService.isFinancialReceive((String)param.get("companyId"),(String)param.get("accountId"))){
            param.put("isHandlerReceive","1");
        }
    }
    private List<MyTaskDataDTO> convertMyTask2(List<MyTaskDTO> data) throws Exception {
        List<MyTaskDataDTO> list = new ArrayList<>();
        for (MyTaskDTO dto : data) {
            list.add(convertMyTask(dto));
        }
        return list;
    }


    /**
     * 方法描述：根据参数查询我的任务（companyId,handlerId，projectId)
     * 作者：MaoSF
     * 日期：2016/12/1
     */
    @Override
    public AjaxMessage getMyTaskByProjectId( Map<String, Object> param) throws Exception{
        String projectId = (String)param.get("projectId");
        String companyId = (String)param.get("companyId");
        String accountId = (String)param.get("accountId");
        param.put("companyId",companyId);
        //此处这样处理的原因是。处理报销之外的任务，全部是跟项目相关的任务，如果projectId 为null，则是报销任务
        if(!StringUtil.isNullOrEmpty(projectId)){
            param.put("projectId",projectId);
        }else {
            param.put("taskType","11"); // 报销任务的taskType = 11
            param.put("status","0");//只查询有效的
        }
        this.initParam(param);
        List<MyTaskDTO> list = this.myTaskDao.getMyTaskByProjectId(param);
        int total = this.myTaskDao.getMyTaskCount();//此语句必须紧跟在 List<MyTaskEntity> list = this.myTaskDao.getMyTaskByParam(param); 后面
        List<MyTaskDataDTO> dtoList = new ArrayList<>();
        int unCompleteNum = 0;
        for(MyTaskDTO myTaskDTO:list){
            MyTaskDataDTO dto = this.convertMyTask(myTaskDTO);
            dtoList.add(dto);
            if(!"1".equals(myTaskDTO.getStatus())){
                unCompleteNum++;
            }
        }
        param.clear();
        param.put("myTaskList",dtoList);
        param.put("total",total);
        param.put("unCompleteNum",unCompleteNum);
        return  AjaxMessage.succeed("查询成功").setData(param);
    }

    @Override
    public String getCompleteTaskId(Map<String,Object> param) {
        return this.myTaskDao.getCompleteTaskId(param);
    }

    /**
     * 方法描述：任务经营人，任务技术负责人
     * 作者：MaoSF
     * 日期：2017/1/9
     */
    private MyTaskDataDTO getTaskContentForIssue(MyTaskDTO dto) throws Exception {
        MyTaskDataDTO data = new MyTaskDataDTO();
        BaseDTO.copyFields(dto,data);
        ProjectEntity projectEntity = this.projectDao.selectById(dto.getProjectId());
        if (projectEntity != null) {
            //任务描述
            Map<String,String> desc = new HashMap<>();
            desc.put("projectName",projectEntity.getProjectName());
            desc.put("desc","你被指定为经营负责人");
            if(projectEntity.getCompanyId().equals(dto.getCompanyId())){//立项方
                String taskName = this.projectTaskDao.getIssueTaskName(dto.getProjectId(),dto.getCompanyId(),1,null);
                desc.put("taskName",taskName);
            }else {//合作方
                String taskName = this.projectTaskDao.getIssueTaskName(dto.getProjectId(),dto.getCompanyId(),2,null);
                desc.put("taskName",taskName);
            }
            data.setDescription(desc);
        }
        return data;
    }

    /**
     * 方法描述：3,12,13(获取任务信息)
     * 作者：MaoSF
     * 日期：2017/1/9
     */
    private MyTaskDataDTO getTaskContentForTask(MyTaskDTO dto) throws Exception {
        MyTaskDataDTO data = new MyTaskDataDTO();
        BaseDTO.copyFields(dto,data);
        String taskId = dto.getTargetId();
        if(dto.getTaskType()==3){
            taskId = dto.getParam1();
        }
        ProjectTaskDataDTO task = this.projectTaskDao.getProjectTaskById(taskId,dto.getCompanyId());
        if (task != null) {
            TaskDescDTO desc = new TaskDescDTO();
            BaseDTO.copyFields(task,desc);
            Integer taskState = this.projectTaskDao.getTaskState(taskId);
            desc.setTaskState(taskState ==null?0:taskState);
            desc.setStateHtml( this.projectTaskDao.getStateText(desc.getTaskState(),desc.getStartTime(),desc.getEndTime(),task.getCompleteDate()));
            data.setDescription(desc);
        }
        return data;
    }

    /**
     * 方法描述：3,12,13(获取任务信息)
     * 作者：MaoSF
     * 日期：2017/1/9
     */
    private MyTaskDataDTO getTaskContentForTask22(MyTaskDTO dto) throws Exception {
        MyTaskDataDTO data = new MyTaskDataDTO();
        BaseDTO.copyFields(dto,data);
        ProjectEntity project = this.projectDao.selectById(dto.getProjectId());
        if(project==null){
            return data;
        }
        int type = 1;
        if(!project.getCompanyId().equals(dto.getCompanyId())){
            type = 2;
        }
        String taskContent = this.projectTaskDao.getIssueTaskName(dto.getProjectId(),dto.getCompanyId(),type,null);
//        if (task != null) {
//            TaskDescDTO desc = new TaskDescDTO();
//            BaseDTO.copyFields(task,desc);
//            Integer taskState = this.projectTaskDao.getTaskState(taskId);
//            desc.setTaskState(taskState ==null?0:taskState);
//            desc.setStateHtml( this.projectTaskDao.getStateText(desc.getTaskState(),desc.getStartTime(),desc.getEndTime(),task.getCompleteDate()));
//            data.setDescription(desc);
//        }
        data.setTaskContent(taskContent);
        return data;
    }

    /**
     * 方法描述：合同回款，其他费用
     * 作者：MaoSF
     * 日期：2017/1/9
     */
    private MyTaskDataDTO getTaskContentForFee(MyTaskDTO dto) throws Exception {
        MyTaskDataDTO data = new MyTaskDataDTO();
        BaseDTO.copyFields(dto,data);
        ProjectCostPointDataForMyTaskDTO desc = this.projectCostService.getProjectCostPointDetailForMyTask(dto.getTargetId(),null,dto.getTaskType(),dto.getCompanyId());
        data.setDescription(desc);
        return data;
    }

    /**
     * 方法描述：技术审查费，合作设计费，财务--到款，付款 16-19
     * 作者：MaoSF
     * 日期：2017/1/9
     */
    private MyTaskDataDTO getTaskContentForFee2(MyTaskDTO dto) throws Exception {
        MyTaskDataDTO data = new MyTaskDataDTO();
        BaseDTO.copyFields(dto,data);
        ProjectCostPointDataForMyTaskDTO desc = this.projectCostService.getProjectCostPointDetailForMyTask(null,dto.getTargetId(),dto.getTaskType(),dto.getCompanyId());
        data.setDescription(desc);
        return data;
    }

    /**
     * 方法描述：报销审批的任务
     * 作者：MaoSF
     * 日期：2017/1/9
     */
    private MyTaskDataDTO getTaskContentForExp(MyTaskDTO dto) throws Exception {
        MyTaskDataDTO data = new MyTaskDataDTO();
        BaseDTO.copyFields(dto,data);
        ExpMainDTO desc = this.expMainDao.getByMainIdForMyTask(dto.getTargetId());
        data.setDescription(desc);
        return data;
    }

    /**
     * 方法描述：myTaskEntity重新查找数据组装成dto
     * 作者：MaoSF
     * 日期：2017/1/6
     */
    private MyTaskDataDTO convertMyTask(MyTaskDTO entity) throws Exception {
        int taskType = entity.getTaskType();
        //暂时未处理
        switch (taskType) {
            case 1:
                return getTaskContentForIssue(entity);
            case 3:
            case 12:
            case 13:
                return getTaskContentForTask(entity);
            case 22:
                return getTaskContentForTask(entity);
            case 4:
            case 5:
            case 6:
            case 7:
            case 10:
            case 20:
            case 21:
                return getTaskContentForFee2(entity);
            case 11:
                return getTaskContentForExp(entity);
            case 16:
            case 17:
            case 18:
            case 19:
                return getTaskContentForFee(entity);
            default:
                return null;
        }
    }

    /**
     * @param request 交付申请信息
     * @author 张成亮
     * @date 2018/7/16
     * @description 创建交付相关的个人任务
     **/
    @Override
    public void saveDeliverTask(DeliverEditDTO request) {
        //创建交付任务，用于查找交付历史
        DeliverEntity deliver = saveDeliverAction(request,DeliverEntity.DELIVER_ACTION);

        //创建负责人任务，用于标记上传任务完成
        saveDeliverResponseTask(deliver,request.getChangedResponseList());

        //为每个负责人发送一条需要完成上传的消息
        List<BaseShowDTO> receiverConfirmList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(request.getChangedResponseList())) {
            for (ResponseEditDTO response : request.getChangedResponseList()) {
                receiverConfirmList = addUserToListByResponse(receiverConfirmList,response);
            }
        }
        List<MessageEntity> messageConfirmList = messageService.createDeliverChangedMessageListFrom(
                request,receiverConfirmList,SystemParameters.MESSAGE_TYPE_DELIVER_CONFIRM,request.getIssueId(),
                "");
        messageService.sendMessage(messageConfirmList);

        //创建上传者列表，默认为新增加的所有负责人
        List<BaseShowDTO> receiverUploadList = receiverConfirmList;

        //获取负责人名称列表
        StringBuilder responseNameBuilder = new StringBuilder();
        List<ResponseDTO> responseList = listResponse(deliver);
        responseList.forEach(response->{
            final String SPLIT_RESPONSE_NAME = "、";
            if (responseNameBuilder.length() > 0){
                responseNameBuilder.append(SPLIT_RESPONSE_NAME);
            }
            responseNameBuilder.append(response.getName());
        });


        //创建上传者任务，用于快速跳转到上传目录,同时添加任务参与人到上传者列表，为发送上传消息做准备
        if (!StringUtils.isEmpty(request.getIssueId())) {
            //如果指定了任务，为所有参与此任务的人员创建上传者任务
            List<ResponseEditDTO> memberList = listMember(request.getIssueId());
            if (!ObjectUtils.isEmpty(memberList)) {
                saveDeliverUploadTask(deliver, memberList);
                for (ResponseEditDTO member : memberList) {
                    receiverUploadList = addUserToListByResponse(receiverUploadList,member);
                }
            }
        } else {
            //如果没有指定任务（有可能使用目录来生成交付），把负责人当做上传者
            saveDeliverUploadTask(deliver, request.getChangedResponseList());
        }
        //为每个上传者发送一条上传的消息
        List<MessageEntity> messageUploadList = messageService.createDeliverChangedMessageListFrom(
                request,receiverUploadList,SystemParameters.MESSAGE_TYPE_DELIVER_UPLOAD,
                deliver.getTargetId(), responseNameBuilder.toString());
        messageService.sendMessage(messageUploadList);

        //如果设置了状态，进行状态修改
        if (request.getIsFinished() != null){
            handleMyTaskDeliver(deliver,request);
        }

    }

    //获取交付任务负责人列表
    private List<ResponseDTO> listResponse(DeliverEntity deliver){
        MyTaskQueryDTO query = new MyTaskQueryDTO();
        query.setMyTaskType(MyTaskEntity.DELIVER_CONFIRM_FINISH);
        query.setStatus(0);
        query.setTaskId(deliver.getId());
        List<MyTaskEntity> list = myTaskDao.listEntityByQuery(query);
        List<ResponseDTO> responseList = new ArrayList<>();
        list.forEach(myTask->{
            ResponseDTO response = new ResponseDTO();
            response.setId(myTask.getHandlerId());
            response.setName(companyUserDao.getUserName(myTask.getHandlerId()));
            responseList.add(response);
        });
        return responseList;
    }

    //把项目成员列表转换为负责人编辑列表
    private List<ResponseEditDTO> toResponseEditList(List<BaseShowDTO> companyUserList){
        List<ResponseEditDTO> list = new ArrayList<>();
        if (!ObjectUtils.isEmpty(companyUserList)) {
            companyUserList.forEach(companyUser -> {
                ResponseEditDTO response = new ResponseEditDTO();
                //只设置id号，设置全部成员为选中状态
                response.setId(companyUser.getId());
                response.setIsSelected("1");
                response.setName(companyUser.getName());
                list.add(response);
            });
        }
        return list;
    }

    //以ResponseEditDTO类型获取所有任务及子任务的成员
    private List<ResponseEditDTO> listMember(String taskId){
        if (StringUtils.isEmpty(taskId)){
            return null;
        }
        UserQueryDTO query = new UserQueryDTO();
        query.setParentTaskId(taskId);
        List<BaseShowDTO> list = userService.listWithCompanyUserIdByQuery(query);
        return toResponseEditList(list);
    }

    private boolean isTrue(String b){
        return "1".equals(b);
    }

    //如果添加response,根据companyUserId查找user,如果在列表内不存在User，添加User到列表
    private List<BaseShowDTO> addUserToListByResponse(List<BaseShowDTO> list, ResponseEditDTO response){
        if (isTrue(response.getIsSelected())){
            CompanyUserEntity companyUser = companyUserDao.selectById(response.getId());
            if (companyUser != null) {
                list = addToListWhenNotExist(list, new BaseShowDTO(companyUser.getId(), response.getName()));
            }
        }
        return list;
    }

    //如果在列表内不存在数据，添加数据到列表，否则不添加
    private <T extends BaseShowDTO> List<T> addToListWhenNotExist(List<T> list, T dto){
        boolean isFound = false;
        for (T t : list) {
            if (t.getId().equals(dto.getId())){
                isFound = true;
                break;
            }
        }
        if (!isFound){
            list.add(dto);
        }
        return list;
    }

    //创建上传者任务，用于快速跳转到上传目录
    private List<MyTaskEntity> saveDeliverResponseTask(DeliverEntity deliver, List<ResponseEditDTO> responseList){
        return saveMyTaskFrom(deliver,responseList,MyTaskEntity.DELIVER_CONFIRM_FINISH);
    }

    //创建负责人任务，用于标记上传任务完成
    private List<MyTaskEntity> saveDeliverUploadTask(DeliverEntity deliver, List<ResponseEditDTO> responseList){
        return saveMyTaskFrom(deliver,responseList,MyTaskEntity.DELIVER_EXECUTE);
    }


    //根据交付信息创建并保存deliver记录
    private DeliverEntity saveDeliverAction(DeliverEditDTO request, int actionType){
        DeliverEntity entity = new DeliverEntity();
        if (StringUtils.isEmpty(request.getId())){
            entity.initEntity();
        } else {
            entity.setId(request.getId());
            entity.resetUpdateDate();
        }
        entity.setCompanyId(request.getCompanyId());
        entity.setProjectId(request.getProjectId());
        entity.setTaskType(actionType);
        entity.setHandlerId(request.getCreateBy());
        entity.setCreateBy(request.getCreateBy());
        entity.setSendCompanyId(request.getCompanyId());
        entity.setCreateDate(new Date());
        entity.setDeadline(request.getEndTime());
        entity.setTargetId(request.getNodeId());
        entity.setTaskTitle(request.getName());
        entity.setTaskContent(request.getDescription());

        if (StringUtils.isEmpty(request.getId())) {
            deliverDao.insert(entity);
        } else {
            deliverDao.updateById(entity);
        }
        return entity;
    }

    //根据交付信息创建myTask记录
    //taskType: 要创建的myTask类型，DELIVER_CONFIRM_FINISH:确认交付文件上传完毕, DELIVER_EXECUTE:进行交付文件上传
    private List<MyTaskEntity> saveMyTaskFrom(DeliverEntity deliver, List<ResponseEditDTO> changedResponseList,int myTaskType){
        List<MyTaskEntity> entityList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(changedResponseList)) {
            changedResponseList.forEach(response -> {
                if (isNewResponse(response)) {
                    MyTaskEntity entity = new MyTaskEntity();
                    entity.setId(StringUtil.buildUUID());
                    entity.setCompanyId(deliver.getCompanyId());
                    entity.setProjectId(deliver.getProjectId());
                    entity.setTaskType(myTaskType);
                    entity.setHandlerId(response.getId());
                    entity.setCreateBy(deliver.getCreateBy());
                    entity.setSendCompanyId(deliver.getCompanyId());
                    entity.setCreateDate(new Date());
                    entity.setDeadline(deliver.getDeadline());
                    entity.setTargetId(deliver.getId());
                    entity.setTaskTitle(deliver.getTaskTitle());
                    entity.setTaskContent(deliver.getTaskContent());
                    entityList.add(entity);
                }
            });
        }
        myTaskDao.insert(entityList);
        return entityList;
    }

    //判断负责人是否尚未添加个人任务
    private boolean isNewResponse(ResponseEditDTO response){
        return !"0".equals(response.getIsSelected());
    }

    /**
     * @param request 交付任务申请
     * @return 创建或修改后的交付任务
     * @author 张成亮
     * @date 2018/7/19
     * @description 创建或修改交付任务
     **/
    @Override
    public void changeDeliver(DeliverEditDTO request) {
        //创建交付目录
        String nodeId = projectSkyDriverService.createDeliverDir(request);

        //创建并保存交付任务，包括发起的交付任务，各个负责人的交付任务和上传任务
        if (!StringUtils.isEmpty(nodeId)) {
            request.setNodeId(nodeId);
        }
        saveDeliverTask(request);

    }
}
