package com.maoding.projectconstruct.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtil;
import com.maoding.project.dto.ProjectConstructDTO;
import com.maoding.project.service.ProjectConstructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ProjectConstructController
 * 描    述 : 建设单位Controller
 * 作    者 : LY
 * 日    期 : 2016/7/22-9:52
 */
@Controller
@RequestMapping("iWork/projectConstruct")
public class ProjectConstructController extends BaseController{

    @Autowired
    private ProjectConstructService projectConstructService;

    @ModelAttribute
    public void before(){
        this.currentUserId = this.getFromSession("userId",String.class);
        this.currentCompanyId =this.getFromSession("companyId",String.class);
    }

    /**
     * 方法描述：根据当前组织查找客户管理列表
     * 作   者：LY
     * 日   期：2016/7/22-9:52
     * @param
     */
    @RequestMapping("/getConstructByCompanyId")
    @ResponseBody
    public List<ProjectConstructDTO> getConstructByCompanyId() throws Exception{
        String companyId =  this.currentCompanyId;
        List<ProjectConstructDTO> list = projectConstructService.getConstructByCompanyId(companyId);
        return list;
    }

    /**
     * 方法描述：保存或者添加建设单位联系人
     * 作   者：LY
     * 日   期：2016/7/22 11:57
     * @param  dto
     * @return
     *
    */
    @RequestMapping(value ={"/projectConstruct","/projectConstruct/{id}"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateProjectConstruct(@RequestBody ProjectConstructDTO dto) throws Exception {
        dto.setCompanyId( this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        AjaxMessage ajaxMessage = projectConstructService.saveOrUpdateProjectConstruct(dto);
        if("0".equals(ajaxMessage.getCode()))
        {
            ProjectConstructDTO dto1 = (ProjectConstructDTO)ajaxMessage.getData();
            Map<String,ProjectConstructDTO> projectConstructMap = null;
            if(null == this.getSession().getAttribute("projectConstruct")){
               projectConstructMap = new HashMap<String,ProjectConstructDTO>();
            }else {
                projectConstructMap = (Map)this.getSession().getAttribute("projectConstruct");
            }
            projectConstructMap.put(dto1.getId(),dto1);
            this.getSession().setAttribute("projectConstruct",projectConstructMap);

        }

        return ajaxMessage;
    }

    /**
     * 方法描述：获取建设单位及联系人
     * 作   者：LY
     * 日   期：2016/7/22 11:57
     * @param  id
     * @return
     *
     */
    @RequestMapping(value ="/projectConstruct/{id}" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getProjectConstruct(@PathVariable("id") String id) throws Exception {
        ProjectConstructDTO dto = projectConstructService.getProjectConstructById(id);
        return this.ajaxResponseSuccess().setData(dto);
    }

    /**
     * 方法描述：项目详情（项目立项）界面，查看联系人获取数据
     * 作   者：MaoSF
     * 日   期：2016/7/22 11:57
     * @param  map
     * @return
     *
     */
    @RequestMapping(value ="/projectConstructOther" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectConstructByIdAndOtherDetail(@RequestBody Map<String,Object> map) throws Exception {
        String id = (String) map.get("id");
        String projectId = (String) map.get("projectId");
        ProjectConstructDTO dto = null;
        //看session中是否有当前id的对象Str//看session中是否有当前
        if(!StringUtil.isNullOrEmpty(id)){
            if(this.getSession().getAttribute("projectConstruct")!=null) {
                Map<String, ProjectConstructDTO> projectConstructMap = (Map) this.getSession().getAttribute("projectConstruct");
                dto = projectConstructMap.get(id);
            }
        }
        //如果没有，从数据空中获取
        if(dto == null){
            dto = projectConstructService.getProjectConstructByIdAndOtherDetail(id,this.currentCompanyId,projectId);
        }
        return this.ajaxResponseSuccess().setData(dto);
    }

}
