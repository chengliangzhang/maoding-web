package com.maoding.companybill.dao.impl;

import com.maoding.companybill.dao.CompanyBillDao;
import com.maoding.companybill.dao.CompanyBillDetailDao;
import com.maoding.companybill.entity.CompanyBillDetailEntity;
import com.maoding.companybill.entity.CompanyBillEntity;
import com.maoding.core.base.dao.GenericDao;
import org.springframework.stereotype.Service;

@Service("companyBillDetailDao")
public class CompanyBillDetailDaoImpl extends GenericDao<CompanyBillDetailEntity> implements CompanyBillDetailDao {

    @Override
    public void deleteCompanyBillDetailByBillId(String billId) {
        this.sqlSession.delete("CompanyBillDetailEntityMapper.deleteCompanyBillDetailByBillId",billId);
    }

    @Override
    public String getDetailIdsByBillId(String billId) {
        return this.sqlSession.selectOne("CompanyBillDetailEntityMapper.getDetailIdsByBillId",billId);
    }
}
