package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/19
 * 类名: com.maoding.dynamicForm.dto.FormGroupQueryDTO
 * 作者: 张成亮
 * 描述: 动态表单群组查询条件
 **/
public class FormGroupQueryDTO extends CoreQueryDTO {
    /** id:群组编号 **/

    /** 是否包含表单 **/
    private Integer isIncludeForm;

    public Integer getIsIncludeForm() {
        return isIncludeForm;
    }

    public void setIsIncludeForm(Integer isIncludeForm) {
        this.isIncludeForm = isIncludeForm;
    }
}
