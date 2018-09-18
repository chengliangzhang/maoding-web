package com.maoding.commonModule.dto;

import com.maoding.core.base.dto.CoreShowDTO;

public class AuditCopyDataDTO extends CoreShowDTO{

    /**
     * 所属记录的id（目前是 processType的id）
     */
    private String targetId;

    /**
     * targetId关联记录的类型（1= processType表中的记录）
     */
    private String targetType;

    /**
     * 记录的id（companyUserId，departId等）
     */
    private String relationId;

    /**
     * 记录的类型（单个的人员 = 4，部门 = 2，角色等）
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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
