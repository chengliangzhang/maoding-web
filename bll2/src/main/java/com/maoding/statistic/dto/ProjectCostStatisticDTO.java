package com.maoding.statistic.dto;

import com.maoding.core.base.dto.BaseDTO;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 表示统计列表每一行数据的DTO
 * Created by Chengliang.zhang on 2017/4/27.
 */
@Alias("projectCostStatisticDTO")
public class ProjectCostStatisticDTO extends BaseDTO {
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 此类别费用总金额（各设定节点/申请款项的费用金额汇总）
     */
    private BigDecimal pointedFee;
    /**
     * 没有到账的收款费用总额
     */
    private BigDecimal unFinishedPaidFee;
    /**
     * 没有到账的付款费用总额
     */
    private BigDecimal unFinishedPayFee;
    /**
     * 已发起回款/申请付款的费用金额
     */
    private BigDecimal detailedFee;
    /**
     * 尚未发起回款/申请付款的费用金额
     */
    private BigDecimal unDetailedFee;
    /**
     * 已到款 ///支付的费用金额
     */
    private BigDecimal paidFee;
    /**
     * 支付的费用金额
     */
    private BigDecimal payFee;
    /**
     * 已发起回款/申请付款，但尚未到款的费用金额
     */
    private BigDecimal unPaidFee;
    /**
     * 已发起回款/申请付款，但尚未付款的费用金额
     */
    private BigDecimal unPayFee;
    /**
     * 最后回款日期
     */
    private Date lastPaidDate;

    /**
     * 最后支付日期
     */
    private Date lastPayDate;

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

    public BigDecimal getPointedFee() {
        return pointedFee;
    }

    public void setPointedFee(BigDecimal pointedFee) {
        this.pointedFee = pointedFee;
    }

    public BigDecimal getUnFinishedPaidFee() {
        return unFinishedPaidFee;
    }

    public void setUnFinishedPaidFee(BigDecimal unFinishedPaidFee) {
        this.unFinishedPaidFee = unFinishedPaidFee;
    }

    public BigDecimal getUnFinishedPayFee() {
        return unFinishedPayFee;
    }

    public void setUnFinishedPayFee(BigDecimal unFinishedPayFee) {
        this.unFinishedPayFee = unFinishedPayFee;
    }

    public BigDecimal getDetailedFee() {
        return detailedFee;
    }

    public void setDetailedFee(BigDecimal detailedFee) {
        this.detailedFee = detailedFee;
    }

    public BigDecimal getUnDetailedFee() {
        return unDetailedFee;
    }

    public void setUnDetailedFee(BigDecimal unDetailedFee) {
        this.unDetailedFee = unDetailedFee;
    }

    public BigDecimal getPaidFee() {
        return paidFee;
    }

    public void setPaidFee(BigDecimal paidFee) {
        this.paidFee = paidFee;
    }

    public BigDecimal getUnPaidFee() {
        return unPaidFee;
    }

    public void setUnPaidFee(BigDecimal unPaidFee) {
        this.unPaidFee = unPaidFee;
    }

    public Date getLastPaidDate() {
        return lastPaidDate;
    }

    public void setLastPaidDate(Date lastPaidDate) {
        this.lastPaidDate = lastPaidDate;
    }

    public BigDecimal getPayFee() {
        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
    }

    public BigDecimal getUnPayFee() {
        return unPayFee;
    }

    public void setUnPayFee(BigDecimal unPayFee) {
        this.unPayFee = unPayFee;
    }

    public Date getLastPayDate() {
        return lastPayDate;
    }

    public void setLastPayDate(Date lastPayDate) {
        this.lastPayDate = lastPayDate;
    }
}
