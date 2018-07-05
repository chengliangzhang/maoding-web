package com.maoding.project.dto;
import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Wuwq on 2016/10/27.
 */
public class ProjectProcessDTO extends BaseDTO {

    private String processName;

    private String companyId;

    private String projectId;

    private String taskManageId;

    private String status;

    private int seq;

    /**
     * 移动端使用
     */
    private int flag;//0:没有完成，1：完成

    /**
     * 移动端使用
     */
    private String currentNodeId;//处理到当前节点的id

    /**
     * 移动端使用
     */
    private int endSatus;//0,4：无操作，1.提交，2.打回，通过，3.打回，完成
    private int nodeSeq;//当前操作节点序号

    private int lastHandSeq;//最后一个处理流程的节点序号

    private String companyUserId;

    private Integer memberType;

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    private List<ProjectProcessNodeDTO> nodes=new ArrayList<>();

    private List<ProjectProcessNode> projectProcessNodes=new ArrayList<ProjectProcessNode>();

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
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

    public String getTaskManageId() {
        return taskManageId;
    }

    public void setTaskManageId(String taskManageId) {
        this.taskManageId = taskManageId;
    }

    public List<ProjectProcessNodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<ProjectProcessNodeDTO> nodes) {
        this.nodes = nodes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public int getEndSatus() {
        return endSatus;
    }

    public void setEndSatus(int endSatus) {
        this.endSatus = endSatus;
    }

    public int getNodeSeq() {
        return nodeSeq;
    }

    public void setNodeSeq(int nodeSeq) {
        this.nodeSeq = nodeSeq;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getLastHandSeq() {
        return lastHandSeq;
    }

    public void setLastHandSeq(int lastHandSeq) {
        this.lastHandSeq = lastHandSeq;
    }

    public List<ProjectProcessNode> getProjectProcessNodes() {
        return projectProcessNodes;
    }

    public void setProjectProcessNodes(List<ProjectProcessNode> projectProcessNodes) {
        this.projectProcessNodes = projectProcessNodes;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }
}
