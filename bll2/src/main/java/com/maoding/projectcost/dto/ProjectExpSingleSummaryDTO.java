package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.CoreDTO;
import com.maoding.core.util.DigitUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
    private BigDecimal expense;

    /** 费用金额 */
    private BigDecimal cost;

    public BigDecimal getExpense() {
        return (expense != null) ? expense : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setExpense(BigDecimal expense) {
        this.expense = DigitUtils.toBigDecimal(expense,6);
    }

    public BigDecimal getCost() {
        return (cost != null) ? cost : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setCost(BigDecimal cost) {
        this.cost = DigitUtils.toBigDecimal(cost,6);
    }
}
