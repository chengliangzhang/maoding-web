package com.maoding.process.entity;

import com.maoding.core.base.entity.BaseEntity;

public class ProcessEntity extends BaseEntity{

    private String companyId;

    private String processName;

    private String description;

    private Integer processType;

    private Integer seq;

    private Integer deleted;

    private Integer lastProcess;

    private Integer startFlag;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName == null ? null : processName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getProcessType() {
        return processType;
    }

    public void setProcessType(Integer processType) {
        this.processType = processType;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getLastProcess() {
        return lastProcess;
    }

    public void setLastProcess(Integer lastProcess) {
        this.lastProcess = lastProcess;
    }

    public Integer getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(Integer startFlag) {
        this.startFlag = startFlag;
    }
}