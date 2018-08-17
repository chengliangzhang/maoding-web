package com.maoding.statistic.dto;

import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/15
 * 类名: com.maoding.statistic.dto.StatisticTitleFilterDTO
 * 作者: 张成亮
 * 描述:
 **/
public class StatisticTitleFilterDTO {
    /** 收支类型过滤条件 */
    private List<CostTypeDTO> feeTypeParentNameList;

    /** 收支类型子项过滤条件 */
    private List<CostTypeDTO> feeTypeNameList;

    /** 金额过滤条件 **/
    private List<CoreShowDTO> feeList;

    public List<CoreShowDTO> getFeeList() {
        return feeList;
    }

    public void setFeeList(List<CoreShowDTO> feeList) {
        this.feeList = feeList;
    }

    public List<CostTypeDTO> getFeeTypeParentNameList() {
        return feeTypeParentNameList;
    }

    public void setFeeTypeParentNameList(List<CostTypeDTO> feeTypeParentNameList) {
        this.feeTypeParentNameList = feeTypeParentNameList;
    }

    public List<CostTypeDTO> getFeeTypeNameList() {
        return feeTypeNameList;
    }

    public void setFeeTypeNameList(List<CostTypeDTO> feeTypeNameList) {
        this.feeTypeNameList = feeTypeNameList;
    }
}
