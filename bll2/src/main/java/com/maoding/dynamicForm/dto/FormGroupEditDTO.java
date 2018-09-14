package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreEditDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/14
 * 类名: com.maoding.dynamicForm.dto.FormGroupEditDTO
 * 作者: 张成亮
 * 描述: 群组编辑信息
 **/
public class FormGroupEditDTO extends CoreEditDTO {
    /** id:群组编号 **/

    /** 目标群组名称 **/
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
