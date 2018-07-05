package com.maoding.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Creator: sandy
 * Date:2017/11/13
 * 类名：maoding-web
 * 任务状态计算字段
 */
public class TaskBaseDTO {

    /**
     * 状态(1:正常进行，2：超时进行，3：正常完成，4.超时完成,5.未开始，10,无状态)
     */
    private int taskState;

    /**
     * 状态文字
     */
    private String statusText;

    /**
     * 状态html
     */
    private String stateHtml;

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


    private Map<String,Object> stateMap = new HashMap<>();

    public int getTaskState() {
        if(this.stateMap!=null && stateMap.get("taskState")!=null){

            taskState = Integer.parseInt(((Long) stateMap.get("taskState")).toString());
        }
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public String getStateHtml() {
        String stateStr;
        switch (taskState) {
            case 1:
            case 2:
            case 6:
                stateStr = "进行中";
                break;
            case 5:
                stateStr = "未开始";
                break;
            case 3:
            case 4:
                stateStr = "已完成";
                break;
            case 7:
                stateStr = "未发布";
                break;
            default:
                // stateStr = "--";
                stateStr = "进行中";
        }
        return stateStr;
    }

    public void setStateHtml(String stateHtml) {
        this.stateHtml = stateHtml;
    }

    public Map<String, Object> getStateMap() {
        return stateMap;
    }

    public void setStateMap(Map<String, Object> stateMap) {
        this.stateMap = stateMap;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Date getCompleteDate() {
        if(this.stateMap!=null && stateMap.get("completeDate")!=null){
            if (isComplete()) {
                completeDate = (Date) stateMap.get("completeDate");
            }
        }
        return completeDate;
    }

    private boolean isComplete(){
        return (getTaskState() == 3) || (getTaskState() == 4);
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
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
}
