package com.maoding.project.dto;

import com.maoding.core.base.dto.CoreEditDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/23
 * 类名: com.maoding.project.dto.TitleEditDTO
 * 作者: 张成亮
 * 描述:
 **/
public class TitleEditDTO extends CoreEditDTO {
    /** id: 标题栏编号 **/

    /** 标题栏类型: 0-我的项目标题栏，1-项目总览标题栏，2-发票汇总标题栏 **/
    private Integer type;

    /** 选中的标题栏，保存code **/
    private List<String> titleCodeList;

    public List<String> getTitleCodeList() {
        return titleCodeList;
    }

    public void setTitleCodeList(List<String> titleCodeList) {
        this.titleCodeList = titleCodeList;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
