package com.maoding.org.dto;

import com.maoding.attach.dto.FilePathWithBaseDTO;
import com.maoding.org.entity.OrgUserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyUserDTO
 * 类描述：公司人员DTO
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午5:38:15
 */
public class CompanyUserDTO extends FilePathWithBaseDTO implements java.io.Serializable{

	/**
	 * 手机号码
	 */
	private String cellphone;
	/**
	 * 公司id
	 */
    private String companyId;
    
    /**
     * 用户id
     */
    private String userId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 关联类别(1.自己创建的组织；0.我申请，2.受邀请)
     */
    private String relationType;

    /**
     * 审核状态(0.申请加入，1.审核通过，2.邀请加入，3.审批拒绝。4:离职。自己创建的企业此字段为1)
     */
    private String auditStatus;

    /**
     * 员工类别（全职或兼职）
     */
    private String employeeType;

    /**
     * 员工状态(在职或离职)
     */
    private String employeeStatus;

    /**
     * 入职时间
     */
    private String entryTime;

    /**
     * 离职时间
     */
    private String departureTime;

    /**
     * 说明
     */
    private String illustration;

    /**
     * 联系号码
     */
    private String phone;

    /**
     * 邮箱（所在公司邮箱）
     */
    private String email;

    /**
     * 离职原因
     */
    private String departureReason;
    
    /**
     * 公司名称
     */
    private String companyName;
    
    /**
     * 部门（批量导入的时候，部门名称）
     */
    private String departName;
    
    /**
     * 备注
     */
    private String remark;

    /**
     *排序
     */
    private int seq;

    /**
     * 组织id（用于通知公告接收人员 数据携带）
     */
    private String orgId;
    
    /**
     * 所在部门orgUserEntity信息（新增编辑功能）
     */
    private List<OrgUserEntity> orgList =new ArrayList<OrgUserEntity>();
    
    /**
     * 所在部门orgUserEntity信息（删除部门集合）（此属性仅用于修改用户信息，删除已有部门使用）
     */
    private List<OrgUserEntity> delOrgList =new ArrayList<OrgUserEntity>();
    
    public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType == null ? null : relationType.trim();
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus == null ? null : auditStatus.trim();
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType == null ? null : employeeType.trim();
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus == null ? null : employeeStatus.trim();
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getIllustration() {
        return illustration;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration == null ? null : illustration.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getDepartureReason() {
        return departureReason;
    }

    public void setDepartureReason(String departureReason) {
        this.departureReason = departureReason == null ? null : departureReason.trim();
    }

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<OrgUserEntity> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<OrgUserEntity> orgList) {
		this.orgList = orgList;
	}

	public List<OrgUserEntity> getDelOrgList() {
		return delOrgList;
	}

	public void setDelOrgList(List<OrgUserEntity> delOrgList) {
		this.delOrgList = delOrgList;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}