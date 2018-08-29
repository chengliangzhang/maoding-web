package com.maoding.process.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.financial.dto.AuditBaseDTO;

import java.util.HashMap;
import java.util.Map;

public class ActivitiDTO extends AuditBaseDTO {

    private String businessKey;

    private String targetType;

    private String companyUserId;

    private Map<String, Object> param = new HashMap<>();

    public ActivitiDTO(){}

    public ActivitiDTO(String businessKey,String companyUserId,String companyId,String accountId,Object param,String targetType ){
        this.businessKey = businessKey;
        this.companyUserId = companyUserId;
        this.targetType = targetType;
        this.setAccountId(accountId);
        this.setAppOrgId(companyId);
        if(param!=null ){
            this.param.put(this.getTargetType(),param);
        }
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}
