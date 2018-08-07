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

    /** 允许执行任务的角色(group)组合字符串 */
    private List<GroupDTO> candidateGroups;

    /** 允许执行任务的用户(user)组合字符串 */
    private List<UserDTO> candidateUsers;

    /** 默认执行人 */
    private UserDTO assignee;

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public List<GroupDTO> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<GroupDTO> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public List<UserDTO> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<UserDTO> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public UserDTO getAssignee() {
        return assignee;
    }

    public void setAssignee(UserDTO assignee) {
        this.assignee = assignee;
    }
}
