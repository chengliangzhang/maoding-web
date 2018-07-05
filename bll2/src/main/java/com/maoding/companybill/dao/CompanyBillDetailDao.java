package com.maoding.companybill.dao;

import com.maoding.companybill.entity.CompanyBillDetailEntity;
import com.maoding.core.base.dao.BaseDao;

public interface CompanyBillDetailDao extends BaseDao<CompanyBillDetailEntity> {

    /**
     * 根据billId删除详情
     */
    void deleteCompanyBillDetailByBillId(String billId);

    String getDetailIdsByBillId(String billId);
}
