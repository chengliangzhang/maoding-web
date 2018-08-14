package com.maoding.invoice.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.base.dto.CorePageDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.ObjectUtils;
import com.maoding.invoice.dto.InvoiceDTO;
import com.maoding.invoice.dto.InvoiceQueryDTO;
import com.maoding.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Idccapp22 on 2017/3/17.
 */
@Controller
@RequestMapping("/iWork/invoice")
public class InvoiceController extends BaseController {

    @Autowired
    private InvoiceService invoiceService;

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }

    @RequestMapping(value = {"/listInvoice"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage listInvoice(@RequestBody InvoiceQueryDTO query) throws Exception {
        updateCurrentUserInfo(query);
        if (ObjectUtils.isEmpty(query.getPageSize())){
            List<InvoiceDTO> list = invoiceService.listInvoice(query);
            return AjaxMessage.succeed(list);
        } else {
            CorePageDTO<InvoiceDTO> page = invoiceService.listPageInvoice(query);
            return AjaxMessage.succeed(page);
        }
    }
}
