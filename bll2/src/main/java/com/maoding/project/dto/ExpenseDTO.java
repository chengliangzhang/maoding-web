package com.maoding.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/2/23 11:37
 * 描    述 : 报销费用
 */
public class ExpenseDTO {
    //"申请时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expDate;
    //金额
    private BigDecimal expAmount;
    //费用类型
    private String expType;

    private String expTypeStr;
    //申请人
    private String expUserId;

    private String expUser;
    //审批人
    private String auditPersonId;
    private String auditPerson;
    //收支类型
    private String expAllname;
    //用途说明
    private String expUse;
    //拨款人id
    private String allocationUserId;
    private String allocationUser;
    //关联项目
    private String projectId;
    private String projectName;

    private String errorReason;

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public BigDecimal getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(BigDecimal expAmount) {
        this.expAmount = expAmount;
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public String getExpUserId() {
        return expUserId;
    }

    public void setExpUserId(String expUserId) {
        this.expUserId = expUserId;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getExpAllname() {
        return expAllname;
    }

    public void setExpAllname(String expAllname) {
        this.expAllname = expAllname;
    }

    public String getExpUse() {
        return expUse;
    }

    public void setExpUse(String expUse) {
        this.expUse = expUse;
    }

    public String getAllocationUserId() {
        return allocationUserId;
    }

    public void setAllocationUserId(String allocationUserId) {
        this.allocationUserId = allocationUserId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getExpTypeStr() {
        return expTypeStr;
    }

    public void setExpTypeStr(String expTypeStr) {
        this.expTypeStr = expTypeStr;
    }

    public String getExpUser() {
        return expUser;
    }

    public void setExpUser(String expUser) {
        this.expUser = expUser;
    }

    public String getAuditPersonId() {
        return auditPersonId;
    }

    public void setAuditPersonId(String auditPersonId) {
        this.auditPersonId = auditPersonId;
    }

    public String getAllocationUser() {
        return allocationUser;
    }

    public void setAllocationUser(String allocationUser) {
        this.allocationUser = allocationUser;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
}
