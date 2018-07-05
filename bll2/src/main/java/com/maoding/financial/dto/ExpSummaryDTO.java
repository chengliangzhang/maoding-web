package com.maoding.financial.dto;

import com.maoding.org.dto.CompanyRelationDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/3/23 15:35
 * 描    述 :
 */
public class ExpSummaryDTO {
    private BigDecimal expSumAmount;
    private BigDecimal financialAllocationSumAmount;
    private List<CompanyRelationDTO> companyList;


    public List<CompanyRelationDTO> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CompanyRelationDTO> companyList) {
        this.companyList = companyList;
    }

    public ExpSummaryDTO(){
        expSumAmount = new BigDecimal("0");
        financialAllocationSumAmount = new BigDecimal("0");
        companyList = new ArrayList<>();
    }

    public BigDecimal getExpSumAmount() {
        return expSumAmount.setScale(6,BigDecimal.ROUND_HALF_UP);
    }

    public void setExpSumAmount(BigDecimal expSumAmount) {
        this.expSumAmount = expSumAmount;
    }

    public BigDecimal getFinancialAllocationSumAmount() {
        return financialAllocationSumAmount.setScale(6,BigDecimal.ROUND_HALF_UP);
    }

    public void setFinancialAllocationSumAmount(BigDecimal financialAllocationSumAmount) {
        this.financialAllocationSumAmount = financialAllocationSumAmount;
    }
}
