package com.maoding.org.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrgAuthenticationDataDTO {

    private String id;

    /**
     * 企业名称
     */
    private String orgName;

    /**
     * 认证状态(0.否，1.是，2申请认证)
     */
    private Integer authenticationStatus;

    /**
     * 经办人
     */
    private String operatorName;

    /**
     * 认证不通过原因
     */
    private String rejectReason;

    /**
     * 法人代表
     */
    private String legalRepresentative;

    /**
     * 营业执照的类型（0：普通营业执照（仍然标识为15位的“注册号”），1：多证合一营业执照（原“注册号”字样，调整为18位的“统一社会信用代码”））
     */
    private Integer businessLicenseType;

    /**
     * 工商营业执照号码（注册号）
     */
    private String businessLicenseNumber;

    /**
     * 申请日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date applyDate;

    /**
     * 有效期（结束日期）
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date  expiryDate;


    /**
     * 附件
     */
    List<ProjectSkyDriveEntity> attachList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getAuthenticationStatus() {
        return authenticationStatus;
    }

    public void setAuthenticationStatus(Integer authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public Integer getBusinessLicenseType() {
        return businessLicenseType;
    }

    public void setBusinessLicenseType(Integer businessLicenseType) {
        this.businessLicenseType = businessLicenseType;
    }

    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public List<ProjectSkyDriveEntity> getAttachList() {
        return attachList;
    }

    public void setAttachList(List<ProjectSkyDriveEntity> attachList) {
        this.attachList = attachList;
    }
}
