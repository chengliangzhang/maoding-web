package com.maoding.role.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.role.entity.RolePermissionEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RolePermissionService
 * 类描述：角色视图表（dao）
 * 作    者：wrb
 * 日    期：2016年11月2日-上午11:38:47
 */
public interface RolePermissionDao extends BaseDao<RolePermissionEntity> {


    /**
     * 方法描述：根据roleId删除
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:roleId,companyId
     */
    int deleteByRoleId(Map<String,Object> map);

    /**
     * 方法描述：获取所有默认权限（用于创建组织配置权限）
     * 作者：MaoSF
     * 日期：2016/11/2
     */
    List<RolePermissionEntity> getAllDefaultPermission();

    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2016/11/2
     */
    int deleteByCompanyId(String companyId);

    /**
     * 方法描述：获取当前角色下所有的权限关系（用于给用户添加角色的时候，把权限赋给用户）
     * 作者：MaoSF
     * 日期：2016/11/10
     */
    List<RolePermissionEntity> getPermissionByRole(String roleId,String companyId);


}