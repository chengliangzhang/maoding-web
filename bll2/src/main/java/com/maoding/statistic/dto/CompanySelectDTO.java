package com.maoding.statistic.dto;

import org.apache.ibatis.type.Alias;


/**
 * Created by Chengliang.zhang on 2017/5/9.
 */
@Alias("CompanySelectDTO")
public class CompanySelectDTO {
    private String companyId;
    private String companyName;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
