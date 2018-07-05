package com.maoding.financial.dto;

public class ExpFixedAmountDTO {

    private String expDate;

    private String userName;

    private String incomeAmount;

    private String expAmount;

    private String amount;//以前用的合计

    public ExpFixedAmountDTO(){

    }
    public ExpFixedAmountDTO(String expDate){
        this.expDate=expDate;
        this.userName="";
        this.incomeAmount="0";
        this.expAmount="0";
        this.amount="0";
    }
    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(String incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(String expAmount) {
        this.expAmount = expAmount;
    }

    public String getAmount() {
        amount = expAmount;
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
