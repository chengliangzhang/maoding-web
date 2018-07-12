package com.maoding.task.service.impl;


import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.maoding.conllaboration.SyncCmd;
import com.maoding.conllaboration.service.CollaborationService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.BaseShowDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.NetFileType;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.BeanUtilsEx;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.dynamic.dao.ZInfoDAO;
import com.maoding.dynamic.service.DynamicService;
import com.maoding.message.dto.QueryMessageDTO;
import com.maoding.message.dto.SendMessageDTO;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import com.maoding.mytask.dao.MyTaskDao;
import com.maoding.mytask.dto.HandleMyTaskDTO;
import com.maoding.mytask.dto.MyTaskDTO;
import com.maoding.mytask.entity.MyTaskEntity;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dto.CompanyDataDTO;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dao.ProjectProcessNodeDao;
import com.maoding.project.dao.ProjectSkyDriverDao;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectProcessNodeEntity;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import com.maoding.project.service.ProjectProcessService;
import com.maoding.project.service.ProjectService;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.project.service.ProjectTaskResponsibleService;
import com.maoding.projectcost.dao.ProjectCostDao;
import com.maoding.projectcost.dto.ProjectCostDTO;
import com.maoding.projectcost.service.ProjectCostService;
import com.maoding.projectmember.dto.ProjectMemberDTO;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.projectmember.service.ProjectMemberService;
import com.maoding.role.service.PermissionService;
import com.maoding.task.dao.ProjectProcessTimeDao;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.dao.ProjectTaskRelationDao;
import com.maoding.task.dto.*;
import com.maoding.task.entity.ProjectProcessTimeEntity;
import com.maoding.task.entity.ProjectTaskEntity;
import com.maoding.task.entity.ProjectTaskRelationEntity;
import com.maoding.task.service.ProjectManagerService;
import com.maoding.task.service.ProjectTaskService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjecManagerServiceImpl
 * 类描述：项目任务
 * 作    者：MaoSF
 * 日    期：2016年12月28日-下午5:28:54
 */
@Service("projectTaskService")
class ProjectTaskServiceImpl extends GenericService<ProjectTaskEntity> implements ProjectTaskService {

    @Autowired
    private ProjectTaskDao projectTaskDao;

    @Autowired
    private ZInfoDAO zInfoDAO;

    @Autowired
    private ProjectProcessTimeDao projectProcessTimeDao;

    @Autowired
    private ProjectTaskResponsibleService projectTaskResponsibleService;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private ProjectTaskRelationDao projectTaskRelationDao;

    @Autowired
    private CollaborationService collaborationService;

    @Autowired
    private ProjectProcessService projectProcessService;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private ProjectManagerService projectManagerService;

    @Autowired
    private ProjectProcessNodeDao projectProcessNodeDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectCostDao projectCostDao;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectCostService projectCostService;

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MyTaskDao myTaskDao;
    @Autowired
    private ProjectSkyDriverDao projectSkyDriverDao;
    @Autowired
    private PermissionService permissionService;


    @Override
    public int insert(ProjectTaskEntity entity) {
        if ((entity.getSeq() == null) || (entity.getSeq() == 0)) {
            entity.setSeq(this.projectTaskDao.getProjectTaskMaxSeq(entity.getProjectId(), entity.getTaskPid()));
        }
        int i = this.projectTaskDao.insert(entity);
        //处理文件
        if (entity.getTaskType() == SystemParameters.TASK_TYPE_PHASE || entity.getTaskType() == SystemParameters.TASK_TYPE_ISSUE) {
            //需要的话建立根目录
            ProjectEntity tmp = createProjectEntityFrom(entity);
            projectSkyDriverService.createProjectFile(tmp);

            projectSkyDriverService.createFileMasterForTask(entity);
            projectSkyDriverService.createFileMasterForArchivedFile(entity);
        }
        return i;
    }

    private ProjectEntity createProjectEntityFrom(ProjectTaskEntity task){
        ProjectEntity dst = new ProjectEntity();
        dst.setId(task.getProjectId());
        dst.setCompanyId(task.getFromCompanyId());
        dst.setCompanyBid(task.getCompanyId());
        return dst;
    }


    @Override
    public int updateById(ProjectTaskEntity entity) {
        int i = this.projectTaskDao.updateById(entity);
        this.updateSkyDriver(entity);
        return i;
    }

    /**
     * 方法描述：发布当前记录后，修改该任务下的子集的taskPid，taskPath(重新设置为正式记录的taskPid)
     * param：publishId:被发布记录的ID，taskPid：新的taskPid,新的taskPath=parentPath+id
     * 作者：MaoSF
     * 日期：2017/6/23
     */
    private void updateModifyTaskPid(String publishId, String taskPid, String parentPath) {

        this.projectTaskDao.updateModifyTaskPid(publishId, taskPid, parentPath);
    }

    public List<ProjectDesignTaskShow> getOperatingTaskShowList(String companyId, String projectId, String companyUserId) throws Exception {
        Map<String, Object> param = Maps.newHashMap();
        List<ProjectTaskDTO> dtos = new ArrayList<ProjectTaskDTO>();

        List<ProjectTaskDTO> projectTaskList = new ArrayList<ProjectTaskDTO>();
        //首先，判断是否是乙方
        ProjectEntity projectEntity = this.projectDao.selectById(projectId);

        //如果是乙方
        if (!StringUtil.isNullOrEmpty(projectEntity.getCompanyBid()) && projectEntity.getCompanyBid().equals(companyId)) {
            //查询所有经营任务
            param.put("projectId", projectId);
            param.put("taskType", "1");
            projectTaskList.addAll(projectTaskDao.selectProjectTaskList(param));
        }

        param.clear();
        param.put("projectId", projectId);
        param.put("relationCompany", companyId);
        projectTaskList.addAll(projectTaskDao.selectProjectTaskList(param));

        if (!CollectionUtils.isEmpty(projectTaskList)) {
            List<String> taskIdList = new ArrayList<String>();
            for (ProjectTaskDTO child : projectTaskList) {
                taskIdList.addAll(Arrays.asList(child.getTaskPath().split("-")));
            }
            param.clear();
            List<String> types = Lists.newArrayList();
            types.add("1");
            types.add("2");
            param.put("taskIdList", taskIdList);
            param.put("types", types);
            param.put("orderByType", "1");
            dtos = this.projectTaskDao.selectProjectTaskList(param);
        }
        List<ProjectDesignTaskShow> projectDesignTaskShowList = this.dealOperatingTaskList(dtos, companyId, projectEntity);
        handleOperatorTaskIsHasChild(projectDesignTaskShowList);

        for (ProjectDesignTaskShow dto : projectDesignTaskShowList) {
            //处理权限
            this.handleTaskDetailRoleFlagForOperate(dto, projectEntity.getCompanyId(), companyId, companyUserId, projectId, dto.getTaskPath());
        }
        return projectDesignTaskShowList;
    }


    private void handleOperatorTaskIsHasChild(List<ProjectDesignTaskShow> projectDesignTaskShowList) {
        Map<String, ProjectDesignTaskShow> taskMap = new HashMap<String, ProjectDesignTaskShow>();
        //1.先保存到map中，id为key值
        for (ProjectDesignTaskShow dto : projectDesignTaskShowList) {
            taskMap.put(dto.getId(), dto);
        }
        //2.判断，当前id的父id是否存在在map中
        for (ProjectDesignTaskShow dto : projectDesignTaskShowList) {
            if (!StringUtil.isNullOrEmpty(dto.getTaskPid())) {
                if (taskMap.containsKey(dto.getTaskPid())) {
                    taskMap.get(dto.getTaskPid()).setIsHasOperatorTask(1);
                }
            }
        }
    }

    /**
     * 方法描述：经营任务分解
     * 作者：CHENZHUJIE
     * 日期：2017/2/24
     *
     * @param dto
     */
    @Override
    public AjaxMessage saveProjectTask2(SaveProjectTaskDTO dto) throws Exception {

        AjaxMessage ajaxMessage = null;
        if (dto.getTaskType() == 0 || dto.getTaskType() == 5) {
            ajaxMessage = this.saveProductionTask(dto);
            // ajaxMessage = saveNotPublishProductionTask(dto);
        } else {
            //ajaxMessage = this.saveOperaterTask(dto);
            ajaxMessage = saveIssueTask(dto);
        }

        return ajaxMessage;
    }

    private AjaxMessage validateSaveProjectTask(SaveProjectTaskDTO dto) {
        if (!StringUtil.isNullOrEmpty(dto.getTaskName()) && dto.getTaskName().length() > 200) {
            return AjaxMessage.failed("任务名称过长");
        }
//        if(StringUtil.isNullOrEmpty(dto.getManagerId())){
//            return AjaxMessage.failed("项目负责人不能为空");
//        }
//        if(StringUtil.isNullOrEmpty(dto.getTaskPid())){
//            return AjaxMessage.failed("父任务id不能为空");
//        }
//        if(dto.getFlag()==1 && dto.getAppOrgId().equals(dto.getManagerId())) {//签发给公司
//            return AjaxMessage.failed("合作任务不能签发给本公司");
//        }

        //去除重名的问题
        if (StringUtil.isNullOrEmpty(dto.getId())) {
            ProjectTaskEntity projectTaskEntity = this.projectTaskDao.getProjectTaskByPidAndTaskName(dto.getProjectId(), dto.getTaskPid(), dto.getTaskName());
            if (projectTaskEntity != null) {
                return AjaxMessage.failed(dto.getTaskName() + "已存在");
            }
        } else {
            if (!StringUtil.isNullOrEmpty(dto.getTaskName())) {
                ProjectTaskEntity projectTask = this.projectTaskDao.selectById(dto.getId());
                if (projectTask != null) {
                    ProjectTaskEntity projectTaskEntity = this.projectTaskDao.getProjectTaskByPidAndTaskName(projectTask.getProjectId(), projectTask.getTaskPid(), dto.getTaskName());
                    if (projectTaskEntity != null && !projectTask.getId().equals(projectTaskEntity.getId()) && !projectTaskEntity.getId().equals(projectTask.getBeModifyId())) {
                        return AjaxMessage.failed(dto.getTaskName() + "已存在");
                    }
                }
            }
        }
        return null;
    }

    /**
     * 方法描述：生产任务分解
     * 作者：CHENZHUJIE
     * 日期：2017/2/24
     */
    public AjaxMessage saveProductionTask(SaveProjectTaskDTO dto) throws Exception {
        //验证
        AjaxMessage responseBean = this.validateSaveProjectTask(dto);
        if (responseBean != null) {
            return responseBean;
        }
        CompanyUserEntity companyUser = this.companyUserDao.getCompanyUserByUserIdAndCompanyId(dto.getAccountId(), dto.getCurrentCompanyId());
        if (companyUser == null) {
            AjaxMessage.failed("保存失败");
        }
        ProjectTaskEntity entity = new ProjectTaskEntity();
        BaseDTO.copyFields(dto, entity);
        //新增
        if (StringUtil.isNullOrEmpty(dto.getId())) {
            entity.setId(StringUtil.buildUUID());
            entity.setTaskStatus("0");
            entity.setCreateBy(dto.getAccountId());
            //查询父任务
            ProjectTaskEntity parentTask = this.projectTaskDao.selectById(dto.getTaskPid());
            if (parentTask != null) {
                entity.setTaskLevel(parentTask.getTaskLevel() + 1);
                entity.setTaskPath(parentTask.getTaskPath() + "-" + entity.getId());
                //自己组织签发给部门后 生产安排分解任务需要带部门
                if (StringUtil.isNullOrEmpty(dto.getOrgId()) && !StringUtil.isNullOrEmpty(parentTask.getOrgId())) {
                    entity.setOrgId(parentTask.getOrgId());
                }
            } else {
                entity.setTaskLevel(1);
                entity.setTaskPath(entity.getId());
            }
            entity.setCompanyId(dto.getCurrentCompanyId());
            entity.setFromCompanyId(dto.getCurrentCompanyId());
            this.insert(entity);
            //添加项目动态
            dynamicService.addDynamic(null, entity, dto.getCurrentCompanyId(), dto.getAccountId());
            //负责人
            String targetId = dto.getDesignerId();
            //保存任务负责人
            if (!StringUtil.isNullOrEmpty(targetId)) {
                this.saveProjectTaskResponsibler(dto, entity.getId(), targetId, dto.getTaskPid(), false);
            }
            if (dto.getTaskType() == 0) {
                //处理设校审
                //设计人
                this.projectProcessService.saveProjectProcessForNewTask(entity, dto.getDesignUserList(), ProjectMemberType.PROJECT_DESIGNER, companyUser.getId());
                //校对
                this.projectProcessService.saveProjectProcessForNewTask(entity, dto.getCheckUserList(), ProjectMemberType.PROJECT_PROOFREADER, companyUser.getId());
                //审核
                this.projectProcessService.saveProjectProcessForNewTask(entity, dto.getExamineUserList(), ProjectMemberType.PROJECT_AUDITOR, companyUser.getId());
            } else {
                //保存设计人。不需要产生任务，不需要推送消息
                this.projectProcessService.saveProjectProcessForDesignTask(entity, ProjectMemberType.PROJECT_DESIGNER, companyUser.getId());
            }
            //处理时间
            this.saveProjectProcessTime(dto, entity.getId());
        } else {
            this.updateProjectTask(dto);
        }
        //创建文件夹
        ProjectTaskEntity taskEntity = new ProjectTaskEntity();
        taskEntity.setProjectId(entity.getProjectId());
        taskEntity.setTaskPid(entity.getTaskPid());
        taskEntity.setCompanyId(entity.getCompanyId());
        taskEntity.setTaskName(entity.getTaskName());
        taskEntity.setId(entity.getId());
        taskEntity.setSeq(entity.getTaskLevel());
        //创建归档文件夹
//        projectSkyDriverService.createFileMasterForProductionFile(taskEntity);
        //返回信息
        return AjaxMessage.succeed("保存成功");
    }

