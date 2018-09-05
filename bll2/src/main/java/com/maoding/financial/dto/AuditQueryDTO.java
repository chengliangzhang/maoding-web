package com.maoding.financial.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

public class AuditQueryDTO extends CoreQueryDTO {

    private String mainId;

    /** 审批类型，定义见ProcessTypeConst.PROCESS_TYPE_xxx **/
    private String auditType;

    private String companyUserId;

    public AuditQueryDTO(){

    }
    public AuditQueryDTO(String mainId, String auditType, String companyUserId){
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
