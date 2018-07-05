package com.maoding.user.dto;

/**
 * Created by Chengliang.zhang on 2017/7/26.
 */
public class CurrentUserDTO {
    /** 当前用户ID */
    String userId;
    /** 当前用户所选择单位ID */
    String companyId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
