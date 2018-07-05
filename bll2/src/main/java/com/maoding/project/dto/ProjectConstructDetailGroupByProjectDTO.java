package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectConstructDetailDTO
 * 类描述：建设单位表项目联系人DTO
 * 作    者：LY
 * 日    期：2016年7月19日-下午5:02:50
 */
public class ProjectConstructDetailGroupByProjectDTO extends BaseDTO {


    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 联系人数组
     */
    private List<ProjectConstructDetailDTO> detailList = new ArrayList<ProjectConstructDetailDTO>();


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<ProjectConstructDetailDTO> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<ProjectConstructDetailDTO> detailList) {
        this.detailList = detailList;
    }
}