package com.maoding.companybill.dao.impl;

import com.maoding.companybill.dao.CompanyBalanceDao;
import com.maoding.companybill.dao.CompanyBillDao;
import com.maoding.companybill.dto.SaveCompanyBillDTO;
import com.maoding.companybill.entity.CompanyBalanceEntity;
import com.maoding.companybill.entity.CompanyBillEntity;
import com.maoding.core.base.dao.GenericDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("companyBalanceDao")
public class CompanyBalanceDaoImpl extends GenericDao<CompanyBalanceEntity> implements CompanyBalanceDao {

    @Override
    public CompanyBalanceEntity getCompanyBalanceByCompanyId(String companyId) {
        return this.sqlSession.selectOne("CompanyBalanceEntityMapper.getCompanyBalanceByCompanyId",companyId);
    }

    @Override
    public int update(CompanyBalanceEntity balance) {
        return this.sqlSession.update("CompanyBalanceEntityMapper.update",balance);
    }
}
