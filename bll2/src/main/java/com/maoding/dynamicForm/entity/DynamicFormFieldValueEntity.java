package com.maoding.dynamicForm.entity;

import com.maoding.core.base.entity.BaseEntity;

public class DynamicFormFieldValueEntity extends BaseEntity{

     //审批主表id（maoding_web_exp_main 的id）
    private String mainId;

    //动态字段表id（maoding_dynamic_form_field的id）
    private String fieldId;

     //值（全部用字符串类型接收）
    private String fieldValue;

     //父记录id（maoding_dynamic_form_field_value）,如果pid不为null，则代表的是明细中的字段
    private String fieldValuePid;

     // 删除标识
    private Integer deleted;

    //明细中用于分组的编号
    private Integer groupNum;

    //字段值2（备注额外的值）
    private String fieldValue2;

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

    public void  setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue == null ? null : fieldValue.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getFieldValuePid() {
        return fieldValuePid;
    }

    public void setFieldValuePid(String fieldValuePid) {
        this.fieldValuePid = fieldValuePid;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.deleted = 0;
    }

    public Integer getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(Integer groupNum) {
        this.groupNum = groupNum;
    }

    public String getFieldValue2() {
        return fieldValue2;
    }

    public void setFieldValue2(String fieldValue2) {
        this.fieldValue2 = fieldValue2;
    }
}