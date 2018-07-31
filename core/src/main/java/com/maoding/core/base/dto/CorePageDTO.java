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
public class CorePageDTO<T extends CoreDTO> implements Serializable,Cloneable {
    /** 查询出的信息总数 */
    private int count;
    /** 当前页编号 */
    private int pageIndex;
    /** 当前页大小 */
    private int pageSize;
    /** 当前页内容（元素列表） */
    private List<T> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
