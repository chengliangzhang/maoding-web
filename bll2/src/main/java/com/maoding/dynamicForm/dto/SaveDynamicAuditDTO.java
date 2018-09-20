package com.maoding.dynamicForm.dto;

import com.maoding.financial.dto.AuditBaseDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveDynamicAuditDTO extends AuditBaseDTO {

    private String type;

    /**
     * 前端生成的报销单Id
     */
    private String targetId;

    private List<DynamicFormFieldValueDTO> fieldList = new ArrayList<>();

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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
