package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveRelationRecordDTO extends BaseDTO {

    private String targetId;

    private Integer deleted;

    private Integer recordType;

    private List<RelationTypeDTO> relationList = new ArrayList<>();

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
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

    public List<RelationTypeDTO> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<RelationTypeDTO> relationList) {
        this.relationList = relationList;
    }
}
