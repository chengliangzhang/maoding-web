package com.maoding.project.service.impl;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.maoding.commonModule.dto.ContentDTO;
import com.maoding.commonModule.dto.UpdateConstDTO;
import com.maoding.commonModule.service.ConstService;
import com.maoding.conllaboration.SyncCmd;
import com.maoding.conllaboration.service.CollaborationService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.bean.ApiResult;
import com.maoding.core.constant.ProjectConst;
import com.maoding.core.constant.ProjectCostConst;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.*;
import com.maoding.dynamic.dao.OrgDynamicDao;
import com.maoding.dynamic.dao.ZInfoDAO;
import com.maoding.dynamic.dto.ZProjectDTO;
import com.maoding.dynamic.service.DynamicService;
import com.maoding.enterprise.dto.EnterpriseSearchQueryDTO;
import com.maoding.enterprise.service.EnterpriseService;
import com.maoding.message.dto.SendMessageDTO;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import com.maoding.mytask.dao.MyTaskDao;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.notice.service.NoticeService;
import com.maoding.org.dao.BusinessPartnerDao;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyPropertyDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.dto.CompanyQueryDTO;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.org.entity.BusinessPartnerEntity;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyPropertyEntity;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.service.CompanyUserService;
import com.maoding.project.constDefine.EnterpriseServer;
import com.maoding.project.dao.*;
import com.maoding.project.dto.*;
import com.maoding.project.entity.*;
import com.maoding.project.service.ProjectConditionService;
import com.maoding.project.service.ProjectDesignContentService;
import com.maoding.project.service.ProjectService;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.projectcost.dao.ProjectCostDao;
import com.maoding.projectcost.dto.CostAmountDTO;
import com.maoding.projectcost.dto.ProjectCostQueryDTO;
import com.maoding.projectcost.dto.ProjectCostSingleSummaryDTO;
import com.maoding.projectcost.dto.ProjectCostSummaryQueryDTO;
import com.maoding.projectcost.service.ProjectCostService;
import com.maoding.projectmember.dto.MemberQueryDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;
import com.maoding.projectmember.dto.ProjectMemberGroupDTO;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.projectmember.service.ProjectMemberService;
import com.maoding.role.dto.PermissionDTO;
import com.maoding.role.dto.ProjectUserPermissionEnum;
import com.maoding.role.service.PermissionService;
import com.maoding.system.dto.DataDictionaryDTO;
import com.maoding.system.service.DataDictionaryService;
import com.maoding.task.dao.ProjectProcessTimeDao;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.dao.ProjectTaskRelationDao;
import com.maoding.task.dto.ProjectTaskDataDTO;
import com.maoding.task.dto.TaskChangeCompanyDTO;
import com.maoding.task.entity.ProjectProcessTimeEntity;
import com.maoding.task.entity.ProjectTaskEntity;
import com.maoding.task.entity.ProjectTaskRelationEntity;
import com.maoding.task.service.ProjectTaskService;
import com.maoding.user.dao.AttentionDao;
import com.maoding.user.entity.AttentionEntity;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectService
 * 类描述：项目Service
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:28:54
 */
@Service("projectService")
public class ProjectServiceImpl extends GenericService<ProjectEntity> implements ProjectService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    //项目dao
    @Autowired
    private ProjectDao projectDao;

    //数据字典dao
    @Autowired
    private DataDictionaryService dataDictionaryService;

    //设计范围dao
    @Autowired
    private ProjectDesignRangeDao projectDesignRangeDao;

    //设计阶段service
    @Autowired
    private ProjectDesignContentService projectDesignContentService;

    //设计依据dao
    @Autowired
    private ProjectDesignBasisDao projectDesignBasisDao;

    //项目审核
    @Autowired
    private ProjectAuditDao projectAuditDao;

    @Autowired
    private ProjectConstructDao projectConstructDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private ProjectProcessTimeDao projectProcessTimeDao;

    @Autowired
    private ProjectTaskRelationDao projectTaskRelationDao;

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private MyTaskDao myTaskDao;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private OrgDynamicDao orgDynamicDao;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private ProjectTaskDao projectTaskDao;

    @Autowired
    private ProjectDynamicsDao projectDynamicsDao;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private ProjectCostService projectCostService;

    @Autowired
    private AttentionDao attentionDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CollaborationService collaborationService;

    @Autowired
    private BusinessPartnerDao businessPartnerDao;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private ZInfoDAO zInfoDAO;

    @Autowired
    private ProjectPropertyDao projectPropertyDao;

    @Autowired
    private CompanyPropertyDao companyPropertyDao;

    @Autowired
    private ProjectDesignContentDao projectDesignContentDao;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ConstService constService;

    @Autowired
    private ProjectConditionService projectConditionService;

    @Autowired
    private ProjectCostDao projectCostDao;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private EnterpriseServer enterpriseServer;
    /**
     * 方法描述：保存项目（数据验证）
     * 作者：MaoSF
     * 日期：2016/7/29
     *
     * @param:
     * @return:
     */
    public AjaxMessage validateSaveOrUpdateProject(ProjectDTO dto) {

        if (StringUtil.isNullOrEmpty(dto.getId()) && StringUtil.isNullOrEmpty(dto.getProjectName())) {
            return new AjaxMessage().setCode("1").setInfo("请输入项目名称");
        } else if (!StringUtil.isNullOrEmpty(dto.getProjectName()) && dto.getProjectName().length() > 100) {
            return new AjaxMessage().setCode("1").setInfo("项目名称长度过长");
        }
        /*if (StringUtil.isNullOrEmpty(dto.getConstructCompany())) {
            return new AjaxMessage().setCode("1").setInfo("甲方名称不能为空");
		}
		if(StringUtil.isNullOrEmpty(dto.getProjectType())){
			return new AjaxMessage().setCode("1").setInfo("项目类型不能为空");
		}
		if (CollectionUtils.isEmpty(dto.getProjectDesignContentList())) {
			return new AjaxMessage().setCode("1").setInfo("设计范围不能为空");
		}*/
        /** 2018-6-25 卢昕要求取消立项时的设计任务 */
//        if (CollectionUtils.isEmpty(dto.getProjectDesignContentList()) && StringUtil.isNullOrEmpty(dto.getId())) {
//            return new AjaxMessage().setCode("1").setInfo("设计内容不能为空");
//        }

//        if(StringUtil.isNullOrEmpty(dto.getId()) && StringUtil.isNullOrEmpty(dto.getProjectManagerId())){
//            return new AjaxMessage().setCode("1").setInfo("经营负责人不能为空");
//        }

        if (dto.getInvestmentEstimation() != null &&
                (dto.getInvestmentEstimation().compareTo(new BigDecimal(-99999999999999.999999)) < 0
                        || dto.getInvestmentEstimation().compareTo(new BigDecimal(99999999999999.999999)) > 0)) {
            return new AjaxMessage().setCode("1").setInfo("投资估算长度过长");
        }

        if (dto.getTotalContractAmount() != null &&
                (dto.getTotalContractAmount().compareTo(new BigDecimal(-99999999999999.999999)) < 0
                        || dto.getTotalContractAmount().compareTo(new BigDecimal(99999999999999.999999)) > 0)) {
            return new AjaxMessage().setCode("1").setInfo("合同总金额长度过长");
        }

        if (!CollectionUtils.isEmpty(dto.getProjectDesignContentList())) {
            Map<String, String> contentNames = new HashMap<>();
            for (ProjectDesignContentDTO designContentDTO : dto.getProjectDesignContentList()) {
                if (contentNames.containsKey(designContentDTO.getContentName())) {
                    return new AjaxMessage().setCode("1").setInfo(designContentDTO.getContentName() + "重复");
                } else {
                    contentNames.put(designContentDTO.getContentName(), designContentDTO.getContentName());
                }
            }
        }

        return new AjaxMessage().setCode("0");
    }


    /**
     * 方法描述：编辑，新增项目，处理设计阶段（非接口）
     * 作者：MaoSF
     * 日期：2016/8/12
     */
    private void handleProjectDesignContent(List<ProjectDesignContentDTO> dtoList, String projectId, String companyId, String accountId, String managerId, boolean isUpadte) throws Exception {
        int seq = 1;
        String taskDetailId = "";
        Map<String, Object> map = new HashMap<>();
        map.put("projectId", projectId);
        map.put("taskType", 1);//只查询根任务
        List<ProjectTaskEntity> oldTaskList = this.projectTaskDao.selectByParam(map);
        for (ProjectDesignContentDTO projectDesignContentDTO : dtoList) {
            if ("0".equals(projectDesignContentDTO.getInsertStatus())) {
                String designContentId = projectDesignContentDTO.getId();
                //重新设置dto中startTime，endTime，因为前端存放在projectProcessTimeEntityList中,所以在此获取
                Date startTime = null;
                Date endTime = null;
                for (ProjectProcessTimeEntity time : projectDesignContentDTO.getProjectProcessTimeEntityList()) {
                    if (StringUtil.isNullOrEmpty(time.getId())) {
                        startTime = time.getStartTime();
                        endTime = time.getEndTime();
                    }
                }

                //处理设计阶段信息
                if (StringUtil.isNullOrEmpty(projectDesignContentDTO.getId())) {
                    designContentId = this.saveTask(projectId, companyId, projectDesignContentDTO.getContentName(), seq++, startTime, endTime, accountId);
                    taskDetailId = this.saveTaskDetail(projectId, companyId, projectDesignContentDTO.getContentName(), seq++, startTime, endTime, accountId);
                } else {
                    for (ProjectTaskEntity entity : oldTaskList) {
                        if (entity.getId().equals(projectDesignContentDTO.getId())) {
                            entity.setSeq((seq++));
                            entity.setTaskName(projectDesignContentDTO.getContentName());
                            entity.setUpdateBy(accountId);
                            entity.setStartTime(startTime);
                            entity.setEndTime(endTime);
                            this.projectTaskService.updateById(entity);
                        }
                    }
                }
                this.saveProjectTime(projectDesignContentDTO, designContentId, companyId, accountId);
                if (null != startTime || null != endTime) {
                    ProjectProcessTimeEntity projectProcessTimeEntity = new ProjectProcessTimeEntity();
                    projectProcessTimeEntity.setStartTime(startTime);
                    projectProcessTimeEntity.setEndTime(endTime);
                    List<ProjectProcessTimeEntity> projectProcessTimeEntityList = new ArrayList<ProjectProcessTimeEntity>();
                    projectProcessTimeEntityList.add(projectProcessTimeEntity);
                    projectDesignContentDTO.setProjectProcessTimeEntityList(projectProcessTimeEntityList);
                    this.saveProjectTime(projectDesignContentDTO, taskDetailId, companyId, accountId);
                }
            }
            if ("1".equals(projectDesignContentDTO.getInsertStatus())) {
                this.projectTaskService.deleteProjectTaskByIdForDesignContent(projectDesignContentDTO.getId(), accountId, companyId);
                this.messageService.deleteMessage(projectDesignContentDTO.getId());
            }
        }
    }

    //保存任务信息
    private String saveTask(String projectId, String companyId, String taskName, int seq, Date startTime, Date endTime, String accountId) throws Exception {
        ProjectTaskEntity projectTaskEntity = new ProjectTaskEntity();
        projectTaskEntity.setId(StringUtil.buildUUID());
        projectTaskEntity.setFromCompanyId(companyId);
        projectTaskEntity.setCompanyId(companyId);
        projectTaskEntity.setProjectId(projectId);
        projectTaskEntity.setSeq(seq);
        projectTaskEntity.setTaskLevel(1);
        projectTaskEntity.setTaskName(taskName);
        projectTaskEntity.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);//新增的时候为未发布状态
        projectTaskEntity.setTaskPath(projectTaskEntity.getId());
        projectTaskEntity.setTaskType(SystemParameters.TASK_TYPE_MODIFY);
        projectTaskEntity.setStartTime(startTime);
        projectTaskEntity.setEndTime(endTime);
        projectTaskEntity.setCreateBy(accountId);
        projectTaskEntity.setIsOperaterTask(0);
        this.projectTaskService.insert(projectTaskEntity);
        return projectTaskEntity.getId();
    }

    //保存设计阶段信息
    private String saveTaskDetail(String projectId, String companyId, String taskName, int seq, Date startTime, Date endTime, String accountId) throws Exception {
        ProjectDesignContentEntity projectDesignContentEntity = new ProjectDesignContentEntity();
        projectDesignContentEntity.setId(StringUtil.buildUUID());
        projectDesignContentEntity.setProjectId(projectId);
        projectDesignContentEntity.setCompanyId(companyId);
        projectDesignContentEntity.setSeq(seq + "");
        projectDesignContentEntity.setContentName(taskName);
        this.projectDesignContentDao.insert(projectDesignContentEntity);
        return projectDesignContentEntity.getId();
    }

    //保存设定的设计阶段的时间
    private void saveProjectTime(ProjectDesignContentDTO projectDesignContentDTO, String targetId, String companyId, String accountId) throws Exception {
        if (!CollectionUtils.isEmpty(projectDesignContentDTO.getProjectProcessTimeEntityList())) {
            Calendar calendar = Calendar.getInstance();
            System.out.println(calendar.getTime());
            for (ProjectProcessTimeEntity projectProcessTimeEntity : projectDesignContentDTO.getProjectProcessTimeEntityList()) {
                if (StringUtil.isNullOrEmpty(projectProcessTimeEntity.getId())) {
                    calendar.add(Calendar.SECOND, 1);
                    Date date = calendar.getTime();
                    projectProcessTimeEntity.setTargetId(targetId);
                    projectProcessTimeEntity.setId(StringUtil.buildUUID());
                    projectProcessTimeEntity.setCompanyId(companyId);
                    projectProcessTimeEntity.setType(1);
                    projectProcessTimeEntity.setCreateBy(accountId);
                    projectProcessTimeEntity.setCreateDate(date);
                    projectProcessTimeDao.insert(projectProcessTimeEntity);
                }
            }
        }

    }

    /**
     * 方法描述：删除项目（逻辑删除）
     * 作者：MaoSF
     * 日期：2016/8/4
     */
    @Override
    public AjaxMessage deleteProjectById(String id, String currentCompanyUserId) throws Exception {
        //添加项目动态
        ProjectEntity origin = projectDao.selectById(id);
        CompanyUserEntity user = companyUserDao.selectById(currentCompanyUserId);
        if(origin==null || user==null){
            return AjaxMessage.error("数据错误");
        }
        if(this.handleDeleteRole(origin.getCreateBy(),origin.getCompanyId(),user.getCompanyId(),user.getUserId())==0){
            return AjaxMessage.error("对不起，你无权限删除");
        }

        dynamicService.addDynamic(origin, (ProjectEntity) null, currentCompanyUserId);

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(id);
        projectEntity.setPstatus("1");
        this.projectDao.updatePstatus(projectEntity);

        myTaskDao.deleteProjectTask(id);

        orgDynamicDao.updatefield2ByTargetId(id);

        //通知协同
        this.collaborationService.pushSyncCMD_PU(id);

        //删除消息
        messageService.deleteMessage(id);

        //删除项目群组
//        Map<String, String> map = new HashMap<>();
//        map.put("orgId", id);
//        imGroupService.removeGroupDestination(map);

        return new AjaxMessage().setCode("0").setInfo("删除成功");
    }


    @Override
    public ProjectShowDTO getProjectShow(Map<String, Object> map) throws Exception {
        ProjectShowDTO projectShowDTO = new ProjectShowDTO();
        String companyId = (String) map.get("companyId");
        List<DataDictionaryDTO> constructionCateList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_CONSTRUCTFUNCTION);//建筑功能
        List<DataDictionaryDTO> projectTypeList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_Type);//项目类别
        List<DataDictionaryDTO> designBasicList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_DESIGNBASIC);//设计依据
        List<DataDictionaryDTO> designRangeList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_DESIGNRANGE);//设计范围
        List<DataDictionaryDTO> designContentList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_DESIGNCONTENT);
        //设计阶段
        List<DataDictionaryDTO> taskManageList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.TASK_MANAGER);
        //任务管理

        projectShowDTO.setConstructionCateList(constructionCateList);
        projectShowDTO.setDesignBasicList(designBasicList);
        projectShowDTO.setDesignRangeList(designRangeList);
        projectShowDTO.setDesignContentList(designContentList);
        projectShowDTO.setProjectTypeList(projectTypeList);
        projectShowDTO.setTaskManageList(taskManageList);
        //乙方
        map.put("orgId", companyId);
        List<CompanyDTO> companyList = companyDao.getCompanyFilterbyParam(map);
        projectShowDTO.setPartyBList(companyList);
        //甲方：建设单位
