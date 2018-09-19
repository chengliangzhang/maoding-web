package com.maoding.dynamicForm.dto;

import java.util.ArrayList;
import java.util.List;

public class DynamicFormFieldValueDTO<T> extends DynamicFormFieldBaseDTO{

    //审批主表id（maoding_web_exp_main 的id）
    private String mainId;

    private String fieldId;

    private T fieldValue;

    private String fieldValuePid;

    List<DynamicFormFieldValueDTO> detailFieldList = new ArrayList<>();

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public T getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(T fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public List<DynamicFormFieldValueDTO> getDetailFieldList() {
        return detailFieldList;
    }

    public void setDetailFieldList(List<DynamicFormFieldValueDTO> detailFieldList) {
        this.detailFieldList = detailFieldList;
    }

    public String getFieldValuePid() {
        return fieldValuePid;
    }

    public void setFieldValuePid(String fieldValuePid) {
        this.fieldValuePid = fieldValuePid;
    }
}
