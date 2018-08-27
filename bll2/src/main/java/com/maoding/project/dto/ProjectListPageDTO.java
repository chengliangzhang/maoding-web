package com.maoding.project.dto;

import com.maoding.core.base.dto.CorePageDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/27
 * 类名: com.maoding.project.dto.ProjectListPageDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectListPageDTO extends CorePageDTO<ProjectVariableDTO> {
    /** 项目是否可编辑 **/
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
