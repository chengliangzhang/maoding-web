package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveDynamicFormDTO extends BaseDTO {
/* 此dtoId,就是动态字段表id（maoding_dynamic_form的id）*/


    /** 名称 **/
    private String formName;

    /** 表单类型 **/
    private String formType;

    /** 1：被启用，0：未被启用 **/
    private Integer status;

    /** 组织id **/
    private String companyId;

    /** 说明文字 **/
    private String documentation;

    /*排序*/
    private Integer seq;

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

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }


}
