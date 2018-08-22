package com.maoding.invoice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.CoreDTO;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/14
 * 类名: com.maoding.invoice.dto.InvoiceDTO
 * 作者: 张成亮
 * 描述:
 **/
public class InvoiceDTO extends CoreDTO {
    /** id: 发票编号 */

    /**
     * 申请日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date applyDate;

    /**
     * 申请人姓名
     */
    private String companyUserName;

    /**
     * 发票金额（元）
     */
    private double fee;

    /**
     * 收支分类子项编号
     */
    private int costType;

    /**
     * 收支分类子项名称
     */
    private String costTypeName;

    /**
     * 款项备注
     */
    private String feeDescription;

    /**
     *  收发票组织的id
     */
    private String relationCompanyId;

    /**
     * 收发票组织的名称（如果组织没在卯丁系统中，则直接存储名字）
     */
    private String relationCompanyName;

    /**
     * 关联项目编号
     */
    private String projectId;

    /**
     * 关联项目名称
     */
    private String projectName;

    /**
     * 发票类型：{增值税普通发票=1,增值税专用发票=2}
     */
    private int invoiceType;

    /**
     * 发票类型名称：{增值税普通发票=1,增值税专用发票=2}
     */
    private String invoiceTypeName;

    /**
     * 发票号码
     */
    private String invoiceNo;

    /** 个人任务编号 **/
    private String myTaskId;

    public String getMyTaskId() {
        return myTaskId;
    }

    public void setMyTaskId(String myTaskId) {
        this.myTaskId = myTaskId;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getCompanyUserName() {
        return companyUserName;
    }

    public void setCompanyUserName(String companyUserName) {
        this.companyUserName = companyUserName;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getCostType() {
        return costType;
    }

    public void setCostType(int costType) {
        this.costType = costType;
    }

    public String getCostTypeName() {
        return costTypeName;
    }

    public void setCostTypeName(String costTypeName) {
        this.costTypeName = costTypeName;
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceTypeName() {
        return invoiceTypeName;
    }

    public void setInvoiceTypeName(String invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}
