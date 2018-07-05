package com.maoding.statistic.dto;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/16 17:45
 * 描    述 : 财务费用
 */
public class FinancialCostDTO {

    String financialCostTotal[];//财务费用合计
    String financialCost[][];//财务费用
    String totalProfit[];//利润总额合计
    String incomeTaxExpense[];//所得税费用
    String retainedProfits[];//净利润

    public String[] getFinancialCostTotal() {
        return financialCostTotal;
    }

    public void setFinancialCostTotal(String[] financialCostTotal) {
        this.financialCostTotal = financialCostTotal;
    }

    public String[][] getFinancialCost() {
        return financialCost;
    }

    public void setFinancialCost(String[][] financialCost) {
        this.financialCost = financialCost;
    }

    public String[] getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String[] totalProfit) {
        this.totalProfit = totalProfit;
    }

    public String[] getIncomeTaxExpense() {
        return incomeTaxExpense;
    }

    public void setIncomeTaxExpense(String[] incomeTaxExpense) {
        this.incomeTaxExpense = incomeTaxExpense;
    }

    public String[] getRetainedProfits() {
        return retainedProfits;
    }

    public void setRetainedProfits(String[] retainedProfits) {
        this.retainedProfits = retainedProfits;
    }
}
