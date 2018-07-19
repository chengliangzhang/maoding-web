package com.maoding.mytask.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * Created by Chengliang.zhang on 2017/5/20.
 */
public class MyTaskQueryDTO extends BaseDTO {
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

    /** 要查询的签发任务编号 */
    private String issueTaskId;
    /** 是否查询交付任务 查询交付任务时必须为"1" */
    private String isDelivery;
    private String isDeliver;
    /** 归档目录编号 */
    private String nodeId;
    /** 要查询的个人任务类型 */
    private Integer myTaskType;

    public Integer getMyTaskType() {
        return myTaskType;
    }

    public void setMyTaskType(Integer myTaskType) {
        this.myTaskType = myTaskType;
    }

    public String getIssueTaskId() {
        return getTaskId();
    }

    public void setIssueTaskId(String issueTaskId) {
        setTaskId(issueTaskId);
    }

    public String getIsDeliver() {
        return isDeliver;
    }

    public void setIsDeliver(String isDeliver) {
        this.isDeliver = isDeliver;
    }

    public String getIsDelivery() {
        return getIsDeliver();
    }

    public void setIsDelivery(String isDelivery) {
        setIsDeliver(isDelivery);
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

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
