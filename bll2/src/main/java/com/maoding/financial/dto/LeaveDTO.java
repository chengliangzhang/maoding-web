package com.maoding.financial.dto;

import com.maoding.core.util.StringUtil;
import com.maoding.org.dto.CompanyUserDataDTO;

import java.util.Date;
import java.util.List;

public class LeaveDTO extends AuditDetailDTO{

    /**
     * 请假类型
     */
    private String leaveType;

    /**
     * 请假类型
     */
    private String leaveTypeName;

    /**
     * 出差地
     */
    private String address;

    /**
     * 时间单位，目前默认为天
     */
    private Integer timeUnit;

    private String leaveTime;

    /**
     * 请假、出差的开始时间
     */
    private Date startTime;

    /**
     * 请假、出差的结束时间
     */
    private Date endTime;

    private String submitTime;

    private Object processFlag;

    /**
     * 流程的条件列表（人员列表）
     */
    private Object conditionList;

    /**
     * 流程的类型
     */
    private Object processType;

    private List<CompanyUserDataDTO> ccCompanyUserList;

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getLeaveTypeName() {
        if(StringUtil.isNullOrEmpty(this.leaveType)){
            return "其他";
        }
        switch (this.leaveType){
            case "1":
                return "年假";
            case "2":
                return "事假";
            case "3":
                return "病假";
            case "4":
                return "调休假";
            case "5":
                return "婚假";
            case "6":
                return "产假";
            case "7":
                return "陪产假";
            case "8":
                return "丧假";
            case "9":
                return "其他";
            case "10":
                return "出差";
            default:
                return "其他";
        }
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public List<CompanyUserDataDTO> getCcCompanyUserList() {
        return ccCompanyUserList;
    }

    public void setCcCompanyUserList(List<CompanyUserDataDTO> ccCompanyUserList) {
        this.ccCompanyUserList = ccCompanyUserList;
    }

    public Object getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(Object processFlag) {
        this.processFlag = processFlag;
    }

    public Object getConditionList() {
        return conditionList;
    }

    public void setConditionList(Object conditionList) {
        this.conditionList = conditionList;
    }

    public Object getProcessType() {
        return processType;
    }

    public void setProcessType(Object processType) {
        this.processType = processType;
    }
}
