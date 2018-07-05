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

    private String projectId;

    private String pointId;

    private BigDecimal fee;

    private String invoice;

    private String field1;

    private String status;

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

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1 == null ? null : field1.trim();
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
}