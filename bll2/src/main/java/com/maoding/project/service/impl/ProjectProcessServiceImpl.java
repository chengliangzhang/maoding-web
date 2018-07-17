package com.maoding.project.service.impl;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.maoding.conllaboration.SyncCmd;
import com.maoding.conllaboration.service.CollaborationService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.BaseShowDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.StringUtil;
import com.maoding.dynamic.dao.ZInfoDAO;
import com.maoding.dynamic.service.DynamicService;
import com.maoding.message.dto.SendMessageDTO;
import com.maoding.message.service.MessageService;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dao.ProjectProcessNodeDao;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectProcessNodeEntity;
import com.maoding.project.service.ProjectProcessService;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.projectmember.service.ProjectMemberService;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.dto.SaveProjectTaskDTO;
import com.maoding.task.dto.TaskWithFullNameDTO;
import com.maoding.task.entity.ProjectTaskEntity;
import com.maoding.task.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuwq on 2016/10/27.
 */
@Service("projectProcessService")
public class ProjectProcessServiceImpl implements ProjectProcessService {

    @Autowired
    private ProjectProcessNodeDao projectProcessNodeDao;


    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private CollaborationService collaborationService;

    @Autowired
    private ProjectTaskDao projectTaskDao;

    @Autowired
    private ZInfoDAO zInfoDAO;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    /**
     * 获取指定任务下的流程
     */
    @Override
    public AjaxMessage getProcessesByTask(String taskManageId) throws Exception {
        if (StringUtil.isNullOrEmpty(taskManageId)) {
            return AjaxMessage.failed(null).setInfo("请指定任务节点Id");
        }

        List<ProjectTaskProcessNodeDTO> list = this.projectMemberService.listDesignMember(taskManageId);
        Map<String, Object> processResult = Maps.newHashMap();
        //设置当前节点
        ProjectProcessNode designProcessNode = new ProjectProcessNode();
        designProcessNode.setNodeName("设计");
        ProjectProcessNode proofreadingProcessNode = new ProjectProcessNode();
        proofreadingProcessNode.setNodeName("校对");
        ProjectProcessNode reviewProcessNode = new ProjectProcessNode();
        reviewProcessNode.setNodeName("审核");
        if (!CollectionUtils.isEmpty(list)) {
            for (ProjectTaskProcessNodeDTO nodeDTO : list) {
                List<ProjectProcessNodeDTO> nodes = Lists.newArrayList();
                int i = 1;
                for (ProjectDesignUser user : nodeDTO.getUserList()) {
                    ProjectProcessNodeDTO node = new ProjectProcessNodeDTO();
                    node.setNodeName(nodeDTO.getNodeName());
                    node.setUserName(user.getUserName());
                    node.setCompanyUserId(user.getCompanyUserId());
                    node.setId(user.getId());
                    node.setSeq(nodeDTO.getMemberType() - 3);
                    node.setNodeSeq((i++));
                    nodes.add(node);
                }
                if (nodeDTO.getMemberType() == ProjectMemberType.PROJECT_DESIGNER) {
                    designProcessNode.setProjectProcessNodeDTOList(nodes);
                }
                if (nodeDTO.getMemberType() == ProjectMemberType.PROJECT_PROOFREADER) {
                    proofreadingProcessNode.setProjectProcessNodeDTOList(nodes);
                }
                if (nodeDTO.getMemberType() == ProjectMemberType.PROJECT_AUDITOR) {
                    reviewProcessNode.setProjectProcessNodeDTOList(nodes);
                }
            }
        }

        ProjectProcessDTO processDTO = new ProjectProcessDTO();
        processDTO.getProjectProcessNodes().add(designProcessNode);
        processDTO.getProjectProcessNodes().add(proofreadingProcessNode);
        processDTO.getProjectProcessNodes().add(reviewProcessNode);
        processResult.put("projectProcess", processDTO);
        return AjaxMessage.succeed(processResult);
    }


