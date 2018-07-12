package com.maoding.task.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：QueryProjectTaskDTO
 * 类描述：任务查询条件DTO
 * 作    者：MaoSF
 * 日    期：2016年12月31日-上午10:13:28
 */
public class QueryProjectTaskDTO extends BaseDTO{

    Integer pageIndex;
    Integer pageSize;

    private String companyId;//当前公司id

    private String projectId;//当前项目

    private String currentCompanyUserId;
    /**
     * 是否是立项方（1：是，0：否）
     */
    private int isCreator;//是否是立项方

    /**
     * 所属的签发任务的id
     * 如果为空，则查询所有生产任务，否则，只查询对应此签发任务的生产任务
     */
    private String issueTaskId;

    public String getIssueTaskId() {
        return issueTaskId;
    }

    public void setIssueTaskId(String issueTaskId) {
        this.issueTaskId = issueTaskId;
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

    public int getIsCreator() {
        return isCreator;
    }

    public void setIsCreator(int isCreator) {
        this.isCreator = isCreator;
    }

    public String getCurrentCompanyUserId() {
        return currentCompanyUserId;
    }

    public void setCurrentCompanyUserId(String currentCompanyUserId) {
        this.currentCompanyUserId = currentCompanyUserId;
    }
}
