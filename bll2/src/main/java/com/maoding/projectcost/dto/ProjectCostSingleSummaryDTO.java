package com.maoding.projectcost.dto;

import com.maoding.core.base.dto.CoreDTO;

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
    private double plan;

    /** 实际发生（到账或付款）金额 */
    private double real;

    public double getPlan() {
        return plan;
    }

    public void setPlan(double plan) {
        this.plan = plan;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }
}
