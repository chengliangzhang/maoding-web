package com.maoding.invoice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.project.dto.DynamicQueryDTO;

import java.util.Date;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/14
 * 类名: com.maoding.invoice.dto.InvoiceQueryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class InvoiceQueryDTO extends DynamicQueryDTO {
    /** id: 发票编号 */

    /** 查询的公司编号 **/
    private String companyId;

    /** 起始日期 **/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;

    /** 终止日期 **/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;
    
    /** 分类子项 **/
    private List<String> costTypeNameList;

    /** 收票方名称 **/
    private List<String> relationCompanyNameList;
    private String relationCompanyNameLike;

    /** 申请人 **/
    private String companyUserName;
    private String companyUserNameLike;
    
    /** 发票类型 **/
    private String invoiceTypeName;
    
    /** 纳税识别号 **/
    private String taxIdNumber;
    
    /** 关联项目 **/
    private String projectName;
    private String projectNameLike;

    /** 发票号码 **/
    private String invoiceNo;
    private String invoiceNoLike;

    public String getRelationCompanyNameLike() {
        return relationCompanyNameLike;
    }

    public void setRelationCompanyNameLike(String relationCompanyNameLike) {
        this.relationCompanyNameLike = relationCompanyNameLike;
    }

    public String getCompanyUserNameLike() {
        return companyUserNameLike;
    }

    public void setCompanyUserNameLike(String companyUserNameLike) {
        this.companyUserNameLike = companyUserNameLike;
    }

    public String getProjectNameLike() {
        return projectNameLike;
    }

    public void setProjectNameLike(String projectNameLike) {
        this.projectNameLike = projectNameLike;
    }

    public String getInvoiceNoLike() {
        return invoiceNoLike;
    }

    public void setInvoiceNoLike(String invoiceNoLike) {
        this.invoiceNoLike = invoiceNoLike;
    }

    public List<String> getCostTypeNameList() {
        return costTypeNameList;
    }

    public void setCostTypeNameList(List<String> costTypeNameList) {
        this.costTypeNameList = costTypeNameList;
    }

    public List<String> getRelationCompanyNameList() {
        return relationCompanyNameList;
    }

    public void setRelationCompanyNameList(List<String> relationCompanyNameList) {
        this.relationCompanyNameList = relationCompanyNameList;
    }

    public String getCompanyUserName() {
        return companyUserName;
    }

    public void setCompanyUserName(String companyUserName) {
        this.companyUserName = companyUserName;
    }

    public String getInvoiceTypeName() {
        return invoiceTypeName;
    }

    public void setInvoiceTypeName(String invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
    }

    public String getTaxIdNumber() {
        return taxIdNumber;
    }

    public void setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
