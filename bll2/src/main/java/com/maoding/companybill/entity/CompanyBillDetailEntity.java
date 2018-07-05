package com.maoding.companybill.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class CompanyBillDetailEntity extends BaseEntity{

    private String billId;

    private BigDecimal fee;

    private String expTypeParentName;

    private String expTypeName;

    private String feeDescription;

    private String projectName;

    private Integer seq;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId == null ? null : billId.trim();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getExpTypeParentName() {
        return expTypeParentName;
    }

    public void setExpTypeParentName(String expTypeParentName) {
        this.expTypeParentName = expTypeParentName == null ? null : expTypeParentName.trim();
    }

    public String getExpTypeName() {
        return expTypeName;
    }

    public void setExpTypeName(String expTypeName) {
        this.expTypeName = expTypeName == null ? null : expTypeName.trim();
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription == null ? null : feeDescription.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}