package com.maoding.org.dao.impl;


import com.beust.jcommander.internal.Maps;
import com.maoding.commonModule.dto.QueryCopyRecordDTO;
import com.maoding.core.base.dao.GenericDao;
import com.maoding.core.util.ChineseToEnglishUtil;
import com.maoding.core.util.StringUtil;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.dto.*;
import com.maoding.org.entity.CompanyUserEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyDaoImpl
 * 类描述：类描述：团队(公司）DaoImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:44:06
 */
@Service("companyUserDao")
public class CompanyUserDaoImpl extends GenericDao<CompanyUserEntity> implements CompanyUserDao{

	@Override
	public int insert(CompanyUserEntity entity){
		entity.setPinYin(ChineseToEnglishUtil.getPingYin(entity.getUserName()));
		int seq = this.sqlSession.selectOne("CompanyUserEntityMapper.getMaxCompanyUserSeq");
		entity.setSeq(seq);
		return  super.insert(entity);
	}

	@Override
	public int updateById(CompanyUserEntity entity){
		entity.setPinYin(ChineseToEnglishUtil.getPingYin(entity.getUserName()));
		return  super.updateById(entity);
	}

	@Override
	public CompanyUserEntity getCompanyUserByUserIdAndCompanyId(String userId, String companyId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("companyId", companyId);
		return this.sqlSession.selectOne("CompanyUserEntityMapper.getCompanyUserByUserIdAndCompanyId", map);
	}

	/**
	 * 方法描述：查找当前公司所有人员
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param companyId
	 * @return
	 */
	@Override
	public List<CompanyUserEntity> getCompanyUserByCompanyId(String companyId){
		return this.sqlSession.selectList("CompanyUserEntityMapper.getCompanyUserByCompanyId", companyId);
	}


	/**
	 * 方法描述： 根据参数查找当前公司所有人员
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 * @param param
	 * @return
	 */
	@Override
	public List<CompanyUserEntity> getCompanyUserByParam(Map <String,Object> param){
		return this.sqlSession.selectList("CompanyUserEntityMapper.getCompanyUserByParam", param);
	}
	/**
	 * 方法描述：根据id查询数据
	 * 作        者：MaoSF
	 * 日        期：2016年7月8日-下午4:24:08
	 *
	 * @param id
	 * @return
	 */
	@Override
	public CompanyUserTableDTO getCompanyUserById(String id) throws Exception {
		return this.sqlSession.selectOne("GetCompanyUserByIdMapper.getCompanyUserById", id);
	}

	@Override
	public CompanyUserLiteDTO getCompanyUserLiteDTO(String accountId, String companyId) {
		Map<String,Object> param= Maps.newHashMap();
		param.put("accountId",accountId);
		param.put("companyId",companyId);
		return this.sqlSession.selectOne("CompanyUserEntityMapper.getCompanyUserLiteDTO",param);
	}

	@Override
	public int getCompanyUserByOrgIdCount(Map<String, Object> param)
			throws Exception {
		return this.sqlSession.selectOne("GetCompanyUserByOrgIdMapper.getCompanyUserByOrgIdCount-bak", param);
	}

	/**
	 * 方法描述：组织人员查询（分页查询）(admin项目）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param@return
	 * @throws Exception
	 */
	@Override
	public List<CompanyUserTableDTO> getCompanyUserByOrgIdOfAdmin(Map<String, Object> param) throws Exception {
		return this.sqlSession.selectList("GetCompanyUserByOrgIdMapper.getCompanyUserByOrgIdOfAdmin", param);
	}

	/**
	 * 方法描述：组织人员查询（分页查询）（work项目）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param@return
	 * @throws Exception
	 */
	@Override
	public List<CompanyUserTableDTO> getCompanyUserByOrgIdOfWork(Map<String, Object> param) throws Exception {
		return this.sqlSession.selectList("GetCompanyUserByOrgIdMapper.getCompanyUserByOrgIdOfWork", param);
	}

	/**
	 * 方法描述：组织人员查询
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 * @param param(orgId)【orgId组织Id，companyId 公司Id】
	 */
	@Override
	public List<CompanyUserTableDTO> getUserByOrgId (Map<String,Object> param) throws Exception{
		return this.sqlSession.selectList("GetCompanyUserByOrgIdMapper.getUserByOrgId", param);
	}

	/**
	 * 方法描述：组织人员条数（分页查询）（work项目）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getCompanyUserByOrgIdCountOfWork(Map<String, Object> param) throws Exception {
		return this.sqlSession.selectOne("GetCompanyUserByOrgIdMapper.getCompanyUserByOrgIdCount", param);
	}

	/**
	 * 方法描述：组织人员条数（分页查询）（admin项目）
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 *
	 * @param param (orgId,keyword)【orgId必传】
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getCompanyUserByOrgIdCountOfAdmin(Map<String, Object> param) throws Exception {
		return this.sqlSession.selectOne("GetCompanyUserByOrgIdMapper.getCompanyUserByOrgIdCount", param);
	}

	/**
	 * 方法描述：组织人员部门及权限
	 * 作        者：MaoSF
	 * 日        期：2016年7月9日-下午2:28:06
	 */
	@Override
	public List<UserDepartDTO> getCompanyUserDepartRole(String userId, String companyId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId",userId);
		param.put("companyId",companyId);
		return this.sqlSession.selectList("GetCompanyUserByOrgIdMapper.getCompanyUserDepartRole", param);
	}

	@Override
	public List<CompanyUserTableDTO> getCompanyUserOfNotActive(Map<String, Object> param) throws Exception {
		return this.sqlSession.selectList("GetCompanyUserByOrgIdMapper.getCompanyUserOfNotActive", param);
	}

	@Override
	public int getCompanyUserOfNotActiveCount(Map<String, Object> param) throws Exception {
		return this.sqlSession.selectOne("GetCompanyUserByOrgIdMapper.getCompanyUserOfNotActiveCount", param);
	}

	/**
	 * 方法描述：根据id集合查询人员名字（任务分解--负责人，设计人）使用
	 * 作        者：MaoSF
	 * 日        期：2016年4月23日-下午6:02:15
	 * @param param
	 * @return
	 */
	@Override
	public List<CompanyUserEntity> getUserByDepartId(Map<String, Object> param){
		return this.sqlSession.selectList("CompanyUserEntityMapper.getUserByDepartId",param);
	}

	@Override
	public List<CompanyUserEntity> getCompanyUserId(String userId){
		return  this.sqlSession.selectList("CompanyUserEntityMapper.getCompanyByUserId",userId);
	}

	/**
	 * 方法描述：根据角色id查询公司人员
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 */
	@Override
	public List<CompanyUserTableDTO> getCompanyUserByRoleId(Map<String, Object> param) {
		return  this.sqlSession.selectList("GetCompanyUserByRoleIdMapper.getCompanyUserByRoleId",param);
	}

	/**
	 * 方法描述：根据角色id查询公司人员
	 * 作者：MaoSF
	 * 日期：2016/11/3
	 */
	@Override
	public List<CompanyUserTableDTO> getCompanyUserByPermissionId(Map<String, Object> param) {
		return  this.sqlSession.selectList("GetCompanyUserByRoleIdMapper.getCompanyUserByPermissionId",param);
	}

	/**
	 * 方法描述：根据orgList查询人员的账号id
	 * 作者：MaoSF
	 * 日期：2016/12/8
	 */
	@Override
	public List<String> getUserByOrgIdForNotice(Map<String, Object> param) {
		return  this.sqlSession.selectList("GetUserByOrgIdForNoticeMapper.getUserByOrgIdForNotice",param);
	}



	/**
	 * 方法描述：根据关键字查询公司成员
	 * 作者：MaoSF
	 * 日期：2017/3/2
	 * @param param(companyId,keyword)
	 */
	@Override
	public List<Map<String, String>> getUserByKeyWord(Map<String, Object> param) {
		return  this.sqlSession.selectList("CompanyUserEntityMapper.getUserByKeyWord",param);
	}

	/**
	 * 方法描述：获取所有人员（用于数据迁移处理）
	 * 作者：MaoSF
	 * 日期：2017/4/13
	 */
	@Override
	public List<CompanyUserEntity> getAllCompanyUser() {
		return  this.sqlSession.selectList("CompanyUserEntityMapper.getAllCompanyUser");
	}

	/**
	 * 获取字段值
	 */
	@Override
	public String getUserId(CompanyUserEntity companyUser) {
		return (companyUser!=null)?companyUser.getUserId():null;
	}

	@Override
	public String getCompanyId(CompanyUserEntity companyUser) {
		return (companyUser!=null)?companyUser.getCompanyId():null;
	}

	@Override
	public String getCompanyUserId(CompanyUserEntity companyUser) {
		return (companyUser!=null)?companyUser.getId():null;
	}

	@Override
	public String getUserName(CompanyUserEntity companyUser) {
		if (companyUser == null) {
			return "";
		}
		return (StringUtil.isNullOrEmpty(companyUser.getUserName())) ? "" : companyUser.getUserName();
	}

	@Override
	public String getUserNameByCompanyIdAndUserId(String companyId, String userId) {
		return getUserName(getCompanyUserByUserIdAndCompanyId(userId,companyId));
	}

	@Override
	public String getUserName(String companyUserId){
		CompanyUserEntity user = this.selectById(companyUserId);
		if(user!=null){
			return user.getUserName();
		}
		return null;
	}
	@Override
	public CompanyUserAppDTO getCompanyUserDataById(String companyUserId) {
		return this.sqlSession.selectOne("GetCompanyUserByOrgIdMapper.getCompanyUserDataById", companyUserId);
	}


	@Override
	public List<CompanyUserDataDTO> getCopyUser(QueryCopyRecordDTO query) {
		return this.sqlSession.selectList("GetCompanyUserByOrgIdMapper.getCopyUser", query);
	}
}
