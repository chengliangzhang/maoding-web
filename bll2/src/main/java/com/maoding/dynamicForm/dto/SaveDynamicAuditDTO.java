package com.maoding.dynamicForm.dto;

import com.maoding.financial.dto.AuditBaseDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveDynamicAuditDTO extends AuditBaseDTO {

    private String auditType;

    private List<DynamicFormFieldValueDTO> fieldList = new ArrayList<>();

    private Map<String,List<DynamicFormFieldValueDTO>> detailList = new HashMap<>();

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public List<DynamicFormFieldValueDTO> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DynamicFormFieldValueDTO> fieldList) {
        this.fieldList = fieldList;
    }

    public Map<String, List<DynamicFormFieldValueDTO>> getDetailList() {
        return detailList;
    }

    public void setDetailList(Map<String, List<DynamicFormFieldValueDTO>> detailList) {
        this.detailList = detailList;
    }

}
