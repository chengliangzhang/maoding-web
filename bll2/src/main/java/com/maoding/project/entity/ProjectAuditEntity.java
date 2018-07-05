package com.maoding.project.entity;

import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.StringUtil;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectAuditEntity
 * 类描述：权限实体
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:04:50
 */
public class ProjectAuditEntity extends BaseEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 审批流程的节点id
     */
    private String nodeId;

    /**
     * 审核日期
     */
    private String auditDate;

    /**
     * 审核类型(1=项目评审，2＝项目签订,3＝项目备案，4＝设计承包费审核,5=我要报销审核)
     */
    private String auditType;

    /**
     * 审批状态(0=通过，1＝不通过即拒绝)
     */
    private String auditStatus;

    /**
     * 来源id（项目id或者节点id或者服务内容ID）
     */
    private String fromId;

    public ProjectAuditEntity() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getAuditDate() {
       if(StringUtil.isNullOrEmpty(auditDate)) {
           return null;
       }
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
}
