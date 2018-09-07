package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/5
 * 类名: com.maoding.projectcost.dto.ProjectCostQueryDTO
 * 作者: 张成亮
 * 描述: 费用查询条件
 **/
public class ProjectCostQueryDTO extends CoreQueryDTO {
    /**
     * 公司ID
     */
    private String companyId;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 费用类型：1:合同回款，2：技术审查费，3：合作设计费，4:其他费用
     */
    private Integer type;

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
     * 收款计划：1:收款，2：付款
     */
    private Integer payType;

    /**
     * 申请单据id（exp_main 中的id）
     */
    private String mainId;

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

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

    public ProjectCostQueryDTO(){}
    public ProjectCostQueryDTO(String projectId,String companyId){
        setProjectId(projectId);
        setCompanyId(companyId);
    }
}
