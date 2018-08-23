package com.maoding.project.dto;

import com.maoding.core.base.dto.CoreDTO;
import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/23
 * 类名: com.maoding.project.dto.TitleColumnDTO
 * 作者: 张成亮
 * 描述:
 **/
public class TitleColumnDTO extends CoreDTO {
    /** id: 标题编号 **/
    
    /** 标题关键字 **/
    private String code;

    /** 标题名称 **/
    private String name;

    /** 是否可排序,0-不可排序,1-可以排序 **/
    private String canBeOrder;

    /** 字段类型：1-字符串,2-日期,3-金额（万元）,4-金额（元） **/
    private int type;

    /** 过滤器类型：0-无过滤器，1-字符串，2-单选列表，3-多选列表，4-时间，5-地址 **/
    private int filterType;

    /** 过滤器是列表型：0-不是,1-是 **/
    private String hasList;

    /** 过滤器，仅对列表型过滤器有效 **/
    private List<CoreShowDTO> filterList;

    public String getHasList() {
        return hasList;
    }

    public void setHasList(String hasList) {
        this.hasList = hasList;
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

    public String getCanBeOrder() {
        return canBeOrder;
    }

    public void setCanBeOrder(String canBeOrder) {
        this.canBeOrder = canBeOrder;
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
