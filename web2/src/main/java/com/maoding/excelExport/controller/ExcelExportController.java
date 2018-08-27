package com.maoding.excelExport.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.util.TxtFileUtil;
import com.maoding.excelExport.service.BalanceDetailExportService;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("iWork/excel")
public class ExcelExportController extends BaseController {

    @Autowired
    private BalanceDetailExportService balanceDetailExportService;

    @ModelAttribute
    public void before() throws Exception {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }
    /**
     * 方法描述：收支明细导出
     * 作   者： ZhangChengliang
     * 日   期：2017/4/24
     * param  （projectId)
     */
    @RequestMapping(value = "/exportBalanceDetail", method = RequestMethod.POST)
    public void exportMyTaskList(HttpServletResponse response,@RequestBody StatisticDetailQueryDTO query) throws Exception {
       // dto.setDestFileName("export_" + DateUtils.date2Str(DateUtils.yyyyMMdd) + ".xls");
        query.setTemplateFileName(new TxtFileUtil().getWebRoot() + "assets/template/statistic/balanceDetail.xlsx");
        balanceDetailExportService.exportDownloadResource(response, query);
    }

}
