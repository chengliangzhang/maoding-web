package com.maoding.project.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.CoreQueryDTO;

import java.util.Date;
import java.util.List;


/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/24
 * 类名: com.maoding.project.dto.ProjectQueryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectQueryDTO extends CoreQueryDTO {
    /** 立项时间 **/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date projectCreateDateStart;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date projectCreateDateEnd;

    /** 更改为projectCreateDateStart、projectCreateDateEnd **/
    @Deprecated
    private Date createDate;

    /** 项目状态 **/
    private String status;

    /** 甲方 **/
    private String partyA;

    /** 乙方 **/
    private String partyB;

    /** 合同签订 **/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private String signDateStart;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private String signDateEnd;

    /** 立项组织 */
    private String createCompany;

    /** 使用createCompany代替 **/
    @Deprecated
    private String companyId;

    /** 项目地点 **/
    private String address;

    /** 功能分类 **/
    private List<String> buildNameList;

    /** 合作组织 **/
    private String relationCompany;

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

    /** 立项时间排序：0-正序，1-倒序 **/
    private Integer projectCreateDateOrder;

    /** 合同签订时间排序：0-正序，1-倒序 **/
    private Integer signDateOrder;

    /** 项目编号 */
    /** 目前没有定义此筛选条件 **/
    @Deprecated
    private String projectNo;

    /** 项目名称 */
    /** 目前没有定义此筛选条件 **/
    @Deprecated
    private String projectName;

    public Integer getProjectCreateDateOrder() {
        return projectCreateDateOrder;
    }

    public void setProjectCreateDateOrder(Integer projectCreateDateOrder) {
        this.projectCreateDateOrder = projectCreateDateOrder;
    }

    public Integer getSignDateOrder() {
        return signDateOrder;
    }

    public void setSignDateOrder(Integer signDateOrder) {
        this.signDateOrder = signDateOrder;
    }

    public Date getProjectCreateDateStart() {
        return projectCreateDateStart;
    }

    public void setProjectCreateDateStart(Date projectCreateDateStart) {
        this.projectCreateDateStart = projectCreateDateStart;
    }

    public Date getProjectCreateDateEnd() {
        return projectCreateDateEnd;
    }

    public void setProjectCreateDateEnd(Date projectCreateDateEnd) {
        this.projectCreateDateEnd = projectCreateDateEnd;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSignDateStart() {
        return signDateStart;
    }

    public void setSignDateStart(String signDateStart) {
        this.signDateStart = signDateStart;
    }

    public String getSignDateEnd() {
        return signDateEnd;
    }

    public void setSignDateEnd(String signDateEnd) {
        this.signDateEnd = signDateEnd;
    }

    public String getCreateCompany() {
        return createCompany;
    }

    public void setCreateCompany(String createCompany) {
        this.createCompany = createCompany;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<String> getBuildNameList() {
        return buildNameList;
    }

    public void setBuildNameList(List<String> buildNameList) {
        this.buildNameList = buildNameList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRelationCompany() {
        return relationCompany;
    }

    public void setRelationCompany(String relationCompany) {
        this.relationCompany = relationCompany;
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

    public ProjectQueryDTO(){}
    public ProjectQueryDTO(String companyId, String projectNo, String projectName, Date createDate){
        setCompanyId(companyId);
        setProjectNo(projectNo);
        setProjectName(projectName);
        setCreateDate(createDate);
        setPageSize(1);
    }

}
