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
    private List<String> candidateGroups;

    /** 允许执行任务的用户(user)组合字符串 */
    private List<String> candidateUsers;

    /** 默认执行人 */
    private String assignee;

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
}
