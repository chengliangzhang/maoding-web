package com.maoding.role.dto;

import java.util.ArrayList;
import java.util.List;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.role.entity.RoleUserEntity;

public class RoleUserDTO extends BaseDTO{

	private String userId;
	
	private String companyId;
	
	private List<UserOrgRoleDTO> orgRoles = new ArrayList<UserOrgRoleDTO>();

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public List<UserOrgRoleDTO> getOrgRoles() {
		return orgRoles;
	}

	public void setOrgRoles(List<UserOrgRoleDTO> orgRoles) {
		this.orgRoles = orgRoles;
	}


	
	
}
