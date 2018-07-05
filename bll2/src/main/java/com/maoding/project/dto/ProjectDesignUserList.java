package com.maoding.project.dto;

import com.maoding.core.constant.ProjectMemberType;

/**
 * Created by Wuwq on 2016/10/27.
 */
public class ProjectDesignUserList {

    /**
     * 设计人
     */
    private ProjectTaskProcessNodeDTO designUser = new ProjectTaskProcessNodeDTO("设计人", ProjectMemberType.PROJECT_DESIGNER);

    /**
     * 校对人
     */
    private ProjectTaskProcessNodeDTO checkUser = new ProjectTaskProcessNodeDTO("校对人", ProjectMemberType.PROJECT_PROOFREADER);

    /**
     * 审核人
     */
    private ProjectTaskProcessNodeDTO examineUser = new ProjectTaskProcessNodeDTO("审核人", ProjectMemberType.PROJECT_AUDITOR);

    public ProjectTaskProcessNodeDTO getDesignUser() {
        return designUser;
    }

    public void setDesignUser(ProjectTaskProcessNodeDTO designUser) {
        this.designUser = designUser;
    }

    public ProjectTaskProcessNodeDTO getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(ProjectTaskProcessNodeDTO checkUser) {
        this.checkUser = checkUser;
    }

    public ProjectTaskProcessNodeDTO getExamineUser() {
        return examineUser;
    }

    public void setExamineUser(ProjectTaskProcessNodeDTO examineUser) {
        this.examineUser = examineUser;
    }
}
