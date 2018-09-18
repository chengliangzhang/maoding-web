package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/13
 * 类名: com.maoding.dynamicForm.dto.FormQueryDTO
 * 作者: 张成亮
 * 描述: 动态表单内容查询条件
 **/
public class FormQueryDTO extends CoreQueryDTO {
    /** id:表单模板编号，如果为空则使用空白表单，如果不为空则加载表单上使用的控件 **/

    /** 是否加载所有控件详细信息 **/
    private Integer isLoadWidgetDetail;

    public Integer getIsLoadWidgetDetail() {
        return isLoadWidgetDetail;
    }

    public void setIsLoadWidgetDetail(Integer isLoadWidgetDetail) {
        this.isLoadWidgetDetail = isLoadWidgetDetail;
    }
}
