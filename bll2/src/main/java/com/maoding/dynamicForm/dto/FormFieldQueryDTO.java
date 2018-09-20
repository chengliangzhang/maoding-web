package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/13
 * 类名: com.maoding.dynamicForm.dto.FormFieldQueryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class FormFieldQueryDTO extends CoreQueryDTO {
    /** id:控件编号 **/

    /** 表单模板编号 **/
    private String formId;

    /** 父控件编号 **/
    private String fieldPid;

    /** 用于条件设置 **/
    private Integer toCondition;

    public Integer getToCondition() {
        return toCondition;
    }

    public void setToCondition(Integer toCondition) {
        this.toCondition = toCondition;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFieldPid() {
        return fieldPid;
    }

    public void setFieldPid(String fieldPid) {
        this.fieldPid = fieldPid;
    }

}
