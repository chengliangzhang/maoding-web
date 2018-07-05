package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveCopyRecordDTO extends BaseDTO {

    private String targetId;

    private String sendCompanyUserId;

    private String operateRecordId;

    private Integer recordType;

    private List<String> companyUserList = new ArrayList<>();

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public List<String> getCompanyUserList() {
        return companyUserList;
    }

    public void setCompanyUserList(List<String> companyUserList) {
        this.companyUserList = companyUserList;
    }

    public String getSendCompanyUserId() {
        return sendCompanyUserId;
    }

    public void setSendCompanyUserId(String sendCompanyUserId) {
        this.sendCompanyUserId = sendCompanyUserId;
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
}
