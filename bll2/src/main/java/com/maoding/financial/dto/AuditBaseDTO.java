package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;

public class AuditBaseDTO extends BaseDTO {

    /**
     * 审批内容
     */
    private String auditMessage;

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }
}
