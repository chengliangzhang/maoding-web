package com.maoding.org.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.DepartDao;
import com.maoding.org.dto.DepartRoleDTO;
import com.maoding.org.dto.UserDepartDTO;
import com.maoding.org.entity.DepartEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DepartDaoImpl
 * 类描述：部门 DaoImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:44:06
 */
@Service("departDao")
public class DepartDaoImpl extends GenericDao<DepartEntity> implements DepartDao{

	@Override
	public int insert(DepartEntity entity){
		int departSeq = this.sqlSession.selectOne("DepartEntityMapper.getmaxDepartSeq");
		entity.setDepartSeq(departSeq);
		return  super.insert(entity);
	}

    @Override
	public List<DepartEntity> getDepartByCompanyId(Map<String,Object>paraMap){
		return this.sqlSession.selectList("GetDepartByCompanyIdMapper.selectDepartBycompanyId",paraMap);
	}

	/**
	 * 方法描述：根据companyId和userId查询Departs（部门）包含公司
	 * 作        者：TangY
	 * 日        期：2016年7月8日-下午3:32:16
	 * @param paraMap （companyId（公司ID）,userId（用户Id））
	 */
	@Override
	public List<DepartEntity> getDepartByUserIdContentCompany(Map<String, Object> paraMap) {
		return this.sqlSession.selectList("GetDepartByCompanyIdMapper.getDepartByUserIdContentCompany",paraMap);
	}



	@Override
	public DepartEntity getByDepartNameAndPid(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return this.sqlSession.selectOne("DepartEntityMapper.selectByDepartNameAndPid",paraMap);
	}

	@Override
	public List<DepartEntity> getDepartsByDepartPath(String departPath) {
		return this.sqlSession.selectList("GetDepartByCompanyIdMapper.selectDepartsByDepartPath",departPath);
	}

	@Override
	public List<DepartEntity> selectDepartNodesByCompanyIds(
			Map<String, Object> param) {
		return this.sqlSession.selectList("DepartEntityMapper.selectDepartNodesByCompanyIds",param);
	}

	@Override
	public List<Map<String, Object>> selectDepartEdgesByCompanyIds(
			Map<String, Object> param) {
		return this.sqlSession.selectList("DepartEntityMapper.selectDepartEdgesByCompanyIds",param);
	}

	@Override
	public List<UserDepartDTO> selectUserDpartByParam(Map<String, Object> param) {
		return this.sqlSession.selectList("GetDepartByCompanyIdMapper.selectUserDpartByParam",param);
	}

	@Override
	public int getDepartByCompanyIdCount(String companyId) {
		// TODO Auto-generated method stub
		return this.sqlSession.selectOne("DepartEntityMapper.getDepartByCompanyIdCount",companyId);
	}

	/**
	 * 方法描述：根据departPath删除（批量删除，删除自己及所有子部门）
	 * 作        者：MaoSF
	 * 日        期：2016年7月14日-下午8:21:40
	 */
	@Override
	public int deleteByDepartPath(String departPath) {
		return this.sqlSession.update("DepartEntityMapper.deleteByDepartPath",departPath);
	}

	/**
	 * 方法描述：获取具有某些角色的部门（当前人在当前组织下的部门）
	 * 作者：MaoSF
	 * 日期：2016/8/16
	 */
	@Override
	public List<DepartRoleDTO> getDepartByRole(Map<String, Object> paraMap) {
		return this.sqlSession.selectList("GetDepartByRoleMapper.getDepartByRole",paraMap);
	}

	/**
	 * 方法描述：获取所有组织的角色（在当前公司下）
	 * 作者：MaoSF
	 * 日期：2016/8/16
	 */
	@Override
	public List<DepartRoleDTO> getOrgRole(Map<String, Object> paraMap) {
		return this.sqlSession.selectList("GetDepartByRoleMapper.getOrgRole",paraMap);
	}

	@Override
	public List<DepartEntity> selectDepartByParam(Map<String,Object> paraMap){
		return this.sqlSession.selectList("GetDepartByCompanyIdMapper.selectDepartByParam",paraMap);
	}

	@Override
	public List<DepartEntity> selectStairDepartCompanyId(Map<String,Object> paraMap){
		return this.sqlSession.selectList("GetDepartByCompanyIdMapper.selectStairDepartCompanyId",paraMap);
	}
	@Override
	public List<DepartEntity> selectNotCreateGroupDepart(Map<String,Object> paraMap){
		return this.sqlSession.selectList("GetDepartByCompanyIdMapper.selectNotCreateGroupDepart",paraMap);
	}
	@Override
	public List<DepartEntity> selectCreateGroupDepart(Map<String,Object> paraMap){
		return this.sqlSession.selectList("GetDepartByCompanyIdMapper.selectCreateGroupDepart",paraMap);
	}
}
