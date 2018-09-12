package com.maoding.dynamicForm.entity;

import com.maoding.core.base.entity.BaseEntity;

public class DynamicFormFieldValueEntity extends BaseEntity{
/*
    审批主表id（maoding_web_exp_main 的id）
*/
    private String mainId;
/*
    动态字段表id（maoding_dynamic_form_field的id）
*/
    private String fieldId;
/*
    值（全部用字符串类型接收）
*/
    private String fieldValue;
/*
    删除标识
*/
    private Integer deleted;

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId == null ? null : mainId.trim();
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId == null ? null : fieldId.trim();
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue == null ? null : fieldValue.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}