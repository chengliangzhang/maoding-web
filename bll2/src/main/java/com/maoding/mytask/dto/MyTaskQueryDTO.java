package com.maoding.mytask.dto;

/**
 * Created by Chengliang.zhang on 2017/5/20.
 */
public class MyTaskQueryDTO {
    /**
     * 匹配的任务ID
     */
    String taskId;
    /**
     * 匹配的用户ID
     */
    String userId;
    /**
     * 匹配的公司ID
     */
    String companyId;
    /**
     * 要查询的个人任务的状态
     */
    Integer status;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
