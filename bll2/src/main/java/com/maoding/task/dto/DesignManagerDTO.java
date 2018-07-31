package com.maoding.task.dto;

import com.maoding.projectmember.dto.ProjectMemberDTO;

public class DesignManagerDTO {

    /** 设计负责人信息 */
    private ProjectMemberDTO projectManager;

    /** 设计助理信息 */
    private ProjectMemberDTO assistant;

    public ProjectMemberDTO getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectMemberDTO projectManager) {
        this.projectManager = projectManager;
    }

    public ProjectMemberDTO getAssistant() {
        return assistant;
    }

    public void setAssistant(ProjectMemberDTO assistant) {
        this.assistant = assistant;
    }

}
