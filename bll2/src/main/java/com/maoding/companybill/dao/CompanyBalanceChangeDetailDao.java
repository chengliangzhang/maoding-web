package com.maoding.companybill.dao;

import com.maoding.companybill.entity.CompanyBalanceChangeDetailEntity;
import com.maoding.companybill.entity.CompanyBalanceEntity;
import com.maoding.core.base.dao.BaseDao;

import java.math.BigDecimal;
import java.util.List;

public interface CompanyBalanceChangeDetailDao extends BaseDao<CompanyBalanceChangeDetailEntity> {


    /**
     * 详情明细
     */
    List<CompanyBalanceChangeDetailEntity> listCompanyBalanceChangeDetail(String balanceId);

    BigDecimal getCompanyBalanceChangeDetailSum(String balanceId);
}
