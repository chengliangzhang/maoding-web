package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class SaveExpMainDTO extends BaseDTO {

    /**
     * 当前处理人
     */
    private String companyUserId;

    /**
     * 被选择的审核人id
     */
    private String auditPerson;

    /**
     * 版本控制字段
     */
    private Integer versionNum;

    /**
     * 是否是新增审批主记录
     */
    private boolean isSaveAuditMain;

    /**
     * 审核状态：0：提交主记录，1：同意（结束整个审核），2：拒绝，3：同意并转交
     */
    private String approveStatus;

    /**
     * 上一条审核记录的id
     */
    private String parentAuditId;

    /**
     * 审批类型
     */
    private String auditType;

    /**
     * 需要抄送人的companyUserId
     */
    private List<String> ccCompanyUserList = new ArrayList<>();

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    public List<String> getCcCompanyUserList() {
        return ccCompanyUserList;
    }

    public void setCcCompanyUserList(List<String> ccCompanyUserList) {
        this.ccCompanyUserList = ccCompanyUserList;
    }

    public boolean isSaveAuditMain() {
        return isSaveAuditMain;
    }

    public void setSaveAuditMain(boolean saveAuditMain) {
        isSaveAuditMain = saveAuditMain;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getParentAuditId() {
        return parentAuditId;
    }

    public void setParentAuditId(String parentAuditId) {
        this.parentAuditId = parentAuditId;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }
}
