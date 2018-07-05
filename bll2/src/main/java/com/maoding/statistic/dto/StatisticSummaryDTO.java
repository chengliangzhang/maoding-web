package com.maoding.statistic.dto;

import com.maoding.core.base.dto.BaseDTO;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Chengliang.zhang on 2017/5/8.
 */
@Alias("StatisticSummaryDTO")
public class StatisticSummaryDTO extends BaseDTO implements Serializable {
    private BigDecimal amount;
    private BigDecimal contract;
    private BigDecimal technical;
    private BigDecimal cooperator;
    private BigDecimal other;
    private BigDecimal expenses;
    private BigDecimal income;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getContract() {
        return contract;
    }

    public void setContract(BigDecimal contract) {
        this.contract = contract;
    }

    public BigDecimal getTechnical() {
        return technical;
    }

    public void setTechnical(BigDecimal technical) {
        this.technical = technical;
    }

    public BigDecimal getCooperator() {
        return cooperator;
    }

    public void setCooperator(BigDecimal cooperator) {
        this.cooperator = cooperator;
    }

    public BigDecimal getOther() {
        return other;
    }

    public void setOther(BigDecimal other) {
        this.other = other;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}