//        List<ProjectConstructDTO> projectConstructList = projectConstructDao.getConstructByCompanyId(companyId);
//        projectShowDTO.setPartyAList(projectConstructList);
        return projectShowDTO;
    }


    /**
     * 方法描述：根据id查询项目信息
     * 作者：MaoSF
     * 日期：2016/7/28
     *
     * @param:
     * @return:
     */
    @Override
    public ProjectDTO getProjectById(String id, String companyId, String userId) throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        //项目基本信息
        ProjectEntity projectEntity = projectDao.selectById(id);
        BaseDTO.copyFields(projectEntity, projectDTO);
        CompanyEntity companyEntity = companyDao.selectById(projectDTO.getCompanyId());
        projectDTO.setCompanyName(companyEntity.getAliasName());

        Map<String, Object> paraMap = Maps.newHashMap();
        paraMap.put("targetId", id);
        paraMap.put("type", "1");
        paraMap.put("companyId", companyId);
        paraMap.put("companyUserId", companyUserDao.getCompanyUserByUserIdAndCompanyId(userId, companyId).getId());
        AttentionEntity attentionEntity = attentionDao.getAttentionEntity(paraMap);
        if (null != attentionEntity) {
            projectDTO.setAttentionId(attentionEntity.getId());
        }
        //项目类型
        projectDTO.setProjectTypeName(projectEntity.getProjectType());
//        if (null != projectEntity.getProjectType()) {
//            DataDictionaryEntity projectType = dataDictionaryService.selectById(projectEntity.getProjectType());
//            projectDTO.setProjectTypeName(projectType.getName());
//        }
        //获取甲方名称
        if (!StringUtil.isNullOrEmpty(projectEntity.getConstructCompany())) {
            //  ProjectConstructEntity constructCompany = projectConstructDao.selectById(projectEntity.getConstructCompany());
            projectDTO.setConstructCompanyName(projectDao.getEnterpriseName(projectEntity.getConstructCompany()));

        }

        if (!StringUtil.isNullOrEmpty(projectEntity.getCompanyBid())) {
            CompanyEntity companyB = companyDao.selectById(projectEntity.getCompanyBid());
            projectDTO.setCompanyBidName(companyB.getCompanyName());

            //处理项目负责人和项目经营人
            List<ProjectMemberDTO> projectManagerDTOList = projectMemberService.listProjectMemberByParam(projectEntity.getId(), projectEntity.getCompanyBid(), null, null);
            for (ProjectMemberDTO projectManagerDTO : projectManagerDTOList) {
                if (projectManagerDTO.getMemberType() == ProjectMemberType.PROJECT_OPERATOR_MANAGER) {
                    projectDTO.setPartBDesignerId(projectManagerDTO.getCompanyUserId());
                    projectDTO.setPartBManagerName(projectManagerDTO.getCompanyUserName());
                }
                if (projectManagerDTO.getMemberType() == ProjectMemberType.PROJECT_DESIGNER_MANAGER) {
                    projectDTO.setPartBManagerId(projectManagerDTO.getCompanyUserId());
                    projectDTO.setPartBDesignerName(projectManagerDTO.getCompanyUserName());
                }
            }
        }

        //合同签订日期
        // List<ProjectAuditEntity> signEntity = projectAuditDao.getProjectAuditEntityByProjectAndType(id, "2");
        if (!StringUtil.isNullOrEmpty(projectEntity.getContractDate())) {
            projectDTO.setSignDate(DateUtils.formatDate(projectEntity.getContractDate()));
        }

        //备案日期
        List<ProjectAuditEntity> auditEntity = projectAuditDao.getProjectAuditEntityByProjectAndType(id, "3");
        if (!auditEntity.isEmpty()) {
            projectDTO.setAuditPreTheftDate(auditEntity.get(0).getAuditDate());
        }

        //合同备案附件
        ProjectSkyDriveEntity attachList = projectSkyDriverService.getProjectContractAttach(id);
        if (attachList != null) {
            projectDTO.setFileName(attachList.getFileName());
            projectDTO.setFilePath(attachList.getFileGroup() + "/" + attachList.getFilePath());
        }

        //项目设计依据
        List<ProjectDesignBasisDTO> projectDesignBasisList = projectDesignBasisDao.getDesignBasisByProjectId(id);
        projectDTO.setProjectDesignBasisList(projectDesignBasisList);

        //项目设计范围
        List<ProjectDesignRangeEntity> projectDesignRangeEntityList = projectDesignRangeDao.getDesignRangeByProjectId(id);
        projectDTO.setProjectDesignRangeList(BaseDTO.copyFields(projectDesignRangeEntityList, ProjectDesignRangeDTO.class));

        Map<String, Object> param = new HashMap<>();
        param.put("companyId", companyEntity.getId());
        param.put("projectId", id);

        //处理项目负责人和项目经营人
        List<ProjectMemberDTO> projectManagerDTOList = projectMemberService.listProjectMemberByParam(id, companyEntity.getId(), null, null);
        for (ProjectMemberDTO projectManagerDTO : projectManagerDTOList) {
            if (projectManagerDTO.getMemberType() == ProjectMemberType.PROJECT_OPERATOR_MANAGER) {
                projectDTO.setOperatorPerson(projectManagerDTO.getCompanyUserId());
                projectDTO.setOperatorPersonName(projectManagerDTO.getCompanyUserName());
            }
            if (projectManagerDTO.getMemberType() == ProjectMemberType.PROJECT_DESIGNER_MANAGER) {
                projectDTO.setManagerPerson(projectManagerDTO.getCompanyUserId());
                projectDTO.setManagerPersonName(projectManagerDTO.getCompanyUserName());
            }
        }

        List<DataDictionaryDTO> constructionCateList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_CONSTRUCTFUNCTION);
        projectDTO.setConstructionCateList(constructionCateList);

        //项目设计阶段
        param.clear();
        param.put("companyId", companyId);
        param.put("userId", userId);
        param.put("projectCompanyId", projectEntity.getCompanyId());
        List<ProjectDesignContentDTO> projectDesignContentList = projectDesignContentService.getProjectDesignContentByProjectId(id, param);
        projectDTO.setProjectDesignContentList(projectDesignContentList);

        //合同回款节点 开票明细 到款明细
        Map<String, Object> numMap = this.getProjectBasicNum(projectDTO);
        projectDTO.setTotalNum((int) numMap.get("totalNum"));
        projectDTO.setCompletionsNum((int) numMap.get("completionsNum"));
        projectDTO.setOutstandingNum((int) numMap.get("outstandingNum"));
        return projectDTO;
    }

    /**
     * 方法：根据id查询项目详细信息（带自定义字段）
     * 作者：Zhangchengliang
     * 日期：2017/8/15
     *
     * @param id        项目Id
     * @param companyId 查询者当前所在公司id
     * @param userId    查询者id
     */
    @Override
    public ProjectDetailsDTO loadProjectDetails(String id, String companyId, String userId) throws Exception {
        //填充基本信息
        ProjectDetailsDTO project = projectDao.getProjectDetailsById(id);
        if (null == project) {
            return null;
        }
        //填充项目类型名称
//        if (null != project.getProjectType() && !StringUtil.isEmpty(project.getProjectType().getId()) && StringUtil.isEmpty(project.getProjectType().getContent())) {
//            DataDictionaryEntity entity = dataDictionaryService.selectById(project.getProjectType().getId());
//            project.getProjectType().setContent(entity.getName());
//        }
        //填充项目状态名称
        if (project.getProjectStatus() != null && project.getProjectStatus().getValueId() != null && StringUtil.isEmpty(project.getProjectStatus().getContent())) {
            //todo 改为从数据库读取，目前为0＝进行中，1＝已暂停•未结清，2＝已完成•未结清 ，3 = 已终止•未结清， 4＝已完成•已结清，5＝已暂停•已结清 ，6 = 已终止•已结清
            project.getProjectStatus().setContent(SystemParameters.PROJECT_STATUS.get(project.getProjectStatus().getValueId().toString()));
        }
        //填充公司名
        if (null != project.getCreatorCompany() && null != project.getCreatorCompany().getId() && null == project.getCreatorCompany().getCompanyName()) {
            CompanyEntity companyEntity = companyDao.selectById(project.getCreatorCompany().getId());
            if (null != companyEntity) {
                project.getCreatorCompany().setCompanyName(companyEntity.getAliasName());
            }
        }
        if (null != project.getPartyACompany() && null != project.getPartyACompany().getId() && null == project.getPartyACompany().getCompanyName()) {
            //甲方数据获取
            project.getPartyACompany().setCompanyName(projectDao.getEnterpriseName(project.getPartyACompany().getId()));
//            ProjectConstructEntity constructCompany = projectConstructDao.selectById(project.getPartyACompany().getId());
//            if (constructCompany != null){
//                project.getPartyACompany().setCompanyName(constructCompany.getCompanyName());
//            }
        }
        if (null != project.getPartyBCompany() && null != project.getPartyBCompany().getId() && null == project.getPartyBCompany().getCompanyName()) {
            CompanyEntity companyEntity = companyDao.selectById(project.getPartyBCompany().getId());
            if (null != companyEntity) {
                project.getPartyBCompany().setCompanyName(companyEntity.getAliasName());
            }
            //填充乙方项目负责人
            List<ProjectMemberDTO> memberList = projectMemberService.listProjectMemberByParam(project.getId(), project.getPartyBCompany().getId(), null, null);
            for (ProjectMemberDTO member : memberList) {
                if (ProjectMemberType.PROJECT_OPERATOR_MANAGER.equals(member.getMemberType())) {
                    project.setOperatorOfPartyB(member);
                } else if (ProjectMemberType.PROJECT_DESIGNER_MANAGER.equals(member.getMemberType())) {
                    project.setManagerOfPartyB(member);
                }
            }

        }
        //填充合同文件
        List<NetFileDTO> attachList = projectSkyDriverService.listProjectContractAttach(id);
        if (!CollectionUtils.isEmpty(attachList)) {
            List<Map<String, String>> fileList = new ArrayList<>();
            Map<String, String> lastFile = null;
            for (ProjectSkyDriveEntity attach : attachList) {
                Map<String, String> f = new HashMap<>();
                f.put("filePath", attach.getFileGroup() + "/" + attach.getFilePath());
                f.put("fileName", attach.getFileName());
                f.put("id", attach.getId());
                fileList.add(f);
                lastFile = f;
            }
            if (null != lastFile) {
                project.setFilePath(lastFile.get("filePath"));
                project.setFileName(lastFile.get("fileName"));
            }
            project.setContractList(fileList);
        }
        //填充功能分类
        QueryProjectDTO queryProject = new QueryProjectDTO();
        queryProject.setId(project.getId());
        List<ContentDTO> constBuiltTypeList = projectDao.listBuiltTypeConst(queryProject);
        List<ContentDTO> customBuiltTypeList = projectDao.listBuiltTypeCustom(queryProject);
        if (constBuiltTypeList != null){
            if (project.getBuildTypeList() == null) {
                project.setBuildTypeList(new ArrayList<>());
            }
            if (project.getConstructionCateList() == null) {
                project.setConstructionCateList(new ArrayList<>());
            }
            constBuiltTypeList.stream()
                .forEach(bt->{
                    //保存默认功能分类列表
                    DataDictionaryDTO prjBuiltType = new DataDictionaryDTO();
                    prjBuiltType.setId(bt.getId());
                    prjBuiltType.setName(bt.getName());
                    project.getConstructionCateList().add(prjBuiltType);

                    //保存选中的默认功能分类列表
                    if (StringUtils.contains(project.getBuiltType(),bt.getId())) {
                        project.getBuildTypeList().add(prjBuiltType);
                    }
                });
        }
        if (customBuiltTypeList != null){
            if (project.getBuildTypeList() == null) {
                project.setBuildTypeList(new ArrayList<>());
            }
            customBuiltTypeList.stream()
                    .forEach(bt->{
                        //保存自定义功能分类列表
                        DataDictionaryDTO prjBuiltType = new DataDictionaryDTO();
                        prjBuiltType.setId(bt.getId());
                        prjBuiltType.setName(bt.getName());
                        project.getBuildTypeList().add(prjBuiltType);
                    });
        }

        //填充自定义属性
        List<CustomProjectPropertyDTO> propertyList = projectPropertyDao.listProperty(id);
        project.setProjectPropertyList(propertyList);
        //填充设计范围
        List<ProjectDesignRangeEntity> entityList = projectDesignRangeDao.getDesignRangeByProjectId(id);
        if (!CollectionUtils.isEmpty(entityList)) {
            List<ProjectDesignRangeDTO> rangeList = entityList.stream().map(ProjectDesignRangeDTO::new).collect(Collectors.toList());
            project.setProjectRangeList(rangeList);
        }
        //填充设计内容
        Map<String, Object> param = new HashMap<>();
        param.put("companyId", companyId);
        param.put("userId", userId);
        if (null != project.getCreatorCompany()) {
            param.put("projectCompanyId", project.getCreatorCompany().getId());
        }
        List<ProjectDesignContentDTO> designList = projectDesignContentService.getProjectDesignContentByProjectId(id, param);
        project.setProjectDesignContentList(designList);
        //填充项目在当前用户关注的项目列表中的关注编号
        if (null == project.getAttentionId()) {
            Map<String, Object> paraMap = Maps.newHashMap();
            paraMap.put("targetId", id);
            paraMap.put("type", "1");
            paraMap.put("companyId", companyId);
            paraMap.put("companyUserId", companyUserDao.getCompanyUserByUserIdAndCompanyId(userId, companyId).getId());
            AttentionEntity attentionEntity = attentionDao.getAttentionEntity(paraMap);
            if (null != attentionEntity) {
                project.setAttentionId(attentionEntity.getId());
            }
        }
        return project;
    }


    private List<ContentDTO> getProjectBuildType(String projectId){
        List<ContentDTO> list = new ArrayList<>();
        QueryProjectDTO queryProject = new QueryProjectDTO();
        queryProject.setId(projectId);
        List<ContentDTO> constBuiltTypeList = projectDao.listBuiltTypeConst(queryProject);
        List<ContentDTO> customBuiltTypeList = projectDao.listBuiltTypeCustom(queryProject);
        list.addAll(constBuiltTypeList);
        list.addAll(customBuiltTypeList);
        return list;
    }

    private String getProjectBuildName(String projectId) {
        List<String> buildNameList = new ArrayList<>();
        List<ContentDTO> buildList = this.getProjectBuildType(projectId);
        buildList.stream().forEach(b->{
            if(b.getSelected()){
                buildNameList.add(b.getName());
            }
        });
       return String.join(",", buildNameList);
    }

    @Override
    public List<ContentDTO> getProjectBuildType(List<String> projectIdList) {
        List<ContentDTO> list = new ArrayList<>();
        QueryProjectDTO queryProject = new QueryProjectDTO();
        queryProject.setMemberProjects(projectIdList);
        List<ContentDTO> constBuiltTypeList = projectDao.listBuiltTypeConst(queryProject);
        List<ContentDTO> customBuiltTypeList = projectDao.listBuiltTypeCustom(queryProject);
        list.addAll(constBuiltTypeList);
        list.addAll(customBuiltTypeList);
        return list;
    }

    /**
     * 方法：根据id查询项目自定义字段详细信息
     * 作者：Zhangchengliang
     * 日期：2017/8/17
     *
     * @param query 查询条件
     */
    @Override
    public CustomProjectPropertyEditDTO loadProjectCustomFields(ProjectCustomFieldQueryDTO query) throws Exception {
        CustomProjectPropertyEditDTO result = new CustomProjectPropertyEditDTO();

        //公司默认的自定义属性列表
        List<CustomProjectPropertyDTO> defaultList = companyPropertyDao.listDefaultProperty(query.getCompanyId());
        if (!CollectionUtils.isEmpty(defaultList)) {
            result.setBasicPropertyList(defaultList);
        } else {
            defaultList = createDefaultList(query.getCompanyId());
            if (!CollectionUtils.isEmpty(defaultList)) {
                result.setBasicPropertyList(defaultList);
            }
        }

        //单位列表
        List<String> unitNameList = companyPropertyDao.listUnitName();
        if (!CollectionUtils.isEmpty(unitNameList)) {
            result.setUnitNameList(unitNameList);
        }

        //公司可编辑的自定义属性列表
        List<CustomProjectPropertyDTO> customList = companyPropertyDao.listCustomProperty(query.getCompanyId());
        if (!CollectionUtils.isEmpty(customList)) {
            result.setCustomPropertyList(customList);
        }

        //项目使用到的自定义属性列表
        List<CustomProjectPropertyDTO> selectedList = projectPropertyDao.listProperty(query.getProjectId());
        if (!CollectionUtils.isEmpty(selectedList)) {
            result.setSelectedPropertyList(selectedList);
        }
        return result;
    }

    /**
     * 方法：保存项目自定义字段详细信息，包括公司自定义属性
     * 作者：Zhangchengliang
     * 日期：2017/8/17
     *
     * @param properties 自定义界面上的数据
     */
    @Override
    public void saveProjectCustomFields(CustomProjectPropertyEditDTO properties) throws Exception {
        final Short CHANGE_STATUS_DEL = -1;
        final Short CHANGE_STATUS_ADD = 1;
        final Short CHANGE_STATUS_CHANGE = 2;
        //保存公司自定义字段
        if (properties.getCustomPropertyList() != null) {
            Integer n = 0;
            for (CustomProjectPropertyDTO property : properties.getCustomPropertyList()) {
                if (CHANGE_STATUS_DEL.equals(property.getChangeStatus())) {
                    companyPropertyDao.fakeDeleteById(property.getId());
                } else if ((CHANGE_STATUS_ADD.equals(property.getChangeStatus())) || (CHANGE_STATUS_CHANGE.equals(property.getChangeStatus()))) {
                    CompanyPropertyEntity entity = new CompanyPropertyEntity(property);
                    entity.setCompanyId(properties.getCompanyId());
                    entity.setBeDefault(false);
                    entity.setBeSelected(false);
                    if (properties.getSelectedPropertyList() != null) {
                        for (CustomProjectPropertyDTO projectProperty : properties.getSelectedPropertyList()) {
                            if (!CHANGE_STATUS_DEL.equals(projectProperty.getChangeStatus())
                                    && StringUtil.isSame(property.getFieldName(), projectProperty.getFieldName())) {
                                entity.setBeSelected(true);
                                break;
                            }
                        }
                    }
                    entity.setSequencing(n++);
                    if ((CHANGE_STATUS_ADD.equals(property.getChangeStatus()))) {
                        entity.resetId();
                        entity.resetCreateDate();
                        companyPropertyDao.insert(entity);
                    } else {
                        entity.resetUpdateDate();
                        companyPropertyDao.updateById(entity);
                    }
                }
            }
        }
        //保存项目自定义字段
        if (properties.getSelectedPropertyList() != null) {
            Integer n = 0;
            for (CustomProjectPropertyDTO property : properties.getSelectedPropertyList()) {
                if (CHANGE_STATUS_DEL.equals(property.getChangeStatus())) {
                    projectPropertyDao.fakeDeleteById(property.getId());
                } else if ((CHANGE_STATUS_ADD.equals(property.getChangeStatus())) || (CHANGE_STATUS_CHANGE.equals(property.getChangeStatus()))) {
                    ProjectPropertyEntity entity = new ProjectPropertyEntity(property);
                    entity.setProjectId(properties.getProjectId());
                    if (CHANGE_STATUS_ADD.equals(property.getChangeStatus())) {
                        entity.resetId();
                        entity.resetCreateDate();
                        entity.setCreateBy(properties.getOperatorId());
                        entity.setSequencing(n++);
                        projectPropertyDao.insert(entity);
                    } else {
                        entity.resetUpdateDate();
                        entity.setUpdateBy(properties.getOperatorId());
                        entity.setSequencing(n++);
                        projectPropertyDao.updateById(entity);
                    }
                }
            }
        }
    }

    /**
     * 方法：添加公司默认的自定义属性，并返回公司默认自定义属性列表
     * 作者：Zhangchengliang
     * 日期：2017/8/17
     */
    private List<CustomProjectPropertyDTO> createDefaultList(String companyId) {
        List<CustomProjectPropertyDTO> defaultList = new ArrayList<>();
        List<CompanyPropertyEntity> commonDefaultList = companyPropertyDao.listCommonDefaultProperty();
        if (!CollectionUtils.isEmpty(commonDefaultList)) {
            companyPropertyDao.insertDefaultProperty(companyId, commonDefaultList);
            for (CompanyPropertyEntity entity : commonDefaultList) {
                CustomProjectPropertyDTO dto = new CustomProjectPropertyDTO(entity);
                defaultList.add(dto);
            }
        }
        return defaultList;
    }

    /**
     * 方法描述：根据companyId查询所有有效项目(我要报销 选择项目下拉框 )
     * 作   者：LY
     * 日   期：2016/8/8 14:38
     */
    @Override
    public List<ProjectDTO> getProjectListByCompanyId(ProjectDTO dto) {
        return projectDao.getProjectListByCompanyId(dto);
    }


    /**
     * 方法描述：修改项目设计依据
     * 作        者：ChenZJ
     * 日        期：2016年7月20日-下午4:47:25
     */
    @Override
    public AjaxMessage saveProjectDesignBasic(ProjectDTO dto) throws Exception {

        String projectId = dto.getId();
        //先删除设计依据
        projectDesignBasisDao.deleteDBasisByProjectId(projectId);

        //保存设计依据
        ProjectDesignBasisEntity projectDesignBasisEntity = null;
        if (dto.getProjectDesignBasisList().size() > 0) {
            int seq = 1;
            for (ProjectDesignBasisDTO designBasisDTO : dto.getProjectDesignBasisList()) {
                projectDesignBasisEntity = new ProjectDesignBasisEntity();
                designBasisDTO.setId(StringUtil.buildUUID());
                BaseDTO.copyFields(designBasisDTO, projectDesignBasisEntity);
                projectDesignBasisEntity.setProjectId(projectId);
                projectDesignBasisEntity.setCreateBy(dto.getAccountId());
                projectDesignBasisEntity.setUpdateBy(dto.getAccountId());
                projectDesignBasisEntity.setSeq(seq++);
                projectDesignBasisDao.insert(projectDesignBasisEntity);
            }
        }
        return new AjaxMessage().setCode("0").setInfo("保存成功").setData(dto);
    }


    //===============================2.0新接口===============================================


    /**
     * 方法描述：项目立项及修改
     * 作者：MaoSF
     * 日期：2016/12/6
     *
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage saveOrUpdateProjectNew(ProjectDTO dto) throws Exception {
        AjaxMessage ajaxMessage = this.validateSaveOrUpdateProject(dto);
        if (!"0".equals(ajaxMessage.getCode())) {
            return ajaxMessage;
        }

        //如果建设单位id为null，则通过建设单位名查找或增加新的建设单位
//        if (dto.getFlag() == 2 && StringUtil.isNullOrEmpty(dto.getConstructCompany())) {
//            //处理甲方
//            if (!StringUtil.isNullOrEmpty(dto.getConstructCompanyName())) {
//                String constructId = this.handlProjectConstructCompanyName(dto.getConstructCompanyName(), dto.getCompanyId());
//                dto.setConstructCompany(constructId);
//            }
//        }

        //如果建设单位id为null，则通过建设单位名查找或增加新的建设单位
        if (2 == dto.getFlag()) {
            if (!StringUtil.isNullOrEmpty(dto.getEnterpriseId())) {
                //处理甲方
                String constructId = this.handleProjectConstruct(dto.getEnterpriseId(), dto.getCompanyId());
                dto.setConstructCompany(constructId);
            } else if (!StringUtil.isNullOrEmpty(dto.getConstructCompanyName())) {
                String constructId = this.handleProjectConstructName(dto.getConstructCompanyName(), dto.getCompanyId());
                dto.setConstructCompany(constructId);
            }
        }

        String companyBid = null;
        int partBFlag = 0;
        if (3 == dto.getFlag()) {
            partBFlag = this.projectPartB(dto);
        }

        //更新人
        String accountId = dto.getAccountId();
        //项目
        ProjectEntity projectEntity = new ProjectEntity();
        BaseDTO.copyFields(dto, projectEntity);

        ProjectEntity originProject = (dto.getId() != null) ? projectDao.selectById(dto.getId()) : null; //保存原有的项目内容
        ZProjectDTO originProjectEx = zInfoDAO.getProjectByProject(originProject); //保存原有项目的设计范围和功能分类

        boolean isUpdate = false;
        if (StringUtil.isNullOrEmpty(dto.getId())) {
            if (!dto.getCompanyId().equals(dto.getCurrentCompanyId())) {//如果是给其他组织（分公司，事业合伙人）立项的话，则需要记录是谁帮助立项的
                projectEntity.setHelperCompanyUserId(dto.getCurrentCompanyUserId());
            }
            //新增项目信息
            String id = StringUtil.buildUUID();
            projectEntity.setId(id);
            projectEntity.setPstatus("0");
            projectEntity.setStatus("0");
            projectEntity.setCreateBy(accountId);
            projectDao.insert(projectEntity);

            //建立默认的自定义字段
            createDefaultProjectProperty(projectEntity);

            //保存立项人
            this.setProjectCreator(projectEntity.getCompanyId(), projectEntity.getId(), dto.getCurrentCompanyId(), dto.getCurrentCompanyUserId(), dto.getAccountId());
            //创建默认磁盘
            this.projectSkyDriverService.createProjectFile(projectEntity);

            /*************处理设计阶段*************/
            /** 2018-6-25 卢昕要求取消立项时的设计任务 */
