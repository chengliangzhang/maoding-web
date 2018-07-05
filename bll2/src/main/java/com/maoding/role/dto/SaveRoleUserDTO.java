package com.maoding.role.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveRoleUserDTO extends BaseDTO{

	private String roleId;

	List<String> userIds = new ArrayList<String>();

	List<String> deleteUserIds = new ArrayList<String>();

	public List<String> getDeleteUserIds() {
		return deleteUserIds;
	}

	public void setDeleteUserIds(List<String> deleteUserIds) {
		this.deleteUserIds = deleteUserIds;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
}
