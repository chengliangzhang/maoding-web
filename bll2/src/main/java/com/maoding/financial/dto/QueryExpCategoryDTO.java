package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class QueryExpCategoryDTO extends BaseDTO {

    private String companyId;

    private Integer categoryType;

    /**
     * 不为null时，包含项目费用类型的数据
     */
    private String isContainSystemType;

    private String showStatus;

    private String rootCompanyId;

    private List<String> companyIdList = new ArrayList<>();

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getIsContainSystemType() {
        return isContainSystemType;
    }

    public void setIsContainSystemType(String isContainSystemType) {
        this.isContainSystemType = isContainSystemType;
    }

    public String getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
    }

    public String getRootCompanyId() {
        return rootCompanyId;
    }

    public void setRootCompanyId(String rootCompanyId) {
        this.rootCompanyId = rootCompanyId;
    }

    public List<String> getCompanyIdList() {
        return companyIdList;
    }

    public void setCompanyIdList(List<String> companyIdList) {
        this.companyIdList = companyIdList;
    }
}
