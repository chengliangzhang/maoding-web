package com.maoding.role.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.role.dto.SaveRolePermissionDTO;
import com.maoding.role.entity.RolePermissionEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RolePermissionService
 * 类描述：角色视图表（dao）
 * 作    者：wrb
 * 日    期：2016年11月2日-上午11:38:47
 */
public interface RolePermissionService extends BaseService<RolePermissionEntity> {

    /**
     * 方法描述：保存角色权限关联数据
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:
     * @return:
     */
    AjaxMessage saveRolePermission(SaveRolePermissionDTO dto) throws Exception;


}