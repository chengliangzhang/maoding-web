package com.maoding.excelExport.controller;

import com.alibaba.fastjson.JSONObject;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.TxtFileUtil;
import com.maoding.excelExport.service.BaseExportService;
import com.maoding.project.dto.ProjectQueryDTO;
import com.maoding.statistic.dto.StatisticDetailQueryDTO;
import com.maoding.task.dto.ProjectTaskExportDTO;
import com.maoding.task.service.ProjectTaskExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("iWork/excel")
public class ExcelExportController extends BaseController {

    @Autowired
    @Qualifier("projectExportService")
    private BaseExportService projectExportService;

    @Autowired
    @Qualifier("balanceDetailExportService")
    private BaseExportService balanceDetailExportService;

    @Autowired
    private ProjectTaskExportService projectTaskExportService;


    @ModelAttribute
    public void before() throws Exception {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }

    /**
     * 方法描述：收支明细导出
     * 作   者： MaoSF
     * 日   期：2018/8/28
     */
    @RequestMapping(value = "/exportBalanceDetail", method = RequestMethod.POST)
    public void exportMyTaskList(HttpServletResponse response, HttpServletRequest request) throws Exception {
        StatisticDetailQueryDTO query = new StatisticDetailQueryDTO();
        BeanUtils.copyProperties(this.getParam(request),query);
        query.setCurrentCompanyId(this.currentCompanyId);
        query.setExcelFileName("台账明细.xlsx");
        //query.setTemplateFileName(new TxtFileUtil().getWebRoot() + "assets/template/statistic/balanceDetail.xlsx");
        balanceDetailExportService.exportDownloadResource(response, query);
    }

    /**
     * 方法描述：项目列表导出
     * 作   者： MaoSF
     * 日   期：2018/8/28
     */
    @RequestMapping(value = "/exportProjectList", method = RequestMethod.POST)
    public void exportProjectList(HttpServletResponse response, HttpServletRequest request) throws Exception {
        ProjectQueryDTO query = new ProjectQueryDTO();
        BeanUtils.copyProperties(this.getParam(request),query);
        query.setCurrentCompanyId(this.currentCompanyId);
        query.setAccountId(this.currentUserId);
        query.setCurrentCompanyUserId(this.currentCompanyUserId);
        query.setType(1);
        query.setExcelFileName("项目列表.xlsx");
        projectExportService.exportDownloadResource(response, query);
    }

    /**
     * 方法描述：收支明细导出
     * 作   者： ZhangChengliang
     * 日   期：2017/4/24
     * param  （projectId)
     */
    @RequestMapping(value = "/exportProductTaskList", method = RequestMethod.POST)
    public void exportProductTaskList(HttpServletResponse response, HttpServletRequest request) throws Exception {
        ProjectTaskExportDTO query = new ProjectTaskExportDTO();
        BeanUtils.copyProperties(this.getParam(request),query);
        query.setCurrentCompanyId(this.currentCompanyId);
        query.setType(0);
        if(StringUtil.isNullOrEmpty(query.getCompanyId())){
            query.setCompanyId(this.currentCompanyId);
        }
        query.setDestFileName("export_" + DateUtils.date2Str(DateUtils.yyyyMMdd) + ".xls");
        query.setTemplateFileName(new TxtFileUtil().getWebRoot() + "assets/template/task/template.xls");
        projectTaskExportService.exportDownloadResource(response, query);
    }

    public static JSONObject getParam(HttpServletRequest request){
        JSONObject JSONObject = new JSONObject();
        Map pmap = request.getParameterMap();
        //通过循环遍历的方式获得key和value并set到jsonobject中
        Iterator it = pmap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            String[] values = (String[])pmap.get(key);
            JSONObject.put(key, values[0]);
        }
        return JSONObject;
    }
}
