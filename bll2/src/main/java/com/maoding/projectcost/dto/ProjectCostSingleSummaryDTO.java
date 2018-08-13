package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.CoreDTO;
import com.maoding.core.util.DigitUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/10
 * 类名: com.maoding.projectcost.dto.ProjectCostSingleSummaryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectCostSingleSummaryDTO extends CoreDTO {
    /** id: 项目编号 */

    /** 计划金额 */
    private BigDecimal plan;

    /** 实际发生（到账或付款）金额 */
    private BigDecimal real;

    public BigDecimal getPlan() {
        return (plan != null) ? plan : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setPlan(BigDecimal plan) {
        this.plan = DigitUtils.toBigDecimal(plan,6);
    }

    public BigDecimal getReal() {
        return (real != null) ? real : new BigDecimal(0).setScale(6, RoundingMode.HALF_UP);
    }

    public void setReal(BigDecimal real) {
        this.real = DigitUtils.toBigDecimal(real,6);
    }
}
