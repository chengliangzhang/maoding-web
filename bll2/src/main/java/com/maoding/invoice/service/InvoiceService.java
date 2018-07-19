package com.maoding.invoice.service;

import com.maoding.invoice.dto.InvoiceEditDTO;

public interface InvoiceService {

    /**
     * 保存发票信息（项目收款填写发票信息）
     */
    String saveInvoice(InvoiceEditDTO dto);
}
