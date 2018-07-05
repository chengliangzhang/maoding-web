package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostDTO
 * 类描述：项目费用总记录表（项目中某款项的总金额，包含支付方，和收款方）
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostDTO extends BaseDTO {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 付款方公司id
     */
    private String fromCompanyId;

    /**
     * 收款方公司id
     */
    private String toCompanyId;

    /**
     * 类型1:合同总金额，2：技术审查费,3合作设计费付款
     */
    private String type;

    /**
     * 标示：1：正式协议，2：补充协议
     */
    private String flag;

    /**
     * 金额
     */
    private BigDecimal fee;

    /**
     * 备注
     */
    private String remark;


    /**
     * 状态：0：生效，1：不生效（删除）
     */
    private String status;


    private String 	createBy;
    private String 	updateBy;
    private Date createDate;
    private Date 	updateDate;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
    }

    public String getToCompanyId() {
        return toCompanyId;
    }

    public void setToCompanyId(String toCompanyId) {
        this.toCompanyId = toCompanyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
