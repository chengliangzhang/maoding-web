package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.BaseIdObject;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/13
 * 类名: com.maoding.dynamicForm.dto.FormDetailDTO
 * 作者: 张成亮
 * 描述: 动态表单显示的控件信息
 **/
public class FormDetailDTO extends BaseIdObject {
    /** id:动态表单模板编号 **/

    /** 表单名称 **/
    private String name;

    /** 控件列表 **/
    private DynamicFormFieldDTO fieldList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DynamicFormFieldDTO getFieldList() {
        return fieldList;
    }

    public void setFieldList(DynamicFormFieldDTO fieldList) {
        this.fieldList = fieldList;
    }
}
