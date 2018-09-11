package com.maoding.companybill.dao.impl;

import com.maoding.companybill.dao.CompanyBalanceChangeDetailDao;
import com.maoding.companybill.entity.CompanyBalanceChangeDetailEntity;
import com.maoding.core.base.dao.GenericDao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("companyBalanceChangeDetailDao")
public class CompanyBalanceChangeDetailDaoImpl extends GenericDao<CompanyBalanceChangeDetailEntity> implements CompanyBalanceChangeDetailDao {


    @Override
    public List<CompanyBalanceChangeDetailEntity> listCompanyBalanceChangeDetail(String balanceId) {
        return this.sqlSession.selectList("CompanyBalanceChangeDetailEntityMapper.listCompanyBalanceChangeDetail",balanceId);
    }

    @Override
    public BigDecimal getCompanyBalanceChangeDetailSum(String balanceId) {
        return this.sqlSession.selectOne("CompanyBalanceChangeDetailEntityMapper.getCompanyBalanceChangeDetailSum",balanceId);
    }
}
