package com.maoding.core.base.dto;


import javax.persistence.Id;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/30
 * 类名: com.maoding.core.base.dto.BaseIdObject
 * 作者: 张成亮
 * 描述: 带有编号的对象基类
 **/
public class BaseIdObject implements Cloneable {
    /** 元素编号 */
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
