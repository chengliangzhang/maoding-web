package com.maoding.task.dto;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.project.dto.ProjectTaskProcessNodeDTO;
import com.maoding.project.dto.ProjectTaskResponsibleDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Idccapp21 on 2016/12/29.
 */
public class ProjectTaskDTO extends BaseDTO {
    /**
     * 公司Id
     */
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 项目Id
     */
    private String projectId;

    /**
     * 任务名称
     */
    private String taskName;


    /**
     * 任务父Id
     */
    private String taskPid;


    /**
     * 任务类型
     */
    private int taskType;

    /**
     * 正式记录的类型
     */
    private int beModifyTaskType;

    /**
     * 签发次数级别
     */
    private int taskLevel;

    /**
     * 任务状态 0生效，1不生效
     */
    private String taskStatus;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 备注
     */
    private String taskRemark;
    /**
     * 排序
     */
    private int seq;
    /**
     * 任务完整路径id-id
     */
    private String taskPath;

    /**
     * 父任务的层级
     */
    private int parentLevel;
    /**
     * 生产任务的层级
     */
    private int produceTaskLevel;

    private int isOperaterTask;

    private String parentTaskPath;

    /**
     * 设计人
     */
    private String designer;

    /**
     * 根节点下所有的任务关系
     */
    private  int relationCount;

    private int isHasChild;

    /**
     * 未完成的任务数
     */
    private int notCompleteCount;

    private String orgId;

    private String departName;

    private int allDay;

    private int isRootTask;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date completeDate;

    private String beModifyId;

    private String beModifyTaskPid;

    public List<ProjectTaskDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ProjectTaskDTO> children) {
        this.children = children;
    }

    /**
     * 子节点
     */
    private List<ProjectTaskDTO> children=new ArrayList<ProjectTaskDTO>();

    /**
     * 计划剩余天数
     */
    private int planDays;


    private Date updateDate;
    /**
     * 计划状态(0,未开始1,未完成，2，完成，3，超时，4，超时完成)
     */
    private String planStatus;

    public int getPlanDays() {
        return planDays;
    }

    public void setPlanDays(int planDays) {
        this.planDays = planDays;
    }

    public int getAppointmentDays() {
        return appointmentDays;
    }

    public void setAppointmentDays(int appointmentDays) {
        this.appointmentDays = appointmentDays;
    }

    /**
     * 约定剩余天数
     */
    private int appointmentDays;

    /**
     * 约定状态(0,未开始1,未完成，2，完成，3，超时，4，超时完成)
     */
    private String appointmentStatus;

    public List<ProjectTaskResponsibleDTO> getProjectTaskResponsibleDTOList() {
        return projectTaskResponsibleDTOList;
    }


    /**
     * 计划开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date planStartTime;

    /**
     * 计划结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date  planEndTime;

    /**
     * 约定开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date  appointmentStartTime;

    /**
     * 约定结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date  appointmentEndTime;


    /**
     * 发包方
     */
    private String toCompanyName;

    /**
     * 接包方
     */
    private String fromCompanyName;

    /**
     * 发包方
     */
    private String toCompany;

    /**
     * 接包方
     */
    private String fromCompany;

    /**
     * 负责人列表

     */
    private List<ProjectTaskResponsibleDTO> projectTaskResponsibleDTOList=new ArrayList<ProjectTaskResponsibleDTO>();

    private String personInCharge;//负责人

    /**
     * 状态(1:正常进行，2：超时进行，3：正常完成，4.超时完成,5:未开始，10,无状态)
     */
    private int taskState;

    private List<ProjectTaskProcessNodeDTO> designersList= Lists.newArrayList();

    public void setProjectTaskResponsibleDTOList(List<ProjectTaskResponsibleDTO> projectTaskResponsibleDTOList) {
        this.projectTaskResponsibleDTOList = projectTaskResponsibleDTOList;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getToCompany() {
        return toCompany;
    }

    public void setToCompany(String toCompany) {
        this.toCompany = toCompany;
    }

    public String getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(String fromCompany) {
        this.fromCompany = fromCompany;
    }
    public String getToCompanyName() {
        return toCompanyName;
    }

    public void setToCompanyName(String toCompanyName) {
        this.toCompanyName = toCompanyName;
    }

    public String getFromCompanyName() {
        return fromCompanyName;
    }

    public void setFromCompanyName(String fromCompanyName) {
        this.fromCompanyName = fromCompanyName;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public Date getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(Date appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public Date getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(Date appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public String getTaskPid() {
        return taskPid;
    }

    public void setTaskPid(String taskPid) {
        this.taskPid = taskPid == null ? null : taskPid.trim();
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(int taskLevel) {
        this.taskLevel = taskLevel;
    }

    public int getProduceTaskLevel() {
        return produceTaskLevel;
    }

    public void setProduceTaskLevel(int produceTaskLevel) {
        this.produceTaskLevel = produceTaskLevel;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus == null ? null : taskStatus.trim();
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark == null ? null : taskRemark.trim();
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }


    public String getTaskPath() {
        return taskPath;
    }

    public void setTaskPath(String taskPath) {
        this.taskPath = taskPath == null ? null : taskPath.trim();
    }

    public int getParentLevel() {
        return parentLevel;
    }

    public void setParentLevel(int parentLevel) {
        this.parentLevel = parentLevel;
    }

    public String getParentTaskPath() {
        return parentTaskPath;
    }

    public void setParentTaskPath(String parentTaskPath) {
        this.parentTaskPath = parentTaskPath;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getRelationCount() {
        return relationCount;
    }

    public void setRelationCount(int relationCount) {
        this.relationCount = relationCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getIsOperaterTask() {
        return isOperaterTask;
    }

    public void setIsOperaterTask(int isOperaterTask) {
        this.isOperaterTask = isOperaterTask;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public int getIsHasChild() {
        return isHasChild;
    }

    public void setIsHasChild(int isHasChild) {
        this.isHasChild = isHasChild;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public int getAllDay() {
        if(!StringUtil.isNullOrEmpty(this.planStartTime) && !StringUtil.isNullOrEmpty(this.planEndTime)){
            allDay = DateUtils.daysOfTwo(DateUtils.date_sdf.format(planEndTime),DateUtils.date_sdf.format(planStartTime))+1;
        }
        return allDay;
    }

    public void setAllDay(int allDay) {
        this.allDay = allDay;
    }

    public int getNotCompleteCount() {
        return notCompleteCount;
    }

    public void setNotCompleteCount(int notCompleteCount) {
        this.notCompleteCount = notCompleteCount;
    }

    public int getIsRootTask() {
        return isRootTask;
    }

    public void setIsRootTask(int isRootTask) {
        this.isRootTask = isRootTask;
    }

    public List<ProjectTaskProcessNodeDTO> getDesignersList() {
        return designersList;
    }

    public void setDesignersList(List<ProjectTaskProcessNodeDTO> designersList) {
        this.designersList = designersList;
    }

    public String getBeModifyId() {
        return beModifyId;
    }

    public void setBeModifyId(String beModifyId) {
        this.beModifyId = beModifyId;
    }

    public String getBeModifyTaskPid() {
        return beModifyTaskPid;
    }

    public void setBeModifyTaskPid(String beModifyTaskPid) {
        this.beModifyTaskPid = beModifyTaskPid;
    }

    public int getBeModifyTaskType() {
        return beModifyTaskType;
    }

    public void setBeModifyTaskType(int beModifyTaskType) {
        this.beModifyTaskType = beModifyTaskType;
    }
}
