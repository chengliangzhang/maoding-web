package com.maoding.project.dto;

import com.maoding.core.base.dto.CoreShowDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/10
 * 类名: com.maoding.project.dto.ProjectSimpleDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectSimpleDTO extends CoreShowDTO {
    /** 项目立项方 */
    private String creator;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
