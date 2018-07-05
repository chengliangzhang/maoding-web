package com.maoding.user.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RegisterAccountDTO
 * 类描述：用户注册DTO
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午9:50:37
 */
@SuppressWarnings("serial")
public class UserInfoDTO extends BaseDTO {
	
/************************用户信息******************************/
	
	/**
	 * 姓名
	 */
	private String userName;//user表中的姓名
	
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
    private String cellphone;//account表中的信息
    
    /**
     * 验证码
     */
    private String code;//前台验证码
    
    /**
     * 邮箱
     */
    private String email;//account表中的信息
    /**
     * 默认企业id
     */
    private String defaultCompanyId;
    
    
	/**
	 * 出生日期
	 */
    private String birthday;//user表中的信息
    /**
     * 性别
     */
    private String sex;//user表中的信息

	/**
	 * 头像地址
     */
	private String headImg;
    
    /**
     * 账号状态(1:未激活，0：激活）
     */
    private String status;

    private String oldPassword;

	/**
	 * 当前所在公司的公司名
     */
	private String companyName;

	/**
	 * 专业
     */
	private String major;

	/**
	 * 专业名称
     */
	private String majorName;
    
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

	public String getDefaultCompanyId() {
		return defaultCompanyId;
	}

	public void setDefaultCompanyId(String defaultCompanyId) {
		this.defaultCompanyId = defaultCompanyId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}
}
