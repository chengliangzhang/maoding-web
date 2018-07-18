package com.maoding.process.dto;

import com.maoding.core.base.dto.BaseDTO;

public class QueryProcessDTO extends BaseDTO {

    private String companyId;

    private String processId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
