package com.maoding.project.dto;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/20
 * 类名: com.maoding.project.dto.OptionalTitleDTO
 * 作者: 张成亮
 * 描述:
 **/
public class OptionalTitleDTO {
    /** 编号 **/
    private String code;

    /** 显示字符串 **/
    private String name;

    /** 是否被选中 **/
    private String isSelected;

    public OptionalTitleDTO(){}
    public OptionalTitleDTO(String code,String name){
        setCode(code);
        setName(name);
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
