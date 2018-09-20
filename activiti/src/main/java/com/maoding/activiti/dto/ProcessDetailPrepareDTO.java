package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreEditDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/8/2
 * @description :
 */
public class ProcessDetailPrepareDTO extends CoreEditDTO {
    /** id:流程key */

    /** 流程名称 */
    private String name;

    /** 流程键值，定义见ProcessTypeConst.PROCESS_TYPE_xxx */
    private String key;

    /** 流程类型，定义见ProcessTypeConst.TYPE_xxx */
    private Integer type;

    /** 分条件流程内的变量名称 **/
    private String varName;

    /** 分条件流程内的变量单位 **/
    private String varUnit;
	
    /** 条件控件编号 **/
    private String conditionFieldId;

    /** 模板流程编号 */
    private String srcProcessDefineId;

    /** 流程启动时的数字式的审批条件 */
    private DigitConditionEditDTO startDigitCondition;

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarUnit() {
        return varUnit;
    }

    public void setVarUnit(String varUnit) {
        this.varUnit = varUnit;
    }

    public String getConditionFieldId() {
        return conditionFieldId;
    }

    public void setConditionFieldId(String conditionFieldId) {
        this.conditionFieldId = conditionFieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getSrcProcessDefineId() {
        return srcProcessDefineId;
    }

    public void setSrcProcessDefineId(String srcProcessDefineId) {
        this.srcProcessDefineId = srcProcessDefineId;
    }

    public DigitConditionEditDTO getStartDigitCondition() {
        return startDigitCondition;
    }

    public void setStartDigitCondition(DigitConditionEditDTO startDigitCondition) {
        this.startDigitCondition = startDigitCondition;
    }
}
