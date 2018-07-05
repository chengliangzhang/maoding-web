package com.maoding.org.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.io.Serializable;

/**
 * Created by Chengliang.zhang on 2017/5/6.
 */
public class PartnerEntity extends BaseEntity implements Serializable {
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 合伙人所在公司
     * 如果为空，则说明合伙人尚未指定组织
     */
    private String companyId;
    /**
     * 合伙人所属项目
     * 如果为空，则说明合伙人是分支机构/事业合伙人
     */
    private String projectId;
    /**
     * 发出邀请的公司
     */
    private String fromCompanyId;
    /**
     * 发出邀请的用户
     */
    private String fromUserId;
    /**
     * 合伙人类别
     */
    private Integer type;
    /**
     * 合伙人在发出邀请的公司内的别名
     */
    private String nickName;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
