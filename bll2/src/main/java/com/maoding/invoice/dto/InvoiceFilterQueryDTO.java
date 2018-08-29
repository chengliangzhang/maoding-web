package com.maoding.invoice.dto;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/29
 * 类名: com.maoding.invoice.dto.InvoiceFilterQueryDTO
 * 作者: 张成亮
 * 描述: 
 **/
public class InvoiceFilterQueryDTO extends InvoiceQueryDTO {
    /** 标题栏关键字 **/
    private String titleCode;

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }
}
