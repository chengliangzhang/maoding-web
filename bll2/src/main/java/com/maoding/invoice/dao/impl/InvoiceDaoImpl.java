package com.maoding.invoice.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.invoice.dao.InvoiceDao;
import com.maoding.invoice.entity.InvoiceEntity;
import org.springframework.stereotype.Service;

/**
 * 发票数据实现层
 */
@Service("invoiceDao")
public class InvoiceDaoImpl extends GenericDao<InvoiceEntity> implements InvoiceDao {
}
