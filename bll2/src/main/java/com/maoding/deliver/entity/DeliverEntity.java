package com.maoding.deliver.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.util.Date;


public class DeliverEntity extends BaseEntity{
    /** 交付任务类型 */
    public static final int DELIVER_ACTION = 28;


    private String taskTitle;

    private Integer taskType;

    /** 处理人编号，使用company_user的id */
    private String handlerId;

    private String companyId;

    private String status;

    private String targetId;

    private String param1;//保存targetId所关联的节点的id（保存所在收款节点，便于后面查询，type=3保存所在任务的id，便于后面查询）,

    private String param2;

    private String param3;//任务分组：1：财务类型（项目财务任务，可一批人处理，所以没有handlerId，用param3=1标识为财务型）

    private String param4; //删除标识：1：删除，0：有效

    private String projectId;

    private String taskContent;

    /**
     * 任务发送公司id
     */
    private String sendCompanyId;

    /**
     * 截止日期
     */
    private Date deadline;

    /**
     * 完成时间
     */
    private Date completeDate;

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle == null ? null : taskTitle.trim();
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId == null ? null : handlerId.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }


    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1 == null ? null : param1.trim();
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2 == null ? null : param2.trim();
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3 == null ? null : param3.trim();
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4 == null ? null : param4.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent == null ? null : taskContent.trim();
    }

    public String getSendCompanyId() {
        return sendCompanyId;
    }

    public void setSendCompanyId(String sendCompanyId) {
        this.sendCompanyId = sendCompanyId;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }
}