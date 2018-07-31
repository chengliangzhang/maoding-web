package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreEditDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/31
 * @description : 为某个流程设置数字式的审批条件的申请
 */
public class DigitConditionEditDTO extends CoreEditDTO {
    /** id：流程编号 */

    /** 判断条件变量名 */
    private String varKey;
    /** 判断条件显示名 */
    private String varName;
    /** 判断条件取值序列，如果是金额，以分为单位 */
    private List<Long> pointList;

    public String getVarKey() {
        return varKey;
    }

    public void setVarKey(String varKey) {
        this.varKey = varKey;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public List<Long> getPointList() {
        return pointList;
    }

    public void setPointList(List<Long> pointList) {
        this.pointList = pointList;
    }
}
