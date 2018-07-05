package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class QueryCopyRecordDTO extends BaseDTO {

    private String targetId;

    private String copyCompanyUserId;

    private String operateRecordId;

    private Integer recordType;

    private String companyId;

    public QueryCopyRecordDTO(){

    }
    public QueryCopyRecordDTO(String targetId,String copyCompanyUserId){
        this.targetId = targetId;
        this.copyCompanyUserId = copyCompanyUserId;
    }
    public QueryCopyRecordDTO(String targetId){
        this.targetId = targetId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getCopyCompanyUserId() {
        return copyCompanyUserId;
    }

    public void setCopyCompanyUserId(String copyCompanyUserId) {
        this.copyCompanyUserId = copyCompanyUserId;
    }

    public String getOperateRecordId() {
        return operateRecordId;
    }

    public void setOperateRecordId(String operateRecordId) {
        this.operateRecordId = operateRecordId;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
