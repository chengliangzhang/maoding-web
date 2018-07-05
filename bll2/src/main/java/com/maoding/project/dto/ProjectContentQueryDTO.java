package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/5
 * @description :
 */
public class ProjectContentQueryDTO extends BaseDTO {
    /** 要查询的项目编号 */
    private String projectId;
    /** 是否包含功能分类 */
    private Boolean isFunction;
    /** 是否包含专业信息 */
    private Boolean isMeasure;
    /** 是否包含设计范围 */
    private Boolean isRange;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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
