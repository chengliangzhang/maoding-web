package com.maoding.core.base.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: CoreQueryDTO
 * @description : 前端发送给后台的用于查询信息的条件
 *      当调用list、count、listPage等方法时，进行实际的数据查找
 */
public abstract class CoreQueryDTO extends BaseDTO implements Serializable,Cloneable {
    /** 要查找多个元素时，目标元素编号列表 */
    private List<String> idList;
    /** 分页查找时指定的页码，如果不进行分页查找，设置为null */
    private Integer pageIndex;
    /** 分页查找时指定的页大小，如果页码为空，此信息无效 */
    private Integer pageSize;

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
