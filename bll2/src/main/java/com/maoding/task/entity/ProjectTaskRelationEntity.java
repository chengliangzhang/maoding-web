package com.maoding.task.entity;


import com.maoding.core.base.entity.BaseEntity;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskRelationEntity
 * 类描述：项目任务（任务签发组织之间的关系）
 * 作    者：MaoSF
 * 日    期：2016年12月28日-上午10:13:28
 */
public class ProjectTaskRelationEntity extends BaseEntity{

    private String projectId;

    /**
     * 任务发包方id
     */
    private String fromCompanyId;

    /**
     * 任务接包方id
     */
    private String toCompanyId;


    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务状态
     */
    private int relationStatus;

    /**
     *  暂时未用
     */
    private int relationType;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getToCompanyId() {
        return toCompanyId;
    }

    public void setToCompanyId(String toCompanyId) {
        this.toCompanyId = toCompanyId == null ? null : toCompanyId.trim();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public int getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(int relationStatus) {
        this.relationStatus = relationStatus;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
    }
}