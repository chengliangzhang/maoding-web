package com.maoding.statistic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseDTO;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/8 9:19
 * 描    述 : 应收、应付表
 */
@Alias("ReceivableDetailDTO")
public class ReceivableDetailDTO extends BaseDTO implements Serializable {
    /**
     * 费用节点名称
     */
    private String feeName;
    /**
     * 费用所属项目ID
     */
    private String projectId;
    /**
     * 费用所属项目名称
     */
    private String projectName;
    /**
     * 费用类型：1-合同回款，2-技术审查费，3-合作设计费，4-其他费用（付款），5-其他费用（收款）
     */
    private Integer feeType;
    /**
     * 费用类型中文名称
     */
    private String feeTypeName;
    /**
     * 发起日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createDate;
    /**
     * 节点金额
     */
    private BigDecimal nodeFee;
    /**
     * 发起金额
     * */
    private BigDecimal launchFee;
    /**
     * 到账金额
     * */
    private BigDecimal accountFee;
    /**
     * 应付金额
     * */
    private BigDecimal receivableFee;
    /**
     * 到账明细
     * */
    private List<PaymentDetailDTO> paymentDetailDTOList;

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getNodeFee() {
        return nodeFee;
    }

    public void setNodeFee(BigDecimal nodeFee) {
        this.nodeFee = nodeFee;
    }

    public BigDecimal getLaunchFee() {
        return launchFee;
    }

    public void setLaunchFee(BigDecimal launchFee) {
        this.launchFee = launchFee;
    }

    public BigDecimal getAccountFee() {
        return accountFee;
    }

    public void setAccountFee(BigDecimal accountFee) {
        this.accountFee = accountFee;
    }

    public BigDecimal getReceivableFee() {
        return receivableFee;
    }

    public void setReceivableFee(BigDecimal receivableFee) {
        this.receivableFee = receivableFee;
    }

    public List<PaymentDetailDTO> getPaymentDetailDTOList() {
        return paymentDetailDTOList;
    }

    public void setPaymentDetailDTOList(List<PaymentDetailDTO> paymentDetailDTOList) {
        this.paymentDetailDTOList = paymentDetailDTOList;
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
