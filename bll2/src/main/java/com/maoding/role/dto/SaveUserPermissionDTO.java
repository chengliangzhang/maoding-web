package com.maoding.role.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveUserPermissionDTO extends BaseDTO {

    /**
     * userID
     */
    private String userId;
    /**
     * 权限ID
     */
    private List<String> permissionIds = new ArrayList<String>();

    /**
     * 删除权限的时候，带了roleId，则删除此角色
     */
    private List<String> roleIds = new ArrayList<String>(); //

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<String> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}