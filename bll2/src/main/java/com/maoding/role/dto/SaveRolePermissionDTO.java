package com.maoding.role.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveRolePermissionDTO extends BaseDTO {

    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 权限ID
     */
    private List<String> permissionIds = new ArrayList<String>();



    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public List<String> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<String> permissionIds) {
        this.permissionIds = permissionIds;
    }
}