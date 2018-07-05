package com.maoding.statistic.dto;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/5/8.
 */
public class StatisticSummaryQueryDTO {
    /**
     * 单位：1-万元，其他-元
     */
    Integer unitType;
    /**
     * 要查询的公司ID列表
     */
    List<String> companyIdList;

    public List<String> getCompanyIdList() {
        return companyIdList;
    }

    public void setCompanyIdList(List<String> companyIdList) {
        this.companyIdList = companyIdList;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }
}
