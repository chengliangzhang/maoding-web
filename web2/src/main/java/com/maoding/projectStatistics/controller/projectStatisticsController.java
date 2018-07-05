package com.maoding.projectStatistics.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by idccapp12 on 2016/7/19.
 */
@Controller
@RequestMapping("iWork/projectStatistics")
public class projectStatisticsController  extends BaseController {
    @Autowired
    private SystemService systemService;
    //  项目统计
    @RequestMapping("/projectStatistics")
    public String projectStatistics(ModelMap model)throws Exception{
        systemService.getCurrUserInfoOfWork(model,this.getSession());
        return "work/views/projectStatistics/projectStatistics";
    }

    @ModelAttribute
    public void before(){
        this.currentUserId = this.getFromSession("userId",String.class);
        this.currentCompanyId =this.getFromSession("companyId",String.class);
    }
}

