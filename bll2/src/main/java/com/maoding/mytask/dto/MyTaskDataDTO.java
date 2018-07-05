package com.maoding.mytask.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class MyTaskDataDTO {

    private String id;

   /* 任务类型(1.签发：经营负责人,2.生产安排（项目设计负责人）.13.生产安排（任务负责人。）
            3.设计（设计，校对，审核），
            4.付款（技术审查费-确认付款款（经营负责人）），5.付款（技术审查费-确认付款款（企业负责人）），6.付款（合作设计费-付款确认（经营负责人）），7.付款（合作设计费-付款确认（企业负责人）），
            8.到款（技术审查费-确认到款），9.到款（合作设计费-到款确认）10.到款（合同回款-到款确认）
            11.报销单审核,12.同意邀请合作伙伴) */
    private int taskType;

    private String status;

    private String targetId;

    private String projectId;

    private String role;

    private String companyName;

    private String taskContent;

    private String taskTitle;

    /**
     * 描述
     */
    private Object description;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskTitle() {
        //暂时未处理
        switch (taskType){
            case 1:
                return "任务签发";
            case 3://（设计，校对，审核）任务完成
                return  "确定设计任务已完成";
            case 4:
                return "技术审查费 - 确认付款金额";
            case 6:
                return "合作设计费 - 确认付款金额";
            case 10:
                return "合同回款 - 确认到账金额及日期";
            case 11:
                return "审批任务";
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
                return "其他费用 - 确认付款金额及日期";
            case 21:
                return "其他费用 - 确认到账金额及日期";
            case 22://所有设计任务已完成，给组织的设计负责人推送任务
                return "确定设计任务已完成";
            default:return null;
        }
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getRole() {
        //暂时未处理
        switch (taskType){
            case 1:
            case 14:
            case 4:
            case 6:
                return "经营负责人";
            case 3:
                return taskContent+"人";
            case 11:
                return "审批";
            case 12:
            case 13:
                return "任务负责人";
            case 10:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                return  "财务";
            case 15:
            case 22:
                return "设计负责人";
            default:return null;
        }
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }
}