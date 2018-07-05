package com.maoding.companybill.dao.impl;

import com.maoding.companybill.dao.CompanyBillDao;
import com.maoding.companybill.dto.SaveCompanyBillDTO;
import com.maoding.companybill.entity.CompanyBillEntity;
import com.maoding.core.base.dao.GenericDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("companyBillDao")
public class CompanyBillDaoImpl extends GenericDao<CompanyBillEntity> implements CompanyBillDao {

    @Override
    public List<CompanyBillEntity> getCompanyBill(SaveCompanyBillDTO dto) {
        return this.sqlSession.selectList("CompanyBillEntityMapper.getCompanyBill",dto);
    }

    @Override
    public List<CompanyBillEntity> getCompanyBillIgnoreCompanyRelation(SaveCompanyBillDTO dto) {
        return this.sqlSession.selectList("CompanyBillEntityMapper.getCompanyBillIgnoreCompanyRelation",dto);
    }

    @Override
    public List<CompanyBillEntity> getCompanyBillByTargetId(SaveCompanyBillDTO dto) {
        return this.sqlSession.selectList("CompanyBillEntityMapper.getCompanyBillByTargetId",dto);
    }

    @Override
    public int deletedCompanyBill(String targetId) {
        return this.sqlSession.update("CompanyBillEntityMapper.deletedCompanyBill",targetId);
    }
}
