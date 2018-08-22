package com.maoding.financial.dto;

import com.maoding.core.util.DateUtils;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/4/18 18:13
 * 描    述 :
 */
public class LeaveSimpleDTO {
    private static final String LEAVE_TYPE_WORK_OUT = "10";
    private String id;
    /** 请假类型 */
    private String leaveType;
    /** 请假、出差的开始时间 */
    private Date startTime;
    /** 请假、出差的结束时间 */
    private Date endTime;
    /** 时间字符串 */
    private String timeText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
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

    public String getTimeText() {
        String s = timeText;
        if (s == null){
            if (LEAVE_TYPE_WORK_OUT.equals(leaveType)){
                s = DateUtils.getTimeText(startTime,endTime, DateUtils.workOutFormat);
            } else {
                s = DateUtils.getTimeText(startTime,endTime, DateUtils.leaveOffFormat);
            }
        }
        return s;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }
}
