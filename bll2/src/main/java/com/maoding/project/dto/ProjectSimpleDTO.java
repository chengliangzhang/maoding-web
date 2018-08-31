package com.maoding.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseIdObject;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/10
 * 类名: com.maoding.project.dto.ProjectSimpleDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectSimpleDTO extends BaseIdObject {
    /** 项目名 */
    private String projectName;

    /** 项目立项方 */
    private String projectCreator;

    /** 立项时间 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date projectCreateDate;

    public Date getProjectCreateDate() {
        return projectCreateDate;
    }

    public void setProjectCreateDate(Date projectCreateDate) {
        this.projectCreateDate = projectCreateDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCreator() {
        return projectCreator;
    }

    public void setProjectCreator(String projectCreator) {
        this.projectCreator = projectCreator;
    }
}
