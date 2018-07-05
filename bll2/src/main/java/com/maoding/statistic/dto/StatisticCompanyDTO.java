package com.maoding.statistic.dto;

import java.math.BigDecimal;
import java.util.Date;

public class StatisticCompanyDTO{

    /**
     * 起始日期
     */
    private Date startDate;
    /**
     * 终止日期
     */
    private Date endDate;

    /**
     * 关联组织
     */
    private String companyId;

    private String initBalanceData;

    /**
     * 最低余额设置日期（用于当前余额使用）
     */
    private Date setBalanceDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getInitBalanceData() {
        return initBalanceData;
    }

    public void setInitBalanceData(String initBalanceData) {
        this.initBalanceData = initBalanceData;
    }

    public Date getSetBalanceDate() {
        return setBalanceDate;
    }

    public void setSetBalanceDate(Date setBalanceDate) {
        this.setBalanceDate = setBalanceDate;
    }
}
