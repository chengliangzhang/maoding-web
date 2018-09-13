package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveDynamicFormDTO extends BaseDTO {

    /*名称*/
    private String formName;

    /*表单类型*/
    private Integer formType;

    /*1：被启用，0：未被启用*/
    private Integer status;

    private String CompanyId;

    List<DynamicFormFieldDTO> fieldList = new ArrayList<>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public List<DynamicFormFieldDTO> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DynamicFormFieldDTO> fieldList) {
        this.fieldList = fieldList;
    }
}
