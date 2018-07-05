package com.maoding.mytask.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class MyTaskDTO {

    private String id;

    private String taskTitle;

    private String taskTitle2;

   /* 任务类型(1.签发：经营负责人,2.生产安排（项目设计负责人）.13.生产安排（任务负责人。）
            3.设计（设计，校对，审核），
            4.付款（技术审查费-确认付款款（经营负责人）），5.付款（技术审查费-确认付款款（企业负责人）），6.付款（合作设计费-付款确认（经营负责人）），7.付款（合作设计费-付款确认（企业负责人）），
            8.到款（技术审查费-确认到款），9.到款（合作设计费-到款确认）10.到款（合同回款-到款确认）
            11.报销单审核,12.同意邀请合作伙伴) */
    private int taskType;

    private String handlerId;

    private String companyId;

    private String status;

    private String targetId;

    private String projectId;

    private String taskContent;

    private String param1;

    /**
     *
     */
    private String taskMemo;

    /**
     * 描述（目前保存合同收款明细的发票信息）,任务描述也放在此
     */
    private String description;

    /**
     * 费用（目前保存合同收款明细的回款信息）
     */
    private String fee;

    /**
     * 费用使用，已收款
     */
    private String paymentFee;//已收款
    /**
     * 费用使用，未收
     */
    private String notReceiveFee;//未收

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    private String title;

    private String createBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle == null ? null : taskTitle.trim();
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTaskMemo() {
        return taskMemo;
    }

    public void setTaskMemo(String taskMemo) {
        this.taskMemo = taskMemo;
    }

    public String getTaskTitle2() {
        return taskTitle2;
    }

    public void setTaskTitle2(String taskTitle2) {
        this.taskTitle2 = taskTitle2;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(String paymentFee) {
        this.paymentFee = paymentFee;
    }

    public String getNotReceiveFee() {
        return notReceiveFee;
    }

    public void setNotReceiveFee(String notReceiveFee) {
        this.notReceiveFee = notReceiveFee;
    }

    private String getTitle(){
        //暂时未处理
        switch (taskType){
            case 1:
                return "任务签发";
//            case 2:已经取消了2
//                return  convertMyTask2(entity);
            case 3://（设计，校对，审核）任务完成
                return  "确定设计任务已完成";
            case 4:
                return "技术审查费 - 确认付款金额";
            case 6:
                return "合作设计费 - 确认付款金额";
//            case 6: 已经取消了6,7，8,9
//            case 7:
//            case 8:
//            case 9:
//                return convertMyTask8(entity);
            case 10:
                return "合同回款 - 确认到付金额及日期";

//            case 11: 报销的不需要重新设置
//                return  convertMyTask11(entity);
//            case 12: 已经没有12
//                return convertMyTask12(entity);
            case 12: // 任务负责人，生产安排任务
                return "生产安排";
            case 13: //任务负责人所负责的任务完成，13完成，同时触发12完成
                return "确定设计任务已完成";
            case 14:
                return "设置设计负责人";
            case 15:
                return "设置任务负责人";
            case 16:
                return "技术审查费 - 确认付款日期";
            case 17:
                return "技术审查费 - 确认到账日期";
            case 18:
                return "合作设计费 - 确认付款日期";
            case 19:
                return  "合作设计费 - 确认到账日期";
            case 20:
                return "其他费用 - 确认付款日期";
            case 21:
                return "其他费用 - 确认到账日期";
            case 22://所有设计任务已完成，给组织的设计负责人推送任务
                return "确定设计任务已完成";
            default:return null;
        }
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}