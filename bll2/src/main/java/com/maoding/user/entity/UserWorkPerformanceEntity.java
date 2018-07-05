package com.maoding.user.entity;

import java.util.Date;

import com.maoding.core.base.entity.BaseEntity;

public class UserWorkPerformanceEntity extends BaseEntity implements java.io.Serializable{

    private String userId;

    private String workPerformanceStartDate;

    private String workPerformanceEndDate;

    private String workPerformanceProjectName;

    private String workPerformanceMain;

    private String selfEffect;

    private String companyBelonged;

    private String qualifications;

    private String workPerformanceWitness;

    private String witnessPhone;

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

    public String getWorkPerformanceStartDate() {
        return workPerformanceStartDate;
    }

    public void setWorkPerformanceStartDate(String workPerformanceStartDate) {
        this.workPerformanceStartDate = workPerformanceStartDate == null ? null : workPerformanceStartDate.trim();
    }

    public String getWorkPerformanceEndDate() {
        return workPerformanceEndDate;
    }

    public void setWorkPerformanceEndDate(String workPerformanceEndDate) {
        this.workPerformanceEndDate = workPerformanceEndDate == null ? null : workPerformanceEndDate.trim();
    }

    public String getWorkPerformanceProjectName() {
        return workPerformanceProjectName;
    }

    public void setWorkPerformanceProjectName(String workPerformanceProjectName) {
        this.workPerformanceProjectName = workPerformanceProjectName == null ? null : workPerformanceProjectName.trim();
    }

    public String getWorkPerformanceMain() {
        return workPerformanceMain;
    }

    public void setWorkPerformanceMain(String workPerformanceMain) {
        this.workPerformanceMain = workPerformanceMain == null ? null : workPerformanceMain.trim();
    }

    public String getSelfEffect() {
        return selfEffect;
    }

    public void setSelfEffect(String selfEffect) {
        this.selfEffect = selfEffect == null ? null : selfEffect.trim();
    }

    public String getCompanyBelonged() {
        return companyBelonged;
    }

    public void setCompanyBelonged(String companyBelonged) {
        this.companyBelonged = companyBelonged == null ? null : companyBelonged.trim();
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications == null ? null : qualifications.trim();
    }

    public String getWorkPerformanceWitness() {
        return workPerformanceWitness;
    }

    public void setWorkPerformanceWitness(String workPerformanceWitness) {
        this.workPerformanceWitness = workPerformanceWitness == null ? null : workPerformanceWitness.trim();
    }

    public String getWitnessPhone() {
        return witnessPhone;
    }

    public void setWitnessPhone(String witnessPhone) {
        this.witnessPhone = witnessPhone == null ? null : witnessPhone.trim();
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