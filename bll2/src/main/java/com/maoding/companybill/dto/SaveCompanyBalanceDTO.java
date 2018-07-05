package com.maoding.companybill.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.util.StringUtil;

import java.util.Date;

public class SaveCompanyBalanceDTO extends BaseDTO {

    /**
     * 组织id(如果不填，则从后台默认为当前组织）
     */
    private String companyId;
    /**
     * 余额初始值
     */
    private String initialBalance;
    /**
     * 最低余额值
     */
    private String lowBalance;
    /**
     * 余额初始值设置日期
     */
    private Date setBalanceDate;

    /**
     * initialBalance,lowBalance,setBalanceDate
     */
    private String type;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getInitialBalance() {
        if(StringUtil.isNullOrEmpty(initialBalance)){
            initialBalance = null;
        }
        return initialBalance;
    }

    public void setInitialBalance(String initialBalance) {
        this.initialBalance = initialBalance;
    }

    public String getLowBalance() {
        if(StringUtil.isNullOrEmpty(lowBalance)){
            lowBalance = null;
        }
        return lowBalance;
    }

    public void setLowBalance(String lowBalance) {
        this.lowBalance = lowBalance;
    }

    public Date getSetBalanceDate() {
        if(StringUtil.isNullOrEmpty(setBalanceDate)){
            setBalanceDate = null;
        }
        return setBalanceDate;
    }

    public void setSetBalanceDate(Date setBalanceDate) {
        this.setBalanceDate = setBalanceDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
