package com.maoding.role.entity;

import com.maoding.core.base.entity.BaseEntity;

public class RolePermissionEntity extends BaseEntity {

    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 权限ID
     */
    private String permissionId;

    private String companyId;

    /**
     * 禁止
     */
    private String forbidRelationTypeId;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId == null ? null : permissionId.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getForbidRelationTypeId() {
        return forbidRelationTypeId;
    }

    public void setForbidRelationTypeId(String forbidRelationTypeId) {
        this.forbidRelationTypeId = forbidRelationTypeId;
    }
}