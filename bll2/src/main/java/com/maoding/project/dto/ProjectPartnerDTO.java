package com.maoding.project.dto;

/**
 * Created by Chengliang.zhang on 2017/5/6.
 */
public class ProjectPartnerDTO {

    String id;
    /**
     * 被邀请人的电话号码
     */
    String phone;
    /**
     * 被邀请人的公司
     * 如果为空，则被邀请人尚未创建或指定公司
     */
    String companyId;
    /**
     * 被邀请人的公司的名字
     */
    String companyName;
    /**
     * 被邀请人所在公司的企业负责人
     */
    String companyManagerId;
    /**
     * 被邀请人所在公司的企业负责人姓名
     */
    String companyManagerName;
    /**
     * 被邀请人的运营负责人
     */
    String projectManagerId;
    /**
     * 被邀请人的运营负责人的名字
     */
    String projectManagerName;
    /**
     * 签发给被邀请人的任务名称的拼接字符串
     */
    String taskNameSplice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyManagerId() {
        return companyManagerId;
    }

    public void setCompanyManagerId(String companyManagerId) {
        this.companyManagerId = companyManagerId;
    }

    public String getCompanyManagerName() {
        return companyManagerName;
    }

    public void setCompanyManagerName(String companyManagerName) {
        this.companyManagerName = companyManagerName;
    }

    public String getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(String projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    public String getTaskNameSplice() {
        return taskNameSplice;
    }

    public void setTaskNameSplice(String taskNameSplice) {
        this.taskNameSplice = taskNameSplice;
    }
}
