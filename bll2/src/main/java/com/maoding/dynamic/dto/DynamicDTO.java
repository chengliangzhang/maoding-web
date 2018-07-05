package com.maoding.dynamic.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/6/7.
 */
public class DynamicDTO {
    /**
     * 操作者名称
     */
    @JsonIgnore
    String operatorName;
    /**
     * 操作节点名称
     */
    @JsonIgnore
    String nodeName;
    /**
     * 操作附加说明
     */
    @JsonIgnore
    String extra;
    /**
     * 转换模板
     */
    @JsonIgnore
    String template;
    /**
     * 创建时间
     */
    Date createDate;
    /**
     * 合成字符串
     */
    String content;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
