package com.maoding.process.dto;

import com.maoding.core.base.dto.BaseDTO;

public class QueryProcessDTO extends BaseDTO {

    //组织id
    private String companyId;

    //流程id
    private String processId;

    //流程节点的id
    private String nodeId;

    //节点条件的类型
    private String dataType;

    //流程节点的类型
    private Integer nodeType;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }
}
