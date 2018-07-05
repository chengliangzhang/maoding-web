package com.maoding.project.dto;

/**
 * Created by Wuwq on 2016/10/27.
 */
public class ProjectDesignUser {

    private String id;
    private String userName;
    private String completeTime;
    private String companyUserId;
    private String accountId;
    private String headImg;
    private String cellphone;
    private String email;
    private Integer memberType;
    /**
     * 是否可以设置经营负责人/设计负责人（1：可以，0：不可以）
     */
    private int isUpdateOperator;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public int getIsUpdateOperator() {
        return isUpdateOperator;
    }

    public void setIsUpdateOperator(int isUpdateOperator) {
        this.isUpdateOperator = isUpdateOperator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
