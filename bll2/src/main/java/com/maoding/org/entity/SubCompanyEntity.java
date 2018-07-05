package com.maoding.org.entity;

import com.maoding.core.base.entity.BaseEntity;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：BusinessPartnerEntity
 * 类描述：分支机构实体
 * 作    者：wangrb
 * 日    期：2015年11月18日-下午5:28:09
 */
public class SubCompanyEntity extends BaseEntity{

    /**
     * 昵称
     */
    private String nickName;

    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }
}