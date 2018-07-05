package com.maoding.role.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.role.dao.RoleDao;
import com.maoding.role.dto.RoleDTO;
import com.maoding.role.entity.RoleEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RoleUserDaoImpl
 * 类描述：权限--用户Dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午11:48:29
 */
@Service("roleDao")
public class RoleDaoImpl extends GenericDao<RoleEntity> implements RoleDao {

	@Override
	public int insert(RoleEntity entity){
		int seq = this.getMaxRoleIndex();
		entity.setOrderIndex(seq);
		return  super.insert(entity);
	}


	@Override
	public List<RoleEntity> getCompanyRole(String companyId) {
		return this.sqlSession.selectList("RoleEntityMapper.selectByCompanyId", companyId);
	}
	@Override
	public List<RoleDTO> getCompanyRoleDTO(String companyId) {
		return this.sqlSession.selectList("RoleDTOMapper.selectByCompanyId", companyId);
	}

	@Override
	public List<RoleDTO> getRolePermissionByType(String type) {
		return this.sqlSession.selectList("RoleDTOMapper.getRolePermissionByType", type);
	}

	@Override
	public List<RoleEntity> getUserRolesByOrgId(String userId, String orgId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("orgId", orgId);
		return this.sqlSession.selectList("RoleEntityMapper.getUserRolesByOrgId", param);
	}


	/**
	 * 方法描述：获取最大index
	 * 作者：MaoSF
	 * 日期：2016/11/2
	 *
	 * @param:
	 * @return:
	 */
	@Override
	public int getMaxRoleIndex() {
		return this.sqlSession.selectOne("RoleEntityMapper.getMaxRoleIndex");
	}
}
