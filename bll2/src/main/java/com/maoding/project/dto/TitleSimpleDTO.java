package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseIdObject;
import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/23
 * 类名: com.maoding.project.dto.TitleColumnDTO
 * 作者: 张成亮
 * 描述:
 **/
public class TitleSimpleDTO extends BaseIdObject {
    /** id: 标题编号 **/

    /** 标题关键字 **/
    private String code;

    /** 标题名称 **/
    private String name;

    /** 字段类型：1-字符串,2-日期,3-金额（万元）,4-金额（元） **/
    private int type;

    /** 过滤器类型：0-无过滤器，1-字符串，2-单选列表，3-多选列表，4-时间，5-地址 **/
    private int filterType;

    /** 是否可排序,0-不可排序,1-可以排序 **/
    private short canBeOrder;

    /** 过滤器，仅对列表型过滤器有效 **/
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

    public short getCanBeOrder() {
        return canBeOrder;
    }

    public void setCanBeOrder(short canBeOrder) {
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
