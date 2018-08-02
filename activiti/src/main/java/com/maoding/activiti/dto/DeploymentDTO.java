package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreDTO;
import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;
import java.util.Map;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/30
 * @description : 流程信息，用于显示流程、启动流程
 */
public class DeploymentDTO extends CoreShowDTO {
    /** id: 流程key */
    /** 流程说明 */
    private String documentation;
    /** 允许启动流程的角色(group)列表 */
    private List<CoreDTO> candidateGroupList;
    /** 允许启动流程的用户(user)列表 */
    private List<CoreDTO> candidateUserList;
    /** 流程启动时的判断条件 */
    @Deprecated
    private List<FlowSequenceDTO> startSequenceList;
    /** 流程所拥有的任务节点序列，以条件编号-任务序列对展现 */
    private Map<String,List<FlowTaskDTO>> flowTaskListMap;
    /** 流程判断条件的名称 */
    private String varKey;
    /** 流程分类关键字，定义见ProcessTypeConst.PROCESS_TYPE_xxx */
    private String key;
    /** 流程类型，定义见ProcessTypeConst.TYPE_xxx */
    private Integer type;

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

    public String getVarKey() {
        return varKey;
    }

    public void setVarKey(String varKey) {
        this.varKey = varKey;
    }

    public List<FlowSequenceDTO> getStartSequenceList() {
        return startSequenceList;
    }

    public void setStartSequenceList(List<FlowSequenceDTO> startSequenceList) {
        this.startSequenceList = startSequenceList;
    }

    public Map<String, List<FlowTaskDTO>> getFlowTaskListMap() {
        return flowTaskListMap;
    }

    public void setFlowTaskListMap(Map<String, List<FlowTaskDTO>> flowTaskListMap) {
        this.flowTaskListMap = flowTaskListMap;
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
}
