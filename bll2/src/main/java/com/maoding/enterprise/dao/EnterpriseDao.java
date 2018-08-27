package com.maoding.enterprise.dao;

import com.maoding.enterprise.dto.EnterpriseDTO;

public interface EnterpriseDao {

    String getEnterpriseNameById(String id);

    EnterpriseDTO getEnterpriseById(String id);
}