    /**
     * 方法描述：根据任务id删除流程
     * 作者：MaoSF
     * 日期：2017/3/3
     */
    @Override
    public AjaxMessage deleteProcessByTaskId(String taskId) throws Exception {
        //后期优化此问题
        Map<String, Object> map = new HashMap();
        map.put("taskManageId", taskId);
        List<ProjectProcessNodeEntity> list = this.projectProcessNodeDao.getProcessNodeByParam(map);
        for (ProjectProcessNodeEntity dto : list) {
            deleteProcessNodeById(dto.getId());
        }
        return AjaxMessage.succeed("删除成功");
    }

    /**
     * 方法描述：新建或更新流程
     * 作者：MaoSF
     * 日期：2017/1/4
     */
    public AjaxMessage saveOrUpdateProcess_publish(ProjectProcessDTO dto) throws Exception {
        String taskId = dto.getTaskManageId();
        ProjectTaskEntity taskEntity = this.projectTaskDao.selectById(taskId);
        if (StringUtil.isNullOrEmpty(taskId) || taskEntity == null) {
            return AjaxMessage.failed("操作失败");
        }
        if (taskEntity != null && taskEntity.getTaskType() != SystemParameters.TASK_PRODUCT_TYPE_MODIFY) {//则新增一条被修改的记录
            taskId = projectTaskService.copyProjectTask(new SaveProjectTaskDTO(), taskEntity);
        }
        List<ProjectMemberEntity> list = this.projectMemberService.listProjectMember(null, null, null, taskId);
        //流程节点
        int nodeSeq = 1;
        StringBuffer ids = new StringBuffer();
        for (ProjectProcessNodeDTO nodeDTO : dto.getNodes()) {
            ProjectMemberEntity memberEntity = this.projectMemberService.getProjectMember(nodeDTO.getCompanyUserId(), nodeDTO.getSeq() + 3, taskId);
            if (null == memberEntity) {
                memberEntity = this.projectMemberService.saveProjectMember(dto.getProjectId(), dto.getCompanyId(), nodeDTO.getCompanyUserId(), taskId, nodeDTO.getSeq() + 3, nodeSeq, dto.getAccountId(),dto.getCurrentCompanyId());
            }
            nodeSeq++;
            ids.append(memberEntity.getId() + ",");
        }
        String nodeIds = ids.toString();
        for (ProjectMemberEntity member : list) {
            if (member.getMemberType() != ProjectMemberType.PROJECT_TASK_RESPONSIBLE) {
                if (nodeIds.indexOf(member.getId()) < 0) {
                    this.projectMemberService.deleteProjectMember(member.getId());
                }
            }
        }
        this.projectTaskService.updateByTaskIdStatus(taskId, SystemParameters.TASK_STATUS_MODIFIED);
        return new AjaxMessage().setCode("0").setInfo("保存成功");
    }


