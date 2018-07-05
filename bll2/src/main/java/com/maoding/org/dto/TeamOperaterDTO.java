package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;



public class TeamOperaterDTO extends BaseDTO{

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

    /**
     * 当前用户密码app端使用
     * @return
     */
    private String userPassword;
    /**
     * 新管理员密码
     */
    private String newAdminPassword;

    /**
     * 1：系统管理员，2.企业负责人
     */
    private String type;

    /**
     * 设置类型（1：移交，2：设置）
     */
    private String flag;


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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getNewAdminPassword() {
        return newAdminPassword;
    }

    public void setNewAdminPassword(String newAdminPassword) {
        this.newAdminPassword = newAdminPassword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}