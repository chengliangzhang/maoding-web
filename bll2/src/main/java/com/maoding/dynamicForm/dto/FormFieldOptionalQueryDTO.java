package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/13
 * 类名: com.maoding.dynamicForm.dto.FormFieldOptionalQueryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class FormFieldOptionalQueryDTO extends CoreQueryDTO {
    /** id:选项编号 **/

    /** 字段编号 **/
    private String fieldId;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }
}
