package com.maoding.commonModule.entity;

import com.maoding.core.base.entity.BaseEntity;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/6/26 12:49
 * 描    述 :
 */
public class CustomConstEntity extends BaseEntity {
    private String companyId;
    private String projectId;
    private String taskId;
    private Short classicId;
    private Short codeId;
    private String title;
    private String extra;
    private Boolean deleted;

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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Short getClassicId() {
        return classicId;
    }

    public void setClassicId(Short classicId) {
        this.classicId = classicId;
    }

    public Short getCodeId() {
        return codeId;
    }

    public void setCodeId(Short codeId) {
        this.codeId = codeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
