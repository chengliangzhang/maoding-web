package com.maoding.process.entity;

import com.maoding.core.base.entity.BaseEntity;

public class ProcessTypeEntity extends BaseEntity {

    /**
     * 组织id
     */
    private String companyId;

    /**
     * 流程的类型（参考processTypeConst）
     */
    private String targetType;

    /**
     * 动态字段表id（maoding_dynamic_form的id）
     */
    private String formId;

    /** 表单类型(关联id=DynamicFormGroupEntity的id) **/
    private String formType;

    /**
     * 动态表单中，用于作为条件流程的字段id（对应formId记录中的field数据的id）
     */
    private String conditionFieldId;

    /** 动态表单中，用于作为存储财务拨款条件的字段id **/
    private String financeFieldId;

    /**
     * （1：自由流程，2：固定流程，3：分条件流程）
     */
    private Integer type;

    /**
     * 0:未启用，1：启用
     */
    private Integer status;

    /**
     * 通知方式
     */
    private Integer noticeType;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 1:删除，0：有效
     */
    private Integer deleted;

    public String getFinanceFieldId() {
        return financeFieldId;
    }

    public void setFinanceFieldId(String financeFieldId) {
        this.financeFieldId = financeFieldId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType == null ? null : targetType.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getConditionFieldId() {
        return conditionFieldId;
    }

    public void setConditionFieldId(String conditionFieldId) {
        this.conditionFieldId = conditionFieldId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }
}