package com.maoding.mytask.dto;

import com.maoding.task.dto.TaskBaseDTO;


public class MyTaskList2DTO extends TaskBaseDTO {

    private String id;

   /* 任务类型(1.签发：经营负责人,2.生产安排（项目设计负责人）.13.生产安排（任务负责人。）
            3.设计（设计，校对，审核），
            4.付款（技术审查费-确认付款款（经营负责人）），5.付款（技术审查费-确认付款款（企业负责人）），6.付款（合作设计费-付款确认（经营负责人）），7.付款（合作设计费-付款确认（企业负责人）），
            8.到款（技术审查费-确认到款），9.到款（合作设计费-到款确认）10.到款（合同回款-到款确认）
            11.报销单审核,12.同意邀请合作伙伴) */
    private int taskType;

    /**
     * 类型
     */
    private String taskTypeText;

    private String targetId;

    private String param1;

    private String projectId;

    private String handlerId;

    private String projectName;

    private String taskName;

    private String taskContent;

    private Integer type;//该字段用于作为鉴别器的字段，sql中使用到

    private String createDate;

    private String role;

    private String description;

    private String companyId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
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
        this.taskContent = taskContent;
    }

    public String getTaskTypeText() {
        //暂时未处理
        switch (taskType){
            case 1:
                return "任务签发";
            case 3://（设计，校对，审核）任务完成
                return  this.taskContent+"任务";
            case 11:
                return "报销审批";
            case 23:
                return "费用审批";
            case 12: // 任务负责人，生产安排任务
                return "生产安排";
//            case 14:
//                return "设置设计负责人";
//            case 15:
//                return "设置任务负责人"; //暂时无该任务
            case 4:
            case 6:
            case 10:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                return "收支管理";
            case 13: //任务负责人所负责的任务完成，13完成，同时触发12完成
            case 22: //所有设计任务已完成，给组织的设计负责人推送任务
                return "审批";
            default:return null;
        }
    }

    public void setTaskTypeText(String taskTypeText) {
        this.taskTypeText = taskTypeText;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}