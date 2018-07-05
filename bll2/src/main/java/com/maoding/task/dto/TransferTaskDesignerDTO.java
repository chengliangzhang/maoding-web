package com.maoding.task.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Idccapp21 on 2016/12/30.
 */
public class TransferTaskDesignerDTO extends BaseDTO {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 接收的人
     */
    private String companyUserId;


    private List<String> taskList=new ArrayList<String>();

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public List<String> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<String> taskList) {
        this.taskList = taskList;
    }
}
