package com.maoding.user.entity;

import java.util.Date;

import com.maoding.core.base.entity.BaseEntity;

public class UserTechnicalEntity extends BaseEntity implements java.io.Serializable{

    private String userId;

    private String technicalLevel;

    private String technicalMajor;

    private String technicalName;

    private String technicalNo;

    private String technicalAuthority;

    private Date technicalIssuingDate;

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

    public String getTechnicalLevel() {
        return technicalLevel;
    }

    public void setTechnicalLevel(String technicalLevel) {
        this.technicalLevel = technicalLevel == null ? null : technicalLevel.trim();
    }

    public String getTechnicalMajor() {
        return technicalMajor;
    }

    public void setTechnicalMajor(String technicalMajor) {
        this.technicalMajor = technicalMajor == null ? null : technicalMajor.trim();
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName == null ? null : technicalName.trim();
    }

    public String getTechnicalNo() {
        return technicalNo;
    }

    public void setTechnicalNo(String technicalNo) {
        this.technicalNo = technicalNo == null ? null : technicalNo.trim();
    }

    public String getTechnicalAuthority() {
        return technicalAuthority;
    }

    public void setTechnicalAuthority(String technicalAuthority) {
        this.technicalAuthority = technicalAuthority == null ? null : technicalAuthority.trim();
    }

    public Date getTechnicalIssuingDate() {
        return technicalIssuingDate;
    }

    public void setTechnicalIssuingDate(Date technicalIssuingDate) {
        this.technicalIssuingDate = technicalIssuingDate;
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