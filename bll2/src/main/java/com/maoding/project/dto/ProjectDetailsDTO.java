package com.maoding.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;
import com.maoding.system.dto.ConstDTO;
import com.maoding.system.dto.DataDictionaryDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/8/14.
 * 项目属性细节
 */
public class ProjectDetailsDTO {
    /** 项目id */
    String id;
    /** 项目编号 */
    private String projectNo;
    /** 项目名称 */
    private String projectName;
    /** 项目类型 */
    private ConstDTO projectType;
    /** 项目状态 */
    private ConstDTO projectStatus;
    /** 立项组织 */
    private CompanyDTO creatorCompany;
    /** 甲方 */
    private CompanyDTO partyACompany;
    /** 乙方 */
    private CompanyDTO partyBCompany;
    private String companyBidName; //为了维持与ProjectDTO的兼容性而保留

    /** 乙方运营负责人 */
    private ProjectMemberDTO managerOfPartyB;
    /** 乙方设计负责人 */
    private ProjectMemberDTO operatorOfPartyB;
    /** 合同签订时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date contractDate;
    /** 合同文件 */
    private String filePath;
    private String fileName;
    private List<Map<String,String>> contractList;
    /** 项目地点 */
    private LocationDTO projectLocation;
    /** 功能分类 */
    private String builtType;
    private List<DataDictionaryDTO> buildTypeList;
    private List<DataDictionaryDTO> constructionCateList;
    /** 自定义属性 */
    private List<CustomProjectPropertyDTO> projectPropertyList;
    /** 设计范围 */
    private List<ProjectDesignRangeDTO> projectRangeList;
    /** 设计内容 */
    private List<ProjectDesignContentDTO> projectDesignContentList;
    /** 项目在当前用户关注的项目列表中的关注编号 */
    private String attentionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ConstDTO getProjectType() {
        return projectType;
    }

    public void setProjectType(ConstDTO projectType) {
        this.projectType = projectType;
    }

    public ConstDTO getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ConstDTO projectStatus) {
        this.projectStatus = projectStatus;
    }

    public CompanyDTO getCreatorCompany() {
        return creatorCompany;
    }

    public void setCreatorCompany(CompanyDTO creatorCompany) {
        this.creatorCompany = creatorCompany;
    }

    public CompanyDTO getPartyACompany() {
        return partyACompany;
    }

    public void setPartyACompany(CompanyDTO partyACompany) {
        this.partyACompany = partyACompany;
    }

    public CompanyDTO getPartyBCompany() {
        return partyBCompany;
    }

    public void setPartyBCompany(CompanyDTO partyBCompany) {
        this.partyBCompany = partyBCompany;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Map<String, String>> getContractList() {
        return contractList;
    }

    public void setContractList(List<Map<String, String>> contractList) {
        this.contractList = contractList;
    }

    public LocationDTO getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(LocationDTO projectLocation) {
        this.projectLocation = projectLocation;
    }

    public List<DataDictionaryDTO> getBuildTypeList() {
        return buildTypeList;
    }

    public void setBuildTypeList(List<DataDictionaryDTO> buildTypeList) {
        this.buildTypeList = buildTypeList;
    }

    public List<DataDictionaryDTO> getConstructionCateList() {
        return constructionCateList;
    }

    public void setConstructionCateList(List<DataDictionaryDTO> constructionCateList) {
        this.constructionCateList = constructionCateList;
    }

    public List<CustomProjectPropertyDTO> getProjectPropertyList() {
        return projectPropertyList;
    }

    public void setProjectPropertyList(List<CustomProjectPropertyDTO> projectPropertyList) {
        this.projectPropertyList = projectPropertyList;
    }

    public List<ProjectDesignRangeDTO> getProjectRangeList() {
        return projectRangeList;
    }

    public void setProjectRangeList(List<ProjectDesignRangeDTO> projectRangeList) {
        this.projectRangeList = projectRangeList;
    }

    public List<ProjectDesignContentDTO> getProjectDesignContentList() {
        return projectDesignContentList;
    }

    public void setProjectDesignContentList(List<ProjectDesignContentDTO> projectDesignContentList) {
        this.projectDesignContentList = projectDesignContentList;
    }

    public String getAttentionId() {
        return attentionId;
    }

    public void setAttentionId(String attentionId) {
        this.attentionId = attentionId;
    }

    public String getBuiltType() {
        return builtType;
    }

    public void setBuiltType(String builtType) {
        this.builtType = builtType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ProjectMemberDTO getManagerOfPartyB() {
        return managerOfPartyB;
    }

    public void setManagerOfPartyB(ProjectMemberDTO managerOfPartyB) {
        this.managerOfPartyB = managerOfPartyB;
    }

    public ProjectMemberDTO getOperatorOfPartyB() {
        return operatorOfPartyB;
    }

    public void setOperatorOfPartyB(ProjectMemberDTO operatorOfPartyB) {
        this.operatorOfPartyB = operatorOfPartyB;
    }

    public String getCompanyBidName() {
        return companyBidName;
    }

    public void setCompanyBidName(String companyBidName) {
        this.companyBidName = companyBidName;
    }
}
