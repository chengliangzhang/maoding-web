package com.maoding.projectcost.dto;

import com.maoding.attach.dto.FileEditDTO;
import com.maoding.core.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostDTO
 * 类描述：项目费用总记录表（项目中某款项的总金额，包含支付方，和收款方）
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostEditDTO extends BaseDTO {

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
     * 1:收款计划，2：付款计划
     */
    private Integer payType;

    /**
     * 备注
     */
    private String remark;


    /**
     * 状态：0：生效，1：不生效（删除）
     */
    private String status;

    /**
     * 是否是内部组织
     */
    private boolean isInnerCompany;


    /**
     * 上传附件的list：map（id：附件id,isUploadFile=1:新上传的）
     */
    private List<FileEditDTO> contactAttachList = new ArrayList<>();


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

    public boolean isInnerCompany() {
        return isInnerCompany;
    }

    public void setInnerCompany(boolean innerCompany) {
        isInnerCompany = innerCompany;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<FileEditDTO> getContactAttachList() {
        return contactAttachList;
    }

    public void setContactAttachList(List<FileEditDTO> contactAttachList) {
        this.contactAttachList = contactAttachList;
    }
}
