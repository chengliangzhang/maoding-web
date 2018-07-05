package com.maoding.project.dto;

import com.maoding.core.util.BeanUtilsEx;

/**
 * Created by Chengliang.zhang on 2017/8/14.
 * 自定义项目属性
 */
@Deprecated
public class CustomProjectPropertyDTO {
    /** 属性id */
    private String id;
    /** 属性名称 */
    private String fieldName;
    /** 属性值（统一格式化为字符串 */
    private String fieldValue;
    /** 属性单位 */
    private String unitName;
    /** 属性在自定义属性中的排序序号 */
    private Integer sequencing;
    /** 记录更新状态,-1：删除，0：未更改，1：新增，2：更改 */
    private Short changeStatus;

    public CustomProjectPropertyDTO(){}
    public CustomProjectPropertyDTO(Object obj){
        BeanUtilsEx.copyProperties(obj,this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getSequencing() {
        return sequencing;
    }

    public void setSequencing(Integer sequencing) {
        this.sequencing = sequencing;
    }

    public Short getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(Short changeStatus) {
        this.changeStatus = changeStatus;
    }
}
