package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.math.BigDecimal;

public class ApplyProjectCostDTO extends BaseDTO {

    /**
     * 申请节点的id
     */
    private String targetId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 申请金额
     */
    private BigDecimal applyAmount;

    /**
     * 发起申请的人的id
     */
    private String companyUserId;

    /**
     * 如果是自由流程，则需要传递auditPerson
     */
    private String auditPerson;

    private Integer type = 5;//默认为项目费用申请

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
