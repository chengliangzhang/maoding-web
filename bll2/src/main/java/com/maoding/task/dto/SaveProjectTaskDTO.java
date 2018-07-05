package com.maoding.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskDTO
 * 类描述：任务分解数据
 * 作    者：MaoSF
 * 日    期：2016年12月31日-上午10:13:28
 */
public class SaveProjectTaskDTO extends BaseDTO {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 备注
     */
    private String taskRemark;

    /**
     * 任务父Id
     */
    private String taskPid;


    /**
     *  开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;


    /**
     *经营负责人
     */
    private String managerId;

    /**
     * 技术负责人
     */
    private String designerId;

    /**
     *  设计人（companyUserId）
     */
    private List<String> designUserList = new ArrayList<>();

    /**
     *  校对人（companyUserId）
     */
    private List<String> checkUserList = new ArrayList<>();

    /**
     *  审核人（companyUserId）
     */
    private List<String> examineUserList = new ArrayList<>();

    /**
     *任务所属公司id
     */
    private String companyId;

    /**
     *任务类型（经营方：2，生产方：0（默认不传递））
     */
    private int taskType;

    private int flag;//0：自营，1：签发给合作伙伴

    private String orgId;//部门id

    private String memo;//变更原因

    private int isChangeOrg; //如果是改变组织，则为1，否则为0

    /** 任务优先级 */
    private Short priority;

    public Short getPriority() {
        return priority;
    }

    public void setPriority(Short priority) {
        this.priority = priority;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskPid() {
        return taskPid;
    }

    public void setTaskPid(String taskPid) {
        this.taskPid = taskPid;
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

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getDesignerId() {
        return designerId;
    }

    public void setDesignerId(String designerId) {
        this.designerId = designerId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<String> getDesignUserList() {
        return designUserList;
    }

    public void setDesignUserList(List<String> designUserList) {
        this.designUserList = designUserList;
    }

    public List<String> getCheckUserList() {
        return checkUserList;
    }

    public void setCheckUserList(List<String> checkUserList) {
        this.checkUserList = checkUserList;
    }

    public List<String> getExamineUserList() {
        return examineUserList;
    }

    public void setExamineUserList(List<String> examineUserList) {
        this.examineUserList = examineUserList;
    }

    public int getIsChangeOrg() {
        return isChangeOrg;
    }

    public void setIsChangeOrg(int isChangeOrg) {
        this.isChangeOrg = isChangeOrg;
    }
}