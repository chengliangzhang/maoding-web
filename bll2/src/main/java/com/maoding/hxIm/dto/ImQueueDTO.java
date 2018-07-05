package com.maoding.hxIm.dto;


import com.maoding.core.util.StringUtil;

import java.io.Serializable;

public class ImQueueDTO implements Serializable{
    private static final long serialVersionUID = -295422703255886286L;
    private String id;

    /** 操作队列号 */
    private Long queueNo;

    /** IMAccount_ID或IMGroup_ID **/
    private String targetId;

    /** 操作类型 */
    private String operation;

    /** 消息内容，JSON */
    private String content;

    /** 重试次数 */
    private Integer retry;

    /** 忽略修正补偿 */
    private Boolean ignoreFix;

    /** 是否属于补偿修正 */
    public boolean isFixMode() {
        return !StringUtil.isNullOrEmpty(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Boolean getIgnoreFix() {
        return ignoreFix;
    }

    public void setIgnoreFix(Boolean ignoreFix) {
        this.ignoreFix = ignoreFix;
    }
}
