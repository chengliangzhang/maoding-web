package com.maoding.process.dto;

import com.maoding.core.base.dto.BaseDTO;

public class ProcessEditDTO extends BaseDTO {

    private String processId;

    private String processName;//流程名称

    private String description;//说明

    private String companyId;//使用团队

    private String relationCompanyId;//关联团队

    private Integer processType;//流程类型（请参考ProcessConst）

    private Integer companyType;//组织类型（前端暂不需要）

    private Integer status;//选中状态（1：被选择，0：取消选中）

    /** 项目收支流程节点状态 **/
    /** 项目收支流程 **/
//    private Integer invoiceStatus;//发票状态，1：选择状态
//
//    private Integer receiveOrPayStatus;//到账/付款状态，1：选择状态
//
//    private Integer receiveOrPayAbleStatus;//应收状态，1：选择状态
//
//    private Integer syncStatus;//同步状态 1：选择状态

    //1：发票类型，2：应收状态，3：应付状态，4：已收状态，5：已付状态，6：同步
    private Integer statusType;

    //节点id
    private String nodeId;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRelationCompanyId() {
        return relationCompanyId;
    }

    public void setRelationCompanyId(String relationCompanyId) {
        this.relationCompanyId = relationCompanyId;
    }

    public Integer getProcessType() {
        return processType;
    }

    public void setProcessType(Integer processType) {
        this.processType = processType;
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatusType() {
        return statusType;
    }

    public void setStatusType(Integer statusType) {
        this.statusType = statusType;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
