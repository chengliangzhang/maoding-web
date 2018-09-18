package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.CoreShowDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/18
 * 类名: com.maoding.commonModule.dto.WidgetDTO
 * 作者: 张成亮
 * 描述: 动态表单可使用控件的设置信息
 **/
public class WidgetDTO extends CoreShowDTO {
    /** 标题 **/
    private String fieldTitle;

    /** 字段类型，见SystemParameters.DYNAMIC_FORM_FIELD_TYPE_xxx **/
    private Integer fieldType;

    /** 日期字段的显示格式，1-年/月/日,2-年/月/日 时:分,3-年/月/日 上午&下午 **/
    private Integer dateType;

    /** 金额（带单位数字）字段的单位 **/
    private String fieldUnit;

    /** 提示文本 **/
    private String fieldTooltip;

    /** 默认值（暂时定100个长度） **/
    private String fieldDefaultValue;

    /** 排列方式 ，针对 fieldSelectedValueList 中的数据以横向还是纵向排列：1-横向，2-纵向 **/
    private Integer arrangeType;

    /** 默认值0(由数据字典提供，如果为0，则从maoding_dynamic_form_field_selectable_value 去获取,如果有多个，用逗号隔开 **/
    private String fieldSelectValueType;

    /** 必填类型（1：必填，0：非必填） **/
    private Integer requiredType;

    /** 是否参与统计（数字字段是否属于分条件流程内的条件字段）0:不参与统计，1：参与统计 **/
    private Integer isStatistics;
    
    /** 项目列表字段的筛选条件：1-我参与的项目，2-所有项目 **/
    private Integer listProjectFilter;
    
    /** 关联审批字段的筛选条件：1-报销，2-费用申请，3-请假，4-出差 **/
    private Integer listAuditFilter;

    /** 报销列表字段的筛选条件：1-财务设置中的报销类型，2-财务设置中的费用类型，3-系统中的请假类型，0-自定义添加 **/
    private Integer listExpFilter;

    public Integer getListProjectFilter() {
        return listProjectFilter;
    }

    public void setListProjectFilter(Integer listProjectFilter) {
        this.listProjectFilter = listProjectFilter;
    }

    public Integer getListAuditFilter() {
        return listAuditFilter;
    }

    public void setListAuditFilter(Integer listAuditFilter) {
        this.listAuditFilter = listAuditFilter;
    }

    public Integer getListExpFilter() {
        return listExpFilter;
    }

    public void setListExpFilter(Integer listExpFilter) {
        this.listExpFilter = listExpFilter;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    public String getFieldUnit() {
        return fieldUnit;
    }

    public void setFieldUnit(String fieldUnit) {
        this.fieldUnit = fieldUnit;
    }

    public String getFieldTooltip() {
        return fieldTooltip;
    }

    public void setFieldTooltip(String fieldTooltip) {
        this.fieldTooltip = fieldTooltip;
    }

    public String getFieldDefaultValue() {
        return fieldDefaultValue;
    }

    public void setFieldDefaultValue(String fieldDefaultValue) {
        this.fieldDefaultValue = fieldDefaultValue;
    }

    public Integer getArrangeType() {
        return arrangeType;
    }

    public void setArrangeType(Integer arrangeType) {
        this.arrangeType = arrangeType;
    }

    public String getFieldSelectValueType() {
        return fieldSelectValueType;
    }

    public void setFieldSelectValueType(String fieldSelectValueType) {
        this.fieldSelectValueType = fieldSelectValueType;
    }

    public Integer getRequiredType() {
        return requiredType;
    }

    public void setRequiredType(Integer requiredType) {
        this.requiredType = requiredType;
    }

    public Integer getIsStatistics() {
        return isStatistics;
    }

    public void setIsStatistics(Integer isStatistics) {
        this.isStatistics = isStatistics;
    }
}
