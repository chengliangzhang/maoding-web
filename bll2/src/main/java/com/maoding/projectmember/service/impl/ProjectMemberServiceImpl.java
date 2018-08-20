package com.maoding.projectmember.service.impl;

import com.maoding.core.constant.ProjectMemberStatus;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.StringUtil;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import com.maoding.mytask.dao.MyTaskDao;
import com.maoding.mytask.entity.MyTaskEntity;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dto.CompanyUserTableDTO;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dto.ProjectDesignUser;
import com.maoding.project.dto.ProjectDesignUserList;
import com.maoding.project.dto.ProjectTaskProcessNodeDTO;
import com.maoding.project.dto.ProjectWorkingHoursDTO;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.service.ProjectProcessService;
import com.maoding.projectmember.dao.ProjectMemberDao;
import com.maoding.projectmember.dto.MemberQueryDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.projectmember.service.ProjectMemberService;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.entity.ProjectTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by Idccapp22 on 2017/6/6.
 */
@Service("projectMemberService")
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired
    private ProjectMemberDao projectMemberDao;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProjectTaskDao projectTaskDao;

    @Autowired
    private MyTaskDao myTaskDao;

    @Autowired
    private ProjectProcessService projectProcessService;

    @Override
    public ProjectMemberEntity getProjectMemberById(String id) {
        return projectMemberDao.selectById(id);
    }

    @Override
    public ProjectMemberEntity insertProjectMember(String projectId, String companyId, String companyUserId, String accountId, Integer memberType, String targetId, String nodeId, Integer seq, String createBy) throws Exception {
        ProjectMemberEntity projectMember = new ProjectMemberEntity();
        projectMember.setId(StringUtil.buildUUID());
        projectMember.setProjectId(projectId);
        projectMember.setCompanyId(companyId);
        projectMember.setCompanyUserId(companyUserId);
        projectMember.setAccountId(accountId);
        projectMember.setTargetId(targetId);
        projectMember.setNodeId(nodeId);
        projectMember.setMemberType(memberType);
        projectMember.setStatus(ProjectMemberStatus.FORMAL_STATUS);
        projectMember.setDeleted(0);
        projectMember.setSeq(seq == null ? 0 : seq);
        projectMember.setCreateBy(createBy);
        projectMember.setCreateDate(new Date());
        syncCompanyUserIdToAccountId(projectMember, companyUserId, accountId);
        projectMemberDao.insert(projectMember);
        return projectMember;
    }

    /**
     * 方法描述：保存项目成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public ProjectMemberEntity saveProjectMember(String projectId, String companyId, String companyUserId, String accountId, Integer memberType, String targetId, String nodeId, Integer seq, String createBy, boolean isSendMessage, String currentCompanyId) throws Exception {
        ProjectMemberEntity projectMember = this.insertProjectMember(projectId, companyId, companyUserId, accountId, memberType, targetId, nodeId, seq, createBy);
        //TODO 推送任务
        this.sendTaskToUser(projectId, companyId, companyUserId, memberType, targetId, nodeId, isSendMessage, createBy, currentCompanyId);

        //TODO 把人员添加到项目群组中
        // this.imGroupService.addUserToProjectGroup(projectId,companyUserId,accountId,targetId,memberType);

        return projectMember;
    }

    /**
     * 方法描述：保存项目成员（未发布版本的数据：status=2）
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public ProjectMemberEntity saveProjectMember(String projectId, String companyId, String companyUserId, String targetId, Integer memberType, Integer seq, String createBy, String currentCompanyId) throws Exception {
        ProjectMemberEntity projectMember = null;
        if (memberType == ProjectMemberType.PROJECT_TASK_RESPONSIBLE) {
            projectMember = insertProjectMember(projectId, companyId, companyUserId, null, memberType, targetId, null, seq, createBy);
        } else {
            projectMember = insertProjectMember(projectId, companyId, companyUserId, null, memberType, null, targetId, seq, createBy);
        }
        return projectMember;
    }

    /**
     * 方法描述：保存项目成员(只推送消息，不发送任务)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public ProjectMemberEntity saveProjectMember(String projectId, String companyId, String companyUserId, Integer memberType, String createBy, boolean isSendMessage, String currentCompanyId) throws Exception {
        ProjectMemberEntity projectMember = this.insertProjectMember(projectId, companyId, companyUserId, null, memberType, null, null, 1, createBy);
        //推送消息
        if (isSendMessage) {
            sendMessageToUser(projectId, companyId, companyUserId, memberType, createBy, currentCompanyId);
        }
        //如果是添加立项人，则不处理添加到项目成员中，因为，立项人就是创建项目群组的管理员
//        if(memberType != ProjectMemberType.PROJECT_CREATOR){
//            //TODO 把人员添加到项目群组中
//            imGroupService.addUserToProjectGroup(projectId,companyUserId,null,null,memberType);
//        }
        return projectMember;
    }


    /**
     * 方法描述：保存项目成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public ProjectMemberEntity saveProjectMember(String projectId, String companyId, String companyUserId, String accountId, Integer memberType, String targetId, String createBy, boolean isSendMessage, String currentCompanyId) throws Exception {
        return this.saveProjectMember(projectId, companyId, companyUserId, accountId, memberType, targetId, null, null, createBy, isSendMessage, currentCompanyId);
    }

    /**
     * 方法描述：保存项目成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public ProjectMemberEntity saveProjectMember(String projectId, String companyId, String companyUserId, String accountId, Integer memberType, String createBy, boolean isSendMessage, String currentCompanyId) throws Exception {
        return this.saveProjectMember(projectId, companyId, companyUserId, accountId, memberType, null, null, null, createBy, isSendMessage, currentCompanyId);
    }

    /**
     * 方法描述：经营负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    @Override
    public ProjectMemberEntity getOperatorManager(String projectId, String companyId) {
        return this.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_OPERATOR_MANAGER, null);
    }

    /**
     * 方法描述：经营助理
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    @Override
    public ProjectMemberEntity getOperatorAssistant(String projectId, String companyId) {
        return this.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_OPERATOR_MANAGER_ASSISTANT, null);
    }

    /**
     * 方法描述：设计负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    @Override
    public ProjectMemberEntity getDesignManager(String projectId, String companyId) {
        return this.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_DESIGNER_MANAGER, null);
    }

    @Override
    public ProjectMemberEntity getDesignManagerAssistant(String projectId, String companyId) {
        return this.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_DESIGNER_MANAGER_ASSISTANT, null);
    }

    /**
     * 方法描述：设计负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    @Override
    public ProjectMemberEntity getTaskDesigner(String taskId) {
        return this.getProjectMember(null, null, ProjectMemberType.PROJECT_TASK_RESPONSIBLE, taskId);
    }

    /**
     * 方法描述：经营负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    @Override
    public ProjectMemberDTO getOperatorManagerDTO(String projectId, String companyId) {
        return this.getProjectMemberByParam(projectId, companyId, ProjectMemberType.PROJECT_OPERATOR_MANAGER, null);
    }

    /**
     * 方法描述：设计负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    @Override
    public ProjectMemberDTO getDesignManagerDTO(String projectId, String companyId) {
        return this.getProjectMemberByParam(projectId, companyId, ProjectMemberType.PROJECT_DESIGNER_MANAGER, null);
    }

    /**
     * 方法描述：设计负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    @Override
    public ProjectMemberDTO getTaskDesignerDTO(String taskId) {
        return this.getProjectMemberByParam(null, null, ProjectMemberType.PROJECT_TASK_RESPONSIBLE, taskId);
    }

    /**
     * 方法描述：立项人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    @Override
    public ProjectMemberDTO getProjectCreatorDTO(String projectId) {
        return this.getProjectMemberByParam(projectId, null, ProjectMemberType.PROJECT_CREATOR, null);
    }

    /**
     * 方法描述：经营负责人+设计负责人
     * 作者：MaoSF
     * 日期：2017/6/12
     */
    @Override
    public List<ProjectMemberDTO> listProjectManager(String projectId, String companyId) {
        List<ProjectMemberDTO> list = this.listProjectMemberByParam(projectId, companyId, null, null);
        List<ProjectMemberDTO> result = new ArrayList<>();
        for (ProjectMemberDTO dto : list) {
            if (dto.getMemberType() == ProjectMemberType.PROJECT_OPERATOR_MANAGER || dto.getMemberType() == ProjectMemberType.PROJECT_DESIGNER_MANAGER) {
                result.add(dto);
            }
        }
        return result;
    }

    /**
     * 方法描述：copy 任务下所有的人员
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    @Override
    public void copyProjectMember(String taskId, String newTaskId) {
        List<ProjectMemberEntity> list = this.projectMemberDao.listProjectMemberByTaskId(taskId);
        for (ProjectMemberEntity entity : list) {
            entity.setId(StringUtil.buildUUID());
            entity.setStatus(ProjectMemberStatus.BE_MODIFY_STATUS);
            if (entity.getMemberType() == ProjectMemberType.PROJECT_TASK_RESPONSIBLE) {
                entity.setTargetId(newTaskId);
            } else {
                entity.setNodeId(newTaskId);
            }
            this.projectMemberDao.insert(entity);
        }
    }

    /**
     * 方法描述：发布任务，处理当前任务下所有的人员
     * 作者：MaoSF
     * 日期：2017/6/21
     *
     * @param taskId（正式记录的ID）
     * @param modifyTaskId（正式记录被关联用于修改记录的ID）
     */
    @Override
    public void publishProjectMember(String projectId, String companyId, String taskId, String modifyTaskId, String accountId) throws Exception {
        CompanyUserEntity userEntity = this.companyUserDao.getCompanyUserByUserIdAndCompanyId(accountId, companyId);
        if (userEntity == null) {
            return;
        }
        List<ProjectMemberEntity> memberList = this.listProjectMember(null, null, null, taskId);
        List<ProjectMemberEntity> modifyMemberList = this.listProjectMember(null, null, null, modifyTaskId);
        //处理任务负责人
        publishTaskResponsible(projectId, companyId, taskId, userEntity.getId(), accountId, memberList, modifyMemberList, companyId);
        //处理设计人员
        publishTaskDesign(projectId, companyId, taskId, userEntity.getId(), accountId, memberList, modifyMemberList);
    }

    /**
     * 方法描述：获取设计人员
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    @Override
    public List<ProjectTaskProcessNodeDTO> listDesignMember(String taskId) {
        return this.projectMemberDao.listDesignMember(taskId);
    }

    @Override
    public ProjectDesignUserList listDesignMemberList(String taskId) {
        ProjectDesignUserList dto = new ProjectDesignUserList();
        List<ProjectTaskProcessNodeDTO> list = listDesignMember(taskId);
        for (ProjectTaskProcessNodeDTO design : list) {
            if (design.getMemberType() == ProjectMemberType.PROJECT_DESIGNER) {
                dto.setDesignUser(design);
            }
            if (design.getMemberType() == ProjectMemberType.PROJECT_PROOFREADER) {
                dto.setCheckUser(design);
            }
            if (design.getMemberType() == ProjectMemberType.PROJECT_AUDITOR) {
                dto.setExamineUser(design);
            }
        }
        return dto;
    }

    /**
     * 方法描述：获取设计人员
     * 作者：MaoSF
     * 日期：2017/6/24
     */
    @Override
    public String getDesignUserByTaskId(String taskId) {
        return this.projectMemberDao.getDesignUserByTaskId(taskId);
    }

    /**
     * 获取人员工时
     */
    @Override
    public List<ProjectMemberDTO> getProjectWorkingHours(ProjectWorkingHoursDTO hoursDTO) throws Exception {
        return this.projectMemberDao.getProjectWorkingHours(hoursDTO);
    }

    /**
     * 获取人员工时总页数
     */
    @Override
    public Integer getProjectWorkingHoursCount(ProjectWorkingHoursDTO hoursDTO) throws Exception {
        return this.projectMemberDao.getProjectWorkingHoursCount(hoursDTO);
    }

    @Override
    public String getProjectWorkingHoursSum(ProjectWorkingHoursDTO hoursDTO) throws Exception {
        return this.projectMemberDao.getProjectWorkingHoursSum(hoursDTO);
    }


    /**
     * 方法描述：更新成员(交换操作)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public void updateProjectMember(ProjectMemberEntity projectMember, String companyUserId, String accountId, String updateBy, String currentCompanyId, boolean isSendMessage) throws Exception {
        String oldCompanyUserId = projectMember.getCompanyUserId();
        projectMember.setCompanyUserId(companyUserId);
        projectMember.setAccountId(accountId);
        syncCompanyUserIdToAccountId(projectMember, companyUserId, accountId);
        this.projectMemberDao.updateById(projectMember);

        String projectId = projectMember.getProjectId();
        String companyId = projectMember.getCompanyId();
        //处理任务
        if (ProjectMemberType.PROJECT_OPERATOR_MANAGER == projectMember.getMemberType()) {
            this.transferOperatorManagerTask(oldCompanyUserId, companyUserId, projectMember.getCompanyId(), projectMember.getProjectId(), isSendMessage, updateBy, currentCompanyId);
        }
        if (ProjectMemberType.PROJECT_DESIGNER_MANAGER == projectMember.getMemberType()) {
            this.sendMessageTaskDesigner(projectMember.getProjectId(), companyUserId, projectMember.getCompanyId(), null, updateBy, currentCompanyId);
        }
        if (ProjectMemberType.PROJECT_TASK_RESPONSIBLE == projectMember.getMemberType()) {
            this.sendTaskForChangeMember(projectMember.getProjectId(), projectMember.getCompanyId(), companyUserId, oldCompanyUserId, projectMember.getMemberType(), projectMember.getTargetId(), isSendMessage, updateBy, projectMember.getCompanyId());
        }
        if (ProjectMemberType.PROJECT_OPERATOR_MANAGER_ASSISTANT == projectMember.getMemberType()) {
            //把经营助理的相关任务全部copy给新的助理
            this.sendTaskForOperatorAssistMember(projectId, companyId, companyUserId, oldCompanyUserId, updateBy);
        }
        if (ProjectMemberType.PROJECT_DESIGNER_MANAGER_ASSISTANT == projectMember.getMemberType()) {
            //把设计助理的相关任务全部copy给新的助理
            this.sendTaskForDesignAssistMember(projectId, companyId, companyUserId, oldCompanyUserId, updateBy);
        }
        //把人员添加到项目群组中
        //imGroupService.addUserToProjectGroup(projectMember.getProjectId(),companyUserId,accountId,projectMember.getTargetId(),projectMember.getMemberType());

        //移除旧人员
        //this.imGroupService.removeNotContentProject(projectMember.getProjectId(),oldCompanyUserId,null,projectMember.getTargetId(),projectMember.getMemberType());
    }

    /**
     * 方法描述：更新成员状态
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public void updateProjectMember(String projectId, String companyId, String companyUserId, Integer memberType, String targetId) throws Exception {
        ProjectMemberEntity projectMember = new ProjectMemberEntity();
        projectMember.setProjectId(projectId);
        projectMember.setCompanyId(companyId);
        projectMember.setCompanyUserId(companyUserId);
        projectMember.setTargetId(targetId);
        projectMember.setMemberType(memberType);
        this.projectMemberDao.updateProjectMemberStatus(projectMember);
    }

    @Override
    public void updateProjectMember(ProjectMemberEntity projectMember) {
        this.projectMemberDao.updateById(projectMember);
    }

    /**
     * 方法描述：删除成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public void deleteProjectMember(String projectId, String companyId, Integer memberType, String targetId) throws Exception {
        // List<ProjectMemberEntity> list = this.listProjectMember(projectId,companyId,memberType,targetId);
        if (memberType == ProjectMemberType.PROJECT_OPERATOR_MANAGER) {
            ProjectMemberEntity m = this.getOperatorManager(projectId, companyId);
            if (m != null) {
                this.myTaskService.ignoreMyTask(projectId, SystemParameters.ISSUE_TASK, companyId, m.getCompanyUserId());
            }
        }

        ProjectMemberEntity projectMember = new ProjectMemberEntity();
        projectMember.setProjectId(projectId);
        projectMember.setCompanyId(companyId);
        projectMember.setTargetId(targetId);
        projectMember.setMemberType(memberType);
        //TODO 删除操作
        this.projectMemberDao.deleteProjectMember(projectMember);
        //删除任务经营任务
        //TODO 从项目群组移除成员
//        for(ProjectMemberEntity member:list){
//            this.imGroupService.removeNotContentProject(member.getProjectId(),member.getCompanyUserId(),null,member.getTargetId(),member.getMemberType());
//        }
    }

    /**
     * 方法描述：删除成员
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public void deleteProjectMember(Integer memberType, String targetId) throws Exception {
        this.deleteProjectMember(null, null, memberType, targetId);
    }

    /**
     * 方法描述：删除成员(删除修改版本的数据，物理删除)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public void deleteProjectMember(String id) throws Exception {
        this.projectMemberDao.deleteById(id);
    }

    /**
     * 方法描述：获取成员信息
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public List<ProjectMemberEntity> listProjectMember(String projectId, String companyId, Integer memberType, String targetId) {
        MemberQueryDTO projectMember = new MemberQueryDTO();
        projectMember.setProjectId(projectId);
        projectMember.setCompanyId(companyId);
        projectMember.setMemberType(memberType);
        projectMember.setTargetId(targetId);
        return this.projectMemberDao.listProjectMember(projectMember);
    }

    @Override
    public boolean isParentDesigner(String projectId,String taskPath, String companyId, String companyUserId) {
        Map<String, Object> map = new HashMap<>();
        map.put("projectId", projectId);
        map.put("companyId", companyId);
        String[] taskPathList = taskPath.split("-");
        map.put("taskPathList", Arrays.asList(taskPathList));
        map.put("companyUserId", companyUserId);
        if (taskPathList.length>0 && this.projectMemberDao.listParentDesigner(map).size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 方法描述：获取成员信息
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public ProjectMemberEntity getProjectMember(String projectId, String companyId, Integer memberType, String targetId) {
        List<ProjectMemberEntity> list = this.listProjectMember(projectId, companyId, memberType, targetId);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 方法描述：获取成员信息(社校审)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public ProjectMemberEntity getProjectMember(String companyUserId, Integer memberType, String targetId) {
        MemberQueryDTO projectMember = new MemberQueryDTO();
        projectMember.setCompanyUserId(companyUserId);
        projectMember.setMemberType(memberType);
        projectMember.setTargetId(targetId);
        List<ProjectMemberEntity> list = this.projectMemberDao.listProjectMember(projectMember);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ProjectMemberEntity> listDesignManagerAndAssist(String projectId, String companyId) {
        List<ProjectMemberEntity> list = new ArrayList<>();
        ProjectMemberEntity member = this.getDesignManager(projectId, companyId);
        if (member != null) {
            list.add(member);
        }
        ProjectMemberEntity assistant = this.getDesignManagerAssistant(projectId, companyId);//设计助理
        if (assistant != null) {
            list.add(assistant);
        }
        return list;
    }

    /**
     * 方法描述：获取成员信息(带有个人资料信息)
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    @Override
    public List<ProjectMemberDTO> listProjectMemberByParam(String projectId, String companyId, Integer memberType, String targetId) {
        MemberQueryDTO projectMember = new MemberQueryDTO();
        projectMember.setProjectId(projectId);
        projectMember.setCompanyId(companyId);
        projectMember.setMemberType(memberType);
        projectMember.setTargetId(targetId);
        return this.projectMemberDao.listProjectMemberByParam(projectMember);
    }

    @Override
    public List<ProjectMemberDTO> listProjectMemberByParam(String projectId, String companyId) {
        MemberQueryDTO projectMember = new MemberQueryDTO();
        projectMember.setProjectId(projectId);
        projectMember.setCompanyId(companyId);
        projectMember.setNotContainHeadImg("1");//不包含头像
        return this.projectMemberDao.listProjectMemberByParam(projectMember);
    }

    @Override
    public ProjectMemberDTO getProjectMemberByParam(String projectId, String companyId, Integer memberType, String targetId) {
        List<ProjectMemberDTO> list = this.listProjectMemberByParam(projectId, companyId, memberType, targetId);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ProjectMemberDTO> listProjectMember(String projectId, String currentCompanyId, String accountId) {

        ProjectEntity project = projectDao.selectById(projectId);
        if (project == null) {
            return new ArrayList<>();
        }
        MemberQueryDTO projectMember = new MemberQueryDTO();
        projectMember.setProjectId(projectId);
        projectMember.setProjectCompanyId(currentCompanyId);
        List<ProjectMemberDTO> list = this.projectMemberDao.listProjectAllMember(projectMember);
        return list;
    }

    private void setPermission(List<ProjectTaskProcessNodeDTO> list, String companyId, String currentCompanyId, String accountId) {
        if (companyId.equals(currentCompanyId)) {//如果是当前公司的记录，则处理是否具有修改经营负责人和设计负责人的权限
            for (ProjectTaskProcessNodeDTO dto : list) {
                for (ProjectDesignUser user : dto.getUserList()) {
                    if (dto.getMemberType() == ProjectMemberType.PROJECT_OPERATOR_MANAGER || dto.getMemberType() == ProjectMemberType.PROJECT_DESIGNER_MANAGER) {
                        setPermission(dto.getMemberType(), user, companyId, currentCompanyId, accountId);
                    }
                }
            }
        }
    }

    private void setPermission(int type, ProjectDesignUser user, String companyId, String currentCompanyId, String accountId) {

        /******************设置权限代码*******************/
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("companyId", companyId);
        if (1 == type) {
            map.put("permissionId", "51");//经营负责人
        }
        if (2 == type) {
            map.put("permissionId", "52");//经营负责人
        }
        map.put("companyId", currentCompanyId);
        map.put("userId", accountId);
        List<CompanyUserTableDTO> companyUserList = this.companyUserDao.getCompanyUserByPermissionId(map);
        if (!CollectionUtils.isEmpty(companyUserList)) {
            user.setIsUpdateOperator(1);
        }
        /*************************************/
    }

    /**
     * 方法描述：同步插入companyUserId，accountId（防止接口中没有同时、传入CompanyUserId，accountId）
     * 作者：MaoSF
     * 日期：2017/6/6
     */
    private void syncCompanyUserIdToAccountId(ProjectMemberEntity projectMember, String companyUserId, String accountId) {
        if (companyUserId == null && accountId != null) {
            CompanyUserEntity companyUser = companyUserDao.getCompanyUserByUserIdAndCompanyId(accountId, projectMember.getCompanyId());
            if (companyUser != null) {
                projectMember.setCompanyUserId(companyUser.getId());
                projectMember.setAccountId(companyUser.getUserId());
            }
        }
        if (companyUserId != null && accountId == null) {
            CompanyUserEntity companyUser = companyUserDao.selectById(companyUserId);
            if (companyUser != null) {
                projectMember.setAccountId(companyUser.getUserId());
            }
        }
    }

    /**
     * 方法描述：给项目成员推送任务
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    private void sendTaskToUser(String projectId, String companyId, String companyUserId, Integer memberType, String targetId, String nodeId, boolean isSendMessage, String accountId, String currentCompanyUserId) throws Exception {
        switch (memberType) {
            case 1:
                myTaskService.saveMyTask(projectId, SystemParameters.ISSUE_TASK, companyId, companyUserId, isSendMessage, accountId, currentCompanyUserId);
                break;
            case 3:
                //给负责人发送任务负责人
                this.myTaskService.saveMyTask(targetId, SystemParameters.PRODUCT_TASK_RESPONSE, companyId, companyUserId, isSendMessage, accountId, currentCompanyUserId);
                break;
            case 4:
            case 5:
            case 6:
                this.myTaskService.saveMyTask(targetId, SystemParameters.PROCESS_DESIGN, companyId, companyUserId, isSendMessage, accountId, currentCompanyUserId);
                break;
            case 7:
                //给经营助理推送任务
                this.sendTaskForOperatorAssistMember(projectId, companyId, companyUserId, null, accountId);
                break;
            case 8:
                this.sendTaskForDesignAssistMember(projectId, companyId, companyUserId, null, accountId);
                break;
            default:
                ;
        }
    }

    /**
     * 方法描述：乙方和设计负责人，只推送消息
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    private void sendMessageToUser(String projectId, String companyId, String companyUserId, Integer memberType, String currentUserId, String currentCompanyId) throws Exception {
        if (memberType == ProjectMemberType.PROJECT_OPERATOR_MANAGER) {//此处代表乙方
            sendMessageToPartBManager(projectId, companyId, companyUserId, SystemParameters.ISSUE_TASK, null, currentUserId, currentCompanyId);
        }
        if (memberType == ProjectMemberType.PROJECT_DESIGNER_MANAGER) {
            sendMessageTaskDesigner(projectId, companyUserId, companyId, null, currentUserId, currentCompanyId);
        }
    }

    private void sendMessageToPartBManager(String projectId, String companyId, String managerId, int type, String content, String currentUserId, String currentCompanyId) throws Exception {

        CompanyUserEntity user = this.companyUserDao.selectById(managerId);
        if (content == null) {
            ProjectEntity project = this.projectDao.selectById(projectId);
            if (project != null) {
                content = project.getProjectName();
            }
        }
        if (user != null) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setMessageType(type);
            messageEntity.setProjectId(projectId);
            messageEntity.setCompanyId(companyId);
            messageEntity.setMessageContent(content);
            messageEntity.setTargetId(projectId);
            messageEntity.setUserId(user.getUserId());
            messageEntity.setCreateBy(currentUserId);
            messageEntity.setCreateDate(new Date());
            messageEntity.setSendCompanyId(currentCompanyId);
            this.messageService.sendMessage(messageEntity);
        }
    }

    /**
     * 方法描述：更换经营负责人，把经营负责人的所有任务移交给新的经营负责人
     * 作者：MaoSF
     * 日期：2017/5/9
     */
    private void transferOperatorManagerTask(String oldCompanyUserId, String newCompanyUserId, String companyId, String projectId, boolean isSendMessage, String currentUserId, String currentCompanyId) throws Exception {

        //查询当前项目中经营负责人处理费用的任务
        Map<String, Object> param = new HashMap<>();
        param.put("status", "0");
        List<String> typeList = new ArrayList<>();
        typeList.add("" + SystemParameters.ISSUE_TASK);
        typeList.add("" + SystemParameters.ARRANGE_TASK_DESIGN);
        typeList.add("" + SystemParameters.TECHNICAL_REVIEW_FEE_OPERATOR_MANAGER);
        typeList.add("" + SystemParameters.COOPERATIVE_DESIGN_FEE_ORG_MANAGER);
        param.put("typeList", typeList);
        param.put("companyId", companyId);
        param.put("projectId", projectId);
        param.put("handlerId", oldCompanyUserId);
        List<MyTaskEntity> myTaskList = this.myTaskService.getMyTaskByParamter(param);
        if (!CollectionUtils.isEmpty(myTaskList)) {
            for (MyTaskEntity myTask : myTaskList) {
                myTask.setHandlerId(newCompanyUserId);
                this.myTaskService.saveMyTask(myTask, isSendMessage);
            }
            //把所有的任务设置为无效
            param.put("param4", "1");
            this.myTaskDao.updateStatesByTargetId(param);
        } else {//如果是乙方，没有任务的时候，则只推送消息
            if (isSendMessage) {
                CompanyUserEntity user = this.companyUserDao.selectById(newCompanyUserId);
                if (user == null) {
                    return;
                }
                ProjectEntity project = this.projectDao.selectById(projectId);
                if (project == null) {
                    return;
                }
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setMessageType(SystemParameters.MESSAGE_TYPE_3);
                messageEntity.setProjectId(projectId);
                messageEntity.setCompanyId(companyId);
                messageEntity.setMessageContent(project.getProjectName());
                messageEntity.setTargetId(projectId);
                messageEntity.setUserId(user.getUserId());
                messageEntity.setCreateBy(currentUserId);
                messageEntity.setCreateDate(new Date());
                messageEntity.setSendCompanyId(currentCompanyId);
                this.messageService.sendMessage(messageEntity);
            }
        }

    }

    public void sendMessageTaskDesigner(String projectId, String companyUserId, String companyId, String taskId, String currentUserId, String currentCompanyId) throws Exception {
        ProjectEntity projectEntity = this.projectDao.selectById(projectId);
        boolean isPartB = false;
        CompanyUserEntity companyUserEntity = this.companyUserDao.selectById(companyUserId);
        if (projectEntity != null && companyUserEntity != null) {
            isPartB = !StringUtil.isNullOrEmpty(projectEntity.getCompanyBid()) && !projectEntity.getCompanyId().equals(projectEntity.getCompanyBid()) && !companyId.equals(projectEntity.getCompanyId());
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setProjectId(projectId);
            messageEntity.setTargetId(projectId);
            messageEntity.setCompanyId(companyId);
            messageEntity.setUserId(companyUserEntity.getUserId());
            messageEntity.setMessageType(isPartB ? SystemParameters.MESSAGE_TYPE_2 : SystemParameters.MESSAGE_TYPE_4);
            messageEntity.setCreateBy(currentUserId);
            messageEntity.setCreateDate(new Date());
            messageEntity.setSendCompanyId(currentCompanyId);
            if (taskId == null) {
                List<String> taskList = projectTaskDao.getProjectTaskOfToCompany(projectId, companyId);
                if ((taskList == null) || (taskList.size() == 0)) {
                    messageEntity.setMessageContent(projectEntity.getProjectName());
                    this.messageService.sendMessage(messageEntity);
                } else {
                    for (String t : taskList) {
                        messageEntity.setMessageContent(projectEntity.getProjectName() + " - " + projectTaskDao.getTaskParentName(t));
                        this.messageService.sendMessage(messageEntity);
                    }
                }
            } else {
                messageEntity.setMessageContent(projectEntity.getProjectName() + " - " + projectTaskDao.getTaskParentName(taskId));
                this.messageService.sendMessage(messageEntity);
            }
        }
    }

    /**
     * 方法描述：更换任务负责人任务处理
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    private void sendTaskForChangeMember(String projectId, String companyId, String companyUserId, String oldCompanyUserId, Integer memberType, String targetId, boolean isSendMessage, String accountId, String currentCompanyUserId) throws Exception {

        int taskType = 0;
        if (memberType == ProjectMemberType.PROJECT_OPERATOR_MANAGER) {
            taskType = SystemParameters.ISSUE_TASK;
        }
        if (memberType == ProjectMemberType.PROJECT_TASK_RESPONSIBLE) {
            taskType = SystemParameters.PRODUCT_TASK_RESPONSE;
        }

        if (taskType > 0) {
            if (oldCompanyUserId != null) {
                //忽略原来负责人的任务
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("companyId", companyId);
                paramMap.put("projectId", projectId);
                paramMap.put("taskType", taskType);
                paramMap.put("param4", "1");
                paramMap.put("handlerId", oldCompanyUserId);
                paramMap.put("targetId", targetId);
                myTaskService.updateStatesByTargetId(paramMap);
                //必要时忽略原负责人的任务分解任务
                if (taskType == SystemParameters.PRODUCT_TASK_RESPONSE) {
                    //寻找原负责人在此公司、项目中是否有生产任务
                    List<ProjectTaskEntity> list = projectTaskDao.getMyResponsibleTask(projectId, companyId, oldCompanyUserId, "onlyNotComplete");
                    //如果没有，则删除任务分解任务
                    if ((list == null) || (list.size() == 0)) {
                        myTaskService.ignoreMyTaskForResponsible(projectId, companyId, oldCompanyUserId);
                    }
                }
            }
            if (!StringUtil.isNullOrEmpty(companyUserId)) {
                //给新的任务负责人推送任务
                this.myTaskService.saveMyTask(targetId, taskType, companyId, companyUserId, isSendMessage, accountId, currentCompanyUserId);
            }
        }
    }

    /**
     * 删除助理
     */
    @Override
    public void deleteAssistMember(ProjectMemberEntity entity, String accountId) throws Exception {
        Integer memberType = ProjectMemberType.PROJECT_OPERATOR_MANAGER_ASSISTANT;
        if (ProjectMemberType.PROJECT_OPERATOR_MANAGER_ASSISTANT.equals(memberType)) {
            deleteTaskForOperatorAssistMember(entity.getProjectId(), entity.getCompanyId(), entity.getCompanyUserId(), null, accountId);
        } else if (ProjectMemberType.PROJECT_DESIGNER_MANAGER_ASSISTANT.equals(memberType)) {
            deleteTaskForDesignAssistMember(entity.getProjectId(), entity.getCompanyId(), entity.getCompanyUserId(), null, accountId);
        }
        deleteProjectMember(entity.getId());
    }

    @Override
    public List<String> getProjectMemberByUserIdAndTyep(Map<String, Object> map) {
        return this.projectMemberDao.getProjectMemberByUserIdAndTyep(map);
    }

    /**
     * 方法描述：删除经营助理的所有任务
     * 作者：ZCL
     * 日期：2018/3/7
     */
    private void deleteTaskForOperatorAssistMember(String projectId, String companyId, String companyUserId, String oldCompanyUserId, String updateBy) throws Exception {
        //删除指定组织，指定项目的经营助理的任务
        Map<String, Object> param = new HashMap<>();
        param.put("param4","1");
        List<String> typeList = new ArrayList<>();
        typeList.add("" + SystemParameters.ISSUE_TASK);
        typeList.add("" + SystemParameters.ARRANGE_TASK_DESIGN);
        typeList.add("" + SystemParameters.TECHNICAL_REVIEW_FEE_OPERATOR_MANAGER);
        typeList.add("" + SystemParameters.COOPERATIVE_DESIGN_FEE_ORG_MANAGER);
        param.put("typeList", typeList);
        param.put("companyId", companyId);
        param.put("projectId", projectId);
        param.put("handlerId", oldCompanyUserId);
        this.myTaskDao.updateStatesByTargetId(param);
    }

    /**
     * 方法描述：把经营负责人的所有任务copy到助理上
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    private void sendTaskForOperatorAssistMember(String projectId, String companyId, String companyUserId, String oldCompanyUserId, String updateBy) throws Exception {
        ProjectMemberEntity member = this.getOperatorManager(projectId, companyId);
        if (member != null && !member.getCompanyUserId().equals(companyUserId)) { //如果经营负责人！=设计助理
            //查询当前项目中经营负责人处理费用的任务
            Map<String, Object> param = new HashMap<>();
            param.put("status", "0");
            List<String> typeList = new ArrayList<>();
            typeList.add("" + SystemParameters.ISSUE_TASK);
            typeList.add("" + SystemParameters.ARRANGE_TASK_DESIGN);
            typeList.add("" + SystemParameters.TECHNICAL_REVIEW_FEE_OPERATOR_MANAGER);
            typeList.add("" + SystemParameters.COOPERATIVE_DESIGN_FEE_ORG_MANAGER);
            param.put("typeList", typeList);
            param.put("companyId", companyId);
            param.put("projectId", projectId);
            param.put("handlerId", member.getCompanyUserId());
            List<MyTaskEntity> myTaskList = this.myTaskService.getMyTaskByParamter(param);
            if (!CollectionUtils.isEmpty(myTaskList)) {
                for (MyTaskEntity myTask : myTaskList) {
                    myTask.setHandlerId(companyUserId);
                    myTask.setCreateBy(updateBy);
                    this.myTaskService.saveMyTask(myTask, false);
                }
            }
            //如果是更换设计助理
            if (oldCompanyUserId != null && !member.getCompanyUserId().equals(oldCompanyUserId)) {
                //把所有的任务设置为无效
                param.put("param4","1");
                param.put("handlerId", oldCompanyUserId);
                this.myTaskDao.updateStatesByTargetId(param);
            }
        }
    }

    /**
     * 方法描述：删除设计助理的任务
     * 作者：zcl
     * 日期：2017/6/7
     */
    private void deleteTaskForDesignAssistMember(String projectId, String companyId, String companyUserId, String oldCompanyUserId, String updateBy) throws Exception {
        //删除指定项目，指定组织设计助理的任务
        Map<String, Object> param = new HashMap<>();
        param.put("param4","1");
        List<String> typeList = new ArrayList<>();
        typeList.add("" + SystemParameters.TASK_COMPLETE);
        param.put("typeList", typeList);
        param.put("companyId", companyId);
        param.put("projectId", projectId);
        param.put("handlerId", oldCompanyUserId);
        this.myTaskDao.updateStatesByTargetId(param);
    }

    /**
     * 方法描述：把经营负责人的所有任务copy到助理上
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    private void sendTaskForDesignAssistMember(String projectId, String companyId, String companyUserId, String oldCompanyUserId, String updateBy) throws Exception {
        ProjectMemberEntity member = this.getDesignManager(projectId, companyId);
        if (member != null && !member.getCompanyUserId().equals(companyUserId)) { //如果经营负责人！=设计助理
            //查询当前项目中经营负责人处理费用的任务
            Map<String, Object> param = new HashMap<>();
            param.put("status", "0");
            List<String> typeList = new ArrayList<>();
            typeList.add("" + SystemParameters.TASK_COMPLETE);
            param.put("typeList", typeList);
            param.put("companyId", companyId);
            param.put("projectId", projectId);
            param.put("handlerId", member.getCompanyUserId());
            List<MyTaskEntity> myTaskList = this.myTaskService.getMyTaskByParamter(param);
            if (!CollectionUtils.isEmpty(myTaskList)) {
                for (MyTaskEntity myTask : myTaskList) {
                    myTask.setHandlerId(companyUserId);
                    myTask.setCreateBy(updateBy);
                    this.myTaskService.saveMyTask(myTask, false);
                }
            }
            //如果是更换设计助理
            if (oldCompanyUserId != null && !member.getCompanyUserId().equals(oldCompanyUserId)) {
                //把所有的任务设置为无效
                param.put("param4","1");
                param.put("handlerId", oldCompanyUserId);
                this.myTaskDao.updateStatesByTargetId(param);
            }
        }
    }

    /**
     * 方法描述：发布任务，处理任务负责人
     * 作者：MaoSF
     * 日期：2017/6/22
     */
    private void publishTaskResponsible(String projectId, String companyId, String targetId, String currentCompanyUserId, String accountId, List<ProjectMemberEntity> memberList, List<ProjectMemberEntity> modifyMemberList, String currentCompanyId) throws Exception {
        List<ProjectMemberEntity> oldList = getByMemberType(memberList, ProjectMemberType.PROJECT_TASK_RESPONSIBLE);
        List<ProjectMemberEntity> modifyList = getByMemberType(modifyMemberList, ProjectMemberType.PROJECT_TASK_RESPONSIBLE);
        ProjectMemberEntity oldMember = (CollectionUtils.isEmpty(oldList) ? null : oldList.get(0));
        String oldMemberId = (CollectionUtils.isEmpty(oldList) ? null : oldList.get(0).getCompanyUserId());
        String modifyMemberId = (CollectionUtils.isEmpty(modifyList) ? null : modifyList.get(0).getCompanyUserId());
        if ((StringUtil.isNullOrEmpty(oldMemberId) && StringUtil.isNullOrEmpty(modifyMemberId)) || (!StringUtil.isNullOrEmpty(oldMemberId) && !StringUtil.isNullOrEmpty(modifyMemberId) && oldMemberId.equals(modifyMemberId))) {
            return;
        }
        boolean isSendMessage = false;
        if (!StringUtil.isNullOrEmpty(modifyMemberId) && !currentCompanyUserId.equals(modifyMemberId)) {
            isSendMessage = true;
        }
        if (oldMember == null) {
            //新增
            this.saveProjectMember(projectId, companyId, modifyMemberId, null, ProjectMemberType.PROJECT_TASK_RESPONSIBLE, targetId, accountId, isSendMessage, companyId);
        } else {
            //更新

            updateProjectMember(oldMember, modifyMemberId, null, accountId, currentCompanyId, isSendMessage);
        }
    }

    /**
     * 方法描述：发布任务，处理设计人员
     * 作者：MaoSF
     * 日期：2017/6/22
     */
    private void publishTaskDesign(String projectId, String companyId, String targetId, String companyUserId, String accountId, List<ProjectMemberEntity> memberList, List<ProjectMemberEntity> modifyMemberList) throws Exception {
        //设计
        List<ProjectMemberEntity> oldList = getByMemberType(memberList, ProjectMemberType.PROJECT_DESIGNER);
        List<ProjectMemberEntity> modifyList = getByMemberType(modifyMemberList, ProjectMemberType.PROJECT_DESIGNER);
        publishTaskDesignByMemberType(projectId, companyId, targetId, ProjectMemberType.PROJECT_DESIGNER, companyUserId, accountId, oldList, modifyList);
        //校对
        oldList = getByMemberType(memberList, ProjectMemberType.PROJECT_PROOFREADER);
        modifyList = getByMemberType(modifyMemberList, ProjectMemberType.PROJECT_PROOFREADER);
        publishTaskDesignByMemberType(projectId, companyId, targetId, ProjectMemberType.PROJECT_PROOFREADER, companyUserId, accountId, oldList, modifyList);
        //审核
        oldList = getByMemberType(memberList, ProjectMemberType.PROJECT_AUDITOR);
        modifyList = getByMemberType(modifyMemberList, ProjectMemberType.PROJECT_AUDITOR);
        publishTaskDesignByMemberType(projectId, companyId, targetId, ProjectMemberType.PROJECT_AUDITOR, companyUserId, accountId, oldList, modifyList);
    }

    private List<ProjectMemberEntity> getByMemberType(List<ProjectMemberEntity> memberList, Integer memberType) {
        List<ProjectMemberEntity> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(memberList)) {
            for (ProjectMemberEntity member : memberList) {
                if (member.getMemberType() == memberType) {
                    list.add(member);
                }
            }
        }
        return list;
    }

    private void publishTaskDesignByMemberType(String projectId, String companyId, String targetId, Integer memberType, String currentCompanyUserId, String accountId, List<ProjectMemberEntity> oldList, List<ProjectMemberEntity> modifyList) throws Exception {
        List<ProjectMemberEntity> deleteList = new ArrayList<>();
        List<ProjectMemberEntity> addList = new ArrayList<>();
        if (CollectionUtils.isEmpty(oldList) && CollectionUtils.isEmpty(modifyList)) {//不做处理
            return;
        }
        if (CollectionUtils.isEmpty(oldList) && !CollectionUtils.isEmpty(modifyList)) {//全部添加
            addList.addAll(modifyList);
        }
        if (!CollectionUtils.isEmpty(oldList) && CollectionUtils.isEmpty(modifyList)) {//全部移除
            deleteList.addAll(oldList);
        }
        if (!CollectionUtils.isEmpty(oldList) && !CollectionUtils.isEmpty(modifyList)) {//两者对比
            for (ProjectMemberEntity m : oldList) {
                boolean isDelete = true;
                for (ProjectMemberEntity m2 : modifyList) {
                    if (m.getCompanyUserId().equals(m2.getCompanyUserId())) {
                        isDelete = false;
                        break;
                    }
                }
                if (isDelete) {
                    deleteList.add(m);
                }
            }
            for (ProjectMemberEntity m : modifyList) {
                boolean isAdd = true;
                for (ProjectMemberEntity m2 : oldList) {
                    if (m.getCompanyUserId().equals(m2.getCompanyUserId())) {
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd) {
                    addList.add(m);
                }
            }
        }

        for (ProjectMemberEntity m : deleteList) {
            this.projectProcessService.deleteProcessNodeById(m.getTargetId());
            //忽略任务
            myTaskService.ignoreMyTask(m.getTargetId());
            this.deleteProjectMember(projectId, null, memberType, m.getTargetId());
        }
        for (ProjectMemberEntity m : addList) {
            boolean isSendMessage = true;
            if (currentCompanyUserId.equals(m.getCompanyUserId())) {
                isSendMessage = false;
            }
            //保存设计流程
            String nodeId = this.projectProcessService.saveProjectProcessNode(projectId, memberType, m.getSeq(), targetId, m.getCompanyUserId());
            //保存成员
            this.saveProjectMember(projectId, companyId, m.getCompanyUserId(), null, memberType, nodeId, targetId, m.getSeq(), accountId, isSendMessage, companyId);
        }
    }

    /**
     * @param query 查询条件
     * @return 项目成员列表
     * @author 张成亮
     * @date 2018/7/19
     * @description 查询项目成员
     **/
    @Override
    public List<ProjectMemberDTO> listByQuery(MemberQueryDTO query) {
        return projectMemberDao.listEntityByQuery(query);
    }
}
