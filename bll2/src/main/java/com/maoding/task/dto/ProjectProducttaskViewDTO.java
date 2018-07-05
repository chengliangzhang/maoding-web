package com.maoding.task.dto;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.project.dto.ProjectDesignUser;
import com.maoding.project.dto.ProjectTaskProcessNodeDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectProducttaskViewDTO implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 项目id
     */
    private String projectId;
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务描述
     */
    private String taskRemark;
    /**
     * 状态(1:正常进行，2：超时进行，3：正常完成，4.超时完成,5.未开始，10,无状态)
     */
    private int taskState;

    /**
     * 剩余多少天
     */
    private String stateHtml;

    /**
     * task_pid
     * */
    private String taskPid;

    /**
     * 任务状态(0生效，1不生效,2:未发布)
     */
    private String taskStatus;
    /**
     * 任务类型（0：生产，1：设计阶段，2：签发并发布，3：未发布的签发数据）
     */
    private int taskType;
    /**
     * 设计内容的开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date planStartTime;

    /**
     * 设计内容的结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date planEndTime;
    /**
     * 设计组织id
     */
    private String companyId;

    /**
     * 设计组织名
     */
    private String companyName;

    /**
     * 任务负责人
     * */
    private String personInChargeName;


    /**
     * 设计人
     */
    private String designUserName;

    /**
     * 校对人
     */
    private String checkUserName;

    /**
     * 校对人
     */
    private String examineUserName;

    /**
     * 设计人员列表
     */
    private List<ProjectDesignUser> designersList= Lists.newArrayList();
    /**
     * 设计人
     */
    private ProjectTaskProcessNodeDTO designUser = new ProjectTaskProcessNodeDTO("设计人", ProjectMemberType.PROJECT_DESIGNER);
    /**
     * 校对人
     */
    private ProjectTaskProcessNodeDTO checkUser = new ProjectTaskProcessNodeDTO("校对人", ProjectMemberType.PROJECT_PROOFREADER);
    /**
     * 审核人
     */
    private ProjectTaskProcessNodeDTO examineUser = new ProjectTaskProcessNodeDTO("审核人", ProjectMemberType.PROJECT_AUDITOR);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public String getTaskPid() {
        return taskPid;
    }

    public void setTaskPid(String taskPid) {
        this.taskPid = taskPid;
    }

    public String getPersonInChargeName() {
        return personInChargeName;
    }

    public void setPersonInChargeName(String personInChargeName) {
        this.personInChargeName = personInChargeName;
    }


    public ProjectTaskProcessNodeDTO getDesignUser() {
        return designUser;
    }

    public void setDesignUser(ProjectTaskProcessNodeDTO designUser) {
        this.designUser = designUser;
    }

    public ProjectTaskProcessNodeDTO getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(ProjectTaskProcessNodeDTO checkUser) {
        this.checkUser = checkUser;
    }

    public ProjectTaskProcessNodeDTO getExamineUser() {
        return examineUser;
    }

    public void setExamineUser(ProjectTaskProcessNodeDTO examineUser) {
        this.examineUser = examineUser;
    }

    public String getDesignUserName() {
        return designUserName;
    }

    public void setDesignUserName(String designUserName) {
        this.designUserName = designUserName;
    }

    public String getCheckUserName() {
        return checkUserName;
    }

    public void setCheckUserName(String checkUserName) {
        this.checkUserName = checkUserName;
    }

    public String getExamineUserName() {
        return examineUserName;
    }

    public void setExamineUserName(String examineUserName) {
        this.examineUserName = examineUserName;
    }

    public List<ProjectDesignUser> getDesignersList() {
        return designersList;
    }

    public void setDesignersList(List<ProjectDesignUser> designersList) {
        this.designersList = designersList;
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    public String getStateHtml() {
        String stateStr;
        switch (taskState) {
            case 1:
                stateStr = "进行中";
                break;
            case 2:
                stateStr = "超时进行";
                break;
            case 3:
                stateStr = "已完成";
                break;
            case 4:
                stateStr = "超时完成";
                break;
            case 5:
                stateStr = "未开始";
                break;
            case 6:
                stateStr = "进行中";
                break;
            case 7:
                stateStr = "未发布";
                break;
            default:
                // stateStr = "--";
                stateStr = "进行中";
        }
        return stateStr;
    }

    public void setStateHtml(String stateHtml) {
        this.stateHtml = stateHtml;
    }
}