    /**
     * 方法描述：新建或更新流程
     * 作者：MaoSF
     * 日期：2017/1/4
     */
    @Override
    public AjaxMessage saveOrUpdateProcess(ProjectProcessDTO dto) throws Exception {
        TaskWithFullNameDTO origin = zInfoDAO.getTaskByTaskId(dto.getTaskManageId());//保留原有数据
        String taskId = dto.getTaskManageId();
        ProjectTaskEntity taskEntity = this.projectTaskDao.selectById(taskId);
        if (StringUtil.isNullOrEmpty(taskId) || taskEntity == null) {
            return AjaxMessage.failed("操作失败");
        }
        //获取单节点设校审人员
        List<ProjectMemberEntity> list = this.projectMemberService.listProjectMember(null, null, dto.getMemberType(), taskId);
        //流程节点
        String companyId = dto.getCurrentCompanyId();
        StringBuffer ids = new StringBuffer();
        List<ProjectProcessNodeDTO> addNodes = new ArrayList<>();
        int i = 1;
        for (ProjectProcessNodeDTO nodeDTO : dto.getNodes()) {
            ProjectMemberEntity memberEntity = this.projectMemberService.getProjectMember(nodeDTO.getCompanyUserId(), dto.getMemberType(), taskId);
            if (null == memberEntity) {
                if (nodeDTO.getNodeName().indexOf("人") != -1) {
                    nodeDTO.setNodeName(nodeDTO.getNodeName().substring(0, nodeDTO.getNodeName().length() - 1));
                }
                ProjectProcessNodeEntity node = new ProjectProcessNodeEntity();
                nodeDTO.setId(StringUtil.buildUUID());
                nodeDTO.setProcessId(taskId);
                BaseDTO.copyFields(nodeDTO, node);
                node.setCreateBy(dto.getAccountId());
                node.setSeq(dto.getMemberType()-3);
                node.setNodeSeq(i);
                projectProcessNodeDao.insert(node);
                addNodes.add(nodeDTO);
            } else {
                ids.append(memberEntity.getId() + ",");
                memberEntity.setSeq(i);
                this.projectMemberService.updateProjectMember(memberEntity);
                ProjectProcessNodeEntity node = new ProjectProcessNodeEntity();
                node.setId(memberEntity.getTargetId());
                node.setNodeSeq(i);
                node.setSeq(dto.getMemberType()-3);//此处防止，原来没有设置成功，后面补上seq的值
                projectProcessNodeDao.updateById(node);
            }
            i++;
        }
        //  保存任务并推送消息
        String newIds = this.saveMyTask(addNodes, companyId, dto.getProjectId(), dto.getAccountId(), dto.getTaskManageId(), dto.getMemberType());

        String nodeIds = ids.toString() + "," + newIds;
        for (ProjectMemberEntity member : list) {
            if (member.getMemberType() != ProjectMemberType.PROJECT_TASK_RESPONSIBLE) {
                if (nodeIds.indexOf(member.getId()) < 0) {
                    deleteProcessNodeById(member.getTargetId());
                    this.projectMemberService.deleteProjectMember(member.getProjectId(), null, member.getMemberType(), member.getTargetId());
                }
            }
        }
        //添加项目动态
        TaskWithFullNameDTO target = zInfoDAO.getTaskByTaskId(dto.getTaskManageId()); //获取新任务;
        dynamicService.addDynamic(target, target, dto.getCompanyId(), dto.getAccountId());
        //通知协同
        collaborationService.pushSyncCMD_PT(taskEntity.getProjectId(), taskEntity.getTaskPath(), SyncCmd.PT2);
        //给乙方任务负责人推送消息
//        String msg = (origin != null) ? origin.getMembers() : "";
//        msg += ";";
//        msg += (target != null) ? target.getMembers() : "";
//        this.sendMessageToPartBDesigner(dto.getProjectId(), dto.getTaskManageId(), msg);

        //在项目文档中建立设计文件目录中创建相关人员目录
        //准备添加设计文件目录中的人员目录
        List<BaseShowDTO> designerList = createDesignListFrom(addNodes);
        List<ProjectTaskEntity> issueList = projectTaskDao.listIssueParentByTaskId(dto.getId());
        projectSkyDriverService.createDesignerDir(getCreateDesignerDirFirstParam(dto),issueList,designerList);

        return new AjaxMessage().setCode("0").setInfo("保存成功");
    }

    //获取要调用createDesignerDir时，存放projectId等信息的参数
    private SaveProjectTaskDTO getCreateDesignerDirFirstParam(ProjectProcessDTO dto){
        SaveProjectTaskDTO tmpDTO = new SaveProjectTaskDTO();
        tmpDTO.setProjectId(dto.getProjectId());
        tmpDTO.setCompanyId(dto.getCompanyId());
        tmpDTO.setAccountId(dto.getAccountId());
        return tmpDTO;
    }

    //获取添加人员
    private List<BaseShowDTO> createDesignListFrom(List<ProjectProcessNodeDTO> nodes){
        List<BaseShowDTO> designerList = new ArrayList<>();
        nodes.forEach(node-> designerList.add(new BaseShowDTO(node.getCompanyUserId(),node.getUserName())));
        return designerList;
    }

