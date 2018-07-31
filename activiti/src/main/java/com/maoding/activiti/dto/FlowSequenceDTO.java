package com.maoding.activiti.dto;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/30
 * @description : 流程的连接线信息
 */
public class FlowSequenceDTO extends FlowElementDTO {
    /** id: 连接线编号 */

    /** 起始流程元素编号 */
    private String startFlowElementId;
    /** 终止流程元素编号 */
    private String endFlowElementId;
    /** 执行条件 */
    private String condition;

    public String getStartFlowElementId() {
        return startFlowElementId;
    }

    public void setStartFlowElementId(String startFlowElementId) {
        this.startFlowElementId = startFlowElementId;
    }

    public String getEndFlowElementId() {
        return endFlowElementId;
    }

    public void setEndFlowElementId(String endFlowElementId) {
        this.endFlowElementId = endFlowElementId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
