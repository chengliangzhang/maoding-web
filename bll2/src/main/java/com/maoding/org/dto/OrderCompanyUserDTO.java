package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：CompanyDTO
 * 类描述：企业信息DTO
 * 作    者：MaoSF
 * 日    期：2016年7月8日-上午11:47:57
 */
public class OrderCompanyUserDTO extends BaseDTO{

	private String orgId;
    private CompanyUserTableDTO companyUser1;
    private CompanyUserTableDTO companyUser2;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public CompanyUserTableDTO getCompanyUser1() {
        return companyUser1;
    }

    public void setCompanyUser1(CompanyUserTableDTO companyUser1) {
        this.companyUser1 = companyUser1;
    }

    public CompanyUserTableDTO getCompanyUser2() {
        return companyUser2;
    }

    public void setCompanyUser2(CompanyUserTableDTO companyUser2) {
        this.companyUser2 = companyUser2;
    }
}
