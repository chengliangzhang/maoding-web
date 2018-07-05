package com.maoding.projectDynamic.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.BeanUtilsEx;
import com.maoding.dynamic.dto.ProjectDynamicDTO;
import com.maoding.dynamic.dto.QueryDynamicDTO;
import com.maoding.dynamic.service.DynamicService;
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
 * Created by Wuwq on 2017/3/4.
 */
@Controller
@RequestMapping("iWork/project")
public class ProjectDynamicController extends BaseController {
    @Autowired
    private SystemService systemService;

    @Autowired
    private DynamicService dynamicService;

    @RequestMapping(value = "/dynamicList", method = RequestMethod.GET)
    public String index(ModelMap model) throws Exception{
        systemService.getCurrUserInfoOfWork(model,this.getSession());
        return "views/projectDynamic/dynamicList";
    }


    @RequestMapping(value = "/getProjectDynamicList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectDynamicList(@RequestBody Map<String,Object> paraMap){
        QueryDynamicDTO query = new QueryDynamicDTO();
        BeanUtilsEx.copyProperties(paraMap,query);
        ProjectDynamicDTO result = dynamicService.getProjectDynamic(query);
        AjaxMessage ajax = AjaxMessage.succeed(result);
        //projectDynamicsService.getProjectDynamicByPage(paraMap)
        return  ajax;
    }
}
