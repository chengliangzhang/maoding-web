package com.maoding.project.dto;

/**
 * Created by Chengliang.zhang on 2017/8/15.
 */
public class ProjectFieldChangeDTO extends CustomProjectPropertyDTO{
    /** 操作者id */
    private String operatorId;
    /** 字段原始值 */
    private String originFieldValue;
    /** 操作项目的id */
    private String projectId;

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOriginFieldValue() {
        return originFieldValue;
    }

    public void setOriginFieldValue(String originFieldValue) {
        this.originFieldValue = originFieldValue;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
