package com.maoding.companybill.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class CompanyBillEntity extends BaseEntity{

    private String companyId;

    private BigDecimal fee;

    private Integer feeType;

    private Integer payType;

    private Integer feeUnit;

    private String billDescription;

    private String payerName; //付款方

    private String payeeName; //收款方

    private String projectName;

    private String paymentDate;

    private String operatorName;

    private String invoiceNo;

    private Integer deleted;

    private String paramV1;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getFeeUnit() {
        return feeUnit;
    }

    public void setFeeUnit(Integer feeUnit) {
        this.feeUnit = feeUnit;
    }

    public String getBillDescription() {
        return billDescription;
    }

    public void setBillDescription(String billDescription) {
        this.billDescription = billDescription == null ? null : billDescription.trim();
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName == null ? null : payerName.trim();
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName == null ? null : payeeName.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate == null ? null : paymentDate.trim();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo == null ? null : invoiceNo.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getParamV1() {
        return paramV1;
    }

    public void setParamV1(String paramV1) {
        this.paramV1 = paramV1 == null ? null : paramV1.trim();
    }

}