package com.maoding.statistic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseDTO;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/5/8.
 */
@Alias("StatisticDetailDTO")
public class StatisticDetailDTO extends BaseDTO implements Serializable {
    /**
     * 收支日期
     */
    private String profitDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date paidDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date payDate;
    /**
     * 收支金额
     */
    private BigDecimal receiveFee;

    private BigDecimal payFee;

    private BigDecimal profitFee;

    /**
     * 收支类型：1-收，2-支
     */
    private Integer profitType;
    /**
     * 费用类型：1-合同回款，2-技术审查费，3-合作设计费，4-其他费用（付款），5-其他费用（收款）
     */
    private Integer feeType;
    /**
     * 费用类型中文名称
     */
    private String feeTypeName;
    /**
     * 付款公司ID，可能为空
     */
    private String fromCompanyId;
    /**
     * 付款公司名称，可能为空
     */
    private String fromCompanyName;
    /**
     * 收款公司ID，可能为空
     */
    private String toCompanyId;
    /**
     * 收款公司名称，可能为空
     */
    private String toCompanyName;
    /**
     * 费用所属项目ID
     */
    private String projectId;
    /**
     * 费用所属项目名称
     */
    private String projectName;

    /**
     * 费用节点名称
     */
    private String feeName;

    private String createDate;

    /**
     * 财务管理，报销汇总，code
     */
    private String pcode;//主code
    private String code;//子code

    private BigDecimal profitFeeSum;

    private Integer status;//报销列表的状态；0正常，1删除


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getProfitDate() {
        return profitDate;
    }

    public void setProfitDate(String profitDate) {
        this.profitDate = profitDate;
    }

    public BigDecimal getProfitFee() {
        if(profitType!=null && 2==profitType){
            return new BigDecimal("0").subtract(profitFee);
        }
        return profitFee;
    }

    public void setProfitFee(BigDecimal profitFee) {
        this.profitFee = profitFee;
    }

    public Integer getProfitType() {
        return profitType;
    }

    public void setProfitType(Integer profitType) {
        this.profitType = profitType;
    }

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
    }

    public String getFromCompanyName() {
        return fromCompanyName;
    }

    public void setFromCompanyName(String fromCompanyName) {
        this.fromCompanyName = fromCompanyName;
    }

    public String getToCompanyId() {
        return toCompanyId;
    }

    public void setToCompanyId(String toCompanyId) {
        this.toCompanyId = toCompanyId;
    }

    public String getToCompanyName() {
        return toCompanyName;
    }

    public void setToCompanyName(String toCompanyName) {
        this.toCompanyName = toCompanyName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public String getFeeTypeName() {
        return feeTypeName;
    }

    public void setFeeTypeName(String feeTypeName) {
        this.feeTypeName = feeTypeName;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getProfitFeeSum() {
        return profitFeeSum;
    }

    public void setProfitFeeSum(BigDecimal profitFeeSum) {
        this.profitFeeSum = profitFeeSum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getReceiveFee() {
        return receiveFee;
    }

    public void setReceiveFee(BigDecimal receiveFee) {
        this.receiveFee = receiveFee;
    }

    public BigDecimal getPayFee() {
        return payFee;
    }

    public void setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
    }
}
