package com.maoding.projectcost.dto;

import java.math.BigDecimal;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostPointDTO
 * 类描述：费用节点表（记录费用在哪个节点上产生的，费用的描述，金额）
 * 项目费用收款节点表
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostPointInfoDTO extends ProjectCooperatorCostDTO {

    private String id;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 回款节点描述
     */
    private String feeDescription;

    /**
     * 回款比例
     */
    private String feeProportion;
    /**
     * 回款金额
     */
    private BigDecimal fee;

    /**
     * 发起收款，发起付款的条目数
     */
    private int pointDetailCount;

    /**
     * 发起收款，付款申请  =1
     */
    private String isEditFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
    }

    public String getFeeProportion() {
        return feeProportion;
    }

    public void setFeeProportion(String feeProportion) {
        this.feeProportion = feeProportion;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public int getPointDetailCount() {
        return pointDetailCount;
    }

    public void setPointDetailCount(int pointDetailCount) {
        this.pointDetailCount = pointDetailCount;
    }

    public String getIsEditFlag() {
        return isEditFlag;
    }

    public void setIsEditFlag(String isEditFlag) {
        this.isEditFlag = isEditFlag;
    }
}
