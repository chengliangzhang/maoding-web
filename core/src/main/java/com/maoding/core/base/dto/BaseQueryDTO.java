package com.maoding.core.base.dto;

/**
 * Created by Chengliang.zhang on 2017/7/12.
 */
public class BaseQueryDTO {
    /**
     * 组织ID
     */
    String id;
    /**
     * 分页时的页码
     */
    Integer pageIndex;
    /**
     * 分页查询时的页面大小
     */
    Integer pageSize;
    /**
     * 起始记录数
     */
    Integer startLine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPageIndex() {
        Integer i = pageIndex;
        if ((pageIndex == null) && (startLine != null) && (pageSize != null)){
            i = (startLine/pageSize) + 1;
        }
        return i;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
        if ((this.pageIndex != null) && (pageSize != null)){
            startLine = this.pageIndex * pageSize;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        if ((this.pageSize != null) && (pageIndex != null)){
            startLine = pageIndex * this.pageSize;
        }
    }

    public Integer getStartLine() {
        Integer i = startLine;
        if ((startLine == null) && (pageIndex != null) && (pageSize != null)){
            i = pageIndex * pageSize;
        }
        return i;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
        if ((this.startLine != null) && (pageSize != null)){
            pageIndex = (this.startLine/pageSize) + 1;
        }
    }

}
