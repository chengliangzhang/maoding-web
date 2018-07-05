package com.maoding.role.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RoleDTO
 * 类描述：权限DTO
 * 作    者：MaoSF
 * 日    期：2016年7月11日-下午3:22:50
 */
public class RoleDTO extends BaseDTO {

    private String companyId;

    private String code;

    private String name;

    private int orderIndex;

    /**
     * 是否拥有当前角色 1=是，0＝否
     */
    private String isWithRole;

    List<PermissionDTO> permissionList = new ArrayList<>();


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getIsWithRole() {
        return isWithRole;
    }

    public void setIsWithRole(String isWithRole) {
        this.isWithRole = isWithRole;
    }

    public List<PermissionDTO> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<PermissionDTO> permissionList) {
        this.permissionList = permissionList;
    }

}