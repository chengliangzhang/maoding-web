package com.maoding.companybill.service;

import com.maoding.companybill.dto.CompanyBalanceDTO;
import com.maoding.companybill.dto.QueryCompanyBalanceDTO;
import com.maoding.companybill.dto.SaveCompanyBalanceDTO;
import com.maoding.companybill.entity.CompanyBalanceEntity;

import java.util.Date;
import java.util.List;

public interface CompanyBalanceService {

    int saveCompanyBalance(SaveCompanyBalanceDTO dto) throws Exception;

    String saveCompanyBalance(String companyId) throws Exception;

    List<CompanyBalanceDTO> getCompanyBalance(QueryCompanyBalanceDTO query) throws Exception;

    CompanyBalanceEntity getCompanyBalanceByCompanyId(String companyId) ;

    boolean isCanBeAllocate(String companyId, String amount, String paymentDate) throws Exception;
}
