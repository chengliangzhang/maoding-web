package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.financial.entity.ExpCategoryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ExpTypeDTO
 * 类描述：变动支出类型Bean（用于新增，数据封装）
 * 作    者：MaoSF
 * 日    期：2015年12月7日-上午11:13:40
 */
public class ExpTypeDTO extends BaseDTO {



    private ExpCategoryEntity parent;
    private List<ExpCategoryEntity> child=new ArrayList<ExpCategoryEntity>();

    /**
     * 获取: parent
     */
    public ExpCategoryEntity getParent() {
        return parent;
    }
    /**
     * 设置:parent
     */
    public void setParent(ExpCategoryEntity parent) {
        this.parent = parent;
    }
    /**
     * 获取: child
     */
    public List<ExpCategoryEntity> getChild() {
        return child;
    }
    /**
     * 设置:child
     */
    public void setChild(List<ExpCategoryEntity> child) {
        this.child = child;
    }



}
