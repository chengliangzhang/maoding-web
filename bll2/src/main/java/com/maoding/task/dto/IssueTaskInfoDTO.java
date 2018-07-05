package com.maoding.task.dto;

import com.maoding.org.dto.CompanyDataDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/5/15.
 */
public class IssueTaskInfoDTO {
    /**
     * 项目相关组织信息
     */
    private List<ProjectManagerDataDTO> projectManagerList;
    /**
     * 签发任务列表
     */
    private List<ProjectIssueTaskDTO> contentTaskList;
    /**
     * 项目乙方数据（保持与原有签发显示信息兼容）
     */
    private ProjectManagerDataDTO partB;
    /**
     * 项目立项人（保持与原有签发显示信息兼容）
     */
    private String createBy;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 立项人姓名
     */
    private String createName;

    /**
     * 项目立项组织（保持与原有签发显示信息兼容）
     */
    private String companyId;

    /**
     * 当前数据所属的companyId
     */
    private String dataCompanyId;

    /**
     * 经营负责人id
     */
    private String managerId;

    private ProjectMemberDTO projectManager;

    private ProjectMemberDTO assistant;

    private List<CompanyDataDTO> orgList = new ArrayList<>();

    public List<ProjectManagerDataDTO> getProjectManagerList() {
        return projectManagerList;
    }

    public void setProjectManagerList(List<ProjectManagerDataDTO> projectManagerList) {
        this.projectManagerList = projectManagerList;
    }

    public List<ProjectIssueTaskDTO> getContentTaskList() {
        return contentTaskList;
    }

    public void setContentTaskList(List<ProjectIssueTaskDTO> contentTaskList) {
        this.contentTaskList = contentTaskList;
    }

    public ProjectManagerDataDTO getPartB() {
        return partB;
    }

    public void setPartB(ProjectManagerDataDTO partB) {
        this.partB = partB;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public ProjectMemberDTO getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectMemberDTO projectManager) {
        this.projectManager = projectManager;
    }

    public ProjectMemberDTO getAssistant() {
        return assistant;
    }

    public void setAssistant(ProjectMemberDTO assistant) {
        this.assistant = assistant;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDataCompanyId() {
        return dataCompanyId;
    }

    public void setDataCompanyId(String dataCompanyId) {
        this.dataCompanyId = dataCompanyId;
    }

    public List<CompanyDataDTO> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<CompanyDataDTO> orgList) {
        this.orgList = orgList;
    }
}
