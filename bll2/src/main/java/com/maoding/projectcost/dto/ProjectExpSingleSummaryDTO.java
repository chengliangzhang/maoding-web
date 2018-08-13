package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.CoreDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/13
 * 类名: com.maoding.projectcost.dto.ProjectExpSingleSummaryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectExpSingleSummaryDTO extends CoreDTO {
    /** id: 项目编号 */

    /** 报销金额 */
    private double expense;

    /** 费用金额 */
    private double cost;

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
