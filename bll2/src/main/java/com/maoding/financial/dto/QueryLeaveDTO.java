package com.maoding.financial.dto;

import com.maoding.core.base.dto.QueryDTO;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * @author : 张成亮
 * 日    期 : 2018/4/18 10:35
 * 描    述 :
 */
public class QueryLeaveDTO extends QueryDTO {
    private String expId;
    private String companyUserId;
    private String approveStatus;
    private Date minLeaveEndTime;
    private String expType;
    private String leaveType;

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Date getMinLeaveEndTime() {
        return minLeaveEndTime;
    }

    public void setMinLeaveEndTime(Date minLeaveEndTime) {
        this.minLeaveEndTime = minLeaveEndTime;
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }
}
