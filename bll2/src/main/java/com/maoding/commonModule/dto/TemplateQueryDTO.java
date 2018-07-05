package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/7/4 20:02
 * 描    述 : 模板内容查询
 */
public class TemplateQueryDTO extends BaseDTO {
    /** 名称 */
    private String name;
    /** 是否包含功能分类 */
    private Boolean isFunction;
    /** 是否包含专业信息 */
    private Boolean isMeasure;
    /** 是否包含设计范围 */
    private Boolean isRange;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFunction() {
        return isFunction;
    }

    public void setFunction(Boolean function) {
        isFunction = function;
    }

    public Boolean getMeasure() {
        return isMeasure;
    }

    public void setMeasure(Boolean measure) {
        isMeasure = measure;
    }

    public Boolean getRange() {
        return isRange;
    }

    public void setRange(Boolean range) {
        isRange = range;
    }
}
