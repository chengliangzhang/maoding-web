package com.maoding.projectcost.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.math.BigDecimal;
/**
 * 深圳市设计同道技术有限公司
 * 项目费用详情
 * 类    名：ProjectCostDetailDTO
 * 类描述：发起回款，发起收款（合同回款，技术审查费，合作设计费）
 * 作    者：MaoSF
 * 日    期：2017年4月25日-下午4:11:50
 */
public class ProjectCostPointDetailEntity extends BaseEntity{

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 发起收款所属节点的id（关联ProjectCostPointEntity中的id）
     */
    private String pointId;

    /**
     * 发起金额
     */
    private BigDecimal fee;

    /**
     * 发票号
     */
    private String invoice;

    /**
     * 描述
     */
    private String pointDetailDescription;

    /**
     * 0：有效，1：无效
     */
    private String status;

    /**
     * 费用的状态
     */
    private Integer feeStatus;

    /**
     * 发起组织的id(如果是付款方，则对应 cost 中的 fromCompanyId,如果是收款方则是：toCompanyId)，使用companyId，代表记录的主体方是谁处理
     */
    private String companyId;

    /**
     * 关联团队的id(与companyId对立，为了兼容原有的数据。因为需求更改成发起收款，付款是分开的。)如果后面收付双方进行分开发起收付，则是不同的记录处理
     */
    private String relationCompanyId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId == null ? null : pointId.trim();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getPointDetailDescription() {
        return pointDetailDescription;
    }

    public void setPointDetailDescription(String pointDetailDescription) {
        this.pointDetailDescription = pointDetailDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public Integer getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(Integer feeStatus) {
        this.feeStatus = feeStatus;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRelationCompanyId() {
        return relationCompanyId;
    }

    public void setRelationCompanyId(String relationCompanyId) {
        this.relationCompanyId = relationCompanyId;
    }
}