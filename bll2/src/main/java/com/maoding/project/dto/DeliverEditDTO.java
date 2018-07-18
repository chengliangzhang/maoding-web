package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.Date;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/16
 * @description :
 */
public class DeliverEditDTO extends BaseDTO {
    /** 交付名称 */
    private String name;
    /** 说明 */
    private String description;
    /** 截止时间 */
    private Date endTime;
    /** 发起人 */
    private String createBy;
    /** 发起时间 */
    private Date createDate;
    /** 交付是否已经结束 */
    private String isFinished;
    /** 更改的负责人信息列表 */
    private List<ResponseEditDTO> changedResponseList;
    /** 签发任务编号 */
    private String issueId;
    /** 项目编号 */
    private String projectId;
    /** 公司编号 */
    private String companyId;
    /** 父目录编号 */
    private String parentDirId;
    /** 父目录编号路径 */
    private String parentDirIdPath;
    /** 目录类型 */
    private Integer type;
    /** 排序序号 */
    private Integer seq;
    /** 交付目录编号 */
    private String nodeId;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParentDirIdPath() {
        return parentDirIdPath;
    }

    public void setParentDirIdPath(String parentDirIdPath) {
        this.parentDirIdPath = parentDirIdPath;
    }

    public String getParentDirId() {
        return parentDirId;
    }

    public void setParentDirId(String parentDirId) {
        this.parentDirId = parentDirId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public List<ResponseEditDTO> getChangedResponseList() {
        return changedResponseList;
    }

    public void setChangedResponseList(List<ResponseEditDTO> changedResponseList) {
        this.changedResponseList = changedResponseList;
    }
}
