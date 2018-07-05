package com.maoding.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Idccapp21 on 2016/12/29.
 */
public class ProjectProcessTimeDTO extends BaseDTO {

    private String projectId;//为了做权限控制传递过来的参数

    /**
     *  组织ID（当前操作的组织ID）
     */
    private String companyId;

    /**
     *  开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;


    /**
     * 1=约定，2＝计划
     */
    private Integer type;


    /**
     * 备注
     */
    private String memo;


    /**
     * 任务id（设计阶段的id）
     */
    private String targetId;

    /**
     * 共多少天
     */
    private int allDay;

    /**
     * 操作者名称
     */
    private String userName;

    /**
     * 更改时间
     */
    private Date changedTime;

    /**
     * 计划进度的更改历史字符串
     */
    private String historyText;

    public String getHistoryText() {
        return historyText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getChangedTime() {
        return changedTime;
    }

    public void setChangedTime(Date changedTime) {
        this.changedTime = changedTime;
    }

    public void setHistoryText(String historyText) {
        this.historyText = historyText;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getAllDay() {
        if(this.endTime!=null && this.startTime!=null)
        {
            allDay = DateUtils.daysOfTwo(DateUtils.date_sdf.format(this.endTime), DateUtils.date_sdf.format(this.startTime))+1;
        }
        return allDay;
    }

    public void setAllDay(int allDay) {
        this.allDay = allDay;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
