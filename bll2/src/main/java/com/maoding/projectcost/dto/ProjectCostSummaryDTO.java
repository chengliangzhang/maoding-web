package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.CoreDTO;
import com.maoding.core.util.DigitUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/10
 * 类名: com.maoding.projectcost.dto.ProjectCostSummaryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectCostSummaryDTO extends CoreDTO {
    /** id: 项目编号 */

    /** 项目名称 */
    private String projectName;

    /** 立项组织/立项人 */
    private String projectCreator;

    /** 合同回款 */
    private BigDecimal contract;

    /** 合同回款到账金额 */
    private BigDecimal contractReal;

    /** 技术审查费/收 */
    private BigDecimal design;

    /** 技术审查费到账金额 */
    private BigDecimal designReal;

    /** 合作设计费/收 */
    private BigDecimal cooperateGain;

    /** 合作设计费到账金额 */
    private BigDecimal cooperateGainReal;

    /** 累计到账 */
    private BigDecimal gainRealSummary;

    /** 合作设计费/付 */
    private BigDecimal cooperatePay;

    /** 合作设计费付款金额 */
    private BigDecimal cooperatePayReal;

    /** 报销 */
    private BigDecimal payExpense;

    /** 费用 */
    private BigDecimal payOther;

    /** 累计付款 */
    private BigDecimal payRealSummary;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCreator() {
        return projectCreator;
    }

    public void setProjectCreator(String projectCreator) {
        this.projectCreator = projectCreator;
    }

    public BigDecimal getContract() {
        return (contract != null) ? contract : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setContract(BigDecimal contract) {
        this.contract = DigitUtils.toBigDecimal(contract,6);
    }

    public BigDecimal getContractReal() {
        return (contractReal != null) ? contractReal : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setContractReal(BigDecimal contractReal) {
        this.contractReal = DigitUtils.toBigDecimal(contractReal,6);
    }

    public BigDecimal getDesign() {
        return (design != null) ? design : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setDesign(BigDecimal design) {
        this.design = this.contract = DigitUtils.toBigDecimal(design,6);
    }

    public BigDecimal getDesignReal() {
        return (designReal != null) ? designReal : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setDesignReal(BigDecimal designReal) {
        this.designReal = this.contract = DigitUtils.toBigDecimal(designReal,6);
    }

    public BigDecimal getCooperateGain() {
        return (cooperateGain != null) ? cooperateGain : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setCooperateGain(BigDecimal cooperateGain) {
        this.cooperateGain = DigitUtils.toBigDecimal(cooperateGain,6);
    }

    public BigDecimal getCooperateGainReal() {
        return (cooperateGainReal != null) ? cooperateGainReal : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setCooperateGainReal(BigDecimal cooperateGainReal) {
        this.cooperateGainReal = DigitUtils.toBigDecimal(cooperateGainReal,6);
    }

    public BigDecimal getGainRealSummary() {
        return (gainRealSummary != null) ? gainRealSummary : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setGainRealSummary(BigDecimal gainRealSummary) {
        this.gainRealSummary = DigitUtils.toBigDecimal(gainRealSummary,6);
    }

    public BigDecimal getCooperatePay() {
        return (cooperatePay != null) ? cooperatePay : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setCooperatePay(BigDecimal cooperatePay) {
        this.cooperatePay = DigitUtils.toBigDecimal(cooperatePay,6);
    }

    public BigDecimal getCooperatePayReal() {
        return (cooperatePayReal != null) ? cooperatePayReal : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setCooperatePayReal(BigDecimal cooperatePayReal) {
        this.cooperatePayReal = DigitUtils.toBigDecimal(cooperatePayReal,6);
    }

    public BigDecimal getPayExpense() {
        return (payExpense != null) ? payExpense : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setPayExpense(BigDecimal payExpense) {
        this.payExpense = DigitUtils.toBigDecimal(payExpense,6);
    }

    public BigDecimal getPayOther() {
        return (payOther != null) ? payOther : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setPayOther(BigDecimal payOther) {
        this.payOther = DigitUtils.toBigDecimal(payOther,6);
    }

    public BigDecimal getPayRealSummary() {
        return payRealSummary;
    }

    public void setPayRealSummary(BigDecimal payRealSummary) {
        this.payRealSummary = DigitUtils.toBigDecimal(payRealSummary,6);
    }
}
