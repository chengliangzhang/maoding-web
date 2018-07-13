package com.maoding.companybill.service;

import com.maoding.companybill.dto.SaveCompanyBillDTO;
import com.maoding.core.base.service.NewBaseService;

public interface CompanyBillService {

    /**
     * 财务数据保存统一接口处理
     */
    int saveCompanyBill(SaveCompanyBillDTO dto) throws Exception;

    /**
     * 固定支出 财务数据 业务处理（单独给固定支出 使用）
     */
    int saveCompanyBillForFix(SaveCompanyBillDTO dto) throws Exception;

    /**
     * 删除财务数据(用于删除系统中的业务数据，关联的财务数据 也被删除)
     */
    void deleteCompanyBill(String targetId) throws Exception;


}
