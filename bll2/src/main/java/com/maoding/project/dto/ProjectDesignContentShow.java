package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * Created by Idccapp21 on 2017/2/28.
 */
public class ProjectDesignContentShow extends BaseDTO {

    private String contentName;

    /**
     * 状态(1:正常进行，2：超时进行，3：正常完成，4.超时完成,5.未开始，10,无状态)
     */
    private int status;

    private String statusText;

    /**
     *开始时间
     */
    private String planStartTime;

    /**
     *结束时间
     */
    private String planEndTime;

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        switch (status) {
            case 1:
                statusText = "进行中";
                break;
            case 2:
                statusText = "超时进行"; // + (DateUtils.daysOfTwo(DateUtils.date2Str(DateUtils.date_sdf),endTime)) + "天";
                break;
            case 3:
                statusText = "已完成";
            break;
            case 4:

                statusText = "超时完成";
            break;
            case 5:
                statusText = "未开始";
                break;
            case 6:
                statusText = "进行中";
                break;
            case 7:
                statusText = "未发布";
                break;
            default:
                statusText = "--";
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }
}
