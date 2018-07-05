package com.maoding.org.entity;

import com.maoding.core.base.entity.BaseEntity;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyRelationEntity
 * 类描述：公司加盟（审核）实体
 * 作    者：MaoSF
 * 日    期：2015年11月27日-上午11:59:17
 */
public class CompanyRelationAuditEntity extends BaseEntity implements java.io.Serializable{


	/**
	 * 公司id
	 */
	private String orgId;

	/**
	 * 企业状态（0审核通过，1审核拒绝，2审核中）
	 */
    private String auditStatus;
    
    /**
     * 关联类别(0－申请，1－受邀请)
     */
    private String relationType;
    /**
     * 父id
     */
    private String orgPid;
    /**
     * 加入时间
     */
    private String joinDate;
    /**
     * 通过时间
     */
    private String passDate;
    /**
     * 附加说明
     */
    private String extraComment;
    
    /**
     * 挂靠类型
     */
    private String type;

    
    
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getOrgPid() {
        return orgPid;
    }

    public void setOrgPid(String orgPid) {
        this.orgPid = orgPid == null ? null : orgPid.trim();
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getPassDate() {
        return passDate;
    }

    public void setPassDate(String passDate) {
        this.passDate = passDate;
    }


	/**
	 * 获取：extraComment
	 */
	public String getExtraComment() {
		return extraComment;
	}

	/**
	 * 设置：extraComment
	 */
	public void setExtraComment(String extraComment) {
		this.extraComment = extraComment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if(null==type || "".equals(type)){
			type = "0";
		}
		this.type = type;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	
}