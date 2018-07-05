package com.maoding.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/2/23 11:41
 * 描    述 :项目收支管理
 */
public class ExpenditureDTO {
    //项目名称
    private String projectName;
    private String projectId;
    //类型
    private String projectType;

    private String projectTypeStr;
    //节点信息
    private String node;
    //收支金额
    private BigDecimal costAmount;
    //日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date costDate;
    //收入组织
    private String companyInName;
    private String companyInId;
    //支出组织
    private String companyOutName;
    private String companyOutId;

    private String errorReason;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public Date getCostDate() {
        return costDate;
    }

    public void setCostDate(Date costDate) {
        this.costDate = costDate;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectTypeStr() {
        return projectTypeStr;
    }

    public void setProjectTypeStr(String projectTypeStr) {
        this.projectTypeStr = projectTypeStr;
    }

    public String getCompanyInName() {
        return companyInName;
    }

    public void setCompanyInName(String companyInName) {
        this.companyInName = companyInName;
    }

    public String getCompanyInId() {
        return companyInId;
    }

    public void setCompanyInId(String companyInId) {
        this.companyInId = companyInId;
    }

    public String getCompanyOutName() {
        return companyOutName;
    }

    public void setCompanyOutName(String companyOutName) {
        this.companyOutName = companyOutName;
    }

    public String getCompanyOutId() {
        return companyOutId;
    }

    public void setCompanyOutId(String companyOutId) {
        this.companyOutId = companyOutId;
    }
}
