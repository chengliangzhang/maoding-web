package com.maoding.dynamicForm.dto;

public class DynamicFormFieldValueDTO extends DynamicFormFieldDTO{

    //审批主表id（maoding_web_exp_main 的id）
    private String mainId;

    //值（全部用字符串类型接收）
    private String fieldValue;


    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

}
