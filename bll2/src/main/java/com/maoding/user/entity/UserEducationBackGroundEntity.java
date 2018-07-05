package com.maoding.user.entity;

import java.util.Date;

import com.maoding.core.base.entity.BaseEntity;

public class UserEducationBackGroundEntity extends BaseEntity implements java.io.Serializable{

    private String userId;

    private String educationBackgroundStartDate;

    private String educationBackgroundEndDate;

    private String educationBackgroundSchool;

    private String educationBackgroundMajor;

    private String educationBackgroundDegrees;

    private String academicDegree;

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

    public String getEducationBackgroundStartDate() {
        return educationBackgroundStartDate;
    }

    public void setEducationBackgroundStartDate(String educationBackgroundStartDate) {
        this.educationBackgroundStartDate = educationBackgroundStartDate == null ? null : educationBackgroundStartDate.trim();
    }

    public String getEducationBackgroundEndDate() {
        return educationBackgroundEndDate;
    }

    public void setEducationBackgroundEndDate(String educationBackgroundEndDate) {
        this.educationBackgroundEndDate = educationBackgroundEndDate == null ? null : educationBackgroundEndDate.trim();
    }

    public String getEducationBackgroundSchool() {
        return educationBackgroundSchool;
    }

    public void setEducationBackgroundSchool(String educationBackgroundSchool) {
        this.educationBackgroundSchool = educationBackgroundSchool == null ? null : educationBackgroundSchool.trim();
    }

    public String getEducationBackgroundMajor() {
        return educationBackgroundMajor;
    }

    public void setEducationBackgroundMajor(String educationBackgroundMajor) {
        this.educationBackgroundMajor = educationBackgroundMajor == null ? null : educationBackgroundMajor.trim();
    }

    public String getEducationBackgroundDegrees() {
        return educationBackgroundDegrees;
    }

    public void setEducationBackgroundDegrees(String educationBackgroundDegrees) {
        this.educationBackgroundDegrees = educationBackgroundDegrees == null ? null : educationBackgroundDegrees.trim();
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree == null ? null : academicDegree.trim();
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