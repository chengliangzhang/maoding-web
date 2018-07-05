package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RegisterAccountDTO
 * 类描述：企业登录DTO
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午9:50:37
 */
@SuppressWarnings("serial")
public class LoginAdminDTO extends BaseDTO {
	

	/**
	 * 密码
	 */
    private String password;

    /**
     * 手机号码
     */
    private String cellphone;
    
    /**
     * 企业id
     */
    private String companyId;
    
    /**
     * 管理员密码
     */
    private String adminPassword;


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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}


    
}
