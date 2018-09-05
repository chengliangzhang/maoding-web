package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.BaseIdObject;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/5
 * 类名: com.maoding.projectcost.dto.CostAmountDTO
 * 作者: 张成亮
 * 描述: 费用汇总
 **/
public class CostAmountDTO extends BaseIdObject {
    /** 合同回款 **/
    private Double contract;
    
    /** 技术审查费收入 **/
    private Double technicalGain;
    
    /** 合作设计费收入 **/
    private Double cooperateGain;
    
    /** 其他费用收入 **/
    private Double otherGain;
    
    /** 技术审查费支出 **/
    private Double technicalPay;
    
    /** 合作设计费支出 **/
    private Double cooperatePay;

    /** 其他费用支出 **/
    private Double otherPay;

    public Double getContract() {
        return contract;
    }

    public void setContract(Double contract) {
        this.contract = contract;
    }

    public Double getTechnicalGain() {
        return technicalGain;
    }

    public void setTechnicalGain(Double technicalGain) {
        this.technicalGain = technicalGain;
    }

    public Double getCooperateGain() {
        return cooperateGain;
    }

    public void setCooperateGain(Double cooperateGain) {
        this.cooperateGain = cooperateGain;
    }

    public Double getOtherGain() {
        return otherGain;
    }

    public void setOtherGain(Double otherGain) {
        this.otherGain = otherGain;
    }

    public Double getTechnicalPay() {
        return technicalPay;
    }

    public void setTechnicalPay(Double technicalPay) {
        this.technicalPay = technicalPay;
    }

    public Double getCooperatePay() {
        return cooperatePay;
    }

    public void setCooperatePay(Double cooperatePay) {
        this.cooperatePay = cooperatePay;
    }

    public Double getOtherPay() {
        return otherPay;
    }

    public void setOtherPay(Double otherPay) {
        this.otherPay = otherPay;
    }

}
