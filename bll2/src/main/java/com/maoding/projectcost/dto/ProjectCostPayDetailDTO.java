package com.maoding.projectcost.dto;

import java.math.BigDecimal;

/**
 * 深圳市设计同道技术有限公司
 * 付款费用详情
 * 类    名：ProjectCostPayDetailDTO
 * 作    者：FYT
 * 日    期：2018/9/6
 */
public class ProjectCostPayDetailDTO {

    //关联项目
    private String projectName;
    //收款单位
    private String fromCompanyId;
    //收款单位名称
    private String toCompanyName;

    //节点描述
    private String feeDescription;
    //比例
    private String  feeProportion;
    //发起人
    private String  userName;
    //发起金额
    private BigDecimal fee;



    //描述
    private String pointDetailDescription;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPointDetailDescription() {
        return pointDetailDescription;
    }

    public void setPointDetailDescription(String pointDetailDescription) {
        this.pointDetailDescription = pointDetailDescription;
    }

    public String getToCompanyName() {
        return toCompanyName;
    }

    public void setToCompanyName(String toCompanyName) {
        this.toCompanyName = toCompanyName;
    }
    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
