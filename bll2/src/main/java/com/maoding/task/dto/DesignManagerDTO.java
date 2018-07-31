package com.maoding.task.dto;

import com.maoding.org.dto.CompanyDataDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;

import java.util.ArrayList;
import java.util.List;

public class DesignManagerDTO {

    /** 设计负责人信息 */
    private ProjectMemberDTO projectManager;

    /** 设计助理信息 */
    private ProjectMemberDTO assistant;

    /** 当前数据所属的companyId */
    private String dataCompanyId;

    /** 组织信息 */
    private List<CompanyDataDTO> orgList = new ArrayList<>();

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
