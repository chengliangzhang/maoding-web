package com.maoding.commonModule.dto;

public class RelationTypeDTO {

    private String relationId;

    private Integer recordType;

    private String operateRecordId;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public String getOperateRecordId() {
        return operateRecordId;
    }

    public void setOperateRecordId(String operateRecordId) {
        this.operateRecordId = operateRecordId;
    }

}
