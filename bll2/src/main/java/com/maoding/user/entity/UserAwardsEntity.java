package com.maoding.user.entity;

import java.util.Date;

import com.maoding.core.base.entity.BaseEntity;

public class UserAwardsEntity extends BaseEntity implements java.io.Serializable {

    private String userId;

    private String grantDate;

    private String awardsHonorsTitle;

    private String awardsHonorsOrganization;

    private String awardsHonorsContent;

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

    public String getGrantDate() {
        return grantDate;
    }

    public void setGrantDate(String grantDate) {
        this.grantDate = grantDate == null ? null : grantDate.trim();
    }

    public String getAwardsHonorsTitle() {
        return awardsHonorsTitle;
    }

    public void setAwardsHonorsTitle(String awardsHonorsTitle) {
        this.awardsHonorsTitle = awardsHonorsTitle == null ? null : awardsHonorsTitle.trim();
    }

    public String getAwardsHonorsOrganization() {
        return awardsHonorsOrganization;
    }

    public void setAwardsHonorsOrganization(String awardsHonorsOrganization) {
        this.awardsHonorsOrganization = awardsHonorsOrganization == null ? null : awardsHonorsOrganization.trim();
    }

    public String getAwardsHonorsContent() {
        return awardsHonorsContent;
    }

    public void setAwardsHonorsContent(String awardsHonorsContent) {
        this.awardsHonorsContent = awardsHonorsContent == null ? null : awardsHonorsContent.trim();
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