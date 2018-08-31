package com.maoding.core.base.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: CorePageDTO
 * @description : 后台传递给前端用于分页显示的信息的基类
 */
public class CorePageDTO<T extends BaseIdObject> implements Serializable,Cloneable {
    /** 查询出的信息总数 */
    private int total;
    /** 当前页编号 */
    private int pageIndex;
    /** 当前页大小 */
    private int pageSize;
    /** 当前页内容（元素列表） */
    private List<T> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
