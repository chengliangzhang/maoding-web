package com.maoding.process.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.financial.dto.AuditBaseDTO;

import java.util.HashMap;
import java.util.Map;

public class TaskDTO extends AuditBaseDTO {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 当前处理人id
     */
    private String companyUserId;

    /**
     * 下一个处理人id（用于自由流程传递下一个处理人的id）
     */
    private String nextCompanyUserId;

    /**
     * 业务记录的id
     */
    private String businessKey;

    /**
     * 版本控制字段
     */
    private Integer versionNum;

    /**
     * 审批状态（1：通过，2：拒绝）
     */
    private String approveStatus;

    /**
     * 处理任务携带的参数
     */
    Map<String, Object> variables = new HashMap<>();

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getNextCompanyUserId() {
        return nextCompanyUserId;
    }

    public void setNextCompanyUserId(String nextCompanyUserId) {
        this.nextCompanyUserId = nextCompanyUserId;
    }
}
