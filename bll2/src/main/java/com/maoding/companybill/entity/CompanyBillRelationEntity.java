package com.maoding.companybill.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.util.Date;

public class CompanyBillRelationEntity extends BaseEntity{

    private String fromCompanyId;//付款方id

    private String toCompanyId;//收款方id

    private String operatorId;//操作者id（companyUserId)

    private String projectId;//项目id

    private String targetId;//目标id(如：报销id)


    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId == null ? null : fromCompanyId.trim();
    }

    public String getToCompanyId() {
        return toCompanyId;
    }

    public void setToCompanyId(String toCompanyId) {
        this.toCompanyId = toCompanyId == null ? null : toCompanyId.trim();
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

}