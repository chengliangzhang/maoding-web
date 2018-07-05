package com.maoding.core.base.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/7/11.
 */
public class BasePageDTO<T>  implements Serializable {
    /**
     * 记录总条数
     */
    Integer total;
    /**
     * 本页记录列表
     */
    List<T> list;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
