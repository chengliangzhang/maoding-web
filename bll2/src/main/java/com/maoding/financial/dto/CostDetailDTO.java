package com.maoding.financial.dto;

import java.math.BigDecimal;

public class CostDetailDTO {

    /**
     * 款项金额
     */
    private BigDecimal expAmount;

    /**
     *  支出类别名称
     */
    private String expName;

    /**
     * 款项用途
     */
    private String expUse;

    public BigDecimal getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(BigDecimal expAmount) {
        this.expAmount = expAmount;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getExpUse() {
        return expUse;
    }

    public void setExpUse(String expUse) {
        this.expUse = expUse;
    }
}
