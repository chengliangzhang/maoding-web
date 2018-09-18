package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.CoreShowDTO;

public class AuditCopyDTO extends CoreShowDTO{

    /**
     * 记录的id（companyUserId，departId等）
     */
    private String relationId;

    /**
     * 记录的类型（单个的人员 = 1，部门 = 2，角色等）
     */
    private String relationType;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}
