package com.maoding.companybill.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class CompanyBillDetailEntity extends BaseEntity{

    private String billId;//主记录id

    private BigDecimal fee;//该项对应的金额

    private String expTypeParentName;//费用父类型

    private String expTypeName;//费用类型

    private String feeDescription;//费用描述

    private String projectName;//项目名称

    private Integer seq;//排序

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