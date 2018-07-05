package com.maoding.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/7/26.
 */
public class ImportProjectDTO {
    /**
     * 项目编号
     */
    String projectNo;
    /**
     * 项目名称
     */
    String projectName;
    /**
     * 立项组织
     */
    String creatorOrgName;
    /**
     * 立项人
     */
    String creatorUserName;
    /**
     * 立项日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    Date projectCreateDate;
    /**
     * 合同签订日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    Date contractDate;
    /**
     * 省/直辖市
     */
    String province;
    /**
     * 市/直辖市区
     */
    String city;
    /**
     * 区/县
     */
    String county;
    /**
     * 详细地址
     */
    String detailAddress;
    /**
     * 项目状态
     */
    String projectStatus;
    /**
     * 甲方公司名称
     */
    String aCompanyName;
    /**
     * 乙方公司名称
     */
    String bCompanyName;
    /**
     * 导入错误原因
     */
    String errorReason;
    /**
     * 设计范围
     */
    List<ImportDesignContentDTO> designContentList;

    /**
     * 添加设计阶段
     */
    public void addDesignContent(String contentName, Date startDate, Date endDate, Date finishDate, String designOrganization) {
        if (contentName == null) return;
        addDesignContent(new ImportDesignContentDTO(contentName, startDate, endDate, finishDate, designOrganization));
    }

    public void addDesignContent(ImportDesignContentDTO content) {
        if ((content != null) && (content.getContentName() != null)) {
            if (designContentList == null) designContentList = new ArrayList<>();
            designContentList.add(content);
        }
    }

    public String getProjectNo() {
        return (projectNo != null) ? projectNo : "";
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

    public String getCreatorOrgName() {
        return (creatorOrgName != null) ? creatorOrgName : "";
    }

    public void setCreatorOrgName(String creatorOrgName) {
        this.creatorOrgName = creatorOrgName;
    }

    public String getCreatorUserName() {
        return (creatorUserName != null) ? creatorUserName : "";
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public Date getProjectCreateDate() {
        return projectCreateDate;
    }

    public void setProjectCreateDate(Date projectCreateDate) {
        this.projectCreateDate = projectCreateDate;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public String getProvince() {
        return (province != null) ? province : "";
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return (city != null) ? city : "";
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return (county != null) ? county : "";
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDetailAddress() {
        return (detailAddress != null) ? detailAddress : "";
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getaCompanyName() {
        return aCompanyName;
    }

    public void setaCompanyName(String aCompanyName) {
        this.aCompanyName = aCompanyName;
    }

    public String getbCompanyName() {
        return bCompanyName;
    }

    public void setbCompanyName(String bCompanyName) {
        this.bCompanyName = bCompanyName;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public ImportProjectDTO setErrorReason(String errorReason) {
        this.errorReason = errorReason;
        return this;
    }

    public List<ImportDesignContentDTO> getDesignContentList() {
        return designContentList;
    }

    public void setDesignContentList(List<ImportDesignContentDTO> designContentList) {
        this.designContentList = designContentList;
    }
}
