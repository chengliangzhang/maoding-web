package com.maoding.dynamicForm.dto;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/18
 * 类名: com.maoding.dynamicForm.dto.FormWithOptionalDTO
 * 作者: 张成亮
 * 描述: 带有可选控件和可选群组的表单模板编辑数据
 **/
public class FormWithOptionalDTO extends FormDTO {

    /** 编辑时可使用的控件列表 **/
    private List<DynamicFormFieldDTO> optionalWidgetList;

    /** 自定义表单群组列表 **/
    private List<FormGroupDTO> formGroupList;

    public List<DynamicFormFieldDTO> getOptionalWidgetList() {
        return optionalWidgetList;
    }

    public void setOptionalWidgetList(List<DynamicFormFieldDTO> optionalWidgetList) {
        this.optionalWidgetList = optionalWidgetList;
    }

    public List<FormGroupDTO> getFormGroupList() {
        return formGroupList;
    }

    public void setFormGroupList(List<FormGroupDTO> formGroupList) {
        this.formGroupList = formGroupList;
    }
}
