package com.maoding.statistic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseDTO;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/8 9:41
 * 描    述 : 应收、应付明细（弹出层信息）
 */
@Alias("PaymentDetailDTO")
public class PaymentDetailDTO extends BaseDTO implements Serializable {
    /**
     * 到账时间
     * */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createDate;
    /**
     * 到账金额
     * */
    private BigDecimal accountFee;
    /**
     * 费用类型：1-合同回款，2-技术审查费，3-合作设计费，4-其他费用（付款），5-其他费用（收款）
     */
    private Integer feeType;
    /**
     * 费用类型中文名称
     */
    private String feeTypeName;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getAccountFee() {
        return accountFee;
    }

    public void setAccountFee(BigDecimal accountFee) {
        this.accountFee = accountFee;
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
}
