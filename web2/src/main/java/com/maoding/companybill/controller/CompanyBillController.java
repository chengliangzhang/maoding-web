package com.maoding.companybill.controller;

import com.maoding.companybill.dto.CompanyBalanceDTO;
import com.maoding.companybill.dto.QueryCompanyBalanceDTO;
import com.maoding.companybill.dto.SaveCompanyBalanceDTO;
import com.maoding.companybill.service.CompanyBalanceService;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("iWork/companyBill")
public class CompanyBillController extends BaseController {

    @Autowired
    private CompanyBalanceService companyBalanceService;


    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }

    /**
     * 方法描述：公司最低余额饿设置
     * 作   者：MaoSF
     * 日   期：2018/5/24 17:35
     */
    @RequestMapping(value = "/saveCompanyBalance", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveCompanyBalance(@RequestBody SaveCompanyBalanceDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        if(StringUtil.isNullOrEmpty(dto.getCompanyId())){
            dto.setCompanyId(this.currentCompanyId);
        }
        int i = companyBalanceService.saveCompanyBalance(dto);
        return i>0?AjaxMessage.succeed("保存成功"):AjaxMessage.error("操作失败");
    }

    /**
     * 方法描述：公司财务汇总
     * 作   者：MaoSF
     * 日   期：2018/5/24 17:35
     */
    @RequestMapping(value = "/getCompanyBalance", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getCompanyBalance(@RequestBody QueryCompanyBalanceDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        List<CompanyBalanceDTO> list = companyBalanceService.getCompanyBalance(dto);
        return AjaxMessage.succeed(list);
    }
}
