package com.maoding.hxIm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maoding.core.base.entity.BaseEntity;


public class ImQueueEntity extends BaseEntity {
    /** 操作队列号 */
    private Long queueNo;
    /** IMAccount_ID或IMGroup_ID **/
    private String targetId;
    /** 操作类型 */
    private String operation;
    /** 消息内容，JSON */
    private String content;
    /** 原因 */
    private String reason;
    /** 重试次数 */
    private Integer retry;
    /** 状态，0-待执行 1-执行中 2-终止 */
    private Integer queueStatus;
    /** 版本控制(乐观锁） */
    @JsonIgnore
    private Long upVersion;
    /** 已删除 **/
    private Boolean deleted;

    public Long getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(Long queueNo) {
        this.queueNo = queueNo;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Integer getQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(Integer queueStatus) {
        this.queueStatus = queueStatus;
    }

    public Long getUpVersion() {
        return upVersion;
    }

    public void setUpVersion(Long upVersion) {
        this.upVersion = upVersion;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
