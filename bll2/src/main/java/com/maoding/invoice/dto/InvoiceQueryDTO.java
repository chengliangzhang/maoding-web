package com.maoding.invoice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.project.dto.DynamicQueryDTO;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/14
 * 类名: com.maoding.invoice.dto.InvoiceQueryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class InvoiceQueryDTO extends DynamicQueryDTO {
    /** id: 发票编号 */

    /** 查询的公司编号 **/
    private String companyId;

    /** 起始日期 **/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;

    /** 终止日期 **/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
