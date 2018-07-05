package com.maoding.user.entity;

import com.maoding.core.base.entity.BaseEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：AttentionEntity
 * 类描述：个人关注
 * 作    者：wrb
 * 日    期：2017年01月06日-下午16:38:05
 */
public class AttentionEntity extends BaseEntity implements java.io.Serializable{

    /**
     * 根据类型来存相对应的ID(项目ID,)
     */
    private String targetId;

    /**
     * 类型(1=关注项目)
     */
    private String type;

    /**
     * 组织ID
     */
    private String companyId;

    /**
     * 组织人员ID
     */
    private String companyUserId;


    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId == null ? null : companyUserId.trim();
    }

}