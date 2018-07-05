package com.maoding.statistic.dto;

import java.math.BigDecimal;

public class ClassifiedStatisticDataDTO {

    private BigDecimal countExpenditure;//总支出

    private BigDecimal countRevenue;//总收入

    private String timeData;

    public BigDecimal getCountExpenditure() {
        return countExpenditure;
    }

    public void setCountExpenditure(BigDecimal countExpenditure) {
        this.countExpenditure = countExpenditure;
    }

    public BigDecimal getCountRevenue() {
        return countRevenue;
    }

    public void setCountRevenue(BigDecimal countRevenue) {
        this.countRevenue = countRevenue;
    }

    public String getTimeData() {
        return timeData;
    }

    public void setTimeData(String timeData) {
        this.timeData = timeData;
    }
}
