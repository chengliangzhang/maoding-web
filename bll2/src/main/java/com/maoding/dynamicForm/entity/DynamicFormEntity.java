package com.maoding.dynamicForm.entity;

import com.maoding.core.base.entity.BaseEntity;

public class DynamicFormEntity extends BaseEntity{

    /** 名称 **/
    private String formName;

    /** 组织id（如果为null，则为公共的可以供每个组织选择复制的） **/
    private String companyId;

    /** 表单类型 **/
    private String formType;

    /** 排序 **/
    private Integer seq;

    /** 1：被启用，0：未被启用 **/
    private Integer status;
    
    /** 删除标识 **/
    private Integer deleted;
    
    /** 流程关键字 **/
    private String formCode;
    
    /** 所属流程编号 **/
    private String groupTypeId;
    
    /** 说明 **/
    private String documentation;
    
    /** 分条件流程时用的变量名 **/
    private String varName;
    
    /** 分条件流程时用的变量单位 **/
    private String varUnit;

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getGroupTypeId() {
        return groupTypeId;
    }

    public void setGroupTypeId(String groupTypeId) {
        this.groupTypeId = groupTypeId;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarUnit() {
        return varUnit;
    }

    public void setVarUnit(String varUnit) {
        this.varUnit = varUnit;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName == null ? null : formName.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

}