package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;

public class AuditBaseDTO extends BaseDTO {

    /**
     * 审批内容
     */
    private String auditMessage;

    /**
     * 审核人id
     */
    private String auditPerson;

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }
}
