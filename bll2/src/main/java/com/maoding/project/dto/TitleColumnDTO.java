package com.maoding.project.dto;

import com.maoding.core.base.dto.CoreDTO;
import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/23
 * 类名: com.maoding.project.dto.TitleDTO
 * 作者: 张成亮
 * 描述:
 **/
public class TitleColumnDTO extends CoreDTO {
    /** id: 标题编号 **/
    
    /** 标题关键字 **/
    private String code;

    /** 标题名称 **/
    private String name;

    /** 标题是否可隐藏,0:不可隐藏，1-可隐藏 **/
    private int isCanHide;

    /** 字段类型：0-字符串，1-日期 **/
    private int type;

    /** 过滤器类型：0-无过滤器，1-字符串，2-单选列表，3-多选列表，4-时间 **/
    private int filterType;

    /** 过滤器，仅对单选、多选类型过滤器有效 **/
    private List<CoreShowDTO> filterList;

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

    public int getIsCanHide() {
        return isCanHide;
    }

    public void setIsCanHide(int isCanHide) {
        this.isCanHide = isCanHide;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public List<CoreShowDTO> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<CoreShowDTO> filterList) {
        this.filterList = filterList;
    }
}
