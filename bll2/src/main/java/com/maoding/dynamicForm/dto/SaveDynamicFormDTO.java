package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveDynamicFormDTO extends BaseDTO {

    /** 名称 **/
    private String formName;

    /** 表单类型 **/
    private String formType;

    /** 1：被启用，0：未被启用 **/
    private Integer status;

    /** 组织id **/
    private String companyId;

    /** 流程关键字 **/
    private String formCode;

    /** 表单群组编号 **/
    private String groupTypeId;

    /** 说明文字 **/
    private String documentation;

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

    List<DynamicFormFieldDTO> fieldList = new ArrayList<>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public List<DynamicFormFieldDTO> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DynamicFormFieldDTO> fieldList) {
        this.fieldList = fieldList;
    }

}
