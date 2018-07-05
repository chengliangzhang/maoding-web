package com.maoding.org.entity;

import com.maoding.core.base.entity.BaseEntity;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：BusinessPartnerEntity
 * 类描述：合作伙伴实体
 * 作    者：wangrb
 * 日    期：2015年11月18日-下午5:28:09
 */
public class BusinessPartnerEntity extends BaseEntity{

    /**
     * 昵称
     */
    private String nickName;
    /**
     * 公司id
     */
    private String companyId;
    /**
     * 类型
     */
    private Integer type;

    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}