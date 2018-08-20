package com.maoding.project.dto;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/20
 * 类名: com.maoding.project.dto.OptionalTitleSelectedDTO
 * 作者: 张成亮
 * 描述:
 **/
public class OptionalTitleSelectedDTO {
    /** 可选择列表 **/
    private List<OptionalTitleGroupDTO> optionalTitleGroupList;

    /** 已选择列表 **/
    private List<OptionalTitleDTO> selectedTitleList;

    public List<OptionalTitleGroupDTO> getOptionalTitleGroupList() {
        return optionalTitleGroupList;
    }

    public void setOptionalTitleGroupList(List<OptionalTitleGroupDTO> optionalTitleGroupList) {
        this.optionalTitleGroupList = optionalTitleGroupList;
    }

    public List<OptionalTitleDTO> getSelectedTitleList() {
        return selectedTitleList;
    }

    public void setSelectedTitleList(List<OptionalTitleDTO> selectedTitleList) {
        this.selectedTitleList = selectedTitleList;
    }
}
