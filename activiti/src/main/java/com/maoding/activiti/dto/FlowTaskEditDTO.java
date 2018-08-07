package com.maoding.activiti.dto;


import com.maoding.user.dto.UserDTO;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: FlowTaskEditDTO
 * @description : 流程的任务节点编辑信息
 */
public class FlowTaskEditDTO extends FlowElementEditDTO {
    /** id: 流程任务编号 */

    /** 任务说明 */
    private String documentation;

    /** 允许执行任务的角色编号组合字符串 */
    private List<String> candidateGroups;

    /** 允许执行任务的用户编号组合字符串 */
    private List<String> candidateUsers;

    /** 默认执行人编号 */
    private String assignee;

    /** 允许执行任务的角色编号组合字符串 */
    private List<GroupDTO> candidateGroupList;

    /** 允许执行任务的用户编号组合字符串 */
    private List<UserDTO> candidateUserList;

    /** 默认执行人 */
    private UserDTO assigneeUser;

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public List<String> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public List<String> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<String> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public List<GroupDTO> getCandidateGroupList() {
        return candidateGroupList;
    }

    public void setCandidateGroupList(List<GroupDTO> candidateGroupList) {
        this.candidateGroupList = candidateGroupList;
    }

    public List<UserDTO> getCandidateUserList() {
        return candidateUserList;
    }

    public void setCandidateUserList(List<UserDTO> candidateUserList) {
        this.candidateUserList = candidateUserList;
    }

    public UserDTO getAssigneeUser() {
        return assigneeUser;
    }

    public void setAssigneeUser(UserDTO assigneeUser) {
        this.assigneeUser = assigneeUser;
    }
}
