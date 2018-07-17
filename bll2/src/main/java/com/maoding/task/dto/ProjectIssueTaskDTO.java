package com.maoding.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskDTO
 * 类描述：签发DTO
 * 作    者：MaoSF
 * 日    期：2015年8月15日-上午10:13:28
 */
public class ProjectIssueTaskDTO extends TaskBaseDTO{

    /**
     * id
     */
    private String id;

    /** 排序序号 */
    private Integer seq;

    private String taskPid;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 被修改记录的id，用于修改任务，新增一条未被发布的数据，该字段记录被修改记录的id
     */
    private String beModifyId;


    /**
     * 发布版本的id
     */
    private String publishId;


    /**
     * 任务状态(0生效，1不生效,2:未发布)
     */
    private String taskStatus;

    /**
     * 任务类型（0：生产，1：设计阶段，2：签发并发布，3：未发布的签发数据）
     */
    private int taskType;

    private String taskRemark;

    /**
     * 签发组织id
     */
    private String fromCompanyId;

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
     * 子任务的个数
     */
    private int isHasChild;


    /**
     * 路径
     */
    private String taskPath;

    private int taskLevel;

    /**
     * 是否是第一个
     */
    private boolean isFirst;

    /**
     * 是否是最后一个
     */
    private boolean isLast;

    /**
     * 设计任务
     */
    List<ProjectIssueTaskDTO> childList = new ArrayList<>();

    /**
     * 签发次数
     */
    Integer issueLevel;

    /**
     * 是否能被删除
     */
    Boolean canBeDelete;

    /**
     * 是否能被编辑名称
     */
    Boolean canBeEdit;

    Boolean isAddChild;

    /**
     * 是否有变更记录
     */
    Boolean isChangeTime;

    private String currentCompanyId;

    private Integer isOperaterTask;

    /**
     * 与此任务相关的其他组织的计划进度
     */
    List<ProjectTaskPlanDTO> issuePlanList = new ArrayList<>();

    /** 签发人编号 */
    private String issueUserId;

    /** 签发人名称 */
    private String issueUserName;

    /** 签发时间 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date issueTime;

    public String getIssueUserId() {
        return issueUserId;
    }

    public void setIssueUserId(String issueUserId) {
        this.issueUserId = issueUserId;
    }

    public String getIssueUserName() {
        return issueUserName;
    }

    public void setIssueUserName(String issueUserName) {
        this.issueUserName = issueUserName;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
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
        this.taskName = taskName;
    }

    public String getBeModifyId() {
        return beModifyId;
    }

    public void setBeModifyId(String beModifyId) {
        this.beModifyId = beModifyId;
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

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
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

    public int getIsHasChild() {
        return isHasChild;
    }

    public void setIsHasChild(int isHasChild) {
        this.isHasChild = isHasChild;
    }

    public List<ProjectIssueTaskDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<ProjectIssueTaskDTO> childList) {
        this.childList = childList;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }



    public String getTaskPath() {
        return taskPath;
    }

    public void setTaskPath(String taskPath) {
        this.taskPath = taskPath;
    }

    public Integer getIssueLevel() {
        return issueLevel;
    }

    public void setIssueLevel(Integer issueLevel) {
        this.issueLevel = issueLevel;
    }

    public Boolean getCanBeDelete() {
        return canBeDelete;
    }

    public void setCanBeDelete(Boolean canBeDelete) {
        this.canBeDelete = canBeDelete;
    }

    public String getTaskPid() {
        return taskPid;
    }

    public void setTaskPid(String taskPid) {
        this.taskPid = taskPid;
    }

    public List<ProjectTaskPlanDTO> getIssuePlanList() {
        return issuePlanList;
    }

    public void setIssuePlanList(List<ProjectTaskPlanDTO> issuePlanList) {
        this.issuePlanList = issuePlanList;
    }

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
    }

    public String getCurrentCompanyId() {
        return currentCompanyId;
    }

    public void setCurrentCompanyId(String currentCompanyId) {
        this.currentCompanyId = currentCompanyId;
    }

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public int getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(int taskLevel) {
        this.taskLevel = taskLevel;
    }

    public Integer getIsOperaterTask() {
        return isOperaterTask;
    }

    public void setIsOperaterTask(Integer isOperaterTask) {
        this.isOperaterTask = isOperaterTask;
    }

    public Boolean getAddChild() {
        return isAddChild;
    }

    public void setAddChild(Boolean addChild) {
        isAddChild = addChild;
    }

    public Boolean getCanBeEdit() {
        return canBeEdit;
    }

    public void setCanBeEdit(Boolean canBeEdit) {
        this.canBeEdit = canBeEdit;
    }

    public Boolean getChangeTime() {
        return isChangeTime;
    }

    public void setChangeTime(Boolean changeTime) {
        isChangeTime = changeTime;
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }
}