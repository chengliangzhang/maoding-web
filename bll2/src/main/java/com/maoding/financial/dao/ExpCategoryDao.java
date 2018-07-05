package com.maoding.financial.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.financial.dto.ExpCategoryDataDTO;
import com.maoding.financial.dto.ExpCountDTO;
import com.maoding.financial.dto.QueryExpCategoryDTO;
import com.maoding.financial.dto.SaveExpCategoryShowStatusDTO;
import com.maoding.financial.entity.ExpCategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DataDictionaryDao
 * 类描述：报销类别DAO
 * 作    者：MaoSF
 * 日    期：2016年10月09日-下午2:24:31
 */
public interface ExpCategoryDao extends BaseDao<ExpCategoryEntity>{
	

	/**
	 * 方法描述：根据相关参数查找
	 * 作        者：MaoSF
	 * 日        期：2016年10月09日-下午2:46:28
	 */
	List<ExpCategoryEntity> getDataByParemeter(Map<String, Object> map);

	/**
	 * 方法描述：根据父id和那么查询
	 * 作者：MaoSF
	 * 日期：2016/10/13
	 */
	ExpCategoryEntity selectByName(String pid,String name);

	ExpCountDTO  getExpCategoryByCompanyId (String companyId);

	/**
	 * 方法描述：获取最大的seq值
	 * 作者：MaoSF
	 * 日期：2016/10/9
	 */
	int getMaxExpCategorySeq(String companyId);


	int deleteByPId(Map<String, Object> map);

	/**
	 * 查询组织的费用类型，如果 categoryType = null,则 categoryType<3 ,因为categoryType 为费用分摊
	 */
	List<ExpCategoryDataDTO> getExpCategoryList(QueryExpCategoryDTO query);

	List<ExpCategoryDataDTO> getExpTypeListForProfitReport(String companyId,String subCompanyId);
	List<ExpCategoryDataDTO> getExpTypeListForProfitReport(String companyId,List<String> subCompanyIdList);

	List<ExpCategoryDataDTO> getExpCategoryListByType(QueryExpCategoryDTO query);

	int saveExpCategoryShowStatus(SaveExpCategoryShowStatusDTO dto);

	String getCategoryTypeRelationCompanyId(String companyId);

}
