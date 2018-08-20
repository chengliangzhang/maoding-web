package com.maoding.project.dto;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/20
 * 类名: com.maoding.project.dto.OptionalTitleGroupDTO
 * 作者: 张成亮
 * 描述:
 **/
public class OptionalTitleGroupDTO {
    /** 可选显示字段群组名称 **/
    private String name;

    /** 可选标题 **/
    private List<OptionalTitleDTO> optionalTitleList;

    public OptionalTitleGroupDTO(){}
    public OptionalTitleGroupDTO(String name,List<OptionalTitleDTO> list){
        setName(name);
        setOptionalTitleList(list);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OptionalTitleDTO> getOptionalTitleList() {
        return optionalTitleList;
    }

    public void setOptionalTitleList(List<OptionalTitleDTO> optionalTitleList) {
        this.optionalTitleList = optionalTitleList;
    }
}
