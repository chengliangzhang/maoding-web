package com.maoding.dynamicForm.dto;

import java.util.ArrayList;
import java.util.List;

/*
* 保存审核表单样式DTO
*/
public class DynamicFormFieldDTO extends DynamicFormFieldBaseDTO {

    List<DynamicFormFieldDTO> detailFieldList = new ArrayList<>();

    List<DynamicFormFieldSelectedValueDTO> fieldSelectedValueList = new ArrayList<>();

    public List<DynamicFormFieldDTO> getDetailFieldList() {
        return detailFieldList;
    }

    public void setDetailFieldList(List<DynamicFormFieldDTO> detailFieldList) {
        this.detailFieldList = detailFieldList;
    }

    public List<DynamicFormFieldSelectedValueDTO> getFieldSelectedValueList() {
        return fieldSelectedValueList;
    }

    public void setFieldSelectedValueList(List<DynamicFormFieldSelectedValueDTO> fieldSelectedValueList) {
        this.fieldSelectedValueList = fieldSelectedValueList;
    }
}
