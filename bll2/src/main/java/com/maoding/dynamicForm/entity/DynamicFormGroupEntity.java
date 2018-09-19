package com.maoding.dynamicForm.entity;

import com.maoding.core.base.entity.BaseEntity;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/14
 * 类名: com.maoding.dynamicForm.entity.DynamicFormGroupEntity
 * 作者: 张成亮
 * 描述:
 **/
public class DynamicFormGroupEntity extends BaseEntity {
    /** 所属组织编号 **/
    private String companyId;

    /** 群组名称 **/
    private String groupName;

    /**排序**/
    private Integer seq;

    /**是否展示**/
    private Integer isEdit;

    /**删除**/
    private Integer deleted;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Integer isEdit) {
        this.isEdit = isEdit;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

}
