package com.maoding.dynamicForm.dto;

import com.maoding.commonModule.dto.WidgetDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/18
 * 类名: com.maoding.dynamicForm.dto.FormWithOptionalDTO
 * 作者: 张成亮
 * 描述:
 **/
public class FormWithOptionalDTO extends FormDTO {

    /** 编辑时可使用的控件列表 **/
    private List<WidgetDTO> optionalWidgetList;

    public List<WidgetDTO> getOptionalWidgetList() {
        return optionalWidgetList;
    }

    public void setOptionalWidgetList(List<WidgetDTO> optionalWidgetList) {
        this.optionalWidgetList = optionalWidgetList;
    }
}
