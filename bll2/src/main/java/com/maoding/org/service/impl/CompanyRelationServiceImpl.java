package com.maoding.org.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dao.CompanyRelationDao;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyRelationEntity;
import com.maoding.org.service.CompanyRelationService;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：DepartServiceImpl
 * 类描述：团队（公司）ServiceImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午4:24:38
 */
@Service("CompanyRelationService")
public class CompanyRelationServiceImpl extends GenericService<CompanyRelationEntity>  implements CompanyRelationService{

	@Autowired
	private CompanyRelationDao companyRelationDao;
	
	@Autowired
	private CompanyDao companyDao;
	

	@Override
	public List<CompanyRelationDTO> getCompanyRelationByParam(
			Map<String, Object> parma) {
		return companyRelationDao.getCompanyRelationByParam(parma);
	}

	@Override
	public int getCompanyRelationByParamCount(Map<String, Object> parma) {
		return companyRelationDao.getCompanyRelationByParamCount(parma);
	}

}
