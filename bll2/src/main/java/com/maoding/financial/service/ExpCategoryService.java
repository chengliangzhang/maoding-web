package com.maoding.financial.service;


import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.financial.dto.*;
import com.maoding.financial.entity.ExpCategoryEntity;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DataDictionaryDao
 * 类描述：报销类别DAO
 * 作    者：MaoSF
 * 日    期：2016年10月09日-下午2:24:31
 */
public interface ExpCategoryService extends BaseService<ExpCategoryEntity> {

    /**
     * 方法描述：报销基础数据
     * 作   者：LY
     * 日   期：2016/7/27 17:59
     */
    AjaxMessage getExpBaseData(String companyId, String userId) throws Exception ;

    /**
     * 方法描述：报销类别基础数据
     * 作   者：LY
     * 日   期：2016/7/27 17:59
     */
    AjaxMessage getCategoryBaseData(String companyId, String userId) throws Exception ;

    /**
     * 方法描述:saveOrUpdateCategoryBaseData
     * 作   者：LY
     * 日   期：2016/7/27 17:39
     */
    AjaxMessage saveOrUpdateCategoryBaseData(ExpTypeOutDTO dto, String companyId)throws Exception;

    /**
     * 保存固定支出类型
     */
    int saveExpFixCategory(ExpCategoryDTO dto) throws Exception;
    /**
     * 方法描述:deleteCategoryBaseData
     * 作   者：LY
     * 日   期：2016/7/27 17:39
     *
     */
    AjaxMessage deleteCategoryBaseData(String id)throws Exception;

    /**
     * 方法描述：查询报销类型
     * 用于我要报销界面，报销类型选择
     * 作        者：MaoSF
     * 日        期：2015年12月7日-上午11:21:49
     */
    List<ExpTypeDTO> getExpTypeList(String companyId);

    /**
     * 方法描述：查询组织的所有费用类型
     * 用于我要报销界面，报销类型选择
     * 作        者：MaoSF
     * 日        期：2015年12月7日-上午11:21:49
     */
    List<ExpCategoryDataDTO> getExpTypeList(QueryExpCategoryDTO query) throws Exception;

    /**
     * 费用分摊
     */
    List<ExpCategoryDataDTO> getExpShareTypeList(QueryExpCategoryDTO query) throws Exception;

    /**
     * 方法描述：查询所有收支项（展示的项）
     * 用于我要报销界面，报销类型选择
     * 作        者：MaoSF
     * 日        期：2018年5月31日-上午11:21:49
     */
    List<ExpCategoryDataDTO> getExpTypeListForProfitReport(QueryExpCategoryDTO query) throws Exception;

    List<ExpCategoryDataDTO> getExpCategoryListByType(String companyId,Integer categoryType);
    List<ExpCategoryDataDTO> getExpCategoryListByType(List<String> companyIdList,Integer categoryType);

    int saveExpTypeShowStatus(SaveExpCategoryShowStatusDTO dto) throws Exception;

    int saveExpShareTypeShowStatus(SaveExpCategoryShowStatusDTO dto) throws Exception;

    void setDefaultExp(String companyId) throws Exception;

    void insertCategoryFromRootCompany(QueryExpCategoryDTO query);
}
