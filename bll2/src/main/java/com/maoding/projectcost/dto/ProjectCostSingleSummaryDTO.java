package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.CoreDTO;

import java.math.BigDecimal;

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

    /** 到账金额 */
    private BigDecimal real;

    public BigDecimal getPlan() {
        return plan;
    }

    public void setPlan(BigDecimal plan) {
        this.plan = plan;
    }

    public BigDecimal getReal() {
        return real;
    }

    public void setReal(BigDecimal real) {
        this.real = real;
    }
}
