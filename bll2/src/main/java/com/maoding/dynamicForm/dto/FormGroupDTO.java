package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/14
 * 类名: com.maoding.dynamicForm.dto.FormGroupDTO
 * 作者: 张成亮
 * 描述: 表单群组信息
 **/
public class FormGroupDTO extends CoreShowDTO {
    /** 此组包含的自定义表单 */
    private List<FormDTO> formList;

    public List<FormDTO> getFormList() {
        return formList;
    }

    public void setFormList(List<FormDTO> formList) {
        this.formList = formList;
    }
}
