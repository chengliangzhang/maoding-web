package com.maoding.dynamic.dto;

import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/6/20.
 */
public class ZProcessNodeDTO {
    /**
     * 节点所属任务名称
     */
    String taskName;
    /**
     * 节点所属任务全名
     */
    String taskFullName;
    /**
     * 节点类型名称
     */
    String nodeName;
    /**
     * 负责人名称
     */
    String userName;
    /**
     * 完成时间
     */
    Date completeDate;

    /**
     * 项目编号
     */
    String projectId;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getTaskFullName() {
        return taskFullName;
    }

    public void setTaskFullName(String taskFullName) {
        this.taskFullName = taskFullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
