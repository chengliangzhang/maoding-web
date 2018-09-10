package com.maoding.companybill.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.util.Date;

public class CompanyBalanceChangeDetailEntity extends BaseEntity{
    private String id;

    private String balanceId;

    private String beforeChangeAmount;

    private String afterChangeAmount;

    private String changeAmount;

    private Integer changeType;

    private String changeReason;

    private String remark;

    private Date changeDate;

    private String operatorId;

    private Integer deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(String balanceId) {
        this.balanceId = balanceId == null ? null : balanceId.trim();
    }

    public String getBeforeChangeAmount() {
        return beforeChangeAmount;
    }

    public void setBeforeChangeAmount(String beforeChangeAmount) {
        this.beforeChangeAmount = beforeChangeAmount == null ? null : beforeChangeAmount.trim();
    }

    public String getAfterChangeAmount() {
        return afterChangeAmount;
    }

    public void setAfterChangeAmount(String afterChangeAmount) {
        this.afterChangeAmount = afterChangeAmount == null ? null : afterChangeAmount.trim();
    }

    public String getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount == null ? null : changeAmount.trim();
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason == null ? null : changeReason.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}