package com.maoding.statistic.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/13 15:40
 * 描    述 :
 */
public class ExpensesStatisticsDTO implements Serializable {
    private BigDecimal otherRevenue;//其他收支
    private BigDecimal contractRevenue;//合同汇款
    private BigDecimal cooperationRevenue;//合作设计费
    private BigDecimal technologyRevenue;//技术审查费
    private BigDecimal countRevenue;//总收入
    private BigDecimal mainBusiness;//主营业务税
    private BigDecimal managementFee;//管理费用
    private BigDecimal incomeTax;//所得税
    private BigDecimal financialCost;//财务费用
    private BigDecimal mainBusinessCost;//主营业务成本
    private BigDecimal countExpenditure;//总支出

    public ExpensesStatisticsDTO(){
        this.otherRevenue = new BigDecimal("0");
        this.contractRevenue = new BigDecimal("0");
        this.cooperationRevenue = new BigDecimal("0");
        this.technologyRevenue = new BigDecimal("0");
        this.countRevenue = new BigDecimal("0");
        this.mainBusiness = new BigDecimal("0");
        this.managementFee = new BigDecimal("0");
        this.incomeTax = new BigDecimal("0");
        this.financialCost = new BigDecimal("0");
        this.mainBusinessCost = new BigDecimal("0");
        this.countExpenditure = new BigDecimal("0");
    }

    public BigDecimal getOtherRevenue() {
        return otherRevenue;
    }

    public void setOtherRevenue(BigDecimal otherRevenue) {
        this.otherRevenue = otherRevenue;
    }

    public BigDecimal getContractRevenue() {
        return contractRevenue;
    }

    public void setContractRevenue(BigDecimal contractRevenue) {
        this.contractRevenue = contractRevenue;
    }

    public BigDecimal getCooperationRevenue() {
        return cooperationRevenue;
    }

    public void setCooperationRevenue(BigDecimal cooperationRevenue) {
        this.cooperationRevenue = cooperationRevenue;
    }

    public BigDecimal getTechnologyRevenue() {
        return technologyRevenue;
    }

    public void setTechnologyRevenue(BigDecimal technologyRevenue) {
        this.technologyRevenue = technologyRevenue;
    }

    public BigDecimal getCountRevenue() {
        return countRevenue;
    }

    public void setCountRevenue(BigDecimal countRevenue) {
        this.countRevenue = countRevenue;
    }

    public BigDecimal getMainBusiness() {
        return mainBusiness;
    }

    public void setMainBusiness(BigDecimal mainBusiness) {
        this.mainBusiness = mainBusiness;
    }

    public BigDecimal getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(BigDecimal managementFee) {
        this.managementFee = managementFee;
    }

    public BigDecimal getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(BigDecimal incomeTax) {
        this.incomeTax = incomeTax;
    }

    public BigDecimal getFinancialCost() {
        return financialCost;
    }

    public void setFinancialCost(BigDecimal financialCost) {
        this.financialCost = financialCost;
    }

    public BigDecimal getMainBusinessCost() {
        return mainBusinessCost;
    }

    public void setMainBusinessCost(BigDecimal mainBusinessCost) {
        this.mainBusinessCost = mainBusinessCost;
    }

    public BigDecimal getCountExpenditure() {
        return countExpenditure;
    }

    public void setCountExpenditure(BigDecimal countExpenditure) {
        this.countExpenditure = countExpenditure;
    }
}
