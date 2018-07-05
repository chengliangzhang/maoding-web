package com.maoding.org.entity;

import com.maoding.core.base.entity.BaseEntity;



public class TeamOperaterEntity extends BaseEntity implements java.io.Serializable{

	/**
	 * 团队id
	 */
    private String companyId;

	/**
	 * 用户id
	 */
    private String userId;

	/**
	 * 管理员密码
	 */
    private String adminPassword;


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword == null ? null : adminPassword.trim();
    }

}