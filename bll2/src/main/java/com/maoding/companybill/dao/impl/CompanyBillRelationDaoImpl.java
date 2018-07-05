package com.maoding.companybill.dao.impl;

import com.maoding.companybill.dao.CompanyBillDao;
import com.maoding.companybill.dao.CompanyBillRelationDao;
import com.maoding.companybill.entity.CompanyBillEntity;
import com.maoding.companybill.entity.CompanyBillRelationEntity;
import com.maoding.core.base.dao.GenericDao;
import org.springframework.stereotype.Service;

@Service("companyBillRelationDao")
public class CompanyBillRelationDaoImpl extends GenericDao<CompanyBillRelationEntity> implements CompanyBillRelationDao {

}
