package com.maoding.financial.entity;

import com.maoding.core.base.entity.BaseEntity;

public class ExpCategoryRelationEntity extends BaseEntity {

    private String companyId;

    private String categoryTypeId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(String categoryTypeId) {
        this.categoryTypeId = categoryTypeId == null ? null : categoryTypeId.trim();
    }
}