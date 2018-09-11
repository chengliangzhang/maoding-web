package com.maoding.companybill.service;

import com.maoding.companybill.dto.CompanyBalanceChangeDetailDTO;
import com.maoding.companybill.dto.QueryCompanyBalanceDTO;
import com.maoding.companybill.dto.SaveCompanyBalanceChangeDetailDTO;

import java.util.List;

public interface CompanyBalanceChangeDetailService {
    int SaveCompanyBalanceChangeDetail(SaveCompanyBalanceChangeDetailDTO dto) throws Exception;

    List<CompanyBalanceChangeDetailDTO> listCompanyBalanceChangeDetail(QueryCompanyBalanceDTO dto) throws Exception;
}
