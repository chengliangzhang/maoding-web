package com.maoding.project.entity;

import com.maoding.core.base.entity.BaseEntity;

public class ProjectProcessNodeEntity extends BaseEntity {
    private String processId;

    private String nodeName;

    /**
     * 节点在流程中给的序号
     */
    private int seq;

    /**
     * 节点在流程中所排的顺序
     */
    private int nodeSeq;

    private String companyUserId;

    private int status;//2:未完成，1：完成

    /**
     *完成时间
     */
    private String completeTime;

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

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId == null ? null : companyUserId.trim();
    }

    public int getNodeSeq() {
        return nodeSeq;
    }

    public void setNodeSeq(int nodeSeq) {
        this.nodeSeq = nodeSeq;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }
}