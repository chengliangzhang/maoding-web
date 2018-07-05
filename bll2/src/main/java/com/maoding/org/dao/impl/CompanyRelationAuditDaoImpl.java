package com.maoding.org.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.CompanyRelationAuditDao;
import com.maoding.org.dto.CompanyRelationAuditDTO;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.org.entity.CompanyRelationAuditEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyRelationAuditDaoImpl
 * 类描述：类描述：待审核的组织关系DaoImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:44:06
 */
@Service("companyRelationAuditDao")
public class CompanyRelationAuditDaoImpl extends GenericDao<CompanyRelationAuditEntity> implements CompanyRelationAuditDao {

	@Override
	public List<CompanyRelationAuditDTO> getCompanyRelationAuditByParam(
			Map<String, Object> parma) {
		return this.sqlSession.selectList("QueryCompanyRelationAuditEntityMapper.getCompanyRelationAuditByParam", parma);
	}

	@Override
	public int getCompanyRelationAuditByParamCount(Map<String, Object> parma) {
		return  this.sqlSession.selectOne("QueryCompanyRelationAuditEntityMapper.getCompanyRelationAuditByParamCount", parma);
	}

	/**
	 * 方法描述：删除组织关系，（解散团队的时候使用）
	 * 作者：MaoSF
	 * 日期：2016/8/5
	 */
	@Override
	public int deleteCompanyRelationByOrgId(String orgId) {
		return  this.sqlSession.delete("CompanyRelationAuditEntityMapper.deleteByOrgId", orgId);
	}

}
