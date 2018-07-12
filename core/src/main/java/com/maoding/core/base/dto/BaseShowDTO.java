package com.maoding.core.base.dto;

import java.io.Serializable;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/12
 * @description : 用于显示的对象的基类
 */
public class BaseShowDTO implements Serializable {
    /** 组件编号 */
    private String id;
    /** 组件名称 */
    private String name;

    public BaseShowDTO(){}
    public BaseShowDTO(String id,String name){
        setId(id);
        setName(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
