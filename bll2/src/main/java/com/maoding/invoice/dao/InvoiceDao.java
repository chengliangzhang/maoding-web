package com.maoding.invoice.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.invoice.dto.InvoiceDTO;
import com.maoding.invoice.dto.InvoiceQueryDTO;
import com.maoding.invoice.entity.InvoiceEntity;

import java.util.List;

/**
 * 发票模块的数据层接口
 */
public interface InvoiceDao extends BaseDao<InvoiceEntity> {

    List<InvoiceDTO> listInvoice(InvoiceQueryDTO query);

    int getLastQueryCount();
}
