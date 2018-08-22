package com.maoding.projectcost.dto;

import java.math.BigDecimal;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostPointDTO
 * 类描述：费用节点表（记录费用在哪个节点上产生的，费用的描述，金额）
 * 项目费用收款节点表
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostPointDetailInfoDTO extends ProjectCooperatorCostDTO {

    private String id;

    /**
     * 回款金额
     */
    private BigDecimal fee;

    /**
     * 发起人
     */
    private String userName;

    /**
     * 描述
     */
    private String pointDetailDescription;

    /**
     * 发起日期
     */
    private String pointDetailDate;

    /**
     * 关联的发票id
     */
    private String invoice;

    /**
     * 费用的状态
     */
    private Integer feeStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPointDetailDate() {
        return pointDetailDate;
    }

    public void setPointDetailDate(String pointDetailDate) {
        this.pointDetailDate = pointDetailDate;
    }

    public String getPointDetailDescription() {
        return pointDetailDescription;
    }

    public void setPointDetailDescription(String pointDetailDescription) {
        this.pointDetailDescription = pointDetailDescription;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public Integer getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(Integer feeStatus) {
        this.feeStatus = feeStatus;
    }
}
