package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/25 10:01
 * 描    述 : 出差、请假查询条件
 */
@Alias("LeaveDetailQueryDTO")
public class LeaveDetailQueryDTO extends BaseDTO implements Serializable {
    /**
     * 起始日期
     */
    private Date startDate;
    /**
     * 终止日期
     */
    private Date endDate;
    /**
     * 返回的统计列表从符合条件的记录集中第几条记录开始
     */
    private Integer startLine;
    /**
     * 返回的统计列表最多为多少条
     */
    private Integer linesCount;
    /**
     * 申请人
     */
    private String applyName;
    /**
     * 审批人
     */
    private String auditPerson;
    /**
     * 审批时间开始
     */
    private Date startApproveDate;
    /**
     * 审批时间结束
     */
    private Date endApproveDate;
    /**
     * 类型
     * */
    private Integer type;
    /**
     * 组织id
     * */
    private String companyId;
    /**
     * 请假类型tyep
     * */
    private String leaveType;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getLinesCount() {
        return linesCount;
    }

    public void setLinesCount(Integer linesCount) {
        this.linesCount = linesCount;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public Date getStartApproveDate() {
        return startApproveDate;
    }

    public void setStartApproveDate(Date startApproveDate) {
        this.startApproveDate = startApproveDate;
    }

    public Date getEndApproveDate() {
        return endApproveDate;
    }

    public void setEndApproveDate(Date endApproveDate) {
        this.endApproveDate = endApproveDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
}