//            this.handleProjectDesignContent(dto.getProjectDesignContentList(), projectEntity.getId(), dto.getCompanyId(), dto.getAccountId(), dto.getProjectManagerId(), isUpdate);


            //保存经营负责人
            setProjectManagerForCompany(projectEntity.getCompanyId(), projectEntity.getId(), "51", accountId, true, dto.getCurrentCompanyId());
            //保存设计负责人
            setProjectManagerForCompany(projectEntity.getCompanyId(), projectEntity.getId(), "52", accountId, true, dto.getCurrentCompanyId());

            //发送组织动态消息
            this.noticeService.saveNoticeForProject(projectEntity.getId(), projectEntity.getCompanyId(), projectEntity.getCreateBy(), dto.getCurrentCompanyUserId(), dto.getProjectDesignContentList());

        } else {//修改
            //保存功能分类
            if ((dto.getChangedBuiltTypeList() != null) && (dto.getChangedBuiltTypeList().size() > 0)) {
                List<ContentDTO> changedBuiltTypeList = dto.getChangedBuiltTypeList();
                StringBuilder builtTypeIdStr = new StringBuilder();
                changedBuiltTypeList.stream()
                        .forEach(bt->{
                            if (StringUtils.isEmpty(bt.getId())) {
                                UpdateConstDTO request = new UpdateConstDTO();
                                request.setProjectId(dto.getId());
                                request.setTitle(bt.getName());
                                request.setClassicId(ConstService.CONST_TYPE_BUILT_TYPE);
                                String id = constService.insertConst(request);
                                builtTypeIdStr.append(id).append(",");
                            } else if ((bt.getSelected() != null) && (bt.getSelected())) {
                                builtTypeIdStr.append(bt.getId()).append(",");
                                if ((bt.getDefault() == null) || (!bt.getDefault())) {
                                    UpdateConstDTO request = new UpdateConstDTO();
                                    request.setId(bt.getId());
                                    request.setTitle(bt.getName());
                                    constService.updateConst(request);
                                }
                            } else if ((bt.getDefault() == null) || (!bt.getDefault())){
                                constService.deleteConst(bt.getId());
                            }
                        });
                projectEntity.setBuiltType(builtTypeIdStr.toString());
            }

            if (0 != dto.getFlag()) {
                BeanUtilsEx.copyProperties(originProject, projectEntity);

                companyBid = projectEntity.getCompanyBid();
                if (1 == dto.getFlag()) {
                    projectEntity.setInvestmentEstimation(dto.getInvestmentEstimation());
                }
                if (2 == dto.getFlag()) {
                    projectEntity.setConstructCompany(dto.getConstructCompany());
                }
                if (3 == dto.getFlag()) {
                    projectEntity.setCompanyBid(dto.getCompanyBid());
                }
                if (4 == dto.getFlag()) {
                    projectEntity.setContractDate(dto.getContractDate());
                }
                projectDao.update(projectEntity);//更新全部字段
            } else {
                projectEntity.setUpdateBy(accountId);
                projectDao.updateById(projectEntity);
            }

            isUpdate = true;
            /*************处理设计阶段*************/
            //查询经营负责人
            ProjectMemberEntity manager = this.projectMemberService.getProjectMember(projectEntity.getId(), projectEntity.getCompanyId(), ProjectMemberType.PROJECT_OPERATOR_MANAGER, null);
            String managerId = null;
            if (manager != null) {
                managerId = manager.getCompanyUserId();
            }
            if (!CollectionUtils.isEmpty(dto.getProjectDesignContentList())) {
                this.handleProjectDesignContent(dto.getProjectDesignContentList(), dto.getId(), dto.getCompanyId(), dto.getAccountId(), managerId, isUpdate);
            }

            //修改项目群
//            if (!StringUtil.isNullOrEmpty(projectEntity.getProjectName())) {
//                Map<String, String> projectParam = new HashMap<>();
//                projectParam.put("orgId", projectEntity.getId());
//                projectParam.put("orgName", projectEntity.getProjectName());
//                imGroupService.updateGroupDestination(projectParam);
//            }
        }

        //处理乙方变更
        if (1 == partBFlag || 2 == partBFlag || 4 == partBFlag) {
            //处理乙方财务
            this.projectCostService.handPartBChange(projectEntity.getId(), accountId, partBFlag);

            if ((2 == partBFlag || 4 == partBFlag) && !StringUtil.isNullOrEmpty(companyBid)) {//删除经营负责人
                handleIsDeletePartBManager(projectEntity.getId(), companyBid);
            }
            if (!StringUtil.isNullOrEmpty(dto.getCompanyBid()) && !dto.getCompanyBid().equals(dto.getCompanyId())) {
                //查看乙方是否存在此项目的经营负责人及设计负责人，如果不存在则设立并发送通知
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("projectId", projectEntity.getId());
                map.put("companyId", dto.getCompanyBid());
                List<ProjectMemberEntity> list = this.projectMemberService.listProjectMember(projectEntity.getId(), dto.getCompanyBid(), null, null);
                if (list.size() == 0) { //不存在此项目的负责人
                    List<String> userIds = Lists.newArrayList();
                    userIds.add(setProjectManagerForCompany(dto.getCompanyBid(), projectEntity.getId(), "51", accountId, false, dto.getCurrentCompanyId()));
                    userIds.add(setProjectManagerForCompany(dto.getCompanyBid(), projectEntity.getId(), "52", accountId, false, dto.getCurrentCompanyId()));
                    //企业负责人、经营负责人、设计负责人（乙方组织）推送消息
                    this.sendMessageForPartB(projectEntity.getId(), dto.getCompanyBid(), dto.getCurrentCompanyId(), dto.getAccountId(), userIds);
                }

            }

        }

        String projectId = dto.getId();

        /***************end******************/

        //保存设计范围
        ProjectDesignRangeEntity projectDesignRangeEntity = null;
        if ("1".equals(dto.getProjectDesign())) {
            projectDesignRangeDao.deleteDRangeByProjectId(projectId);
        }
        if (dto.getProjectDesignRangeList().size() > 0) {
            //先删除设计范围
            projectDesignRangeDao.deleteDRangeByProjectId(projectId);
            int seq = 1;
            for (ProjectDesignRangeDTO designRangeDTO : dto.getProjectDesignRangeList()) {
                projectDesignRangeEntity = new ProjectDesignRangeEntity();
                designRangeDTO.setId(StringUtil.buildUUID());
                BaseDTO.copyFields(designRangeDTO, projectDesignRangeEntity);
                projectDesignRangeEntity.setProjectId(projectId);
                projectDesignRangeEntity.setCreateBy(accountId);
                projectDesignRangeEntity.setUpdateBy(accountId);
                projectDesignRangeEntity.setSeq(seq++);
                projectDesignRangeDao.insert(projectDesignRangeEntity);
            }
        }

        //合同签订数据插入审核表中
        //1.删除审核表中的备案数据
        // List<ProjectAuditEntity> projectAuditEntitys = projectAuditDao.getProjectAuditEntityByProjectAndType(projectId, "2");

        if (!StringUtil.isNullOrEmpty(dto.getSignDate())) {
            projectAuditDao.delAuditByProjectAndType(projectId, "2");
            ProjectAuditEntity auditEntity = new ProjectAuditEntity();
            auditEntity.setId(StringUtil.buildUUID());
            auditEntity.setProjectId(projectId);
            auditEntity.setAuditType("2");
            auditEntity.setUserId(dto.getAccountId());
            auditEntity.setAuditDate(dto.getSignDate());
            auditEntity.setAuditStatus("0");
            auditEntity.setCreateBy(dto.getAccountId());
            auditEntity.setUpdateBy(dto.getAccountId());
            projectAuditDao.insert(auditEntity);
        }
        //添加项目动态
        ProjectEntity targetProject = projectDao.selectById(projectEntity.getId());
        dynamicService.addDynamic(originProject, targetProject, dto.getCurrentCompanyId(), dto.getAccountId()); //普通内容
        if ((originProject != null) && (projectEntity != null) &&
                ((projectEntity.getStatus() == null) || (originProject.getStatus().equals(projectEntity.getStatus())))) {
            ZProjectDTO targetProjectEx = zInfoDAO.getProjectByProject(projectEntity);
            dynamicService.addDynamic(originProjectEx, targetProjectEx, dto.getCurrentCompanyId(), dto.getAccountId()); //设计范围、功能分类、合同签订日期
        }

        //通知协同
        this.collaborationService.pushSyncCMD_PU(projectEntity.getId());

        //推送消息
        this.sendMsgForAddProject(projectEntity.getId(), projectEntity.getCompanyId());

        ajaxMessage.setData(projectEntity.getId());

        return ajaxMessage.setInfo("保存成功");
    }

    /**
     * 方述：建立新增项目的默认自定义属性
     * 作者：ZCL
     * 日期：2017/9/6
     *
     * @param project 要添加的项目对象
     */
    @Override
    public void createDefaultProjectProperty(ProjectEntity project) {
        List<CustomProjectPropertyDTO> companyPropertyList = companyPropertyDao.listCompanyProperty(project.getCompanyId());
        if (CollectionUtils.isEmpty(companyPropertyList)) {
            companyPropertyList = createDefaultList(project.getCompanyId());
        }
        if (!CollectionUtils.isEmpty(companyPropertyList)) {
            projectPropertyDao.insertDefaultProperty(companyPropertyList, project.getId(), project.getCreateBy());
        }
    }

    /**
     * 保存立项人
     */
    private void setProjectCreator(String companyId, String projectId, String currentCompanyId, String currentCompanyUserId, String accountId) throws Exception {
        if (companyId.equals(currentCompanyId)) {//如果是本组织立项
            this.projectMemberService.saveProjectMember(projectId, companyId, currentCompanyUserId, ProjectMemberType.PROJECT_CREATOR, accountId, false, currentCompanyId);
        } else {
            //查询立项组织的企业负责人作为立项人
            CompanyUserTableDTO companyUser = this.companyUserService.getOrgManager(companyId);
            if (companyUser != null) {
                this.projectMemberService.saveProjectMember(projectId, companyId, companyUser.getId(), ProjectMemberType.PROJECT_CREATOR, accountId, false, currentCompanyId);
            }
        }
    }

    /**
     * 设置某公司某项目的管理者
     *
     * @param companyId    公司编号
     * @param projectId    项目编号
     * @param permissionId 权限编号，经营负责人：51，设计负责人：52
     * @param accountId    立项人用户编号
     * @param isCreator    是否立项方
     */
    private String setProjectManagerForCompany(String companyId, String projectId, String permissionId, String accountId, Boolean isCreator, String currentCompanyId) throws Exception {
        ProjectMemberEntity pm2 = (isSettingIssueManager(permissionId) ?
                projectMemberService.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_OPERATOR_MANAGER, null) :
                projectMemberService.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_DESIGNER_MANAGER, null));
        String userId = null;
        String companyUserId = null;
        if (pm2 == null) {
            //签发到的公司中选择具备相应权限的人员中选择第一个填入项目经营负责人位置
            Map<String, Object> map = new HashMap<>();
            map.put("permissionId", permissionId);//相应权限id
            map.put("companyId", companyId);
            List<CompanyUserTableDTO> companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
            if (companyUserList.size() > 0) {
                CompanyUserTableDTO cut = null;
                if (isCreator) {//如果是立项方
                    for (CompanyUserTableDTO userTableDTO : companyUserList) {
                        if (userTableDTO.getUserId().equals(accountId)) {//如果当前人就是经营负责人。则设置为经营负责人
                            cut = userTableDTO;
                        }
                    }
                }
                if (cut == null) {//如果当前人不是经营负责人，则默认第一个
                    cut = companyUserList.get(0);
                }
                if (cut != null) {
                    userId = cut.getUserId();
                    companyUserId = cut.getId();
                }
            } else if (isCreator) { //如果没有定义总监，保持原有逻辑，设置立项人为经营负责人及设计负责人
                userId = accountId;
                CompanyUserEntity cue = this.companyUserDao.getCompanyUserByUserIdAndCompanyId(accountId, companyId);
                if (cue != null) {
                    userId = cue.getUserId();
                    companyUserId = cue.getId();
                }
            }
            if ((companyUserId == null) || (userId == null)) return null;
            //给其推送任务或消息
            if (isSettingIssueManager(permissionId)) {
                if (isCreator) {//此处加上 && isCreator 区分立项还是乙方设置，添加乙方经营负责人不需要推送任务
                    this.projectMemberService.saveProjectMember(projectId, companyId, companyUserId, userId, (isSettingIssueManager(permissionId) ? ProjectMemberType.PROJECT_OPERATOR_MANAGER : ProjectMemberType.PROJECT_DESIGNER_MANAGER), accountId, !accountId.equals(userId), currentCompanyId);
                } else {//如果是乙方，则只推送消息
                    //只推送消息
                    this.projectMemberService.saveProjectMember(projectId, companyId, companyUserId, (isSettingIssueManager(permissionId) ? ProjectMemberType.PROJECT_OPERATOR_MANAGER : ProjectMemberType.PROJECT_DESIGNER_MANAGER), accountId, false, currentCompanyId);
                }
            } else {
                //设计负责人不推送消息
                this.projectMemberService.saveProjectMember(projectId, companyId, companyUserId, (isSettingIssueManager(permissionId) ? ProjectMemberType.PROJECT_OPERATOR_MANAGER : ProjectMemberType.PROJECT_DESIGNER_MANAGER), accountId, false, currentCompanyId);
            }
        }
        return userId;
    }

    private Boolean isSettingIssueManager(String permissionId) {
        return ("51".equals(permissionId));
    }

    private void handleIsDeletePartBManager(String projectId, String companyBid) throws Exception {
        //查询生产或经营的任务
        List<ProjectTaskEntity> list = this.projectTaskDao.getProjectTaskByCompanyIdOfOperater(projectId, companyBid);
        if (CollectionUtils.isEmpty(list)) {
            projectMemberService.deleteProjectMember(projectId, companyBid, null, null);
        }
    }


    /**
     * 方法描述：判断乙方的变更的情况，费用部分的数据变更
     * 作者：MaoSF
     * 日期：2017/3/2
     *
     * @param:
     * @return:
     */
    private int projectPartB(ProjectDTO dto) {
        if (StringUtil.isNullOrEmpty(dto.getId()) && !StringUtil.isNullOrEmpty(dto.getCompanyBid())) {
            return 1;//新增并且选择了乙方
        }
        if (!StringUtil.isNullOrEmpty(dto.getId())) {
            ProjectEntity entity = this.projectDao.selectById(dto.getId());
            String companyBid = entity.getCompanyBid();
            if (StringUtil.isNullOrEmpty(entity.getCompanyBid())) {
                companyBid = entity.getCompanyId();//为了后面少null的判断，因为乙方设置为自己和不设是一样
            }
            if (companyBid.equals(entity.getCompanyId())) {//原来没有设置乙方，或许乙方是自己的情况
                if (!StringUtil.isNullOrEmpty(dto.getCompanyBid()) && !dto.getCompanyBid().equals(dto.getCompanyId())) {
                    return 1;//全部设置
                }
            } else {//存在乙方，并且乙方不是自己
                if (StringUtil.isNullOrEmpty(dto.getCompanyBid()) || dto.getCompanyBid().equals(entity.getCompanyId())) {
                    return 2;//全部删除
                } else {
                    if (!companyBid.equals(dto.getCompanyBid())) {
                        return 4;//先删除后全部重置
                    } else {
                        return 3;//没有变更乙方，但是乙方是其他组织
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 方法描述：处理甲方
     * 作者：MaoSF
     * 日期：2016/12/29
     * constructCompanyId:从工商局获取的id（长度36位）
     */
    private String handleProjectConstruct(String constructCompanyId, String companyId) throws Exception {
        EnterpriseSearchQueryDTO query = new EnterpriseSearchQueryDTO();
        query.setEnterpriseId(constructCompanyId);
        query.setCompanyId(companyId);
        query.setSave(true);
        return getEnterpriseOrgId(enterpriseServer.getQueryDetail(), query);
    }

    /**
     * 方法描述：处理甲方
     * 作者：MaoSF
     * 日期：2016/12/29
     * constructCompanyId:从工商局获取的id（长度36位）
     */
    @Override
    public String handleProjectConstructName(String constructCompanyName, String companyId) throws Exception {
        EnterpriseSearchQueryDTO query = new EnterpriseSearchQueryDTO();
        query.setName(constructCompanyName);
        query.setCompanyId(companyId);
        query.setSave(true);
        return getEnterpriseOrgId(enterpriseServer.getQueryFull(), query);
    }

    /**
     * 方法描述：处理甲方
     * 作者：MaoSF
     * 日期：2016/12/29
     * constructCompanyId:从工商局获取的id（长度36位）
     */
    private String getEnterpriseOrgId(String url, EnterpriseSearchQueryDTO query) throws Exception {
        Response res = null;
        try {
            res = OkHttpUtils.postJson(url, query);
            if (res.isSuccessful()) {
                ApiResult<Map<String, Object>> apiResult = JsonUtils.json2pojo(res.body().string(), ApiResult.class);
                String enterpriseOrgId = (String) apiResult.getData().get("enterpriseOrgId");
                return enterpriseOrgId;
            } else {
                return null;
            }
        } catch (IOException e) {
            //写入日志
            log.error("getEnterpriseOrgId is fail->" + e.getMessage());
            return null;
        }
    }


    /**
     * 方法描述：（新建项目，项目修改）发送消息给相关组织成员
     * 作者：MaoSF
     * 日期：2017/2/20
     *
     * @param:
     * @return:
     */
    private void sendMsgForAddProject(String id, String companyId) {
        List<String> companyList = new ArrayList<String>();
        companyList.add(companyId);
        //查询项目相关的组织
        List<ProjectTaskRelationEntity> relationList = projectTaskRelationDao.getProjectTaskRelationByProjectId(id);
        if (!CollectionUtils.isEmpty(relationList)) {
            for (ProjectTaskRelationEntity relationEntity : relationList) {
                if (!companyList.contains(relationEntity.getFromCompanyId())) {
                    companyList.add(relationEntity.getFromCompanyId());
                }
                if (!companyList.contains(relationEntity.getToCompanyId())) {
                    companyList.add(relationEntity.getToCompanyId());
                }
            }
        }
        this.sendMsgToRelationCompanyUser(companyList);

    }

    /**
     * 方法描述：（新建项目，任务签发给其他组织）发送消息给相关组织成员
     * 作者：MaoSF
     * 日期：2017/2/20
     *
     * @param:
     * @return:
     */
    @Override
    public void sendMsgToRelationCompanyUser(List<String> companyList) {

        /*//查询companyList中所有的人员
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("companyList", companyList);
        List<String> userIdList = this.companyUserDao.getUserByCompanyForSendMsg(param);

        //发送消息
        try {
            Map<String, Object> messageMap = new HashMap<String, Object>();
            messageMap.put("messSourceType", "company");
            messageMap.put("messServerType", SystemParameters.PROJECT_TYPE);//项目类型的通知
            messageMap.put("messType", "2");//通知公告类型
            messageMap.put("noticeTitle", "Project");
            messageMap.put("noticeContent", "Operator");
            messageMap.put("userIdList", userIdList);
            messageProducer.sendSystemMessage(systemMessageDestination, messageMap);
        } catch (Exception e) {
            e.printStackTrace();

        }*/
    }

    /**
     * 方法描述：获取建筑功能
     * 作者：MaoSF
     * 日期：2017/3/11
     *
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage getBuildType(String companyId) throws Exception {
        List<String> builtTypeList = this.projectDao.getProjectBuildType(companyId);
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        if (!CollectionUtils.isEmpty(builtTypeList)) {
            List<String> idList = new ArrayList<String>();
            for (String ids : builtTypeList) {
                idList.addAll(Arrays.asList(ids.split(",")));
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idList", idList);
            result = this.projectDao.getBuildType(map);
        }
        return AjaxMessage.succeed("查询成功").setData(result);
    }


    @Override
    public List<DataDictionaryDTO> getDesignRangeList() throws Exception {
        List<DataDictionaryDTO> designRangeList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_DESIGNRANGE);//设计范围
        return designRangeList;
    }

    //添加合同签订动态
    public void setProjectDynamicsByAudit(ProjectEntity projectEntity, String signDate) {
        ProjectDynamicsEntity projectDynamicsEntity = new ProjectDynamicsEntity();
        projectDynamicsEntity.setId(StringUtil.buildUUID());
        //projectDynamicsEntity.setStatus("0");//直接在insert语句中默认为0
        projectDynamicsEntity.setCompanyId(projectEntity.getCompanyId());
        projectDynamicsEntity.setContent("“" + projectEntity.getProjectName() + "”项目签订合同");
        projectDynamicsEntity.setProjectId(projectEntity.getId());
        projectDynamicsDao.insert(projectDynamicsEntity);
    }

    @Override
    public List<DataDictionaryDTO> getDesignContentList() throws Exception {
        List<DataDictionaryDTO> designContentList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_DESIGNCONTENT);
        //设计阶段
        return designContentList;
    }


    @Override
    public List<ProjectConstructDTO> getProjectConstructListByName(Map<String, Object> map) throws Exception {
        List<ProjectConstructDTO> projectConstructDTOList = projectConstructDao.getConstructByParam(map);
        return projectConstructDTOList;
    }

    /**
     * 描述     获取标题栏过滤列表
     * 日期     2018/8/21
     *
     * @param param 查询条件
     * @return 各个标题栏过滤列表
     * @author 张成亮
     **/
    @Override
    public Map<String, Object> getTitleFilter(Map<String, Object> param,Map<String, Object> condition) throws Exception {
        String companyId = (String)param.get("companyId");
        String accountId = (String)param.get("accountId");
        String companyUserId = (String) param.get("companyUserId");
        param.remove("pageIndex");
        param.remove("pageSize");//此处不分页处理，查询所有项目信息

        //查询被选中的显示列
        String columnCodes = "";
        Map<String, Object> proCondition = getProConditionMap(param, companyId, accountId);
        List<ProjectConditionDTO> conditionDTOS = projectConditionService.selProjectConditionList(proCondition);
        if (0 < conditionDTOS.size()) {
            columnCodes = conditionDTOS.get(0).getCode();
        }


        List<ProjectTableDTO> dataList = getProjectsByPage(getProjectParam(condition));
        // 遍历查询条件
        Map<String, String> companyNames = new HashMap<>();
        Map<String, String> partyANames = new HashMap<>();
        Map<String, String> partyBNames = new HashMap<>();
        Map<String, String> busPersonInChargeMap = new HashMap<>();//经营负责人
        Map<String, String> designPersonInChargeMap = new HashMap<>();//设计负责人
        Map<String, String> busPersonInChargeAssistantMap = new HashMap<>();//经营负责人助理
        Map<String, String> designPersonInChargeAssistantMap = new HashMap<>();//设计负责人助理
        LinkedHashMap<String, String> buildTypeNames = new LinkedHashMap<>();
        StringBuffer buildTypeIds = new StringBuffer();
        setSelConditions(dataList, companyNames, partyANames, partyBNames, buildTypeNames, buildTypeIds,
                busPersonInChargeMap, designPersonInChargeMap, busPersonInChargeAssistantMap, designPersonInChargeAssistantMap);
        Map<String, Object> para = setProjectUserPermissionParam(companyId,companyUserId);
        List<PermissionDTO> permissionDTOS = permissionService.getProjectUserPermission(para);
        if (0 < permissionDTOS.size()) {
            param.put("flag", 1);
        } else {
            param.put("flag", 0);
        }

        //为项目列表添加合作组织信息过滤选择列表，即当前组织发布了签发任务给到的组织，不包含自己
        //这里需要查询所有项目的合作组织
        CompanyQueryDTO cooperatorCompanyQuery = new CompanyQueryDTO();
        cooperatorCompanyQuery.setCurrentCompanyId(companyId);
        //只查询外发的组织
        cooperatorCompanyQuery.setIsPay("1");
        List<CompanyDTO> cooperatorCompanyList = companyDao.listCompanyCooperate(cooperatorCompanyQuery);
        //生成查询组织列表
        Map<String, String> designCompanyNames = new HashMap<>();
        if (ObjectUtils.isNotEmpty(cooperatorCompanyList)){
            cooperatorCompanyList.forEach(company->
                    designCompanyNames.put(company.getId(),company.getCompanyName())
            );
        }

        //添加任务负责人、设计、校对、审核的过滤列表
        //任务负责人
        if (columnCodes.contains(ProjectConst.TITLE_PROJECT_MEMBER_TASK_LEADER)){
            param.put(ProjectConst.TITLE_PROJECT_MEMBER_TASK_LEADER + "List",toCoreShowList(listProjectMember(companyId, null, ProjectConst.MEMBER_TYPE_TASK_LEADER)));
        }
        //设计人员
        if (columnCodes.contains(ProjectConst.TITLE_PROJECT_MEMBER_TASK_DESIGNER)){
            param.put(ProjectConst.TITLE_PROJECT_MEMBER_TASK_DESIGNER + "List",toCoreShowList(listProjectMember(companyId, null, ProjectConst.MEMBER_TYPE_TASK_DESIGN)));
        }
        //校对人员
        if (columnCodes.contains(ProjectConst.TITLE_PROJECT_MEMBER_TASK_CHECKER)){
            param.put(ProjectConst.TITLE_PROJECT_MEMBER_TASK_CHECKER + "List",toCoreShowList(listProjectMember(companyId, null, ProjectConst.MEMBER_TYPE_TASK_CHECK)));
        }
        //审核人员
        if (columnCodes.contains(ProjectConst.TITLE_PROJECT_MEMBER_TASK_AUDITOR)){
            param.put(ProjectConst.TITLE_PROJECT_MEMBER_TASK_AUDITOR + "List",toCoreShowList(listProjectMember(companyId, null, ProjectConst.MEMBER_TYPE_TASK_AUDIT)));
        }

        //传递其他过滤列表
        param.put("designCompanyNameList",toCoreShowList(designCompanyNames));
        param.put("companyNameList", toCoreShowList(companyNames));
        param.put("partyANameList", toCoreShowList(partyANames));
        param.put("partyBNameList", toCoreShowList(partyBNames));
        param.put("buildTypeNameList", toCoreShowList(buildTypeNames));
        param.put("busPersonInChargeList", toCoreShowList(busPersonInChargeMap));
        param.put("designPersonInChargeList", toCoreShowList(designPersonInChargeMap));
        param.put("busPersonInChargeAssistantList", toCoreShowList(busPersonInChargeAssistantMap));
        param.put("designPersonInChargeAssistantList", toCoreShowList(designPersonInChargeAssistantMap));


        param.put("designCompanyNames",designCompanyNames);
        param.put("companyNames", companyNames);
        param.put("partyANames", partyANames);
        param.put("partyBNames", partyBNames);
        param.put("buildTypeNames", buildTypeNames);
        param.put("busPersonInChargeMap", busPersonInChargeMap);
        param.put("designPersonInChargeMap", designPersonInChargeMap);
        param.put("busPersonInChargeAssistantMap", busPersonInChargeAssistantMap);
        param.put("designPersonInChargeAssistantMap", designPersonInChargeAssistantMap);

        return param;
    }


    private void setSelConditions(List<ProjectTableDTO>  dataList,
                                  Map<String, String> companyNames, Map<String, String> partyANames,
                                  Map<String, String> partyBNames, LinkedHashMap<String, String> buildTypeNames,
                                  StringBuffer buildTypeIds, Map<String, String> busPersonInChargeMap, Map<String, String> designPersonInCharge,
                                  Map<String, String> busPersonInChargeAssistantMap, Map<String, String> designPersonInChargeAssistantMap) {

        List<String> projectIdList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ProjectTableDTO dto = dataList.get(i);
            projectIdList.add(dto.getId());
            if (null != dto && null != dto.getCompanyId() && null != dto.getCompanyName()) {
                companyNames.put(dto.getCompanyId(), dto.getCompanyName());
            }
            if (null != dto && null != dto.getConstructCompany() && null != dto.getPartyA()) {
                partyANames.put(dto.getConstructCompany(), dto.getPartyA());
            }
            if (null != dto && null != dto.getCompanyBid() && null != dto.getPartyB()) {
                partyBNames.put(dto.getCompanyBid(), dto.getPartyB());
            }
            if (null != dto && null != dto.getBuildType()) {
                buildTypeIds.append(dto.getBuildType());
            }
            if (null != dto && null != dto.getBusPersonInChargeUserId() && null != dto.getBusPersonInCharge()) {
                busPersonInChargeMap.put(dto.getBusPersonInChargeUserId(), dto.getBusPersonInCharge());
            }
            if (null != dto && null != dto.getBusPersonInChargeAssistantUserId() && null != dto.getBusPersonInChargeAssistant()) {
                busPersonInChargeAssistantMap.put(dto.getBusPersonInChargeAssistantUserId(), dto.getBusPersonInChargeAssistant());
            }
            if (null != dto && null != dto.getDesignPersonInChargeUserId() && null != dto.getDesignPersonInCharge()) {
                designPersonInCharge.put(dto.getDesignPersonInChargeUserId(), dto.getDesignPersonInCharge());
            }
            if (null != dto && null != dto.getDesignPersonInChargeAssistantUserId() && null != dto.getDesignPersonInChargeAssistant()) {
                designPersonInChargeAssistantMap.put(dto.getDesignPersonInChargeAssistantUserId(), dto.getDesignPersonInChargeAssistant());
            }
        }

//        List<String> idList = Arrays.asList(buildTypeIds.toString().split(","));
//        Map<String, Object> para1 = new HashMap<>();
//        para1.put("idList", idList);
//        List<DataDictionaryEntity> entities = dataDictionaryService.getDataByParemeter(para1);

        if(!CollectionUtils.isEmpty(projectIdList)){
            List<ContentDTO> buildTypeList = getProjectBuildType(projectIdList);
            buildTypeList.stream().forEach(b->{
                buildTypeNames.put(b.getName(), b.getName());//后台用name查找
            });
        }
    }

    /**
     * 方法描述：查询项目列表（进行中的项目）
     * 作者：TangY
     * 日期：2016/7/29
     */
    @Override
    public Map<String, Object> getProcessingProjectsByPage(Map<String, Object> param) throws Exception {
        Map<String, Object> result = new HashMap<>();
        QueryProjectDTO query = this.getProjectParam(param);

        //查询被选中的显示列
        String columnCodes = "";
        Map<String, Object> proCondition = getProConditionMap(param, query.getCurrentCompanyId(), query.getAccountId());
        List<ProjectConditionDTO> conditionDTOS = projectConditionService.selProjectConditionList(proCondition);
        if (0 < conditionDTOS.size()) {
            columnCodes = conditionDTOS.get(0).getCode();
        }
        result.put("columnCodes", columnCodes);

        int total = 0;
        query.setNeedSearchBuildType(true);
        List<ProjectTableDTO> data = this.getProjectsByPage(query);
        //   this.detailProjectsPersonAndDesignStatus(data); //界面上面没有使用，暂时屏蔽
        if(!CollectionUtils.isEmpty(data)){
            total = projectDao.getProjectListByProcessingCount(query);
        }

        //为每个项目行添加合作组织信息，即当前组织发布了签发任务给到的组织，不包含自己
        //这里只查询显示出的项目行，不包括所有项目的合作组织
        if (ObjectUtils.isNotEmpty(data)){
            //把当前公司编号保存到查询的currentCompanyId字段中备用
            if (StringUtils.isEmpty(query.getCurrentCompanyId())){
                query.setCurrentCompanyId((String)param.get("relationCompany"));
            }
            data.forEach(project->{
                CompanyQueryDTO cooperatorCompanyQuery = new CompanyQueryDTO();
                cooperatorCompanyQuery.setProjectId(project.getId());
                cooperatorCompanyQuery.setCurrentCompanyId(query.getCurrentCompanyId());
                //只查询外发的组织
                cooperatorCompanyQuery.setIsPay("1");
                List<CompanyDTO> cooperatorCompanyList = companyDao.listCompanyCooperate(cooperatorCompanyQuery);
                if (ObjectUtils.isNotEmpty(cooperatorCompanyList)){
                    //合并组织名称
                    StringBuilder sb = new StringBuilder();
                    cooperatorCompanyList.forEach(company->{
                        if (sb.length() > 0){
                            sb.append(",");
                        }
                        sb.append(company.getCompanyName());
                    });
                    project.setDesignCompanyName(sb.toString());
                }
            });
        }

        //补充项目列表其他信息
        if (ObjectUtils.isNotEmpty(data)) {
            String columns = columnCodes;
            data.forEach(project->{
                //添加人员信息，如任务负责人、设计人员、校对人员、审核人员
                //任务负责人
                if (columns.contains(ProjectConst.TITLE_PROJECT_MEMBER_TASK_LEADER)){
                    project.setTaskLeaders(getProjectMembers(query.getCompanyId(), project.getId(), ProjectConst.MEMBER_TYPE_TASK_LEADER));
                }
                //设计人员
                if (columns.contains(ProjectConst.TITLE_PROJECT_MEMBER_TASK_DESIGNER)){
                    project.setDesigners(getProjectMembers(query.getCompanyId(), project.getId(), ProjectConst.MEMBER_TYPE_TASK_DESIGN));
                }
                //校对人员
                if (columns.contains(ProjectConst.TITLE_PROJECT_MEMBER_TASK_CHECKER)){
                    project.setCheckers(getProjectMembers(query.getCompanyId(), project.getId(), ProjectConst.MEMBER_TYPE_TASK_CHECK));
                }
                //审核人员
                if (columns.contains(ProjectConst.TITLE_PROJECT_MEMBER_TASK_AUDITOR)){
                    project.setAuditors(getProjectMembers(query.getCompanyId(), project.getId(), ProjectConst.MEMBER_TYPE_TASK_AUDIT));
                }

                //添加费用信息，如合同回款、合同到款等
                //合同回款，合同到款
                if (isIncludeContract(columns)){
                    ProjectCostSingleSummaryDTO cost = getProjectFee(project.getId(),query.getCompanyId(),ProjectCostConst.FEE_TYPE_CONTRACT);
                    if (cost != null) {
                        project.setContract(cost.getPlan());
                        project.setContractReal(cost.getReal());
                    } else {
                        project.setContract(0);
                        project.setContractReal(0);
                    }
                }

                //技术审查费（支出）及到款，技术审查费（收入）及付款
                if (isIncludeTechnical(columns)){
                    //如果是项目乙方，则设置技术审查费（收入），如果不是项目乙方，则设置技术审查费（支出），否则无需设置
                    ProjectCostSingleSummaryDTO cost = getProjectFee(project.getId(),query.getCompanyId(),ProjectCostConst.FEE_TYPE_TECHNICAL);
                    if (cost != null) {
                        if (StringUtils.isSame(project.getCompanyBid(),query.getCompanyId())) {
                            project.setTechnicalGain(cost.getPlan());
                            project.setTechnicalGainReal(cost.getReal());
                            project.setTechnicalPay(0);
                            project.setTechnicalPayReal(0);
                        } else {
                            project.setTechnicalGain(0);
                            project.setTechnicalGainReal(0);
                            project.setTechnicalPay(cost.getPlan());
                            project.setTechnicalPayReal(cost.getReal());
                        }
                    } else {
                        project.setTechnicalGain(0);
                        project.setTechnicalGainReal(0);
                        project.setTechnicalPay(0);
                        project.setTechnicalPayReal(0);
                    }
                }

                //合作设计费（收款）及到款
                if (isIncludeCooperateGain(columns)){
                    ProjectCostSingleSummaryDTO cost = getProjectFee(project.getId(),query.getCompanyId(),ProjectCostConst.FEE_TYPE_COOPERATE_GAIN);
                    if (cost != null) {
                        project.setCooperateGain(cost.getPlan());
                        project.setCooperateGainReal(cost.getReal());
                    } else {
                        project.setCooperateGain(0);
                        project.setCooperateGainReal(0);
                    }
                }

                //其他费用（收入）及到款
                if (isIncludeOtherGain(columns)){
                    ProjectCostSingleSummaryDTO cost = getProjectFee(project.getId(),query.getCompanyId(),ProjectCostConst.FEE_TYPE_IN);
                    if (cost != null) {
                        project.setOtherGain(cost.getPlan());
                        project.setOtherGainReal(cost.getReal());
                    } else {
                        project.setOtherGain(0);
                        project.setOtherGainReal(0);
                    }
                }
                //合作设计费（支出）及付款
                if (isIncludeCooperatePay(columns)){
                    ProjectCostSingleSummaryDTO cost = getProjectFee(project.getId(),query.getCompanyId(),ProjectCostConst.FEE_TYPE_COOPERATE_PAY);
                    if (cost != null) {
                        project.setCooperatePay(cost.getPlan());
                        project.setCooperatePayReal(cost.getReal());
                    } else {
                        project.setCooperatePay(0);
                        project.setCooperatePayReal(0);
                    }
                }
                //其他费用（支出）及付款
                if (isIncludeOtherPay(columns)){
                    ProjectCostSingleSummaryDTO cost = getProjectFee(project.getId(),query.getCompanyId(),ProjectCostConst.FEE_TYPE_OUT);
                    if (cost != null) {
                        project.setOtherPay(cost.getPlan());
                        project.setOtherPayReal(cost.getReal());
                    } else {
                        project.setOtherPay(0);
                        project.setOtherPayReal(0);
                    }
                }
            });
        }


        //添加项目是否可编辑信息
        Map<String, Object> para = setProjectUserPermissionParam((String)param.get("companyId"),(String)param.get("companyUserId"));
        List<PermissionDTO> permissionDTOS = permissionService.getProjectUserPermission(para);
        if (0 < permissionDTOS.size()) {
            result.put("flag", 1);
        } else {
            result.put("flag", 0);
        }

        result.put("data",data);
        result.put("total", total);
        result.put("pageIndex", query.getPageIndex());
        return result ;
    }

    //是否包含合同回款
    private boolean isIncludeContract(String columns){
        TraceUtils.check(columns != null);
        return columns.contains(ProjectConst.TITLE_PROJECT_COST_CONTRACT)
                || columns.contains(ProjectConst.TITLE_PROJECT_COST_CONTRACT_REAL);
    }

    //是否包含合作设计费收款
    private boolean isIncludeCooperateGain(String columns){
        TraceUtils.check(columns != null);
        return columns.contains(ProjectConst.TITLE_PROJECT_COST_COOPERATE_GAIN)
                || columns.contains(ProjectConst.TITLE_PROJECT_COST_COOPERATE_GAIN_REAL);
    }

    //是否包含合作设计费收款
    private boolean isIncludeOtherGain(String columns){
        TraceUtils.check(columns != null);
        return columns.contains(ProjectConst.TITLE_PROJECT_COST_OTHER_GAIN)
                || columns.contains(ProjectConst.TITLE_PROJECT_COST_OTHER_GAIN_REAL);
    }

    //是否包含合作设计费付款
    private boolean isIncludeCooperatePay(String columns){
        TraceUtils.check(columns != null);
        return columns.contains(ProjectConst.TITLE_PROJECT_COST_COOPERATE_PAY)
                || columns.contains(ProjectConst.TITLE_PROJECT_COST_COOPERATE_PAY_REAL);
    }

    //是否包含合作设计费付款
    private boolean isIncludeOtherPay(String columns){
        TraceUtils.check(columns != null);
        return columns.contains(ProjectConst.TITLE_PROJECT_COST_OTHER_PAY)
                || columns.contains(ProjectConst.TITLE_PROJECT_COST_OTHER_PAY_REAL);
    }

    //是否包含技术审查费
    private boolean isIncludeTechnical(String columns){
        TraceUtils.check(columns != null);
        return columns.contains(ProjectConst.TITLE_PROJECT_COST_TECHNICAL_GAIN)
                || columns.contains(ProjectConst.TITLE_PROJECT_COST_TECHNICAL_GAIN_REAL)
                || columns.contains(ProjectConst.TITLE_PROJECT_COST_TECHNICAL_PAY)
                || columns.contains(ProjectConst.TITLE_PROJECT_COST_TECHNICAL_PAY_REAL);
    }

    //获取某项目某类型费用，如合同计划收款、合同到账信息等
    private ProjectCostSingleSummaryDTO getProjectFee(String projectId, String companyId, int feeType){
        final String IS_DETAIL = "1";
        final String IS_NOT_DETAIL = "0";

        ProjectCostSummaryQueryDTO query = new ProjectCostSummaryQueryDTO();
        query.setProjectId(projectId);
        query.setCompanyId(companyId);
        query.setCostType(feeType);
        query.setIsDetail(IS_DETAIL);
        List<ProjectCostSingleSummaryDTO> singleSummaryList = projectCostDao.listProjectCostSummary(query);
        ProjectCostSingleSummaryDTO singleSummary = ObjectUtils.getFirst(singleSummaryList);
        if (singleSummary == null){
            query.setIsDetail(IS_NOT_DETAIL);
            singleSummaryList = projectCostDao.listProjectCostSummary(query);
            singleSummary = ObjectUtils.getFirst(singleSummaryList);
        }
        return singleSummary;
    }

    //获取某项目某类型成员的合并字符串，如任务负责人、设计人员、校对人员、审核人员
    private String getProjectMembers(String companyId, String projectId, Integer memberType){
        //查询此项目此类型成员
        List<ProjectMemberDTO> list = listProjectMember(companyId,projectId,memberType);

        //转换为字符串
        StringBuilder memberBuilder = new StringBuilder();
        if (ObjectUtils.isNotEmpty(list)){
            for (ProjectMemberDTO member : list) {
                if (memberBuilder.length() > 0){
                    memberBuilder.append("、");
                }
                memberBuilder.append(member.getCompanyUserName());
            }
        }
        return memberBuilder.toString();
    }

    //获取某项目某类型成员的列表
    private List<ProjectMemberDTO> listProjectMember(String companyId, String projectId, Integer memberType){
        //查询此项目此类型成员
        MemberQueryDTO query = new MemberQueryDTO();
        query.setCompanyId(companyId);
        query.setProjectId(projectId);
        query.setMemberType(memberType);
        return projectMemberService.listByQuery(query);
    }

    //转换为id-name对
    private List<CoreShowDTO> toCoreShowList(List<ProjectMemberDTO> memberList){
        List<CoreShowDTO> list = new ArrayList<>();
        for (ProjectMemberDTO member : memberList) {
            list.add(new CoreShowDTO(member.getId(),member.getCompanyUserName()));
        }
        return list;
    }

    private List<CoreShowDTO> toCoreShowList(Map<String,String> stringMap){
        List<CoreShowDTO> list = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(stringMap)) {
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                list.add(new CoreShowDTO(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }


    @Override
    public QueryProjectDTO getProjectParam(Map<String, Object> param) throws Exception {
        QueryProjectDTO dto = new QueryProjectDTO();
        if (param.containsKey("projectName")) {
            param.put("projectNameMask", param.get("projectName").toString());
            param.remove("projectName");
        }
        BeanUtilsEx.copyProperties(param, dto);
        if (ObjectUtils.isNotEmpty(param.get("designCompanyName"))){
            List<String> designCompanyNameList = Arrays.asList(param.get("designCompanyName").toString().split(","));
            dto.setDesignCompanyNames(designCompanyNameList);
        }
        if (null != param.get("companyNames") && !"".equals(param.get("companyNames"))) {
            dto.setCompanyId(param.get("companyNames").toString());
        }
        if (null != param.get("partyANames") && !"".equals(param.get("partyANames"))) {
            dto.setConstructCompany(param.get("partyANames").toString());
        }
        if (null != param.get("partyBNames") && !"".equals(param.get("partyBNames"))) {
            dto.setCompanyBid(param.get("partyBNames").toString());
        }
        if (null != param.get("buildTypeNames") && !"".equals(param.get("buildTypeNames"))) {
            dto.setBuildTypeNames(Arrays.asList(param.get("buildTypeNames").toString().split(",")));
            //重新查询
            List<ContentDTO>  buildList = projectDao.listBuildTypeByName(dto);
            List<String> buildIdList = new ArrayList<>();
            buildList.stream().forEach(b->{
                buildIdList.add(b.getId());
            });
            dto.setBuildTypeList(buildIdList);
//            dto.setBuildType(param.get("buildTypeNames").toString());
        }
        if (null != param.get("status") && !"".equals(param.get("status"))) {
            dto.setStatus(Integer.parseInt(param.get("status").toString()));
        }
        if (null != param.get("type") && !"".equals(param.get("type"))) {
            dto.setType(Integer.parseInt(param.get("type").toString()));
        }
        if (null != param.get("keyword") && !"".equals(param.get("keyword"))) {
            dto.setKeyWord(param.get("keyword").toString());
        }
        if (null != param.get("status") && !"".equals(param.get("status"))) {
            dto.setStatus(Integer.parseInt(param.get("status").toString()));
        }
        if ((null != param.get("pageIndex") && !"".equals(param.get("pageIndex")) && (null != param.get("pageSize") && !"".equals(param.get("pageSize"))))) {
            dto.setPageIndex(Integer.parseInt(null != param.get("pageIndex") ? param.get("pageIndex").toString() : "0"));
            dto.setPageSize(Integer.parseInt(null != param.get("pageSize") ? param.get("pageSize").toString() : "0"));
        }
        List<String> memberUserIds = new ArrayList<>();
        List<String> memberTypes = new ArrayList<>();
        Map<String, Object> memberMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        //判断查询条件是否包含经营负责人、经营负责人助理、设计负责人、设计负责人助理
        List<String> projectIds = null;
        if (!"".equals(param.get("busPersonInCharge")) && null != param.get("busPersonInCharge")) {
            memberUserIds.add(param.get("busPersonInCharge").toString());
            paramMap.put("busPersonInCharge", param.get("busPersonInCharge").toString());
        }
        if (!"".equals(param.get("busPersonInChargeAssistant")) && null != param.get("busPersonInChargeAssistant")) {
            memberUserIds.add(param.get("busPersonInChargeAssistant").toString());
            paramMap.put("busPersonInChargeAssistant", param.get("busPersonInChargeAssistant").toString());
        }
        if (!"".equals(param.get("designPersonInCharge")) && null != param.get("designPersonInCharge")) {
            memberUserIds.add(param.get("designPersonInCharge").toString());
            paramMap.put("designPersonInCharge", param.get("designPersonInCharge").toString());
        }
        if (!"".equals(param.get("designPersonInChargeAssistant")) && null != param.get("designPersonInChargeAssistant")) {
            memberUserIds.add(param.get("designPersonInChargeAssistant").toString());
            paramMap.put("designPersonInChargeAssistant", param.get("designPersonInChargeAssistant").toString());
        }
        paramMap.put("memberMap", memberMap);
        paramMap.put("companyId", param.get("companyMainId"));
        if (null != memberUserIds && memberUserIds.size() > 0) {
            projectIds = projectMemberService.getProjectMemberByUserIdAndTyep(paramMap);
            if (null != projectIds && projectIds.size() > 0) {
                dto.setMemberProjects(projectIds);
            } else {
                dto.setMemberStatusType("1");
            }
        }
        return dto;
    }

    private Map<String, Object> setProjectUserPermissionParam(String companyId, String companyUserId) {
        Map<String, Object> para = new HashMap<>();
        para.put("companyUserId", companyUserId);
        para.put("companyId", companyId);
        List<String> codes = new ArrayList<>();
        codes.add(ProjectUserPermissionEnum.ORG_MANAGER.getName());
        codes.add(ProjectUserPermissionEnum.PROJECT_MANAGER.getName());
        codes.add(ProjectUserPermissionEnum.DESIGN_MANAGER.getName());
        codes.add(ProjectUserPermissionEnum.SUPER_PROJECT_EDIT.getName());
        codes.add(ProjectUserPermissionEnum.PROJECT_CHARGE_MANAGER.getName());
        codes.add(ProjectUserPermissionEnum.FINANCE_BACK_FEE.getName());
        codes.add(ProjectUserPermissionEnum.PROJECT_EDIT.getName());
        codes.add(ProjectUserPermissionEnum.PROJECT_OVERVIEW.getName());
        para.put("codes", codes);
        return para;
    }

    private Map<String, Object> getProConditionMap(@RequestBody Map<String, Object> param, String companyId,String userId) {
        Map<String, Object> proCondition = new HashMap<>();
        proCondition.put("companyId", companyId);
        proCondition.put("userId", userId);
        if ("1".equals(param.get("type"))) {
            proCondition.put("type", 0);
        } else {
            proCondition.put("type", 1);
        }
        proCondition.put("status", 0);
        return proCondition;
    }

    public List<ProjectTableDTO> getProjectsByPage(QueryProjectDTO queryProjectDTO) throws Exception {
        if ((queryProjectDTO.getPageIndex() != null) && (queryProjectDTO.getPageSize() != null)) {
            if(queryProjectDTO.getPageIndex()<0){
                queryProjectDTO.setPageIndex(0); //如果前端存储了负数，默认使用0
            }
            if(queryProjectDTO.getPageSize()<1){
                queryProjectDTO.setPageSize(10); //如果前端存储了负数，默认使用10
            }
            queryProjectDTO.setStartLine(queryProjectDTO.getPageIndex() * queryProjectDTO.getPageSize());
            queryProjectDTO.setMaxCount(queryProjectDTO.getPageSize());
        }
        if (queryProjectDTO.getOrgMask() != null) {
            List<String> ids = (queryProjectDTO.getOrgIds() == null) ? (new ArrayList<>()) : queryProjectDTO.getOrgIds();
            List<CompanyEntity> ces = companyDao.getAllCompanyListBySearch(queryProjectDTO.getOrgMask());
            if ((ces != null) && (ces.size() > 0)) {
                ids.addAll(ces.stream().map(BaseEntity::getId).collect(Collectors.toList()));
            }
            List<BusinessPartnerEntity> pes = businessPartnerDao.getListByNameMask(queryProjectDTO.getOrgMask());
            if ((pes != null) && (pes.size() > 0)) {
                ids.addAll(pes.stream().map(BaseEntity::getId).collect(Collectors.toList()));
            }
            queryProjectDTO.setOrgIds(ids);
            //如果找不到匹配的组织，则直接返回
            if (queryProjectDTO.getOrgIds() == null || queryProjectDTO.getOrgIds().size() == 0) {
                return new ArrayList<>();
            }
        }
        List<ProjectTableDTO> list = projectDao.getProjectListByProcessing(queryProjectDTO);
        if(queryProjectDTO.isNeedSearchBuildType()){
            list.stream().forEach(p->{
                p.setBuildName(this.getProjectBuildName(p.getId()));
            });
        }
        return list;
    }

    public void detailProjectsPersonAndDesignStatus(List<ProjectTableDTO> data) {
        for (ProjectTableDTO projectTableDTO : data) {
            Map<String, Object> paraMap = Maps.newHashMap();
            paraMap.put("projectId", projectTableDTO.getId());
            paraMap.put("companyId", projectTableDTO.getCompanyId());
            //处理项目负责人和项目经营人
            List<ProjectMemberDTO> projectManagerDTOList = projectMemberService.listProjectMemberByParam(projectTableDTO.getId(), projectTableDTO.getCompanyId(), null, null);
            for (ProjectMemberDTO projectManagerDTO : projectManagerDTOList) {
                if (projectManagerDTO.getMemberType() == ProjectMemberType.PROJECT_OPERATOR_MANAGER) {
                    projectTableDTO.setBusPersonInCharge(projectManagerDTO.getCompanyUserName());
                }
                if (projectManagerDTO.getMemberType() == ProjectMemberType.PROJECT_DESIGNER_MANAGER) {
                    projectTableDTO.setDesignPersonInCharge(projectManagerDTO.getCompanyUserName());
                }
            }
        }
    }

    /**
     * 方法描述：获取当前公司的甲方
     * 作者：TangY
     * 日期：2016/7/29
     */
    @Override
    public List<ProjectConstructDTO> getProjectConstructList(String companyId) throws Exception {
        //甲方：建设单位
        List<ProjectConstructDTO> projectConstructList = projectConstructDao.getConstructByCompanyId(companyId);
        return projectConstructList;
    }

    /**
     * 方法描述：获取本项目的设计阶段
     * 作者：TangY
     * 日期：2016/7/29

     */
    @Override
    public List<ProjectDesignContentEntity> getDesignContentListByProjectId(String projectId) throws Exception {
        List<ProjectDesignContentEntity> projectDesignContentEntityList = projectDesignContentService.getProjectDesignContentByProjectId(projectId);
        return projectDesignContentEntityList;
    }

    /**
     * 方法描述：查询项目的设计范围
     * 作者：MaoSF
     * 日期：2016/7/28
     */
    @Override
    public List<ProjectDesignRangeDTO> getProjectDesignRangeById(String projectId) throws Exception {
        //项目设计范围
        List<ProjectDesignRangeEntity> projectDesignRangeEntityList = projectDesignRangeDao.getDesignRangeByProjectId(projectId);
        List<ProjectDesignRangeDTO> projectDesignRangeDTOList = new ArrayList<ProjectDesignRangeDTO>();
        projectDesignRangeDTOList = BaseDTO.copyFields(projectDesignRangeEntityList, ProjectDesignRangeDTO.class);
        return projectDesignRangeDTOList;
    }

    @Override
    public Map<String, Object> getProjectBasicNum(ProjectDTO projectDTO) throws Exception {
        int completionsNum = 20;
        int outstandingNum = 0;
        int totalNum = 20;
        //编号
        if (null == projectDTO.getProjectNo() || "".equals(projectDTO.getProjectNo())) {
            completionsNum--;
            outstandingNum++;
        }
        //名字
        if (null == projectDTO.getProjectName() || "".equals(projectDTO.getProjectName())) {
            completionsNum--;
            outstandingNum++;
        }
        //状态
        if (null == projectDTO.getStatus() || "".equals(projectDTO.getStatus())) {
            completionsNum--;
            outstandingNum++;
        }
        //甲方
        if (null == projectDTO.getConstructCompany() || "".equals(projectDTO.getConstructCompany())) {
            completionsNum--;
            outstandingNum++;
        }
        //类型
        if (null == projectDTO.getProjectType() || "".equals(projectDTO.getProjectType())) {
            completionsNum--;
            outstandingNum++;
        }
        //基地面积
        if (null == projectDTO.getBaseArea() || "".equals(projectDTO.getBaseArea())) {
            completionsNum--;
            outstandingNum++;
        }
        //详细地址
        if (StringUtil.isNullOrEmpty(projectDTO.getCity()) || StringUtil.isNullOrEmpty(projectDTO.getProvince()) || StringUtil.isNullOrEmpty(projectDTO.getDetailAddress())) {
            completionsNum--;
            outstandingNum++;
        }
        //乙方
        if (null == projectDTO.getCompanyBid() || "".equals(projectDTO.getCompanyBid())) {
            completionsNum--;
            outstandingNum++;
        }
        //容积率
        if (null == projectDTO.getVolumeRatio() || "".equals(projectDTO.getVolumeRatio())) {
            completionsNum--;
            outstandingNum++;
        }
        //覆盖率
        if (null == projectDTO.getCoverage() || "".equals(projectDTO.getCoverage())) {
            completionsNum--;
            outstandingNum++;
        }
        //总建筑面积
        if (null == projectDTO.getTotalConstructionArea() || "".equals(projectDTO.getTotalConstructionArea())) {
            completionsNum--;
            outstandingNum++;
        }
        //计容面积
        if (null == projectDTO.getCapacityArea() || "".equals(projectDTO.getCapacityArea())) {
            completionsNum--;
            outstandingNum++;
        }
        //绿化率
        if (null == projectDTO.getGreeningRate() || "".equals(projectDTO.getGreeningRate())) {
            completionsNum--;
            outstandingNum++;
        }
        //建筑高度
        if (null == projectDTO.getBuiltHeight() || "".equals(projectDTO.getBuiltHeight())) {
            completionsNum--;
            outstandingNum++;
        }
        //投资估算
        if (null == projectDTO.getInvestmentEstimation() || "".equals(projectDTO.getInvestmentEstimation())) {
            completionsNum--;
            outstandingNum++;
        }
        //建筑层数
        if ((null == projectDTO.getBuiltFloorDown() || "".equals(projectDTO.getBuiltFloorDown())) && (null == projectDTO.getBuiltFloorUp() || "".equals(projectDTO.getBuiltFloorUp()))) {
            completionsNum--;
            outstandingNum++;
        }
        //合同签订
        if (null == projectDTO.getSignDate() || "".equals(projectDTO.getSignDate())) {
            completionsNum--;
            outstandingNum++;
        }
        //合同扫描件
        if (null == projectDTO.getFilePath() || "".equals(projectDTO.getFilePath())) {
            completionsNum--;
            outstandingNum++;
        }
        //设计依据
        /*if (null == projectDTO.getProjectDesignBasisList()) {
            completionsNum--;
            outstandingNum++;
        }*/
        //设计范围
        if (CollectionUtils.isEmpty(projectDTO.getProjectDesignRangeList())) {
            completionsNum--;
            outstandingNum++;
        }
        //设计阶段
        if (null == projectDTO.getProjectDesignContentList()) {
            completionsNum--;
            outstandingNum++;
        }
        Map<String, Object> resuletMap = new HashMap<String, Object>();
        resuletMap.put("totalNum", totalNum);
        resuletMap.put("completionsNum", completionsNum);
        resuletMap.put("outstandingNum", outstandingNum);
        return resuletMap;
    }

    /**
     * 方法描述：删除动态
     * 作者：TangY
     * 日期：2016/7/29
     */
    @Override
    public int deleteProjectDynamics(String dynamicsId) {
        ProjectDynamicsEntity projectDynamicsEntity = new ProjectDynamicsEntity();
        projectDynamicsEntity.setId(dynamicsId);
        projectDynamicsEntity.setStatus(1);
        int i = projectDynamicsDao.updateById(projectDynamicsEntity);
        return i;
    }


    @Override
    public AjaxMessage getSearchBaseData(Map<String, Object> param) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtil.isNullOrEmpty(param.get("companyUserId"))) {
            List<CompanyEntity> companyEntityList = companyDao.getCompanyForProject(param.get("companyId").toString());
            result.put("companyList", companyEntityList);
        } else {
            List<CompanyEntity> companyEntityList = companyDao.getCompanyForMyProject(param.get("companyUserId").toString());
            result.put("companyList", companyEntityList);
        }
        List<DataDictionaryDTO> projectTypeList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_CONSTRUCTFUNCTION);//项目类别

        result.put("projectTypeList", projectTypeList);
        return new AjaxMessage().setCode("0").setInfo("查询成功").setData(result);
    }


    /**
     * 方法描述：获取项目菜单权限
     * 作者：MaoSF
     * 日期：2017/3/18
     */
    @Override
    public Map<String, Object> projectNavigationRole(String projectId, String companyId, String accountId, String companyUserId) throws Exception {
        Map<String, Object> role = new HashMap<String, Object>();
        ProjectEntity project = this.projectDao.selectById(projectId);
        if (project == null){
            return null;
        }
        int deleteFlag = this.handleDeleteRole(project.getCreateBy(), project.getCompanyId(), companyId, accountId);
        if (deleteFlag == 1) {
            role.put("deleteFlag", "1");
        }

        int editFlag = this.handleProjectEditRole(project.getCompanyId(), companyId, accountId, project.getCreateBy());
        if (editFlag == 1) {
            role.put("editFlag", "1");
        }

        int flag = this.handleNavigationRole(projectId, companyId, accountId, companyUserId);
        role.put("flag", flag);
        //---------------合同回款菜单权限-------------
        if (project.getCompanyId().equals(companyId) || companyId.equals(project.getCompanyBid())) {
            if (flag == 1) {
                role.put("flag1", "1");
            }
        }

        //---------------技术审查费菜单权限-------------
        if (!StringUtil.isNullOrEmpty(project.getCompanyBid()) && !project.getCompanyBid().equals(project.getCompanyId()) && (project.getCompanyId().equals(companyId) || companyId.equals(project.getCompanyBid()))) {
            if (flag == 1) {
                role.put("flag2", "1");
            }
        }

        //--------------------合作设计费-------------------------
        List<ProjectTaskRelationEntity> relationEntities = this.projectTaskRelationDao.getProjectTaskRelationByCompanyId(projectId, companyId);
        if (!CollectionUtils.isEmpty(relationEntities)) {//若存在签发，则具有权限
            if (flag == 1) {
                for (ProjectTaskRelationEntity entity : relationEntities) {
                    if (companyId.equals(entity.getFromCompanyId()) || companyId.equals(entity.getToCompanyId())) {
                        role.put("flag3", "1");
                        break;
                    }
                }
            }
        }

        //---------------其他费用菜单权限-------------

        if (flag == 1) {
            role.put("flag4", "1");
        }
        //当前用户是否是当前项目的经营负责人
        ProjectMemberEntity managerEntity = this.getProjectOperaterManager(projectId, companyId);
        if (managerEntity != null && managerEntity.getCompanyUserId().equals(companyUserId)) {
            role.put("managerFlag", "1");
        }
        role.put("projectName", project.getProjectName());
        return role;
    }


    private ProjectMemberEntity getProjectOperaterManager(String projectId, String companyId) throws Exception {
        return this.projectMemberService.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_OPERATOR_MANAGER, null);
    }

    private ProjectMemberEntity getProjectDesignManager(String projectId, String companyId) throws Exception {
        return this.projectMemberService.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_DESIGNER_MANAGER, null);
    }

    @Override
    public Map<String, Object> getCostRoleByCompanyId(String projectId, String companyId) throws Exception {
        Map<String, Object> role = new HashMap<String, Object>();
        ProjectEntity project = this.projectDao.selectById(projectId);

        //---------------合同回款菜单权限-------------
        if (project.getCompanyId().equals(companyId) || companyId.equals(project.getCompanyBid())) {
            role.put("flag1", "1");
        }

        //---------------技术审查费菜单权限-------------
        if (!StringUtil.isNullOrEmpty(project.getCompanyBid()) && !project.getCompanyBid().equals(project.getCompanyId())
                && (project.getCompanyId().equals(companyId) || companyId.equals(project.getCompanyBid()))) {

            role.put("flag2", "1");
        }

        //--------------------合作设计费-------------------------
        List<ProjectTaskRelationEntity> relationEntities = this.projectTaskRelationDao.getProjectTaskRelationByCompanyId(projectId, companyId);
        if (!CollectionUtils.isEmpty(relationEntities)) {//若存在签发，则具有权限
            for (ProjectTaskRelationEntity entity : relationEntities) {
                if (companyId.equals(entity.getFromCompanyId()) || companyId.equals(entity.getToCompanyId())) {
                    role.put("flag3", "1");
                    break;
                }
            }
        }

        //---------------其他费用菜单权限-------------
        role.put("flag4", "1");

        return role;
    }

    private int handleDeleteRole(String projectCreateBy, String projectCompanyId, String companyId, String accountId) throws Exception {
        if (projectCompanyId.equals(companyId)) {
            if (accountId.equals(projectCreateBy)) {
                return 1;
            }
            //企业负责人
            if (permissionService.isOrgManager(companyId, accountId)) {
                return 1;
            }
            //拥有删除项目权限
            if (permissionService.haveProjectDeletePermision(companyId, accountId)) {
                return 1;
            }
        }
        return 0;
    }


    /**
     * 方法描述：项目编辑权限
     * 作者：MaoSF
     * 日期：2017/3/22
     */
    private int handleProjectEditRole(String projectCompanyId, String companyId, String accountId, String createBy) throws Exception {
        if (projectCompanyId.equals(companyId)) {//立项人
            if (accountId.equals(createBy)) {
                return 1;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            List<CompanyUserTableDTO> companyUserList = null;

            //项目基本信息编辑人
            map.put("permissionId", "20");//项目基本信息编辑人权限id
            map.put("companyId", projectCompanyId);
            companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
            if (!CollectionUtils.isEmpty(companyUserList)) {
                for (CompanyUserTableDTO dto : companyUserList) {
                    if (dto.getUserId().equals(accountId)) {//具有编辑权限的人
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    private int handleNavigationRole(String projectId, String companyId, String accountId, String companyUserId) throws Exception {
        //项目经营负责人
        ProjectMemberEntity managerEntity = this.projectMemberService.getOperatorManager(projectId, companyId);
        if (managerEntity != null && managerEntity.getCompanyUserId().equals(companyUserId)) {
            return 1;
        }
        //项目经营负责人
        ProjectMemberEntity assistant = this.projectMemberService.getOperatorAssistant(projectId, companyId);
        if (assistant != null && assistant.getCompanyUserId().equals(companyUserId)) {
            return 1;
        }
        //企业负责人
        if (permissionService.isOrgManager(companyId, accountId)) {
            return 1;
        }
        //财务人员
        if (permissionService.isFinancial(companyId, accountId)) {
            return 1;
        }
        //财务人员
        if (permissionService.isFinancialReceive(companyId, accountId)) {
            return 1;
        }
        return 0;
    }


    public int getTaskState(ProjectTaskDataDTO dataDTO) {
        dataDTO.setTaskState(projectTaskDao.getTaskState(dataDTO.getId()));
        return dataDTO.getTaskState();
    }

    public String getStateHtml(ProjectTaskDataDTO dataDTO) {
        String stateHtml = projectTaskDao.getStateText(dataDTO.getTaskState(), dataDTO.getStartTime(),
                dataDTO.getEndTime(), dataDTO.getCompleteDate());
        dataDTO.setStateHtml(stateHtml);
        return stateHtml;
    }

    /**
     * 方法描述：设置任务状态
     * 作者：MaoSF
     * 日期：2017/4/10
     */
    @Override
    public void setProjectTaskState(ProjectTaskDataDTO dataDTO) throws Exception {

        this.getTaskState(dataDTO);
        this.getStateHtml(dataDTO);
    }

    private AjaxMessage validateSaveOrUpdateProjectDesign(ProjectDesignContentDTO dto) {
        //去除重名的问题
        if (StringUtil.isNullOrEmpty(dto.getId())) {
            ProjectTaskEntity projectTaskEntity = this.projectTaskDao.getProjectTaskByPidAndTaskName(dto.getProjectId(), null, dto.getContentName());
            if (projectTaskEntity != null) {
                return AjaxMessage.failed(dto.getContentName() + "已存在");
            }
        } else {
            if (!StringUtil.isNullOrEmpty(dto.getContentName())) {
                ProjectTaskEntity projectTask = this.projectTaskDao.selectById(dto.getId());
                if (projectTask != null) {
                    ProjectTaskEntity projectTaskEntity = this.projectTaskDao.getProjectTaskByPidAndTaskName(projectTask.getProjectId(), projectTask.getTaskPid(), dto.getContentName());
                    if (projectTaskEntity != null && !projectTask.getId().equals(projectTaskEntity.getId())) {
                        return AjaxMessage.failed(dto.getContentName() + "已存在");
                    }
                }
            }
        }
        return null;
    }

    /**
     * 方法描述：设计阶段添加与修改
     * 作者：MaoSF
     * 日期：2017/4/20
     *
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage saveOrUpdateProjectDesign(ProjectDesignContentDTO designContentDTO) throws Exception {
        //由于产品要求合同信息内的设计任务与任务签发内的签发任务脱钩，因此不必检查是否存在同名
//        AjaxMessage ajaxMessage = this.validateSaveOrUpdateProjectDesign(designContentDTO);
//        if (ajaxMessage != null) {
//            return ajaxMessage;
//        }
        String taskId = null;
        String taskDetailId = null;
        if (StringUtil.isNullOrEmpty(designContentDTO.getId())) {//新增
//            taskId = this.saveTask(designContentDTO.getProjectId(), designContentDTO.getCompanyId(), designContentDTO.getContentName(), 0,
//                    DateUtils.str2Date(designContentDTO.getStartTime(), DateUtils.date_sdf), DateUtils.str2Date(designContentDTO.getEndTime(), DateUtils.date_sdf), designContentDTO.getAccountId());
            //保存基本信息
            Integer seq = this.projectDesignContentDao.getProjectContentMaxSeq(designContentDTO.getProjectId());
            taskDetailId = this.saveTaskDetail(designContentDTO.getProjectId(), designContentDTO.getCompanyId(), designContentDTO.getContentName(), seq,
                    DateUtils.str2Date(designContentDTO.getStartTime(), DateUtils.date_sdf), DateUtils.str2Date(designContentDTO.getEndTime(), DateUtils.date_sdf), designContentDTO.getAccountId());
//            insertProjectProcessTime(designContentDTO, taskId);
            insertProjectProcessTime(designContentDTO, taskDetailId);
            //保存项目动态
            ProjectTaskEntity target = projectTaskDao.selectById(taskId);
            dynamicService.addDynamic(null, target, designContentDTO.getCompanyId(), designContentDTO.getAccountId());
            //通知协同
//            collaborationService.pushSyncCMD_PT(target.getProjectId(), target.getTaskPath(), SyncCmd.PT0);
            //推送消息 给本团队的经营负责人推送消息 MESSAGE_TYPE_302
            this.messageService.sendMessageForProjectManager(new SendMessageDTO(designContentDTO.getProjectId(), designContentDTO.getCompanyId(), designContentDTO.getAccountId(), designContentDTO.getCompanyId(), SystemParameters.MESSAGE_TYPE_302));
        } else {//修改

            //修改任务名称
            ProjectTaskEntity taskEntity = this.projectTaskDao.selectById(designContentDTO.getId());
            //保留原有数据
            ProjectTaskEntity origin = null;
            if (taskEntity != null) {
                origin = new ProjectTaskEntity();
                BeanUtilsEx.copyProperties(taskEntity, origin);
            }
            //此处需要修改
            //       projectDynamicsService.addProjectDynamic(projectDynamicsService.getInsertProjectDynamicDTO(designContentDTO, taskEntity));
            taskEntity.setTaskName(designContentDTO.getContentName());
            this.projectTaskService.updateById(taskEntity);
            //添加项目动态
            dynamicService.addDynamic(origin, taskEntity, designContentDTO.getCompanyId(), designContentDTO.getAccountId());

            //通知协同
            collaborationService.pushSyncCMD_PT(taskEntity.getProjectId(), taskEntity.getTaskPath(), SyncCmd.PT0);
        }

        return AjaxMessage.succeed("保存成功");
    }

    private void insertProjectProcessTime(ProjectDesignContentDTO designContentDTO, String taskId) {
        if (!StringUtil.isNullOrEmpty(designContentDTO.getStartTime()) || !StringUtil.isNullOrEmpty(designContentDTO.getEndTime())) {
            ProjectProcessTimeEntity projectProcessTimeEntity = new ProjectProcessTimeEntity();
            projectProcessTimeEntity.setTargetId(taskId);
            projectProcessTimeEntity.setId(StringUtil.buildUUID());
            projectProcessTimeEntity.setCompanyId(designContentDTO.getCompanyId());
            projectProcessTimeEntity.setStartTime(DateUtils.str2Date(designContentDTO.getStartTime(), DateUtils.date_sdf));
            projectProcessTimeEntity.setEndTime(DateUtils.str2Date(designContentDTO.getEndTime(), DateUtils.date_sdf));
            projectProcessTimeEntity.setType(1);
            projectProcessTimeEntity.setCreateBy(designContentDTO.getAccountId());
            projectProcessTimeEntity.setCreateDate(DateUtils.getDate());
            projectProcessTimeDao.insert(projectProcessTimeEntity);
        }
    }

    /**
     * 获取经营负责人姓名
     * 作者：ZCL
     * 日期：2017/4/25
     */
    @Override
    public String getManagerName(String projectId, String companyId) throws Exception {
        ProjectMemberEntity pm = getProjectOperaterManager(projectId, companyId);
        return getManagerName(pm, companyId);
    }

    @Override
    public String getManagerName(ProjectMemberEntity projectManagerEntity, String companyId) {
        if ((projectManagerEntity == null) || (companyId == null)) return "";
        CompanyUserEntity cue = companyUserDao.selectById(projectManagerEntity.getCompanyUserId());
        if (cue == null) return "";
        return cue.getUserName();
    }

    /**
     * 获取设计负责人姓名
     * 作者：ZCL
     * 日期：2017/4/25
     */
    @Override
    public String getDesignerName(String projectId, String companyId) throws Exception {
        ProjectMemberEntity pm = getProjectDesignManager(projectId, companyId);
        return getDesignerName(pm, companyId);
    }

    @Override
    public String getDesignerName(ProjectMemberEntity projectManagerEntity, String companyId) {
        if ((projectManagerEntity == null) || (companyId == null)) {
            return "";
        }
        CompanyUserEntity cue = companyUserDao.selectById(projectManagerEntity.getCompanyUserId());
        if (cue == null) {
            return "";
        }
        return cue.getUserName();
    }

    /**
     * 保存自定义字段的值
     * 作者：Zhangchengliang
     *
     * @param changedField 自定义字段值及更改者
     */
    @Override
    public void saveCustomProperty(ProjectFieldChangeDTO changedField) {
        ProjectPropertyEntity entity = new ProjectPropertyEntity();
        entity.setId(changedField.getId());
        entity.setFieldValue(changedField.getFieldValue());
        entity.resetUpdateDate();
        entity.setUpdateBy(changedField.getOperatorId());
        projectPropertyDao.updateById(entity);
    }

    @Override
    public List<ProjectWorkingHourTableDTO> getProjectWorking(ProjectWorkingHoursDTO hoursDTO) {
        if (null != hoursDTO.getPageIndex()) {
            int page = (Integer) hoursDTO.getPageIndex();
            int pageSize = (Integer) hoursDTO.getPageSize();
            hoursDTO.setPageIndex(page * pageSize);
            hoursDTO.setPageSize(pageSize);
        }
        return this.projectDao.getProjectWorking(hoursDTO);
    }

    @Override
    public Integer getProjectWorkingCount(ProjectWorkingHoursDTO hoursDTO) {
        return this.projectDao.getProjectWorkingCount(hoursDTO);
    }

    @Override
    public AjaxMessage changeSetUpCompany(ProjectDTO dto) throws Exception {
        //
        ProjectEntity project = this.projectDao.selectById(dto.getId());
        if (!StringUtil.isNullOrEmpty(project.getCompanyBid()) && !project.getCompanyId().equals(project.getCompanyBid())) {

            //处理任务签发的和生产安排，把所有生产安排的东西都可以更换
            TaskChangeCompanyDTO changeDTO = new TaskChangeCompanyDTO();
            changeDTO.setChangeCompanyId(project.getCompanyBid());
            changeDTO.setCompanyId(project.getCompanyId());
            changeDTO.setProjectId(project.getId());
            this.projectTaskService.updateProjectTaskForChangeCompany(changeDTO);
            //删除任务
            this.myTaskService.ignoreMyTask(project.getId(), null, project.getCompanyId(), null);
            //变更立项方
            project.setCompanyId(project.getCompanyBid());
            this.projectDao.updateCompanyId(project);
        }
        return null;
    }

    @Override
    public boolean isEditProject(String projectId, String currentCompanyId, String account) throws Exception {
        ProjectEntity project = this.projectDao.selectById(projectId);
        if (project != null) {
            if (this.handleProjectEditRole(project.getCompanyId(), currentCompanyId, account, project.getCreateBy()) == 1) {
                return true;
            }
        }
        return false;
    }

    private void sendMessageForPartB(String projectId, String companyId, String currentCompanyId, String accountId, List<String> userIds) {
        //推送消息给乙方经营负责人，设计负责人，企业负责人
        MessageEntity m = new MessageEntity();
        m.setMessageType(SystemParameters.MESSAGE_TYPE_PART_B);
        m.setProjectId(projectId);
        m.setUserId(null);
        m.setCompanyId(companyId);
        m.setCreateBy(accountId);
        m.setSendCompanyId(currentCompanyId);
        for (String userId : userIds) {//经营负责人+设计负责人
            if (userId == null) {
                continue;
            }
            m.setUserId(userId);
            this.messageService.sendMessage(m);
        }
        //企业负责人
        CompanyUserTableDTO orgManager = this.companyUserService.getOrgManager(companyId);
        if (orgManager != null) {
            m.setUserId(orgManager.getUserId());
            this.messageService.sendMessage(m);
        }
    }

    /**
     * 描述       查询项目列表
     * 日期       2018/8/24
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<ProjectVariableDTO> listProject(ProjectQueryDTO query) {
        updateQuery(query);

        List<ProjectVariableDTO> mainList =  projectDao.listProjectBasic(query);

        return updateProjectList(mainList,query);
    }

    /**
     * 描述       分页查询项目列表
     * 日期       2018/8/24
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public ProjectListPageDTO listPageProject(ProjectQueryDTO query) {
        long t0 = TraceUtils.enter(query);

        updateQuery(query);

        //查询主列表，包括projectId、过滤、排序、总数信息
        List<ProjectVariableDTO> mainList = projectDao.listProjectBasic(query);
        int count = projectDao.countProject(query);

        long t = TraceUtils.info("读取项目主列表",t0);

        //添加项目是否可编辑信息
        Map<String, Object> para = setProjectUserPermissionParam(query.getCompanyId(),query.getCurrentCompanyUserId());
        List<PermissionDTO> permissionDTOS = permissionService.getProjectUserPermission(para);
        boolean flag = (permissionDTOS != null) && (permissionDTOS.size() > 0);

        t = TraceUtils.info("读取项目可编辑信息",t);

        //创建并返回结果
        ProjectListPageDTO page = new ProjectListPageDTO();
        page.setTotal(count);
        page.setPageIndex(DigitUtils.parseInt(query.getPageIndex()));
        page.setPageSize(DigitUtils.parseInt(query.getPageSize()));
        page.setData(updateProjectList(mainList,query));
        page.setFlag((flag) ? "1" : "0");

        TraceUtils.info("补充项目信息",t);

        TraceUtils.exit(t0);
        return page;
    }

    //更新查询条件
    private ProjectQueryDTO updateQuery(ProjectQueryDTO query){
        //设定要查询的公司编号
        if (StringUtils.isEmpty(query.getCompanyId())){
            query.setCompanyId(query.getCurrentCompanyId());
        }

        //设定要查询的属性
        TitleQueryDTO titleQuery = BeanUtils.createFrom(query,TitleQueryDTO.class);
        titleQuery.setType(DigitUtils.parseInt(query.getType()));
        titleQuery.setWithList(0);
        List<TitleColumnDTO> titleList = projectConditionService.listTitle(titleQuery);
        return getNeedFillColumn(titleList,query);
    }


    //补充在查询中没有填充但需要显示的项目信息
    private List<ProjectVariableDTO> updateProjectList(List<ProjectVariableDTO> mainList,ProjectQueryDTO query){
        List<String> projectList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(mainList)){
            mainList.forEach(project->{
                projectList.add(project.getId());
                //甲方信息
                if (query.getNeedPartA() == 1){
                    project.setPartA(getProjectPartA(project.getId()));
                }

                //乙方信息
                if (query.getNeedPartB() == 1){
                    project.setPartB(getProjectPartB(project.getCompanyBid()));
                }

                //添加费用信息，如合同回款、合同到款等
                //计划款项
                if (query.getNeedPlanCost() == 1){
                    ProjectCostQueryDTO costQuery = new ProjectCostQueryDTO(project.getId(),query.getCompanyId());
                    CostAmountDTO planAmount = projectCostDao.getCostAmountPlan(costQuery);
                    if (planAmount != null) {
                        project.setContract(DigitUtils.parseDouble(planAmount.getContract()));
                        project.setTechnicalGain(DigitUtils.parseDouble(planAmount.getTechnicalGain()));
                        project.setCooperateGain(DigitUtils.parseDouble(planAmount.getCooperateGain()));
                        project.setOtherGain(DigitUtils.parseDouble(planAmount.getOtherGain()));
                        project.setTechnicalPay(DigitUtils.parseDouble(planAmount.getTechnicalPay()));
                        project.setCooperatePay(DigitUtils.parseDouble(planAmount.getCooperatePay()));
                        project.setOtherPay(DigitUtils.parseDouble(planAmount.getOtherPay()));
                    }
                }

                //实际款项
                if (query.getNeedRealCost() == 1){
                    ProjectCostQueryDTO costQuery = new ProjectCostQueryDTO(project.getId(),query.getCompanyId());
                    CostAmountDTO realAmount = projectCostDao.getCostAmountReal(costQuery);
                    if (realAmount != null) {
                        project.setContractReal(DigitUtils.parseDouble(realAmount.getContract()));
                        project.setTechnicalGainReal(DigitUtils.parseDouble(realAmount.getTechnicalGain()));
                        project.setCooperateGainReal(DigitUtils.parseDouble(realAmount.getCooperateGain()));
                        project.setOtherGainReal(DigitUtils.parseDouble(realAmount.getOtherGain()));
                        project.setTechnicalPayReal(DigitUtils.parseDouble(realAmount.getTechnicalPay()));
                        project.setCooperatePayReal(DigitUtils.parseDouble(realAmount.getCooperatePay()));
                        project.setOtherPayReal(DigitUtils.parseDouble(realAmount.getOtherPay()));
                    }
                }
            });
        }

        //填充项目成员列数据
        List<Integer> memberTypeList = this.getProjectMemberType(query);
        if(!CollectionUtils.isEmpty(projectList) && !CollectionUtils.isEmpty(mainList)){
            //读取所有所需成员
            MemberQueryDTO memberQueryDTO = new MemberQueryDTO();
            memberQueryDTO.setCompanyId(query.getCompanyId());
            memberQueryDTO.setMemberTypeList(memberTypeList);
            memberQueryDTO.setProjectList(projectList);
            List<ProjectMemberGroupDTO> memberList = this.projectMemberService.getMemberForProjectList(memberQueryDTO);
            //分配到相应列
            getProjectMemberType(query,mainList,memberList);
        }

        return mainList;
    }

    private void getProjectMemberType(ProjectQueryDTO query,List<ProjectVariableDTO> mainList,List<ProjectMemberGroupDTO> memberList){
        Map<String,String> memberMap = new HashMap<>();
        memberList.stream().forEach(m->{
            memberMap.put(m.getProjectId()+"_"+m.getMemberType(),m.getMemberName());
        });
        mainList.stream().forEach(p->{
            if(query.getNeedBusInCharge()==1){
                p.setBusPersonInCharge(memberMap.get(p.getId()+"_"+"1"));
            }
            if(query.getNeedBusAssistant()==1){
                p.setBusPersonInChargeAssistant(memberMap.get(p.getId()+"_"+"7"));
            }
            if(query.getNeedDesignInCharge()==1){
                p.setDesignPersonInCharge(memberMap.get(p.getId()+"_"+"2"));
            }
            if(query.getNeedDesignAssistant()==1){
                p.setDesignPersonInChargeAssistant(memberMap.get(p.getId()+"_"+"8"));
            }
            if(query.getNeedTaskLeader()==1){
                p.setTaskLeader(memberMap.get(p.getId()+"_"+"3"));
            }
            if(query.getNeedDesigner()==1){
                p.setDesigner(memberMap.get(p.getId()+"_"+"4"));
            }
            if(query.getNeedChecker()==1){
                p.setChecker(memberMap.get(p.getId()+"_"+"5"));
            }
            if(query.getNeedAuditor()==1){
                p.setAuditor(memberMap.get(p.getId()+"_"+"6"));
            }
        });

    }

    private List<Integer> getProjectMemberType(ProjectQueryDTO query){
        List<Integer> memberTypeList = new ArrayList<>();
        if(query.getNeedBusInCharge()==1){
            memberTypeList.add(1);
        }
        if(query.getNeedBusAssistant()==1){
            memberTypeList.add(7);
        }
        if(query.getNeedDesignInCharge()==1){
            memberTypeList.add(2);
        }
        if(query.getNeedDesignAssistant()==1){
            memberTypeList.add(8);
        }
        if(query.getNeedTaskLeader()==1){
            memberTypeList.add(3);
        }
        if(query.getNeedDesigner()==1){
            memberTypeList.add(4);
        }
        if(query.getNeedChecker()==1){
            memberTypeList.add(5);
        }
        if(query.getNeedAuditor()==1){
            memberTypeList.add(6);
        }
        return memberTypeList;
    }

    //获取甲方信息
    private String getProjectPartA(String projectId){
        return enterpriseService.getEnterpriseNameByProjectId(projectId);
    }

    //获取乙方信息
    private String getProjectPartB(String companyId){
        return companyDao.getCompanyName(companyId);
    }


    //获取项目的合作组织信息
    private String getRelationCompany(String projectId, String companyId){
        CompanyQueryDTO cooperatorCompanyQuery = new CompanyQueryDTO();
        cooperatorCompanyQuery.setProjectId(projectId);
        cooperatorCompanyQuery.setCurrentCompanyId(companyId);
        //只查询外发的组织
        cooperatorCompanyQuery.setIsPay("1");
        List<CompanyDTO> cooperatorCompanyList = companyDao.listCompanyCooperate(cooperatorCompanyQuery);
        StringBuilder sb = new StringBuilder();
        if (ObjectUtils.isNotEmpty(cooperatorCompanyList)){
            //合并组织名称
            cooperatorCompanyList.forEach(company->{
                if (sb.length() > 0){
                    sb.append(",");
                }
                sb.append(company.getCompanyName());
            });
        }
        return sb.toString();
    }

    //需要填充项目基本信息
    private ProjectQueryDTO getNeedFillColumn(List<TitleColumnDTO> titleList, ProjectQueryDTO query){
        for (TitleColumnDTO title : titleList) {
            if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_RELATION_COMPANY) {
                query.setNeedRelationCompany(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_PART_A) {
                query.setNeedPartA(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_PART_B) {
                query.setNeedPartB(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_BUILD_TYPE) {
                query.setNeedBuildType(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_STATUS) {
                query.setNeedStatus(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_LEADER) {
                query.setNeedBusInCharge(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_ASSITANT) {
                query.setNeedBusAssistant(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_DESIGN) {
                query.setNeedDesignInCharge(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_DESIGN_ASSISTANT) {
                query.setNeedDesignAssistant(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_TASK_LEADER) {
                query.setNeedTaskLeader(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_TASK_DESIGNER) {
                query.setNeedDesigner(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_TASK_CHECKER) {
                query.setNeedChecker(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_TASK_AUDITOR) {
                query.setNeedAuditor(1);
            } else if ((title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_CONTRACT)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_TECHNICAL_GAIN)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_TECHNICAL_PAY)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_COOPERATE_GAIN)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_COOPERATE_PAY)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_OTHER_GAIN)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_OTHER_PAY)) {
                query.setNeedPlanCost(1);
            } else if ((title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_CONTRACT_REAL)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_TECHNICAL_GAIN_REAL)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_TECHNICAL_PAY_REAL)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_COOPERATE_GAIN_REAL)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_COOPERATE_PAY_REAL)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_OTHER_GAIN_REAL)
                    || (title.getTypeId() == SystemParameters.TITLE_TYPE_PROJECT_OTHER_PAY_REAL)) {
                query.setNeedRealCost(1);
            }
        }
        return query;
    }

    //是功能分类过滤条件
    private boolean isProjectBuildTypeFilter(ProjectQueryDTO query){
        return ObjectUtils.isNotEmpty(query.getBuildNameList());
    }

    //是合作组织过滤条件
    private boolean isRelationCompanyFilter(ProjectQueryDTO query){
        return StringUtils.isNotEmpty(query.getRelationCompany());
    }

    //是人员过滤条件
    private boolean isProjectMemberFilter(ProjectQueryDTO query){
        return StringUtils.isNotEmpty(query.getBusPersonInCharge())
                || StringUtils.isNotEmpty(query.getBusPersonInChargeAssistant())
                || StringUtils.isNotEmpty(query.getDesignPersonInCharge())
                || StringUtils.isNotEmpty(query.getDesignPersonInChargeAssistant())
                || StringUtils.isNotEmpty(query.getTaskLeader())
                || StringUtils.isNotEmpty(query.getDesigner())
                || StringUtils.isNotEmpty(query.getChecker())
                || StringUtils.isNotEmpty(query.getAuditor());
    }

}
