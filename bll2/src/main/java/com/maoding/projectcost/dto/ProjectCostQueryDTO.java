package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 用于查询的DTO
 * Created by Chengliang.zhang on 2017/4/27.
 */
public class ProjectCostQueryDTO extends BaseDTO {
    /**
     * 公司ID
     */
    private String companyId;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 查询类型
     * 1:合同回款，2:技术审查费-付款，3:合作设计费-付款，4:其他费用-付款
     * 5:技术审查费-收款，6:合作设计费-收款，7:其他费用-收款
     * SystemParameters.FEE_TYPE_CONTRACT -- SystemParameters.FEE_TYPE_OTHER_DEBIT
     */
    private Integer type;
    /**
     * 显示第几页
     */
    private Integer pageIndex;
    /**
     * 显示每页行数
     */
    private Integer pageSize;
    /**
     * 返回的统计列表从符合条件的记录集中第几条记录开始
     */
    private Integer startLine;
    /**
     * 返回的统计列表最多为多少条
     */
    private Integer linesCount;
    /**
     * 返回的统计列表是否按项目名排序
     * null:不按此项排序，1:按此项从小到大排序，-1:按此项从大到小排序
     */
    private Integer orderByProjectName;
    /**
     * 返回的统计列表是否按最后到款日期排序
     * null:不按此项排序，1:按此项从小到大排序，-1:按此项从大到小排序
     */
    private Integer orderByLastPaidDate;

    /**
     * 是否计算总条数
     */
    private Boolean isCount;
	
	/**
     * 主记录id
     */
    private String costId;
	

    /**
     * 收款节点的id
     */
    private String pointId;

    /**
     * 发起收款的节点id
     */
    private String pointDetailId;

    /**
     * 收款计划：1，付款计划：2
     */
    private Integer payType;

    public Boolean getCount() {
        return isCount;
    }

    public void setCount(Boolean count) {
        isCount = count;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getPointDetailId() {
        return pointDetailId;
    }

    public void setPointDetailId(String pointDetailId) {
        this.pointDetailId = pointDetailId;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        return ((getPageIndex() != null) && (getPageSize() != null)) ? getPageIndex()*getPageSize() : null;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getLinesCount() {
        return getPageSize();
    }

    public void setLinesCount(Integer linesCount) {
        this.linesCount = linesCount;
    }

    public Integer getOrderByProjectName() {
        return orderByProjectName;
    }

    public void setOrderByProjectName(Integer orderByProjectName) {
        this.orderByProjectName = orderByProjectName;
    }

    public Integer getOrderByLastPaidDate() {
        return orderByLastPaidDate;
    }

    public void setOrderByLastPaidDate(Integer orderByLastPaidDate) {
        this.orderByLastPaidDate = orderByLastPaidDate;
    }

    public Boolean getIsCount() {
        return isCount;
    }

    public void setIsCount(Boolean isCount) {
        this.isCount = isCount;
    }
}
