package com.maoding.commonModule.entity;

import com.maoding.core.base.entity.BaseEntity;

public class CopyRecordEntity extends BaseEntity {

    private String targetId;

    private String companyUserId;

    private String sendCompanyUserId;

    private String operateRecordId;

    private Integer recordType;

    private Integer deleted;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId == null ? null : companyUserId.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getSendCompanyUserId() {
        return sendCompanyUserId;
    }

    public void setSendCompanyUserId(String sendCompanyUserId) {
        this.sendCompanyUserId = sendCompanyUserId;
    }

    public String getOperateRecordId() {
        return operateRecordId;
    }

    public void setOperateRecordId(String operateRecordId) {
        this.operateRecordId = operateRecordId;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }
}