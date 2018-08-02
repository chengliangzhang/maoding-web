package com.maoding.activiti.dto;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/30
 * @description : 流程的连接线信息
 */
@Deprecated
public class FlowSequenceDTO extends FlowElementDTO {
    /** id: 连接线编号 */

    /** 起始流程元素编号 */
    private String sourceRef;
    /** 终止流程元素编号 */
    private String targetRef;
    /** 执行条件 */
    private String conditionExpression;

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }
}
