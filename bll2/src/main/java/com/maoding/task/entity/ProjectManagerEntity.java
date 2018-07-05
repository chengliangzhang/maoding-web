package com.maoding.task.entity;

import com.maoding.core.base.entity.BaseEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectManagerEntity
 * 类描述：项目经营人、负责人实体
 * 作    者：MaoSF
 * 日    期：2016年12月28日-上午10:13:28
 */
public class ProjectManagerEntity extends BaseEntity{

    /**
     *项目id
     */
    private String projectId;

    /**
     *company_user所在组织id
     */
    private String companyId;

    /**
     * 1：项目经营人，2：项目负责人
     */
    private int type;

    /**
     *company_user表中的id
     */
    private String companyUserId;


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId == null ? null : companyUserId.trim();
    }

}