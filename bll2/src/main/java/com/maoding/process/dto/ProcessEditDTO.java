package com.maoding.process.dto;

import com.maoding.core.base.dto.BaseDTO;

public class ProcessEditDTO extends BaseDTO {

    private String processId;

    private String processName;//流程名称

    private String description;//说明

    private String companyId;//使用团队

    private String relationCompanyId;//关联团队

    private Integer processType;//流程类型（请参考ProcessConst）

    private Integer companyType;//组织类型（前端暂不需要）

    private Integer status;//选中状态（1：被选择，0：取消选中）

    /** 项目收支流程节点状态 **/
    private Integer nodeStatus1;//应收状态，1：选择状态，0：取消状态
    private Integer nodeStatus2;//到账/付款状态 1：选择状态 0：取消状态
    private Integer nodeStatus3;//同步状态 1：选择状态 0：取消状态
    private String relationContent;//关联的内容

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNodeStatus1() {
        return nodeStatus1;
    }

    public void setNodeStatus1(Integer nodeStatus1) {
        this.nodeStatus1 = nodeStatus1;
    }

    public Integer getNodeStatus2() {
        return nodeStatus2;
    }

    public void setNodeStatus2(Integer nodeStatus2) {
        this.nodeStatus2 = nodeStatus2;
    }

    public Integer getNodeStatus3() {
        return nodeStatus3;
    }

    public void setNodeStatus3(Integer nodeStatus3) {
        this.nodeStatus3 = nodeStatus3;
    }

    public String getRelationContent() {
        return relationContent;
    }

    public void setRelationContent(String relationContent) {
        this.relationContent = relationContent;
    }
}
