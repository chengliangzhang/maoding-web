package com.maoding.org.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.dto.CompanyRelationAuditDTO;
import com.maoding.org.entity.CompanyRelationAuditEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：TeamOperaterDao
 * 类描述：团队管理Dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:42:48
 */
public interface CompanyRelationAuditDao extends BaseDao<CompanyRelationAuditEntity>{

	/**
	 * 方法描述：根据参数查询待审核的组织关系
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午10:27:38
	 * @param parma
	 */
	public List<CompanyRelationAuditDTO> getCompanyRelationAuditByParam(Map<String, Object> parma);
	
	/**
	 * 方法描述：根据参数查询组织关系条数（用于分页）
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午10:27:38
	 * @param parma
	 */
	public int getCompanyRelationAuditByParamCount(Map<String, Object> parma);

	/**
	 * 方法描述：删除组织关系，（解散团队的时候使用）
	 * 作者：MaoSF
	 * 日期：2016/8/5
	 * @param:
	 * @return:
	 */
	public int deleteCompanyRelationByOrgId(String orgId);
}
