package com.maoding.commonModule.entity;

import com.maoding.core.base.entity.BaseEntity;

public class RelationRecordEntity extends BaseEntity{

    /** 被关联项（报销，费用，请假，出差等） id */
    private String relationId;

    /**关联项记录id（暂时为报销费用）*/
    private String targetId;

    private String operateRecordId;

    private Integer deleted;

    private Integer recordType;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId == null ? null : relationId.trim();
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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