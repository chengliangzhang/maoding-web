package com.maoding.hxIm.entity;

import com.maoding.core.base.entity.BaseEntity;

/**
 * IM群成员
 */
public class ImGroupMemberEntity extends BaseEntity {

    /** 数据库群ID */
    private String groupId;

    /** 账号ID */
    private String accountId;

    /** 组织ID */
    private String orgId;

    /** 公司用户id,目前仅供自定义群使用 */
    private String companyUserId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }
}
