package com.maoding.invoice.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.ProjectCostConst;
import com.maoding.core.util.DigitUtils;
import com.maoding.core.util.ObjectUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.StringUtils;
import com.maoding.invoice.dao.InvoiceDao;
import com.maoding.invoice.dto.InvoiceDTO;
import com.maoding.invoice.dto.InvoiceEditDTO;
import com.maoding.invoice.dto.InvoiceQueryDTO;
import com.maoding.invoice.entity.InvoiceEntity;
import com.maoding.invoice.service.InvoiceService;
import com.maoding.org.dao.CompanyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("invoiceService")
public class InvoiceServiceImpl extends NewBaseService implements InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private CompanyDao companyDao;

    @Override
    public String saveInvoice(InvoiceEditDTO dto) {
        InvoiceEntity invoice = (InvoiceEntity) BaseDTO.copyFields(dto, InvoiceEntity.class);
        if (StringUtil.isNullOrEmpty(dto.getId())) {
            invoice.initEntity();
            invoice.setDeleted(0);//设置为有效
            invoiceDao.insert(invoice);
        } else {
            invoiceDao.updateById(invoice);
        }
        return invoice.getId();
    }

    /**
     * 描述     查询发票列表
     * 日期     2018/8/14
     *
     * @param query 查询条件
     * @return 发票列表
     * @author 张成亮
     **/
    @Override
    public List<InvoiceDTO> listInvoice(InvoiceQueryDTO query) {
        List<InvoiceDTO> list = invoiceDao.listInvoice(query);
        if (ObjectUtils.isNotEmpty(list)){
            list.forEach(invoice->{
                invoice.setCostTypeName(getCostTypeName(invoice.getCostType()));
                invoice.setInvoiceTypeName(getInvoiceTypeName(invoice.getInvoiceType()));
                if (StringUtils.isEmpty(invoice.getRelationCompanyName()) && StringUtils.isNotEmpty(invoice.getRelationCompanyId())){
                    String companyName = companyDao.getCompanyName(invoice.getRelationCompanyId());
                    invoice.setRelationCompanyName(companyName);
                }
            });
        }
        return list;
    }

    //获取收支分类子项名称
    private String getCostTypeName(int costType){
        return ProjectCostConst.COST_TYPE_MAP.get(costType);
    }

    //获取收支分类子项名称
    private String getInvoiceTypeName(int invoiceType){
        return (invoiceType == 1) ? "普票" : "专票";
    }

    /**
     * 描述     查询发票列表
     * 日期     2018/8/14
     *
     * @param query
     * @return
     * @author 张成亮
     **/
    @Override
    public CorePageDTO<InvoiceDTO> listPageInvoice(InvoiceQueryDTO query) {
        List<InvoiceDTO> invoiceList = listInvoice(query);
//        int total = invoiceDao.getLastQueryCount();

        //建立分页返回信息
        CorePageDTO<InvoiceDTO> page = new CorePageDTO<>();
//        page.setTotal(total);
        page.setPageSize(DigitUtils.parseInt(query.getPageSize()));
        page.setPageIndex(DigitUtils.parseInt(query.getPageIndex()));
        page.setData(invoiceList);
        return page;
    }
}
