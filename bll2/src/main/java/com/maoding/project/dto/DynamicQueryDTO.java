package com.maoding.project.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/24
 * 类名: com.maoding.project.dto.ProjectNeedInfoDTO
 * 作者: 张成亮
 * 描述: 需要填充的信息
 **/
public class DynamicQueryDTO extends CoreQueryDTO {
    /** 发票类型 **/
    private int needInvoiceType;

    /** 分类子项 **/
    private int needCostTypeName;

    /** 收票方名称 **/
    private int needRelationCompanyName;

    /** 纳税识别号 **/
    private int needTaxIdNumber;

    /** 开票内容 **/
    private int needInvoiceContent;

    /** 关联项目 **/
    private int needProjectName;

    /** 合作组织信息 **/
    private int needRelationCompany;

    /** 项目状态 **/
    private int needStatus;

    /** 甲方 **/
    private int needPartA;

    /** 乙方 **/
    private int needPartB;

    /** 经营负责人 **/
    private int needBusInCharge;

    /** 经营助理 **/
    private int needBusAssistant;

    /** 设计负责人 **/
    private int needDesignInCharge;

    /** 设计助理 **/
    private int needDesignAssistant;

    /** 任务负责人 **/
    private int needTaskLeader;

    /** 设计人员 **/
    private int needDesigner;

    /** 校对人员 **/
    private int needChecker;

    /** 审核人员 **/
    private int needAuditor;

    /** 计划款项 **/
    private int needPlanCost;

    /** 实际款项 **/
    private int needRealCost;

    /** 合同回款、合同到款 **/
    private int needContractFee;

    /** 技术审查费（支出）及到款、技术审查费（收入)及付款 **/
    private int needTechnicalFee;

    /** 合作设计费（收款）及到款 **/
    private int needCooperateGainFee;

    /** 其他费油（收入）及到款 **/
    private int needOtherGainFee;

    /** 合作设计费支出及付款 **/
    private int needCooperatePayFee;

    /** 其他费用（支出）及付款 **/
    private int needOtherPayFee;

    /** 功能分类 **/
    private int needBuildType;

    public int getNeedPlanCost() {
        return needPlanCost;
    }

    public void setNeedPlanCost(int needPlanCost) {
        this.needPlanCost = needPlanCost;
    }

    public int getNeedRealCost() {
        return needRealCost;
    }

    public void setNeedRealCost(int needRealCost) {
        this.needRealCost = needRealCost;
    }

    public int getNeedCostTypeName() {
        return needCostTypeName;
    }

    public void setNeedCostTypeName(int needCostTypeName) {
        this.needCostTypeName = needCostTypeName;
    }

    public int getNeedRelationCompanyName() {
        return needRelationCompanyName;
    }

    public void setNeedRelationCompanyName(int needRelationCompanyName) {
        this.needRelationCompanyName = needRelationCompanyName;
    }

    public int getNeedTaxIdNumber() {
        return needTaxIdNumber;
    }

    public void setNeedTaxIdNumber(int needTaxIdNumber) {
        this.needTaxIdNumber = needTaxIdNumber;
    }

    public int getNeedInvoiceContent() {
        return needInvoiceContent;
    }

    public void setNeedInvoiceContent(int needInvoiceContent) {
        this.needInvoiceContent = needInvoiceContent;
    }

    public int getNeedProjectName() {
        return needProjectName;
    }

    public void setNeedProjectName(int needProjectName) {
        this.needProjectName = needProjectName;
    }

    public int getNeedInvoiceType() {
        return needInvoiceType;
    }

    public void setNeedInvoiceType(int needInvoiceType) {
        this.needInvoiceType = needInvoiceType;
    }

    public int getNeedRelationCompany() {
        return needRelationCompany;
    }

    public void setNeedRelationCompany(int needRelationCompany) {
        this.needRelationCompany = needRelationCompany;
    }

    public int getNeedStatus() {
        return needStatus;
    }

    public void setNeedStatus(int needStatus) {
        this.needStatus = needStatus;
    }

    public int getNeedPartA() {
        return needPartA;
    }

    public void setNeedPartA(int needPartA) {
        this.needPartA = needPartA;
    }

    public int getNeedPartB() {
        return needPartB;
    }

    public void setNeedPartB(int needPartB) {
        this.needPartB = needPartB;
    }

    public int getNeedBusInCharge() {
        return needBusInCharge;
    }

    public void setNeedBusInCharge(int needBusInCharge) {
        this.needBusInCharge = needBusInCharge;
    }

    public int getNeedBusAssistant() {
        return needBusAssistant;
    }

    public void setNeedBusAssistant(int needBusAssistant) {
        this.needBusAssistant = needBusAssistant;
    }

    public int getNeedDesignInCharge() {
        return needDesignInCharge;
    }

    public void setNeedDesignInCharge(int needDesignInCharge) {
        this.needDesignInCharge = needDesignInCharge;
    }

    public int getNeedDesignAssistant() {
        return needDesignAssistant;
    }

    public void setNeedDesignAssistant(int needDesignAssistant) {
        this.needDesignAssistant = needDesignAssistant;
    }

    public int getNeedTaskLeader() {
        return needTaskLeader;
    }

    public void setNeedTaskLeader(int needTaskLeader) {
        this.needTaskLeader = needTaskLeader;
    }

    public int getNeedDesigner() {
        return needDesigner;
    }

    public void setNeedDesigner(int needDesigner) {
        this.needDesigner = needDesigner;
    }

    public int getNeedChecker() {
        return needChecker;
    }

    public void setNeedChecker(int needChecker) {
        this.needChecker = needChecker;
    }

    public int getNeedAuditor() {
        return needAuditor;
    }

    public void setNeedAuditor(int needAuditor) {
        this.needAuditor = needAuditor;
    }

    public int getNeedContractFee() {
        return needContractFee;
    }

    public void setNeedContractFee(int needContractFee) {
        this.needContractFee = needContractFee;
    }

    public int getNeedTechnicalFee() {
        return needTechnicalFee;
    }

    public void setNeedTechnicalFee(int needTechnicalFee) {
        this.needTechnicalFee = needTechnicalFee;
    }

    public int getNeedCooperateGainFee() {
        return needCooperateGainFee;
    }

    public void setNeedCooperateGainFee(int needCooperateGainFee) {
        this.needCooperateGainFee = needCooperateGainFee;
    }

    public int getNeedOtherGainFee() {
        return needOtherGainFee;
    }

    public void setNeedOtherGainFee(int needOtherGainFee) {
        this.needOtherGainFee = needOtherGainFee;
    }

    public int getNeedCooperatePayFee() {
        return needCooperatePayFee;
    }

    public void setNeedCooperatePayFee(int needCooperatePayFee) {
        this.needCooperatePayFee = needCooperatePayFee;
    }

    public int getNeedOtherPayFee() {
        return needOtherPayFee;
    }

    public void setNeedOtherPayFee(int needOtherPayFee) {
        this.needOtherPayFee = needOtherPayFee;
    }

    public int getNeedBuildType() {
        return needBuildType;
    }

    public void setNeedBuildType(int needBuildType) {
        this.needBuildType = needBuildType;
    }
}
