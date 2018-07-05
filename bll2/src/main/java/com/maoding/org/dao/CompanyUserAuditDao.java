package com.maoding.org.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.entity.CompanyUserAuditEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyUserAuditDao
 * 类描述：公司人员审核Dao
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:42:48
 */
public interface CompanyUserAuditDao extends BaseDao<CompanyUserAuditEntity>{

    /**
     * 方法描述：根据手机号和公司id查询审核表中的记录
     * 作        者：MaoSF
     * 日        期：2016年7月9日-下午2:28:06
     * @param cellphone
     *  @param companyId
     * @return
     * @throws Exception
     */
    public CompanyUserAuditEntity selectByCellphoneAndCompanyId(String cellphone,String companyId);

    /**
     * 方法描述：待审核列表数据
     * 作        者：MaoSF
     * 日        期：2016年7月12日-上午2:39:17
     * @param param(companyId,startPage,endPage)
     * @return
     * @throws Exception
     */
    public List<CompanyUserAuditEntity> getCompanyUserAuditByCompanyId(Map<String,Object> param) throws Exception;


    /**
     * 方法描述：待审核列表数据条目数
     * 作        者：MaoSF
     * 日        期：2016年7月12日-上午2:39:17
     * @param param(companyId,startPage,endPage)
     * @return
     * @throws Exception
     */
    public int getCompanyUserAuditByCompanyIdCount(Map<String,Object> param) throws Exception;

    /**
     * 方法描述：根据cellphone和companyId删除数据
     * 作        者：MaoSF
     * 日        期：2016年7月12日-上午2:39:17
     * @return
     * @throws Exception
     */
    public int deleteByCellphoneAndCompanyId(String cellphone,String companyId);
	
}
