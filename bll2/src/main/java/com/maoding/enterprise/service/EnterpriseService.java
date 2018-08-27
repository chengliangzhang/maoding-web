package com.maoding.enterprise.service;

import com.maoding.core.bean.ResponseBean;
import com.maoding.enterprise.dto.EnterpriseDTO;

public interface EnterpriseService {

    ResponseBean getRemoteData(String url, Object param) throws Exception;

    String getEnterpriseName(String id);

    EnterpriseDTO getEnterpriseById(String id) ;

}
