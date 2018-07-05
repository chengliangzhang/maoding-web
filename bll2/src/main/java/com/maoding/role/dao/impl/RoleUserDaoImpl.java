package com.maoding.role.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.role.dao.RoleUserDao;
import com.maoding.role.entity.RoleUserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RoleUserDaoImpl
 * 类描述：权限--用户Dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午11:48:29
 */
@Service("roleUserDao")
public class RoleUserDaoImpl extends GenericDao<RoleUserEntity> implements RoleUserDao {

	@Override
	public int deleteUserRole(Map<String,Object> param) {
		return this.sqlSession.delete("RoleUserEntityMapper.deleteUserRole", param);
	}

	/**
	 * 方法描述：把角色对应的权限全部赋予给相应的用户 2011-11-11(数据迁移使用)
	 * 作者：MaoSF
	 * 日期：2016/11/11
	 */
	@Override
	public List<RoleUserEntity> selectAllRoleUser() {
		return this.sqlSession.selectList("RoleUserEntityMapper.selectAllRoleUser");
	}

}
