package com.maoding.org.dto;

import com.maoding.attach.dto.FilePathDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyUserTableEntity
 * 类描述：公司人员信息(组织人员列表Entity，此Entity不对应数据库中的表)
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午5:38:15
 */
public class CompanyUserDataDTO extends FilePathDTO {

    private String id;

    private String companyUserId;//组织成员id ，companyUserId = id，为了前端方便区分

    /**
     *组织id
     */
    private String companyId;
    
    /**
     * 用户id
     */
    private String userId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 用户名（account 表中的user_name)
     */
    private String accountName;

    /**
     * 手机号码
     */
    private String cellphone;

    /**
     * 组织名称
     */
    private String companyName;

    /**
     * 邮箱
     */
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyUserId() {
        if(companyUserId==null){
            companyUserId = id;
        }
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}