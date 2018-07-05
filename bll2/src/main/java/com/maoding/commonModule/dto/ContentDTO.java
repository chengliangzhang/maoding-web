package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/6/25 17:24
 * 描    述 : 项目可自定义属性，可用于显示，也可用于修改
 *          如果是显示数据，isSelected表示是否被选中，isDefault表示是否是模板内的属性，建立项目之后添加的属性isDefault为false
 *          如果是修改数据
 *              增加自定义属性时，id设置为空，isSelect设置为true
 *              取消属性时，修改数据需包含目标属性，并把isSelected设置为false，id不可为空
 *              更改属性名称时，修改数据需包含目标，并把isSelected设置为true，id不可为空
 */
public class ContentDTO extends BaseDTO {
    /** 自定义属性名称 */
    private String name;
    /** 选中状态 */
    private Boolean isSelected;
    /** 是否属于模板内容 */
    private Boolean isDefault;
    /** 横轴位置 */
    private Integer xPos;
    /** 纵轴位置 */
    private Integer yPos;
    /** 是否属于功能分类 */
    private Boolean isFunction;
    /** 是否属于专业信息 */
    private Boolean isMeasure;
    /** 是否属于设计范围 */
    private Boolean isRange;
    /** 在大类中的编号 */
    private Integer detailId;
    /** 组件类型编号 */
    private Integer detailTypeId;
    /** 序列参数说明字符串，仅用于序列类型 */
    private String detailVarName;
    /** 单位 */
    private String detailUnit;

    /** 序列前导字符串，仅用于序列类型*/
    private Map<String,String> prefixMap;

    public String getDetailVarName() {
        return detailVarName;
    }

    public void setDetailVarName(String detailVarName) {
        this.detailVarName = detailVarName;
    }

    public String getDetailUnit() {
        return detailUnit;
    }

    public void setDetailUnit(String detailUnit) {
        this.detailUnit = detailUnit;
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

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public Integer getDetailTypeId() {
        return detailTypeId;
    }

    public void setDetailTypeId(Integer detailTypeId) {
        this.detailTypeId = detailTypeId;
    }

    public Map<String, String> getPrefixMap() {
        return prefixMap;
    }

    public void setPrefixMap(Map<String, String> prefixMap) {
        this.prefixMap = prefixMap;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
