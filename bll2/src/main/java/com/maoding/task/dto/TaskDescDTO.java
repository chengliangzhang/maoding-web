package com.maoding.task.dto;

import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskDTO
 * 类描述：任务数据
 * 作    者：MaoSF
 * 日    期：2016年12月31日-上午10:13:28
 */
public class TaskDescDTO {

    /**
     * 任务id
     */
    private String id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 备注
     */
    private String taskRemark;

    /**
     * 状态(1:正常进行，2：超时进行，3：正常完成，4.超时完成,5.未开始，10,无状态)
     */
    private int taskState;

    /**
     *开始时间
     */
    private String startTime;

    /**
     *结束时间
     */
    private String endTime;

    /**
     *状态字符串
     */
    private String stateHtml;

    /**
     *共多少天
     */
    private int allDay;

    private String companyName;

    private String taskFullName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStateHtml() {
        return stateHtml;
    }

    public void setStateHtml(String stateHtml) {
        this.stateHtml = stateHtml;
    }

    public int getAllDay() {
        if(!StringUtil.isNullOrEmpty(this.startTime) && !StringUtil.isNullOrEmpty(this.endTime)){
            allDay = DateUtils.daysOfTwo(endTime, this.startTime)+1;
        }
        return allDay;
    }

    public void setAllDay(int allDay) {
        this.allDay = allDay;
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaskFullName() {
        return taskFullName;
    }

    public void setTaskFullName(String taskFullName) {
        this.taskFullName = taskFullName;
    }
}