package com.maoding.role.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;


public class PermissionDTO extends BaseDTO {

    /**
     * code值
     */
    private String code;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 父权限ID
     */
    private String pid;
    /**
     * 根权限ID
     */
    private String rootId;
    /**
     * 排序
     */
    private int seq;

    /**
     * 对应RolePermission中permissionId（如果为null,则没有选择，否则选择）
     */
    private String permissionId;

    /**
     * 权限描述
     */
    private String description;

    private int type;//是否被选中1,2：选中，0：非选中

    /**
     * 子项
     */
    List<PermissionDTO> childList = new ArrayList<PermissionDTO>();

    /**
     * 权限中对应的人员
     */
    List<UserPermissionDTO> companyUserList = new ArrayList<UserPermissionDTO>();

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId == null ? null : rootId.trim();
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public List<PermissionDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<PermissionDTO> childList) {
        this.childList = childList;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public int getType() {
//        type = 0;
//        if(!StringUtil.isNullOrEmpty(this.permissionId)){
//            type = 1;
//        }
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserPermissionDTO> getCompanyUserList() {
        return companyUserList;
    }

    public void setCompanyUserList(List<UserPermissionDTO> companyUserList) {
        this.companyUserList = companyUserList;
    }
}