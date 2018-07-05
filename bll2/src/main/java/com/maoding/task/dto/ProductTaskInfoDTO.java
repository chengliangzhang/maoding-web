package com.maoding.task.dto;

import com.maoding.org.dto.CompanyDataDTO;
import com.maoding.projectmember.dto.ProjectMemberDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductTaskInfoDTO {

    private int isEmployeesOfCompany;

    private ProjectManagerDataDTO partB;

    private String projectCreateBy;

    private String projectName;

    private String projectCompanyId;

    private String managerId;

    private ProjectMemberDTO projectManager;

    private ProjectMemberDTO assistant;

    /**
     * 当前数据所属的companyId
     */
    private String dataCompanyId;


    private List<CompanyDataDTO> orgList = new ArrayList<>();

    private List<ProjectManagerDataDTO> projectManagerDataDTOList;

    private List<ProjectDesignTaskShow> projectDesignContentShowList;

    public int getIsEmployeesOfCompany() {
        return isEmployeesOfCompany;
    }

    public void setIsEmployeesOfCompany(int isEmployeesOfCompany) {
        this.isEmployeesOfCompany = isEmployeesOfCompany;
    }

    public ProjectManagerDataDTO getPartB() {
        return partB;
    }

    public void setPartB(ProjectManagerDataDTO partB) {
        this.partB = partB;
    }

    public String getProjectCreateBy() {
        return projectCreateBy;
    }

    public void setProjectCreateBy(String projectCreateBy) {
        this.projectCreateBy = projectCreateBy;
    }

    public String getProjectCompanyId() {
        return projectCompanyId;
    }

    public void setProjectCompanyId(String projectCompanyId) {
        this.projectCompanyId = projectCompanyId;
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

    public List<ProjectManagerDataDTO> getProjectManagerDataDTOList() {
        return projectManagerDataDTOList;
    }

    public void setProjectManagerDataDTOList(List<ProjectManagerDataDTO> projectManagerDataDTOList) {
        this.projectManagerDataDTOList = projectManagerDataDTOList;
    }

    public List<ProjectDesignTaskShow> getProjectDesignContentShowList() {
        return projectDesignContentShowList;
    }

    public void setProjectDesignContentShowList(List<ProjectDesignTaskShow> projectDesignContentShowList) {
        this.projectDesignContentShowList = projectDesignContentShowList;
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
