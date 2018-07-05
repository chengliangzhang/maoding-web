package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.Date;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/3 11:31
 * 描    述 : 工时查询DTO
 */
public class ProjectWorkingHoursDTO extends BaseDTO {
    /**
     * 起始日期
     */
    private Date startDate;
    /**
     * 终止日期
     */
    private Date endDate;
    /**
     * 返回的统计列表从符合条件的记录集中第几条记录开始
     */
    private Integer pageIndex;
    /**
     * 返回的统计列表最多为多少条
     */
    private Integer pageSize;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 工时(0：从小到大，1：从大到小)
     */
    private Integer seq;

    /**
     * 项目ID
     */
    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
