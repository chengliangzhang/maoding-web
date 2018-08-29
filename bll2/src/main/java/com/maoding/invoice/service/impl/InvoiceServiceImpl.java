package com.maoding.invoice.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.DigitUtils;
import com.maoding.core.util.StringUtil;
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
import com.maoding.project.dto.TitleColumnDTO;
import com.maoding.project.dto.TitleQueryDTO;
import com.maoding.project.service.ProjectConditionService;
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

    @Autowired
    private ProjectConditionService projectConditionService;


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

    @Override
    public String getInvoiceReceiveCompanyName(String invoiceId) {
        InvoiceInfoDTO invoiceInfo = this.getInvoice(invoiceId);
        if(invoiceInfo!=null){
            return invoiceInfo.getRelationCompanyName();
        }
        return null;
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
        updateQuery(query);

        return invoiceDao.listInvoice(query);
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
        updateQuery(query);

        List<InvoiceDTO> invoiceList = invoiceDao.listInvoice(query);
        int total = invoiceDao.getLastQueryCount();

        //建立分页返回信息
        CorePageDTO<InvoiceDTO> page = new CorePageDTO<>();
        page.setTotal(total);
        page.setPageSize(DigitUtils.parseInt(query.getPageSize()));
        page.setPageIndex(DigitUtils.parseInt(query.getPageIndex()));
        page.setData(invoiceList);
        return page;
    }

    //更新查询条件内要查询的属性
    private InvoiceQueryDTO updateQuery(InvoiceQueryDTO query){
        TitleQueryDTO titleQuery = BeanUtils.createFrom(query,TitleQueryDTO.class);
        titleQuery.setType(2);
        titleQuery.setWithList(0);
        List<TitleColumnDTO> titleList = projectConditionService.listTitle(titleQuery);
        return getNeedFillColumn(titleList,query);
    }

    //获取需要填充的动态内容
    private InvoiceQueryDTO getNeedFillColumn(List<TitleColumnDTO> titleList, InvoiceQueryDTO query){
        final int TITLE_TYPE_INVOICE_APPLY_DATE = 35;
        final int TITLE_TYPE_INVOICE_APPLY_USER = 36;
        final int TITLE_TYPE_INVOICE_FEE = 37;
        final int TITLE_TYPE_INVOICE_INVOICE_TYPE = 38;
        final int TITLE_TYPE_INVOICE_COST_TYPE = 39;
        final int TITLE_TYPE_INVOICE_RELATION_COMPANY = 40;
        final int TITLE_TYPE_INVOICE_TAX_ID = 41;
        final int TITLE_TYPE_INVOICE_FEE_DESCRIPTION = 42;
        final int TITLE_TYPE_INVOICE_PROJECT_NAME = 43;
        final int TITLE_TYPE_INVOICE_NO = 44;
        for (TitleColumnDTO title : titleList) {
            if (title.getTypeId() == SystemParameters.TITLE_TYPE_INVOICE_INVOICE_TYPE){
                query.setNeedInvoiceType(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_INVOICE_COST_TYPE){
                query.setNeedCostTypeName(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_INVOICE_RELATION_COMPANY){
                query.setNeedRelationCompanyName(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_INVOICE_TAX_ID){
                query.setNeedTaxIdNumber(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_INVOICE_FEE_DESCRIPTION){
                query.setNeedInvoiceContent(1);
            } else if (title.getTypeId() == SystemParameters.TITLE_TYPE_INVOICE_PROJECT_NAME) {
                query.setNeedProjectName(1);
            }
        }
        return query;
    }
}
