package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;

public class AuditEditDTO extends BaseDTO {

    private String mainId;

    private String auditType;

    private String companyUserId;

    public AuditEditDTO(){

    }
    public AuditEditDTO(String mainId, String auditType, String companyUserId){
        this.mainId = mainId;
        this.auditType = auditType;
        this.companyUserId = companyUserId;
    }
    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }
}
