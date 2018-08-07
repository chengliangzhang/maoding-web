package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/3
 * 类名: com.maoding.activiti.dto.FlowTaskGroupDTO
 * 作者: 张成亮
 * 描述:
 **/
public class FlowTaskGroupDTO extends CoreShowDTO {
    /** 条件最小值 */
    private Double minValue;

    /** 条件最大值 */
    private Double maxValue;

    /** 显示条件 */
    private String title;

    /** 此组包含的用户任务序列 */
    private List<FlowTaskDTO> flowTaskList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public List<FlowTaskDTO> getFlowTaskList() {
        return flowTaskList;
    }

    public void setFlowTaskList(List<FlowTaskDTO> flowTaskList) {
        this.flowTaskList = flowTaskList;
    }

    public FlowTaskGroupDTO(){}
    public FlowTaskGroupDTO(String name,List<FlowTaskDTO> flowTaskList){
        setName(name);
        setFlowTaskList(flowTaskList);
    }
}
