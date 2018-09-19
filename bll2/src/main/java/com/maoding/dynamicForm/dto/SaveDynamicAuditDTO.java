package com.maoding.dynamicForm.dto;

import com.maoding.financial.dto.AuditBaseDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveDynamicAuditDTO extends AuditBaseDTO {

    private String type;

    private List<DynamicFormFieldValueDTO> fieldList = new ArrayList<>();

    private Map<String,List<DynamicFormFieldValueDTO>> detailList = new HashMap<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
