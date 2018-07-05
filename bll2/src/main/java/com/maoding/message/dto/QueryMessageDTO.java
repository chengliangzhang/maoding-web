package com.maoding.message.dto;

import java.util.Date;

/**
 * Created by Idccapp21 on 2016/10/17.
 */
public class QueryMessageDTO {

    private String id;

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 项目Id
     */
    private String projectId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }
}

