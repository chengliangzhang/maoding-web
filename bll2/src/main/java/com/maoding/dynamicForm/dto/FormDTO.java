package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.BaseIdObject;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/13
 * 类名: com.maoding.dynamicForm.dto.FormDTO
 * 作者: 张成亮
 * 描述: 动态表单显示的控件信息
 **/
public class FormDTO extends BaseIdObject {
    /** id:动态表单模板编号 **/

    /** 表单名称 **/
    private String name;

    /** 控件列表 **/
    private List<DynamicFormFieldDTO> fieldList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DynamicFormFieldDTO> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DynamicFormFieldDTO> fieldList) {
        this.fieldList = fieldList;
    }
}
