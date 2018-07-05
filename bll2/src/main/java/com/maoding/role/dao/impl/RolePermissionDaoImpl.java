package com.maoding.role.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.role.dao.RolePermissionDao;
import com.maoding.role.entity.RolePermissionEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("rolePermissionDao")
public class RolePermissionDaoImpl extends GenericDao<RolePermissionEntity> implements RolePermissionDao {


    @Override
    public int deleteByRoleId(Map<String, Object> map) {
        return this.sqlSession.delete("RolePermissionEntityMapper.deleteByRoleId",map);
    }


    @Override
    public List<RolePermissionEntity> getAllDefaultPermission(){
        return this.sqlSession.selectList("RolePermissionEntityMapper.getAllDefaultPermission");
    }

    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2016/11/2
     */
    @Override
    public int deleteByCompanyId(String companyId) {
        return this.sqlSession.delete("RolePermissionEntityMapper.deleteByCompanyId",companyId);
    }

    /**
     * 方法描述：获取当前角色下所有的权限关系（用于给用户添加角色的时候，把权限赋给用户）
     * 作者：MaoSF
     * 日期：2016/11/10
     */
    @Override
    public List<RolePermissionEntity> getPermissionByRole(String roleId,String companyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleId",roleId);
        map.put("companyId",companyId);
        return this.sqlSession.selectList("RolePermissionEntityMapper.getPermissionByRole",map);
    }


}