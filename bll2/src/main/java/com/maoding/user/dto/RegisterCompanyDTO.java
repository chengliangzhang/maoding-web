package com.maoding.user.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RegisterCompanyDTO
 * 类描述：企业注册DTO
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午11:26:57
 */
public class RegisterCompanyDTO extends BaseDTO{

	/******************用户注册信息**************************/
	private String id;
	
	/**
	 * 姓名
	 */
	private String userName;
	
	/**
	 * 昵称
	 */
    private String nickName;

	/**
	 * 密码
	 */
    private String password;

    /**
     * 手机号码
     */
    private String cellphone;
    
    /**
     * 验证码
     */
    private String code;//前台验证码
    
   
    /**********************公司信息*********************/
    
    private String companyId;
    /**
     * 公司名称
     */
    private String companyName;
    
    /**
     * 公司简称
     */
    private String companyShortName;
    
    /**
     * 管理员密码
     */
    private String adminPassword;
    
    /**
	 * 企业所属省
	 */
    private String province;

	/**
	 * 企业所属市
	 */
    private String city;

	/**
	 * 县或区或镇
     */
	private String county;

	/**
	 * 服务类型
     */
	private String serverType;

	/**附件id**/
	private String attachId;

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyShortName() {
		return companyShortName;
	}

	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
