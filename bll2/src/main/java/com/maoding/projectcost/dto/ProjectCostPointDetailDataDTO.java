package com.maoding.projectcost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 项目费用详情
 * 类    名：ProjectCostDetailDTO
 * 类描述：费用收付款明显详情记录（支付方金额，收款方金额，发票信息等）
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostPointDetailDataDTO {

    private String id;

    /**
     * 当前记录所属组织id
     */
    private String companyId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 费用节点id
     */
    private String pointId;

    /**
     * 费用状态
     */
    private String feeStatus;

    /**
     * 金额
     */
    private BigDecimal fee;

    /**
     * 到款金额，已收金额
     */
    private BigDecimal paidFee;

    /**
     * 到款金额，已付金额
     */
    private BigDecimal payFee;

    /**
     * 发票信息
     */
    private String invoice;


    private String type;

    /**
     * 应收未收
     */
    private BigDecimal notReceiveFee;

    /**
     * 应付未付
     */
    private BigDecimal notPayFee;

    /**
     * 应付未付（没有添加付款金额的项。用于兼容老数据）
     */
    private BigDecimal notPayFee2;

    /**
     * 对应的收款总额
     */
    private BigDecimal paymentFee;

    /**
     * 是否可以删除（0：可删除，1：不可删除）
     */
    private int deleteFlag;

    /**
     * 审批人
     */
    private String auditPersonName;

    /**
     * 操作人
     */
    private String userName;

    /**
     * 操作人
     */
    private String userName2;

    /**
     * 操作人
     */
    private String userName3;

    private String field1;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createDate;


    private String invoiceNo;

    private BigDecimal invoiceAmount;

    private String invoiceType;


    /**
     * 权限map:
     * 4.付款（技术审查费-确认付款款（经营负责人）），
     * 5.付款（技术审查费-确认付款款（企业负责人）），
     * 6.付款（合作设计费-付款确认（经营负责人）），
     * 7.付款（合作设计费-付款确认（企业负责人）），
     * 8.到款（技术审查费-确认到款），
     * 9.到款（合作设计费-到款确认）
     * 10.到款（合同回款-到款确认）
     * 根据map中的key中对应界面每个格子的权限操作,value对应任务的id。
    */
    private Map<String,Object> roleMap = new HashMap<String,Object>();

    /**
     * 节点明细
     */
    private List<ProjectCostPaymentDetailDataDTO> paymentList= new ArrayList<ProjectCostPaymentDetailDataDTO>();

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getPaidFee() {
        return paidFee;
    }

    public void setPaidFee(BigDecimal paidFee) {
        this.paidFee = paidFee;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getNotReceiveFee() {
        if(fee!=null && paidFee!=null){
            notReceiveFee = fee.subtract(paidFee);
        }else {
            notReceiveFee = fee;
        }
        return notReceiveFee;
    }

    public void setNotReceiveFee(BigDecimal notReceiveFee) {

        this.notReceiveFee = notReceiveFee;
    }

    public BigDecimal getPayFee() {

        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
    }


    public BigDecimal getNotPayFee() {
        if(fee!=null && payFee!=null && payFee.doubleValue()>0){ //如果payFee不为null ，则feeStatus一定=1
            notPayFee = fee.subtract(payFee);
        }else {
            if( "1".equals(feeStatus)){
                notPayFee = fee;
            }else {
                notPayFee = new BigDecimal("0");//否则未付就是为0，因为审批还未审批完，不应该计入应付状态
            }
        }
        return notPayFee;
    }

    public void setNotPayFee(BigDecimal notPayFee) {
        this.notPayFee = notPayFee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName2() {
        return userName2;
    }

    public void setUserName2(String userName2) {
        this.userName2 = userName2;
    }

    public String getUserName3() {
        return userName3;
    }

    public void setUserName3(String userName3) {
        this.userName3 = userName3;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public Map<String, Object> getRoleMap() {
        return roleMap;
    }

    public void setRoleMap(Map<String, Object> roleMap) {
        this.roleMap = roleMap;
    }

    public List<ProjectCostPaymentDetailDataDTO> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<ProjectCostPaymentDetailDataDTO> paymentList) {
        this.paymentList = paymentList;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public BigDecimal getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(BigDecimal paymentFee) {
        this.paymentFee = paymentFee;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(String feeStatus) {
        this.feeStatus = feeStatus;
    }

    public String getAuditPersonName() {
        return auditPersonName;
    }

    public void setAuditPersonName(String auditPersonName) {
        this.auditPersonName = auditPersonName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getNotPayFee2() {
        if(fee!=null && payFee!=null){
            notPayFee2 = fee.subtract(payFee);
        }else {
            notPayFee2 = fee;
        }
        return notPayFee2;
    }

    public void setNotPayFee2(BigDecimal notPayFee2) {
        this.notPayFee2 = notPayFee2;
    }
}
