package com.maoding.task.service.impl;

import com.beust.jcommander.internal.Lists;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.BeanUtilsEx;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.StringUtils;
import com.maoding.dynamic.dao.ZInfoDAO;
import com.maoding.dynamic.service.DynamicService;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.projectmember.dao.ProjectMemberDao;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.projectmember.service.ProjectMemberService;
import com.maoding.task.dao.ProjectProcessTimeDao;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.dto.ProjectProcessTimeDTO;
import com.maoding.task.dto.SaveProjectTaskDTO;
import com.maoding.task.dto.TaskWithFullNameDTO;
import com.maoding.task.entity.ProjectProcessTimeEntity;
import com.maoding.task.entity.ProjectTaskEntity;
import com.maoding.task.service.ProjectProcessTimeService;
import com.maoding.task.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjecManagerServiceImpl
 * 类描述：项目任务签发关系
 * 作    者：MaoSF
 * 日    期：2016年12月28日-下午5:28:54
 */
@Service("projectProcessTimeService")
public class ProjectProcessTimeServiceImpl extends GenericService<ProjectProcessTimeEntity>  implements ProjectProcessTimeService {

    @Autowired
    private ProjectProcessTimeDao projectProcessTimeDao;

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private ZInfoDAO zInfoDAO;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ProjectTaskDao projectTaskDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectMemberDao projectMemberDao;

