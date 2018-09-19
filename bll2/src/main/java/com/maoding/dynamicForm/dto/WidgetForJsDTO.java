package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/19
 * 类名: com.maoding.dynamicForm.dto.WidgetForJsDTO
 * 作者: 张成亮
 * 描述: 返回给前端的可选控件数据
 **/
public class WidgetForJsDTO extends CoreShowDTO {
    /** 字段类型，见SystemParameters.DYNAMIC_FORM_FIELD_TYPE_xxx **/
    private Integer fieldType;

    /** 图标标志 **/
    private String iconKey;

    /** 属性列表 **/
    private List<DynamicFormFieldDTO> propertyList;

    public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }

    public String getIconKey() {
        return iconKey;
    }

    public void setIconKey(String iconKey) {
        this.iconKey = iconKey;
    }

    public List<DynamicFormFieldDTO> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<DynamicFormFieldDTO> propertyList) {
        this.propertyList = propertyList;
    }
}
