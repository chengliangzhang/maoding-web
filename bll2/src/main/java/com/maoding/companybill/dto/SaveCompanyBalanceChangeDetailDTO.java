package com.maoding.companybill.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.Date;

public class SaveCompanyBalanceChangeDetailDTO extends BaseDTO{

    private String balanceId;//余额主表id
    private String currentBalance;//当前余额
    private Date changeDate;//'变更日期'
    private int changeType;//变更类型：1=增加余额值，2=减少余额值
    private String changeAmount;//变更的金额
    private String changeReason;// '变更原因'
    private String beforeChangeAmount;//变更前的金额
    private String afterChangeAmount;//变更后的金额

    public String getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(String balanceId) {
        this.balanceId = balanceId;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public int getChangeType() {
        return changeType;
    }

    public void setChangeType(int changeType) {
        this.changeType = changeType;
    }

    public String getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getBeforeChangeAmount() {
        return beforeChangeAmount;
    }

    public void setBeforeChangeAmount(String beforeChangeAmount) {
        this.beforeChangeAmount = beforeChangeAmount;
    }

    public String getAfterChangeAmount() {
        return afterChangeAmount;
    }

    public void setAfterChangeAmount(String afterChangeAmount) {
        this.afterChangeAmount = afterChangeAmount;
    }
}
