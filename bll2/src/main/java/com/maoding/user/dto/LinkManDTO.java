package com.maoding.user.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：LinkManDTO
 * 类描述：LinkManDTO
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午4:46:50
 */
public class LinkManDTO{
	

	/**
	 * 人员姓名
	 */
    private String userName;

	/**
	 * 手机号
	 */
    private String cellphone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}