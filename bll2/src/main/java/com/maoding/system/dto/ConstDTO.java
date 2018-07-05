package com.maoding.system.dto;

import com.maoding.core.util.BeanUtilsEx;

/**
 * Created by Chengliang.zhang on 2017/8/14.
 * 常量（数值和文字）
 */
public class ConstDTO {
    /** 常量ID */
    String id;
    /** 常量编号 */
    Integer valueId;
    /** 常量内容 */
    String content;

    public ConstDTO(){}
    public ConstDTO(Object obj){
        BeanUtilsEx.copyProperties(obj,this);
    }
    public ConstDTO(Integer valueId){
        this(valueId,null);
    }
    public ConstDTO(Integer valueId,String content){
        setValueId(valueId);
        setContent(content);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getValueId() {
        return valueId;
    }

    public void setValueId(Integer valueId) {
        this.valueId = valueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