    /**
     * 方法描述：保存变更信息
     * 作        者：ChenZJ
     * 日        期：2016年7月21日-上午11:28:56
     */
    @Override
    public AjaxMessage saveOrUpdateProjectProcessTime(ProjectProcessTimeDTO processTimeDTO)throws Exception{
        String msg = "";
        String taskId = processTimeDTO.getTargetId();
        String companyId = null;//需要推送的组织
        //保存原有任务
        TaskWithFullNameDTO origin = zInfoDAO.getTaskByTaskId(taskId);
        if ((origin != null) && (origin.getId() == null)) origin = null;

        //修改任务时间
        ProjectTaskEntity entity = projectTaskDao.selectById(taskId);
        ProjectEntity project = projectDao.selectById(entity.getProjectId());
        if (entity == null || project == null) return AjaxMessage.failed("数据错误");
        String startTime = entity.getStartTime()==null?"未设置": DateUtils.date2Str(entity.getStartTime(),DateUtils.date_sdf2);
        String endTime = entity.getEndTime()==null?"未设置": DateUtils.date2Str(entity.getEndTime(),DateUtils.date_sdf2);
        String startTime2 = processTimeDTO.getStartTime()==null?"未设置": DateUtils.date2Str(processTimeDTO.getStartTime(),DateUtils.date_sdf2);
        String endTime2 = processTimeDTO.getEndTime()==null?"未设置": DateUtils.date2Str(processTimeDTO.getEndTime(),DateUtils.date_sdf2);
        msg+=project.getProjectName()+" ;"+entity.getTaskName()+" ;"+startTime+" ;"+endTime+" ;" +startTime2+" ;"+endTime2;
        List<ProjectTaskEntity> testList = null;
        if(entity.getTaskType() == SystemParameters.TASK_TYPE_PHASE || entity.getTaskType() == SystemParameters.TASK_TYPE_ISSUE){
            //找到当前记录的测试版本
            testList = projectTaskDao.getTaskByBeModifyId(taskId,processTimeDTO.getCompanyId());
            companyId = entity.getCompanyId();
            if(!CollectionUtils.isEmpty(testList)){//说明有测试版本
                ProjectTaskEntity  testData =testList.get(0);
                testData.setStartTime(getNotNullDate(processTimeDTO.getStartTime()));
                testData.setEndTime(getNotNullDate(processTimeDTO.getEndTime()));
                projectTaskDao.updateByTaskId(testData);
                // 保存变更记录
                saveChangeTime(processTimeDTO,testData.getId());
                companyId = testData.getCompanyId();
            }
        }else {
            if(StringUtil.isNullOrEmpty(entity.getBeModifyId()) && SystemParameters.TASK_STATUS_MODIFIED.equals(entity.getTaskStatus()) ){//如果是未发布版本
                //查询最后一条记录
                Map<String,Object> map = new HashMap<>();
                map.put("targetId",taskId);
                ProjectProcessTimeEntity timeEntity = this.projectProcessTimeDao.getProjectProcessTimeByTargetId(map);
                if(timeEntity!=null){
                    //此处，如果是未发布的数据，更新时间的时候，值做更新处理，不做变更
                    processTimeDTO.setId(timeEntity.getId());//设置id，后面做更新处理
                }
            }
            if(!StringUtil.isNullOrEmpty(entity.getBeModifyId())){ // 如果是草稿数据,则查询正式库是否直接是签发给自己的。如果是给自己的，则更改时间processTimeDTO.getCompanyId()传递的是当前公司
                ProjectTaskEntity formalData = projectTaskDao.selectById(entity.getBeModifyId());
                if(formalData!=null && formalData.getCompanyId().equals(processTimeDTO.getCompanyId())){
                    formalData.setStartTime(getNotNullDate(processTimeDTO.getStartTime()));
                    formalData.setEndTime(getNotNullDate(processTimeDTO.getEndTime()));
                    projectTaskDao.updateByTaskId(formalData);
                    //保存变更记录
                    saveChangeTime(processTimeDTO,formalData.getId());
                    companyId = entity.getCompanyId();
                }
            }
        }
        //此处包含了（测试版本的数据，生产数据，签发测试数据并且是自己生产的数据）
        if(CollectionUtils.isEmpty(testList) || entity.getCompanyId().equals(processTimeDTO.getCompanyId())){
            entity.setStartTime(getNotNullDate(processTimeDTO.getStartTime()));
            entity.setEndTime(getNotNullDate(processTimeDTO.getEndTime()));
            projectTaskDao.updateByTaskId(entity);
        }
        //保存变更记录
        ProjectProcessTimeEntity projectProcessTimeEntity = saveChangeTime(processTimeDTO,taskId);
        //如果是阶段或生产任务，更改时间时直接发送消息，签发任务在发布时发送消息
        TaskWithFullNameDTO targetEx = (origin != null) ? ((TaskWithFullNameDTO) origin.clone()) : zInfoDAO.getTaskByTask(entity);
        targetEx.setTaskPeriod(zInfoDAO.getPeriodByTime(projectProcessTimeEntity));

        //添加项目动态
        TaskWithFullNameDTO target;
        if (origin != null){
            target = new TaskWithFullNameDTO();
            BeanUtilsEx.copyProperties(origin,target);
            target.setTaskPeriod(zInfoDAO.getPeriodByTime(projectProcessTimeEntity));
        } else {
            target = zInfoDAO.getTaskByTime(projectProcessTimeEntity);
            if ((target != null) && (target.getId() == null)) target = null;
        }
        dynamicService.addDynamic(origin,target,processTimeDTO.getCompanyId(),processTimeDTO.getAccountId());
        //推送任务
        if(entity.getTaskType() == SystemParameters.TASK_TYPE_PRODUCT){
            //通知下面设校审及直系子任务的任务负责人
            //hi，任务负责人/设计负责人XXX变更了“卯丁科技大厦一期-方案设计”的计划进度〖由2017-05-09至2017-09-09变更为2017-06-09至2017-10-09），请查看并作相应调整，谢谢！
            this.sendMessage(entity.getProjectId(), entity.getId(), entity.getCompanyId(),processTimeDTO.getAccountId(),msg);
        }else {
            if(!StringUtil.isNullOrEmpty(companyId)){
                //通知设计负责人
               this.sendMessage(entity.getProjectId(), entity.getId(), entity.getCompanyId(),processTimeDTO.getAccountId(),msg,processTimeDTO.getCurrentCompanyId());
            }
        }
        return  new AjaxMessage().setCode("0").setInfo("数据保存成功");
    }


    @Override
    public ProjectProcessTimeEntity saveChangeTime(ProjectProcessTimeDTO processTimeDTO,String taskId) throws Exception{
        ProjectProcessTimeEntity projectProcessTimeEntity=new ProjectProcessTimeEntity();
        BaseDTO.copyFields(processTimeDTO,projectProcessTimeEntity);
        projectProcessTimeEntity.setTargetId(taskId);
        if(StringUtil.isNullOrEmpty(processTimeDTO.getId())){
            projectProcessTimeEntity.setId(StringUtil.buildUUID());
            projectProcessTimeEntity.set4Base(processTimeDTO.getAccountId(),null,new Date(),null);
            projectProcessTimeEntity.setType(SystemParameters.PROCESS_TYPE_PLAN);
            projectProcessTimeDao.insert(projectProcessTimeEntity);
        } else {
            projectProcessTimeEntity.setUpdateBy(processTimeDTO.getAccountId());
            projectProcessTimeEntity.setStartTime(getNotNullDate(processTimeDTO.getStartTime()));
            projectProcessTimeEntity.setEndTime(getNotNullDate(processTimeDTO.getEndTime()));
            projectProcessTimeDao.updateById(projectProcessTimeEntity);
        }
        return projectProcessTimeEntity;
    }

