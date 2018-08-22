package com.maoding.financial.dto;

import com.maoding.attach.dto.FileDataDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditDetailDTO {

    private String id;

    /**
     * targetId(app端生产的id，由于文件上传在第三方，所以为了关联文件信息，该处使用targetId携带了本记录的id信息
     */
    private String targetId;

    /**
     * 如果被退回重新编辑，则原来的记录设置为无效，并新增记录。但是要关联原来的记录的id，此处的pid为原记录的id
     */
    private String pid;

    /**
     * 审核人id
     */
    private String auditPerson;

    /**
     * 审核人
     */
    private String auditPersonName;

    /**
     *  用户id(companyUserId)
     */
    private String companyUserId;


    private String accountId;

    /**
     * userId = accountId 前端需要用userId
     */
    private String userId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 日期
     */
    private Date expDate;

    /**
     * 审批状态(0:待审核，1:同意，2，退回,3:撤回,4:删除）
     */
    private String approveStatus;

    /**
     * 企业id
     */
    private String companyId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本控制字段
     */
    private Integer versionNum;

    /**
     * 单号
     */
    private String expNo;

    /**
     * 0:没有任何操作，1:退回记录重新提交,2:新生成记录
     */
    private Integer expFlag;

    /**
     * 报销类别：1=报销申请，2=费用申请,3=请假，4=出差
     */
    private Integer type;

    private String projectId;

    private String projectName;

    private String approveDate;

    /**
     * 退回原因
     */
    private String callbackReason;

    /**
     * 审核记录
     */
    private List<AuditDTO> auditList = new ArrayList<>();

    /**
     * 当前审核人记录
     */
    private AuditDTO currentAuditPerson = new AuditDTO();
    /**
     * 附件
     */
    private List<FileDataDTO> attachList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getAuditPersonName() {
        return auditPersonName;
    }

    public void setAuditPersonName(String auditPersonName) {
        this.auditPersonName = auditPersonName;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        this.approveStatus = approveStatus;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }

    public Integer getExpFlag() {
        return expFlag;
    }

    public void setExpFlag(Integer expFlag) {
        this.expFlag = expFlag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getCallbackReason() {
        return callbackReason;
    }

    public void setCallbackReason(String callbackReason) {
        this.callbackReason = callbackReason;
    }

    public List<AuditDTO> getAuditList() {
        return auditList;
    }

    public void setAuditList(List<AuditDTO> auditList) {
        this.auditList = auditList;
    }

    public List<FileDataDTO> getAttachList() {
        return attachList;
    }

    public void setAttachList(List<FileDataDTO> attachList) {
        this.attachList = attachList;
    }

    public AuditDTO getCurrentAuditPerson() {
        return currentAuditPerson;
    }

    public void setCurrentAuditPerson(AuditDTO currentAuditPerson) {
        this.currentAuditPerson = currentAuditPerson;
    }

    public String getUserId() {
        return accountId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
