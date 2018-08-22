package com.maoding.mytask.dto;

import com.maoding.core.util.DateUtils;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/4/18 18:41
 * 描    述 :
 */
public class MyTaskSimpleDTO {
    private String id;
    /** 任务名称 */
    private String taskTitle;
    /** 截止日期 */
    private Date deadline;
    /** 任务相关的项目名称 */
    private String projectName;
    /** 时间字符串 */
    private String timeText;
    /** 任务开始时间 */
    private Date startTime;
    /** 任务结束时间 */
    private Date endTime;
    /** 任务角色名称 */
    private String roleName;
    /** 是轻量任务 */
    private Boolean isLightTask;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getLightTask() {
        return isLightTask;
    }

    public void setLightTask(Boolean lightTask) {
        isLightTask = lightTask;
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

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTimeText() {
        String s = timeText;
        if (s == null) {
            if ((isLightTask == null) || (isLightTask)) {
                s = DateUtils.date2Str(deadline, DateUtils.date_sdf2);
            } else {
                s = DateUtils.getTimeText(startTime,endTime, DateUtils.date_sdf2);
            }
        }
        return s;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
