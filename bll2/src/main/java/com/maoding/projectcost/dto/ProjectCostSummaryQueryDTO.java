package com.maoding.projectcost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.CoreQueryDTO;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/10
 * 类名: com.maoding.projectcost.dto.ProjectCostSummaryQueryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectCostSummaryQueryDTO extends CoreQueryDTO {
    /** 起始日期 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startDate;

    /** 终止日期 */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
