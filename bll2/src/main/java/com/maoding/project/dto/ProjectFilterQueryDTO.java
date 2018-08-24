package com.maoding.project.dto;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/24
 * 类名: com.maoding.project.dto.ProjectFilterQueryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectFilterQueryDTO extends ProjectQueryDTO {
    /** 标题栏关键字 **/
    private String titleCode;

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }
}
