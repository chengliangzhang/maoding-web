package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyUserDTO
 * 类描述：批量导入公司人员DTO
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午5:38:15
 */
public class ImportFileCompanyUserDTO extends BaseDTO implements java.io.Serializable{

	/**
	 * 手机号码
	 */
	private String cellphone;
    
    /**
     * 用户id
     */
    private String userId;
    
    /**
     * 公司id
     */
    private String companyId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 入职时间
     */
    private String entryTime;


    /**
     * 联系号码
     */
    private String phone;

    /**
     * 邮箱（所在公司邮箱）
     */
    private String email;

    
    /**
     * 部门（批量导入的时候，部门名称）
     */
    private String departName;
    
    /**
     * 岗位
     */
    private String serverStation;
    
    /**
     * 关联类别(1.自己创建的组织；0.我申请，2.受邀请)
     */
    private String relationType;

    /**
     * 审核状态(0.申请加入，1.审核通过，2.邀请加入，3.审批拒绝。4:离职。自己创建的企业此字段为1)
     */
    private String auditStatus;
    
    /**
     * 备注
     */
    private String remark;
    
    
    public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = (cellphone == null) ? null : cellphone.replace(" ","").replace("-","").trim();
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.replace(" ","").replace("-","").trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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

	public String getServerStation() {
		return serverStation;
	}

	public void setServerStation(String serverStation) {
		this.serverStation = serverStation;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}


	
}