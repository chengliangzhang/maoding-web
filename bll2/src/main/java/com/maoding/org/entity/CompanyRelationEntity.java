package com.maoding.org.entity;

import com.maoding.core.base.entity.BaseEntity;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyRelationEntity
 * 类描述：公司加盟实体
 * 作    者：MaoSF
 * 日    期：2015年11月27日-上午11:59:17
 */
public class CompanyRelationEntity extends BaseEntity implements java.io.Serializable{

    /**
     * 事业合伙人类型
     */
    private String typeId;

	/**
	 * 公司id
	 */
	private String orgId;

    /**
     * 父id
     */
    private String orgPid;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }


    public String getOrgPid() {
        return orgPid;
    }

    public void setOrgPid(String orgPid) {
        this.orgPid = orgPid == null ? null : orgPid.trim();
    }

}