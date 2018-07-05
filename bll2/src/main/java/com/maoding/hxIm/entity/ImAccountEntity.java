package com.maoding.hxIm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maoding.core.base.entity.BaseEntity;

/**
 * IM账号（Id等于Web_Account_Id，作为环信的用户名，Web_Account_Name作为环信的昵称）
 */
public class ImAccountEntity extends BaseEntity {

    /** 账号状态，0-待创建 1-已启用 2-已禁用 */
    private Integer accountStatus;
    /** 最后操作队列号 */
    private Long lastQueueNo;
    /** 版本控制(乐观锁） */
    @JsonIgnore
    private Long upVersion;
    /** 已删除 **/
    private Boolean deleted;

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Long getLastQueueNo() {
        return lastQueueNo;
    }

    public void setLastQueueNo(Long lastQueueNo) {
        this.lastQueueNo = lastQueueNo;
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
