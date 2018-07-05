package com.maoding.project.dto;
import com.maoding.core.base.dto.BaseDTO;


/**
 * Created by Wuwq on 2016/10/27.
 */
public class ProjectProcessNodeDTO extends BaseDTO {

    /**
     * 任务id（project_Task 表中的Id)
     */
    private String processId;

    private String nodeName;

    private int seq;

    /**
     * 节点在流程中所排的顺序
     */
    private int nodeSeq;

    private String companyUserId;

    private String userName;

    private String completeTime;

    private int status;//2:未完成，1：完成

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }


    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNodeSeq() {
        return nodeSeq;
    }

    public void setNodeSeq(int nodeSeq) {
        this.nodeSeq = nodeSeq;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }
}
