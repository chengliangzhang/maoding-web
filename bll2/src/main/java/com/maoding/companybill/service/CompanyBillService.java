package com.maoding.companybill.service;

import com.maoding.companybill.dto.SaveCompanyBillDTO;
import com.maoding.core.base.service.NewBaseService;

public interface CompanyBillService {

    int saveCompanyBill(SaveCompanyBillDTO dto) throws Exception;

    int saveCompanyBillForFix(SaveCompanyBillDTO dto) throws Exception;

    void deleteCompanyBill(String targetId) throws Exception;


}
