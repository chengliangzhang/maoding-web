package com.maoding.role.dto;

import java.util.ArrayList;
import java.util.List;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.role.entity.RoleEntity;

public class UserOrgRoleDTO extends BaseDTO{

	/**
	 * 组织id
	 */
	private String orgId;
	/**
	 * 组织名称
	 */
	private String orgName;
	
	/**
	 * 所在组织下的角色
	 */
	private List<RoleEntity> roleList = new ArrayList<RoleEntity>();

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<RoleEntity> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleEntity> roleList) {
		this.roleList = roleList;
	}
	
	
}
