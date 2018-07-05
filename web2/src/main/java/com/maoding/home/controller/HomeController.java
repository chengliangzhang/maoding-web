package com.maoding.home.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.SystemParameters;
import com.maoding.org.dto.CompanyDTO;
import com.maoding.org.service.CompanyService;
import com.maoding.system.dto.DataDictionaryDTO;
import com.maoding.system.service.DataDictionaryService;
import com.maoding.system.service.SystemService;
import com.maoding.user.dto.AccountDTO;
import com.maoding.user.dto.RegisterCompanyDTO;
import com.maoding.user.service.AccountService;
import org.apache.shiro.authc.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：HomeController
 * 类描述：个人首页controller
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午11:50:16
 */
@Controller
@RequestMapping("/iWork/home")
public class HomeController extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 方法描述：个人首页
     * 作者：MaoSF
     * 日期：2016/8/12
     *
     * @param:[model]
     * @return:java.lang.String
     */
    @RequestMapping("/workbench")
    public String toHomeIndex(ModelMap model) throws Exception {
        //systemService.getCurrUserInfoOfWork(model,this.getSession());
        systemService.getCurrUserInfoOfAdmin2(model, this.getSession());
        List<DataDictionaryDTO> projectTypeList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_Type);//项目类别
        model.put("projectTypeList", projectTypeList);

        List<CompanyDTO> list = (List) model.get("orgList");
        if (list == null || list.size() == 0) {
            model.put("isFirst", "1");
            model.put("showPre", "1");
            return "views/home/createOrg";
        } else {
            return "views/home/workbench";
        }
    }
    @RequestMapping("/workbench/{forwordType}")
    public String toHomeIndex1(RedirectAttributes model,ModelMap model1, @PathVariable String forwordType) throws Exception {

        if(forwordType.equals("1")){
            model.addFlashAttribute("isLoginComeIn","1");
            return "redirect:/iWork/home/workbench";
        }else{
            model1.put("forwardType", forwordType);
            return "views/home/workbench";
        }

    }
    @RequestMapping("/workbench/{forwordType}/{secondForwardType}")
    public String toHomeIndex2(ModelMap model, @PathVariable String forwordType, @PathVariable String secondForwardType) throws Exception {

        model.put("isLoginComeIn","1");


        model.put("forwardType", forwordType);
        model.put("secondForwardType", secondForwardType);

        return "views/home/workbench";
    }

    /**
     * 方法描述：个人首页
     * 作者：MaoSF
     * 日期：2016/8/12
     *
     * @param:[model]
     * @return:java.lang.String
     */
    @RequestMapping("/workBusHome")
    public String workBusHome(ModelMap model) throws Exception {
        //systemService.getCurrUserInfoOfWork(model,this.getSession());
        systemService.getCurrUserInfoOfAdmin2(model, this.getSession());
        List<DataDictionaryDTO> projectTypeList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_Type);//项目类别
        model.put("projectTypeList", projectTypeList);
        return "work/views/home/workBusHome";
    }

    @RequestMapping("/createOrg")
    public String workCreateOrg(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfAdmin2(model, this.getSession());
        List<CompanyDTO> list = (List) model.get("orgList");
        if (list == null || list.size() == 0) {
            model.put("isDialog", "1");
            return "views/home/createOrg";
        }
        return "views/home/createOrg";
    }

    @RequestMapping("/createOrg2")
    public String workCreateOrg2(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfAdmin2(model, this.getSession());
        List<CompanyDTO> list = (List) model.get("orgList");
        if (list == null || list.size() == 0) {
            model.put("isFirst", "1");//当存在此值则弹窗
            return "views/home/createOrg";
        }
        return "views/home/createOrg";
    }

    @RequestMapping("/workNewcomersInstruction")
    public String workNewcomersInstruction(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        return "work/views/hlep/workNewcomersInstruction";
    }


    @RequestMapping(value = "/toHelp/{id}", method = RequestMethod.GET)
    public String toHelp(ModelMap model, @PathVariable("id") String id) throws Exception {
        model.put("menuId", id);
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        return "work/views/help/workNewcomersInstruction";
    }

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
    }

    /***************组织切换***********/

	 /* 方法描述：公司切换
     * 个人中心 - 右上角--组织列表--切换
	 * 作        者：MaoSF
	 * 日        期：2015年12月15日-下午3:25:23
	 * @param paraMap
	 * @return
	 */
    @RequestMapping(value = "/switchCompany/{companyId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage switchCompany(HttpServletRequest request, HttpServletResponse response, @PathVariable("companyId") String companyId) throws Exception {
        String userId = this.currentUserId;
        AjaxMessage ajaxMessage = systemService.switchCompany(request, response, companyId, userId);
        ajaxMessage.getExtendData().put("switchFlag","1");//标识当前接口是切换组织的接口。前端需要
        return ajaxMessage;
    }

    public void setSession(Map<String, Object> m) {
        this.getSession().setAttribute("user", m.get("user"));
        this.getSession().setAttribute("userId", m.get("userId"));
        //当前组织， 切换组织的时候，companyId需要更换
        this.getSession().setAttribute("companyId", m.get("companyId"));
        this.getSession().setAttribute("company", m.get("company"));
        this.getSession().setAttribute("adminCompanyId", m.get("companyId"));
        this.getSession().setAttribute("adminCompany", m.get("company"));
        this.getSession().setAttribute("roleCompany", m.get("roleCompany"));
        this.getSession().setAttribute("role", m.get("role"));
        this.getSession().setAttribute("orgType", "company");
    }

    /**
     * 方法描述：设置session
     * 作        者：MaoSF
     * 日        期：2016年7月9日-下午5:29:12
     *
     * @param m
     */
    public void setSession(HttpServletRequest request, HttpServletResponse response, Map<String, Object> m) {
        setSession(m);
        System.out.println("-----------response----------:" + response);
        if (response == null) {
            return;
        }
        try {
            Cookie cookie = this.getCookieByName(request, m.get("userId").toString());
            if (cookie == null) {
                cookie = new Cookie(m.get("userId").toString(), m.get("companyId").toString());
            }
            cookie.setMaxAge(2592000);
            cookie.setPath("/");
            cookie.setValue(m.get("companyId").toString());
            response.addCookie(cookie);
        } catch (Exception e) {
            System.out.println("-----------Exception catch response----------:" + response);
            return;
        }
    }


    public Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     *
     * @return
     */
    private Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }


    /**
     * 方法描述：个人首页（创建团队）
     * 作        者：MaoSF
     * 日        期：2016年7月7日-下午6:25:22
     *
     * @param dto
     *
     * @return
     *
     * @throws Exception
     */
    @RequestMapping(value = "/registerCompany", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage registerCompany(HttpServletRequest request, HttpServletResponse response, @RequestBody RegisterCompanyDTO dto) throws Exception {
        AjaxMessage ajax = accountService.registerCompanyOfWork(dto, this.currentUserId);
        if (ajax.getCode().equals("0")) {
            String companyId = ((AccountDTO) ajax.getData()).getDefaultCompanyId();

            //切换团队
            String userId = this.currentUserId;
            AjaxMessage ajaxMessage = systemService.switchCompany(request, response, companyId, userId);
            if ("1".equals(ajaxMessage.getCode())) {
                throw new AccountException("切换团队失败");
            }
            return ajaxResponseSuccess("成功建立“"+dto.getCompanyName()+"”");
        }
        return ajax;
    }

    //=======================admin==============================================
    @RequestMapping("/adminBusHome")
    public String adminBusHome(ModelMap model) throws Exception {
        //获取当前用户基本信息
        systemService.getCurrUserInfoOfAdmin(model, this.getSession());
        return "admin/views/home/adminBusHome";
    }
}
