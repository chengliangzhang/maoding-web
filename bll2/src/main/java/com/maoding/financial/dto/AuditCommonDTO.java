package com.maoding.financial.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseIdObject;
import com.maoding.org.dto.CompanyUserDataDTO;

import java.util.Date;

public class AuditCommonDTO extends BaseIdObject {

    /**
     * 编号
     */
    private String expNo;

    /**
     * 0:没有任何操作，1:退回记录重新提交,2:新生成记录
     */
    private Integer expFlag;

    /**
     * 申请人id
     */
    private String companyUserId;

    /**
     * 申请人姓名
     */
    private String userName;

    /**
     * 申请时间
     */

    @JsonFormat(pattern="yyyy/MM/dd",timezone = "GMT+8")
    private Date expDate;

    /**
     * 类型id
     */
    private String type;

    /**
     * 类型名称
     */
    private String expTypeName;

    /**
     * 状态值
     */
    private String approveStatus;

    /**
     * 状态值对应的名称
     */
    private String approveStatusName;

    /**
     * 申请人对象
     */
    public CompanyUserDataDTO applyUser = new CompanyUserDataDTO();

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpTypeName() {
        return expTypeName;
    }

    public void setExpTypeName(String expTypeName) {
        this.expTypeName = expTypeName;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getApproveStatusName() {
        if ("0".equals(approveStatus)) {
            return "待审批";
        } else if ("1".equals(approveStatus)) {
            return "已审批";
        } else if ("2".equals(approveStatus)) {
            return "退回";
        } else if ("3".equals(approveStatus)) {
            return "撤回";
        } else if ("4".equals(approveStatus)) {
            return "删除";
        } else if ("5".equals(approveStatus)) {
            return "审批中";
        }else if ("6".equals(approveStatus)) {
            return "已拨款";
        }
        return null;
    }

    public void setApproveStatusName(String approveStatusName) {
        this.approveStatusName = approveStatusName;
    }

    public CompanyUserDataDTO getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(CompanyUserDataDTO applyUser) {
        this.applyUser = applyUser;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public Integer getExpFlag() {
        return expFlag;
    }

    public void setExpFlag(Integer expFlag) {
        this.expFlag = expFlag;
    }
}
