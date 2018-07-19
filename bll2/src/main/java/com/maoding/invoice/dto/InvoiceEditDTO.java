package com.maoding.invoice.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.math.BigDecimal;

public class InvoiceEditDTO extends BaseDTO {

    /**
     * 组织id
     */
    private String companyId;

    /**
     * 收发票组织的名称（如果组织没在卯丁系统中，则直接存储名字）
     */
    private String companyName;

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 发票总金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 发票形式类型（1：电子，2：纸质）
     */
    private Integer invoiceProperty;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 发票内容
     */
    private String invoiceContent;


    /**
     * 发票备注
     */
    private String invoiceRemark;

    /**
     * 发票类型：{增值税普通发票=1,增值税专用发票=2}
     */
    private Integer invoiceType;

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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Integer getInvoiceProperty() {
        return invoiceProperty;
    }

    public void setInvoiceProperty(Integer invoiceProperty) {
        this.invoiceProperty = invoiceProperty;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public String getInvoiceRemark() {
        return invoiceRemark;
    }

    public void setInvoiceRemark(String invoiceRemark) {
        this.invoiceRemark = invoiceRemark;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }
}
