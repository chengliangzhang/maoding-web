package com.maoding.org.entity;
import com.maoding.core.base.entity.BaseEntity;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：OrgEntity
 * 类描述：组织基类（公司，部门，公司人员）
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午5:49:51
 */
public class OrgEntity extends BaseEntity implements java.io.Serializable{

    /**
     * 组织状态
     */
    private String orgStatus;
	/**
	 * 组织类型（公司：0部门：1，分支机构：2，合作伙伴为3,组织成员：4)
	 */
    private String orgType;

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgStatus() {
        return orgStatus;
    }

    public void setOrgStatus(String orgStatus) {
        this.orgStatus = orgStatus;
    }
}