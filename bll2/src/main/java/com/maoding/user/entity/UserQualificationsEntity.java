package com.maoding.user.entity;

import java.util.Date;

import com.maoding.core.base.entity.BaseEntity;

public class UserQualificationsEntity extends BaseEntity implements java.io.Serializable{

    private String userId;

    private String regCertificateLevel;

    private String certificateMajor;

    private String certificateName;

    private String certificateNo;

    private Date issuingDate;

    private String practiceSealNo;

    private String practiceSealFndate;

    private String practiceMajor;

    private String attachId;

    private Date createDate;

    private String createBy;

    private Date updateDate;

    private String updateBy;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getRegCertificateLevel() {
        return regCertificateLevel;
    }

    public void setRegCertificateLevel(String regCertificateLevel) {
        this.regCertificateLevel = regCertificateLevel == null ? null : regCertificateLevel.trim();
    }

    public String getCertificateMajor() {
        return certificateMajor;
    }

    public void setCertificateMajor(String certificateMajor) {
        this.certificateMajor = certificateMajor == null ? null : certificateMajor.trim();
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName == null ? null : certificateName.trim();
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo == null ? null : certificateNo.trim();
    }

    public Date getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(Date issuingDate) {
        this.issuingDate = issuingDate;
    }

    public String getPracticeSealNo() {
        return practiceSealNo;
    }

    public void setPracticeSealNo(String practiceSealNo) {
        this.practiceSealNo = practiceSealNo == null ? null : practiceSealNo.trim();
    }

    public String getPracticeSealFndate() {
        return practiceSealFndate;
    }

    public void setPracticeSealFndate(String practiceSealFndate) {
        this.practiceSealFndate = practiceSealFndate == null ? null : practiceSealFndate.trim();
    }

    public String getPracticeMajor() {
        return practiceMajor;
    }

    public void setPracticeMajor(String practiceMajor) {
        this.practiceMajor = practiceMajor == null ? null : practiceMajor.trim();
    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId == null ? null : attachId.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }
}