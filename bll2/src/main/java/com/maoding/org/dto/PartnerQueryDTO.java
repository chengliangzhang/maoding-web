package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * Created by Chengliang.zhang on 2017/5/8.
 */
public class PartnerQueryDTO extends BaseDTO {
    /**
     * 发出邀请的公司
     */
    private String fromCompanyId;
    /**
     * 合伙人所属的项目
     */
    private String projectId;
    /**
     * 显示第几页
     */
    Integer pageIndex;
    /**
     * 显示每页行数
     */
    Integer pageSize;
    /**
     * 返回的统计列表从符合条件的记录集中第几条记录开始
     */
    Integer startLine;
    /**
     * 返回的统计列表最多为多少条
     */
    Integer linesCount;
    /**
     * 是否计算总条数
     */
    Boolean isCount;

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
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

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getLinesCount() {
        return linesCount;
    }

    public void setLinesCount(Integer linesCount) {
        this.linesCount = linesCount;
    }

    public Boolean getCount() {
        return isCount;
    }

    public void setCount(Boolean count) {
        isCount = count;
    }
}
