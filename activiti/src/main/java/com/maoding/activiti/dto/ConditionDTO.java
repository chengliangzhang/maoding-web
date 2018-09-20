package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreShowDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/20
 * 类名: com.maoding.activiti.dto.ConditionDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ConditionDTO extends CoreShowDTO {
    /** id:表单内控件编号 **/

    /** 单位 **/
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
