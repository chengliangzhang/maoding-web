package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.math.BigDecimal;

/**
 * 深圳市设计同道技术有限公司
 * 项目费用详情
 * 类    名：ProjectCostDetailDTO
 * 类描述：费用收付款明显详情记录（支付方金额，收款方金额，发票信息等）
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostPaymentDetailDTO extends BaseDTO {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 费用节点id
     */
    private String pointDetailId;

    /**
     * 金额
     */
    private BigDecimal fee;


    /**
     * 发票信息
     */
    private String invoice;

    /**
     * 到款日期
     */
    private String paidDate;

    /**
     * 预留字段2
     */
    private String payDate;

    /**
     * 应收未收
     */
    private BigDecimal notReceiveFee;

    /**
     * 当前操作人
     */
    private String currentCompanyUserId;

    private String myTaskId;

    private int taskType;//对应的任务类型

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPointDetailId() {
        return pointDetailId;
    }

    public void setPointDetailId(String pointDetailId) {
        this.pointDetailId = pointDetailId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getNotReceiveFee() {
        return notReceiveFee;
    }

    public void setNotReceiveFee(BigDecimal notReceiveFee) {
        this.notReceiveFee = notReceiveFee;
    }

    public String getCurrentCompanyUserId() {
        return currentCompanyUserId;
    }

    public void setCurrentCompanyUserId(String currentCompanyUserId) {
        this.currentCompanyUserId = currentCompanyUserId;
    }

    public String getMyTaskId() {
        return myTaskId;
    }

    public void setMyTaskId(String myTaskId) {
        this.myTaskId = myTaskId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
}
