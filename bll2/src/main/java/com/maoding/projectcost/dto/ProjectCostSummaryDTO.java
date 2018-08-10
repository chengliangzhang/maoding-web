package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.CoreDTO;

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
        return contract;
    }

    public void setContract(BigDecimal contract) {
        this.contract = contract.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getContractReal() {
        return contractReal;
    }

    public void setContractReal(BigDecimal contractReal) {
        this.contractReal = contractReal.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getDesign() {
        return design;
    }

    public void setDesign(BigDecimal design) {
        this.design = design.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getDesignReal() {
        return designReal;
    }

    public void setDesignReal(BigDecimal designReal) {
        this.designReal = designReal.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getCooperateGain() {
        return cooperateGain;
    }

    public void setCooperateGain(BigDecimal cooperateGain) {
        this.cooperateGain = cooperateGain.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getCooperateGainReal() {
        return cooperateGainReal;
    }

    public void setCooperateGainReal(BigDecimal cooperateGainReal) {
        this.cooperateGainReal = cooperateGainReal.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getGainRealSummary() {
        return gainRealSummary;
    }

    public void setGainRealSummary(BigDecimal gainRealSummary) {
        this.gainRealSummary = gainRealSummary.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getCooperatePay() {
        return cooperatePay;
    }

    public void setCooperatePay(BigDecimal cooperatePay) {
        this.cooperatePay = cooperatePay.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getCooperatePayReal() {
        return cooperatePayReal;
    }

    public void setCooperatePayReal(BigDecimal cooperatePayReal) {
        this.cooperatePayReal = cooperatePayReal.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getPayExpense() {
        return payExpense;
    }

    public void setPayExpense(BigDecimal payExpense) {
        this.payExpense = payExpense.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getPayOther() {
        return payOther;
    }

    public void setPayOther(BigDecimal payOther) {
        this.payOther = payOther.setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal getPayRealSummary() {
        return payRealSummary;
    }

    public void setPayRealSummary(BigDecimal payRealSummary) {
        this.payRealSummary = payRealSummary.setScale(6, RoundingMode.HALF_UP);
    }
}
