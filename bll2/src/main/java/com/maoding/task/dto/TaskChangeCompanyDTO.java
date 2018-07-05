package com.maoding.task.dto;

import com.maoding.core.base.dto.BaseDTO;

public class TaskChangeCompanyDTO extends BaseDTO {

    private String projectId;

    private String companyId;

    private String changeCompanyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getChangeCompanyId() {
        return changeCompanyId;
    }

    public void setChangeCompanyId(String changeCompanyId) {
        this.changeCompanyId = changeCompanyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