    private Date getNotNullDate(Date d){
        return (d == null) ? new Date(0) : d;
    }

    /**
     * 方法描述：保存变更信息
     * 作        者：ChenZJ
     * 日        期：2016年7月21日-上午11:28:56
     */
  //  @Override
    public AjaxMessage saveOrUpdateProjectProcessTime_publish(ProjectProcessTimeDTO processTimeDTO)throws Exception{
        String taskId = processTimeDTO.getTargetId();
        ProjectTaskEntity taskEntity = this.projectTaskDao.selectById(taskId);
        if(taskEntity==null){
            return AjaxMessage.failed("操作失败");
        }

        //把任务改为未发布状态(生产的时间修改经过此方法)
        if(taskEntity.getTaskType()!=SystemParameters.PROCESS_TYPE_CONTACT){
            if(taskEntity.getTaskType()!= SystemParameters.TASK_TYPE_MODIFY && taskEntity.getTaskType()!= SystemParameters.TASK_PRODUCT_TYPE_MODIFY){//则新增一条被修改的记录
                taskId = projectTaskService.copyProjectTask(new SaveProjectTaskDTO(),taskEntity);
            }
            taskEntity.setId(taskId);
            taskEntity.setTaskStatus(SystemParameters.TASK_STATUS_MODIFIED);
        }

        taskEntity.setStartTime(processTimeDTO.getStartTime());
        taskEntity.setEndTime(processTimeDTO.getEndTime());
        this.projectTaskService.updateById(taskEntity);

        //保存原有任务
        TaskWithFullNameDTO origin = zInfoDAO.getTaskByTaskId(taskId);
        if ((origin != null) && (origin.getId() == null)) origin = null;
        ProjectProcessTimeEntity projectProcessTimeEntity=new ProjectProcessTimeEntity();
        BaseDTO.copyFields(processTimeDTO,projectProcessTimeEntity);
        if(StringUtil.isNullOrEmpty(processTimeDTO.getId())){
            projectProcessTimeEntity.setId(StringUtil.buildUUID());
            projectProcessTimeEntity.set4Base(processTimeDTO.getAccountId(),null,new Date(),null);
            projectProcessTimeEntity.setTargetId(taskId);
            projectProcessTimeEntity.setType(taskEntity.getTaskType()==SystemParameters.PROCESS_TYPE_CONTACT?SystemParameters.PROCESS_TYPE_PLAN:SystemParameters.PROCESS_TYPE_NOT_PUBLISH);
            projectProcessTimeDao.insert(projectProcessTimeEntity);
        } else {
            projectProcessTimeEntity.setUpdateBy(processTimeDTO.getAccountId());
            projectProcessTimeDao.updateById(projectProcessTimeEntity);
        }

        //添加项目动态
        TaskWithFullNameDTO target;
        if (origin != null){
            target = new TaskWithFullNameDTO();
            BeanUtilsEx.copyProperties(origin,target);
            target.setTaskPeriod(zInfoDAO.getPeriodByTime(projectProcessTimeEntity));
        } else {
            target = zInfoDAO.getTaskByTime(projectProcessTimeEntity);
            if ((target != null) && (target.getId() == null)) target = null;
        }
        dynamicService.addDynamic(origin,target,processTimeDTO.getCompanyId(),processTimeDTO.getAccountId());

        return  new AjaxMessage().setCode("0").setInfo("数据保存成功");
    }

    /**
     * 方法描述：根据tagetId获取变更信息
     * 作        者：TangY
     * 日        期：2016年7月21日-上午11:28:56
     */
    @Override
    public List<ProjectProcessTimeDTO> getProjectProcessTimeList(Map<String,Object> param)throws Exception{
        List<ProjectProcessTimeDTO> dtoList = projectProcessTimeDao.listTimeHistory(param);
        if (dtoList != null) {
            for (int i=dtoList.size()-1; i>=0; i--) {
                ProjectProcessTimeDTO current = dtoList.get(i);
                ProjectProcessTimeDTO last = (i > 0) ? dtoList.get(i-1) : null;
                current.setHistoryText(getHistoryText(current,last));
            }
        }
        return  dtoList;
    }

