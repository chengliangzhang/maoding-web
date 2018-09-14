package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.BaseIdObject;

public class DynamicFormFieldSelectedValueDTO extends BaseIdObject{

    //动态字段表id（maoding_dynamic_form_field的id）
    private String fieldId;

    //值（例如：select 下拉框对应的value，如果前端并没有设置，后台默认为vlaue = name）
    private String selectableValue;

    //名称（显示在外面的名称）
    private String selectableName;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getSelectableValue() {
        return selectableValue;
    }

    public void setSelectableValue(String selectableValue) {
        this.selectableValue = selectableValue;
    }

    public String getSelectableName() {
        return selectableName;
    }

    public void setSelectableName(String selectableName) {
        this.selectableName = selectableName;
    }
}
