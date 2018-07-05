package com.maoding.org.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.dto.BusinessPartnerDTO;
import com.maoding.org.dto.CompanyRelationAuditDTO;
import com.maoding.org.entity.CompanyRelationAuditEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyRelationAuditService
 * 类描述：组织关系           Service
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:22:43
 */
public  interface CompanyRelationAuditService extends BaseService<CompanyRelationAuditEntity> {

	/**
	 * 方法描述：根据参数查询组织关系
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午10:27:38
	 *
	 * @param parma
	 */
	public  List<CompanyRelationAuditDTO> getCompanyRelationAuditByParam(Map<String, Object> parma) throws Exception;
	
	/**
	 * 方法描述：根据参数查询组织关系条数（用于分页）
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午10:27:38
	 * @param parma
	 */
	public  int getCompanyRelationAuditByParamCount(Map<String, Object> parma)throws Exception;

	/**
	 * 方法描述：邀请或申请加盟
	 * 作        者：MaoSF
	 * 日        期：2016年7月12日-上午1:12:52
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public AjaxMessage applicationOrInvitation(CompanyRelationAuditDTO dto) throws Exception;

	/**
	 * 方法描述：处理加盟信息
	 * 作        者：MaoSF
	 * 日        期：2016年7月11日-下午11:46:41
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public AjaxMessage processingApplicationOrInvitation(CompanyRelationAuditDTO dto) throws Exception;


	/**
	 * 方法描述：申请成为事业合伙人
	 * 作者：MaoSF
	 * 日期：2017/4/1
	 * @param:
	 * @return:
	 */
	public AjaxMessage applyBusinessPartner(BusinessPartnerDTO dto) throws Exception;

}
