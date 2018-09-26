package com.maoding.dynamicForm.dto;

/*
* 保存审核表单样式DTO
*/
public class DynamicFormFieldBaseDTO {

    private String id;
    /** 主表id（maoding_dynamic_form的id） **/
    private String formId;

    /** 父记录id（maoding_dynamic_form_field的id）,如果pid不为null，则代表的是明细中的字段 **/
    private String fieldPid;

    /** 标题 **/
    private String fieldTitle;

    /** 字段类型 **/
    private Integer fieldType;

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

    private Integer isFeeType;

    /** 横坐标排序（如果x相同，则排成一行） **/
    private Integer seqX;

    /** 纵坐标排序 **/
    private Integer seqY;

    /** 必填类型（1：必填，0：非必填） **/
    private Integer requiredType;

    /** 是否参与统计（数字字段是否属于分条件流程内的条件字段）0:不参与统计，1：参与统计 **/
    private Integer isStatistics;

    /** 是否参与财务统计 0:不参与统计，1：参与统计 **/
    private Integer isFinanceStatistics;

    /** 数据类型（用于处理时间的，0：不做处理，1：YYYY/MM/HH,2：YYYY/MM/HH hh:mm,3:YYYY/MM/HH 上午&下午等） **/
    private Integer dateFormatType;

    /** 图标标志 **/
    private String iconKey;

    /** 是否费用分类 **/
    private Integer isFeeClassic;

    public Integer getIsFeeClassic() {
        return isFeeClassic;
    }

    public void setIsFeeClassic(Integer isFeeClassic) {
        this.isFeeClassic = isFeeClassic;
    }

    public String getIconKey() {
        return iconKey;
    }

    public void setIconKey(String iconKey) {
        this.iconKey = iconKey;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFieldPid() {
        return fieldPid;
    }

    public void setFieldPid(String fieldPid) {
        this.fieldPid = fieldPid;
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

    public String getFieldSelectValueType() {
        return fieldSelectValueType;
    }

    public void setFieldSelectValueType(String fieldSelectValueType) {
        this.fieldSelectValueType = fieldSelectValueType;
    }

    public Integer getSeqX() {
        return seqX;
    }

    public void setSeqX(Integer seqX) {
        this.seqX = seqX;
    }

    public Integer getSeqY() {
        return seqY;
    }

    public void setSeqY(Integer seqY) {
        this.seqY = seqY;
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

    public Integer getArrangeType() {
        return arrangeType;
    }

    public void setArrangeType(Integer arrangeType) {
        this.arrangeType = arrangeType;
    }

    public Integer getDateFormatType() {
        return dateFormatType;
    }

    public void setDateFormatType(Integer dateFormatType) {
        this.dateFormatType = dateFormatType;
    }

    public Integer getIsFinanceStatistics() {
        return isFinanceStatistics;
    }

    public void setIsFinanceStatistics(Integer isFinanceStatistics) {
        this.isFinanceStatistics = isFinanceStatistics;
    }

    public Integer getIsFeeType() {
        return isFeeType;
    }

    public void setIsFeeType(Integer isFeeType) {
        this.isFeeType = isFeeType;
    }
}
