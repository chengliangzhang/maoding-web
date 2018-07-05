package com.maoding.org.entity;


import com.maoding.core.base.entity.BaseEntity;

public class CompanyUserAuditEntity extends BaseEntity{

    private String userName;

    private String cellphone;

    /**
     * 企业id
     */
    private String companyId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone == null ? null : cellphone.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}