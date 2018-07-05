package com.maoding.dynamic.dto;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/6/7.
 */
public class ProjectDynamicDTO {
    /**
     * 记录总条数
     */
    private Integer total;
    /**
     * 显示内容
     */
    List<DynamicDTO> dynamicList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<DynamicDTO> getDynamicList() {
        return dynamicList;
    }

    public void setDynamicList(List<DynamicDTO> dynamicList) {
        this.dynamicList = dynamicList;
    }
}
