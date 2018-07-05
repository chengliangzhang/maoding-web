package com.maoding.role.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.role.entity.RoleUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RoleUserDao
 * 类描述：权限--用户（dao）
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午5:20:47
 */
public interface RoleUserDao extends BaseDao<RoleUserEntity>{

	/**
	 * 方法描述：删除用户所在组织的所有权限（companyId必传）
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午5:53:54
	 * @param param(userId,companyId,orgId，roleId)
	 * @return
	 */
	public int deleteUserRole(Map<String,Object> param);

	/**
	 * 方法描述：把角色对应的权限全部赋予给相应的用户 2011-11-11 (数据迁移使用)
	 * 作者：MaoSF
	 * 日期：2016/11/11
	 * @param:
	 * @return:
	 */
	public List<RoleUserEntity> selectAllRoleUser();
}
