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
    /** id:表单模板编号 **/

    /** 是否按照群组汇总：0-否，1-是 **/
    private Integer useGroup;

    /** 是否启用：0-未启用，1-启用 **/
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUseGroup() {
        return useGroup;
    }

    public void setUseGroup(Integer useGroup) {
        this.useGroup = useGroup;
    }
}
