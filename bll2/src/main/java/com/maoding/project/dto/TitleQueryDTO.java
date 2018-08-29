package com.maoding.project.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/23
 * 类名: com.maoding.project.dto.TitleQueryDTO
 * 作者: 张成亮
 * 描述: 标题栏查询条件
 **/
public class TitleQueryDTO extends CoreQueryDTO {
    /** id: 标题栏编号 **/

    /** 标题栏类型: 0-我的项目标题栏，1-项目总览标题栏，2-发票汇总标题栏 **/
    private Integer type;

    /** 是否获取过滤列表: 1/null-查询过滤器列表,0-不查询过滤器列表 **/
    private Integer withList;

    public Integer getWithList() {
        return withList;
    }

    public void setWithList(Integer withList) {
        this.withList = withList;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
