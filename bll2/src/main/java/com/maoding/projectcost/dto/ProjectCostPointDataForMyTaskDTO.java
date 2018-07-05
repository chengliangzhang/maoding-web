package com.maoding.projectcost.dto;

import java.math.BigDecimal;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostPointDTO
 * 类描述：费用节点表（记录费用在哪个节点上产生的，费用的描述，金额）
 * 项目费用收款节点表
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostPointDataForMyTaskDTO {

    private String id;

    private String pointDetailId;
    /**
     * 任务id（只有从任务自动产生的节点才有taskId）
     */
    private String taskId;

    /**
     * 回款节点描述
     */
    private String feeDescription;

    /**
     * 回款比例
     */
    private String feeProportion;

    /**
     * 收款节点金额
     */
    private BigDecimal fee;

    /**
     * 到款、付款金额
     */
    private BigDecimal payFee;

    /**
     * 已经到款、付款金额
     */
    private BigDecimal paidFee;

    /**
     * 发起金额
     */
    private BigDecimal paymentFee;

    /**
     * 未付
     */
    private BigDecimal unpaid;

    /**
     * 类型1:合同总金额，2：技术审查费,3合作设计费付款,4.其他费用付款，5.其他费用收款
     */
    private String type;


    /**
     * 发起的金额金额
     */
    private BigDecimal pointDetailFee;

    /**
     * 操作人（发起人）
     */
    private String userName;

    /**
     * 公司名
     */
    private String companyName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPointDetailId() {
        return pointDetailId;
    }

    public void setPointDetailId(String pointDetailId) {
        this.pointDetailId = pointDetailId;
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
    }

    public String getFeeProportion() {
        return feeProportion;
    }

    public void setFeeProportion(String feeProportion) {
        this.feeProportion = feeProportion;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(BigDecimal unpaid) {
        this.unpaid = unpaid;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public BigDecimal getPayFee() {
        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
    }

    public BigDecimal getPointDetailFee() {
        return pointDetailFee;
    }

    public void setPointDetailFee(BigDecimal pointDetailFee) {
        this.pointDetailFee = pointDetailFee;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(BigDecimal paymentFee) {
        this.paymentFee = paymentFee;
    }

    public BigDecimal getPaidFee() {
        return paidFee;
    }

    public void setPaidFee(BigDecimal paidFee) {
        this.paidFee = paidFee;
    }
}
