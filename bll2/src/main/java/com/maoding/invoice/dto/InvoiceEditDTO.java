package com.maoding.invoice.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.math.BigDecimal;

public class InvoiceEditDTO extends BaseDTO {

    private String pointDetailId;

    /**
     * 组织id
     */
    private String companyId;

    /**
     * 收票方组织id
     */
    private String relationCompanyId;

    /**
     * 收发票组织的名称（如果组织没在卯丁系统中，则直接存储名字）
     */
    private String relationCompanyName;

    /**
     * 申请日期
     */
    private String applyDate;

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 纳税识别号
     */
    private String taxIdNumber;

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
     * 开户行
     */
    private String accountBank;

    /**
     * 银行账户
     */
    private String bankNo;

    /**
     * 发票备注
     */
    private String invoiceRemark;

    /**
     * 发票类型：{增值税普通发票=1,增值税专用发票=2}
     */
    private Integer invoiceType;

    /**
     * 收件人
     */
    private String recipients;

    /**
     * 联系电话
     */
    private String cellphone;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postcode;

    /**
     * 邮箱
     */
    private String email;

    private boolean isAdd;

    public String getPointDetailId() {
        return pointDetailId;
    }

    public void setPointDetailId(String pointDetailId) {
        this.pointDetailId = pointDetailId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRelationCompanyId() {
        return relationCompanyId;
    }

    public void setRelationCompanyId(String relationCompanyId) {
        this.relationCompanyId = relationCompanyId;
    }

    public String getRelationCompanyName() {
        return relationCompanyName;
    }

    public void setRelationCompanyName(String relationCompanyName) {
        this.relationCompanyName = relationCompanyName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
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

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getTaxIdNumber() {
        return taxIdNumber;
    }

    public void setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
