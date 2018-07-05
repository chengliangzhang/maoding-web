package com.maoding.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/7/11.
 */
public class OrgAuthenticationDTO {
    /**
     * 组织ID applyAuthentication*
     */
    String id;

    /**
     * 卯丁内组织名称
     */
    String orgAlias;

    /**
     * 企业名称 applyAuthentication*
     */
    String orgName;

    /**
     * 认证状态(0.否，1.是，2申请认证)
     */
    Integer authenticationStatus;
    /**
     * 认证状态字符串
     */
    String authenticationStatusString;

    /**
     * 经办人姓名*
     */
    String operatorName;
    /**
     * 经办人照片文件名（包含路径）applyAuthentication*
     */
    String operatorPhoto;

    /**
     * 认证不通过原因
     */
    String rejectReason;

    /**
     * 法人代表名称*
     */
    String legalRepresentative;

    /**
     * 法人代表照片文件名（包含路径）applyAuthentication*
     */
    String legalRepresentativePhoto;

    /**
     * 营业执照的类型（0：普通营业执照（仍然标识为15位的“注册号”），1：多证合一营业执照（原“注册号”字样，调整为18位的“统一社会信用代码”））*
     */
    Integer businessLicenseType;
    /**
     * 营业执照的类型字符串 applyAuthentication*
     */
    String businessLicenseTypeString;

    /**
     * 工商营业执照号码（注册号）applyAuthentication*
     */
    String businessLicenseNumber;
    /**
     * 工商营业执照照片文件名（包含路径）applyAuthentication*
     */
    String businessLicensePhoto;

    /**
     * 申请日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    Date applyDate;

    /**
     * 有效期（结束日期）
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    Date expiryDate;

    /**
     *  带公章卯丁协议照片文件（包含路径）applyAuthentication*
     */
    String sealPhoto;

    /**
     * 审核人姓名
     */
    String auditorName;

    /**
     * 审核日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    Date auditDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgAlias() {
        return orgAlias;
    }

    public void setOrgAlias(String orgAlias) {
        this.orgAlias = orgAlias;
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

    public String getAuthenticationStatusString() {
        String s = authenticationStatusString;
        if ((s == null) && (authenticationStatus != null)){
            if (authenticationStatus == 2) {
                s = "认证中";
            } else if (authenticationStatus == 1) {
                s = "是";
            } else {
                s = "否";
            }
        }
        return s;
    }

    public void setAuthenticationStatusString(String authenticationStatusString) {
        this.authenticationStatusString = authenticationStatusString;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorPhoto() {
        return operatorPhoto;
    }

    public void setOperatorPhoto(String operatorPhoto) {
        this.operatorPhoto = operatorPhoto;
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

    public String getLegalRepresentativePhoto() {
        return legalRepresentativePhoto;
    }

    public void setLegalRepresentativePhoto(String legalRepresentativePhoto) {
        this.legalRepresentativePhoto = legalRepresentativePhoto;
    }

    public Integer getBusinessLicenseType() {
        return businessLicenseType;
    }

    public void setBusinessLicenseType(Integer businessLicenseType) {
        this.businessLicenseType = businessLicenseType;
    }

    public String getBusinessLicenseTypeString() {
        String s = businessLicenseTypeString;
        if ((s == null) && (businessLicenseType != null)){
            if (businessLicenseType == 0) {
                s = "注册号";
            } else {
                s = "统一社会信用代码";
            }
        }
        return s;
    }

    public void setBusinessLicenseTypeString(String businessLicenseTypeString) {
        this.businessLicenseTypeString = businessLicenseTypeString;
    }

    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public String getBusinessLicensePhoto() {
        return businessLicensePhoto;
    }

    public void setBusinessLicensePhoto(String businessLicensePhoto) {
        this.businessLicensePhoto = businessLicensePhoto;
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

    public String getSealPhoto() {
        return sealPhoto;
    }

    public void setSealPhoto(String sealPhoto) {
        this.sealPhoto = sealPhoto;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }
}
