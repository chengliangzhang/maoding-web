package com.maoding.org.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.entity.CompanyAttachEntity;
import com.maoding.org.entity.CompanyUserAuditEntity;
import com.maoding.user.dto.ShareInvateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyAttachEntity
 * 类描述：（公司附件）Service
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:22:43
 */
public interface CompanyUserAuditService extends BaseService<CompanyUserAuditEntity>{

	/**
	 * 方法描述：分享邀请注册（companyId,cellphone,userName）
	 * 作        者：MaoSF
	 * 日        期：2016年7月12日-上午2:39:17
	 * @param dto（companyId,cellphone,userName,code,userId）
	 * @return
	 * @throws Exception
	 */
	public AjaxMessage shareInvateRegister(ShareInvateDTO dto) throws Exception;

	/**
	 * 方法描述：审核邀请注册（companyId,cellphone,userName）
	 * 作        者：MaoSF
	 * 日        期：2016年7月12日-上午2:39:17
	 * @param dto（companyId,cellphone,userName,code,userId）
	 * @return
	 * @throws Exception
	 */
	public AjaxMessage auditShareInvate(ShareInvateDTO dto) throws Exception;

	/**
	 * 方法描述：待审核列表数据
	 * 作        者：MaoSF
	 * 日        期：2016年7月12日-上午2:39:17
	 * @param param(companyId,pageNumber,pageSize)
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getCompanyUserAudit(Map<String,Object> param) throws Exception;

	/**
	 * 方法描述：待审核列表数据
	 * 作        者：MaoSF
	 * 日        期：2016年7月12日-上午2:39:17
	 * @param param(companyId,pageNumber,pageSize)
	 * @return
	 * @throws Exception
	 */
	public int getCompanyUserNumAudit(Map<String,Object> param) throws Exception;

	//=========================================新接口2.0===========================================================

	/**
	 * 方法描述：待审核列表数据
	 * 作        者：MaoSF
	 * 日        期：2016年7月12日-上午2:39:17
	 *
	 * @param param(companyId,pageNumber,pageSize)
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getCompanyUserAuditOfPage(Map<String,Object> param) throws Exception;

}