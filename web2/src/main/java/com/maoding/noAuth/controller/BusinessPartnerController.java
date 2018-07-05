package com.maoding.noAuth.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.BeanUtilsEx;
import com.maoding.org.dao.CompanyInviteDao;
import com.maoding.org.dto.BusinessPartnerDTO;
import com.maoding.org.dto.PartnerQueryDTO;
import com.maoding.org.dto.SubCompanyDTO;
import com.maoding.org.entity.CompanyInviteEntity;
import com.maoding.org.entity.PartnerEntity;
import com.maoding.org.service.CompanyRelationAuditService;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.OrganizationService;
import com.maoding.project.dto.ProjectPartnerDTO;
import com.maoding.task.dto.ProjectProcessTimeDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事业合伙人
 * Created by Wuwq on 2017/4/1.
 */
@Controller
@RequestMapping("/na/bPartner")
public class BusinessPartnerController extends BaseController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CompanyRelationAuditService companyRelationAuditService;

    @RequestMapping("/invited/{id}")
    public String invitePageForRecv(ModelMap model,@PathVariable String id) throws Exception{
        CompanyInviteEntity companyInviteEntity = this.companyService.getCompanyInviteById(id);
        if(companyInviteEntity==null){
            return "views/noAuth/businessPartner/invited_no";
        }
        model.addAttribute("invitedId",id);
        model.addAttribute("invitedType",companyInviteEntity.getType());
        return "views/noAuth/businessPartner/invited";
    }

    /**
     * 方法描述：打开发送链接请求数据
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:
     * @return:
     */
    @RequestMapping(value = "/getCompanyByInviteUrl/{id}",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getCompanyByInviteUrl(@PathVariable String id) throws Exception{

        return companyService.getCompanyByInviteUrl(id);
    }

    /**
     * 方法描述：验证身份
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:map(id,cellphone)
     * @return:
     */
    @RequestMapping(value = "/verifyIdentityForParent",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage verifyIdentityForParent(@RequestBody  Map<String, Object> map) throws Exception{
        return companyService.verifyIdentityForParent(map);
    }


    /**
     * 方法描述：验证身份成功后，请求数据
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:map(cellphone,id)
     * @return:
     */
    @RequestMapping(value ="/getCompanyPrincipal",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getCompanyPrincipal(@RequestBody  Map<String, Object> map) throws Exception{
        return AjaxMessage.succeed(companyService.getCompanyPrincipal(map));
    }


    /**
     * 方法描述：创建公司并加入事业合伙人或分支机构
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:map(inviteId:申请记录的id，userName,cellphone,adminPassword,companyName)
     * @return:
     */
    @RequestMapping(value ="/createBusinessPartner",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage createBusinessPartner(@RequestBody BusinessPartnerDTO dto) throws Exception{
        return this.companyService.createBusinessPartner(dto);
    }

    /**
     * 方法描述：选择公司加入事业合伙人或分支机构
     * 作者：MaoSF
     * 日期：2017/4/1
     * @param:map(inviteId:申请记录的id，cellphone,companyId:选择的公司)
     * @return:
     */
    @RequestMapping(value ="/applayBusinessPartner",method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage applayBusinessPartner(@RequestBody BusinessPartnerDTO dto) throws Exception{
        return companyRelationAuditService.applyBusinessPartner(dto);
    }

}