    private String getHistoryText(ProjectProcessTimeDTO current,ProjectProcessTimeDTO last) {
        String changeTimeStr = DateUtils.date2Str(current.getChangedTime(),DateUtils.MD_DAY_H_M);
        String userName = StringUtils.isEmpty(current.getUserName()) ? "" : "【" + current.getUserName() + "】";
        String lastTimeStr = getTimeText(last);
        String currentTimeStr = getTimeText(current);
        return changeTimeStr + " " + userName + " 将进度计划从 " + lastTimeStr + " 调整为 " + currentTimeStr;
    }

    private String getTimeText(ProjectProcessTimeDTO dto){
        if ((dto == null) || ((dto.getStartTime() == null) && (dto.getEndTime() == null))) {
            return "<无>";
        } else {
            String startTimeText = (dto.getStartTime() == null) ? "<无开始时间>" : DateUtils.date2Str(dto.getStartTime(),DateUtils.date_sdf2);
            String endTimeText = (dto.getEndTime() == null) ? "<无结束时间>" : DateUtils.date2Str(dto.getEndTime(),DateUtils.date_sdf2);
            return startTimeText + "～" + endTimeText;
        }
    }

    /**
     * 方法描述：根据tagetId获取变更信息
     * 作        者：TangY
     * 日        期：2016年7月21日-上午11:28:56
     */
    @Override
    public int delProjectProcessTime(String id)throws Exception{
        return  projectProcessTimeDao.deleteById(id);
    }
    private void sendMessage(String projectId,String taskId,String companyId,String accountId,String content) throws Exception{
        ProjectMemberEntity designer = null;
        //设计人员+任务负责人
        List<ProjectMemberEntity> list = this.projectMemberDao.listProjectMemberByTaskId(taskId);
        List<ProjectMemberEntity> designList = this.projectMemberDao.listDesignerMemberByTaskPid(taskId);
        List<String> userList = Lists.newArrayList();
        for(ProjectMemberEntity u:list){
            if(u.getMemberType() == ProjectMemberType.PROJECT_TASK_RESPONSIBLE){
                designer = u;
                continue;
            }
            if(!u.getAccountId().equals(accountId)){
                if(!userList.contains(u.getAccountId())){
                    userList.add(u.getAccountId());
                }
            }
        }
        for(ProjectMemberEntity u:designList){
            if(!u.getAccountId().equals(accountId)){
                if(!userList.contains(u.getAccountId())){
                    userList.add(u.getAccountId());
                }
            }
        }
        Integer messageType = null;
        if(designer!=null && designer.getAccountId().equals(accountId)){
            //任务负责人推送的信息
            messageType = SystemParameters.MESSAGE_TYPE_602;
        }else {
            //设计负责人推送的信息
            messageType = SystemParameters.MESSAGE_TYPE_601;
        }
        for(String userId:userList){
            MessageEntity m = new MessageEntity();
            m.setTargetId(taskId);
            m.setProjectId(projectId);
            m.setCompanyId(companyId);
            m.setUserId(userId);
            m.setSendCompanyId(companyId);
            m.setCreateBy(accountId);
            m.setMessageContent(content);
            m.setMessageType(messageType);
            messageService.sendMessage(m);
        }
    }


    /**
     * 经营负责人变更时间，给设计负责人通知信息
     */
    private void sendMessage(String projectId,String taskId,String companyId,String accountId,String content,String currentCompanyId) throws Exception{
       ProjectMemberEntity member = null;
       Integer messageType = null;
        if(companyId.equals(currentCompanyId)){
             member = this.projectMemberService.getTaskDesigner(taskId);
             messageType = SystemParameters.MESSAGE_TYPE_603;
        }else {
            member = this.projectMemberService.getOperatorManager(projectId,companyId);
            messageType = SystemParameters.MESSAGE_TYPE_604;
        }
        if(member!=null){
            MessageEntity m = new MessageEntity();
            m.setTargetId(taskId);
            m.setProjectId(projectId);
            m.setCompanyId(companyId);
            m.setUserId(member.getAccountId());
            m.setSendCompanyId(currentCompanyId);
            m.setCreateBy(accountId);
            m.setMessageContent(content);
            m.setMessageType(messageType);
            messageService.sendMessage(m);
        }

    }
}
