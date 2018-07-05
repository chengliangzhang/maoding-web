package com.maoding.companybill.dao;

import com.maoding.companybill.dto.SaveCompanyBillDTO;
import com.maoding.companybill.entity.CompanyBillEntity;
import com.maoding.core.base.dao.BaseDao;

import java.util.List;

public interface CompanyBillDao extends BaseDao<CompanyBillEntity> {

    List<CompanyBillEntity> getCompanyBill(SaveCompanyBillDTO dto);

    List<CompanyBillEntity> getCompanyBillIgnoreCompanyRelation(SaveCompanyBillDTO dto);

    List<CompanyBillEntity> getCompanyBillByTargetId(SaveCompanyBillDTO dto);

    int deletedCompanyBill(String targetId);
}
