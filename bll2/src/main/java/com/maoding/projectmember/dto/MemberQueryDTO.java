package com.maoding.projectmember.dto;


import com.maoding.core.base.dto.BaseQueryDTO;

/**
 * Created by Chengliang.zhang on 2017/7/20.
 */
public class MemberQueryDTO extends BaseQueryDTO {
    /** 人员所在公司id */
    private String companyId;
    /** 人员所在项目id */
    private String projectId;
    /** 项目所在公司id */
    private String projectCompanyId;
    /** 人员类型 */
    private Integer memberType;

    private String targetId;

    private String companyUserId;

    private String accountId;

    private String nodeId;

    private Integer status;

    private Integer deleted;

    private Integer seq;

    /**
     * 隐藏标识（1：隐藏）
     */
    private String param1;

    private String param2;

    private String notContainHeadImg;

    /** 相关父任务 */
    private String parentTaskId;

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public MemberQueryDTO(){}
    public MemberQueryDTO(String companyId, String projectId, Integer memberType){
        setCompanyId(companyId);
        setProjectId(projectId);
        setMemberType(memberType);
        setPageSize(1);
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public String getProjectCompanyId() {
        return projectCompanyId;
    }

    public void setProjectCompanyId(String projectCompanyId) {
        this.projectCompanyId = projectCompanyId;
    }

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNotContainHeadImg() {
        return notContainHeadImg;
    }

    public void setNotContainHeadImg(String notContainHeadImg) {
        this.notContainHeadImg = notContainHeadImg;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}
