package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * 方法描述：保存任务流程的DTO
 * 作者：MaoSF
 * 日期：2016/10/28
 * @param:
 * @return:
 */
public class SaveProjectProcessDTO extends BaseDTO {


    private String companyId;

    private String projectId;

    private String taskManageId;

    private List<ProjectProcessDTO> processList = new ArrayList<ProjectProcessDTO>();

    private List<String>  deleteIds = new ArrayList<String>();


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

    public String getTaskManageId() {
        return taskManageId;
    }

    public void setTaskManageId(String taskManageId) {
        this.taskManageId = taskManageId;
    }

    public List<ProjectProcessDTO> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ProjectProcessDTO> processList) {
        this.processList = processList;
    }

    public List<String> getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(List<String> deleteIds) {
        this.deleteIds = deleteIds;
    }
}
