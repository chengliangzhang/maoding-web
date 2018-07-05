package com.maoding.statistic.dto;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/18 18:53
 * 描    述 : 管理费用
 */
public class AdministrativeCostDTO {
    String executiveSalary[][];//管理人员工资
    String housingCosts[][];//房屋物业费用
    String administrativeCost[][];//行政费用
    String workingExpenses[][];//经营费用
    String amortizationAssets[][];//资产摊销
    String impairmentAsset[][];//资产减值准备
    String administrativeCostSum[];//管理费用合计
    String otherData[][];//其他费用

    public String[][] getExecutiveSalary() {
        return executiveSalary;
    }

    public void setExecutiveSalary(String[][] executiveSalary) {
        this.executiveSalary = executiveSalary;
    }

    public String[][] getHousingCosts() {
        return housingCosts;
    }

    public void setHousingCosts(String[][] housingCosts) {
        this.housingCosts = housingCosts;
    }

    public String[][] getAdministrativeCost() {
        return administrativeCost;
    }

    public void setAdministrativeCost(String[][] administrativeCost) {
        this.administrativeCost = administrativeCost;
    }

    public String[][] getWorkingExpenses() {
        return workingExpenses;
    }

    public void setWorkingExpenses(String[][] workingExpenses) {
        this.workingExpenses = workingExpenses;
    }

    public String[][] getAmortizationAssets() {
        return amortizationAssets;
    }

    public void setAmortizationAssets(String[][] amortizationAssets) {
        this.amortizationAssets = amortizationAssets;
    }

    public String[][] getImpairmentAsset() {
        return impairmentAsset;
    }

    public void setImpairmentAsset(String[][] impairmentAsset) {
        this.impairmentAsset = impairmentAsset;
    }

    public String[] getAdministrativeCostSum() {
        return administrativeCostSum;
    }

    public void setAdministrativeCostSum(String[] administrativeCostSum) {
        this.administrativeCostSum = administrativeCostSum;
    }

    public String[][] getOtherData() {
        return otherData;
    }

    public void setOtherData(String[][] otherData) {
        this.otherData = otherData;
    }
}
