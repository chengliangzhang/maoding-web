package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/30
 * @description : 流程信息，用于显示流程、启动流程
 */
public class ProcessDefineDetailDTO extends CoreShowDTO {
    /** id: 流程key */

    /** 流程说明 */
    private String documentation;

    /** 流程分类关键字，定义见ProcessTypeConst.PROCESS_TYPE_xxx */
    private String key;

    /** 流程类型，定义见ProcessTypeConst.TYPE_xxx */
    private Integer type;

    /** 允许启动流程的角色(group)列表 */
    private List<FlowGroupDTO> candidateStarterGroupList;

    /** 允许启动流程的用户(user)列表 */
    private List<FlowUserDTO> candidateStarterUserList;

    //维持兼容性
    /** 允许启动流程的角色编号列表 */
    @Deprecated
    private List<String> candidateStarterGroups;

    /** 允许启动流程的用户编号列表 */
    @Deprecated
    private List<String> candidateStarterUsers;



    /** 流程所拥有的路径序列 */
    private List<FlowTaskGroupDTO> flowTaskGroupList;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public List<FlowTaskGroupDTO> getFlowTaskGroupList() {
        return flowTaskGroupList;
    }

    public void setFlowTaskGroupList(List<FlowTaskGroupDTO> flowTaskGroupList) {
        this.flowTaskGroupList = flowTaskGroupList;
    }

    public List<FlowGroupDTO> getCandidateStarterGroupList() {
        return candidateStarterGroupList;
    }

    public void setCandidateStarterGroupList(List<FlowGroupDTO> candidateStarterGroupList) {
        this.candidateStarterGroupList = candidateStarterGroupList;
    }

    public List<String> getCandidateStarterGroups() {
        return candidateStarterGroups;
    }

    public void setCandidateStarterGroups(List<String> candidateStarterGroups) {
        this.candidateStarterGroups = candidateStarterGroups;
    }

    public List<FlowUserDTO> getCandidateStarterUserList() {
        return candidateStarterUserList;
    }

    public void setCandidateStarterUserList(List<FlowUserDTO> candidateStarterUserList) {
        this.candidateStarterUserList = candidateStarterUserList;
    }

    public List<String> getCandidateStarterUsers() {
        return candidateStarterUsers;
    }

    public void setCandidateStarterUsers(List<String> candidateStarterUsers) {
        this.candidateStarterUsers = candidateStarterUsers;
    }
}
