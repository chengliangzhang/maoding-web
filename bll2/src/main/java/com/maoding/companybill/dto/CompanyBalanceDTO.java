package com.maoding.companybill.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CompanyBalanceDTO {

    private String id;

    /**
     * 组织id
     */
    private String companyId;

    /**
     * 组织名称
     */
    private String companyName;

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
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date setBalanceDate;

    /**
     * 当前余额
     */
    private String currentBalance;

    /**
     * 当前总收入
     */
    private String currentIncome;

    /**
     * 上月费用录入
     */
    private String lastMonthFixFee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(String initialBalance) {
        this.initialBalance = initialBalance;
    }

    public String getLowBalance() {
        return lowBalance;
    }

    public void setLowBalance(String lowBalance) {
        this.lowBalance = lowBalance;
    }

    public Date getSetBalanceDate() {
        return setBalanceDate;
    }

    public void setSetBalanceDate(Date setBalanceDate) {
        this.setBalanceDate = setBalanceDate;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(String currentIncome) {
        this.currentIncome = currentIncome;
    }

    public String getLastMonthFixFee() {
        return lastMonthFixFee;
    }

    public void setLastMonthFixFee(String lastMonthFixFee) {
        this.lastMonthFixFee = lastMonthFixFee;
    }
}
