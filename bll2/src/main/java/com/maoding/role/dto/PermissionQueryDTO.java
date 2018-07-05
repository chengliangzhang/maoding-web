package com.maoding.role.dto;

/**
 * Created by Chengliang.zhang on 2017/6/22.
 */
public class PermissionQueryDTO {
    /**
     * 用户ID
     */
    String accountId;
    /**
     * 组织ID
     */
    String orgId;

    /**
     * 不同组织类型获取不同的权限
     */
    String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
