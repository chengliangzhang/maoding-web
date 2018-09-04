package com.maoding.financial.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpMainEntity
 * 描    述 : 报销主表DTO
 * 作    者 : LY
 * 日    期 : 2016/7/26 15:15
 */
public class ExpMainDTO extends BaseDTO {

    /**
     * 前端生成的报销单Id
     */
    private String targetId;

    /**
     * 支出类别父名称
     */
    private String expPName;

    /**
     * 支出类别子名称
     */
    private String expName;

    /**
     * 支出类别父子名称
     */
    private String expAllName;

    /**
     *  用户id
     */
    private String companyUserId;

    /**
     * 报销人
     */
    private String userName;

    /**
     * 报销日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date expDate;


    /**
     * 审批状态(0:待审核，1:同意，2，退回,3:撤回,4:删除,5.审批中）
     */
    private String approveStatus;

    /**
     * 审批状态名称(0:待审核，1:同意，2，退回,3:撤回,4:删除,5.审批中）
     */
    private String approveStatusName;

    /**
     * 企业id
     */
    private String companyId;

    /** 企业名称 */
    private String companyName;

    /**
     * 部门id
     */
    private String departId;

    /**
     * 部门name
     */
    private String departName;

    /**
     * 审核日期
     */
    private String approveDate;

    /**
     * 详细条目数
     */
    private int detailCount;

    /**
     * 款项金额
     */
    private BigDecimal expSumAmount;

    /**
     * 用途说明
     */
    private String expUse;

    /**
     * 备注
     */
    private String remark;

    /**
     * 拨款日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date allocationDate;

    /**
     * 报销明细
     */
    private List<ExpDetailDTO> detailList = new ArrayList<ExpDetailDTO>();

    /**
     * 审核人id
     */
    private String auditPerson;

    /**
     * 审核人姓名
     */
    private String auditPersonName;

    /**
     * 操作人
     */
    private String updateBy;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 报销类别
     */
    private String expType;

    /**
     * 报销父类别
     */
    private String expParentType;

    /**
     * 报销父类别名称
     */
    private String expTypeParentName;

    /**
     * 报销类别名称
     */
    private String expTypeName;

    /**
     * 是否最新审核 Y是 N否
     */
    private String isNew;

    private String projectName;

    /**
     * 报销类别：1=报销申请，2=费用申请
     */
    private Integer type;

    /**
     * 拨款人
     */
    private String allocationUserName;

    /**
     * 拨款人所在组织id
     */
    private String allocationUserCompanyId;
    /**
     * 报销附件列表
     */
    private List<ExpAttachDTO> expAttachDTOList=new ArrayList<ExpAttachDTO>();

    /**
     * 版本控制字段
     */
    private Integer versionNum;

    /**
     * 报销单号
     */
    private String expNo;

    /**
     * 0:没有任何操作，1:退回记录重新提交,2:新生成记录
     */
    private Integer expFlag;

    /**
     * 退回重新提交用到
     */
    private String pid;

    private ExpFinanceRoleDTO role = new ExpFinanceRoleDTO();

    /** 收款方公司id */
    private String enterpriseId;

    /** 收款方公司名称 */
    private String enterpriseName;

    private List<String> deleteAttachList = new ArrayList<>();


    /**
     * 需要抄送人的companyUserId
     */
    private List<String> ccCompanyUserList = new ArrayList<>();

    public List<String> getCcCompanyUserList() {
        return ccCompanyUserList;
    }

    public void setCcCompanyUserList(List<String> ccCompanyUserList) {
        this.ccCompanyUserList = ccCompanyUserList;
    }

    public List<String> getDeleteAttachList() {
        return deleteAttachList;
    }

    public void setDeleteAttachList(List<String> deleteAttachList) {
        this.deleteAttachList = deleteAttachList;
    }

    public List<ExpDetailDTO> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<ExpDetailDTO> detailList) {
        this.detailList = detailList;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getExpFlag() {
        return expFlag;
    }

    public void setExpFlag(Integer expFlag) {
        this.expFlag = expFlag;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }


    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus == null ? null : approveStatus.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId == null ? null : departId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getAuditPersonName() {
        return auditPersonName;
    }

    public void setAuditPersonName(String auditPersonName) {
        this.auditPersonName = auditPersonName;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public int getDetailCount() {
        return detailCount;
    }

    public void setDetailCount(int detailCount) {
        this.detailCount = detailCount;
    }

    public BigDecimal getExpSumAmount() {
        return expSumAmount;
    }

    public void setExpSumAmount(BigDecimal expSumAmount) {
        this.expSumAmount = expSumAmount;
    }

    public String getExpUse() {
        return expUse;
    }

    public void setExpUse(String expUse) {
        this.expUse = expUse;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getApproveStatusName() {
        return approveStatusName;
    }

    public void setApproveStatusName(String approveStatusName) {
        this.approveStatusName = approveStatusName;
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getExpTypeName() {
        return expTypeName;
    }

    public void setExpTypeName(String expTypeName) {
        this.expTypeName = expTypeName;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getExpParentType() {
        return expParentType;
    }

    public void setExpParentType(String expParentType) {
        this.expParentType = expParentType;
    }

    public String getExpTypeParentName() {
        return expTypeParentName;
    }

    public void setExpTypeParentName(String expTypeParentName) {
        this.expTypeParentName = expTypeParentName;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getExpPName() {
        return expPName;
    }

    public void setExpPName(String expPName) {
        this.expPName = expPName;
    }

    public String getExpAllName() {
        return expAllName;
    }

    public void setExpAllName(String expAllName) {
        this.expAllName = expAllName;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(Date allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getAllocationUserName() {
        return allocationUserName;
    }

    public void setAllocationUserName(String allocationUserName) {
        this.allocationUserName = allocationUserName;
    }

    public String getAllocationUserCompanyId() {
        return allocationUserCompanyId;
    }

    public void setAllocationUserCompanyId(String allocationUserCompanyId) {
        this.allocationUserCompanyId = allocationUserCompanyId;
    }
    public List<ExpAttachDTO> getExpAttachDTOList() {
        return expAttachDTOList;
    }

    public void setExpAttachDTOList(List<ExpAttachDTO> expAttachDTOList) {
        this.expAttachDTOList = expAttachDTOList;
    }

    public ExpFinanceRoleDTO getRole() {
        return role;
    }

    public void setRole(ExpFinanceRoleDTO role) {
        this.role = role;
    }
}