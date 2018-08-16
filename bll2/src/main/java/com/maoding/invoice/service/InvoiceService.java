package com.maoding.invoice.service;

import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.invoice.dto.InvoiceDTO;
import com.maoding.invoice.dto.InvoiceEditDTO;
import com.maoding.invoice.dto.InvoiceInfoDTO;
import com.maoding.invoice.dto.InvoiceQueryDTO;

import java.util.List;

public interface InvoiceService {

    /**
     * 保存发票信息（项目收款填写发票信息）
     */
    String saveInvoice(InvoiceEditDTO dto);

    /**
     * 获取发票信息
     */
    InvoiceInfoDTO getInvoice(String invoiceId);

    /**
     * 描述     查询发票列表
     * 日期     2018/8/14
     * @author  张成亮
     * @return  发票列表
     * @param   query 查询条件
     **/
    List<InvoiceDTO> listInvoice(InvoiceQueryDTO query);

    /**
     * 描述     查询发票列表
     * 日期     2018/8/14
     * @author  张成亮
     * @return
     * @param
     **/
    CorePageDTO<InvoiceDTO> listPageInvoice(InvoiceQueryDTO query);
}
