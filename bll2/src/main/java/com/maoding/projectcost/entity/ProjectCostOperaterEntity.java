package com.maoding.projectcost.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.util.Date;

/**
 * 深圳市设计同道技术有限公司
 *项目费用操作详情表
 * 类    名：ProjectCostOperaterDTO
 * 类描述：费用操作表（记录发起人，回款条件，付款确认人，到款确认人，收款人，和操作日期）
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostOperaterEntity extends BaseEntity {

    /**
     * 费用详情id
     */
    private String costDetailId;

    /**
     * 操作类型（1.发起人，2.回款条件，3.(经营负责人)付款确认,4.（企业负责人）付款确认，5，到款）
     */
    private String type;

    /**
     * 操作人
     */
    private String companyUserId;

    /**
     *操作日期
     */
    private Date operaterDate;
    /**
     * 预留字段1
     */
    private String field1;

    /**
     * 预留字段2
     */
    private String field2;

    public String getCostDetailId() {
        return costDetailId;
    }

    public void setCostDetailId(String costDetailId) {
        this.costDetailId = costDetailId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public Date getOperaterDate() {
        return operaterDate;
    }

    public void setOperaterDate(Date operaterDate) {
        this.operaterDate = operaterDate;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
}
