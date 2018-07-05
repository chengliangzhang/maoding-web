package com.maoding.user.entity;

import com.maoding.core.base.entity.BaseEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：LoginUserEntity
 * 类描述：个人注册信息
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午4:47:05
 */
public class AccountEntity extends BaseEntity implements java.io.Serializable {

	/**
	 * 用户名（冗余）
	 */
	private String userName;
	
	/**
	 * 昵称
	 */
    private String nickName;
    
    /**
     * 
     */
    private String cellphone;
    /**
     * 密码
     */
    private String password;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 默认企业id
     */
    private String defaultCompanyId;
    
    /**
     * 个性签名
     */
    private String signature;
    
    /**
     * 账号状态(1:未激活，0：激活）
     */
    private String status;
    
    /**
     * 邮箱绑定(验证）格式：邮箱-验证码（email-code）
     */
    private String emialCode;

    private String activeTime;//激活时间

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getDefaultCompanyId() {
        return defaultCompanyId;
    }

    public void setDefaultCompanyId(String defaultCompanyId) {
        this.defaultCompanyId = defaultCompanyId == null ? null : defaultCompanyId.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getEmialCode() {
        return emialCode;
    }

    public void setEmialCode(String emialCode) {
        this.emialCode = emialCode == null ? null : emialCode.trim();
    }

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }
}