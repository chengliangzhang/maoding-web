package com.maoding.financial.dto;

public class ExpFinanceRoleDTO {

    /**
     * 财务拨款
     */
    private int financialAllocation;

    /**
     * 财务退回
     */
    private int financialRecall;

    public int getFinancialAllocation() {
        return financialAllocation;
    }

    public void setFinancialAllocation(int financialAllocation) {
        this.financialAllocation = financialAllocation;
    }

    public int getFinancialRecall() {
        return financialRecall;
    }

    public void setFinancialRecall(int financialRecall) {
        this.financialRecall = financialRecall;
    }
}
