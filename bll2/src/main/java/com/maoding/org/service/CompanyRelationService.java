package com.maoding.org.service;

import java.util.List;
import java.util.Map;

import com.maoding.core.base.service.BaseService;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.org.entity.CompanyRelationEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyRelationService
 * 类描述：组织关系           Service
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:22:43
 */
public interface CompanyRelationService extends BaseService<CompanyRelationEntity>{

	/**
	 * 方法描述：根据参数查询组织关系
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午10:27:38
	 * @param parma
	 */
	public List<CompanyRelationDTO> getCompanyRelationByParam(Map<String,Object> parma)throws Exception;
	
	/**
	 * 方法描述：根据参数查询组织关系条数（用于分页）
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午10:27:38
	 * @param parma
	 */
	public int getCompanyRelationByParamCount(Map<String,Object> parma)throws Exception;
	
}
