package com.maoding.mytask.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.util.Date;


public class MyTaskEntity extends BaseEntity{

    /** 确认交付文件上传完毕 */
    public static final int DELIVER_CONFIRM_FINISH = 26;
    /** 进行交付文件上传 */
    public static final int DELIVER_EXECUTE = 27;

    private String taskTitle;

    /**
     * 任务类型(1.签发：经营负责人,2.生产安排（项目设计负责人）.13.生产安排（任务负责人的任务）12.生产安排（界面跳转）,
     3.设计（设计，校对，审核），
     4.付款（技术审查费-确认付款金额（经营负责人）），
     5.付款（技术审查费-确认付款（企业负责人）），-- 没有此任务
     6.付款（合作设计费-确认付款金额（经营负责人）），
     7.付款（合作设计费-付款确认（企业负责人）），-- 没有此任务
     8.到款（技术审查费-确认到款），
     9.到款（合作设计费-到款确认）
     10.到款（合同回款-到款确认）
     11.报销单审核,
    --  14,设置设计负责人，15，设置任务负责人 （已经没有此两种任务了）
     16.技术审查费-财务-付款，17.技术审查费-财务-到款 ，
     18.合作设计费-财务-付款，19.合作设计费-财务-到款,
     20.其他费-财务-付款，21.其他费-财务-到款
     22.所有设计任务已完成，给组织的设计负责人推送任务
     23.费用申请审核
//     24.请假出差审核 请假出差没有任务
//     25.出差审核
     */
    private Integer taskType;

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