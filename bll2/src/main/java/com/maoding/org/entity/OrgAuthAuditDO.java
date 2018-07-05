package com.maoding.org.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/7/12.
 */
public class OrgAuthAuditDO extends BaseEntity implements java.io.Serializable {
    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 认证状态(0.否，1.是，2申请认证)
     */
    private Integer status;

    /**
     * 是否最新审核记录
     */
    private Integer isNew;

    /**
     * 提交认证日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime submitDate;
    /**
     * Date类型提交认证日期
     */
    private Date submitDateCompatible;

    /**
     * 审核执行日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime approveDate;
    /**
     * Date类型审核执行日期
     */
    private Date approveDateCompatible;
    /**
     * 认证审核人姓名
     */
    private String auditPerson;

    /**
     * 认证无法通过原因分类
     */
    private String auditMessage;

    /**
     * 认证不通过原因文字解释
     */
    private String rejectReason;


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public LocalDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(LocalDateTime submitDate) {
        this.submitDate = submitDate;
        if (this.submitDate != null){
            submitDateCompatible = Date.from(this.submitDate.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    public Date getSubmitDateCompatible() {
        return submitDateCompatible;
    }

    public void setSubmitDateCompatible(Date submitDateCompatible) {
        this.submitDateCompatible = submitDateCompatible;
        if (this.submitDateCompatible != null){
            submitDate = LocalDateTime.ofInstant(this.submitDateCompatible.toInstant(),ZoneId.systemDefault());
        }
    }

    public LocalDateTime getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(LocalDateTime approveDate) {
        this.approveDate = approveDate;
        if (this.approveDate != null){
            approveDateCompatible = Date.from(this.approveDate.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    public Date getApproveDateCompatible() {
        return approveDateCompatible;
    }

    public void setApproveDateCompatible(Date approveDateCompatible) {
        this.approveDateCompatible = approveDateCompatible;
        if (this.approveDateCompatible != null){
            approveDate = LocalDateTime.ofInstant(this.approveDateCompatible.toInstant(),ZoneId.systemDefault());
        }
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson == null ? null : auditPerson.trim();
    }

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage == null ? null : auditMessage.trim();
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason == null ? null : rejectReason.trim();
    }
}
