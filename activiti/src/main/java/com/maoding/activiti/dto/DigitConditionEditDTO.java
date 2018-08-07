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

    /** 判断条件取值序列，如果是金额，以分为单位 */
    private List<Double> pointList;

    public List<Double> getPointList() {
        return pointList;
    }

    public void setPointList(List<Double> pointList) {
        this.pointList = pointList;
    }
}
