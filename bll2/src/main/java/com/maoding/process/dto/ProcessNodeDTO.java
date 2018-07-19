package com.maoding.process.dto;

public class ProcessNodeDTO {

    private String id;

    private String processId;//所属流程id

    private String nodeName;//节点的名称

    private String nodeValue;//节点的值

    private String description;//描述

    private String relationContent;//节点关联的内容

    private Integer nodeType;//节点的类型

    private Integer isAllAgree;

    /** 项目收支流程 **/

    private Integer invoiceStatus;//发票状态，1：选择状态

    private Integer receiveOrPayStatus;//到账/付款状态，1：选择状态

    private Integer receiveOrPayAbleStatus;//应收状态，1：选择状态

    private Integer syncStatus;//同步状态 1：选择状态

    private String operatorName;//操作人

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
        this.processId = processId == null ? null : processId.trim();
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName == null ? null : nodeName.trim();
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getIsAllAgree() {
        return isAllAgree;
    }

    public void setIsAllAgree(Integer isAllAgree) {
        this.isAllAgree = isAllAgree;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Integer getReceiveOrPayStatus() {
        return receiveOrPayStatus;
    }

    public void setReceiveOrPayStatus(Integer receiveOrPayStatus) {
        this.receiveOrPayStatus = receiveOrPayStatus;
    }

    public Integer getReceiveOrPayAbleStatus() {
        return receiveOrPayAbleStatus;
    }

    public void setReceiveOrPayAbleStatus(Integer receiveOrPayAbleStatus) {
        this.receiveOrPayAbleStatus = receiveOrPayAbleStatus;
    }

    public Integer getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Integer syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getRelationContent() {
        return relationContent;
    }

    public void setRelationContent(String relationContent) {
        this.relationContent = relationContent;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
