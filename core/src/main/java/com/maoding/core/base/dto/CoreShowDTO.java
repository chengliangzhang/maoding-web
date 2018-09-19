package com.maoding.core.base.dto;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/27
 * 类名: com.maoding.core.base.dto.CoreShowDTO
 * 作者: 张成亮
 * 描述: 后台传递给前端用于显示的信息的基类
 **/
public class CoreShowDTO extends BaseIdObject {
    /** 显示元素内容（名称） */
    private String name;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoreShowDTO(){}
    public CoreShowDTO(String id,String name){
        setId(id);
        setName(name);
    }


}
