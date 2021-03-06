package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreShowDTO;

import java.util.ArrayList;
import java.util.List;

public class DynamicFormFieldValueDTO extends DynamicFormFieldBaseDTO{

    //审批主表id（maoding_web_exp_main 的id）
    private String mainId;

    private String fieldId;

    /**
     * 控件的value值
     */
    private String fieldValue;
    /**
     * 控件的value值所对应的文本，因为fieldValue 有可能是id
     */
    private String fieldValueText;

    private String fieldValuePid;

    //明细中用于分组的编号
    private Integer groupNum;

    //字段值2（备注额外的值）
    private String fieldValue2;

    //参与分条件流程的字段：1=作为条件流程的字段，0=不作为条件流程的字段
    private Integer statisticalCondition;

    /**
     * 控件选择值
     */
    Object fieldSelectedValueList ;

    List<List<DynamicFormFieldValueDTO>> detailFieldList = new ArrayList<>();

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

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public List<List<DynamicFormFieldValueDTO>> getDetailFieldList() {
        return detailFieldList;
    }

    public void setDetailFieldList(List<List<DynamicFormFieldValueDTO>> detailFieldList) {
        this.detailFieldList = detailFieldList;
    }

    public String getFieldValuePid() {
        return fieldValuePid;
    }

    public void setFieldValuePid(String fieldValuePid) {
        this.fieldValuePid = fieldValuePid;
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

    public Object getFieldSelectedValueList() {
        return fieldSelectedValueList;
    }

    public void setFieldSelectedValueList(Object fieldSelectedValueList) {
        this.fieldSelectedValueList = fieldSelectedValueList;
    }

    public String getFieldValueText() {
        return fieldValueText;
    }

    public void setFieldValueText(String fieldValueText) {
        this.fieldValueText = fieldValueText;
    }

    public Integer getStatisticalCondition() {
        return statisticalCondition;
    }

    public void setStatisticalCondition(Integer statisticalCondition) {
        this.statisticalCondition = statisticalCondition;
    }
}