    /**
     * 方法描述：新建或更新流程
     * 新增设、校、审人员
     */
    @Override
    public AjaxMessage saveOrUpdateProcessNew(ProjectProcessDTO dto) {
        String taskId = dto.getTaskManageId();
        ProjectTaskEntity taskEntity = this.projectTaskDao.selectById(taskId);
        if (StringUtil.isNullOrEmpty(taskId) || taskEntity == null) {
            return AjaxMessage.failed("操作失败");
        }
        try {
            List<ProjectProcessNodeDTO> addNodes = new ArrayList<ProjectProcessNodeDTO>();
            for (ProjectProcessNodeDTO nodeDTO : dto.getNodes()) {
                ProjectMemberEntity memberEntity = this.projectMemberService.getProjectMember(dto.getCompanyUserId(), dto.getSeq() + 3, taskId);
                if (null == memberEntity) {
                    ProjectProcessNodeEntity node = new ProjectProcessNodeEntity();
                    nodeDTO.setId(StringUtil.buildUUID());
                    nodeDTO.setProcessId(taskId);
                    BaseDTO.copyFields(nodeDTO, node);
                    node.setCreateBy(dto.getAccountId());
                    projectProcessNodeDao.insert(node);
                    addNodes.add(nodeDTO);
                }
            }
            // 保存任务并推送消息
//            String newIds = this.saveMyTask(addNodes, dto.getCurrentCompanyId(), dto.getProjectId(), dto.getAccountId(), dto.getTaskManageId());
            //通知协同
            collaborationService.pushSyncCMD_PT(taskEntity.getProjectId(), taskEntity.getTaskPath(), SyncCmd.PT2);
        } catch (Exception exception) {
            return AjaxMessage.failed("操作失败");
        }
        return new AjaxMessage().setCode("0").setInfo("保存成功");
    }

    private void sendMessageToPartBDesigner(String projectId, String taskId, String msgContent) throws Exception {

        ProjectEntity projectEntity = this.projectDao.selectById(projectId);
        if (projectEntity != null) {
            if (!StringUtil.isNullOrEmpty(projectEntity.getCompanyBid()) && !projectEntity.getCompanyBid().equals(projectEntity.getCompanyId())) {

                ProjectMemberEntity designManager = this.projectMemberService.getProjectMember(projectId, projectEntity.getCompanyBid(), ProjectMemberType.PROJECT_TASK_RESPONSIBLE, taskId);
                if (designManager != null) {
                    this.messageService.sendMessage(projectId, projectEntity.getCompanyBid(), taskId, SystemParameters.MESSAGE_TYPE_8, null, designManager.getAccountId(), null, msgContent);
                }
            }
        }
    }

    private String saveMyTask(List<ProjectProcessNodeDTO> addNodes, String companyId, String projectId, String accountId, String taskId, Integer memberType) throws Exception {
        String ids = "";
        CompanyUserEntity companyUser = this.companyUserDao.getCompanyUserByUserIdAndCompanyId(accountId, companyId);
        for (ProjectProcessNodeDTO node : addNodes) {
            boolean isSendMessage = true;
            if (companyUser != null && companyUser.getId().equals(node.getCompanyUserId())) {
                isSendMessage = false;
            }else {
                companyUser = this.companyUserDao.selectById(node.getCompanyUserId());
            }
            ProjectMemberEntity m = this.projectMemberService.saveProjectMember(projectId, companyId, node.getCompanyUserId(), companyUser.getUserId(), memberType, node.getId(), taskId, node.getSeq(), accountId, false,companyId);
            if(isSendMessage){
                this.messageService.sendMessageForDesigner(new SendMessageDTO(projectId,companyId,companyUser.getUserId(),accountId,companyId,SystemParameters.MESSAGE_TYPE_501,SystemParameters.MESSAGE_TYPE_502,taskId,taskId,node.getId(),node.getNodeName()));
            }
            ids += m.getId();
        }
        return ids;
    }

    private void deleteProjectProcessNode(String projectId, ProjectProcessNodeEntity nodeEntity) throws Exception {
        //删除
        projectProcessNodeDao.deleteById(nodeEntity.getId());
        //忽略任务
        myTaskService.ignoreMyTask(nodeEntity.getId());

        this.projectMemberService.deleteProjectMember(projectId, null, (3 + nodeEntity.getSeq()), nodeEntity.getId());
    }

