package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyDTO
 * 类描述：创建分支机构DTO
 * 作    者：MaoSF
 * 日    期：2016年7月8日-上午11:47:57
 */
public class SubCompanyDTO extends BaseDTO{

    /**
     * 事业合伙人类型
     */
    private String roleType;

    private int type;//1：分公司，2：事业合伙人
    /**
     *父节点
     */
    private String pid;

    /**
     * 当前公司
     */
    private String companyId;


	/**
	 * 企业名称
	 */
    private String companyName;

    /**
     * 企业简称
     */
    private String companyShortName;

    /**
     * 负责人电话
     */
    private String cellphone;

    /**
     * 负责人姓名
     */
    private String userName;

    /**
     * 管理员密码
     */
    private String adminPassword;

    /**
     * 管理员密码(明码)
     */
    private String clearlyAdminPassword;

	/**
	 * 企业所属省
	 */
    private String province;

	/**
	 * 企业所属市
	 */
    private String city;

    /**
     * 县或镇或区
     */
    private String county;

	/**
	 * 企业地址
	 */
    private String companyAddress;

    /**
     * 服务类型
     */
    private String serverType;


    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClearlyAdminPassword() {
        return clearlyAdminPassword;
    }

    public void setClearlyAdminPassword(String clearlyAdminPassword) {
        this.clearlyAdminPassword = clearlyAdminPassword;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
