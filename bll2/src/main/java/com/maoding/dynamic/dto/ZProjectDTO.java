package com.maoding.dynamic.dto;

import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/6/17.
 */
public class ZProjectDTO {
    /**
     * 项目ID
     */
    String id;
    /**
     * 功能分类合并字符串
     */
    String functions;
    /**
     * 设计范围合并字符串
     */
    String ranges;

    /**
     * 合同签订日期
     */
    Date signDate;

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRanges() {
        return ranges;
    }

    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }
}
