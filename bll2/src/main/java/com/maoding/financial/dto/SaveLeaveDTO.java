package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaveLeaveDTO extends BaseDTO {

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
     *  用户id(companyUserId)
     */
    private String companyUserId;

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

    /**
     * 请假类型
     */
    private String leaveType;

    /**
     * 出差地
     */
    private String address;

    /**
     * 时间单位，目前默认为天
     */
    private Integer timeUnit;

    private String leaveTime;

    private String projectId;

    /**
     * 前端接收的开始的日期
     */
    private String leaveStartTime;

    /**
     * 前端接收的结束的日期
     */
    private String leaveEndTime;


    /**
     * 请假、出差的开始时间
     */
    private Date startTime;

    /**
     * 请假、出差的结束时间
     */
    private Date endTime;

    private List<String> deleteAttachList = new ArrayList<>();

    /**
     * 需要抄送人的companyUserId
     */
    private List<String> ccCompanyUserList = new ArrayList<>();

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
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
        this.approveStatus = approveStatus;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
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

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Date getStartTime() {
        if (!StringUtil.isNullOrEmpty(this.leaveStartTime)){
            startTime = DateUtils.str2Date(leaveStartTime, DateUtils.time_sdf_slash);
        }
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        if (!StringUtil.isNullOrEmpty(this.leaveEndTime)){
            endTime = DateUtils.str2Date(leaveEndTime, DateUtils.time_sdf_slash);
        }
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLeaveStartTime() {
        return leaveStartTime;
    }

    public void setLeaveStartTime(String leaveStartTime) {
        this.leaveStartTime = leaveStartTime;
    }

    public String getLeaveEndTime() {
        return leaveEndTime;
    }

    public void setLeaveEndTime(String leaveEndTime) {
        this.leaveEndTime = leaveEndTime;
    }

    public List<String> getDeleteAttachList() {
        return deleteAttachList;
    }

    public void setDeleteAttachList(List<String> deleteAttachList) {
        this.deleteAttachList = deleteAttachList;
    }

    public List<String> getCcCompanyUserList() {
        return ccCompanyUserList;
    }

    public void setCcCompanyUserList(List<String> ccCompanyUserList) {
        this.ccCompanyUserList = ccCompanyUserList;
    }
}
