package com.maoding.statistic.dto;

import com.maoding.companybill.entity.CompanyBalanceEntity;
import com.maoding.core.base.dto.BaseDTO;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/5/8.
 */
@Alias("StatisticDetailSummaryDTO")
public class StatisticDetailSummaryDTO extends BaseDTO implements Serializable {

    private String companyName;

    /**
     * 总收入
     */
    private BigDecimal gain;
    /**
     * 总支出
     */
    private BigDecimal pay;
    /**
     * 合计收益，当前余额
     */
    private BigDecimal amount;

    private BigDecimal sumBalance;

    /**
     * 记录总条数
     */
    private Integer total;
    /**
     * 当前明细列表
     */
    private List<StatisticDetailDTO> detailList;
    /**
     * 关联组织过滤列表
     */
    List<CompanySelectDTO> selectList;

    private CompanyBalanceEntity balance;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getGain() {
        return gain;
    }

    public void setGain(BigDecimal gain) {
        this.gain = gain;
    }

    public BigDecimal getPay() {
        return pay;
    }

    public void setPay(BigDecimal pay) {
        this.pay = pay;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<StatisticDetailDTO> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<StatisticDetailDTO> detailList) {
        this.detailList = detailList;
    }

    public List<CompanySelectDTO> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<CompanySelectDTO> selectList) {
        this.selectList = selectList;
    }

    public CompanyBalanceEntity getBalance() {
        return balance;
    }

    public void setBalance(CompanyBalanceEntity balance) {
        this.balance = balance;
    }

    public BigDecimal getSumBalance() {
        return sumBalance;
    }

    public void setSumBalance(BigDecimal sumBalance) {
        this.sumBalance = sumBalance;
    }
}
