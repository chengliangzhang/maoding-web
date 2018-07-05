package com.maoding.role.dto;

import com.maoding.org.dto.DepartDTO;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/6/22.
 */
public class PermissionForOrgAndUserDTO {
    /**
     * 用户ID
     */
    String userId;
    /**
     * 公司ID
     */
    String companyId;
    /**
     * 公司名称，如果有别名则为别名
     */
    String companyName;
    /**
     * 公司短名称
     */
    String companyShortName;
    /**
     * 部门信息
     */
    List<DepartDTO> departList;
    /**
     * 角色信息
     */
    List<RoleDTO> roleList;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public List<DepartDTO> getDepartList() {
        return departList;
    }

    public void setDepartList(List<DepartDTO> departList) {
        this.departList = departList;
    }

    public List<RoleDTO> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleDTO> roleList) {
        this.roleList = roleList;
    }
}
