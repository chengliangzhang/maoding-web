package com.maoding.mytask.dto;

/**
 * Created by Chengliang.zhang on 2017/5/20.
 */
public class MyTaskActiveRequestDTO {
    String myTaskId;
    String userId;
    String companyId;
    String currentCompanyUserId;
    String taskId;
    public String getMyTaskId() {
        return myTaskId;
    }

    public void setMyTaskId(String taskId) {
        this.myTaskId = taskId;
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

    public String getCurrentCompanyUserId() {
        return currentCompanyUserId;
    }

    public void setCurrentCompanyUserId(String currentCompanyUserId) {
        this.currentCompanyUserId = currentCompanyUserId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
