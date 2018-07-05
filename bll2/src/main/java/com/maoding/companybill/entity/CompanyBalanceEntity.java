package com.maoding.companybill.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.util.Date;

public class CompanyBalanceEntity  extends BaseEntity {

    private String companyId;

    private String initialBalance;

    private String lowBalance;

    private String income;

    private String currentBalance;

    private String expense;

    private Date setBalanceDate;

    private Integer deleted;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(String initialBalance) {
        this.initialBalance = initialBalance == null ? null : initialBalance.trim();
    }

    public String getLowBalance() {
        return lowBalance;
    }

    public void setLowBalance(String lowBalance) {
        this.lowBalance = lowBalance == null ? null : lowBalance.trim();
    }

    public Date getSetBalanceDate() {
        return setBalanceDate;
    }

    public void setSetBalanceDate(Date setBalanceDate) {
        this.setBalanceDate = setBalanceDate;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }
}