package com.maoding.user.entity;

import java.util.Date;

import com.maoding.core.base.entity.BaseEntity;

public class UserContinueEducationEntity extends BaseEntity implements java.io.Serializable{
    private String id;

    private String userId;

    private String continuingEducationStartDate;

    private String continuingEducationEndDate;

    private String continuingEducationTrainingName;

    private String continuingEducationTrainingContent;

    private String continuingEducationCardnum;

    private Float continuingEducationPeriod;

    private String continuingEducationCompany;

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

    public String getContinuingEducationStartDate() {
        return continuingEducationStartDate;
    }

    public void setContinuingEducationStartDate(String continuingEducationStartDate) {
        this.continuingEducationStartDate = continuingEducationStartDate == null ? null : continuingEducationStartDate.trim();
    }

    public String getContinuingEducationEndDate() {
        return continuingEducationEndDate;
    }

    public void setContinuingEducationEndDate(String continuingEducationEndDate) {
        this.continuingEducationEndDate = continuingEducationEndDate == null ? null : continuingEducationEndDate.trim();
    }

    public String getContinuingEducationTrainingName() {
        return continuingEducationTrainingName;
    }

    public void setContinuingEducationTrainingName(String continuingEducationTrainingName) {
        this.continuingEducationTrainingName = continuingEducationTrainingName == null ? null : continuingEducationTrainingName.trim();
    }

    public String getContinuingEducationTrainingContent() {
        return continuingEducationTrainingContent;
    }

    public void setContinuingEducationTrainingContent(String continuingEducationTrainingContent) {
        this.continuingEducationTrainingContent = continuingEducationTrainingContent == null ? null : continuingEducationTrainingContent.trim();
    }

    public String getContinuingEducationCardnum() {
        return continuingEducationCardnum;
    }

    public void setContinuingEducationCardnum(String continuingEducationCardnum) {
        this.continuingEducationCardnum = continuingEducationCardnum == null ? null : continuingEducationCardnum.trim();
    }

    public Float getContinuingEducationPeriod() {
        return continuingEducationPeriod;
    }

    public void setContinuingEducationPeriod(Float continuingEducationPeriod) {
        this.continuingEducationPeriod = continuingEducationPeriod;
    }

    public String getContinuingEducationCompany() {
        return continuingEducationCompany;
    }

    public void setContinuingEducationCompany(String continuingEducationCompany) {
        this.continuingEducationCompany = continuingEducationCompany == null ? null : continuingEducationCompany.trim();
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