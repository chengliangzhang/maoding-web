package com.maoding.org.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.CompanyRelationDao;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.org.entity.CompanyRelationEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyRelationDaoImpl
 * 类描述：类描述：团队(公司）DaoImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:44:06
 */
@Service("companyRelationDao")
public class CompanyRelationDaoImpl extends GenericDao< CompanyRelationEntity> implements CompanyRelationDao{

	@Override
	public List<CompanyRelationDTO> getCompanyRelationByParam(
			Map<String, Object> parma) {
		return this.sqlSession.selectList("QueryCompanyRelationEntityMapper.getCompanyRelationByParam", parma);
	}

	@Override
	public int getCompanyRelationByParamCount(Map<String, Object> parma) {
		return  this.sqlSession.selectOne("QueryCompanyRelationEntityMapper.getCompanyRelationByParamCount", parma);
	}

	/**
	 * 方法描述：删除组织关系，（解散团队的时候使用）
	 * 作者：MaoSF
	 * 日期：2016/8/5
	 *
	 * @param orgId
	 * @param:
	 * @return:
	 */
	@Override
	public int deleteCompanyRelationByOrgId(String orgId) {
		return  this.sqlSession.delete("CompanyRelationEntityMapper.deleteByOrgId", orgId);
	}

	/**
	 * 方法描述：根据orgId查询
	 * 作者：MaoSF
	 * 日期：2017/4/18
	 *
	 * @param orgId
	 * @param:
	 * @return:
	 */
	@Override
	public CompanyRelationDTO getCompanyRelationByOrgId(String orgId) {
		return  this.sqlSession.selectOne("QueryCompanyRelationEntityMapper.getCompanyRelationByOrgId", orgId);
	}


}
