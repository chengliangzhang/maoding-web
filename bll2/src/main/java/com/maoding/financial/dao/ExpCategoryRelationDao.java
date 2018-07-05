package com.maoding.financial.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.financial.dto.ExpCategoryDataDTO;
import com.maoding.financial.dto.ExpCountDTO;
import com.maoding.financial.dto.QueryExpCategoryDTO;
import com.maoding.financial.dto.SaveExpCategoryShowStatusDTO;
import com.maoding.financial.entity.ExpCategoryEntity;
import com.maoding.financial.entity.ExpCategoryRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DataDictionaryDao
 * 类描述：报销类别DAO
 * 作    者：MaoSF
 * 日    期：2016年10月09日-下午2:24:31
 */
public interface ExpCategoryRelationDao extends BaseDao<ExpCategoryRelationEntity>{

	int getSelectedCategory(String companyId);

	int insertCategoryFromRootCompany(QueryExpCategoryDTO dto);

	int insertBatch(SaveExpCategoryShowStatusDTO dto);

	int deleteCategoryByCompany(SaveExpCategoryShowStatusDTO dto);
}
