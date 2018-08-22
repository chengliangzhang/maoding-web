package com.maoding.invoice.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.ProjectCostConst;
import com.maoding.core.util.*;
import com.maoding.enterprise.service.EnterpriseService;
import com.maoding.invoice.dao.InvoiceDao;
import com.maoding.invoice.dto.InvoiceDTO;
import com.maoding.invoice.dto.InvoiceEditDTO;
import com.maoding.invoice.dto.InvoiceInfoDTO;
import com.maoding.invoice.dto.InvoiceQueryDTO;
import com.maoding.invoice.entity.InvoiceEntity;
import com.maoding.invoice.service.InvoiceService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.entity.CompanyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("invoiceService")
public class InvoiceServiceImpl extends NewBaseService implements InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private EnterpriseService enterpriseService;

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

    @Override
    public InvoiceInfoDTO getInvoice(String invoiceId) {
        InvoiceInfoDTO invoiceInfo = new InvoiceInfoDTO();
        InvoiceEntity invoice = invoiceDao.selectById(invoiceId);
        BeanUtils.copyProperties(invoice,invoiceInfo);
        invoiceInfo.setRelationCompanyName(this.getRelationCompanyName(invoice.getRelationCompanyId(),invoice.getRelationCompanyName()));
        return invoiceInfo;
    }

    /**
     * 获取立项方组织的名称
     */
    private String getRelationCompanyName(String relationCompanyId,String relationCompanyName){
        if(StringUtil.isNullOrEmpty(relationCompanyId)){
            return relationCompanyName;
        }else {
            CompanyEntity companyEntity = null;
            companyEntity = this.companyDao.selectById(relationCompanyId);
            if (companyEntity != null) {
                return  companyEntity.getAliasName();
            }else {
                //从enterprise中查询
                String name = enterpriseService.getEnterpriseName(relationCompanyId);
                if(name!=null){
                    return name;
                }
            }
            return relationCompanyId;
        }
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
        return updateInvoiceList(list);
    }

    //填充公司名称
    private List<InvoiceDTO> updateInvoiceList(List<InvoiceDTO> list){
        if (ObjectUtils.isNotEmpty(list)){
            list.forEach(invoice->{
                invoice.setCostTypeName(getCostTypeName(invoice.getCostType()));
                invoice.setInvoiceTypeName(getInvoiceTypeName(invoice.getInvoiceType()));
                if (StringUtils.isEmpty(invoice.getRelationCompanyName()) && StringUtils.isNotEmpty(invoice.getRelationCompanyId())){
                    String companyName = getRelationCompanyName(invoice.getRelationCompanyId(),invoice.getRelationCompanyName());
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
        List<InvoiceDTO> invoiceList = invoiceDao.listInvoice(query);
        int total = invoiceDao.getLastQueryCount();

        //建立分页返回信息
        CorePageDTO<InvoiceDTO> page = new CorePageDTO<>();
        page.setTotal(total);
        page.setPageSize(DigitUtils.parseInt(query.getPageSize()));
        page.setPageIndex(DigitUtils.parseInt(query.getPageIndex()));
        page.setData(updateInvoiceList(invoiceList));
        return page;
    }
}
