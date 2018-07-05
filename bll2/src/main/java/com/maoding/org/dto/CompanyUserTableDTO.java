package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyUserTableEntity
 * 类描述：公司人员信息(组织人员列表Entity，此Entity不对应数据库中的表)
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午5:38:15
 */
public class CompanyUserTableDTO extends BaseDTO {

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
     * 手机号码
     */
    private String cellphone;

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
     *排序
     */
    private int seq;
    /**
     * 离职原因
     */
    private String departureReason;
    
    /**
     * 职业注册
     */
    private String occupationRegistration;
    
    /**
     * 职称
     */
    private String  positionalTitle;
    
    /**
     * 性别
     */
    private String sex;
    
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 当前人所在选中组织的职位
     */
    private String orgServerStation;

    /**
     * 当前选中组织（组织所在的团队）的管理员标示，null：非管理员，not null ：管理员（用于admin项目，组织架构，控制人员列表删除）
     */
    private String adminFlag;
    /**
     * roleId(用,隔开）
     */
    private String roleIds;

    /**
     * roleName(用,隔开）
     */
    private String roleNames;

    /**权限*/
    private String roleCodes;

    /**
     * 部门信息
     */
    private List<UserDepartDTO> departList = new ArrayList<UserDepartDTO>();

//    /**
//     * 权限列表
//     */
//    private List<PermissionDTO> permissionList = new ArrayList<PermissionDTO>();


    public String getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(String roleCodes) {
        this.roleCodes = roleCodes;
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

	public List<UserDepartDTO> getDepartList() {
		return departList;
	}

	public void setDepartList(List<UserDepartDTO> departList) {
		this.departList = departList;
	}

	public String getOccupationRegistration() {
		return occupationRegistration;
	}

	public void setOccupationRegistration(String occupationRegistration) {
		this.occupationRegistration = occupationRegistration;
	}

	public String getPositionalTitle() {
		return positionalTitle;
	}

	public void setPositionalTitle(String positionalTitle) {
		this.positionalTitle = positionalTitle;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

    public String getOrgServerStation() {
        return orgServerStation;
    }

    public void setOrgServerStation(String orgServerStation) {
        this.orgServerStation = orgServerStation;
    }

    public String getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(String adminFlag) {
        this.adminFlag = adminFlag;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

//    public List<PermissionDTO> getPermissionList() {
//        return permissionList;
//    }
//
//    public void setPermissionList(List<PermissionDTO> permissionList) {
//        this.permissionList = permissionList;
//    }
}