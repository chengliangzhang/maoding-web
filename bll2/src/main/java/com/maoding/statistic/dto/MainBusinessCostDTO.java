package com.maoding.statistic.dto;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/18 11:45
 * 描    述 : 主营业务税金、主营业务成本、主营业务利润DTO
 */
public class MainBusinessCostDTO {
    String[] mainBusinessTax;//主营业务税金及附加
    String[] mainBusinessTaxTotal;//主营业务成本合计
    String[] cooperationDesignFee;//合作设计费
    String[] technicalReviewFee;//技术审查费
    String[][] directProjectCosts;
    String[] otherFee;//其他支出
    String[][] laborCost;//人工成本
    //    String[] insuranceFee;//投入在项目上的员工五险一金
//    String[] bonusFee;//投入在项目上的员工奖金
    String[] mainBusinessProfit;//主营业务利润

    public String[] getMainBusinessTax() {
        return mainBusinessTax;
    }

    public void setMainBusinessTax(String[] mainBusinessTax) {
        this.mainBusinessTax = mainBusinessTax;
    }

    public String[] getMainBusinessTaxTotal() {
        return mainBusinessTaxTotal;
    }

    public void setMainBusinessTaxTotal(String[] mainBusinessTaxTotal) {
        this.mainBusinessTaxTotal = mainBusinessTaxTotal;
    }

    public String[] getCooperationDesignFee() {
        return cooperationDesignFee;
    }

    public void setCooperationDesignFee(String[] cooperationDesignFee) {
        this.cooperationDesignFee = cooperationDesignFee;
    }

    public String[] getTechnicalReviewFee() {
        return technicalReviewFee;
    }

    public void setTechnicalReviewFee(String[] technicalReviewFee) {
        this.technicalReviewFee = technicalReviewFee;
    }

    public String[] getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(String[] otherFee) {
        this.otherFee = otherFee;
    }

    public String[] getMainBusinessProfit() {
        return mainBusinessProfit;
    }

    public void setMainBusinessProfit(String[] mainBusinessProfit) {
        this.mainBusinessProfit = mainBusinessProfit;
    }

    public String[][] getDirectProjectCosts() {
        return directProjectCosts;
    }

    public void setDirectProjectCosts(String[][] directProjectCosts) {
        this.directProjectCosts = directProjectCosts;
    }

    public String[][] getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(String[][] laborCost) {
        this.laborCost = laborCost;
    }
}