    /**
     * 方法描述：处理流程完成
     * 作者：MaoSF
     * 日期：2017/3/12
     */
    @Override
    public AjaxMessage completeProjectProcessNode(String projectId, String companyId, String nodeId, String taskId, String accountId) throws Exception {
        ProjectProcessNodeEntity originNode = projectProcessNodeDao.selectById(nodeId); //保留原有数据
        if (originNode == null) {
            return AjaxMessage.failed("操作失败");
        }
        int i = this.projectProcessNodeDao.updateProcessNodeComplete(nodeId);
        ProjectProcessNodeEntity targetNode = projectProcessNodeDao.selectById(nodeId); //创建修改后数据
        //保存项目动态
        dynamicService.addDynamic(originNode, targetNode, companyId, accountId);
        //通知协同
        ProjectTaskEntity pTask = projectTaskDao.selectById(taskId);
        //如果是否是设计任务，如果是设计任务，当前设计人的所有的设计任务已经完成，父任务的设计人员默认完成
        //如果是生产任务，并且是设计完成，则默认下面所有当前人的设计分解的任务已经完成
        if(originNode.getSeq()==1){
            if(pTask.getTaskType() == SystemParameters.TASK_DESIGN_TYPE){
                //查询是否还有未完成的设计分解任务，如果没有，则父任务的设计任务确定完成
                if(CollectionUtils.isEmpty(projectProcessNodeDao.selectNodeByTaskId(pTask.getTaskPid(),originNode.getCompanyUserId()))){
                    this.projectProcessNodeDao.updateCompleteForParentTask(taskId,originNode.getCompanyUserId());
                }
            }else {
                //把所有当前的设计分解任务指定完成
                projectProcessNodeDao.updateProcessNodeCompleteForDesignTask(taskId,originNode.getCompanyUserId());
            }
        }
        //通知协同
        this.collaborationService.pushSyncCMD_PT(pTask.getProjectId(), pTask.getTaskPath(), SyncCmd.PT2);
        //推送设计--》校对--》审核
       this.sendMessage(projectId,companyId,nodeId,taskId,accountId,originNode.getSeq());

        if (i > 0) {
            return AjaxMessage.succeed("操作成功");
        }
        return AjaxMessage.failed("操作失败");
    }

    /**
     * 方法描述：保存流程节点
     * 作者：MaoSF
     * 日期：2017/6/22
     */
    @Override
    public String saveProjectProcessNode(String projectId, int seq, int nodeSeq, String taskId, String companyUserId) throws Exception {
        int seq1 = seq - 3;//因为对应memberType的值减3
        ProjectProcessNodeEntity node = new ProjectProcessNodeEntity();
        node.setId(StringUtil.buildUUID());
        node.setNodeSeq(nodeSeq);
        node.setSeq(seq1);
        node.setStatus(0);
        node.setNodeName(seq1 == 1 ? "设计" : seq1 == 2 ? "校对" : "审核");
        node.setProcessId(taskId);
        node.setCompanyUserId(companyUserId);
        this.projectProcessNodeDao.insert(node);
        return node.getId();
    }

    /**
     * 方法描述：删除节点
     * 作者：MaoSF
     * 日期：2017/6/22
     */
    @Override
    public void deleteProcessNodeById(String id) throws Exception {
        //删除
        projectProcessNodeDao.deleteById(id);
        this.myTaskService.ignoreMyTask(id);//忽略任务

    }

