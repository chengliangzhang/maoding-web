package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

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
    private List<GroupDTO> candidateStarterGroups;

    /** 允许启动流程的用户(user)列表 */
    private List<UserDTO> candidateStarterUsers;

    /** 流程所拥有的任务节点序列，以条件编号-任务序列对展现 */
    @Deprecated
    private Map<String,List<FlowTaskDTO>> flowTaskListMap;

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

    @Deprecated
    public Map<String, List<FlowTaskDTO>> getFlowTaskListMap() {
        return flowTaskListMap;
    }

    @Deprecated
    public void setFlowTaskListMap(Map<String, List<FlowTaskDTO>> flowTaskListMap) {
        this.flowTaskListMap = flowTaskListMap;
    }

    public List<GroupDTO> getCandidateStarterGroups() {
        return candidateStarterGroups;
    }

    public void setCandidateStarterGroups(List<GroupDTO> candidateStarterGroups) {
        this.candidateStarterGroups = candidateStarterGroups;
    }

    public List<UserDTO> getCandidateStarterUsers() {
        return candidateStarterUsers;
    }

    public void setCandidateStarterUsers(List<UserDTO> candidateStarterUsers) {
        this.candidateStarterUsers = candidateStarterUsers;
    }
}
