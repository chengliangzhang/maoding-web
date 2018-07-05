package com.maoding.project.entity;

import com.maoding.commonModule.dto.ContentDTO;
import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.BeanUtilsEx;

/**
 * @author  张成亮
 * @date    2018/7/5
 * @description     项目可自定义属性，在新建项目时从项目模板中复制到项目，用户可以增加、修改及删除
 **/
public class ProjectPropertyEntity extends BaseEntity{
    /** 属性所在项目id */
    private String projectId;
    /** 属性名称,同ContentDTO.name */
    private String fieldName;
    /** 属性值（统一格式化为字符串） */
    private String fieldValue;
    /** 属性单位,同ContentDTO.detailUnit */
    private String unitName;
    /** 属性在自定义属性中的排序序号,同ContentDTO.detailId */
    private Integer sequencing;
    /** 删除标志 */
    private Short deleted;
    /** 内容类型编号,根据ContentDTO.isFunction,isMeasure,isRange转换 */
    private String contentTypeId;
    /** 组件类型 */
    private Integer detailTypeId;
    /** 横轴位置 */
    private Integer xPos;
    /** 纵轴位置 */
    private Integer yPos;
    /** 是否模板内容 */
    private Boolean isDefault;
    /** 序列参数说明字符串，仅用于序列类型 */
    private String detailVarName;

    public Integer getDetailTypeId() {
        return detailTypeId;
    }

    public void setDetailTypeId(Integer detailTypeId) {
        this.detailTypeId = detailTypeId;
    }

    public String getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public Integer getxPos() {
        return xPos;
    }

    public void setxPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    public void setyPos(Integer yPos) {
        this.yPos = yPos;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getDetailVarName() {
        return detailVarName;
    }

    public void setDetailVarName(String detailVarName) {
        this.detailVarName = detailVarName;
    }

    public ProjectPropertyEntity(){}
    public ProjectPropertyEntity(Object obj){
        BeanUtilsEx.copyProperties(obj,this);
    }
    public ProjectPropertyEntity(ContentDTO obj){
        BeanUtilsEx.copyProperties(obj,this);
        initEntity();
        setFieldName(obj.getName());
        setUnitName(obj.getDetailUnit());
        setSequencing(obj.getDetailId());
        setContentTypeId(getContentTypeIdByContent(obj));
        setDeleted((short)0);
    }

    private String getContentTypeIdByContent(ContentDTO obj){
        final String FUNCTION_TYPE_ID = "33";
        final String MEASURE_TYPE_ID = "34";
        final String RANGE_TYPE_ID = "35";
        final String OTHER_TYPE_ID = "36";
        if (obj.getFunction() != null && obj.getFunction()){
            return FUNCTION_TYPE_ID;
        } else if (obj.getMeasure() != null && obj.getMeasure()){
            return MEASURE_TYPE_ID;
        } else if (obj.getRange() != null && obj.getRange()) {
            return RANGE_TYPE_ID;
        } else {
            return OTHER_TYPE_ID;
        }
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public Short getDeleted() {
        return deleted;
    }

    public void setDeleted(Short deleted) {
        this.deleted = deleted;
    }
}
