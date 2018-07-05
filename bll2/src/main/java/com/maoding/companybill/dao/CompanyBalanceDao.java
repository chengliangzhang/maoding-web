package com.maoding.companybill.dao;

import com.maoding.companybill.entity.CompanyBalanceEntity;
import com.maoding.core.base.dao.BaseDao;

public interface CompanyBalanceDao extends BaseDao<CompanyBalanceEntity> {

    CompanyBalanceEntity getCompanyBalanceByCompanyId(String companyId);

    int update(CompanyBalanceEntity balance);
}
