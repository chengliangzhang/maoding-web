package com.maoding.dynamic.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/6/16.
 */
public class ZCostDTO {
    /**
     * 查询编号
     */
    String id;
    /**
     * 查询编号类型
     */
    Integer idType;
    /**
     * 费用节点名称
     */
    String pointName;
    /**
     * 费用节点类型
     */
    Integer pointType;
    /**
     * 费用节点类型名称
     */
    String pointTypeName;
    /**
     * 费用节点金额
     */
    BigDecimal pointCost;
    /**
     * 费用节点比率
     */
    BigDecimal pointRate;
    /**
     * 发起回款费用金额
     */
    BigDecimal detailCost;
    /**
     * 确认付款人员
     */
    String payUserName;
    /**
     * 确认收款人员
     */
    String paidUserName;
    /**
     * 确认到账费用金额
     */
    BigDecimal paidCost;
    /**
     * 确认付款日期
     */
    Date payDate;
    /**
     * 确认到款日期
     */
    Date paidDate;

    /**
     * 整体名称
     */
    String fullName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public Integer getPointType() {
        return pointType;
    }

    public void setPointType(Integer pointType) {
        this.pointType = pointType;
    }

    public String getPointTypeName() {
        return pointTypeName;
    }

    public void setPointTypeName(String pointTypeName) {
        this.pointTypeName = pointTypeName;
    }

    public BigDecimal getPointCost() {
        return pointCost;
    }

    public void setPointCost(BigDecimal pointCost) {
        this.pointCost = pointCost;
    }

    public BigDecimal getPointRate() {
        return pointRate;
    }

    public void setPointRate(BigDecimal pointRate) {
        this.pointRate = pointRate;
    }

    public BigDecimal getDetailCost() {
        return detailCost;
    }

    public void setDetailCost(BigDecimal detailCost) {
        this.detailCost = detailCost;
    }

    public BigDecimal getPaidCost() {
        return paidCost;
    }

    public void setPaidCost(BigDecimal paidCost) {
        this.paidCost = paidCost;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public String getPayUserName() {
        return payUserName;
    }

    public void setPayUserName(String payUserName) {
        this.payUserName = payUserName;
    }

    public String getPaidUserName() {
        return paidUserName;
    }

    public void setPaidUserName(String paidUserName) {
        this.paidUserName = paidUserName;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
