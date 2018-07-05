package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/3/23 10:38
 * 描    述 :
 */
public class FinancialAllocationDTO extends BaseDTO{

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date financialDate;

    private String reason;

    public Date getFinancialDate() {
        return financialDate;
    }

    public void setFinancialDate(Date financialDate) {
        this.financialDate = financialDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