    /**
     * 方法描述：生产任务新增、修改
     * 作者：MaoSF
     * 日期：2017/2/24
     */
    public AjaxMessage saveNotPublishProductionTask(SaveProjectTaskDTO dto) throws Exception {
        //验证
        AjaxMessage responseBean = this.validateSaveProjectTask(dto);
        if (responseBean != null) {
            return responseBean;
        }
        ProjectTaskEntity entity = new ProjectTaskEntity();
        BaseDTO.copyFields(dto, entity);
        //新增
        if (StringUtil.isNullOrEmpty(dto.getId())) {
            entity.setId(StringUtil.buildUUID());
            entity.setTaskStatus("0");
            entity.setCreateBy(dto.getAccountId());

            //查询父任务
            ProjectTaskEntity parentTask = this.projectTaskDao.selectById(dto.getTaskPid());
            if (parentTask != null) {
                //如果是被修改的记录的ID，并且存在正式数据。则新增的数据的父ID应该为正式记录的ID
                if (parentTask.getTaskType() == SystemParameters.TASK_PRODUCT_TYPE_MODIFY && !StringUtil.isNullOrEmpty(parentTask.getBeModifyId())) {
                    ProjectTaskEntity beModifyTask = this.projectTaskDao.selectById(parentTask.getBeModifyId());
                    entity.setTaskPid(beModifyTask.getId());
                    entity.setTaskPath(beModifyTask.getTaskPath() + entity.getId());
                } else {
                    entity.setTaskPath(parentTask.getTaskPath() + "-" + entity.getId());
                }
                entity.setTaskLevel(parentTask.getTaskLevel() + 1);

                //自己组织签发给部门后 生产安排分解任务需要带部门
                if (StringUtil.isNullOrEmpty(dto.getOrgId()) && !StringUtil.isNullOrEmpty(parentTask.getOrgId())) {
                    entity.setOrgId(parentTask.getOrgId());
                }
            } else {
                entity.setTaskLevel(1);
                entity.setTaskPath(entity.getId());
            }

            //负责人
            String targetId = dto.getDesignerId();
            //保存任务负责人
            if (!StringUtil.isNullOrEmpty(targetId)) {
                this.saveProjectTaskResponsibler(dto, entity.getId(), targetId, entity.getId(), true);
            }
            //处理时间
            ProjectProcessTimeEntity time = this.saveProjectProcessTime(dto, entity.getId(), false);
            if (time != null) {
                entity.setStartTime(time.getStartTime());
                entity.setEndTime(time.getEndTime());
            }
            entity.setTaskType(SystemParameters.TASK_PRODUCT_TYPE_MODIFY);
            entity.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);
            entity.setCompanyId(dto.getCurrentCompanyId());
            this.insert(entity);
        } else {
            //此处需要修改
            this.updateProjectTask(dto);
        }
        //返回信息
        return AjaxMessage.succeed("保存成功");
    }

    private AjaxMessage saveIssueTask(SaveProjectTaskDTO dto) throws Exception {
        //验证
        AjaxMessage result = validateSaveProjectTask(dto);
        if (result != null) return result;

        if (dto.getId() == null) {
            return (saveNewIssueTask(dto, null) < 1) ? AjaxMessage.failed("操作失败") : AjaxMessage.succeed("操作成功");
        } else {
            //已经发布的任务，修改名称，时间，任务描述，不需要发布
            if (!StringUtil.isNullOrEmpty(dto.getTaskName()) || !StringUtil.isNullOrEmpty(dto.getTaskRemark())) {
                ProjectTaskEntity entity = new ProjectTaskEntity();
                entity.setId(dto.getId());
                entity.setTaskName(dto.getTaskName());
                entity.setTaskRemark(dto.getTaskRemark());
                entity.setUpdateBy(dto.getAccountId());
                //修改名称
                projectTaskDao.updateById(entity);

                if (!StringUtil.isNullOrEmpty(dto.getTaskName()) ) {
                    //修改文件夹名称
                    Map<String, Object> param = new HashMap<>();
                    param.put("fileName", dto.getTaskName());
                    param.put("taskId", dto.getId());
                    param.put("projectId", dto.getProjectId());
                    param.put("companyId", dto.getCompanyId());
                    param.put("accountId", dto.getAccountId());
                    projectSkyDriverDao.updateSkyDriveForFileName(param);
                }
                return AjaxMessage.succeed("操作成功");
            }

            ProjectTaskEntity entity = projectTaskDao.selectById(dto.getId());
            String dataCompanyId = entity.getCompanyId();//先保存到dataCompanyId，防止copy对象的时候，设置为null了
            if (entity == null) return AjaxMessage.failed("操作失败");
            //此处修改的内容
            //如果当期修改的任务是被签发过来的任务，或许是根任务，并且已发布的版本。则新增
            if (entity.getTaskType() == SystemParameters.TASK_TYPE_ISSUE || entity.getTaskType() == SystemParameters.TASK_TYPE_PHASE) {
                return (saveNewIssueTask(dto, entity) < 1) ? AjaxMessage.failed("操作失败") : AjaxMessage.succeed("操作成功");
            }
            String tempCompanyId = entity.getCompanyId();
            BaseDTO.copyFields(dto, entity);
            if (!StringUtil.isNullOrEmpty(dto.getCompanyId()) && !dto.getCompanyId().equals(tempCompanyId)) {
                //把部门设置为null
                this.projectTaskDao.updateTaskOrgId(entity.getId());
            } else if (dto.getIsChangeOrg() == 1) {
                if (StringUtil.isNullOrEmpty(dto.getOrgId())) {
                    this.projectTaskDao.updateTaskOrgId(entity.getId());
                }
            }
            //如果是把部门转化成组织的情况下也把部门字段设置为null

            //修改业务：当项目已发布的情况下，修改计划进度不需要再次点击发布按钮，直接更新
            if (SystemParameters.TASK_TYPE_ISSUE != entity.getTaskType() && !SystemParameters.TASK_STATUS_VALID.equals(entity.getTaskStatus())) {
                entity.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);
            }
            int rt = 0;
            if (!StringUtil.isNullOrEmpty(dto.getTaskName())) {
                //修改名称
                rt = projectTaskDao.updateByIdOrModifyId(entity);
            } else {
                rt = projectTaskDao.updateByTaskId(entity);
            }

            if (rt < 1) return AjaxMessage.failed("操作失败");
            if ((dto.getStartTime() != null) || (dto.getEndTime() != null)) {
                if (StringUtil.isNullOrEmpty(entity.getCompanyId())) {//防止前端没有传递companyId
                    entity.setCompanyId(dataCompanyId);
                }
                saveTaskTime(entity, dto.getStartTime(), dto.getEndTime(), dto.getMemo(), SystemParameters.PROCESS_TYPE_NOT_PUBLISH);
            }
        }

        return AjaxMessage.succeed("操作成功");
    }


    /**
     * 方法描述：对象信息复制（用于数据记录更新的时候，不存在被修改的记录，则产生一条永不修改的记录数据）
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    @Override
    public String copyProjectTask(SaveProjectTaskDTO dto, ProjectTaskEntity issuedEntity) throws Exception {
        if (dto == null) {
            dto = new SaveProjectTaskDTO();
        }
        dto.setTaskType(4);
        saveNewIssueTask(dto, issuedEntity);
        this.projectMemberService.copyProjectMember(issuedEntity.getId(), dto.getId());
        return dto.getId();
    }

    private int saveNewIssueTask(SaveProjectTaskDTO dto, ProjectTaskEntity issuedEntity) throws Exception {
        int level = 0;
        String path = "";

        ProjectTaskEntity entity = new ProjectTaskEntity();
        BeanUtilsEx.copyProperties(dto, entity);
        entity.setCompanyId(dto.getCompanyId());
        entity.setId(StringUtil.buildUUID());
        //在草稿版本中，fromCompanyId为修改方的草稿版本
        entity.setFromCompanyId(dto.getCurrentCompanyId());//来自当前公司
        if (issuedEntity != null) {
            entity.setBeModifyId(issuedEntity.getId());
            entity.setTaskName(StringUtil.isNullOrEmpty(dto.getTaskName()) ? issuedEntity.getTaskName() : dto.getTaskName());
            entity.setTaskRemark(StringUtil.isNullOrEmpty(dto.getTaskRemark()) ? issuedEntity.getTaskRemark() : dto.getTaskRemark());
            entity.setOrgId(StringUtil.isNullOrEmpty(dto.getOrgId()) ? issuedEntity.getOrgId() : dto.getOrgId());
            entity.setCompanyId(StringUtil.isNullOrEmpty(dto.getCompanyId()) ? issuedEntity.getCompanyId() : dto.getCompanyId());
            entity.setProjectId(StringUtil.isNullOrEmpty(dto.getProjectId()) ? issuedEntity.getProjectId() : dto.getProjectId());
            entity.setTaskPid(StringUtil.isNullOrEmpty(dto.getTaskPid()) ? issuedEntity.getTaskPid() : dto.getTaskPid());
            entity.setStartTime(StringUtil.isNullOrEmpty(dto.getStartTime()) ? issuedEntity.getStartTime() : dto.getStartTime());
            entity.setEndTime(StringUtil.isNullOrEmpty(dto.getEndTime()) ? issuedEntity.getEndTime() : dto.getEndTime());
            entity.setSeq(issuedEntity.getSeq());
            //复制及添加时间
            Map<String, Object> query = new HashMap<>();
            query.put("targetId", issuedEntity.getId());
            List<ProjectProcessTimeEntity> tps = projectProcessTimeDao.getProjectProcessTime(query);
            for (ProjectProcessTimeEntity t : tps) {
                saveTaskTime(entity, t.getStartTime(), t.getEndTime(), t.getMemo(), SystemParameters.PROCESS_TYPE_PLAN);
            }
            copyProcessTime(entity, issuedEntity, true);
            if ((dto.getStartTime() != null) || (dto.getEndTime() != null)) {
                saveTaskTime(entity, dto.getStartTime(), dto.getEndTime(), dto.getMemo(), SystemParameters.PROCESS_TYPE_NOT_PUBLISH);
            }
        } else if ((dto.getStartTime() == null) && (dto.getEndTime() == null)) {
            //添加默认时间
            Map<String, Object> query = new HashMap<>();
            query.put("targetId", entity.getTaskPid());
            ProjectProcessTimeEntity tp = projectProcessTimeDao.getProjectProcessTimeByTargetId(query);
            if (tp != null) {
                saveTaskTime(entity, tp.getStartTime(), tp.getEndTime(), tp.getMemo(), SystemParameters.PROCESS_TYPE_NOT_PUBLISH);
                entity.setStartTime(tp.getStartTime());
                entity.setEndTime(tp.getEndTime());
            }
        } else {
            //添加指定的时间
            saveTaskTime(entity, dto.getStartTime(), dto.getEndTime(), dto.getMemo(), SystemParameters.PROCESS_TYPE_NOT_PUBLISH);
            entity.setStartTime(dto.getStartTime());
            entity.setEndTime(dto.getEndTime());
        }

        //添加任务
        ProjectTaskEntity parentTask = projectTaskDao.selectById(entity.getTaskPid());
        if (parentTask != null) {
            level = parentTask.getTaskLevel();
            path = parentTask.getTaskPath() + "-";
            if (parentTask.getTaskType() == SystemParameters.TASK_TYPE_MODIFY) {//如果是未发布版本
                if (StringUtil.isNullOrEmpty(parentTask.getBeModifyId())) {//生成一个发布版本的数据，并且设置为已发布状态，并且为经营任务
                    ProjectTaskEntity newTask = new ProjectTaskEntity();
                    BaseDTO.copyFields(parentTask, newTask);
                    newTask.setId(StringUtil.buildUUID());
                    newTask.setTaskPath((level == 1 || !parentTask.getTaskPath().contains("-")) ? newTask.getId() : parentTask.getTaskPath().substring(0, parentTask.getTaskPath().lastIndexOf("-")));
                   // newTask.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
                    newTask.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);
                    newTask.setTaskType(level == 1 ? SystemParameters.TASK_TYPE_PHASE : SystemParameters.TASK_TYPE_ISSUE);
                    newTask.setIsOperaterTask(SystemParameters.ISSUE_TASK);
                    //判断是否签发给别的组织
//                    if (!dto.getCompanyId().equals(newTask.getCompanyId())) {
//                        newTask.setCompanyId(parentTask.getCompanyId());
//                    }
                    this.insert(newTask);
                    //复制及添加时间
                    copyProcessTime(newTask, parentTask, true);
//                    if (!SystemParameters.TASK_STATUS_VALID.equals(parentTask.getTaskStatus())) {
//                        parentTask.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
//                        parentTask.setUpdateBy(dto.getAccountId());
//                        parentTask.setBeModifyId(newTask.getId());
//                        parentTask.setIsOperaterTask(SystemParameters.ISSUE_TASK);
//                        projectTaskDao.updateById(parentTask);
//                    }

                    parentTask.setUpdateBy(dto.getAccountId());
                    parentTask.setBeModifyId(newTask.getId());
                    projectTaskDao.updateById(parentTask);

                    path = newTask.getTaskPath() + "-";
                    entity.setTaskPid(newTask.getId());
                } else {
                    ProjectTaskEntity pTask = projectTaskDao.selectById(parentTask.getBeModifyId());
//                    if (!SystemParameters.TASK_STATUS_VALID.equals(pTask.getTaskStatus())) {//如果是未发布状态，则设置为发布状态
//                        pTask.setIsOperaterTask(SystemParameters.ISSUE_TASK);
//                        pTask.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
//                        projectTaskDao.updateById(pTask);
//                    }
                    path = pTask.getTaskPath() + "-";
                    entity.setTaskPid(pTask.getId());
                }
            }
//            if (!SystemParameters.TASK_STATUS_VALID.equals(parentTask.getTaskStatus())) {//如果是未发布状态，则设置为发布状态
//                parentTask.setIsOperaterTask(SystemParameters.ISSUE_TASK);
//                parentTask.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
//                projectTaskDao.updateById(parentTask);
//            }
        }

        entity.setCreateBy(dto.getAccountId());
        entity.setTaskLevel(level + 1);
        entity.setTaskPath(path + entity.getId());
        entity.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);
        entity.setTaskType(SystemParameters.TASK_TYPE_MODIFY);
        if (dto.getTaskType() == SystemParameters.TASK_PRODUCT_TYPE_MODIFY) {
            entity.setTaskType(SystemParameters.TASK_PRODUCT_TYPE_MODIFY);
        }
        //返回新增实体的id，用于调用方法（copyProjectTask）中使用

        dto.setId(entity.getId());
        return insert(entity);
    }

    private ProjectProcessTimeEntity saveTaskTime(ProjectTaskEntity task, Date startTime, Date endTime, String memo, Integer timeType) throws Exception {
        ProjectProcessTimeEntity entity = new ProjectProcessTimeEntity();
        entity.setId(StringUtil.buildUUID());
        entity.setTargetId(task.getId());
        entity.setCompanyId(task.getCompanyId());
        entity.setType(timeType);
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);
        entity.setCreateBy(task.getCreateBy());
        entity.setMemo(memo);
        projectProcessTimeDao.insert(entity);
        return entity;
    }

    private void updateProjectTask(SaveProjectTaskDTO dto) throws Exception {
        ProjectTaskEntity taskEntity = projectTaskDao.selectById(dto.getId());
        //保存修改前的对象
        ProjectTaskEntity oldTask = new ProjectTaskEntity();
        BeanUtilsEx.copyProperties(taskEntity, oldTask);

        if (StringUtil.isNullOrEmpty(dto.getTaskName())) {
            dto.setTaskName(taskEntity.getTaskName());
        }
        if (!taskEntity.getCompanyId().equals(dto.getCompanyId())) {//如果更改了组织
            //删除任务（包含子任务）
            this.deleteProjectTask(taskEntity, dto.getAccountId());

            //把部门设置为null
            this.projectTaskDao.updateTaskOrgId(taskEntity.getId());
            //更新
            BaseDTO.copyFields(dto, taskEntity);
            taskEntity.setTaskStatus("0"); //再更新（由于上面taskStatus设置为1了，所以，此处设置为0.更新为有效）
            taskEntity.setCompanyId(dto.getCompanyId());
            this.updateById(taskEntity);

            //保存项目动态
            dynamicService.addDynamic(oldTask, taskEntity, dto.getCompanyId(), dto.getAccountId());

            if (!dto.getCompanyId().equals(dto.getCurrentCompanyId())) {
                if (!StringUtil.isNullOrEmpty(dto.getManagerId())) { //设计经营负责人
                    this.saveProjectManager(dto.getProjectId(), dto.getId(), dto.getCompanyId(), dto.getManagerId(), dto.getDesignerId(), dto.getAccountId(), dto.getCurrentCompanyId());
                }
                this.saveProjectTaskRelation2(taskEntity, dto.getCurrentCompanyId(), dto.getAccountId());
            } else {
                this.sendTaskToDesignerForPublishTask(dto.getProjectId(), dto.getId(), dto.getCompanyId(), dto.getCurrentCompanyId(), dto.getAccountId());
            }

        } else {
            ProjectMemberEntity manager = this.projectMemberService.getTaskDesigner(dto.getId());
            if (!StringUtil.isNullOrEmpty(dto.getDesignerId()) && manager != null) {
                if (!dto.getDesignerId().equals(manager.getCompanyUserId())) {//说明更改了任务负责人
                    //保存任务负责人
                    this.saveProjectTaskResponsibler(dto, dto.getId(), dto.getDesignerId(), dto.getId(), false);
                    //忽略原有任务负责人的任务
                    this.myTaskService.ignoreMyTask(taskEntity.getId(), SystemParameters.PRODUCT_TASK_RESPONSE, manager.getCompanyUserId());
                }
            }
            //更新
            BaseDTO.copyFields(dto, taskEntity);
            taskEntity.setTaskStatus("0"); //再更新（由于上面taskStatus设置为1了，所以，此处设置为0.更新为有效）
            taskEntity.setCompanyId(dto.getCompanyId());
            taskEntity.setTaskName(dto.getTaskName());
            this.updateById(taskEntity);

            //保存项目动态
            dynamicService.addDynamic(oldTask, taskEntity, dto.getCompanyId(), dto.getAccountId());
        }

    }


    /**
     * 方法描述：删除任务
     * 作者：MaoSF
     * 日期：2017/1/3
     */
    public AjaxMessage deleteProjectTask(ProjectTaskEntity task, String accountId) throws Exception {

        String id = task.getId();
        if (task.getBeModifyId() != null) {
            id = task.getBeModifyId();
        }
        //更改签发的数据
        List<ProjectTaskEntity> list = this.projectTaskDao.getProjectTaskByTaskPath(id);//查询当前节点下面所有的子任务（包含自己）

        //把设计阶段下面的所以任务都设置为无效
        ProjectTaskEntity projectTaskEntity = new ProjectTaskEntity();
        projectTaskEntity.setTaskPath(id);
        projectTaskEntity.setTaskStatus("1");
        projectTaskEntity.setUpdateBy(accountId);
        projectTaskDao.updateProjectTaskStatus(projectTaskEntity);

        //删除计划时间
        this.projectProcessTimeDao.deleteByTaskId(id);

        //判断是否存在签发给本组织的生产任务，如果没有，则清理设计负责人的个人任务
        Integer count = projectTaskDao.getSubProductTaskCountByTaskId(task.getTaskPid());
        if (count == 0) {
            MyTaskEntity entity = myTaskDao.getMyTaskForDesignManagerByCompanyIdAndProjectId(task.getCompanyId(), task.getProjectId());
            if (entity != null) {
                myTaskService.ignoreMyTask(entity.getTargetId());
            }
        }


        List<String> taskIdList = new ArrayList<String>();
        List<String> companyList = new ArrayList<String>();//记录公司id
        //签发的记录（用来保存被删除记录的签发记录，最后匹配是否还存在其他签发（同项目，同A--B的签发记录，用于判断是否删除合作设计费））
        List<ProjectTaskRelationEntity> taskRelationList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (ProjectTaskEntity taskEntity : list) {
                taskIdList.add(taskEntity.getId());
                companyList.add(taskEntity.getCompanyId());
                //忽略任务
                this.myTaskService.ignoreMyTask(taskEntity.getId());
                //删除流程
                this.projectProcessService.deleteProcessByTaskId(taskEntity.getId());

                //查询签发记录
                Map<String, Object> stringMap = new HashMap<>();
                stringMap.put("taskId", taskEntity.getId());
                ProjectTaskRelationEntity taskRelation = this.projectTaskRelationDao.selectTaskRelationByTaskId(stringMap);
                if (taskRelation != null) {
                    taskRelationList.add(taskRelation);
                }

                //负责人移除群
                this.projectMemberService.deleteProjectMember(ProjectMemberType.PROJECT_TASK_RESPONSIBLE, taskEntity.getId());

            }
            Map<String, Object> map = new HashMap<>();
            map.put("relationStatus", "1");
            map.put("taskIdList", taskIdList);
            this.projectTaskRelationDao.updateTaskRelationStatus(map);

            ProjectEntity projectEntity = projectDao.selectById(task.getProjectId());
            if (projectEntity != null && !StringUtil.isNullOrEmpty(task.getCompanyId()) && !projectEntity.getCompanyId().equals(task.getCompanyId()) && !task.getCompanyId().equals(projectEntity.getCompanyBid())) {
                //处理经营负责人和设计负责人
                List<ProjectTaskRelationEntity> relationEntities = this.projectTaskRelationDao.getProjectTaskRelationByCompanyId(task.getProjectId(), task.getCompanyId());
                if (CollectionUtils.isEmpty(relationEntities)) {
                    this.projectManagerService.deleteProjectManage(task.getProjectId(), task.getCompanyId());
                    //
                }
            }

            //删除费用节点
            this.handleProjectTaskRelationForCooperatorFee(projectEntity, taskRelationList, accountId);
        }

        this.projectSkyDriverService.updateSkyDriveStatus(id, accountId);

        //删除消息
        this.messageService.deleteMessage(id);

        if (!CollectionUtils.isEmpty(companyList)) {
            //发送通知
            this.projectService.sendMsgToRelationCompanyUser(companyList);
        }

        if (!task.getId().equals(id)) {
            deleteIssueEditTask(task.getId(), accountId);
        }

        //返回信息
        return AjaxMessage.succeed("删除成功").setInfo("删除成功");
    }

    private void deleteIssueEditTask(String id, String accountId) {
        //删除任务
        ProjectTaskEntity projectTaskEntity = new ProjectTaskEntity();
        projectTaskEntity.setTaskPath(id);
        projectTaskEntity.setTaskStatus("1");
        projectTaskEntity.setUpdateBy(accountId);
        projectTaskDao.updateProjectTaskStatus(projectTaskEntity);

        //删除计划时间
        this.projectProcessTimeDao.deleteByTaskId(id);
    }

    /**
     * 方法描述：删除任务的时候，处理合作设计费
     * 作者：MaoSF
     * 日期：2017/5/3
     */
    private void handleProjectTaskRelationForCooperatorFee(ProjectEntity projectEntity, List<ProjectTaskRelationEntity> taskRelationList, String accountId) throws Exception {

        for (ProjectTaskRelationEntity taskRelationEntity : taskRelationList) {
            //查询签发记录
            Map<String, Object> stringMap = new HashMap<>();
            stringMap.put("fromCompanyId", taskRelationEntity.getFromCompanyId());
            stringMap.put("toCompanyId", taskRelationEntity.getToCompanyId());
            stringMap.put("projectId", projectEntity.getId());
            //处理经营负责人和设计负责人
            List<ProjectTaskRelationEntity> relationEntities = this.projectTaskRelationDao.getTaskRelationParam(stringMap);
            if (CollectionUtils.isEmpty(relationEntities)) {//如果为空，则删除合作设计费
                stringMap.put("type", "3"); //合作设计费的标示
                List<ProjectCostDTO> costList = this.projectCostDao.selectByParam(stringMap);
                for (ProjectCostDTO costDTO : costList) {
                    this.projectCostService.deleteProjectCost(costDTO.getId(), accountId);
                }
            }
        }
    }

    /**
     * 方法描述：删除任务
     * 作者：MaoSF
     * 日期：2017/1/3
     *
     * @param task
     * @param accountId
     * @param:
     * @return:
     */
    public AjaxMessage deleteProjectTaskForDesignContent(ProjectTaskEntity task, String accountId) throws Exception {

        String id = task.getId();
        if (task.getBeModifyId() != null) {
            id = task.getBeModifyId();
        }
        //更改签发的数据
        List<ProjectTaskEntity> list = this.projectTaskDao.getProjectTaskByTaskPath(id);//查询当前节点下面所有的子任务（包含自己）

        //把设计阶段下面的所以任务都设置为无效
        ProjectTaskEntity projectTaskEntity = new ProjectTaskEntity();
        projectTaskEntity.setTaskPath(id);
        projectTaskEntity.setTaskStatus("1");
        projectTaskEntity.setUpdateBy(accountId);
        projectTaskDao.updateProjectTaskStatus(projectTaskEntity);

        //删除计划时间
        this.projectProcessTimeDao.deleteByTaskId(id);

        List<String> taskIdList = new ArrayList<String>();
        List<String> companyList = new ArrayList<String>();//记录公司id
        //签发的记录（用来保存被删除记录的签发记录，最后匹配是否还存在其他签发（同项目，同A--B的签发记录，用于判断是否删除合作设计费））
        List<ProjectTaskRelationEntity> taskRelationList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (ProjectTaskEntity taskEntity : list) {
                if (taskEntity.getTaskType() != 1) {
                    taskIdList.add(taskEntity.getId());
                }
                companyList.add(taskEntity.getCompanyId());
                //忽略任务
                this.myTaskService.ignoreMyTask(taskEntity.getId());
                //删除流程
                this.projectProcessService.deleteProcessByTaskId(taskEntity.getId());

                //查询签发记录
                Map<String, Object> stringMap = new HashMap<>();
                stringMap.put("taskId", taskEntity.getId());
                ProjectTaskRelationEntity taskRelation = this.projectTaskRelationDao.selectTaskRelationByTaskId(stringMap);
                if (taskRelation != null) {
                    taskRelationList.add(taskRelation);
                }

                //负责人移除群
                this.projectMemberService.deleteProjectMember(ProjectMemberType.PROJECT_TASK_RESPONSIBLE, taskEntity.getId());
            }
            if (!CollectionUtils.isEmpty(taskIdList)) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("relationStatus", "1");
                map.put("taskIdList", taskIdList);
                this.projectTaskRelationDao.updateTaskRelationStatus(map);
            }


            ProjectEntity projectEntity = projectDao.selectById(task.getProjectId());
            if (projectEntity != null && StringUtil.isNullOrEmpty(task.getCompanyId()) && !projectEntity.getCompanyId().equals(task.getCompanyId()) && !task.getCompanyId().equals(projectEntity.getCompanyBid())) {
                //处理经营负责人和设计负责人
                List<ProjectTaskRelationEntity> relationEntities = this.projectTaskRelationDao.getProjectTaskRelationByCompanyId(task.getProjectId(), task.getCompanyId());
                if (CollectionUtils.isEmpty(relationEntities)) {
                    this.projectManagerService.deleteProjectManage(task.getProjectId(), task.getCompanyId());
                }
            }

            //删除费用节点
            this.handleProjectTaskRelationForCooperatorFee(projectEntity, taskRelationList, accountId);
        }

        //删除消息
        this.messageService.deleteMessage(id);

        if (!CollectionUtils.isEmpty(companyList)) {
            //发送通知
            this.projectService.sendMsgToRelationCompanyUser(companyList);
        }

        //删除文档库
        this.projectSkyDriverService.updateSkyDriveStatus(id, accountId);
        if (!task.getId().equals(id)) {
            deleteIssueEditTask(task.getId(), accountId);
        }
        //返回信息
        return AjaxMessage.succeed("删除成功").setInfo("删除成功");
    }

    /**
     * 方法描述：删除任务
     * 作者：MaoSF
     * 日期：2017/1/3
     */
    @Override
    public AjaxMessage deleteProjectTaskById(String id, String accountId, String currentCompanyId) throws Exception {
        if (StringUtil.isNullOrEmpty(id)) return AjaxMessage.failed("无效的任务");
        ProjectTaskEntity pTask = this.projectTaskDao.selectById(id);
        if (pTask == null) {
            return AjaxMessage.failed("无效的任务");
        }
        AjaxMessage ajaxMessage;
        if (StringUtil.isNullOrEmpty(pTask.getTaskPid())) {//如果是根任务，调用删除设计阶段的接口
            ajaxMessage = deleteProjectTaskByIdForDesignContent(id, accountId, currentCompanyId);
        } else {
            //删除任务(要删除的人员项目群)
            ajaxMessage = this.deleteProjectTask(pTask, accountId);
            //添加项目动态
            dynamicService.addDynamic(pTask, null, currentCompanyId, accountId);
        }

        //处理父任务，如果父任务是自己组织创建的任务
        if (!StringUtil.isNullOrEmpty(pTask.getTaskPid()) && CollectionUtils.isEmpty(projectTaskDao.listTaskByPid(pTask.getTaskPid()))) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("taskId", pTask.getTaskPid());
            List<ProjectTaskRelationEntity> relationList = this.projectTaskRelationDao.getTaskRelationParam(map);
            ProjectTaskEntity task = this.projectTaskDao.selectById(pTask.getTaskPid());
            if (task != null && SystemParameters.ISSUE_TASK == task.getIsOperaterTask()) {
                //处理草稿版本的数据的状态=未发布状态
                map.clear();
                map.put("beModifyId", task.getId());
                map.put("fromCompanyId", currentCompanyId);
                map.put("taskStatus", "2");
                map.put("isOperaterTask", SystemParameters.TASK_PRODUCT);
                map.put("beModifyIdIsNull", 1);//把beModifyId设置为null
                this.projectTaskDao.updateTaskStatusByParam(map);
                if (CollectionUtils.isEmpty(relationList)) {
                    task.setIsOperaterTask(SystemParameters.TASK_PRODUCT);
                    task.setTaskStatus(SystemParameters.TASK_STATUS_INVALID); //如果不存在转包，则直接删除正式记录，以免查询签发总览，生产总览有问题
                } else {
                    task.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);//如果不存在转包，则直接展示为未发布（当前公司未发布）
                }
                this.projectTaskDao.updateById(task);
            }
        }


        //通知协同
        if (pTask.getTaskType() == SystemParameters.TASK_TYPE_PHASE)
            collaborationService.pushSyncCMD_PT(pTask.getProjectId(), pTask.getTaskPath(), SyncCmd.PT0);
        else if (pTask.getTaskType() == SystemParameters.TASK_TYPE_ISSUE)
            collaborationService.pushSyncCMD_PT(pTask.getProjectId(), pTask.getTaskPath(), SyncCmd.PT1);
        else
            collaborationService.pushSyncCMD_PT(pTask.getProjectId(), pTask.getTaskPath(), SyncCmd.PT2);
        return ajaxMessage;
    }

    /**
     * 方法描述：删除设计阶段的时候调用
     * 作者：MaoSF
     * 日期：2017/4/8
     *
     * @param id
     * @param accountId
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage deleteProjectTaskByIdForDesignContent(String id, String accountId, String currentCompanyId) throws Exception {
        ProjectTaskEntity projectTaskEntity = this.projectTaskDao.selectById(id);
        //添加项目动态
        dynamicService.addDynamic(projectTaskEntity, (ProjectTaskEntity) null, currentCompanyId, accountId);
        //删除任务(要删除的人员项目群)
        AjaxMessage ajaxMessage = this.deleteProjectTaskForDesignContent(projectTaskEntity, accountId);
        return ajaxMessage;
    }

    /**
     * 生产安排设计任务负责人(nodeId：被触发的任务id，如果是新增，则为父任务的id，如果是修改，则为当前任务的id)
     */
    private void saveProjectTaskResponsibler(SaveProjectTaskDTO dto, String id, String targetId, String nodeId, boolean isModify) throws Exception {
        if (isModify) {
            this.projectTaskResponsibleService.insertTaskResponsible(dto.getProjectId(), dto.getCompanyId(), targetId, id, dto.getAccountId());
        } else {
            this.projectTaskResponsibleService.insertTaskResponsible(dto.getProjectId(), dto.getCompanyId(), targetId, id, nodeId, dto.getAccountId(), dto.getCurrentCompanyId());
        }
    }


    /**
     * 方法描述：保存任务计划时间
     * 作者：MaoSF
     * 日期：2017/1/6
     *
     * @param:
     * @return:
     */
    private void saveProjectProcessTime(SaveProjectTaskDTO dto, String id) throws Exception {
        this.saveProjectProcessTime(dto, id, true);
    }

    /**
     * 方法描述：保存任务计划时间
     * 作者：MaoSF
     * 日期：2017/1/6
     *
     * @param:
     * @return:
     */
    private ProjectProcessTimeEntity saveProjectProcessTime(SaveProjectTaskDTO dto, String id, boolean isPublish) throws Exception {
        if (!StringUtil.isNullOrEmpty(dto.getStartTime())) {
            ProjectProcessTimeEntity projectProcessTimeEntity = new ProjectProcessTimeEntity();
            projectProcessTimeEntity.setId(StringUtil.buildUUID());
            projectProcessTimeEntity.setTargetId(id);
            projectProcessTimeEntity.setCompanyId(dto.getCurrentCompanyId());//存储当前公司id
            projectProcessTimeEntity.setType(isPublish ? SystemParameters.PROCESS_TYPE_PLAN : SystemParameters.PROCESS_TYPE_NOT_PUBLISH);
            projectProcessTimeEntity.setStartTime(dto.getStartTime());
            projectProcessTimeEntity.setEndTime(dto.getEndTime());
            projectProcessTimeEntity.setCreateBy(dto.getAccountId());
            projectProcessTimeEntity.setCreateDate(new Date());
            this.projectProcessTimeDao.insert(projectProcessTimeEntity);
            return projectProcessTimeEntity;
        } else {
            //把父级的默认过来,复制一份给子任务
            if (!StringUtil.isNullOrEmpty(dto.getTaskPid())) {
                Map<String, Object> map = new HashMap<>();
                map.put("targetId", dto.getTaskPid());
                ProjectProcessTimeEntity processTime = this.projectProcessTimeDao.getProjectProcessTimeByTargetId(map);
                if (processTime != null) {
                    processTime.setId(StringUtil.buildUUID());
                    processTime.setTargetId(id);
                    processTime.setType(isPublish ? SystemParameters.PROCESS_TYPE_PLAN : SystemParameters.PROCESS_TYPE_NOT_PUBLISH);
                    this.projectProcessTimeDao.insert(processTime);
                }
                return processTime;
            }
        }
        return null;
    }

    private void saveProjectManager(String projectId, String taskId, String companyId, String managerId, String designerId, String accountId, String currentCompanyId) throws Exception {
        ProjectMemberEntity member = this.projectMemberService.getOperatorManager(projectId, companyId);
        if (member == null) {//如果不存在经营负责人，则添加
            if (!StringUtil.isNullOrEmpty(managerId)) {//如果传递了managerId
                this.projectMemberService.saveProjectMember(projectId, companyId, managerId, ProjectMemberType.PROJECT_OPERATOR_MANAGER, accountId, false, currentCompanyId);
            }
        } else {
            sendMessage(projectId, taskId, companyId, managerId, null, accountId, SystemParameters.MESSAGE_TYPE_5, currentCompanyId);
        }
        //查询签发给的公司是否存在签发的记录
        List<ProjectTaskRelationEntity> taskRelationList = this.projectTaskRelationDao.getProjectTaskRelationByCompanyId(projectId, companyId);
        if (CollectionUtils.isEmpty(taskRelationList)) {//若果不存在签发的记录，则推送签发的任务
            // 发送任务
            this.myTaskService.saveMyTask(taskId, SystemParameters.ISSUE_TASK, companyId, managerId, currentCompanyId);
        }
    }

    /**
     * 方法描述：给设计负责人推送任务-->发布后，任务负责人默认为设计负责人
     * 作者：MaoSF
     * 日期：2017/4/20
     */
    private void sendTaskToDesignerForPublishTask(String projectId, String taskId, String companyId, String currentCompanyId, String accountId) throws Exception {
        //如果不为空，则设置任务负责人，默认为设计负责人
        this.projectTaskResponsibleService.insertTaskResponsibleForPublishTask(projectId, companyId, taskId, accountId, currentCompanyId);
    }

    /**
     * 方法描述：在签发任务时设置设计负责人并通知设计负责人
     * 作者：ZCL
     */
    private void notifyIssuedDesigner(String projectId, String taskId, String companyId, String currentCompanyId, String accountId) throws Exception {
        ProjectMemberEntity member = this.projectMemberService.getDesignManager(projectId, companyId);
        if (member == null) {
            //签发到的公司中选择具备设计权限的人员中选择第一个填入设计负责人位置
            List<CompanyUserTableDTO> companyUserList = this.companyUserService.getDesignManager(companyId);
            if (companyUserList.size() > 0) {
                CompanyUserTableDTO u = companyUserList.get(0);
                this.projectMemberService.saveProjectMember(projectId, companyId, u.getId(), ProjectMemberType.PROJECT_DESIGNER_MANAGER, accountId, false, currentCompanyId);
            }
        }
    }

    private MessageEntity sendMessage(String projectId, String taskId, String companyId, String managerId, String userId, String accountId, Integer messageType, String currentCompanyId) throws Exception {
        if (StringUtil.isNullOrEmpty(userId)) {
            CompanyUserEntity userEntity = this.companyUserDao.selectById(managerId);
            if (userEntity != null) {
                userId = userEntity.getUserId();
            }
        }
        if (!StringUtil.isNullOrEmpty(userId)) {
            String messageContent = null;
//            //所有的生产任务已经完成（仅自己生产的），给本组织的设计负责人推送消息：hi，“卯丁科技大厦一期-方案设计....”的设计任务已完成，请你确认，谢谢！
//            put(String.format("%d", MESSAGE_TYPE_407),"hi，“?-?”的设计任务已完成，请你确认，谢谢！" );
//            //本团队所有的生产任务已经完成（包含签发给其他组织的任务）：hi，“卯丁科技大厦一期-方案设计，初步设计....”所有生产任务已完成，请你确认，谢谢！
//            put(String.format("%d", MESSAGE_TYPE_408),"hi，“?-?”所有生产任务已完成，请你确认，谢谢！" );//此处还应该推送任务，任务类型22
//            //合作方 A 给 B的任务全部完成，给A组织的设计负责人推送消息
//            put(String.format("%d", MESSAGE_TYPE_409),"hi，“?-?”的设计任务已完成，请你跟进相关项目收支的经营工作，谢谢！" );
            if (messageType == SystemParameters.MESSAGE_TYPE_407) {
                //查询本组织生产的所有的根任务
                messageContent = this.projectTaskDao.getProductRootTaskName(projectId, currentCompanyId);
            }
            ProjectEntity project = projectDao.selectById(projectId);
            if (messageType == SystemParameters.MESSAGE_TYPE_408) {
                //查询所有签发的任务(当前公司)
                if (currentCompanyId.equals(project.getCompanyId())) {
                    messageContent = this.projectTaskDao.getIssueTaskName(projectId, currentCompanyId, 1, null);
                } else {
                    messageContent = this.projectTaskDao.getIssueTaskName(projectId, currentCompanyId, 2, null);
                }
            }
            if (messageType == SystemParameters.MESSAGE_TYPE_409) {
                //查询companyId--currentCompanyId（签发的任务）
                //查询所有签发的任务(当前公司)
                if (currentCompanyId.equals(project.getCompanyId())) {
                    messageContent = this.projectTaskDao.getIssueTaskName(projectId, currentCompanyId, 1, null);
                } else {
                    messageContent = this.projectTaskDao.getIssueTaskName(projectId, currentCompanyId, 2, companyId);
                }
            }
            MessageEntity m = new MessageEntity();
            m.setUserId(userId);
            m.setProjectId(projectId);
            m.setCompanyId(companyId);
            m.setTargetId(taskId);
            m.setMessageType(messageType);
            m.setCreateBy(accountId);
            m.setSendCompanyId(currentCompanyId);
            m.setMessageContent(messageContent);
            m.setCreateDate(new Date());
            messageService.sendMessage(m);
        }
        return null;
    }


    /**
     * 方法描述：保存签发团队关系
     * 作者：MaoSF
     * 日期：2017/1/6
     */
    private void saveProjectTaskRelation2(ProjectTaskEntity task, String currentCompanyId, String accountId) throws Exception {
        ProjectTaskRelationEntity relationEntity = new ProjectTaskRelationEntity();
        relationEntity.setId(StringUtil.buildUUID());
        relationEntity.setFromCompanyId(currentCompanyId);
        relationEntity.setToCompanyId(task.getCompanyId());
        relationEntity.setTaskId(task.getId());
        relationEntity.setCreateBy(accountId);
        relationEntity.setProjectId(task.getProjectId());
        this.projectTaskRelationDao.insert(relationEntity);
        this.projectCostService.saveProjectCost(task, currentCompanyId);//保存管理的费用
        //发送通知
        List<String> companyList = new ArrayList<String>();
        companyList.add(task.getCompanyId());
        this.projectService.sendMsgToRelationCompanyUser(companyList);
    }


    /**
     * 签发生产排序
     * @param projectTaskDTOList
     */
    public List<ProjectDesignTaskShow> doProjectDesignTaskShow(List<ProjectDesignTaskShow> projectTaskDTOList, String id) {
        List<ProjectDesignTaskShow> list = new ArrayList<ProjectDesignTaskShow>();
        for (ProjectDesignTaskShow projectTaskDTO : projectTaskDTOList) {
            if ("".equals(id)) {
                if (StringUtil.isNullOrEmpty(projectTaskDTO.getTaskPid())) {//生产那块根节点在sql查出来的taskPid设为null
                    list.add(projectTaskDTO);
                    list.addAll(doProjectDesignTaskShow(projectTaskDTOList, projectTaskDTO.getId()));
                }
            }
            if (id.equals(projectTaskDTO.getTaskPid())) {
                list.add(projectTaskDTO);
                list.addAll(doProjectDesignTaskShow(projectTaskDTOList, projectTaskDTO.getId()));
            }
        }
        return list;
    }

    public List<ProjectDesignTaskShow> dealOperatingTaskList(List<ProjectTaskDTO> projectTaskDTOList, String companyId, ProjectEntity projectEntity) throws Exception {
        List<ProjectDesignTaskShow> projectDesignTaskShowList = Lists.newArrayList();
        for (ProjectTaskDTO projectTaskDTO : projectTaskDTOList) {
            if (projectTaskDTO.getTaskType() == 1 || projectTaskDTO.getTaskType() == 2) {
                ProjectDesignTaskShow projectDesignTaskShow = new ProjectDesignTaskShow();
                projectDesignTaskShow.setId(projectTaskDTO.getId());
                projectDesignTaskShow.setTaskName(projectTaskDTO.getTaskName());
                projectDesignTaskShow.setTaskPid(projectTaskDTO.getTaskPid());
                projectDesignTaskShow.setDesignOrg(projectTaskDTO.getToCompanyName());
                projectDesignTaskShow.setDesignOrgId(projectTaskDTO.getCompanyId());
                projectDesignTaskShow.setTaskPath(projectTaskDTO.getTaskPath());
                projectDesignTaskShow.setCompleteDate(projectTaskDTO.getCompleteDate());
                projectDesignTaskShow.setDepartName(projectTaskDTO.getDepartName());
                projectDesignTaskShow.setDepartId(projectTaskDTO.getOrgId());
                projectDesignTaskShow.setNotCompleteCount(projectTaskDTO.getNotCompleteCount());
                projectDesignTaskShow.setIsRootTask(projectTaskDTO.getIsRootTask());
                projectDesignTaskShow.setIsHasChild(projectTaskDTO.getIsHasChild());

                if (!StringUtil.isNullOrEmpty(projectTaskDTO.getCompanyId()) && !StringUtil.isNullOrEmpty(projectTaskDTO.getTaskPid())) {
                    projectDesignTaskShow.setDesignOrg(companyDao.selectById(projectTaskDTO.getCompanyId()).getAliasName());
                }
                Map<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("targetId", projectTaskDTO.getId());
                paramMap.put("type", "2");
                if (companyId.equals(projectEntity.getCompanyBid()) && projectEntity.getCompanyId().equals(projectTaskDTO.getCompanyId())) {//如果是乙方
                    paramMap.put("companyId", projectEntity.getCompanyId());//看到的是立项方的界面
                } else {
                    paramMap.put("companyId", companyId);
                }

                ProjectProcessTimeEntity projectProcessTimeEntity = projectProcessTimeDao.getProjectProcessTimeByTargetId(paramMap);
                if (null != projectProcessTimeEntity) {
                    projectDesignTaskShow.setPlanStartTime(projectProcessTimeEntity.getStartTime());
                    projectDesignTaskShow.setPlanEndTime(projectProcessTimeEntity.getEndTime());
                }

                ProjectProcessTimeEntity projectProcessContractTimeEntity = null;

                if (companyId.equals(projectEntity.getCompanyBid()) && projectEntity.getCompanyId().equals(projectTaskDTO.getCompanyId())) {
                    paramMap.put("companyId", projectEntity.getCompanyId());//看到的是立项方的界面
                    paramMap.put("type", "1");

                    projectProcessContractTimeEntity = projectProcessTimeDao.getProjectProcessTimeByTargetId(paramMap);

                } else if (companyId.equals(projectTaskDTO.getCompanyId())) {
                    paramMap.put("companyId", companyId);
                    paramMap.put("type", "1");
                    projectProcessContractTimeEntity = projectProcessTimeDao.getProjectProcessTimeByTargetId(paramMap);
                }

                if (null != projectProcessContractTimeEntity) {
                    projectDesignTaskShow.setContractEndTime(projectProcessContractTimeEntity.getEndTime());
                    projectDesignTaskShow.setContractStartTime(projectProcessContractTimeEntity.getStartTime());
                }

                this.handleProjectTaskState(projectDesignTaskShow);

                projectDesignTaskShowList.add(projectDesignTaskShow);
            }
        }
        return projectDesignTaskShowList;
    }


    @Override
    public List<ProjectDesignTaskShow> getProjectDesignTaskShowList(String companyId, String projectId, String companyUserId) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(projectId);
        List<ProjectDesignTaskShow> list = new ArrayList<>();
        if (projectEntity == null) {
            return list;
        }

        Map<String, Object> paramMap = new HashedMap();
        paramMap.put("companyId", companyId);
        paramMap.put("projectId", projectId);
        List<ProjectTaskDTO> projectTaskDTOList = projectTaskDao.getMyProjectProductTask(paramMap);
        if (!CollectionUtils.isEmpty(projectTaskDTOList)) {
            list = this.doProjectDesignTaskShow(this.dealDesignTaskList(projectTaskDTOList), "");
        }

        for (ProjectDesignTaskShow dto : list) {
            this.handleIsMyTask(dto, companyId, companyUserId);
        }

        //乙方设计负责人标示
