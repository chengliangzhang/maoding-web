package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.CoreShowDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/18
 * 类名: com.maoding.commonModule.dto.WidgetPropertyDTO
 * 作者: 张成亮
 * 描述:
 **/
public class WidgetPropertyDTO extends CoreShowDTO {
    /** id:控件属性编号 **/
    
    /** 属性名称 **/
    private String propertyTitle;
    
    /** 属性默认值 **/
    private String defaultValue;
    
    /** 属性输入框类型：1-文本框,2-单选框,3-多选框,4-列表框 **/
    private String propertySettingType;

    /** 属性可选择值，各选择值使用","分隔，如果为空，则可以为任意值 **/
    private String allowValue;

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPropertySettingType() {
        return propertySettingType;
    }

    public void setPropertySettingType(String propertySettingType) {
        this.propertySettingType = propertySettingType;
    }

    public String getAllowValue() {
        return allowValue;
    }

    public void setAllowValue(String allowValue) {
        this.allowValue = allowValue;
    }
}
