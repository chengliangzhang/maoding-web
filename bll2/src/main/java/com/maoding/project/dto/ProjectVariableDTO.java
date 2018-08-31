package com.maoding.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseIdObject;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/23
 * 类名: com.maoding.project.dto.ProjectVariableDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectVariableDTO extends BaseIdObject {
    /** id:项目编号 **/

    /** 项目编号 */
    private String projectNo;

    /** 项目名称 */
    private String projectName;

    /** 立项组织 **/
    private String createCompany;

    /** 立项时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date projectCreateDate;

    /** 合同签订 **/
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String signDate;

    /** 项目状态 **/
    private String status;

    /** 功能分类 **/
    private String buildName;

    /** 项目地点 **/
    private String address;

    /** 甲方 **/
    private String partA;

    /** 乙方 **/
    private String partB;

    /** 乙方组织编号 **/
    private String companyBid;

    /** 合作组织 **/
    private String relationCompany;

    /** 项目类型 **/
    private String projectType;

    /** 经营负责人 **/
    private String busPersonInCharge;

    /** 经营助理 **/
    private String busPersonInChargeAssistant;

    /** 设计负责人 **/
    private String designPersonInCharge;

    /** 设计助理 **/
    private String designPersonInChargeAssistant;

    /** 任务负责人 **/
    private String taskLeader;

    /** 设计人员 **/
    private String designer;

    /** 校对人员 **/
    private String checker;

    /** 审核人员 **/
    private String auditor;

    /** 合同计划收款 **/
    private double contract;

    /** 合同到账信息 **/
    private double contractReal;

    /** 技术审查费计划收款 **/
    private double technicalGain;

    /** 技术审查费到账金额 **/
    private double technicalGainReal;

    /** 合作设计费计划收款 **/
    private double cooperateGain;

    /** 合作设计费到账金额 **/
    private double cooperateGainReal;

    /** 其他收入计划收款 **/
    private double otherGain;

    /** 其他收入到账金额 **/
    private double otherGainReal;

    /** 技术审查费计划付款 **/
    private double technicalPay;

    /** 技术审查费付款金额 **/
    private double technicalPayReal;

    /** 合作设计费计划付款 **/
    private double cooperatePay;

    /** 合作设计费付款金额 **/
    private double cooperatePayReal;

    /** 其他支出计划付款 **/
    private double otherPay;

    /** 其他支出付款金额 **/
    private double otherPayReal;


    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreateCompany() {
        return createCompany;
    }

    public void setCreateCompany(String createCompany) {
        this.createCompany = createCompany;
    }

    public Date getProjectCreateDate() {
        return projectCreateDate;
    }

    public void setProjectCreateDate(Date projectCreateDate) {
        this.projectCreateDate = projectCreateDate;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPartA() {
        return partA;
    }

    public void setPartA(String partA) {
        this.partA = partA;
    }

    public String getPartB() {
        return partB;
    }

    public void setPartB(String partB) {
        this.partB = partB;
    }

    public String getCompanyBid() {
        return companyBid;
    }

    public void setCompanyBid(String companyBid) {
        this.companyBid = companyBid;
    }

    public String getRelationCompany() {
        return relationCompany;
    }

    public void setRelationCompany(String relationCompany) {
        this.relationCompany = relationCompany;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getBusPersonInCharge() {
        return busPersonInCharge;
    }

    public void setBusPersonInCharge(String busPersonInCharge) {
        this.busPersonInCharge = busPersonInCharge;
    }

    public String getBusPersonInChargeAssistant() {
        return busPersonInChargeAssistant;
    }

    public void setBusPersonInChargeAssistant(String busPersonInChargeAssistant) {
        this.busPersonInChargeAssistant = busPersonInChargeAssistant;
    }

    public String getDesignPersonInCharge() {
        return designPersonInCharge;
    }

    public void setDesignPersonInCharge(String designPersonInCharge) {
        this.designPersonInCharge = designPersonInCharge;
    }

    public String getDesignPersonInChargeAssistant() {
        return designPersonInChargeAssistant;
    }

    public void setDesignPersonInChargeAssistant(String designPersonInChargeAssistant) {
        this.designPersonInChargeAssistant = designPersonInChargeAssistant;
    }

    public String getTaskLeader() {
        return taskLeader;
    }

    public void setTaskLeader(String taskLeader) {
        this.taskLeader = taskLeader;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
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

    public double getTechnicalGain() {
        return technicalGain;
    }

    public void setTechnicalGain(double technicalGain) {
        this.technicalGain = technicalGain;
    }

    public double getTechnicalGainReal() {
        return technicalGainReal;
    }

    public void setTechnicalGainReal(double technicalGainReal) {
        this.technicalGainReal = technicalGainReal;
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

    public double getOtherGain() {
        return otherGain;
    }

    public void setOtherGain(double otherGain) {
        this.otherGain = otherGain;
    }

    public double getOtherGainReal() {
        return otherGainReal;
    }

    public void setOtherGainReal(double otherGainReal) {
        this.otherGainReal = otherGainReal;
    }

    public double getTechnicalPay() {
        return technicalPay;
    }

    public void setTechnicalPay(double technicalPay) {
        this.technicalPay = technicalPay;
    }

    public double getTechnicalPayReal() {
        return technicalPayReal;
    }

    public void setTechnicalPayReal(double technicalPayReal) {
        this.technicalPayReal = technicalPayReal;
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

    public double getOtherPay() {
        return otherPay;
    }

    public void setOtherPay(double otherPay) {
        this.otherPay = otherPay;
    }

    public double getOtherPayReal() {
        return otherPayReal;
    }

    public void setOtherPayReal(double otherPayReal) {
        this.otherPayReal = otherPayReal;
    }
}