//        boolean isPartBDesinger = false;
//        if (projectEntity != null && companyId.equals(projectEntity.getCompanyBid())) {//如果是乙方
//            ProjectMemberEntity member = this.projectMemberService.getDesignManager(projectId, companyId);
//            //ProjectManagerEntity managerEntity = this.projectManagerDao.getProjectDesignManager(projectId,companyId);
//            if (member != null && member.getCompanyUserId().equals(companyUserId)) {
//                isPartBDesinger = true;
//            }
//        }

        //       boolean isDesignManager = this.isDesignManager(projectId,companyId,companyUserId);
//        for (ProjectDesignTaskShow dto : list) {
//            //处理权限
//            handleTaskDetailRoleFlag(dto, companyId, companyUserId, projectId,isDesignManager, isPartBDesinger);
//        }
        return list;
    }

    /**
     * 增加父任务后的生产安排查询
     */
    private List<ProjectDesignTaskShow> getProjectDesignTaskList(String companyId, String projectId, String companyUserId, String parentTaskId) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(projectId);
        List<ProjectDesignTaskShow> list = new ArrayList<>();
        if (projectEntity == null) {
            return list;
        }
        //查询生产数据
        QueryProjectTaskDTO query = new QueryProjectTaskDTO();
        query.setCompanyId(companyId);
        query.setProjectId(projectId);
        query.setIssueTaskId(parentTaskId);
        List<ProjectDesignTaskShow> projectTaskDTOList = projectTaskDao.getProductTaskList(query);
        //排序
        if (!CollectionUtils.isEmpty(projectTaskDTOList)) {
            list = orderDesignTaskList(projectTaskDTOList, "");
        }
        //查询相关的参与人员
        List<ProjectMemberDTO> memberList = projectMemberService.listProjectMemberByParam(projectId,null);
        Map<String,ProjectTaskProcessNodeDTO> designNodeList = new HashMap<>();
        Map<String,ProjectMemberDTO> designList = new HashMap<>();
        //把数据放入map中，以便获取
        this.getDesignUser(memberList,designNodeList,designList);
        //重新组装数据 人员数据
        for (ProjectDesignTaskShow dto : list) {
            this.setTaskDesignMember(designNodeList,designList,dto,companyId);
            //如果是设计人的任务，则把任务负责人设置为空
            if (dto.getTaskType() == 5) {
                dto.setDesigner(null);
            }
            this.setShowDesignUserName(dto, companyUserId);
            //获取状态
            Map<String,Object> stateMapParam = new HashMap<>();
            stateMapParam.put("taskId",dto.getId());
            stateMapParam.put("projectId",dto.getProjectId());
            dto.setStateMap(projectTaskDao.getTaskStateMap(stateMapParam));
            //设置任务状态文字
            dto.setStatusText(projectTaskDao.getStateText(dto.getTaskState(), dto.getPlanStartTime(), dto.getPlanEndTime(), dto.getCompleteDate()));
//            //是否是我参与的任务
            this.handleIsMyTask(dto, companyId, companyUserId);
        }
        //乙方设计负责人标示
        boolean isPartBDesinger = false;
        if (projectEntity != null && companyId.equals(projectEntity.getCompanyBid())) {//如果是乙方
            ProjectMemberEntity member = this.projectMemberService.getDesignManager(projectId, companyId);
            if (member != null && member.getCompanyUserId().equals(companyUserId)) {
                isPartBDesinger = true;
            }
        }

        boolean isDesignManager = this.isDesignManager(projectId, companyId, companyUserId);
        boolean isEmployeesOfCompany = false;
        CompanyUserEntity companyUserTableDTO = companyUserDao.selectById(companyUserId);
        if (null != companyUserTableDTO && "1".equals(companyUserTableDTO.getAuditStatus())) {//返回是本员工的标识
            isEmployeesOfCompany = true;
        }
        for (ProjectDesignTaskShow dto : list) {
            //处理权限
            handleTaskDetailRoleFlag(dto, companyId, companyUserId, isEmployeesOfCompany, isDesignManager, isPartBDesinger);
        }
        return list;
    }

    /**
     * 原有的生产安排查询
     */
    @Override
    public List<ProjectDesignTaskShow> getProjectDesignTaskList(String companyId, String projectId, String companyUserId) throws Exception {
        return getProjectDesignTaskList(companyId,projectId,companyUserId,null);
    }

    private void getDesignUser(List<ProjectMemberDTO> memberList,Map<String,ProjectTaskProcessNodeDTO> designNodeList,Map<String,ProjectMemberDTO> designList) throws Exception{
        memberList.stream().forEach(member->{
            if (member.getMemberType() == ProjectMemberType.PROJECT_TASK_RESPONSIBLE) {
                String key = member.getTargetId() + "_" + member.getMemberType();
                designList.put(key,member);
            }else if(member.getMemberType()>3 && member.getMemberType()<7){//设校审人员
                ProjectDesignUser design =new ProjectDesignUser(); //(ProjectDesignUser) BaseDTO.copyFields(member, ProjectDesignUser.class);
                design.setId(member.getId());
                design.setAccountId(member.getAccountId());
                design.setCompanyUserId(member.getCompanyUserId());
                design.setCellphone(member.getCellphone());
                design.setCompleteTime(member.getCompleteTime());
                design.setEmail(member.getEmail());
                design.setMemberType(member.getMemberType());
                design.setUserName(member.getCompanyUserName());
                String key = member.getNodeId() + "_" + member.getMemberType();
                if (designNodeList.containsKey(member.getNodeId() + "_" + member.getMemberType())) {
                    designNodeList.get(key).getUserList().add(design);
                } else {
                    ProjectTaskProcessNodeDTO nodeUser = new ProjectTaskProcessNodeDTO();
                    nodeUser.setMemberType(member.getMemberType());
                    nodeUser.setTargetId(member.getTargetId());
                    nodeUser.getUserList().add(design);
                    designNodeList.put(key, nodeUser);
                }
            }else {
                String key = member.getCompanyId() + "_" + member.getMemberType();
                designList.put(key,member);
            }
        });
    }

    private void setTaskDesignMember(Map<String,ProjectTaskProcessNodeDTO> designNodeList,Map<String,ProjectMemberDTO> designList,ProjectDesignTaskShow dto,String companyId) throws Exception{
        ProjectMemberDTO taskDesign = designList.get(dto.getId()+"_"+ProjectMemberType.PROJECT_TASK_RESPONSIBLE);
        ProjectMemberDTO design = designList.get(dto.getDesignOrgId()+"_"+ProjectMemberType.PROJECT_DESIGNER_MANAGER);
        if(taskDesign!=null){
            dto.setDesigner(taskDesign);
        }else {
            dto.setDesigner(design);
        }

        if(designNodeList.containsKey(dto.getId()+"_"+ProjectMemberType.PROJECT_DESIGNER)){
            dto.setDesignUser(designNodeList.get(dto.getId()+"_"+ProjectMemberType.PROJECT_DESIGNER));
            dto.getDesignersList().add(dto.getDesignUser());
        }
        if(designNodeList.containsKey(dto.getId()+"_"+ProjectMemberType.PROJECT_PROOFREADER)){
            dto.setCheckUser(designNodeList.get(dto.getId()+"_"+ProjectMemberType.PROJECT_PROOFREADER));
            dto.getDesignersList().add(dto.getCheckUser());
        }
        if(designNodeList.containsKey(dto.getId()+"_"+ProjectMemberType.PROJECT_AUDITOR)){
            dto.setExamineUser(designNodeList.get(dto.getId()+"_"+ProjectMemberType.PROJECT_AUDITOR));
            dto.getDesignersList().add(dto.getExamineUser());
        }
    }


    @Override
    public ProductTaskInfoDTO getProductTaskInfo(QueryProjectTaskDTO query) throws Exception {
        ProductTaskInfoDTO info = new ProductTaskInfoDTO();
        String currentCompanyUserId = query.getCurrentCompanyUserId();
        CompanyUserTableDTO companyUserTableDTO = companyUserService.getCompanyUserById(currentCompanyUserId);
        if (null != companyUserTableDTO && "1".equals(companyUserTableDTO.getAuditStatus())) {//返回是本员工的标识
            info.setIsEmployeesOfCompany(1);
        }
        String accountId = query.getAccountId();
        String projectId = query.getProjectId();
        ProjectEntity projectEntity = this.projectDao.selectById(projectId);
        if(projectEntity==null){
            return info;
        }
        info.setOrgList(this.getSelectOrg(query,projectEntity));
        String companyId = query.getCompanyId();//此句必须在getSelectOrg后面。因为在getSelectOrg中可能改变了companyId

        List<ProjectDesignTaskShow> projectDesignContentShowList = getProjectDesignTaskList(companyId, query.getProjectId(), currentCompanyUserId);
//        Map<String, Object> param = new HashMap<>();
//        param.put("companyId", companyId);
//        param.put("accountId", accountId);
//        param.put("projectId", query.getProjectId());
//        param.put("type", "2");//1.经营界面，2：生产界面
//        List<ProjectManagerDataDTO> projectManagerDataDTOList = this.getProjectTaskCoopateCompany(param);
        //查询乙方
//        ProjectManagerDataDTO partB = getProjectTaskPartBCompany(param);

        ProjectMemberDTO managerEntity = this.projectMemberService.getDesignManagerDTO(projectId, companyId);
        if (managerEntity == null) {
            managerEntity = new ProjectMemberDTO();
        }
        //设置是否可以设置经营负责人的权限
        if (permissionService.isDesignManager(companyId, accountId)) {
            managerEntity.setIsUpdateOperator(1);
        }
        info.setManagerId(managerEntity.getCompanyUserId() != null ? managerEntity.getCompanyUserId() : "");
        info.setProjectDesignContentShowList(projectDesignContentShowList);
//        info.setProjectManagerDataDTOList(projectManagerDataDTOList);
//        info.setPartB(partB);
        info.setProjectCreateBy(null != projectEntity ? projectEntity.getCreateBy() : null);
        info.setProjectCompanyId(null != projectEntity ? projectEntity.getCompanyId() : null);
        info.setProjectManager(managerEntity);
        info.setAssistant(this.projectMemberService.getProjectMemberByParam(projectId, companyId, ProjectMemberType.PROJECT_DESIGNER_MANAGER_ASSISTANT, null));
        info.setProjectName(projectEntity.getProjectName());
        info.setDataCompanyId(companyId);
        return info;
    }

    private void setShowDesignUserName(ProjectProducttaskViewDTO dto, String companyUserId) {
        if (dto.getDesignUser() != null && !CollectionUtils.isEmpty(dto.getDesignUser().getUserList())) {
            dto.setDesignUserName(dto.getDesignUser().getUserList().get(0).getUserName());
            for (ProjectDesignUser user : dto.getDesignUser().getUserList()) {
                if (companyUserId.equals(user.getCompanyUserId())) {
                    dto.setDesignUserName(user.getUserName());
                    break;
                }
            }
        }

        if (dto.getCheckUser() != null && !CollectionUtils.isEmpty(dto.getCheckUser().getUserList())) {
            dto.setCheckUserName(dto.getCheckUser().getUserList().get(0).getUserName());
            for (ProjectDesignUser user : dto.getCheckUser().getUserList()) {
                if (companyUserId.equals(user.getCompanyUserId())) {
                    dto.setCheckUserName(user.getUserName());
                    break;
                }
            }
        }
        if (dto.getExamineUser() != null && !CollectionUtils.isEmpty(dto.getExamineUser().getUserList())) {
            dto.setExamineUserName(dto.getExamineUser().getUserList().get(0).getUserName());
            for (ProjectDesignUser user : dto.getExamineUser().getUserList()) {
                if (companyUserId.equals(user.getCompanyUserId())) {
                    dto.setExamineUserName(user.getUserName());
                    break;
                }
            }
        }
    }

    private void setShowDesignUserName(ProjectDesignTaskShow dto, String companyUserId) {
        if (dto.getDesignUser() != null && !CollectionUtils.isEmpty(dto.getDesignUser().getUserList())) {
            dto.setDesignUserName(dto.getDesignUser().getUserList().get(0).getUserName());
            dto.setProjectDesignUser(dto.getDesignUser().getUserList().get(0));
            for (ProjectDesignUser user : dto.getDesignUser().getUserList()) {
                if (companyUserId.equals(user.getCompanyUserId())) {
                    dto.setProjectDesignUser(user);
                    break;
                }
            }
        }

        if (dto.getCheckUser() != null && !CollectionUtils.isEmpty(dto.getCheckUser().getUserList())) {
            dto.setCheckUserName(dto.getCheckUser().getUserList().get(0).getUserName());
            dto.setProjectCheckUser(dto.getCheckUser().getUserList().get(0));
            for (ProjectDesignUser user : dto.getCheckUser().getUserList()) {
                if (companyUserId.equals(user.getCompanyUserId())) {
                    dto.setProjectCheckUser(user);
                    break;
                }
            }
        }
        if (dto.getExamineUser() != null && !CollectionUtils.isEmpty(dto.getExamineUser().getUserList())) {
            dto.setExamineUserName(dto.getExamineUser().getUserList().get(0).getUserName());
            dto.setProjectExamineUser(dto.getExamineUser().getUserList().get(0));
            for (ProjectDesignUser user : dto.getExamineUser().getUserList()) {
                if (companyUserId.equals(user.getCompanyUserId())) {
                    dto.setProjectExamineUser(user);
                    break;
                }
            }
        }
    }

    public List<ProjectDesignTaskShow> dealDesignTaskList(List<ProjectTaskDTO> projectTaskDTOList) throws Exception {
        List<ProjectDesignTaskShow> designTaskShowList = Lists.newArrayList();
        for (ProjectTaskDTO projectTaskDTO : projectTaskDTOList) {
            ProjectDesignTaskShow projectDesignTaskShow = new ProjectDesignTaskShow();
            projectDesignTaskShow.setTaskType(projectTaskDTO.getTaskType());
            projectDesignTaskShow.setId(projectTaskDTO.getId());
            projectDesignTaskShow.setTaskName(projectTaskDTO.getTaskName());
            projectDesignTaskShow.setTaskPid(projectTaskDTO.getTaskPid());
            projectDesignTaskShow.setIsHasChild(projectTaskDTO.getIsHasChild());
            projectDesignTaskShow.setIsOperaterTask(projectTaskDTO.getIsOperaterTask());
            projectDesignTaskShow.setTaskPath(projectTaskDTO.getTaskPath());
            projectDesignTaskShow.setCompleteDate(projectTaskDTO.getCompleteDate());
            projectDesignTaskShow.setDepartName(projectTaskDTO.getDepartName());
            projectDesignTaskShow.setDepartId(projectTaskDTO.getOrgId());
            projectDesignTaskShow.setNotCompleteCount(projectTaskDTO.getNotCompleteCount());
            projectDesignTaskShow.setIsRootTask(projectTaskDTO.getIsRootTask());
            projectDesignTaskShow.setTaskRemark(projectTaskDTO.getTaskRemark());
            projectDesignTaskShow.setPlanStartTime(projectTaskDTO.getPlanStartTime());
            projectDesignTaskShow.setPlanEndTime(projectTaskDTO.getPlanEndTime());
            projectDesignTaskShow.setDesignersList(projectTaskDTO.getDesignersList());
            projectDesignTaskShow.setBeModifyId(projectTaskDTO.getBeModifyId());
            projectDesignTaskShow.setTaskStatus(projectTaskDTO.getTaskStatus());
            projectDesignTaskShow.setBeModifyTaskType(projectTaskDTO.getBeModifyTaskType());
            ProjectMemberDTO projectTaskResponsible = this.projectMemberService.getTaskDesignerDTO(projectTaskDTO.getId());
            if (projectTaskResponsible != null) {
                projectDesignTaskShow.setSetPersonInChargeId(projectTaskResponsible.getId());
                projectDesignTaskShow.setPersonInCharge(projectTaskResponsible.getCompanyUserName());
                projectDesignTaskShow.setPersonInChargeId(projectTaskResponsible.getCompanyUserId());
            }
            if (!StringUtil.isNullOrEmpty(projectTaskDTO.getCompanyId())) {
                projectDesignTaskShow.setDesignOrg(companyDao.selectById(projectTaskDTO.getCompanyId()).getAliasName());
                projectDesignTaskShow.setDesignOrgId(projectTaskDTO.getCompanyId());
            }
            projectDesignTaskShow.setProduceTaskLevel(projectTaskDTO.getProduceTaskLevel());

            this.handleProjectTaskState(projectDesignTaskShow);
            designTaskShowList.add(projectDesignTaskShow);
        }
        return designTaskShowList;
    }


    @Override
    public List<ProjectManagerDataDTO> getProjectTaskCoopateCompany(Map<String, Object> map) throws Exception {
        String companyId = (String) map.get("companyId");
        String projectId = (String) map.get("projectId");
        String accountId = (String) map.get("accountId");
        String type = (String) map.get("type");//1.经营界面，2：生产界面
        //返回的数组
        List<ProjectManagerDataDTO> list = new ArrayList<>();
        ProjectEntity projectEntity = this.projectDao.selectById(projectId);

        Map<String, ProjectManagerDataDTO> returnMap = new HashMap<String, ProjectManagerDataDTO>();
        if (projectEntity != null) {
            //首先查询立项方
            ProjectManagerDataDTO dto = getProjectManager(projectId, projectEntity.getCompanyId(), companyId, accountId);
            dto.setType(1);
            dto.setTreeId("1");
            returnMap.put(projectEntity.getCompanyId(), dto);
            list.add(dto);
            //查询立项方签发给其他组织
            List<ProjectTaskRelationEntity> taskRelationEntityList = this.projectTaskRelationDao.getProjectTaskRelationByFromCompanyId(projectId, projectEntity.getCompanyId());
            if ("2".equals(type)) {
                int treeId = 2;//从2开始
                if (!CollectionUtils.isEmpty(taskRelationEntityList)) {
                    for (ProjectTaskRelationEntity relationEntity : taskRelationEntityList) {
                        ProjectManagerDataDTO dto1 = this.getProjectManager(projectId, relationEntity.getToCompanyId(), companyId, accountId);
                        dto1.setTreeId(treeId + "");
                        dto1.setPid(dto.getTreeId());
                        treeId++;
                        list.add(dto1);
                        //查询立项方签发给其他组织
                        List<ProjectTaskRelationEntity> taskRelationEntityList2 = this.projectTaskRelationDao.getProjectTaskRelationByFromCompanyId(projectId, relationEntity.getToCompanyId());
                        if (!CollectionUtils.isEmpty(taskRelationEntityList2)) {
                            for (ProjectTaskRelationEntity relationEntity2 : taskRelationEntityList2) {
                                ProjectManagerDataDTO dto2 = this.getProjectManager(projectId, relationEntity2.getToCompanyId(), companyId, accountId);
                                dto2.setTreeId(treeId + "");
                                dto2.setPid(dto1.getTreeId());
                                treeId++;
                                list.add(dto2);
                            }
                        }
                    }
                }
            } else {
                list = getOperatorCompany(taskRelationEntityList, companyId, projectId, dto, accountId);
            }


        }
        return list;
    }

    private List<ProjectManagerDataDTO> getOperatorCompany(List<ProjectTaskRelationEntity> taskRelationEntityList, String companyId, String projectId, ProjectManagerDataDTO dto, String accountId) throws Exception {
        int treeId = 2;//从2开始
        List<ProjectManagerDataDTO> list = new ArrayList<ProjectManagerDataDTO>();
        list.add(dto);
        if (!CollectionUtils.isEmpty(taskRelationEntityList)) {
            for (ProjectTaskRelationEntity relationEntity : taskRelationEntityList) {
                boolean isAdd = false;
                if (relationEntity.getFromCompanyId().equals(companyId) || relationEntity.getToCompanyId().equals(companyId)) {
                    isAdd = true;
                }
                ProjectManagerDataDTO dto1 = this.getProjectManager(projectId, relationEntity.getToCompanyId(), companyId, accountId);
                //查询立项方签发给其他组织
                List<ProjectTaskRelationEntity> taskRelationEntityList2 = this.projectTaskRelationDao.getProjectTaskRelationByFromCompanyId(projectId, relationEntity.getToCompanyId());
                if (!CollectionUtils.isEmpty(taskRelationEntityList2)) {
                    for (ProjectTaskRelationEntity relationEntity1 : taskRelationEntityList2)
                        if (relationEntity1.getFromCompanyId().equals(companyId) || relationEntity1.getToCompanyId().equals(companyId)) {
                            isAdd = true;
                            break;
                        }
                }
                if (isAdd) {
                    dto1.setTreeId(treeId + "");
                    dto1.setPid(dto.getTreeId());
                    treeId++;
                    list.add(dto1);
                }
                if (!CollectionUtils.isEmpty(taskRelationEntityList2)) {
                    for (ProjectTaskRelationEntity relationEntity2 : taskRelationEntityList2) {
                        if (relationEntity2.getFromCompanyId().equals(companyId) || relationEntity2.getToCompanyId().equals(companyId)) {
                            ProjectManagerDataDTO dto2 = this.getProjectManager(projectId, relationEntity2.getToCompanyId(), companyId, accountId);
                            dto2.setTreeId(treeId + "");
                            dto2.setPid(dto1.getTreeId());
                            treeId++;
                            list.add(dto2);
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public ProjectManagerDataDTO getProjectTaskPartBCompany(Map<String, Object> map) throws Exception {
        String companyId = (String) map.get("companyId");
        String projectId = (String) map.get("projectId");
        String accountId = (String) map.get("accountId");
        //返回的数组
        ProjectEntity projectEntity = this.projectDao.selectById(projectId);
        if (null != projectEntity && (!StringUtil.isNullOrEmpty(projectEntity.getCompanyBid()) && !projectEntity.getCompanyId().equals(projectEntity.getCompanyBid()))
                && (companyId.equals(projectEntity.getCompanyId()) || companyId.equals(projectEntity.getCompanyBid()))) {
            ProjectManagerDataDTO partB = getProjectManager(projectId, projectEntity.getCompanyBid(), companyId, accountId);
            partB.setTreeId("0");
            return partB;
        }
        return null;
    }

    private ProjectManagerDataDTO getProjectManager(String projectId, String companyId, String currentCompanyId, String accountId) throws Exception {
        ProjectManagerDataDTO dto = new ProjectManagerDataDTO();
        //查询立项方人员
        CompanyEntity companyEntity = this.companyDao.selectById(companyId);
        if (companyEntity != null) {
            //获取当前项目在当前团队的经营负责人和项目负责人
            List<ProjectMemberDTO> managerList = projectMemberService.listProjectMemberByParam(projectId, companyId, null, null);
            dto.setCompanyName(companyEntity.getAliasName());
            dto.setRealName(companyEntity.getCompanyName());
            dto.setId(companyEntity.getId());
            for (ProjectMemberDTO managerDTO : managerList) {
                if (managerDTO.getMemberType() == ProjectMemberType.PROJECT_OPERATOR_MANAGER) {
                    dto.setOperatorPersonName(managerDTO.getCompanyUserName());
                    dto.setOperatorPersonId(managerDTO.getCompanyUserId());
                }
                if (managerDTO.getMemberType() == ProjectMemberType.PROJECT_DESIGNER_MANAGER) {
                    dto.setDesignPersonName(managerDTO.getCompanyUserName());
                    dto.setDesignPersonId(managerDTO.getCompanyUserId());
                }
            }

            //设置权限
            if (companyId.equals(currentCompanyId)) {
                //验证身份
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("permissionId", "51");//经营负责人权限id
                map.put("companyId", currentCompanyId);
                map.put("userId", accountId);
                List<CompanyUserTableDTO> companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
                if (!CollectionUtils.isEmpty(companyUserList)) {
                    dto.setIsUpdateOperator(1);
                }
                map.put("permissionId", "52");//设计负责人权限id
                companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
                if (!CollectionUtils.isEmpty(companyUserList)) {
                    dto.setIsUpdateDesign(1);
                }
            }
        }
        return dto;
    }

    private void updateSkyDriver(ProjectTaskEntity projectTaskEntity) {
        //处理文件
        if (!StringUtil.isNullOrEmpty(projectTaskEntity.getTaskName())) {
            ProjectSkyDriveEntity skyDriveEntity = this.projectSkyDriverService.getSkyDriveByTaskId(projectTaskEntity.getId());
            if (skyDriveEntity != null) {
                skyDriveEntity.setFileName(projectTaskEntity.getTaskName());
                if (!StringUtil.isNullOrEmpty(projectTaskEntity.getCompanyId())) {
                    skyDriveEntity.setCompanyId(projectTaskEntity.getCompanyId());
                }
                this.projectSkyDriverService.updateById(skyDriveEntity);
            } else {
                //为了获取完整的数据。以防创建的文件不准确
                projectTaskEntity = this.projectTaskDao.selectById(projectTaskEntity.getId());
                if (projectTaskEntity.getTaskType() != 0) {
                    this.projectSkyDriverService.createFileMasterForTask(projectTaskEntity);
                    this.projectSkyDriverService.createFileMasterForArchivedFile(projectTaskEntity);
                }
            }
        }
    }

    @Override
    public AjaxMessage updateByTaskId(ProjectTaskEntity projectTaskEntity) {
        ProjectTaskEntity entity = this.projectTaskDao.selectById(projectTaskEntity.getId());
        SaveProjectTaskDTO dto = new SaveProjectTaskDTO();
        dto.setId(projectTaskEntity.getId());
        dto.setTaskName(projectTaskEntity.getTaskName());
        AjaxMessage ajaxMessage = this.validateSaveProjectTask(dto);
        if (ajaxMessage != null) {
            return ajaxMessage;
        }

        this.projectTaskDao.updateByTaskId(projectTaskEntity);

        //如果是生产任务，同时更新生产记录的任务名称
        if (entity != null && entity.getTaskType() == SystemParameters.TASK_PRODUCT_TYPE_MODIFY && !StringUtil.isNullOrEmpty(entity.getBeModifyId())) {
            projectTaskEntity = new ProjectTaskEntity();
            projectTaskEntity.setId(entity.getBeModifyId());
            projectTaskEntity.setTaskName(dto.getTaskName());
            this.projectTaskDao.updateByTaskId(projectTaskEntity);
        }
//        //保存项目动态
//        dynamicService.addDynamic(taskOld,projectTaskEntity,taskOld.getCompanyId(),taskOld.getCreateBy());
        this.updateSkyDriver(projectTaskEntity);

        //通知协同
        if (entity.getTaskType() == SystemParameters.TASK_TYPE_PHASE)
            collaborationService.pushSyncCMD_PT(entity.getProjectId(), entity.getTaskPath(), SyncCmd.PT0);
        else if (entity.getTaskType() == SystemParameters.TASK_TYPE_ISSUE)
            collaborationService.pushSyncCMD_PT(entity.getProjectId(), entity.getTaskPath(), SyncCmd.PT1);
        else
            collaborationService.pushSyncCMD_PT(entity.getProjectId(), entity.getTaskPath(), SyncCmd.PT2);

        return AjaxMessage.succeed(null);
    }


    @Override
    public AjaxMessage updateByTaskIdNew(ProjectTaskEntity projectTaskEntity) throws Exception {
        ProjectTaskEntity taskEntity = this.projectTaskDao.selectById(projectTaskEntity.getId());
        if (StringUtil.isNullOrEmpty(projectTaskEntity.getId()) || taskEntity == null) {
            return AjaxMessage.failed("操作失败");
        }
        if (taskEntity != null && taskEntity.getTaskType() != SystemParameters.TASK_TYPE_MODIFY) {//则新增一条被修改的记录
            SaveProjectTaskDTO dto = new SaveProjectTaskDTO();
            dto.setTaskName(projectTaskEntity.getTaskName());
            projectTaskEntity.setId(this.copyProjectTask(dto, taskEntity));//此处设置为被修改后的ID
        }
        this.updateByTaskIdStatus(projectTaskEntity, SystemParameters.TASK_STATUS_MODIFIED);
        return AjaxMessage.succeed(null);
    }

    /**
     * 方法描述：修改项目状态
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    @Override
    public AjaxMessage updateByTaskIdStatus(ProjectTaskEntity taskEntity, String taskStatus) throws Exception {
        taskEntity.setTaskStatus(taskStatus);
        this.projectTaskDao.updateByTaskId(taskEntity);
        return null;
    }

    @Override
    public AjaxMessage updateByTaskIdStatus(String id, String taskStatus) throws Exception {
        ProjectTaskEntity taskEntity = new ProjectTaskEntity();
        taskEntity.setId(id);
        updateByTaskIdStatus(taskEntity, taskStatus);
        return null;
    }

    /**
     * 方法描述：处理任务完成时间
     * 作者：MaoSF
     * 日期：2017/3/12
     */
    @Override
    public AjaxMessage handleProjectTaskCompleteDate(String projectId, String companyId, String accountId) throws Exception {
        ProjectEntity project = this.projectDao.selectById(projectId);
        if (project == null) {
            return AjaxMessage.error("任务已失效");
        }
        if (project.getCompanyId().equals(companyId)) {
            //推送消息
        } else {
            //查询所有的签发方组织
            Map<String, Object> map = new HashMap<>();
            map.put("projectId", projectId);
            map.put("toCompanyId", companyId);
            List<ProjectTaskRelationEntity> relationList = this.projectTaskRelationDao.getTaskRelationParam(map);
            for (ProjectTaskRelationEntity relation : relationList) {
                List<ProjectMemberEntity> designList = this.projectMemberService.listDesignManagerAndAssist(projectId, relation.getFromCompanyId());
                List<ProjectMemberEntity> designs = getDesignManagerFilterMe(designList, accountId);
                for (ProjectMemberEntity designerManager : designs) {
                    this.sendMessage(projectId, null, designerManager.getCompanyId(), designerManager.getCompanyUserId(), designerManager.getAccountId(), accountId, SystemParameters.MESSAGE_TYPE_409, companyId);
                }
            }
        }
        return AjaxMessage.succeed(null);
    }

    private void sendMeaninglessTask(String projectId, String taskId, String currentCompanyId, String accountId) throws Exception {
        ProjectEntity project = this.projectDao.selectById(projectId);
        if (project == null) {
            return;
        }
        boolean isCompleteAll = false;
        Map<String, Object> map = Maps.newHashMap();
        if (currentCompanyId.equals(project.getCompanyId())) {//如果是立项方
            //判断所有的生产任务是否完成
            map.clear();
            map.put("projectId", projectId);
            map.put("notComplete", "1");//查询未完成的
            map.put("companyId", currentCompanyId);
            if (CollectionUtils.isEmpty(this.projectTaskDao.selectByParam(map))) {//如果不存在未完成的，则全部完成，给设计负责人推送消息
                isCompleteAll = true;
            }
        } else {
            //查询是否全部完成所有的生产，包含了自己的和签发出去的任务
            if (CollectionUtils.isEmpty(this.projectTaskDao.listUnCompletedTaskByCompany(projectId, null, currentCompanyId))) {
                isCompleteAll = true;
            }
        }
        if (isCompleteAll) {//如果全部完成，则推送消息，推送任务，任务类型为SystemParameters.TASK_COMPLETE
            List<ProjectMemberEntity> designList = this.projectMemberService.listDesignManagerAndAssist(projectId, currentCompanyId);
            List<ProjectMemberEntity> designs = getDesignManagerFilterDuplicated(designList);
            for (ProjectMemberEntity designerManager : designs) {
                myTaskService.saveMyTask(taskId, SystemParameters.TASK_COMPLETE, currentCompanyId, designerManager.getCompanyUserId(), false, accountId, currentCompanyId);
            }
            designs = getDesignManagerFilterMe(designList, accountId);
            for (ProjectMemberEntity designerManager : designs) {
                this.sendMessage(projectId, taskId, currentCompanyId, designerManager.getCompanyUserId(), designerManager.getAccountId(), accountId, SystemParameters.MESSAGE_TYPE_408, currentCompanyId);
            }

        }
    }

    private void handleIsMyTask(ProjectDesignTaskShow dto, String companyId, String companyUserId) throws Exception {
        if ( dto.getDesignOrgId().equals(companyId)) {
            if (dto.getDesigner() != null ) {//如果是任务负责人
                if( dto.getDesigner().getCompanyUserId().equals(companyUserId)){
                    dto.setIsMyTask(1);
                }
            } else  {//如果是任务负责人)
                ProjectMemberEntity responsiblerMap = this.projectMemberService.getTaskDesigner(dto.getId());
                if (responsiblerMap != null && responsiblerMap.getCompanyUserId().equals(companyUserId)) {//如果是任务负责人
                    dto.setIsMyTask(1);
                    return;
                }
            }
        }else {
                List<ProjectTaskProcessNodeDTO> designList = dto.getDesignersList();
                for (ProjectTaskProcessNodeDTO dto1 : designList) {
                    for (ProjectDesignUser user : dto1.getUserList()) {
                        if (companyUserId.equals(user.getCompanyUserId())) {
                            dto.setIsMyTask(1);
                            return;
                        }
                    }
                }
            }
    }


    /**
     * 方法描述：处理当前任务权限标示（roleFlag）
     * 作者：MaoSF
     * 日期：2017/1/18
     */
    private void handleTaskDetailRoleFlag(ProjectDesignTaskShow dto, String companyId, String companyUserId, boolean isEmployeesOfCompany, boolean isDesignManager, boolean isPartBDesigner) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("publishFlag", "0");//默认不需要发布
        if (dto.getIsOperaterTask() == SystemParameters.TASK_OPERATOR) {//如果是经营任务，则无权限
            return;
        }

        if (StringUtil.isNullOrEmpty(dto.getDesignOrgId()) || !dto.getDesignOrgId().equals(companyId)) {//如果不是本团队的任务，则无权限
            return;
        }
        ProjectDesignUser designUser = dto.getProjectDesignUser();//设计人员
        if (dto.getTaskType() == 5 && !StringUtil.isNullOrEmpty(companyUserId)) {//companyUserId可能传递为null,则不做任何处理){//如果是设计人员的任务
            if (designUser != null && companyUserId.equals(designUser.getCompanyUserId())) {
                map.put("flag4", "1");
                map.put("flag5", "1");
                map.put("flag6", "1");
            }
        } else {
            //判断生产任务
            if (!StringUtil.isNullOrEmpty(companyUserId)) {//companyUserId可能传递为null,则不做任何处理
                ProjectMemberDTO memberEntity = dto.getDesigner();
                if (isEmployeesOfCompany) {
                    map.put("isEmployeesOfCompany", "1");
                }
//                if (designUser != null && companyUserId.equals(designUser.getCompanyUserId())) {
//                    map.put("flag7", "1");//设计人员分解任务
//                }
                //如果是本团队的任务,且当前人是本团队在职的员工
                if (dto.getDesignOrgId().equals(companyId) && isEmployeesOfCompany) {
                    if (dto.getTaskType() == SystemParameters.TASK_TYPE_ISSUE || dto.getTaskType() == SystemParameters.TASK_TYPE_PHASE) {//如果是经营过来的任务
                        if (isDesignManager) {
                            if (StringUtil.isNullOrEmpty(dto.getCompleteDate())) {
                                map.put("flag1", "1");
                                map.put("flag3", "1");
                                map.put("flag2", "1");
                            }
                        }
                    } else {
                        //如果是设计负责人
                        if (isDesignManager) {
                            if (StringUtil.isNullOrEmpty(dto.getCompleteDate())) {
                                map.put("flag1", "1");
                                map.put("flag3", "1");
                                map.put("flag2", "1");
                                if (dto.getTaskType() == 0) {
                                    map.put("flag4", "1");
                                    map.put("flag5", "1");
                                  //  List<ProjectTaskEntity> projectTaskEntityList = this.projectTaskDao.getProjectTaskByPid(dto.getId());
                                    if (dto.getIsHasChild()==0) {
                                        map.put("flag6", "1");
                                    }
                                }
                            }
                        }else {
                            boolean isParentDesigner = false;
                            if (!StringUtil.isNullOrEmpty(dto.getTaskPid())) {
                                if( dto.getTaskPath().contains("-")){
                                    String parentPath = dto.getTaskPath().substring(0,dto.getTaskPath().lastIndexOf("-"));
                                    isParentDesigner = this.projectMemberService.isParentDesigner(dto.getProjectId(),parentPath, dto.getDesignOrgId(), companyUserId);
                                }
                            }
                            //如果是父级任务负责人
                            if (isParentDesigner) {
                                //如果不是设计负责人，才走下面，因为如果是设计负责人，则已经具有所有的权限了
                                map.put("flag2", "1");
                                if (dto.getTaskType() == 0) {
                                    map.put("flag5", "1");
                                 //   List<ProjectTaskEntity> projectTaskEntityList = this.projectTaskDao.getProjectTaskByPid(dto.getId());
                                    if (dto.getIsHasChild()==0) {
                                        map.put("flag6", "1");
                                    }
                                }
                            }
                        }

                        //如果是任务负责人
                        if (memberEntity != null && memberEntity.getCompanyUserId().equals(companyUserId) && StringUtil.isNullOrEmpty(dto.getCompleteDate())) {//如果是任务负责人
                            map.put("flag1", "1");
                            map.put("flag3", "1");
                            if (dto.getTaskType() == 0) {
                                map.put("flag4", "1");
                            }
                        }
                    }
                }
                //如果是乙方经营负责人且存在任务负责人，则可以操作
                if ((isPartBDesigner && memberEntity != null && StringUtil.isNullOrEmpty(dto.getCompleteDate())
                        && (dto.getTaskType() == 0)) || companyUserId.equals(dto.getPersonInChargeId())) {
                    map.put("flag1", "1");
                    map.put("flag3", "1");
                }
            }
        }
        dto.setRoleFlag(map);
    }

    private boolean isDesignManager(String projectId, String companyId, String companyUserId) throws Exception {
        ProjectMemberEntity member = this.projectMemberService.getDesignManager(projectId, companyId);
        if (member != null && member.getCompanyUserId().equals(companyUserId)) {
            return true;
        }
        member = this.projectMemberService.getDesignManagerAssistant(projectId, companyId);//设计助理
        if (member != null && member.getCompanyUserId().equals(companyUserId)) {
            return true;
        }
        return false;
    }

    private List<ProjectMemberEntity> getDesignManagerFilterMe(List<ProjectMemberEntity> memberList, String account) {
        List<ProjectMemberEntity> list = new ArrayList<>();
        String accountIds = "";
        for (ProjectMemberEntity member : memberList) {
            if (!member.getAccountId().equals(account) && !accountIds.contains(member.getAccountId())) {
                list.add(member);
                accountIds += member.getAccountId();
            }
        }
        return list;
    }

    private List<ProjectMemberEntity> getDesignManagerFilterDuplicated(List<ProjectMemberEntity> memberList) {
        List<ProjectMemberEntity> list = new ArrayList<>();
        String accountIds = "";
        for (ProjectMemberEntity member : memberList) {
            if (!accountIds.contains(member.getAccountId())) {
                list.add(member);
                accountIds += member.getAccountId();
            }
        }
        return list;
    }

    /**
     * 方法描述：处理当前任务权限标示（roleFlag）
     * 作者：MaoSF
     * 日期：2017/1/18
     *
     * @param:
     * @return:
     */
    public void handleTaskDetailRoleFlag_publish(ProjectDesignTaskShow dto, String companyId, String companyUserId, String projectId, String taskPath, boolean isPartBDesigner) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("publishFlag", "0");//默认不需要发布
        if (!StringUtil.isNullOrEmpty(companyUserId)) {//accountId可能传递为null,则不做任何处理

            ProjectMemberEntity memberEntity = this.projectMemberService.getTaskDesigner(dto.getId());
            CompanyUserTableDTO companyUserTableDTO = companyUserDao.getCompanyUserById(companyUserId);

            if (null != companyUserTableDTO && "1".equals(companyUserTableDTO.getAuditStatus())) {//返回是本员工的标识
                map.put("isEmployeesOfCompany", "1");
            }
            //如果是本团队的任务,且当前人是本团队在职的员工
            if (dto.getDesignOrgId().equals(companyId) && null != companyUserTableDTO && "1".equals(companyUserTableDTO.getAuditStatus())) {
                //设计负责人
                ProjectMemberEntity member = this.projectMemberService.getDesignManager(projectId, companyId);
                ProjectMemberEntity responsiblerParentTask = null;
                if (!StringUtil.isNullOrEmpty(dto.getTaskPid())) {
                    responsiblerParentTask = this.projectMemberService.getTaskDesigner(dto.getTaskPid());
                }

                if (memberEntity != null) {
                    //如果是设计负责人
                    if ((member != null && member.getCompanyUserId().equals(companyUserId)) && !StringUtil.isNullOrEmpty(dto.getBeModifyId())) {
                        if (StringUtil.isNullOrEmpty(dto.getCompleteDate())) {
                            map.put("flag1", "1");
                            map.put("flag3", "1");
                        }
                        map.put("flag2", "1");
                        if (dto.getBeModifyTaskType() == 0 || dto.getTaskType() == 0) {
                            map.put("flag4", "1");
                            map.put("flag5", "1");
                            List<ProjectTaskEntity> projectTaskEntityList = this.projectTaskDao.getProjectTaskByPid(dto.getId());
                            if (CollectionUtils.isEmpty(projectTaskEntityList)) {
                                map.put("flag6", "1");
                            }
                        }
                    }

                    //如果是父级任务负责人
                    if (responsiblerParentTask != null && companyUserId.equals(responsiblerParentTask.getCompanyUserId())) {
                        //如果不是设计负责人，才走下面，因为如果是设计负责人，则已经具有所有的权限了
                        if (!(member != null && member.getCompanyUserId().equals(companyUserId))) {
                            map.put("flag2", "1");
                            if (dto.getBeModifyTaskType() == 0 || dto.getTaskType() == 0) {
                                map.put("flag5", "1");
                                List<ProjectTaskEntity> projectTaskEntityList = this.projectTaskDao.getProjectTaskByPid(dto.getId());
                                if (CollectionUtils.isEmpty(projectTaskEntityList)) {
                                    map.put("flag6", "1");
                                }
                            }
                        }
                        if (dto.getBeModifyTaskType() == 4) {
                            map.put("flag1", "1");
                            map.put("flag2", "1");
                            map.put("flag3", "1");
                            map.put("flag4", "1");
                            map.put("flag5", "1");
                            map.put("flag6", "1");
                            if ("2".equals(dto.getTaskStatus())) {
                                map.put("publishFlag", "1");
                            }
                        }
                    }

                    //如果是任务负责人
                    if (memberEntity.getCompanyUserId().equals(companyUserId) && StringUtil.isNullOrEmpty(dto.getCompleteDate())) {//如果是任务负责人
                        map.put("flag1", "1");
                        map.put("flag3", "1");
                        if (dto.getBeModifyTaskType() == 0 || dto.getTaskType() == 0) {
                            map.put("flag4", "1");
                        }
                        if ("2".equals(dto.getTaskStatus()) && !StringUtil.isNullOrEmpty(dto.getBeModifyId())) {
                            map.put("publishFlag", "1");
                        }

                    }
                }
            }
            //如果是乙方经营负责人且存在任务负责人，则可以操作
            if (isPartBDesigner && memberEntity != null && StringUtil.isNullOrEmpty(dto.getCompleteDate()) && (dto.getBeModifyTaskType() == 0) || dto.getTaskType() == 0) {
                map.put("flag3", "1");
            }
        }
        dto.setRoleFlag(map);
    }

    /**
     * 方法描述：处理当前任务权限标示（roleFlag）
     * 作者：MaoSF
     * 日期：2017/1/18
     *
     * @param:
     * @return:
     */
    public void handleTaskDetailRoleFlagForOperate(ProjectDesignTaskShow dto, String projectCompanyId, String companyId, String companyUserId, String projectId, String taskPath) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        if (1 == 1) {
            map.put("flag1", "1");
            map.put("flag4", "1");
            map.put("flag5", "1");
            dto.setRoleFlag(map);
            return;
        }

        if (!StringUtil.isNullOrEmpty(companyUserId)) {//accountId可能传递为null,则不做任何处理

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("projectId", projectId);
            param.put("companyId", companyId);
            ProjectMemberEntity member = this.projectMemberService.getOperatorManager(projectId, companyId);

            if (StringUtil.isNullOrEmpty(dto.getTaskPid())) {//如果是根任务
                dto.setIsShowAppointmentTime(1);//展示约定时间
                if (projectCompanyId.equals(companyId)) {
                    if (member != null && member.getCompanyUserId().equals(companyUserId)) {
                        map.put("flag1", "1");
                        map.put("flag4", "1");
                        map.put("flag5", "1");
                        if (dto.getIsHasChild() == 0) {
                            map.put("flag6", "1");
                        }
                    }
                }
            } else {//如果是子任务
                Map<String, Object> param2 = new HashMap<String, Object>();
                param2.put("taskId", dto.getId());
                ProjectTaskRelationEntity relationEntity = this.projectTaskRelationDao.selectTaskRelationByTaskId(param2);

                if (dto.getDesignOrgId().equals(companyId)) {//如果是本团队的任务

                    if (relationEntity != null) {//如果是转发的来的任务（类似根任务）
                        dto.setIsShowAppointmentTime(1);//展示约定时间
                        if (member != null && member.getCompanyUserId().equals(companyUserId)) {
                            map.put("flag1", "1");
                            map.put("flag4", "1");
                        }

                    } else {
                        if (member != null && member.getCompanyUserId().equals(companyUserId)) {
                            //  map.put("flag1","1");
                            map.put("flag4", "1");
                            map.put("flag5", "1");
                            List<ProjectTaskEntity> projectTaskEntityList = this.projectTaskDao.getProjectTaskByPid(dto.getId());
                            if (CollectionUtils.isEmpty(projectTaskEntityList)) {
                                map.put("flag6", "1");
                            }
                        }
                    }

                } else {//-----------------处理完-----------------//
                    //查询是否是当前公司签发出去的
                    if (relationEntity != null && relationEntity.getFromCompanyId().equals(companyId)) {//说明是签发的公司
                        if (member != null && member.getCompanyUserId().equals(companyUserId)) {
                            map.put("flag4", "1");
                            map.put("flag5", "1");
                            List<ProjectTaskEntity> projectTaskEntityList = this.projectTaskDao.getProjectTaskByPid(dto.getId());
                            if (CollectionUtils.isEmpty(projectTaskEntityList)) {
                                map.put("flag6", "1");
                            }
                        }
                    }
                }
            }
        }
        dto.setRoleFlag(map);
    }


    private Boolean getBeDelete(ProjectIssueTaskDTO dto, String companyId, List<ProjectTaskEntity> testList) {
        if (!companyId.equals(dto.getFromCompanyId())) {
            return false;
        }
        //如果是签发给
        if (dto.getTaskState() == 3 || dto.getTaskState() == 4) {
            return false;
        }
        if (!CollectionUtils.isEmpty(testList)) {
            if (!companyId.equals(testList.get(0).getFromCompanyId()) || testList.size() > 2) {
                return false;
            }
            if (testList.size() == 1 && SystemParameters.TASK_TYPE_MODIFY == dto.getTaskType() && SystemParameters.TASK_STATUS_MODIFIED.equals(dto.getTaskStatus())) {
                return true; //此处为true，新建的测试版本
            }
            if (testList.size() > 1 && "0".equals(testList.get(1).getTaskStatus())) {
                return false;
            }
        }
        if (!CollectionUtils.isEmpty(projectTaskDao.listTaskByPid(dto.getId()))) {
            return false;
        }
        if (dto.getIsOperaterTask() == SystemParameters.TASK_PRODUCT) {//如果是生产任务
            //是否有子任务
            //是否有设校审人员
            if (!CollectionUtils.isEmpty(projectMemberService.listDesignMember(dto.getId()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 方法描述:获取任务签发组织
     * 作者：MaoSF
     * 日期：2017/3/17
     *
     * @param id
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage getIssueTaskCompany(String id, String projectId, String companyId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<CompanyDataDTO> allCompanyListData = null;
        if (StringUtil.isNullOrEmpty(id)) {
            allCompanyListData = this.companyService.getCompanyForSelect(companyId, projectId);
        } else {
            ProjectTaskEntity projectTask = this.projectTaskDao.selectById(id);
            if (projectTask.getTaskType() == SystemParameters.TASK_TYPE_MODIFY) {
                if (!StringUtil.isNullOrEmpty(projectTask.getBeModifyId())) {
                    projectTask = this.projectTaskDao.selectById(projectTask.getBeModifyId());
                }
            }


            //判断是否是二次签发
            Map<String, Object> map2 = new HashMap<String, Object>();
            String[] taskIdList = projectTask.getTaskPath().split("-");
            map2.put("taskPath", projectTask.getTaskPath());
            map2.put("taskIdList", taskIdList);
            int issueCount = this.projectTaskRelationDao.getTaskIssueCount(map2);
            if (issueCount == 2) {//已经存在二次转发。
                //  map.put("allCompanyList",getCurrentCompany(companyId));
                allCompanyListData = getCurrentCompany(companyId);
            } else {
                List<CompanyDataDTO> allCompanyList2 = this.companyService.getCompanyForSelect(companyId, projectTask.getProjectId());
                List<CompanyDataDTO> allCompanyList = new ArrayList<>();
                String companyIdList = this.projectTaskDao.getParentTaskCompanyId(map2);
                if (!StringUtil.isNullOrEmpty(companyIdList)) {
                    for (CompanyDataDTO dataDTO : allCompanyList2) {
                        if (companyId.equals(dataDTO.getId()) || companyIdList.indexOf(dataDTO.getId()) < 0) {
                            allCompanyList.add(dataDTO);
                        }
                    }
                    // map.put("allCompanyList",allCompanyList);
                    allCompanyListData = allCompanyList;
                } else {
                    // map.put("allCompanyList",allCompanyList2);
                    allCompanyListData = allCompanyList2;
                }
            }
        }
        map.put("allCompanyList", allCompanyListData);
        return AjaxMessage.succeed(map).setInfo("查询成功");
    }

    private List<CompanyDataDTO> getCurrentCompany(String companyId) throws Exception {
        CompanyEntity companyEntity = companyDao.selectById(companyId);
        CompanyDataDTO companyDataDTO = new CompanyDataDTO();
        BaseDTO.copyFields(companyEntity, companyDataDTO);
        List<CompanyDataDTO> dataDTOS = new ArrayList<CompanyDataDTO>();
        dataDTOS.add(companyDataDTO);
        return dataDTOS;
    }

    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2017/3/17
     *
     * @param map
     * @param:map(projectId,toCompanyId(签发给这个组织的id)
     * @return:
     */
    @Override
    public AjaxMessage validateIssueTaskCompany(Map<String, Object> map) throws Exception {
        String currentCompanyId = (String) map.get("currentCompanyId");
        String toCompanyId = (String) map.get("toCompanyId");
        String projectId = (String) map.get("projectId");

        //如果不是签发给自己，则查询是否已经签过发给toCompanyId

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("projectId", projectId);
        map1.put("companyId", toCompanyId);
        List<ProjectMemberDTO> list = this.projectMemberService.listProjectMemberByParam(projectId, toCompanyId, null, null);
        // List<ProjectManagerDTO> list = this.projectManagerDao.selectPeojectManagerList(map1);
        if (!CollectionUtils.isEmpty(list)) {
            map1.clear();
            for (ProjectMemberDTO dto : list) {
                map1.put("flag", "0");//默认为0
                if (currentCompanyId.equals(toCompanyId)) {
                    map1.put("flag", "1");//代表选择的是自己
                }
                if (dto.getMemberType() == ProjectMemberType.PROJECT_OPERATOR_MANAGER) {
                    map1.put("managerId", dto.getCompanyUserId());
                    map1.put("managerName", dto.getCompanyUserName());
                }
                /*****************************/
                if (dto.getMemberType() == ProjectMemberType.PROJECT_DESIGNER_MANAGER) {//设计负责人
                    map1.put("designerId", dto.getCompanyUserId());
                    map1.put("designerName", dto.getCompanyUserName());
                }
            }
            return AjaxMessage.succeed(map1);
        }
        return AjaxMessage.succeed(null);
    }


    private void handleProjectTaskState(ProjectDesignTaskShow dto) throws Exception {

        if ((dto.getBeModifyId() == null
                && (dto.getTaskType() == SystemParameters.TASK_PRODUCT_TYPE_MODIFY || dto.getTaskType() == SystemParameters.TASK_TYPE_MODIFY))
                || ("2".equals(dto.getTaskStatus())) && dto.getId().equals(dto.getBeModifyId())) {
            dto.setTaskState(7);
            dto.setStateHtml("未发布");
            return;
        }
        //设置状态
        String taskId = StringUtil.isNullOrEmpty(dto.getBeModifyId()) ? dto.getId() : dto.getBeModifyId();
        int taskState = getTaskState(taskId, dto.getPlanEndTime(), dto.getPlanStartTime(), dto.getCompleteDate(), dto.getIsHasChild(), dto.getIsRootTask(), dto.getNotCompleteCount());
        dto.setTaskState(taskState);
        String stateHtml = getStateHtml(dto.getPlanEndTime(), dto.getPlanStartTime(), dto.getCompleteDate(), taskState);
        dto.setStateHtml(stateHtml);
    }

    public int getTaskState(String taskId, Date planEndTime, Date planStartTime, Date completeDate, int isHasChild, int isRootTask, int notCompleteCount) {
        return projectTaskDao.getTaskState(taskId);
    }


    public String getStateHtml(Date planEndTime, Date planStartTime, Date completeDate, int taskState) {
        return projectTaskDao.getStateText(taskState, planStartTime, planEndTime, completeDate);
    }

    /**
     * 方法描述：移交设计负责人请求数据
     * 作者：MaoSF
     * 日期：2017/3/22
     */
    @Override
    public AjaxMessage getProjectTaskForChangeDesigner(Map<String, Object> map) throws Exception {
        map.put("targetId", map.get("companyUserId"));
        List<ProejctTaskForDesignerDTO> list = this.projectTaskDao.getMyProjectTask(map);
        return AjaxMessage.succeed(list);
    }

    /**
     * 获取签发任务列表
     *
     * @param query 查询条件
     */
    @Override
    public List<ProjectIssueTaskDTO> getProjectIssueTaskList(QueryProjectTaskDTO query) throws Exception {
        String currentCompanyId = query.getCurrentCompanyId();
        String projectId = query.getProjectId();
        List<ProjectIssueTaskDTO> list = this.projectTaskDao.getOperatorTaskList(query);
        list = orderIssueTaskList(list, "");
        setFirstOrLast(list);
        //处理是否可以添加子任务
        for (ProjectIssueTaskDTO issueDTO : list) {
            int issueLevel = issueDTO.getIssueLevel() == null ? 0 : issueDTO.getIssueLevel();
            issueDTO.setAddChild(false);
            //增加权限：1：当前组织的任务，2：issueLevel = taskLevel -1 <2,3:当前任务是草稿版本 && (顶级任务，或是经营任务），|| 不是草稿版本 && 是经验任务
            if (issueDTO.getCompanyId().equals(currentCompanyId) && issueLevel < 2
                    && ((issueDTO.getTaskType() == SystemParameters.TASK_TYPE_MODIFY
                    && (StringUtil.isNullOrEmpty(issueDTO.getTaskPid()) || issueDTO.getIsOperaterTask() == SystemParameters.ISSUE_TASK))
                    || (issueDTO.getTaskType() != SystemParameters.TASK_TYPE_MODIFY && issueDTO.getIsOperaterTask() == SystemParameters.ISSUE_TASK))) {
                issueDTO.setAddChild(true);
            }
            //编辑权限
            List<ProjectTaskEntity> testList = projectTaskDao.getTaskByBeModifyId(issueDTO.getBeModifyId(), null);
            if (!CollectionUtils.isEmpty(testList) && currentCompanyId.equals(testList.get(0).getFromCompanyId())
                    || CollectionUtils.isEmpty(testList) && currentCompanyId.equals(issueDTO.getFromCompanyId())) {
                issueDTO.setCanBeEdit(true);
            }
            //处理删除权限
            issueDTO.setCanBeDelete(getBeDelete(issueDTO, currentCompanyId, testList));
            //设置状态文字
            issueDTO.setStatusText(projectTaskDao.getStateText(issueDTO.getTaskState(), issueDTO.getPlanStartTime(), issueDTO.getPlanEndTime(), issueDTO.getCompleteDate()));
        }
        setIsHasChild(list);
        return list;
    }

    private void setIsHasChild(List<ProjectIssueTaskDTO> list) {
        for (ProjectIssueTaskDTO issueTaskDTO : list) {
            for (ProjectIssueTaskDTO dto : list) {
                if (!StringUtil.isNullOrEmpty(dto.getTaskPid())) {
                    if (issueTaskDTO.getId().equals(dto.getTaskPid())) {
                        issueTaskDTO.setIsHasChild(1);
                        break;
                    }
                }
            }
        }
    }

    private List<ProjectIssueTaskDTO> orderIssueTaskList(List<ProjectIssueTaskDTO> list, String id) {
        //排序
        List<ProjectIssueTaskDTO> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ProjectIssueTaskDTO projectTaskDTO = list.get(i);
            if ("".equals(id)) {
                if (StringUtil.isNullOrEmpty(projectTaskDTO.getTaskPid())) {//生产那块根节点在sql查出来的taskPid设为null
                    result.add(projectTaskDTO);
                    result.addAll(orderIssueTaskList(list, projectTaskDTO.getId()));
                }
            }
            if (id.equals(projectTaskDTO.getTaskPid())) {
                result.add(projectTaskDTO);
                result.addAll(orderIssueTaskList(list, projectTaskDTO.getId()));
            }
        }
        return result;
    }

    private void setFirstOrLast(List<ProjectIssueTaskDTO> result) {
        //设置isFirst，isLast值
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> map1 = new HashMap<>();
        for (int i = 0; i < result.size(); i++) {
            ProjectIssueTaskDTO projectTaskDTO = result.get(i);
            String taskPid = StringUtil.isNullOrEmpty(projectTaskDTO.getTaskPid()) ? "" : projectTaskDTO.getTaskPid();
            if (!map.containsKey(taskPid)) {
                projectTaskDTO.setFirst(true);
                map.put(taskPid, 1);
                map1.put(projectTaskDTO.getId(), 1);
            } else {
                int count = map.get(taskPid).intValue() + 1;
                map.put(taskPid, count);
                map1.put(projectTaskDTO.getId(), count);
            }
        }

        for (int i = 0; i < result.size(); i++) {
            ProjectIssueTaskDTO projectTaskDTO = result.get(i);
            String taskPid = StringUtil.isNullOrEmpty(projectTaskDTO.getTaskPid()) ? "" : projectTaskDTO.getTaskPid();
            if (map.get(taskPid).intValue() == map1.get(projectTaskDTO.getId()).intValue()) {
                projectTaskDTO.setLast(true);
            }
        }
    }

    /**
     * 生产安排排序
     */
    private List<ProjectDesignTaskShow> orderDesignTaskList(List<ProjectDesignTaskShow> list, String id) {
        //排序
        List<ProjectDesignTaskShow> result = new ArrayList<>();

        int size = list.size();
        for (int i = 0; i < list.size(); i++) {
            boolean isFirst = false;
            boolean isLast = false;
            ProjectDesignTaskShow projectDesignTaskShow = list.get(i);
            String taskPid1 = StringUtil.isNullOrEmpty(projectDesignTaskShow.getTaskPid()) ? "" : projectDesignTaskShow.getTaskPid();
            String taskPid2 = "";
            String taskPid3 = "";
            if (i < list.size() - 1) {
                taskPid2 = StringUtil.isNullOrEmpty(list.get(i + 1).getTaskPid()) ? "" : list.get(i + 1).getTaskPid();
            }
            if (i > 0) {
                taskPid3 = StringUtil.isNullOrEmpty(list.get(i - 1).getTaskPid()) ? "" : list.get(i - 1).getTaskPid();
            }
            if (i == size - 1 || !taskPid1.equals(taskPid2)) {
                isLast = true;
            }
            if (i == 0 || !taskPid1.equals(taskPid3)) {
                isFirst = true;
            }
            if ("".equals(id)) {
                if (StringUtil.isNullOrEmpty(projectDesignTaskShow.getTaskPid())) {//生产那块根节点在sql查出来的taskPid设为null
                    projectDesignTaskShow.setFirst(isFirst);
                    projectDesignTaskShow.setLast(isLast);
                    result.add(projectDesignTaskShow);
                    result.addAll(orderDesignTaskList(list, projectDesignTaskShow.getId()));
                }
            }
            if (id.equals(projectDesignTaskShow.getTaskPid())) {
                projectDesignTaskShow.setFirst(isFirst);
                projectDesignTaskShow.setLast(isLast);
                result.add(projectDesignTaskShow);
                result.addAll(orderDesignTaskList(list, projectDesignTaskShow.getId()));
            }
        }
        return result;

    }

    /**
     * 获取签发界面数据
     *
     * @param query 查询条件
     */
    @Override
    public IssueTaskInfoDTO getIssueInfo(QueryProjectTaskDTO query) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(query.getProjectId());
        if (projectEntity != null) {
            IssueTaskInfoDTO issueTaskInfo = new IssueTaskInfoDTO();
            //1.获取签发板块的数据
            //如果是立项方，则设置isCreator=1
            if (projectEntity.getCompanyId().equals(query.getCurrentCompanyId())) {
                query.setIsCreator(1);
            }
            //如果当前组织是乙方
            issueTaskInfo.setOrgList(this.getSelectOrg(query,projectEntity));

            //查询签发树数据
            issueTaskInfo.setContentTaskList(this.getProjectIssueTaskList(query));
            //2.获取组织关系及经营负责人任务负责人数据
            Map<String, Object> map = new HashMap<>();
            BeanUtilsEx.copyProperties(query, map);
            map.put("type", "1");
            issueTaskInfo.setProjectManagerList(this.getProjectTaskCoopateCompany(map));

            //查询乙方公司及经营负责人，任务负责人数据
            issueTaskInfo.setPartB(this.getProjectTaskPartBCompany(map));

            //查询立项人
            CompanyUserEntity companyUser = this.companyUserService.getCompanyUserByUserIdAndCompanyId(projectEntity.getCreateBy(), projectEntity.getCompanyId());
            issueTaskInfo.setCreateName(companyUser != null ? companyUser.getUserName() : "");
            issueTaskInfo.setCreateBy(projectEntity.getCreateBy());
            issueTaskInfo.setCompanyId(projectEntity.getCompanyId());

            //经营负责人
            //  ProjectManagerEntity managerEntity = this.projectManagerDao.getProjectOperaterManager(query.getProjectId(),query.getCompanyId());
            ProjectMemberDTO managerEntity = this.projectMemberService.getOperatorManagerDTO(query.getProjectId(), query.getCompanyId());
            if (managerEntity == null) {
                managerEntity = new ProjectMemberDTO();
            }
            issueTaskInfo.setManagerId(managerEntity.getCompanyUserId() != null ? managerEntity.getCompanyUserId() : "");
            issueTaskInfo.setProjectManager(managerEntity);

            //设置是否可以设置经营负责人的权限
            map.clear();
            map.put("permissionId", "51");//经营负责人权限id
            map.put("companyId", query.getCompanyId());
            map.put("userId", query.getAccountId());
            List<CompanyUserTableDTO> companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
            if (!CollectionUtils.isEmpty(companyUserList)) {
                managerEntity.setIsUpdateOperator(1);
            }
            //助理
            ProjectMemberDTO assistant = this.projectMemberService.getProjectMemberByParam(query.getProjectId(), query.getCompanyId(), ProjectMemberType.PROJECT_OPERATOR_MANAGER_ASSISTANT, null);
            issueTaskInfo.setAssistant(assistant);
            issueTaskInfo.setProjectName(projectEntity.getProjectName());
            issueTaskInfo.setDataCompanyId(query.getCompanyId());
            return issueTaskInfo;
        }
        return null;
    }

    private List<CompanyDataDTO> getSelectOrg(QueryProjectTaskDTO query,ProjectEntity projectEntity) throws Exception{
        List<CompanyDataDTO> orgList = new ArrayList<>();
        //如果当前组织是乙方
        if(!StringUtil.isNullOrEmpty(projectEntity.getCompanyBid()) && query.getCurrentCompanyId().equals(projectEntity.getCompanyBid())){
            orgList = this.projectTaskRelationDao.getTakePartCompany(projectEntity.getId());
            if(CollectionUtils.isEmpty(projectTaskRelationDao.getProjectTaskRelationByCompanyId(projectEntity.getId(),query.getCompanyId()))){
                query.setCompanyId(projectEntity.getCompanyId());//如果乙方没有参与该项目。这查询立项方的数据
            }
        }
        return orgList;
    }

    /**
     * 进行签发
     *
     * @param idList        要发布的签发任务的ID列表
     * @param currentUserId 操作者标识
     */
    @Override
    public AjaxMessage publishIssueTask(List<String> idList, String currentUserId, String currentCompanyId) throws Exception {
        String message = "";
        boolean isSuccess = false;
        List<ProjectTaskEntity> list = projectTaskDao.getTaskByIdList(idList);
        if (CollectionUtils.isEmpty(list)) {
            return AjaxMessage.error("操作失败");
        }
        for (ProjectTaskEntity e : list) {
            if (SystemParameters.TASK_STATUS_INVALID.equals(e.getTaskStatus())) {
                message += e.getTaskName() + "已失效，发布失败；";
                continue;
            }
            isSuccess = true;
            e.setUpdateBy(currentUserId);
            e.setUpdateDate(new Date());
            //TaskWithFullNameDTO origin = zInfoDAO.getTaskByTaskId(e.getBeModifyId());
            ProjectTaskEntity entity = null; //正式记录对象
            if (e.getTaskType() == SystemParameters.TASK_TYPE_MODIFY) {
                //taskPath不能直接从未发布的数据中直接copy过来，因为id不一样。taskPath是由id-id连接起来的
                //查询父任务,设置taskPath
                ProjectTaskEntity parentTask = this.projectTaskDao.selectById(e.getTaskPid());
                String path = parentTask == null ? null : parentTask.getTaskPath();
                if (e.getBeModifyId() == null) {
                    //产生一条正式记录
                    entity = new ProjectTaskEntity();
                    BeanUtilsEx.copyProperties(e, entity);
                    entity.setId(StringUtil.buildUUID());
                    entity.setTaskType(SystemParameters.TASK_TYPE_ISSUE);
                    if (parentTask != null) {
                        entity.setTaskLevel(parentTask.getTaskLevel() + 1);
                        entity.setTaskPath(parentTask.getTaskPath() + "-" + entity.getId());
                    } else {
                        entity.setTaskLevel(1);
                        entity.setTaskPath(entity.getId());
                        entity.setTaskType(SystemParameters.TASK_TYPE_PHASE);
                    }
                    entity.setBeModifyId(null);
                    entity.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
                    if (currentCompanyId.equals(entity.getCompanyId())) {//如果签发给自己
                        entity.setIsOperaterTask(SystemParameters.TASK_PRODUCT);
                        entity.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
                    } else {
                        entity.setIsOperaterTask(SystemParameters.TASK_OPERATOR);
                        entity.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);
                    }
                    //如果签发给别的组织，创建归档文件夹和项目成果文件夹和本组织的存档文件夹
                    if (!currentCompanyId.equals(entity.getCompanyId())) {
                        ProjectEntity projectEntity = new ProjectEntity();
                        projectEntity.setCompanyBid(entity.getCompanyId());
                        projectEntity.setId(entity.getProjectId());
                        projectSkyDriverService.createProjectFile(projectEntity);
                        Map<String, Object> param = new HashMap<>();
                        param.put("fileName", entity.getTaskName());
                        param.put("projectId", entity.getProjectId());
                        param.put("companyId", entity.getFromCompanyId());
                        List<ProjectSkyDriveEntity> parent = projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndCompanyId(param);
                        if (0 >= parent.size()) {
                            //新增父类文件夹
                            ProjectTaskEntity taskEntity = new ProjectTaskEntity();
                            taskEntity.setTaskType(SystemParameters.TASK_TYPE_ISSUE);
                            taskEntity.setProjectId(entity.getProjectId());
                            taskEntity.setCompanyId(entity.getFromCompanyId());
                            taskEntity.setTaskName(entity.getTaskName());
                            taskEntity.setId(entity.getId());
                            taskEntity.setSeq(entity.getSeq());
                            taskEntity.setIsOperaterTask(0);
                            taskEntity.setTaskPid(null != parentTask ? parentTask.getId() : "");
                            taskEntity.setType(NetFileType.DIRECTORY_ACHIEVEMENT);//设计成果
                            projectSkyDriverService.createFileMasterForTask(taskEntity);
                            taskEntity.setType(NetFileType.DIRECTORY_ARCHIVE_NOTICE_PARTYB);//归档通知
                            projectSkyDriverService.createFileMasterForArchivedFile(taskEntity);
                        }
                    }
                    //todo 判断父节点的companyId和子节点的companyId是否相同，在文件表中查询，是否存在。不存在则新增
                    if (null != parentTask) {
                        Map<String, Object> param = new HashMap<>();
                        param.put("fileName", parentTask.getTaskName());
                        param.put("projectId", parentTask.getProjectId());
                        param.put("companyId", entity.getCompanyId());
                        List<ProjectSkyDriveEntity> parent = projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndCompanyId(param);
                        if (0 >= parent.size()) {
                            //新增父类文件夹
                            ProjectTaskEntity taskEntity = new ProjectTaskEntity();
                            taskEntity.setTaskType(SystemParameters.TASK_TYPE_ISSUE);
                            taskEntity.setProjectId(entity.getProjectId());
                            taskEntity.setCompanyId(entity.getCompanyId());
                            taskEntity.setTaskName(parentTask.getTaskName());
                            taskEntity.setId(parentTask.getId());
                            taskEntity.setSeq(parentTask.getSeq());
                            taskEntity.setIsOperaterTask(3);
                            projectSkyDriverService.createFileMasterForTask(taskEntity);
                            projectSkyDriverService.createFileMasterForArchivedFile(taskEntity);
                        }
                    }
                    this.insert(entity);

                } else {
                    entity = projectTaskDao.selectById(e.getBeModifyId());
                    if (currentCompanyId.equals(e.getCompanyId())) {//如果签发给自己
                        //如果存在子任务，则为经营任务
                        if(CollectionUtils.isEmpty(projectTaskDao.listTaskByPid(entity.getId()))){
                            entity.setIsOperaterTask(SystemParameters.TASK_PRODUCT);
                        }else {
                            entity.setIsOperaterTask(SystemParameters.TASK_OPERATOR);
                        }
                        entity.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
                    } else {
                        entity.setIsOperaterTask(SystemParameters.TASK_OPERATOR);
                        entity.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);
                    }
                    entity.setFromCompanyId(e.getFromCompanyId());
                    entity.setCompanyId(e.getCompanyId());
                    this.updateById(entity);
                }
                e.setBeModifyId(entity.getId());
                //父任务未发布
                if(parentTask!=null && SystemParameters.TASK_STATUS_MODIFIED.equals(parentTask.getTaskStatus())){
                    parentTask.setIsOperaterTask(SystemParameters.TASK_OPERATOR);
                    parentTask.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
                    projectTaskDao.updateById(parentTask);
                    //修改发布版本的状态
                    List<ProjectTaskEntity> modifyTaskList = projectTaskDao.getTaskByBeModifyId(parentTask.getId(),currentCompanyId);
                    if(!CollectionUtils.isEmpty(modifyTaskList)){
                        modifyTaskList.get(0).setTaskStatus(SystemParameters.TASK_STATUS_VALID);
                        projectTaskDao.updateById(modifyTaskList.get(0));
                    }
                }

                //处理签发关系
                this.handleProjectTaskRelation(entity, currentCompanyId, currentUserId);
                //复制时间
                handleProcessTime(entity, e, false);
                //保存项目动态（签发任务）
                dynamicService.addDynamic(null, entity, currentCompanyId, currentUserId);


                if (!currentCompanyId.equals(entity.getCompanyId())) {//如果签发给其他
                    //记录一条对方的草稿数据(签发给其他公司，则需要给对方公司产生一条草稿数据)
                    ProjectTaskEntity newEntity = new ProjectTaskEntity();
                    BeanUtilsEx.copyProperties(entity, newEntity);
                    newEntity.setFromCompanyId(entity.getCompanyId());
                    newEntity.setCompanyId(entity.getCompanyId());
                    newEntity.setId(StringUtil.buildUUID());
                    newEntity.setTaskPath(path == null ? newEntity.getId() : path + "-" + newEntity.getId());
                    newEntity.setBeModifyId(entity.getId());
                    newEntity.setTaskType(SystemParameters.TASK_TYPE_MODIFY);
                    newEntity.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);
                    this.insert(newEntity);
                    this.saveTaskTime(newEntity, newEntity.getStartTime(), newEntity.getEndTime(), null, SystemParameters.PROCESS_TYPE_PLAN);
                }
            }else {
                entity = projectTaskDao.selectById(e.getBeModifyId());
                //此处为修改的内容
                if (!StringUtil.isNullOrEmpty(entity.getFromCompanyId()) && !StringUtil.isNullOrEmpty(entity.getFromCompanyId()) && !entity.getFromCompanyId().equals(e.getFromCompanyId())) {//如果发布的记录操作公司与原记录的来源不同，则表示修改了签发组织。
                    //例如：任务A（公司1--》公司2，并且发布，则存一条草稿记录（命名为：草稿1）（fromCompanyId = 公司1，companyId = 公司2，beModifyId = 任务A的id））
                    //发布后，产生正式记录“任务A”（fromCompanyId = 公司1，companyId = 公司2）
                    //此时，如果是公司1进来修改，必然修改的是“草稿1”记录。调整时间，则只修改草稿1记录即可
                    // 公司2对任务A整包转发给公司3.在修改组织的时候，新增一条草稿记录（命名为：草稿2）（fromCompanyId = 公司2，companyId = 公司3 ，beModifyId = 任务A的id）
                    //如果公司2进行发布，则需要新增发布的记录“任务A'”，）（fromCompanyId = 公司2，companyId = 公司3 ）
                    //处理签发关系
                    entity.setCompanyId(e.getCompanyId());
                    entity.setFromCompanyId(e.getFromCompanyId());
                    entity.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
                    this.updateById(entity);
                    this.handleProjectTaskRelation(entity, currentCompanyId, currentUserId);
                } else {
                    updateProjectTaskForPublish(entity, e, currentCompanyId, currentUserId);
                }
            }

            if (currentCompanyId.equals(e.getCompanyId())) {//如果签发给自己
                e.setIsOperaterTask(SystemParameters.TASK_PRODUCT);
            } else {
                e.setIsOperaterTask(SystemParameters.TASK_OPERATOR);
            }
            e.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
            projectTaskDao.updateById(e);

            //通知协同
            this.collaborationService.pushSyncCMD_PT(e.getProjectId(), e.getTaskPath(), SyncCmd.PT1);
        }

        //是否是第一次发布给本团队,如果是，则给企业负责人发送消息
        CompanyUserTableDTO orgManager = this.companyUserService.getOrgManager(currentCompanyId);
        if (orgManager != null) {
            QueryMessageDTO query = new QueryMessageDTO();
            query.setProjectId(list.get(0).getProjectId());
            query.setCompanyId(currentCompanyId);
            query.setMessageType(SystemParameters.MESSAGE_TYPE_PUBLISH_TASK_ORG_MANAGER);
            query.setUserId(orgManager.getUserId());
            if (CollectionUtils.isEmpty(messageService.getMessageByParam(new QueryMessageDTO()))) {
                this.sendMessage(list.get(0).getProjectId(), null, currentCompanyId, orgManager.getId(), orgManager.getUserId(), currentUserId, SystemParameters.MESSAGE_TYPE_PUBLISH_TASK_ORG_MANAGER, currentCompanyId);
            }
        }
        //创建按照目录结构对应的文件夹

        if (StringUtil.isNullOrEmpty(message)) {
            return AjaxMessage.succeed("操作成功");
        } else {
            if (isSuccess) {
                return AjaxMessage.succeed(message);
            }
            return AjaxMessage.error(message);
        }
    }

    /**
     * 进行生产任务发布
     */
    @Override
    public AjaxMessage publishProductTask(List<String> idList, String currentUserId, String currentCompanyId) throws Exception {
        List<ProjectTaskEntity> list = projectTaskDao.getTaskByIdList(idList);
        for (ProjectTaskEntity e : list) {
            e.setUpdateBy(currentUserId);
            e.setUpdateDate(new Date());
            ProjectTaskEntity entity;
            if (e.getBeModifyId() == null) {
                entity = new ProjectTaskEntity();
                BeanUtilsEx.copyProperties(e, entity);
                entity.setId(StringUtil.buildUUID());
                //taskPath不能直接从未发布的数据中直接copy过来，因为id不一样。taskPath是由id-id连接起来的
                //查询父任务,设置taskPath
                ProjectTaskEntity parentTask = this.projectTaskDao.selectById(e.getTaskPid());
                if (parentTask != null && parentTask.getTaskType() == SystemParameters.TASK_PRODUCT_TYPE_MODIFY) {
                    if (StringUtil.isNullOrEmpty(parentTask.getBeModifyId())) {
                        return AjaxMessage.failed("父任务“" + parentTask.getTaskName() + "”未发布，该任务不可发布");
                    }
                    parentTask = this.projectTaskDao.selectById(parentTask.getBeModifyId());
                }
                if (parentTask != null) {
                    entity.setTaskLevel(parentTask.getTaskLevel() + 1);
                    entity.setTaskPath(parentTask.getTaskPath() + "-" + entity.getId());
                    entity.setTaskPid(parentTask.getId());
                } else {
                    entity.setTaskLevel(1);
                    entity.setTaskPath(entity.getId());
                }
                entity.setBeModifyId(null);
                entity.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
                entity.setTaskType(SystemParameters.TASK_TYPE_PRODUCT);
                this.insert(entity);
                //更新子集的父ID（没有发布过记录的父ID，并且记录的父ID的为被修改记得的ID的情况）
                this.updateModifyTaskPid(e.getId(), entity.getId(), entity.getTaskPath());
                //保存项目动态（生产任务）
                dynamicService.addDynamic(null, entity, currentCompanyId, currentUserId);
            } else {
                entity = projectTaskDao.selectById(e.getBeModifyId());
                //保存原有数据
                TaskWithFullNameDTO origin = zInfoDAO.getTaskByTask(entity);

                entity.setTaskName(e.getTaskName());
                entity.setTaskRemark(e.getTaskRemark());
                entity.setOrgId(e.getOrgId());
                entity.setStartTime(e.getStartTime());
                entity.setEndTime(e.getEndTime());
                this.updateById(entity);

                //保存项目动态（生产任务）
                TaskWithFullNameDTO target = zInfoDAO.getTaskByTask(entity);
                dynamicService.addDynamic(origin, target, currentCompanyId, currentUserId);
            }

            //复制时间
            handleProcessTime(entity, e, false);
            //保存项目动态（签发任务）
            dynamicService.addDynamic(null, entity, currentCompanyId, currentUserId);
            //处理人员变动
            this.projectMemberService.publishProjectMember(e.getProjectId(), e.getCompanyId(), entity.getId(), e.getId(), currentUserId);


            //更改状态
            e.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
            e.setBeModifyId(entity.getId());
            projectTaskDao.updateById(e);

            //通知协同
            this.collaborationService.pushSyncCMD_PT(e.getProjectId(), e.getTaskPath(), SyncCmd.PT2);
        }
        return AjaxMessage.succeed("操作成功");
    }

    /**
     * 方法描述：二次转包，发布的时候，自动产生一个生产的根任务
     * 作者：MaoSF
     * 日期：2017/6/2
     *
     * @param:
     * @return:
     */
    private void autoGenerationProductTask(ProjectTaskEntity parentTask) throws Exception {
        ProjectTaskEntity entity = new ProjectTaskEntity();
        BeanUtilsEx.copyProperties(parentTask, entity);
        entity.setId(StringUtil.buildUUID());
        //taskPath不能直接从未发布的数据中直接copy过来，因为id不一样。taskPath是由id-id连接起来的
        //查询父任务,设置taskPath
        entity.setTaskLevel(parentTask.getTaskLevel() + 1);
        entity.setTaskPath(parentTask.getTaskPath() + "-" + entity.getId());
        entity.setTaskPid(parentTask.getId());
        entity.setBeModifyId(null);
        entity.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);
        entity.setTaskType(SystemParameters.TASK_TYPE_MODIFY);
        insert(entity);
        //复制时间
        copyProcessTime(entity, parentTask, false);//此处为false，全部复制，并且为未发布
    }

    /**
     * 方法描述：任务发布--出来任务关系
     * 作者：MaoSF
     * 日期：2017/5/16
     *
     * @param:
     * @return:
     */
    private void handleProjectTaskRelation(ProjectTaskEntity task, String currentCompanyId, String accountId) throws Exception {
        if (currentCompanyId.equals(task.getCompanyId())) {//如果签发的是给自己的团队
            //给设计负责人发送安排任务负责人的任务
            this.sendTaskToDesignerForPublishTask(task.getProjectId(), task.getId(), task.getCompanyId(), currentCompanyId, accountId);
        } else {//如果签发给其他团队
            //查询是否存在已签发给此公司
            //设置并通知对方公司经营负责人
            this.sendTaskToProjectManager(task.getProjectId(), task.getId(), task.getCompanyId(), currentCompanyId, accountId);
            //设置并通知对方公司设计负责人
            this.notifyIssuedDesigner(task.getProjectId(), task.getId(), task.getCompanyId(), currentCompanyId, accountId);
            //保存签发关系
            this.saveProjectTaskRelation2(task, currentCompanyId, accountId);
        }
    }

    /**
     * 方法描述：任务发布后，给外部组织的经营负责人推送任务
     * 作者：MaoSF
     * 日期：2017/5/16
     */
    private void sendTaskToProjectManager(String projectId, String taskId, String companyId, String currentCompanyId, String accountId) throws Exception {
        ProjectMemberEntity memberEntity = this.projectMemberService.getOperatorManager(projectId, companyId);
        boolean isSendTask = false;
        if (memberEntity == null) {
            //签发到的公司中选择具备经营权限的人员中选择第一个填入项目经营负责人位置
            List<CompanyUserTableDTO> companyUserList = this.companyUserService.getOperatorManager(companyId);
            if (companyUserList.size() > 0) {
                CompanyUserTableDTO u = companyUserList.get(0);
                memberEntity = this.projectMemberService.saveProjectMember(projectId, companyId, u.getId(), u.getUserId(), ProjectMemberType.PROJECT_OPERATOR_MANAGER, accountId, false, currentCompanyId);
                isSendTask = true;
            }
        }
        //为乙方服务的
        if (memberEntity != null && !isSendTask) {
            String managerId = memberEntity.getCompanyUserId();
            //查询签发给的公司是否存在签发的记录
            List<ProjectTaskRelationEntity> taskRelationList = this.projectTaskRelationDao.getProjectTaskRelationByCompanyId(projectId, companyId);
            if (CollectionUtils.isEmpty(taskRelationList)) {//若果不存在签发的记录，则推送签发的任务
                // 发送任务 boolean isSendMessage,String createBy,String currentCompanyId
                this.myTaskService.saveMyTask(taskId, SystemParameters.ISSUE_TASK, companyId, managerId, false, accountId, currentCompanyId);
            }
        }
        //给经营负责人推送消息，如果是第一次，则推送MESSAGE_TYPE_303，否则推送MESSAGE_TYPE_306
        QueryMessageDTO query = new QueryMessageDTO();
        query.setProjectId(projectId);
        query.setCompanyId(companyId);
        query.setMessageType(SystemParameters.MESSAGE_TYPE_303);
        query.setUserId(memberEntity.getAccountId());
        if (CollectionUtils.isEmpty(messageService.getMessageByParam(query))) {
            this.sendMessage(projectId, null, companyId, memberEntity.getId(), memberEntity.getAccountId(), accountId, SystemParameters.MESSAGE_TYPE_303, currentCompanyId);
        } else {
            //此处经营负责人+经营助理
            this.messageService.sendMessageForProjectManager(new SendMessageDTO(projectId, companyId, accountId, currentCompanyId, SystemParameters.MESSAGE_TYPE_306));
            //  this.sendMessage(projectId, null, companyId, memberEntity.getId(), memberEntity.getAccountId(), accountId, SystemParameters.MESSAGE_TYPE_306, currentCompanyId);
        }
    }


    /**
     * 发布（更新后的）-- 任务被发布过的，后来修改，需要再次发布
     */
    private void updateProjectTaskForPublish(ProjectTaskEntity taskEntity, ProjectTaskEntity publishTask, String currentCompanyId, String accountId) throws Exception {
        //保存原有数据
        TaskWithFullNameDTO origin = zInfoDAO.getTaskByTask(taskEntity);
        String companyId = taskEntity.getCompanyId();
        if (!companyId.equals(publishTask.getCompanyId())) {//如果更改了组织
            //删除任务（包含子任务）
            this.deleteProjectTask(taskEntity, accountId);
            //把部门设置为null
            this.projectTaskDao.updateTaskOrgId(taskEntity.getId());
            taskEntity.setCompanyId(publishTask.getCompanyId());//此处需要设置为发布后的组织id
            if (!publishTask.getCompanyId().equals(currentCompanyId)) {
                this.handleProjectTaskRelation(taskEntity, currentCompanyId, accountId);
            } else {
                this.sendTaskToDesignerForPublishTask(taskEntity.getProjectId(), taskEntity.getId(), taskEntity.getCompanyId(), currentCompanyId, accountId);
            }
        }
        //处理计划进度时间
        Boolean isChanged = this.handleProcessTime(taskEntity, publishTask, !companyId.equals(publishTask.getCompanyId()));
        //更新
        String id = taskEntity.getId();
        String taskPath = taskEntity.getTaskPath();
        BaseDTO.copyFields(publishTask, taskEntity);
        taskEntity.setId(id);
        taskEntity.setTaskPath(taskPath);
        taskEntity.setTaskStatus(SystemParameters.TASK_STATUS_VALID); //再更新（由于上面taskStatus设置为1了，所以，此处设置为0.更新为有效）
        taskEntity.setTaskType(taskEntity.getTaskType() == SystemParameters.TASK_TYPE_PHASE ? SystemParameters.TASK_TYPE_PHASE : SystemParameters.TASK_TYPE_ISSUE);
        taskEntity.setCompleteDate(null);
        taskEntity.setBeModifyId(null);//此语句必须要设置为null,因为前面copy把beModifyId复制到taskEntity中了
        this.updateById(taskEntity);
        //获取修改后的数据
        TaskWithFullNameDTO target = zInfoDAO.getTaskByTask(taskEntity);
        //保存项目动态
        dynamicService.addDynamic(origin, target, currentCompanyId, accountId);
        //把状态更改为发布的状态
        publishTask.setTaskStatus(SystemParameters.TASK_STATUS_VALID);
        projectTaskDao.updateById(publishTask);
    }


    private Boolean handleProcessTime(ProjectTaskEntity taskEntity, ProjectTaskEntity publishTask, boolean isChangeOrg) throws Exception {
        Boolean isChanged = false; //任务时间是否有变动
        if (isChangeOrg) {
            this.copyProcessTime(taskEntity, publishTask, true);
            return isChanged;
        }
        //把计划进度时间全部复制到新的
        Map<String, Object> map = new HashMap<>();
        map.put("targetId", publishTask.getId());
        map.put("type", SystemParameters.PROCESS_TYPE_NOT_PUBLISH);
        List<ProjectProcessTimeEntity> processList = this.projectProcessTimeDao.getProjectProcessTime(map);
        if (!CollectionUtils.isEmpty(processList)) {
            isChanged = true;
            for (ProjectProcessTimeEntity processTimeEntity : processList) {
                //1.把类型改为计划时间（未发布--计划)
                processTimeEntity.setType(SystemParameters.PROCESS_TYPE_PLAN);
                this.projectProcessTimeDao.updateById(processTimeEntity);
                //2.复制到
                processTimeEntity.setId(StringUtil.buildUUID());
                processTimeEntity.setTargetId(taskEntity.getId());
                this.projectProcessTimeDao.insert(processTimeEntity);
            }

            ProjectProcessTimeEntity processTime = processList.get(0);//只取第1条数据，因为是倒序排列，第一条数据是最新记录
            //查询签发的子任务，由于存在时间变更，需要通知子任务
            List<ProjectIssueTaskDTO> taskList = this.projectTaskDao.getTaskByTaskPidForChangeProcessTime(taskEntity.getId());
            for (ProjectIssueTaskDTO child : taskList) {
                if (StringUtil.isNullOrEmpty(child.getPlanStartTime()) || !(DateUtils.datecompareDate(processTime.getStartTime(), child.getPlanStartTime()) <= 0
                        && DateUtils.datecompareDate(processTime.getEndTime(), child.getPlanEndTime()) >= 0)) {
                    //启动未发布状态
                    ProjectTaskEntity notPublishTask = new ProjectTaskEntity();
                    if (StringUtil.isNullOrEmpty(child.getId())) {//如果没有未发布数据
                        BaseDTO.copyFields(child, notPublishTask);//child中的数据是从发布版中获取的。
                        //则生成一条记录
                        notPublishTask.setId(StringUtil.buildUUID());
                        notPublishTask.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);//未发布状态
                        notPublishTask.setTaskLevel(taskEntity.getTaskLevel() + 1);
                        notPublishTask.setTaskPath(taskEntity.getTaskPath() + "-" + notPublishTask.getId());
                        notPublishTask.setOrgId(child.getDepartId());
                        notPublishTask.setTaskType(SystemParameters.TASK_TYPE_MODIFY);
                        this.insert(notPublishTask);
                    } else {
                        notPublishTask.setId(child.getId());
                        notPublishTask.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);//未发布状态
                        this.projectTaskDao.updateByTaskId(notPublishTask);
                    }
                    //2.复制到修改任务中，并且设置未未发布的版本
                    processTime.setId(StringUtil.buildUUID());
                    processTime.setTargetId(notPublishTask.getId());
                    processTime.setType(SystemParameters.PROCESS_TYPE_NOT_PUBLISH);//设置为未发布版本
                    this.projectProcessTimeDao.insert(processTime);
                }
            }

            //处理当前节点下生产任务的时间
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("taskPath", taskEntity.getId() + "-");
            objectMap.put("taskType", 0);
            List<ProjectTaskEntity> childTaskList = this.projectTaskDao.selectByParam(objectMap);
            for (ProjectTaskEntity child : childTaskList) {
                //2.复制到修改任务中，并且设置未未发布的版本
                processTime.setId(StringUtil.buildUUID());
                processTime.setTargetId(child.getId());
                processTime.setType(SystemParameters.PROCESS_TYPE_PLAN);//设置为计划进度
                this.projectProcessTimeDao.insert(processTime);
            }
        }
        return isChanged;
    }

    /**
     * 方法描述：时间全部复制
     * 作者：MaoSF
     * 日期：2017/6/21
     *
     * @param:isPublish(true:发布，false:未发布）
     */
    private void copyProcessTime(ProjectTaskEntity taskEntity, ProjectTaskEntity publishTask, boolean isPublish) throws Exception {
        int timeType = isPublish ? SystemParameters.PROCESS_TYPE_PLAN : SystemParameters.PROCESS_TYPE_NOT_PUBLISH;
        //复制及添加时间
        Map<String, Object> query = new HashMap<>();
        query.put("targetId", publishTask.getId());
        List<ProjectProcessTimeEntity> tps = projectProcessTimeDao.getProjectProcessTime(query);
        for (ProjectProcessTimeEntity t : tps) {
            saveTaskTime(taskEntity, t.getStartTime(), t.getEndTime(), t.getMemo(), timeType);
        }
    }

    @Override
    public List<ProjectProductTaskDTO> getProductTaskOverview(QueryProjectTaskDTO query) throws Exception {
        ProjectEntity projectEntity = projectService.selectById(query.getProjectId());
        if (null != projectEntity) {
            query.setCompanyId(projectEntity.getCompanyId());
        }
        List<ProjectProductTaskDTO> list = projectTaskDao.getProductTaskOverview(query);
        for (ProjectProductTaskDTO taskDTO : list) {
            for (ProjectProductTaskDTO dto : taskDTO.getChildList()) {
                dto.setTaskState(this.getTaskState(dto.getId(), dto.getPlanEndTime(), dto.getPlanStartTime(), dto.getCompleteDate(), 0, 0, dto.getNotCompleteCount()));
                dto.setStateHtml(this.getStateHtml(dto.getPlanEndTime(), dto.getPlanStartTime(), dto.getCompleteDate(), dto.getTaskState()));
            }
            List<ProjectProductTaskDTO> childList = taskDTO.getChildList();
            taskDTO.setChildList(this.doSortingProductTask(childList, "", taskDTO.getId()));
        }
        return list;
    }

    /**
     * 新增获取成产安排列表
     */
    @Override
    public List<ProjectProducttaskViewDTO> getProductTaskOverviewNew(QueryProjectTaskDTO query) throws Exception {

        List<ProjectProducttaskViewDTO> viewDTOS = projectTaskDao.getProductTaskOverviewNew(query);
        viewDTOS = orderDesignTaskViewList(viewDTOS, "");
        for (ProjectProducttaskViewDTO dto : viewDTOS) {
            //把设校对审放入外层，以便前端取数据
            for (ProjectDesignUser design : dto.getDesignersList()) {
                if (ProjectMemberType.PROJECT_DESIGNER.equals(design.getMemberType())) {
                    dto.getDesignUser().getUserList().add(design);
                }
                if (ProjectMemberType.PROJECT_PROOFREADER.equals(design.getMemberType())) {
                    dto.getCheckUser().getUserList().add(design);
                }
                if (ProjectMemberType.PROJECT_AUDITOR.equals(design.getMemberType())) {
                    dto.getExamineUser().getUserList().add(design);
                }
            }
            setShowDesignUserName(dto, query.getCurrentCompanyUserId());

        }
        return viewDTOS;
    }

    /**
     * 生产安排总览排序
     */
    private List<ProjectProducttaskViewDTO> orderDesignTaskViewList(List<ProjectProducttaskViewDTO> list, String id) {
        //排序
        List<ProjectProducttaskViewDTO> result = new ArrayList<>();

        int size = list.size();
        for (int i = 0; i < list.size(); i++) {
            ProjectProducttaskViewDTO projectDesignTaskShow = list.get(i);

            if ("".equals(id)) {
                if (StringUtil.isNullOrEmpty(projectDesignTaskShow.getTaskPid())) {//生产那块根节点在sql查出来的taskPid设为null
                    result.add(projectDesignTaskShow);
                    result.addAll(orderDesignTaskViewList(list, projectDesignTaskShow.getId()));
                }
            }
            if (id.equals(projectDesignTaskShow.getTaskPid())) {
                result.add(projectDesignTaskShow);
                result.addAll(orderDesignTaskViewList(list, projectDesignTaskShow.getId()));
            }
        }
        return result;
    }


    /**
     * 生产总览排序
     *
     * @param projectTaskDTOList
     * @param id                 当前任务ID
     * @param taskPid            当前任务taskPid
     * @return
     */
    public List<ProjectProductTaskDTO> doSortingProductTask(List<ProjectProductTaskDTO> projectTaskDTOList, String id, String taskPid) {
        List<ProjectProductTaskDTO> list = new ArrayList<ProjectProductTaskDTO>();
        for (ProjectProductTaskDTO projectTaskDTO : projectTaskDTOList) {
            if ("".equals(id)) {
                if (projectTaskDTO.getTaskType() == 2) {//类型为2表示从签发过来的任务，即是生产根节点，添加到集合
                    list.add(projectTaskDTO);
                    list.addAll(doSortingProductTask(projectTaskDTOList, projectTaskDTO.getId(), taskPid));
                }
            }
            if (id.equals(projectTaskDTO.getTaskPid())) {//确定是子节点，添加到集合
                list.add(projectTaskDTO);
                list.addAll(doSortingProductTask(projectTaskDTOList, projectTaskDTO.getId(), taskPid));
            }

        }
        return list;
    }

    @Override
    public Map<String, Object> getProjectInfoForTask(String projectId) throws Exception {

        Map<String, Object> resMap = new HashedMap();
        Map<String, Object> paramMap = new HashedMap();
        ProjectEntity projectEntity = projectDao.selectById(projectId);

        paramMap.put("companyId", projectEntity.getCompanyId());
        paramMap.put("projectId", projectId);
        List<ProjectMemberDTO> memberList = this.projectMemberService.listProjectMemberByParam(projectId, projectEntity.getCompanyId(), null, null);
        if (null != memberList && memberList.size() > 0) {
            for (ProjectMemberDTO dto : memberList) {
                if (dto.getMemberType() == ProjectMemberType.PROJECT_OPERATOR_MANAGER) {
                    resMap.put("operatorPersonName", dto.getCompanyUserName());//经营负责人
                }
                if (dto.getMemberType() == ProjectMemberType.PROJECT_DESIGNER_MANAGER) {
                    resMap.put("designPersonName", dto.getCompanyUserName());//设计负责人
                }
            }
        }
        CompanyUserEntity companyUserEntity = companyUserDao.getCompanyUserByUserIdAndCompanyId(projectEntity.getCreateBy(), projectEntity.getCompanyId());
        resMap.put("projectName", projectEntity.getProjectName());//项目名称
        resMap.put("projectCreateByName", companyUserEntity.getUserName());//立项人姓名

        return resMap;
    }


    /**
     * 获取签发概览数据（版本2.0）
     *
     * @param projectId
     */
    @Override
    public List<ProjectIssueTaskOverviewDTO> listProjectTaskIssueOverview(String projectId, String companyId) throws Exception {
        List<ProjectIssueTaskOverviewDTO> list = projectTaskDao.listProjectTaskIssueOverview(projectId, companyId);
        //排序
        return sortTaskIssue(list, "");
    }


    /**
     * 签发排序
     */
    public List<ProjectIssueTaskOverviewDTO> sortTaskIssue(List<ProjectIssueTaskOverviewDTO> projectTaskDTOList, String id) {
        List<ProjectIssueTaskOverviewDTO> list = new ArrayList<>();
        for (ProjectIssueTaskOverviewDTO projectTaskDTO : projectTaskDTOList) {
            if ("".equals(id)) {
                if (StringUtil.isNullOrEmpty(projectTaskDTO.getTaskPid())) {//生产那块根节点在sql查出来的taskPid设为null
                    list.add(projectTaskDTO);
                    list.addAll(sortTaskIssue(projectTaskDTOList, projectTaskDTO.getTaskId()));
                }
            }
            if (id.equals(projectTaskDTO.getTaskPid())) {
                list.add(projectTaskDTO);
                list.addAll(sortTaskIssue(projectTaskDTOList, projectTaskDTO.getTaskId()));
            }
        }
        return list;
    }


    /**
     * 方法描述：完成生产任务
     * 作者：MaoSF
     * 日期：2017/3/12
     */
    @Override
    public AjaxMessage completeProductTask(HandleMyTaskDTO dto) throws Exception {
        String taskId = dto.getTaskId();
        String companyId = dto.getCurrentCompanyId();
        String projectId = dto.getProjectId();
        String accountId = dto.getAccountId();
        String status = dto.getStatus();
        //把任务设置为完成
        ProjectTaskEntity projectTaskEntity = this.projectTaskDao.selectById(taskId);
        if (projectTaskEntity == null) {
            return AjaxMessage.failed("操作失败");
        }
        //1.判断是否存在设校审，并且是是否完成
        Map<String, Object> map = new HashMap<>();

        /***************此逻辑于2017-11-28调整，去掉设校审的限制********************/
//        map.put("taskManageId", taskId);
//        map.put("notComplete", "1");//查询未完成的
//        List<ProjectProcessNodeEntity> processNodeList = this.projectProcessNodeDao.getProcessNodeByParam(map);
//        if (!CollectionUtils.isEmpty(processNodeList)) {
//            String taskNames = projectTaskEntity.getTaskName() + "中";
//            for (ProjectProcessNodeEntity entity : processNodeList) {
//                taskNames += entity.getNodeName() + "、";
//            }
//            taskNames = taskNames.substring(0, taskNames.length() - 1);
//            return AjaxMessage.failed(taskNames + "工作还未完成，该任务不能标记成完成状态。");
//        }
        //2.判断子任务是否完成
        map.put("taskPid", taskId);
        map.put("notIncludeDesignTask", "1");//不包含设计任务
        map.put("notComplete", "1");//查询未完成的
        List<ProjectTaskEntity> taskList = this.projectTaskDao.selectByParam(map);
        if (!CollectionUtils.isEmpty(taskList)) {
            String taskNames = "";
            for (ProjectTaskEntity entity : taskList) {
                taskNames += entity.getTaskName() + "、";
            }
            taskNames = taskNames.substring(0, taskNames.length() - 1);
            return AjaxMessage.failed("此任务中" + taskNames + "还未完成，该任务不能标记成完成状态");
        }

        if (StringUtil.isNullOrEmpty(projectTaskEntity.getCompleteDate())) {
            //保存原有数据
            ProjectTaskEntity originTask = new ProjectTaskEntity();
            BeanUtilsEx.copyProperties(projectTaskEntity, originTask);

            //设置完成状态
            if ("0".equals(status)) {
                projectTaskEntity.setEndStatus(SystemParameters.STATUS_NOT_COMPLETE);
            } else {
                projectTaskEntity.setEndStatus(SystemParameters.STATUS_COMPLETE);
            }
            projectTaskEntity.setCompletion(dto.getCompletion());
            projectTaskEntity.setCompleteDate(StringUtil.isNullOrEmpty(dto.getPaidDate())?DateUtils.getDate():DateUtils.str2Date(dto.getPaidDate(),DateUtils.date_sdf));
            this.projectTaskDao.updateById(projectTaskEntity);

            //生成项目动态
            dynamicService.addDynamic(originTask, projectTaskEntity, companyId, accountId);
            /**************下面代码需要处理****************/
            //此处只处理生产的根任务
            if (projectTaskEntity.getTaskType() == SystemParameters.TASK_TYPE_ISSUE || projectTaskEntity.getTaskType() == SystemParameters.TASK_TYPE_PHASE) {
                //给经营任务负责人推送消息
                ProjectMemberEntity projectManager = this.projectMemberService.getOperatorManager(projectId, companyId);
                if (projectManager != null) {//该处如果是当前操作人大话，都需要推送此消息
                    this.sendMessage(projectId, taskId, companyId, projectManager.getCompanyUserId(), projectManager.getAccountId(), accountId, SystemParameters.MESSAGE_TYPE_406, companyId);
                }
                //判断是否所有的生产任务已经完成，如果完成，则需要给当前组织的设计负责人推送消息
                map.clear();
                map.put("projectId", projectId);
                map.put("notComplete", "1");//查询未完成的
                map.put("companyId", companyId);
                if (CollectionUtils.isEmpty(this.projectTaskDao.selectByParam(map))) {//如果不存在未完成的，则全部完成，给设计负责人推送消息
                    List<ProjectMemberEntity> designList = this.projectMemberService.listDesignManagerAndAssist(projectId, companyId);
                    List<ProjectMemberEntity> designs = getDesignManagerFilterMe(designList, accountId);
                    for (ProjectMemberEntity designerManager : designs) {
                        this.sendMessage(projectId, taskId, companyId, designerManager.getCompanyUserId(), designerManager.getAccountId(), accountId, SystemParameters.MESSAGE_TYPE_407, companyId);
                    }
                }
                //判断所有的任务是否已经完成
                this.sendMeaninglessTask(projectId, taskId, companyId, accountId);
                /**********20170921屏蔽**************/
                // this.handleProjectTaskCompleteDate(projectTaskEntity.getId(),accountId);
            }
            //如果是生产的任务
            if (!StringUtil.isNullOrEmpty(projectTaskEntity.getTaskPid()) && projectTaskEntity.getTaskType() == SystemParameters.TASK_TYPE_PRODUCT) {
                //通知上一级任务负责人
                ProjectMemberEntity parentLeader = this.projectMemberService.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_TASK_RESPONSIBLE, projectTaskEntity.getTaskPid());
                if (parentLeader != null && !(StringUtil.isSame(parentLeader.getAccountId(), accountId))) {
                    this.sendMessage(projectId, taskId, companyId, parentLeader.getCompanyUserId(), parentLeader.getAccountId(), accountId, SystemParameters.MESSAGE_TYPE_406, companyId);
                }
            }
            /******************通知协同**************************/
            this.collaborationService.pushSyncCMD_PT(projectTaskEntity.getProjectId(), projectTaskEntity.getTaskPath(), SyncCmd.PT2);
        }
        return AjaxMessage.succeed("操作成功");
    }

    /**
     * 交换两个任务排序位置
     *
     * @param taskList   任务编号及任务排序编号
     * @param operatorId 操作者ID
     */
    @Override
    public void exchangeTask(List<TaskSequencingDTO> taskList, String operatorId) {
        assert taskList != null : "exchangeTask 参数错误";

        TaskSequencingDTO task = null;
        for (TaskSequencingDTO t : taskList) {
            //获取任务的排序编号
            if ((t.getSeq() == null) || (t.getSeq() == -1)) {
                ProjectTaskEntity entity = projectTaskDao.selectById(t.getId());
                assert entity != null : "exchangeTask 没有查到对应数据";
                t.setSeq(entity.getSeq());
            }

            //保存第一个元素，把后一个元素的排序编号设定为上一个元素的排序编号
            if (task == null) {
                task = t;
            } else {
                //下一个任务的排序编号设为上一个任务的排序编号
                ProjectTaskEntity entity = new ProjectTaskEntity();
                entity.setId(t.getId());
                entity.setSeq(task.getSeq());
                entity.setUpdateBy(operatorId);
                entity.setUpdateDate(new Date());
                projectTaskDao.updateById(entity);
                projectTaskDao.updateByIdOrModifyId(entity);
                task.setSeq(t.getSeq());
            }
        }

        //第一个任务的排序编号设为最后一个任务的排序编号
        if (task != null) {
            // 更新当前记录的正式数据及草稿的seq
            ProjectTaskEntity entity = new ProjectTaskEntity();
            entity.setId(task.getId());
            entity.setSeq(task.getSeq());
            entity.setUpdateBy(operatorId);
            entity.setUpdateDate(new Date());
            projectTaskDao.updateById(entity);
            projectTaskDao.updateByIdOrModifyId(entity);
        }
    }

    /**
     * 任务负责人确认完成任务
     */
    @Override
    public AjaxMessage completeTask(HandleMyTaskDTO dto) throws Exception {
        String taskId = dto.getTaskId();
        String currentUserId = dto.getAccountId();
        String currentCompanyUserId = dto.getCurrentCompanyUserId();
        MyTaskDTO myTask = myTaskDao.getMyTaskByTaskId(taskId);
        if (myTask != null) {
            myTaskDao.finishMyTaskByTaskIdWithoutId(taskId, myTask.getId());
            //todo 把skydrive内的临时文件类型改为正式文件
//            updateSkyDriveFileType(dto);
            projectTaskDao.finishSubDesignTask(taskId);
            dto.setId(myTask.getId());
            return myTaskService.handleMyTask(dto);
        } else {
            //如果是任务类型是设计分解的任务，则没有MyTask任务。
            ProjectTaskEntity task = projectTaskDao.selectById(taskId);
            if (task != null && task.getTaskType() == SystemParameters.TASK_DESIGN_TYPE) {
                ProjectProcessNodeEntity node = projectProcessNodeDao.getNodeByCompanyUserAndSeq(taskId, currentCompanyUserId, 1);
                if (node != null) {
                    return projectProcessService.completeProjectProcessNode(task.getProjectId(), task.getCompanyId(), node.getId(), taskId, currentUserId);
                }
                return null;
            }
        }
        return AjaxMessage.error("操作失败");
    }

    private void updateSkyDriveFileType(HandleMyTaskDTO dto){
        ProjectSkyDriveDTO data = new ProjectSkyDriveDTO();
        data.setType(1);
        SkyDriveQueryDTO query = new SkyDriveQueryDTO();
        query.setFileIdList(dto.getFileIdList());
        SkyDriveUpdateDTO request = new SkyDriveUpdateDTO();
        request.setData(data);
        request.setQuery(query);
        projectSkyDriverDao.updateSkyDrive(request);
    }

    @Override
    public void updateProjectTaskForChangeCompany(TaskChangeCompanyDTO dto) throws Exception {
        this.projectTaskDao.changeCompany(dto);
    }

    @Override
    public boolean isEditIssueTask(String projectId, String companyId, String accountId) throws Exception {
        //经营负责人
        //  ProjectManagerEntity managerEntity = this.projectManagerDao.getProjectOperaterManager(query.getProjectId(),query.getCompanyId());
        ProjectMemberDTO manager = this.projectMemberService.getOperatorManagerDTO(projectId, companyId);
        if (manager != null) {
            if (manager.getAccountId().equals(accountId)) {
                return true;
            }
        }
        //助理
        ProjectMemberDTO assistant = this.projectMemberService.getProjectMemberByParam(projectId, companyId, ProjectMemberType.PROJECT_OPERATOR_MANAGER_ASSISTANT, null);
        if (assistant != null) {
            if (assistant.getAccountId().equals(accountId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param query 要查询的项目信息
     * @return 相应的签发任务列表，及一个"全部"标签
     * @author 张成亮
     * @date 2018/7/12
     * @description 查询生产任务标签列表
     **/
    @Override
    public ProjectProductTaskGroupInfoDTO getTaskGroupInfo(QueryProjectTaskDTO query) throws Exception {
        List<BaseShowDTO> tabList = new ArrayList<>();
        //添加全部标签
        BaseShowDTO tabAll = new BaseShowDTO("","全部");
        tabList.add(tabAll);
        //获取签发列表
        List<ProjectIssueTaskDTO> issueList = this.projectTaskDao.getOperatorTaskList(query);
        //过滤非本公司签发任务
        List<BaseShowDTO> tmpList = new ArrayList<>();
        issueList.forEach(issue->{
            String currentCompanyId = query.getCurrentCompanyId();
            if (currentCompanyId.equals(issue.getCompanyId())){
                tmpList.add(new BaseShowDTO(issue.getId(),issue.getTaskName()));
            }
        });
        tabList.addAll(tmpList);

        ProjectProductTaskGroupInfoDTO result = new ProjectProductTaskGroupInfoDTO();
        result.setTabList(tabList);
        return result;
    }
}
