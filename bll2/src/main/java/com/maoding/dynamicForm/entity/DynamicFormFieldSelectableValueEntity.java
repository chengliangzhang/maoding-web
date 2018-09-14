package com.maoding.dynamicForm.entity;

import com.maoding.core.base.entity.BaseEntity;

public class DynamicFormFieldSelectableValueEntity extends BaseEntity{
    //动态字段表id（maoding_dynamic_form_field的id）
    private String fieldId;

    //值（例如：select 下拉框对应的value，如果前端并没有设置，后台默认为vlaue = name）
    private String selectableValue;

    //名称（显示在外面的名称）
    private String selectableName;

    //排序
    private Integer seq;

    //删除标识
    private Integer deleted;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId == null ? null : fieldId.trim();
    }

    public String getSelectableValue() {
        return selectableValue;
    }

    public void setSelectableValue(String selectableValue) {
        this.selectableValue = selectableValue == null ? null : selectableValue.trim();
    }

    public String getSelectableName() {
        return selectableName;
    }

    public void setSelectableName(String selectableName) {
        this.selectableName = selectableName == null ? null : selectableName.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

}