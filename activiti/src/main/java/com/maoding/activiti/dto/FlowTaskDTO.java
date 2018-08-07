package com.maoding.activiti.dto;

import com.maoding.user.dto.UserDTO;

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
    private List<GroupDTO> candidateGroupList;

    @Deprecated
    private List<String> candidateGroups;

    /** 允许执行任务的用户(user)组合字符串 */
    private List<UserDTO> candidateUserList;

    @Deprecated
    private List<String> candidateUsers;

    /** 默认执行人 */
    private UserDTO assigneeUser;

    @Deprecated
    private String assignee;

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public List<GroupDTO> getCandidateGroupList() {
        return candidateGroupList;
    }

    public void setCandidateGroupList(List<GroupDTO> candidateGroupList) {
        this.candidateGroupList = candidateGroupList;
    }

    public List<String> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public List<UserDTO> getCandidateUserList() {
        return candidateUserList;
    }

    public void setCandidateUserList(List<UserDTO> candidateUserList) {
        this.candidateUserList = candidateUserList;
    }

    public List<String> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<String> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public UserDTO getAssigneeUser() {
        return assigneeUser;
    }

    public void setAssigneeUser(UserDTO assigneeUser) {
        this.assigneeUser = assigneeUser;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}
