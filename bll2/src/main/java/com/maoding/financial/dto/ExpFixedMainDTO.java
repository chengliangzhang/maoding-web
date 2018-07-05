package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExpFixedMainDTO extends BaseDTO{

    /**
     * 账期
     */
    private String expDate;

    /**
     * 支出金额
     */
    private BigDecimal expAmount;

    /**
     * 收入金额
     */
    private BigDecimal incomeAmount;

    /**
     * 录入人id
     */
    private String userId;

    /**
     * 录入人姓名
     */
    private String userName;

    /**
     * 所属公司
     */
    private String companyId;

    /**
     * 所属部门
     */
    private String departId;

    /**
     * 备注
     */
    private String remark;


    /**
     * 费用项数据
     */
     List<ExpFixedDTO> fixedList = new ArrayList<>();

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate == null ? null : expDate.trim();
    }

    public BigDecimal getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(BigDecimal expAmount) {
        this.expAmount = expAmount;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId == null ? null : departId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public List<ExpFixedDTO> getFixedList() {
        return fixedList;
    }

    public void setFixedList(List<ExpFixedDTO> fixedList) {
        this.fixedList = fixedList;
    }
}
