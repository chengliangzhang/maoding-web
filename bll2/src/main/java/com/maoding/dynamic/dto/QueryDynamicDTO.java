package com.maoding.dynamic.dto;

/**
 * Created by Chengliang.zhang on 2017/6/7.
 */
public class QueryDynamicDTO {
    /**
     * 申请查询的用户编号
     */
    String userId;
    /**
     * 申请查询的组织编号
     */
    String companyId;
    /**
     * 需要过滤的项目编号
     */
    String projectId;
    /**
     * 申请的记录页
     */
    Integer pageIndex;
    /**
     * 申请的起始记录
     */
    Integer startLine;
    /**
     * 申请的页记录个数
     */
    Integer pageSize;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
