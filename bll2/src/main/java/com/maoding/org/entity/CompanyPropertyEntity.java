package com.maoding.org.entity;

import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.BeanUtilsEx;

/**
 * Created by Chengliang.zhang on 2017/8/17.
 * 公司默认模板
 */
public class CompanyPropertyEntity extends BaseEntity{
    /** 属性模板所属公司id */
    String companyId;
    /** 属性名称 */
    String fieldName;
    /** 属性单位 */
    String unitName;
    /** 是否在最后的项目中被选中 */
    Boolean beSelected;
    /** 是否默认属性 */
    Boolean beDefault;
    /** 排序序号 */
    Integer sequencing;
    /** 删除标志 */
    Short deleted;

    public CompanyPropertyEntity(){}
    public CompanyPropertyEntity(Object obj){
        BeanUtilsEx.copyProperties(obj,this);
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Boolean getBeSelected() {
        return beSelected;
    }

    public void setBeSelected(Boolean beSelected) {
        this.beSelected = beSelected;
    }

    public Boolean getBeDefault() {
        return beDefault;
    }

    public void setBeDefault(Boolean beDefault) {
        this.beDefault = beDefault;
    }

    public Integer getSequencing() {
        return sequencing;
    }

    public void setSequencing(Integer sequencing) {
        this.sequencing = sequencing;
    }

    public Short getDeleted() {
        return deleted;
    }

    public void setDeleted(Short deleted) {
        this.deleted = deleted;
    }
}
