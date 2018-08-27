package com.maoding.project.dto;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/24
 * 类名: com.maoding.project.dto.ProjectNeedInfoDTO
 * 作者: 张成亮
 * 描述: 需要填充的信息
 **/
public class ProjectNeedInfoDTO {
    /** 合作组织信息 **/
    private boolean isNeedRelationCompany;

    /** 项目状态 **/
    private boolean isNeedStatus;

    /** 甲方 **/
    private boolean isNeedPartA;
    
    /** 乙方 **/
    private boolean isNeedPartB;

    /** 经营负责人 **/
    private boolean isNeedBusInCharge;

    /** 经营助理 **/
    private boolean isNeedBusAssistant;

    /** 设计负责人 **/
    private boolean isNeedDesignInCharge;

    /** 设计助理 **/
    private boolean isNeedDesignAssistant;

    /** 任务负责人 **/
    private boolean isNeedTaskLeader;

    /** 设计人员 **/
    private boolean isNeedDesigner;

    /** 校对人员 **/
    private boolean isNeedChecker;

    /** 审核人员 **/
    private boolean isNeedAuditor;

    /** 合同回款、合同到款 **/
    private boolean isNeedContractFee;

    /** 技术审查费（支出）及到款、技术审查费（收入)及付款 **/
    private boolean isNeedTechnicalFee;

    /** 合作设计费（收款）及到款 **/
    private boolean isNeedCooperateGainFee;

    /** 其他费油（收入）及到款 **/
    private boolean isNeedOtherGainFee;

    /** 合作设计费支出及付款 **/
    private boolean isNeedCooperatePayFee;

    /** 其他费用（支出）及付款 **/
    private boolean isNeedOtherPayFee;

    /** 功能分类 **/
    private boolean isNeedBuildType;



    /** 需要填充基本信息 **/
    /** 由合作组织信息代替 **/
    @Deprecated
    private boolean needFillBasic;

    /** 需要填充功能分类信息 **/
    /** 由isNeedBuildType代替 **/
    @Deprecated
    private boolean needFillFunction;

    /** 需要填充合作者信息 **/
    /** 由isNeedTaskLeader等代替 **/
    @Deprecated
    private boolean needFillMember;

    /** 需要填充费用信息 **/
    /** 由isNeedOtherPayFee等代替 **/
    @Deprecated
    private boolean needFillFee;

    public boolean isNeedRelationCompany() {
        return isNeedRelationCompany;
    }

    public void setNeedRelationCompany(boolean needRelationCompany) {
        isNeedRelationCompany = needRelationCompany;
    }

    public boolean isNeedTaskLeader() {
        return isNeedTaskLeader;
    }

    public void setNeedTaskLeader(boolean needTaskLeader) {
        isNeedTaskLeader = needTaskLeader;
    }

    public boolean isNeedDesigner() {
        return isNeedDesigner;
    }

    public void setNeedDesigner(boolean needDesigner) {
        isNeedDesigner = needDesigner;
    }

    public boolean isNeedChecker() {
        return isNeedChecker;
    }

    public void setNeedChecker(boolean needChecker) {
        isNeedChecker = needChecker;
    }

    public boolean isNeedAuditor() {
        return isNeedAuditor;
    }

    public void setNeedAuditor(boolean needAuditor) {
        isNeedAuditor = needAuditor;
    }

    public boolean isNeedContractFee() {
        return isNeedContractFee;
    }

    public void setNeedContractFee(boolean needContractFee) {
        isNeedContractFee = needContractFee;
    }

    public boolean isNeedTechnicalFee() {
        return isNeedTechnicalFee;
    }

    public void setNeedTechnicalFee(boolean needTechnicalFee) {
        isNeedTechnicalFee = needTechnicalFee;
    }

    public boolean isNeedCooperateGainFee() {
        return isNeedCooperateGainFee;
    }

    public void setNeedCooperateGainFee(boolean needCooperateGainFee) {
        isNeedCooperateGainFee = needCooperateGainFee;
    }

    public boolean isNeedOtherGainFee() {
        return isNeedOtherGainFee;
    }

    public void setNeedOtherGainFee(boolean needOtherGainFee) {
        isNeedOtherGainFee = needOtherGainFee;
    }

    public boolean isNeedCooperatePayFee() {
        return isNeedCooperatePayFee;
    }

    public void setNeedCooperatePayFee(boolean needCooperatePayFee) {
        isNeedCooperatePayFee = needCooperatePayFee;
    }

    public boolean isNeedOtherPayFee() {
        return isNeedOtherPayFee;
    }

    public void setNeedOtherPayFee(boolean needOtherPayFee) {
        isNeedOtherPayFee = needOtherPayFee;
    }

    public boolean isNeedBuildType() {
        return isNeedBuildType;
    }

    public void setNeedBuildType(boolean needBuildType) {
        isNeedBuildType = needBuildType;
    }

    public boolean isNeedFillBasic() {
        return needFillBasic;
    }

    public void setNeedFillBasic(boolean needFillBasic) {
        this.needFillBasic = needFillBasic;
    }

    public boolean isNeedFillFunction() {
        return needFillFunction;
    }

    public void setNeedFillFunction(boolean needFillFunction) {
        this.needFillFunction = needFillFunction;
    }

    public boolean isNeedFillMember() {
        return needFillMember;
    }

    public void setNeedFillMember(boolean needFillMember) {
        this.needFillMember = needFillMember;
    }

    public boolean isNeedFillFee() {
        return needFillFee;
    }

    public void setNeedFillFee(boolean needFillFee) {
        this.needFillFee = needFillFee;
    }

    public boolean isNeedStatus() {
        return isNeedStatus;
    }

    public void setNeedStatus(boolean needStatus) {
        isNeedStatus = needStatus;
    }

    public boolean isNeedPartA() {
        return isNeedPartA;
    }

    public void setNeedPartA(boolean needPartA) {
        isNeedPartA = needPartA;
    }

    public boolean isNeedPartB() {
        return isNeedPartB;
    }

    public void setNeedPartB(boolean needPartB) {
        isNeedPartB = needPartB;
    }

    public boolean isNeedBusInCharge() {
        return isNeedBusInCharge;
    }

    public void setNeedBusInCharge(boolean needBusInCharge) {
        isNeedBusInCharge = needBusInCharge;
    }

    public boolean isNeedBusAssistant() {
        return isNeedBusAssistant;
    }

    public void setNeedBusAssistant(boolean needBusAssistant) {
        isNeedBusAssistant = needBusAssistant;
    }

    public boolean isNeedDesignInCharge() {
        return isNeedDesignInCharge;
    }

    public void setNeedDesignInCharge(boolean needDesignInCharge) {
        isNeedDesignInCharge = needDesignInCharge;
    }

    public boolean isNeedDesignAssistant() {
        return isNeedDesignAssistant;
    }

    public void setNeedDesignAssistant(boolean needDesignAssistant) {
        isNeedDesignAssistant = needDesignAssistant;
    }
}
