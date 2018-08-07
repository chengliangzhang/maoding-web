package com.maoding.activiti.dto;


import com.maoding.core.base.dto.CoreEditDTO;
import com.maoding.user.dto.UserDTO;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: DeplymentEditDTO
 * @description : 流程修改信息，用于增加流程、删除或关闭流程
 */
public class ProcessDefineDetailEditDTO extends CoreEditDTO {
    /** id: 流程key */

    /** 流程名称 */
    private String name;

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

    /** 流程需更改的任务节点序列 */
    private List<FlowTaskGroupEditDTO> flowTaskGroupList;

    public List<FlowTaskGroupEditDTO> getFlowTaskGroupList() {
        return flowTaskGroupList;
    }

    public void setFlowTaskGroupList(List<FlowTaskGroupEditDTO> flowTaskGroupList) {
        this.flowTaskGroupList = flowTaskGroupList;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }
}
