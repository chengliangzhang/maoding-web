package com.maoding.activiti.dto;


import com.maoding.core.base.dto.CoreDTO;

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
    private String content;
    /** 允许执行任务的角色(group)列表 */
    private List<CoreDTO> candidateGroupList;
    /** 允许执行任务的用户(user)列表 */
    private List<CoreDTO> candidateUserList;
    /** 默认执行人 */
    private CoreDTO assignee;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CoreDTO> getCandidateGroupList() {
        return candidateGroupList;
    }

    public void setCandidateGroupList(List<CoreDTO> candidateGroupList) {
        this.candidateGroupList = candidateGroupList;
    }

    public List<CoreDTO> getCandidateUserList() {
        return candidateUserList;
    }

    public void setCandidateUserList(List<CoreDTO> candidateUserList) {
        this.candidateUserList = candidateUserList;
    }

    public CoreDTO getAssignee() {
        return assignee;
    }

    public void setAssignee(CoreDTO assignee) {
        this.assignee = assignee;
    }
}
