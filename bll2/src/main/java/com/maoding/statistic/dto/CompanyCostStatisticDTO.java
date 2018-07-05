package com.maoding.statistic.dto;

import com.maoding.core.base.dto.BaseDTO;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.List;

/**
 * 表示统计列表的DTO
 * Created by Chengliang.zhang on 2017/4/27.
 */
@Alias("companyCostStatisticDTO")
public class CompanyCostStatisticDTO extends BaseDTO {

    /**
     * 公司ID
     */
    private String companyId;
    /**
     * 统计类型
     * 1:合同回款，2:技术审查费-付款，3:合作设计费-付款，4:其他费用-付款
     * 5:技术审查费-收款，6:合作设计费-收款，7:其他费用-收款
     * SystemParameters.FEE_TYPE_CONTRACT -- SystemParameters.FEE_TYPE_OTHER_DEBIT
     */
    private Integer type;
    /**
     * 项目总条数
     */
    private Integer total;
    /**
     * 项目费用列表（一页所展示的项目）
     */
    private List<ProjectCostStatisticDTO> detailList;
    /**
     * 此类别费用总金额（各设定节点/申请款项的费用金额汇总）
     */
    private BigDecimal pointedFee;
    /**
     * 没有到账的回款费用总额
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
     * 已确认到款的费用金额
     */
    private BigDecimal paidFee;
    /**
     * 已确认付款的费用金额
     */
    private BigDecimal payFee;
    /**
     * 已启动收款，但尚未确认到款日期的费用金额
     */
    private BigDecimal unPaidFee;
    /**
     * 已启动付款，但尚未确认转账日期的费用金额
     */
    private BigDecimal unPayFee;

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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public List<ProjectCostStatisticDTO> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<ProjectCostStatisticDTO> detailList) {
        this.detailList = detailList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
