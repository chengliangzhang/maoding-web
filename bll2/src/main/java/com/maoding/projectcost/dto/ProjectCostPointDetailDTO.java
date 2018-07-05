package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.math.BigDecimal;

/**
 * 深圳市设计同道技术有限公司
 * 项目费用详情
 * 类    名：ProjectCostDetailDTO
 * 类描述：发起回款，发起收款（合同回款，技术审查费，合作设计费）
 * 作    者：MaoSF
 * 日    期：2017年4月25日-下午4:11:50
 */
public class ProjectCostPointDetailDTO extends BaseDTO{

    private String projectId;

    private String pointId;

    private BigDecimal fee;

    private String field1;

    private String field2;

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

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2 == null ? null : field2.trim();
    }
}