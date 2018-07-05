package com.maoding.projectcost.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 深圳市设计同道技术有限公司
 * 项目费用详情
 * 类    名：ProjectCostDetailDTO
 * 类描述：费用收付款明显详情记录（支付方金额，收款方金额，发票信息等）
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostTotalDTO {


    public ProjectCostTotalDTO(){
        this.fee=new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
        this.unpaid = new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
        this.backMoney=new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
        this.toTheMoney=new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
        this.receivedUncollected=new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
        this.payTheMoney=new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
        this.payUncollected=new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
        this.paymentFee=new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
        this.feeProportion=0;

    }

    /**
     *
     */
    private BigDecimal unpaid;
    /**
     * 金额
     */
    private BigDecimal fee;

    /**
     * 回款金额
     */
    private BigDecimal backMoney;


    /**
     * 已收
     */
    private BigDecimal toTheMoney;

    /**
     * 已付
     */
    private BigDecimal payTheMoney;


    /**
     * 总应收未收
     */
    private BigDecimal receivedUncollected;

    /**
     * 总应付未付
     */
    private BigDecimal payUncollected;

    /**
     * 经营负责人收款（付款）明细总额
     */
    private BigDecimal paymentFee;

    /**
     * 回款比例
     */
    private double feeProportion;


    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getBackMoney() {
        return backMoney;
    }

    public void setBackMoney(BigDecimal backMoney) {
        this.backMoney = backMoney;
    }

    public BigDecimal getToTheMoney() {
        return toTheMoney;
    }

    public void setToTheMoney(BigDecimal toTheMoney) {
        this.toTheMoney = toTheMoney;
    }

    public BigDecimal getReceivedUncollected() {
        if(this.backMoney!=null && this.toTheMoney!=null){
            receivedUncollected =backMoney.subtract(toTheMoney);
        } else {
            receivedUncollected = backMoney;
        }
        return receivedUncollected;
    }

    public void setReceivedUncollected(BigDecimal receivedUncollected) {
        this.receivedUncollected = receivedUncollected;
    }

    public double getFeeProportion() {
        return feeProportion;
    }

    public void setFeeProportion(double feeProportion) {
        this.feeProportion = feeProportion;
    }

    public BigDecimal getUnpaid() {
        if(this.fee!=null && this.toTheMoney!=null){
            unpaid =fee.subtract(toTheMoney);
        } else {
            unpaid = fee;
        }
        return unpaid;
    }

    public void setUnpaid(BigDecimal unpaid) {
        this.unpaid = unpaid;
    }

    public BigDecimal getPayUncollected() {
        if(this.backMoney!=null && this.payTheMoney!=null){
            payUncollected =backMoney.subtract(payTheMoney);
        } else {
            payUncollected = backMoney;
        }
        return payUncollected;
    }

    public void setPayUncollected(BigDecimal payUncollected) {
        this.payUncollected = payUncollected;
    }

    public BigDecimal getPayTheMoney() {
        return payTheMoney;
    }

    public void setPayTheMoney(BigDecimal payTheMoney) {
        this.payTheMoney = payTheMoney;
    }

    public BigDecimal getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(BigDecimal paymentFee) {
        this.paymentFee = paymentFee;
    }
}
