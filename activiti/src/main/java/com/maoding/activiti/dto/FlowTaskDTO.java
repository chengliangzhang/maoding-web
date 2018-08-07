package com.maoding.activiti.dto;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/30
 * @description :
 */
public class FlowTaskDTO extends FlowElementDTO {
    /** id: 流程任务编号 */

    /** 任务说明 */
    private String documentation;

    /** 允许执行任务的角色(group)组合字符串 */
    private List<FlowGroupDTO> candidateGroupList;

    /** 允许执行任务的用户(user)组合字符串 */
    private List<FlowUserDTO> candidateUserList;

    /** 默认执行人 */
    private FlowUserDTO assigneeUser;

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public List<FlowGroupDTO> getCandidateGroupList() {
        return candidateGroupList;
    }

    public void setCandidateGroupList(List<FlowGroupDTO> candidateGroupList) {
        this.candidateGroupList = candidateGroupList;
    }

    public List<FlowUserDTO> getCandidateUserList() {
        return candidateUserList;
    }

    public void setCandidateUserList(List<FlowUserDTO> candidateUserList) {
        this.candidateUserList = candidateUserList;
    }

    public FlowUserDTO getAssigneeUser() {
        return assigneeUser;
    }

    public void setAssigneeUser(FlowUserDTO assigneeUser) {
        this.assigneeUser = assigneeUser;
    }
}