    @Override
    public void saveProjectProcessForNewTask(ProjectTaskEntity task, List<String> companyUserList, int memberType, String currentCompanyUserId) throws Exception {
        //保存流程id
        int i = 1;
        for (String companyUserId : companyUserList) {
            CompanyUserEntity u = this.companyUserDao.selectById(companyUserId);
            String nodeId = this.saveProjectProcessNode(null, memberType, i, task.getId(), companyUserId);
            this.projectMemberService.saveProjectMember(task.getProjectId(), task.getCompanyId(),
                    companyUserId, u.getUserId(), memberType, nodeId, task.getId(), i, task.getCreateBy(), false, task.getCompanyId());
            i++;
            if(!currentCompanyUserId.equals(companyUserId)){
                this.messageService.sendMessageForDesigner(new SendMessageDTO(task.getProjectId(), task.getCompanyId(),u.getUserId(),task.getCreateBy(),task.getCompanyId(),SystemParameters.MESSAGE_TYPE_501,SystemParameters.MESSAGE_TYPE_502,task.getId(),task.getId(),nodeId));
            }
        }
    }

    @Override
    public void saveProjectProcessForDesignTask(ProjectTaskEntity task, int memberType, String currentCompanyUserId) throws Exception {
        String nodeId = this.saveProjectProcessNode(null, memberType, 1, task.getId(), currentCompanyUserId);
        projectMemberService.insertProjectMember(task.getProjectId(),task.getCompanyId(),currentCompanyUserId,null,memberType,nodeId,task.getId(),1,task.getCreateBy());
    }

    private void sendMessage(String projectId, String companyId, String nodeId, String taskId, String accountId,int seq) throws Exception{
        //推送设计--》校对--》审核
        ProjectDesignUserList members = null;
        if(seq==1) {//设计
            members = projectMemberService.listDesignMemberList(taskId);
            if (!CollectionUtils.isEmpty(members.getCheckUser().getUserList())) {
                for(ProjectDesignUser u :members.getCheckUser().getUserList()){
                    this.messageService.sendMessageForProcess(new SendMessageDTO(projectId,companyId,u.getAccountId(),accountId,companyId,SystemParameters.MESSAGE_TYPE_503,taskId,members.getCheckUser().getTargetId(),"设计","校对"));
                }

            }else if(!CollectionUtils.isEmpty(members.getExamineUser().getUserList())){
                for(ProjectDesignUser u :members.getExamineUser().getUserList()){
                    this.messageService.sendMessageForProcess(new SendMessageDTO(projectId,companyId,u.getAccountId(),accountId,companyId,SystemParameters.MESSAGE_TYPE_503,taskId,members.getExamineUser().getTargetId(),"设计","审核"));
                }
            }else {
                //任务负责人
                ProjectMemberEntity u = this.projectMemberService.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_TASK_RESPONSIBLE, taskId);
                if(u!=null){
                    this.messageService.sendMessageForProcess(new SendMessageDTO(projectId,companyId,u.getAccountId(),accountId,companyId,SystemParameters.MESSAGE_TYPE_405,taskId,nodeId,"设计",null));
                }
            }
        }
        if(seq==2){
            members = projectMemberService.listDesignMemberList(taskId);
            if (!CollectionUtils.isEmpty(members.getCheckUser().getUserList())) {
                for(ProjectDesignUser u :members.getExamineUser().getUserList()){
                    this.messageService.sendMessageForProcess(new SendMessageDTO(projectId,companyId,u.getAccountId(),accountId,companyId,SystemParameters.MESSAGE_TYPE_503,taskId,members.getExamineUser().getTargetId(),"校对","审核"));
                }
            }else {
                //任务负责人
                ProjectMemberEntity u = this.projectMemberService.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_TASK_RESPONSIBLE, taskId);
                if(u!=null){
                    this.messageService.sendMessageForProcess(new SendMessageDTO(projectId,companyId,u.getAccountId(),accountId,companyId,SystemParameters.MESSAGE_TYPE_405,taskId,nodeId,"校对",null));
                }
            }
        }
        if(seq==3){
            //给任务负责人
            //任务负责人
            ProjectMemberEntity u = this.projectMemberService.getProjectMember(projectId, companyId, ProjectMemberType.PROJECT_TASK_RESPONSIBLE, taskId);
            if(u!=null){
                this.messageService.sendMessageForProcess(new SendMessageDTO(projectId,companyId,u.getAccountId(),accountId,companyId,SystemParameters.MESSAGE_TYPE_405,taskId,nodeId,"审核",null));
            }
        }
    }
}
