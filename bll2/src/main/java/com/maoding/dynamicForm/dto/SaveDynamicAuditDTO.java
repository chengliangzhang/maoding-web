package com.maoding.dynamicForm.dto;

import com.maoding.attach.dto.FileDataDTO;
import com.maoding.financial.dto.AuditBaseDTO;
import com.maoding.org.dto.CompanyUserDataDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveDynamicAuditDTO extends AuditBaseDTO {

    private String type;

    /**
     * 前端生成的报销单Id
     */
    private String targetId;

    private List<CompanyUserDataDTO> ccCompanyUserList = new ArrayList<>();

    private List<FileDataDTO> expAttachList;

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

    public List<CompanyUserDataDTO> getCcCompanyUserList() {
        return ccCompanyUserList;
    }

    public void setCcCompanyUserList(List<CompanyUserDataDTO> ccCompanyUserList) {
        this.ccCompanyUserList = ccCompanyUserList;
    }

    public List<FileDataDTO> getExpAttachList() {
        return expAttachList;
    }

    public void setExpAttachList(List<FileDataDTO> expAttachList) {
        this.expAttachList = expAttachList;
    }
}
