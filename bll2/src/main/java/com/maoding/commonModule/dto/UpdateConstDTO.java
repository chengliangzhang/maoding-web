package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/6/26 12:48
 * 描    述 :
 */
public class UpdateConstDTO extends BaseDTO {
    /** 所属项目编号 */
    private String projectId;
    /** 常量类别编号 */
    private Short classicId;
    /** 常量可显示字符串 */
    private String title;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Short getClassicId() {
        return classicId;
    }

    public void setClassicId(Short classicId) {
        this.classicId = classicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
