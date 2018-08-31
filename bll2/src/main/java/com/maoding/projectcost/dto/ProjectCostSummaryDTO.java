package com.maoding.projectcost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseIdObject;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/10
 * 类名: com.maoding.projectcost.dto.ProjectCostSummaryDTO
 * 作者: 张成亮
 * 描述: 项目费用汇总信息
 **/
public class ProjectCostSummaryDTO extends BaseIdObject {
    /** id: 项目编号 */

    /** 项目名称 */
    private String projectName;

    /** 立项组织/立项人 */
    private String projectCreator;

    /** 立项时间 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date projectCreateDate;


    /** 合同回款 */
    private double contract;

    /** 合同回款到账金额 */
    private double contractReal;

    /** 技术审查费/收 */
    private double technical;

    /** 技术审查费到账金额 */
    private double technicalReal;

    /** 合作设计费/收 */
    private double cooperateGain;

    /** 合作设计费到账金额 */
    private double cooperateGainReal;

    /** 累计到账 */
    private double gainRealSummary;

    /** 合作设计费/付 */
    private double cooperatePay;

    /** 合作设计费付款金额 */
    private double cooperatePayReal;

    /** 报销 */
    private double payExpense;

    /** 费用 */
    private double payOther;

    /** 累计付款 */
    private double payRealSummary;

    public Date getProjectCreateDate() {
        return projectCreateDate;
    }

    public void setProjectCreateDate(Date projectCreateDate) {
        this.projectCreateDate = projectCreateDate;
    }

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

    public double getContract() {
        return contract;
    }

    public void setContract(double contract) {
        this.contract = contract;
    }

    public double getContractReal() {
        return contractReal;
    }

    public void setContractReal(double contractReal) {
        this.contractReal = contractReal;
    }

    public double getTechnical() {
        return technical;
    }

    public void setTechnical(double technical) {
        this.technical = technical;
    }

    public double getTechnicalReal() {
        return technicalReal;
    }

    public void setTechnicalReal(double technicalReal) {
        this.technicalReal = technicalReal;
    }

    public double getCooperateGain() {
        return cooperateGain;
    }

    public void setCooperateGain(double cooperateGain) {
        this.cooperateGain = cooperateGain;
    }

    public double getCooperateGainReal() {
        return cooperateGainReal;
    }

    public void setCooperateGainReal(double cooperateGainReal) {
        this.cooperateGainReal = cooperateGainReal;
    }

    public double getGainRealSummary() {
        return gainRealSummary;
    }

    public void setGainRealSummary(double gainRealSummary) {
        this.gainRealSummary = gainRealSummary;
    }

    public double getCooperatePay() {
        return cooperatePay;
    }

    public void setCooperatePay(double cooperatePay) {
        this.cooperatePay = cooperatePay;
    }

    public double getCooperatePayReal() {
        return cooperatePayReal;
    }

    public void setCooperatePayReal(double cooperatePayReal) {
        this.cooperatePayReal = cooperatePayReal;
    }

    public double getPayExpense() {
        return payExpense;
    }

    public void setPayExpense(double payExpense) {
        this.payExpense = payExpense;
    }

    public double getPayOther() {
        return payOther;
    }

    public void setPayOther(double payOther) {
        this.payOther = payOther;
    }

    public double getPayRealSummary() {
        return payRealSummary;
    }

    public void setPayRealSummary(double payRealSummary) {
        this.payRealSummary = payRealSummary;
    }
}
