package com.maoding.hxIm.dto;

public class ImGroupMemberDTO {
    private String accountId;
    private String companyUserId;
    /**
     * 组织Id
     */
    private String orgId;
    public ImGroupMemberDTO() {

    }

    public ImGroupMemberDTO(String accountId, String companyUserId) {
        this.accountId = accountId;
        this.companyUserId = companyUserId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
