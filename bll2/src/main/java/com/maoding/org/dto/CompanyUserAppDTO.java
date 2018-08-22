package com.maoding.org.dto;

import java.util.Date;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyUserAppDTO
 * 类描述：公司人员信息(组织人员列表Entity，此Entity不对应数据库中的表)
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午5:38:15
 */
public class CompanyUserAppDTO extends CompanyUserDataDTO {

    /**
     *邮箱
     */
    private String email;

    /**
     * app要求头像地址
     */
    private String imgUrl;

    private String title;//所处任务的标题

    private String completeTime;//完成时间,设计任务完成时间
    /**
     * 是否可以设置经营负责人/设计负责人（1：可以，0：不可以）
     */
    private int isUpdateOperator;

    private String targetId;

    private Integer taskState;

    /** 工作状态 */
    private Integer workStatus;

    /** 外出开始时间 */
    private Date startTime;

    /** 外出结束时间 */
    private Date endTime;

    /** 任务总数 */
    private Integer taskCount;

    /** 状态描述字符串 */
    private String statusText;

    /** 工作状态合并字符串 */
    private String workStatusString;

    /** 外出开始时间合并字符串 */
    private String startTimeString;

    /** 外出结束时间合并字符串 */
    private String endTimeString;

    public String getWorkStatusString() {
        return workStatusString;
    }

    public void setWorkStatusString(String workStatusString) {
        this.workStatusString = workStatusString;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public int getIsUpdateOperator() {
        return isUpdateOperator;
    }

    public void setIsUpdateOperator(int isUpdateOperator) {
        this.isUpdateOperator = isUpdateOperator;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Integer getTaskState() {
        return taskState;
    }

    public void setTaskState(Integer taskState) {
        this.taskState = taskState;
    }

}