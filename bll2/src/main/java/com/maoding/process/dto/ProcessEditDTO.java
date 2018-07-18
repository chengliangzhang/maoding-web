package com.maoding.process.dto;

import com.maoding.core.base.dto.BaseDTO;

public class ProcessEditDTO extends BaseDTO {

    private String processName;//流程名称

    private String description;//说明

    private String companyId;//使用团队

    private String relationCompanyId;//关联团队

    private Integer processType;//流程类型（请参考ProcessConst）

    private Integer companyType;//组织类型（前端暂不需要）

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

    public String getRelationCompanyId() {
        return relationCompanyId;
    }

    public void setRelationCompanyId(String relationCompanyId) {
        this.relationCompanyId = relationCompanyId;
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
}
