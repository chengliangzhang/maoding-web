package com.maoding.org.entity;


import com.maoding.core.base.entity.BaseEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：OrgUserEntity
 * 类 描 述：团队人员与部门与组织的关系表
 * 作    者：wangrb
 * 日    期：2016年5月30日-下午4:42:35
 */
public class OrgUserEntity extends BaseEntity implements java.io.Serializable{
	
	private String companyId;

    private String orgId;

    private String cuId;

    private String userId;
    
    private String serverStation;

	private int seq;
    
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getCuId() {
        return cuId;
    }

    public void setCuId(String cuId) {
        this.cuId = cuId == null ? null : cuId.trim();
    }

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServerStation() {
		return serverStation;
	}

	public void setServerStation(String serverStation) {
		this.serverStation = serverStation;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
}