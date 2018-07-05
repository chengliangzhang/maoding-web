package com.maoding.dynamic.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.dynamic.service.OrgDynamicService;
import com.maoding.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Wuwq on 2017/2/22.
 */
@Controller
@RequestMapping("iWork/dynamic")
public class DynamicController extends BaseController {
    @Autowired
    private SystemService systemService;

    @Autowired
    private OrgDynamicService orgDynamicService;

    @RequestMapping(value = "/dynamicList", method = RequestMethod.GET)
    public String index(ModelMap model) throws Exception{
        systemService.getCurrUserInfoOfWork(model,this.getSession());
        return "views/dynamic/dynamicList";
    }

    @RequestMapping("/getCompanyDynamics")
    @ResponseBody
    public AjaxMessage getCompanyDynamics(@RequestBody Map<String,Object> map) throws Exception{
        return this.ajaxResponseSuccess().setData(orgDynamicService.getOrgDynamicListByCompanyId(map));
    }
}
