package com.maoding.invoice.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.invoice.dao.InvoiceDao;
import com.maoding.invoice.dto.InvoiceDTO;
import com.maoding.invoice.dto.InvoiceFilterQueryDTO;
import com.maoding.invoice.dto.InvoiceQueryDTO;
import com.maoding.invoice.entity.InvoiceEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发票数据实现层
 */
@Service("invoiceDao")
public class InvoiceDaoImpl extends GenericDao<InvoiceEntity> implements InvoiceDao {
    @Override
    public List<InvoiceDTO> listInvoice(InvoiceQueryDTO query) {
        return sqlSession.selectList("InvoiceEntityMapper.listInvoice",query);
    }

    /**
     * 描述       查询发票汇总选定的标题栏过滤器列表
     * 日期       2018/8/24
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<CoreShowDTO> getInvoiceFilterList(InvoiceFilterQueryDTO query) {
        return sqlSession.selectList("InvoiceEntityMapper.getInvoiceFilterList",query);
    }
}

