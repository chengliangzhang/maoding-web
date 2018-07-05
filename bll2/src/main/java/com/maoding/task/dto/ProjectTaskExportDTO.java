package com.maoding.task.dto;

import com.maoding.core.base.dto.BaseDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Chengliang.zhang on 2017/4/24.
 */
public class ProjectTaskExportDTO extends BaseDTO{
    /**
     * 项目编号
     */
    String projectId;
    /**
     * 公司编号
     */
    String companyId;
    /**
     * 公司员工称呼编号
     */
    String companyUserId;
    /**
     * 导出类型：1-仅导出自己团队任务，2-导出所有团队任务
     */
    Integer type;
    /**
     * 目标文件名
     */
    String destFileName;
    /**
     * Excel模板文件名
     */
    String templateFileName;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDestFileName() {
        return destFileName;
    }

    public void setDestFileName(String destFileName) {
        this.destFileName = destFileName;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }
}
