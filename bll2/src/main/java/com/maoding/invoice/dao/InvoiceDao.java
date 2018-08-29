package com.maoding.invoice.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.invoice.dto.InvoiceDTO;
import com.maoding.invoice.dto.InvoiceFilterQueryDTO;
import com.maoding.invoice.dto.InvoiceQueryDTO;
import com.maoding.invoice.entity.InvoiceEntity;

import java.util.List;

/**
 * 发票模块的数据层接口
 */
public interface InvoiceDao extends BaseDao<InvoiceEntity> {

    List<InvoiceDTO> listInvoice(InvoiceQueryDTO query);

    /**
     * 描述       查询发票汇总选定的标题栏过滤器列表
     * 日期       2018/8/24
     * @author   张成亮
     **/
    List<CoreShowDTO> getInvoiceFilterList(InvoiceFilterQueryDTO query);

    int getLastQueryCount();
}
