package com.maoding.org.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.io.Serializable;

/**
 * Created by Chengliang.zhang on 2017/5/6.
 */
public class InviteEntity extends BaseEntity implements Serializable {
    /**
     * 邀请类型
     * 1:邀请分支机构，2：邀请公司事业合伙人，3：邀请项目合伙人
     */
    private Integer type;
    /**
     * 发起邀请的用户ID
     */
    private String userId;
    /**
     * 发起邀请的公司ID
     */
    private String companyId;
    /**
     * 发起邀请的项目ID
     */
    private String projectId;
    /**
     * 被邀请人的电话号码
     */
    private String phone;
    /**
     * 被邀请人查看邀请的地址
     */
    private String verifyUrl;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }
}
