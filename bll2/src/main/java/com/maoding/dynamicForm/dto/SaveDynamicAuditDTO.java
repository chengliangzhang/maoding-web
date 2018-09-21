package com.maoding.dynamicForm.dto;

import com.maoding.attach.dto.FileDataDTO;
import com.maoding.financial.dto.AuditBaseDTO;
import com.maoding.financial.dto.AuditCommonDTO;
import com.maoding.financial.dto.AuditDTO;
import com.maoding.org.dto.CompanyUserDataDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveDynamicAuditDTO extends AuditBaseDTO {

    private String type;

    /**
     * 前端生成的报销单Id
     */
    private String targetId;

    /**
     * 审批的基础数据
     */
    private AuditCommonDTO baseAuditData = new AuditCommonDTO();


    /**
     * 1:要审批，其他不要审批
     */
    private Integer auditFlag;//需要审批

    /**
     * 抄送人
     */
    private List<CompanyUserDataDTO> ccCompanyUserList = new ArrayList<>();

    /**
     * 附件
     */
    private List<FileDataDTO> attachList = new ArrayList<>();

    /**
     * 审批记录列表
     */
    private List<AuditDTO> auditList = new ArrayList<>();

    /**
     * 所有控件的记录数据
     */
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

    public List<FileDataDTO> getAttachList() {
        return attachList;
    }

    public void setAttachList(List<FileDataDTO> attachList) {
        this.attachList = attachList;
    }

    public List<AuditDTO> getAuditList() {
        return auditList;
    }

    public void setAuditList(List<AuditDTO> auditList) {
        this.auditList = auditList;
    }

    public void setAuditFlag(Integer auditFlag) {
        this.auditFlag = auditFlag;
    }

    public AuditCommonDTO getBaseAuditData() {
        return baseAuditData;
    }

    public void setBaseAuditData(AuditCommonDTO baseAuditData) {
        this.baseAuditData = baseAuditData;
    }

    public Integer getAuditFlag() {
        return auditFlag;
    }
}
