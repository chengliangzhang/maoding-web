package com.maoding.activiti.dto;


import com.maoding.core.base.dto.CoreDTO;
import com.maoding.core.base.dto.CoreEditDTO;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: DeplymentEditDTO
 * @description : 流程修改信息，用于增加流程、删除或关闭流程
 */
public class DeploymentEditDTO extends CoreEditDTO {
    /** id: 流程编号 */
    /** 流程名称 */
    private String name;
    /** 允许启动流程的角色(group)列表 */
    private List<CoreDTO> candidateGroupList;
    /** 允许启动流程的用户(user)列表 */
    private List<CoreDTO> candidateUserList;
    /** 模板流程编号 */
    private String srcDeployId;
    /** 流程启动时的数字式的审批条件 */
    private DigitConditionEditDTO startDigitCondition;
    /** 流程需更改的任务节点序列，以条件编号-任务序列对展现 */
    private Map<String,List<FlowTaskEditDTO>> flowTaskEditListMap;

    /** 仅后台使用属性 */
    /** 被编辑流程元素列表 */
    private List<FlowElementEditDTO> flowElementEditList;

    public DigitConditionEditDTO getStartDigitCondition() {
        return startDigitCondition;
    }

    public void setStartDigitCondition(DigitConditionEditDTO startDigitCondition) {
        this.startDigitCondition = startDigitCondition;
    }

    public Map<String, List<FlowTaskEditDTO>> getFlowTaskEditListMap() {
        return flowTaskEditListMap;
    }

    public void setFlowTaskEditListMap(Map<String, List<FlowTaskEditDTO>> flowTaskEditListMap) {
        this.flowTaskEditListMap = flowTaskEditListMap;
    }

    public List<FlowElementEditDTO> getFlowElementEditList() {
        return flowElementEditList;
    }

    public void setFlowElementEditList(List<FlowElementEditDTO> flowElementEditList) {
        this.flowElementEditList = flowElementEditList;
    }

    public String getSrcDeployId() {
        return srcDeployId;
    }

    public void setSrcDeployId(String srcDeployId) {
        this.srcDeployId = srcDeployId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
