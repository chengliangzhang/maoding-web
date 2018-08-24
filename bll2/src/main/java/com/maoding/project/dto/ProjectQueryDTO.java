package com.maoding.project.dto;


import com.maoding.core.base.dto.CoreQueryDTO;

import java.util.Date;


/**
 * Created by Chengliang.zhang on 2017/7/20.
 */
public class ProjectQueryDTO extends CoreQueryDTO {
    /** 项目编号 */
    private String projectNo;
    /** 项目名称 */
    private String projectName;
    /** 立项组织 **/
    private String createCompany;
    /** 立项时间 **/
    private String projectCreateDate;
    /** 合同签订 **/
    private String signDate;
    /** 项目状态 **/
    private String status;
    /** 功能分类 **/
    private String buildName;
    /** 项目地点 **/
    private String address;
    /** 甲方 **/
    private String partyA;
    /** 乙方 **/
    private String partyB;
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
    private String contract;
    /** 合同到账信息 **/
    private String contractReal;
    /** 技术审查费计划收款 **/
    private String technicalGain;
    /** 技术审查费到账金额 **/
    private String technicalGainReal;
    /** 合作设计费计划收款 **/
    private String cooperateGain;
    /** 合作设计费到账金额 **/
    private String cooperateGainReal;
    /** 其他收入计划收款 **/
    private String otherGain;
    /** 其他收入到账金额 **/
    private String otherGainReal;
    /** 技术审查费计划付款 **/
    private String technicalPay;
    /** 技术审查费付款金额 **/
    private String technicalPayReal;
    /** 合作设计费计划付款 **/
    private String cooperatePay;
    /** 合作设计费付款金额 **/
    private String cooperatePayReal;
    /** 其他支出计划付款 **/
    private String otherPay;
    /** 其他支出付款金额 **/
    private String otherPayReal;

    /** 立项组织 */
    String companyId;
    /** 立项日期 */
    Date createDate;

    public String getCreateCompany() {
        return createCompany;
    }

    public void setCreateCompany(String createCompany) {
        this.createCompany = createCompany;
    }

    public String getProjectCreateDate() {
        return projectCreateDate;
    }

    public void setProjectCreateDate(String projectCreateDate) {
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

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getPartyB() {
        return partyB;
    }

    public void setPartyB(String partyB) {
        this.partyB = partyB;
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

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getContractReal() {
        return contractReal;
    }

    public void setContractReal(String contractReal) {
        this.contractReal = contractReal;
    }

    public String getTechnicalGain() {
        return technicalGain;
    }

    public void setTechnicalGain(String technicalGain) {
        this.technicalGain = technicalGain;
    }

    public String getTechnicalGainReal() {
        return technicalGainReal;
    }

    public void setTechnicalGainReal(String technicalGainReal) {
        this.technicalGainReal = technicalGainReal;
    }

    public String getCooperateGain() {
        return cooperateGain;
    }

    public void setCooperateGain(String cooperateGain) {
        this.cooperateGain = cooperateGain;
    }

    public String getCooperateGainReal() {
        return cooperateGainReal;
    }

    public void setCooperateGainReal(String cooperateGainReal) {
        this.cooperateGainReal = cooperateGainReal;
    }

    public String getOtherGain() {
        return otherGain;
    }

    public void setOtherGain(String otherGain) {
        this.otherGain = otherGain;
    }

    public String getOtherGainReal() {
        return otherGainReal;
    }

    public void setOtherGainReal(String otherGainReal) {
        this.otherGainReal = otherGainReal;
    }

    public String getTechnicalPay() {
        return technicalPay;
    }

    public void setTechnicalPay(String technicalPay) {
        this.technicalPay = technicalPay;
    }

    public String getTechnicalPayReal() {
        return technicalPayReal;
    }

    public void setTechnicalPayReal(String technicalPayReal) {
        this.technicalPayReal = technicalPayReal;
    }

    public String getCooperatePay() {
        return cooperatePay;
    }

    public void setCooperatePay(String cooperatePay) {
        this.cooperatePay = cooperatePay;
    }

    public String getCooperatePayReal() {
        return cooperatePayReal;
    }

    public void setCooperatePayReal(String cooperatePayReal) {
        this.cooperatePayReal = cooperatePayReal;
    }

    public String getOtherPay() {
        return otherPay;
    }

    public void setOtherPay(String otherPay) {
        this.otherPay = otherPay;
    }

    public String getOtherPayReal() {
        return otherPayReal;
    }

    public void setOtherPayReal(String otherPayReal) {
        this.otherPayReal = otherPayReal;
    }

    public ProjectQueryDTO(){}
    public ProjectQueryDTO(String companyId, String projectNo, String projectName, Date createDate){
        setCompanyId(companyId);
        setProjectNo(projectNo);
        setProjectName(projectName);
        setCreateDate(createDate);
        setPageSize(1);
    }

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
