package com.maoding.project.entity;

import com.maoding.core.base.entity.BaseEntity;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/25 16:28
 * 描    述 : 记录项目查询条件
 */
public class ProjectConditionEntity extends BaseEntity {
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 查询code值
     */
    private String code;
    /**
     * 类型：0：我的项目；1：项目总览
     */
    private Integer type;
    /**
     * 是否有效：0：有效：1：无效
     */
    private Integer status;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
