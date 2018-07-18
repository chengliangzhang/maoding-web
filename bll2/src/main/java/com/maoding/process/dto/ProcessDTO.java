package com.maoding.process.dto;

public class ProcessDTO {

    private String id;//记录id

    private String processId;//流程id

    private String processName;//流程名称

    private String description;//说明

    private String companyId;

    private String companyName;//使用团队

    private String relationCompanyId;//关联团队Id

    private String relationCompanyName;//关联团队

    private String updateUserName;//修改人

    private Integer processType;//流程类型（请参考ProcessConst）

    private Integer companyType;//组织类型（前端暂不需要）

    private Integer status;//是否被选择的状态（1：被选中，0:未被选中)

    private String isTemplate;//1：系统默认的

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRelationCompanyId() {
        return relationCompanyId;
    }

    public void setRelationCompanyId(String relationCompanyId) {
        this.relationCompanyId = relationCompanyId;
    }

    public String getRelationCompanyName() {
        return relationCompanyName;
    }

    public void setRelationCompanyName(String relationCompanyName) {
        this.relationCompanyName = relationCompanyName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Integer getProcessType() {
        return processType;
    }

    public void setProcessType(Integer processType) {
        this.processType = processType;
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(String isTemplate) {
        this.isTemplate = isTemplate;
    }
}
