package com.maoding.org.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.org.dao.CompanyUserAuditDao;
import com.maoding.org.entity.CompanyUserAuditEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：SubCompanyDaoImpl
 * 类描述：分支机构DaoImpl
 * 作    者：MaoSF
 * 日    期：2016年7月7日-下午3:44:06
 */
@Service("companyUserAuditDao")
public class CompanyUserAuditDaoImpl extends GenericDao<CompanyUserAuditEntity> implements CompanyUserAuditDao {


    /**
     * 方法描述：根据手机号和公司id查询审核表中的记录
     * 作        者：MaoSF
     * 日        期：2016年7月9日-下午2:28:06
     *
     * @param cellphone
     * @param companyId
     * @return
     * @throws Exception
     */
    @Override
    public CompanyUserAuditEntity selectByCellphoneAndCompanyId(String cellphone, String companyId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("cellphone",cellphone);
        map.put("companyId",companyId);
        return this.sqlSession.selectOne("CompanyUserAuditEntityMapper.selectByCellphoneAndCompanyId",map);
    }

    /**
     * 方法描述：待审核列表数据
     * 作        者：MaoSF
     * 日        期：2016年7月12日-上午2:39:17
     *
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public List<CompanyUserAuditEntity> getCompanyUserAuditByCompanyId(Map<String,Object> param) throws Exception {
        return this.sqlSession.selectList("CompanyUserAuditEntityMapper.getCompanyUserAuditByCompanyId",param);
    }

    /**
     * 方法描述：待审核列表数据条目数
     * 作        者：MaoSF
     * 日        期：2016年7月12日-上午2:39:17
     *
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public int getCompanyUserAuditByCompanyIdCount(Map<String,Object> param) throws Exception {
        return this.sqlSession.selectOne("CompanyUserAuditEntityMapper.getCompanyUserAuditByCompanyIdCount",param);
    }

    /**
     * 方法描述：根据cellphone和companyId删除数据
     * 作        者：MaoSF
     * 日        期：2016年7月12日-上午2:39:17
     *
     * @param cellphone
     * @param companyId
     * @return
     * @throws Exception
     */
    @Override
    public int deleteByCellphoneAndCompanyId(String cellphone, String companyId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("cellphone",cellphone);
        map.put("companyId",companyId);
        return this.sqlSession.delete("CompanyUserAuditEntityMapper.deleteByCellphoneAndCompanyId",map);
    }
}
