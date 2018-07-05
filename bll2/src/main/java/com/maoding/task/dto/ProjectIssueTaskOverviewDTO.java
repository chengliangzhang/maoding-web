package com.maoding.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：签发任务总览DTO
 * 作    者：ZCL
 * 日    期：2017-5-17
 */
public class ProjectIssueTaskOverviewDTO {
    /**
     * 任务ID（即可以是设计内容，也可以是设计任务）
     */
    String taskId;

    /**
     * 父任务id
     */
    private String taskPid;
    /**
     * 任务名称（即可以是设计内容，也可以是设计任务）
     */
    String taskName;
    /**
     * 状态(1:正常进行，2：超时进行，3：正常完成，4.超时完成,5.未开始，10,无状态)
     */
    Integer taskState;
    /**
     * 任务开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date planStartTime;
    /**
     * 任务结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
     * 部门ID
     */
    private String departId;
    /**
     * 部门名
     */
    private String departName;
    /**
     * 设计组织项目经营人ID
     */
    String managerId;
    /**
     * 设计组织项目经营人名称
     */
    String managerName;
    /**
     * 是否二次签发
     */
    Boolean isManyIssue;
    /**
     * 最后生产完成时间
     */
    Date lastDate;
    /**
     * 状态显示字符串
     */
    String stateHtml;

    /**
     * 当前公司id（传递过来的参数）
     */
    private String currentCompanyId;
    /**
     * 子任务
     */
    List<ProjectIssueTaskOverviewDTO> childrenList;

    /**
     * 与其相关的组织的计划进度
     */
    List<ProjectTaskPlanDTO> issuePlanList = new ArrayList<>();

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getTaskState() {
        return taskState;
    }

    public void setTaskState(Integer taskState) {
        this.taskState = taskState;
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

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Boolean getManyIssue() {
        return isManyIssue;
    }

    public void setManyIssue(Boolean manyIssue) {
        isManyIssue = manyIssue;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public String getStateHtml() {
//        taskState状态(1:正常进行，2：超时进行，3：正常完成，4.超时完成,5.未开始，10,无状态)
        if (1 == taskState) {
            stateHtml = "进行中";
        } else if (2 == taskState) {
            stateHtml = "超时进行";
        } else if (3 == taskState) {
            stateHtml = "已完成";
        } else if (4 == taskState) {
            stateHtml = "超时完成";
        } else if (5 == taskState) {
            stateHtml = "未开始";
        } else if(6==taskState){
            stateHtml="进行中";
        }else if(7==taskState){
            stateHtml = "未发布";
        }else{
            stateHtml = "--";
        }
        return stateHtml;
    }

    public void setStateHtml(String stateHtml) {
        this.stateHtml = stateHtml;
    }

    public List<ProjectIssueTaskOverviewDTO> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<ProjectIssueTaskOverviewDTO> childrenList) {
        this.childrenList = childrenList;
    }

    public List<ProjectTaskPlanDTO> getIssuePlanList() {
        return issuePlanList;
    }

    public void setIssuePlanList(List<ProjectTaskPlanDTO> issuePlanList) {
        this.issuePlanList = issuePlanList;
    }

    public String getCurrentCompanyId() {
        return currentCompanyId;
    }

    public void setCurrentCompanyId(String currentCompanyId) {
        this.currentCompanyId = currentCompanyId;
    }

    public String getTaskPid() {
        return taskPid;
    }

    public void setTaskPid(String taskPid) {
        this.taskPid = taskPid;
    }
}
