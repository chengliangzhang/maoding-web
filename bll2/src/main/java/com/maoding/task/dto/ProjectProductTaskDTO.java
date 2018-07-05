package com.maoding.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 类    名：ProjectProductTaskDTO
 * 类描述：签发DTO
 * 作    者：wrb
 * 日    期：2017年5月17日-上午16:31:28
 */
public class ProjectProductTaskDTO implements Serializable{

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
     * 状态(1:正常进行，2：超时进行，3：正常完成，4.超时完成,5.未开始，10,无状态)
     */
    private int taskState;

    /**
     * 状态html
     */
    private String stateHtml;

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

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date completeDate;

    /**
     * 设计组织id
     */
    private String companyId;

    /**
     * 设计组织名
     */
    private String companyName;

    /**
     * 部门名称
     */
    private String departId;

    /**
     * 部门名
     */
    private String departName;

    /**
     * 任务描述
     */
    private String taskRemark;

    /**
     * 参与人员（设、校、审）
     */
    //private List<ProjectTaskProcessNodeDTO> designersList= Lists.newArrayList();
    private String designerNames;

    /**
     * 任务负责人
     */
    private String personInChargeName;

    /**
     * 未完成数（子任务）
     */
    private int notCompleteCount;

    /**
     * 父节点ID
     */
    private String taskPid;

    /**
     * 节点记录path
     */
    private String taskPath;

    /**
     * 设计任务
     */
    List<ProjectProductTaskDTO> childList = new ArrayList<>();

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

    public String getStateHtml() {
        return stateHtml;
    }

    public void setStateHtml(String stateHtml) {
        this.stateHtml = stateHtml;
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

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
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

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    public String getDesignerNames() {
        return designerNames;
    }

    public void setDesignerNames(String designerNames) {
        this.designerNames = designerNames;
    }

    public String getPersonInChargeName() {
        return personInChargeName;
    }

    public void setPersonInChargeName(String personInChargeName) {
        this.personInChargeName = personInChargeName;
    }

    public List<ProjectProductTaskDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<ProjectProductTaskDTO> childList) {
        this.childList = childList;
    }

    public int getNotCompleteCount() {
        return notCompleteCount;
    }

    public void setNotCompleteCount(int notCompleteCount) {
        this.notCompleteCount = notCompleteCount;
    }

    public String getTaskPid() {
        return taskPid;
    }

    public void setTaskPid(String taskPid) {
        this.taskPid = taskPid;
    }

    public String getTaskPath() {
        return taskPath;
    }

    public void setTaskPath(String taskPath) {
        this.taskPath = taskPath;
    }
}