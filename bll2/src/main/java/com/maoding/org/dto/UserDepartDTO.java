package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DepartRoleDTO
 * 类描述：组织人员列表，部门，权限信息）
 * 作    者：MaoSF
 * 日    期：2016年7月9日-下午4:20:44
 */
public class UserDepartDTO extends BaseDTO{

	/**
	 * 
	 */
	private String departId;
	 /**
     * 部门名称
     */
    private String departName;

    /**
     * 父部门id
     */
    private String pid;
    
    /**
     * 岗位
     */
    private String serverStation;
    
    /**
     * 部分下面的权限
     */
    private String role;

    /**
     * roleId(用,隔开）
     */
    private String orgRoles;
    /**
     * 保存，修改团队成员，传的组织角色
     */
    private List<String> roleList = new  ArrayList<String>();

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getServerStation() {
		return serverStation;
	}

	public void setServerStation(String serverStation) {
		this.serverStation = serverStation;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public String getOrgRoles() {
		return orgRoles;
	}

	public void setOrgRoles(String orgRoles) {
		this.orgRoles = orgRoles;
	}
    
    
}
