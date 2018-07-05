package com.maoding.role.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.role.dao.UserPermissionDao;
import com.maoding.role.entity.UserPermissionEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("userPermissionDao")
public class UserPermissionDaoImpl  extends GenericDao<UserPermissionEntity> implements UserPermissionDao {


    /**
     * 方法描述：userId
     * 作者：MaoSF
     * 日期：2016/11/2
     *
     * @param map
     * @param:userId,companyId
     * @return:
     */
    @Override
    public int deleteByUserId(Map<String, Object> map) {
        return this.sqlSession.delete("UserPermissionEntityMapper.deleteByUserId",map);
    }

    /**
     * 方法描述：userId
     * 作者：MaoSF
     * 日期：2016/11/2
     *
     * @param map
     * @param:userId,companyId，permissionList
     * @return:
     */
    @Override
    public int deleteByUserIdAndPermission(Map<String, Object> map) {
        return this.sqlSession.delete("UserPermissionEntityMapper.deleteByUserIdAndPermission",map);
    }

    /**
     * 方法描述：删除权限里面的所有人员
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:userId,companyId，permissionList
     */
    @Override
    public int deleteByPermissionId(Map<String, Object> map){
        return this.sqlSession.delete("UserPermissionEntityMapper.deleteByPermissionId",map);
    }

    /**
     * 方法描述：根据userId，roleList查询用户所在的权限
     * 作者：MaoSF
     * 日期：2016/11/10
     */
    @Override
    public List<String> getPermissionByUserAndRole(Map<String, Object> map) {
        return this.sqlSession.selectList("GetPermissionByUserAndRoleMapper.getPermissionByUserAndRole",map);
    }

    /**
     * 方法描述：获取最大的seq
     * 作者：MaoSF
     * 日期：2017/6/13
     *
     * @param companyId
     * @param permissionId
     */
    @Override
    public int getMaxSeq(String companyId, String permissionId) {
        Map<String,Object> map = new HashMap<>();
        map.put("companyId",companyId);
        map.put("permissionId",permissionId);
        return this.sqlSession.selectOne("UserPermissionEntityMapper.getMaxSeq",map);
    }
}