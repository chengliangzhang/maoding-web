package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreEditDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/3
 * 类名: com.maoding.activiti.dto.FlowTaskGroupDTO
 * 作者: 张成亮
 * 描述:
 **/
public class FlowTaskGroupEditDTO extends CoreEditDTO {

    /** 用户任务组的名称 */
    private String name;

    /** 此组包含的用户任务序列 */
    private List<FlowTaskEditDTO> flowTaskList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FlowTaskEditDTO> getFlowTaskList() {
        return flowTaskList;
    }

    public void setFlowTaskList(List<FlowTaskEditDTO> flowTaskList) {
        this.flowTaskList = flowTaskList;
    }

    public FlowTaskGroupEditDTO(){}
    public FlowTaskGroupEditDTO(List<FlowTaskEditDTO> flowTaskList){
        setFlowTaskList(flowTaskList);
    }
}
