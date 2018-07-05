package com.maoding.org.entity;

import com.maoding.core.base.entity.BaseEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyNviteEntity
 * 类描述：公司邀请码表
 * 作    者：Chenzhujie
 * 日    期：2017年2月27日-上午10:31:58
 */
public class CompanyInviteEntity extends BaseEntity implements java.io.Serializable {
    /**
     * 被邀请的手机号
     */
    private String inviteCellphone;

     /**
      * 邀请公司id
      */
    private String companyId;


    /**
     *有效时间
     */
    private String effectiveTime;

    private String url;//邀请的连接地址

    /**
     * 邀请类型（1：分公司,2:事业合伙人，3：外部合作）
     */
    private String type;

    /**
     * 项目ID（外部合作才必传）
     */
    private String projectId;

    public String getInviteCellphone() {
        return inviteCellphone;
    }

    public void setInviteCellphone(String inviteCellphone) {
        this.inviteCellphone = inviteCellphone;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}