package com.maoding.process.entity;

import com.maoding.core.base.entity.BaseEntity;

public class ProcessInstanceRelationEntity extends BaseEntity{

    private String targetId;

    private String processInstanceId;

    private String targetType;

    private Integer deleted;
    
    /** 动态表单中，用于作为存储财务拨款条件的字段id **/
    private String financeFieldId;
    
    /** 动态表单中，用于作为条件流程的字段id **/
    private String conditionFieldId;

    /** 动态表单模板的编号 **/
    private String processTypeId;

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }

    public String getFinanceFieldId() {
        return financeFieldId;
    }

    public void setFinanceFieldId(String financeFieldId) {
        this.financeFieldId = financeFieldId;
    }

    public String getConditionFieldId() {
        return conditionFieldId;
    }

    public void setConditionFieldId(String conditionFieldId) {
        this.conditionFieldId = conditionFieldId;
    }

    public ProcessInstanceRelationEntity(){

    }
    public ProcessInstanceRelationEntity(String targetId,String processInstanceId,String targetType){
        this.initEntity();
        this.deleted = 0;
        this.targetId = targetId;
        this.processInstanceId = processInstanceId;
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId == null ? null : processInstanceId.trim();
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

}