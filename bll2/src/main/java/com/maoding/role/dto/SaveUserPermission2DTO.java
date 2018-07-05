package com.maoding.role.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：SaveUserPermission2DTO
 * 类描述：公司人员权限DTO（从权限中添加成员）
 * 作    者：MaoSF
 * 日    期：2016年11月16日-下午5:38:15
 */
public class SaveUserPermission2DTO extends BaseDTO {

    /**
     * 权限ID
     */
    private String permissionId;
    /**
     * userID
     */
    private List<String> userIds = new ArrayList<String>();

    /**
     * 删除userID
     */
    private List<String> deleteUserIds = new ArrayList<String>();

    public List<String> getDeleteUserIds() {
        return deleteUserIds;
    }

    public void setDeleteUserIds(List<String> deleteUserIds) {
        this.deleteUserIds = deleteUserIds;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}