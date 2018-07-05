package com.maoding.statistic.dto;

import com.maoding.attach.dto.FilePathDTO;

/**
 * 人员任务统计DTO on 2017/6/7.
 */
public class UserTaskStaticDTO extends FilePathDTO {

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 公司成员id
     */
    private String companyUserId;

    /**
     * 参与项目数
     */
    private int projectCount;

    /**
     * 任务总数
     */
    private int taskCount;

    /**
     * 完成数
     */
    private int completeCount;

    /**
     * 未完成数
     */
    private int notCompleteCount;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public int getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(int projectCount) {
        this.projectCount = projectCount;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getCompleteCount() {
        completeCount = taskCount - notCompleteCount;
        return completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }

    public int getNotCompleteCount() {
        return notCompleteCount;
    }

    public void setNotCompleteCount(int notCompleteCount) {
        this.notCompleteCount = notCompleteCount;
    }
}
