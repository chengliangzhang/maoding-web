package com.maoding.project.dto;

/**
 * Created by Chengliang.zhang on 2017/8/15.
 */
public class ProjectCustomFieldQueryDTO {
    /** 要查询的项目自定义属性所属的公司ID */
    String companyId;
    /** 要查询的项目自定义属性所属的项目ID */
    String projectId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
