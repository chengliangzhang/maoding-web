package com.maoding.collaboration.controller;

import com.maoding.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Wuwq on 2016/12/21.
 */
@Controller
@RequestMapping("iWork/collaboration")
public class CollaborationController extends BaseController {

    @RequestMapping("/detail")
    public String index(){
        return "views/collaboration/index";
    }
}
