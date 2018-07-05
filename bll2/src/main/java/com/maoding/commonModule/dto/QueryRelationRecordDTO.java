package com.maoding.commonModule.dto;

public class QueryRelationRecordDTO {

    private String relationId;

    private Integer recordType;

    private String operateRecordId;

    private String targetId;

    public QueryRelationRecordDTO(){

    }

    public QueryRelationRecordDTO(String targetId,String operateRecordId){
        this.targetId = targetId;
        this.operateRecordId = operateRecordId;
    }

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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
