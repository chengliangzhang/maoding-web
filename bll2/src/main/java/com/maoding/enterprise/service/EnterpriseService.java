package com.maoding.enterprise.service;

import com.maoding.core.bean.ResponseBean;

public interface EnterpriseService {

    ResponseBean getRemoteData(String url, Object param) throws Exception;

    String getEnterpriseName(String id);
}
