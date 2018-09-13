package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveDynamicAuditDTO extends BaseDTO {

    /**
     * 审核人id
     */
    private String auditPerson;

    private String auditType;

    private List<DynamicFormFieldValueDTO> filedList = new ArrayList<>();

    private Map<String,List<DynamicFormFieldValueDTO>> detailList = new HashMap<>();

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public List<DynamicFormFieldValueDTO> getFiledList() {
        return filedList;
    }

    public void setFiledList(List<DynamicFormFieldValueDTO> filedList) {
        this.filedList = filedList;
    }

    public Map<String, List<DynamicFormFieldValueDTO>> getDetailList() {
        return detailList;
    }

    public void setDetailList(Map<String, List<DynamicFormFieldValueDTO>> detailList) {
        this.detailList = detailList;
    }
}
