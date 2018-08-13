package com.maoding.project.dto;

import com.maoding.core.base.dto.CoreDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/10
 * 类名: com.maoding.project.dto.ProjectSimpleDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectSimpleDTO extends CoreDTO {
    /** 项目名 */
    private String projectName;

    /** 项目立项方 */
    private String projectCreator;

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
