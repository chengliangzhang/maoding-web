package com.maoding.noAuth.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.org.dto.BusinessPartnerDTO;
import com.maoding.org.entity.CompanyInviteEntity;
import com.maoding.org.service.CompanyRelationAuditService;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 事业合伙人
 * Created by Wuwq on 2017/4/1.
 */
@Controller
@RequestMapping("/home")
public class WebsiteController extends BaseController {

    /** products*/
    @RequestMapping("/products")
    public String products(){

        return "views/website/products";
    }
    /** faq*/
    @RequestMapping("/faq")
    public String faq(){

        return "views/website/faq";
    }

    /** deployment_guide*/
    @RequestMapping("/deployment_guide")
    public String deployment_guide(){

        return "views/website/deployment_guide";
    }

    /** pricing*/
    @RequestMapping("/pricing")
    public String pricing(){

        return "views/website/pricing";
    }

    /** security*/
    @RequestMapping("/security")
    public String security(){

        return "views/website/security";
    }
    /** terms*/
    @RequestMapping("/terms")
    public String terms(){

        return "views/website/terms";
    }

    /** solution_1*/
    @RequestMapping("/solution_1")
    public String solution_1(){

        return "views/website/solution_1";
    }
    /** solution_2*/
    @RequestMapping("/solution_2")
    public String solution_2(){

        return "views/website/solution_2";
    }
    /** solution_3*/
    @RequestMapping("/solution_3")
    public String solution_3(){

        return "views/website/solution_3";
    }
    /** solution_4*/
    @RequestMapping("/solution_4")
    public String solution_4(){

        return "views/website/solution_4";
    }
    /** solution_5*/
    @RequestMapping("/solution_5")
    public String solution_5(){

        return "views/website/solution_5";
    }
    /** solution_6*/
    @RequestMapping("/solution_6")
    public String solution_6(){

        return "views/website/solution_6";
    }




}
