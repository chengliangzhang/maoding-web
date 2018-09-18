package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/18
 * 类名: com.maoding.commonModule.dto.WidgetDTO
 * 作者: 张成亮
 * 描述: 动态表单可使用控件的设置信息
 **/
public class WidgetDTO extends CoreShowDTO {
    /** 字段类型，见SystemParameters.DYNAMIC_FORM_FIELD_TYPE_xxx **/
    private Integer fieldType;

    /** 属性列表 **/
    private List<WidgetPropertyDTO> propertyList;

    public List<WidgetPropertyDTO> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<WidgetPropertyDTO> propertyList) {
        this.propertyList = propertyList;
    }

    public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }

}
