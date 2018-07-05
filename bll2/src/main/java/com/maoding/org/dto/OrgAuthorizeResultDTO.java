package com.maoding.org.dto;

/**
 * Created by Chengliang.zhang on 2017/7/11.
 */
public class OrgAuthorizeResultDTO {
    /**
     * 组织ID
     */
    String orgId;
    /**
     * 认证状态(0.否，1.是，2申请认证)
     */
    Integer status;
    /**
     * 认证审核人姓名
     */
    String auditPerson;
    /**
     * 认证无法通过原因分类
     */
    Integer rejectType;
    /**
     * 认证不通过原因文字解释
     */
    String rejectReason;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public Integer getRejectType() {
        return rejectType;
    }

    public void setRejectType(Integer rejectType) {
        this.rejectType = rejectType;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
