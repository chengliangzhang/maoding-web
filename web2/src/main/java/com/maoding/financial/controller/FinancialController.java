package com.maoding.financial.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.RoleConst;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.StringUtils;
import com.maoding.financial.dto.*;
import com.maoding.financial.service.ExpCategoryService;
import com.maoding.financial.service.ExpFixedService;
import com.maoding.financial.service.ExpMainService;
import com.maoding.org.dto.CompanyRelationDTO;
import com.maoding.org.service.CompanyService;
import com.maoding.process.service.ProcessService;
import com.maoding.system.service.SystemService;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.maoding.core.util.MapUtil.objectMap;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：FinancialController
 * 类描述：财务报销
 * 作    者：MaoSF
 * 日    期：2016年7月8日-下午3:12:45
 */
@Controller
@RequestMapping("iWork/finance")
public class FinancialController extends BaseController {

    @Autowired
    private ExpFixedService expFixedService;

    @Autowired
    private ExpMainService expMainService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private ExpCategoryService expCategoryService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProcessService processService;

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }

    //费用报销
    @RequestMapping("/adminCategoryList")
    public String adminCategoryList(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        return "admin/views/finance/adminCategoryList";
    }

    //费用报销
    @RequestMapping("/workReimbursement")
    public String workReimbursement(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        return "work/views/financial/workReimbursement";
    }

    /**
     * 费用报销首页
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/toFinanceInformation")
    public String toFinanceInformation(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        model.addAttribute("expNo", model.getOrDefault("expNo", null));
        model.addAttribute("forwardType", model.getOrDefault("forwardType", null));
        return "views/finance/financeInfo";
    }

    /**
     * 费用报销
     *
     * @param model
     * @param forwordType{1=我报销的,2=我审批的,3=报销汇总,4=我要报销,5=类别设置}
     * @return
     * @throws Exception
     */
    @RequestMapping("/toFinanceInformation/{forwordType}")
    public String toFinanceInformationBy(RedirectAttributes model, @PathVariable String forwordType) throws Exception {
        String dataAction;
        switch (forwordType) {
            case "1":
                dataAction = "myExpense";
                break;
            case "2":
                dataAction = "myChecking";
                break;
            case "3":
                dataAction = "mySummary";
                break;
            case "4":
                dataAction = "myReimbursement";
                break;
            case "5":
                dataAction = "expTypeEdit";
                break;
            default:
                dataAction = "myExpense";
                break;
        }
        model.addFlashAttribute("forwardType", dataAction);
        return "redirect:/iWork/finance/toFinanceInformation";
    }

    /**
     * 费用报销-我报销的
     *
     * @param model
     * @param expNo 报销单号
     * @return
     * @throws Exception
     */
    @RequestMapping("/toMyExpense/{expNo}")
    public String toMyExpense(RedirectAttributes model, @PathVariable String expNo) throws Exception {
        model.addFlashAttribute("expNo", expNo);
        model.addFlashAttribute("forwardType", "myExpense");
        return "redirect:/iWork/finance/toFinanceInformation";
    }

    /**
     * 费用报销-我报销的（会切换组织）
     *
     * @param model
     * @param expNo 报销单号
     * @return
     * @throws Exception
     */
    @RequestMapping("/toCompanyMyExpense/{companyId}/{expNo}")
    public String toCompanyMyExpense(HttpServletRequest request, HttpServletResponse response, RedirectAttributes model, @PathVariable String companyId, @PathVariable String expNo) throws Exception {
        String userId = this.currentUserId;
        AjaxMessage ajaxMessage = systemService.switchCompany(request, response, companyId, userId);
        if ("1".equals(ajaxMessage.getCode())) {
            throw new AccountException("切换团队失败");
        }

        model.addFlashAttribute("expNo", expNo);
        model.addFlashAttribute("forwardType", "myExpense");
        return "redirect:/iWork/finance/toFinanceInformation";
    }

    /**
     * 费用报销-我审批的
     *
     * @param model
     * @param expNo 报销单号
     * @return
     * @throws Exception
     */
    @RequestMapping("/toMyChecking/{expNo}")
    public String toMyChecking(RedirectAttributes model, @PathVariable String expNo) throws Exception {
        model.addFlashAttribute("expNo", expNo);
        model.addFlashAttribute("forwardType", "myChecking");
        return "redirect:/iWork/finance/toFinanceInformation";
    }

    /**
     * 费用报销-我审批的（会切换组织）
     *
     * @param model
     * @param expNo 报销单号
     * @return
     * @throws Exception
     */
    @RequestMapping("/toCompanyMyChecking/{companyId}/{expNo}")
    public String toCompanyMyChecking(HttpServletRequest request, HttpServletResponse response, RedirectAttributes model, @PathVariable String companyId, @PathVariable String expNo) throws Exception {
        String userId = this.currentUserId;
        AjaxMessage ajaxMessage = systemService.switchCompany(request, response, companyId, userId);
        if ("1".equals(ajaxMessage.getCode())) {
            throw new AccountException("切换团队失败");
        }

        model.addFlashAttribute("expNo", expNo);
        model.addFlashAttribute("forwardType", "myChecking");
        return "redirect:/iWork/finance/toFinanceInformation";
    }


    /**
     * 方法描述：报销增加或者修改
     * 作   者：LY
     * 日   期：2016/7/26 17:35
     *
     * @param dto
     */
    @RequestMapping(value = "/saveOrUpdateExpMainAndDetail", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateExpMainAndDetail(@RequestBody ExpMainDTO dto) throws Exception {
        updateCurrentUserInfo(dto);
        String userId = dto.getAccountId();
        String companyId = dto.getCurrentCompanyId();
        if (StringUtils.isEmpty(dto.getCompanyUserId())){
            dto.setCompanyUserId(dto.getCurrentCompanyUserId());
        }
        dto.setAppOrgId(null);
        dto.setAccountId(null);
        return expMainService.saveOrUpdateExpMainAndDetail(dto, userId, companyId);
    }

    /******************财务类别设置********************/

    /**
     * 描述     报销审批基础数据
     * 日期     2018/9/5
     * @author  张成亮
     * @return  expTypeList 报销类型
     *           projectList 关联项目
     *           auditList 关联审批
     *           processType 审批流程类型
     *           conditionList 审批人列表
     *           processFlag 是否自由流程
     * @param   request 审批申请
     *                  auditType 审批类型，定义见ProcessTypeConst.PROCESS_TYPE_xxx
     **/
    @RequestMapping(value = "/getExpBaseData", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getExpBaseDataByPost(@RequestBody AuditQueryDTO request) throws Exception {
        updateCurrentUserInfo(request);
        return AjaxMessage.succeed(expCategoryService.getExpBaseData(request));
    }

    /**
     * 方法描述：审核通过的记录（不包含退回的）
     * 作   者：MaoSF
     * 日   期：2016/12/22
     * @param   query 要查询的审批类型
     *                  auditType 审批类型，定义见ProcessTypeConst.PROCESS_TYPE_xxx
     */
    @RequestMapping(value = "/getPassAuditData", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getPassAuditData(@RequestBody QueryAuditDTO query) throws Exception{
        updateCurrentUserInfo(query);
        return AjaxMessage.succeed(expMainService.getPassAuditData(query));
    }

    /**
     * 描述       获取审批关联项目列表
     * 日期       2018/9/5
     * @author   张成亮
     **/
    @RequestMapping(value = "/getProjectList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectList(@RequestBody BaseDTO query) {
        return AjaxMessage.succeed(expMainService.getProjectListWS(currentCompanyId,currentUserId));
    }

    /**
     * 方法描述：报销类型基础数据
     * 作   者：LY
     * 日   期：2016/7/27 17:59
     */
    @RequestMapping(value = "/getExpTypeList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getExpTypeList() throws Exception {
        return this.ajaxResponseSuccess().setData(expCategoryService.getExpTypeList(this.currentCompanyId));
    }

    /**
     * 方法描述：分摊费用类型接口
     * 作   者：MaoSF
     * 日   期：2016/7/27 17:59
     */
    @RequestMapping(value = "/getExpShareTypeList", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_TYPE}, logical = Logical.OR)
    public AjaxMessage getExpShareTypeList(@RequestBody QueryExpCategoryDTO query) throws Exception {
        query.setCurrentCompanyId(this.currentCompanyId);
        query.setAccountId(this.currentUserId);
        if(StringUtil.isNullOrEmpty(query.getCompanyId())){
            query.setCompanyId(this.currentCompanyId);
        }
        return this.ajaxResponseSuccess().setData(expCategoryService.getExpShareTypeList(query));
    }


    /**
     * 方法描述：固定支出类型基础数据
     * 作   者：MaoSF
     * 日   期：2016/7/27 17:59
     */
    @RequestMapping(value = "/getExpFixTypeList", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_TYPE}, logical = Logical.OR)
    public AjaxMessage getExpFixTypeList(@RequestBody QueryExpCategoryDTO query) throws Exception {
        query.setCurrentCompanyId(this.currentCompanyId);
        query.setAccountId(this.currentUserId);
        if(StringUtil.isNullOrEmpty(query.getCompanyId()) || "root".equals(query.getCompanyId())){
            query.setCompanyId(this.currentCompanyId);
        }
        query.setIsContainSystemType("1");
        return this.ajaxResponseSuccess().setData(expCategoryService.getExpTypeList(query));
    }

    /**
     * 方法描述：财务收支类别showStatus设置，showStatus = 1，展示，showStatus =0不展示
     * 作   者：MaoSF
     * 日   期：2016/7/27 17:59
     */
    @RequestMapping(value = "/saveExpTypeShowStatus", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_TYPE}, logical = Logical.OR)
    public AjaxMessage saveExpTypeShowStatus(@RequestBody SaveExpCategoryShowStatusDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        if(StringUtil.isNullOrEmpty(dto.getCompanyId())){
            dto.setCompanyId(this.currentCompanyId);
        }
        return this.ajaxResponseSuccess().setData(expCategoryService.saveExpTypeShowStatus(dto));
    }

    /**
     * 方法描述：分摊费用-财务收支类别showStatus设置，showStatus = 1，展示，showStatus =0不展示
     * 作   者：MaoSF
     * 日   期：2016/7/27 17:59
     */
    @RequestMapping(value = "/saveExpShareTypeShowStatus", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_TYPE}, logical = Logical.OR)
    public AjaxMessage saveExpShareTypeShowStatus(@RequestBody SaveExpCategoryShowStatusDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        dto.setCategoryType(null);//此处不分类型
        if(StringUtil.isNullOrEmpty(dto.getCompanyId())){
            dto.setCompanyId(this.currentCompanyId);
        }
        return this.ajaxResponseSuccess().setData(expCategoryService.saveExpShareTypeShowStatus(dto));
    }

    /**
     * 方法描述：报销类别基础数据
     * 作   者：LY
     * 日   期：2016/7/27 17:59
     */
    //查询
    @RequestMapping(value = "/expCategory", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getCategoryBaseData() throws Exception {
        return expCategoryService.getCategoryBaseData(this.currentCompanyId, this.currentUserId);
    }

    //增加修改
    @RequestMapping(value = {"/expCategory", "/expCategory/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_TYPE}, logical = Logical.OR)
    public AjaxMessage saveOrUpdateCategoryBaseData(@RequestBody ExpTypeOutDTO dto) throws Exception {
        dto.setAccountId(this.currentUserId);
        AjaxMessage ajax = expCategoryService.saveOrUpdateCategoryBaseData(dto, this.currentCompanyId);
        if ("0".equals(ajax.getCode())) {
            this.getSession().setAttribute("expCategoryDTO", ajax.getData());
        }
        return ajax;
    }

    /**
     * 规定支出类型 新增，修改
     */
    @RequestMapping(value = {"/saveExpFixCategory", "/saveExpFixCategory/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_TYPE}, logical = Logical.OR)
    public AjaxMessage saveExpFixCategory(@RequestBody ExpCategoryDTO dto) throws Exception {
        dto.setAccountId(this.currentUserId);
        dto.setCurrentCompanyId(this.currentCompanyId);
        if(StringUtil.isNullOrEmpty(dto.getCompanyId())){
            dto.setCompanyId(this.currentCompanyId);
        }
        int i= expCategoryService.saveExpFixCategory(dto);
        return AjaxMessage.succeed("操作成功");
    }


    /**************************************/
    /**
     * 规定支出类型 新增，修改
     */
    @RequestMapping(value = {"/deleteExpCategory"}, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_TYPE}, logical = Logical.OR)
    public AjaxMessage deleteExpCategory(@RequestBody ExpCategoryDTO dto) throws Exception {
        return expCategoryService.deleteCategoryBaseData(dto.getId());
    }

    /**
     * 方法描述：得到当前公司和当前组织下面人员
     * 作   者：LY
     * 日   期：2016/8/3 17:17
     *
     * @param orgId 组织Id
     */
    @RequestMapping(value = "/getUserList/{orgId}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getUserList(@PathVariable("orgId") String orgId) throws Exception {
        return this.ajaxResponseSuccess().setData(expMainService.getUserList(this.currentCompanyId, orgId));
    }

    /**
     * 方法描述：我的报销列表
     * 作   者：LY
     * 日   期：2016/7/29 10:53
     *
     * @param
     */
    @RequestMapping(value = "/getExpMainPage", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getExpMainPage(@RequestBody Map<String, Object> param) throws Exception {

        param.put("companyId", this.currentCompanyId);
        List<ExpMainDTO> data = expMainService.getExpMainListPage(param);
        int totalNumber = expMainService.getExpMainListPageCount(param);
        param.clear();
        param.put("data", data);
        param.put("total", totalNumber);
        return this.ajaxResponseSuccess().setData(param);
    }

    /**
     * 方法描述：待我审核列表
     * 作   者：LY
     * 日   期：2016/7/29 10:53
     *
     * @param
     */
    @RequestMapping(value = "/getExpMainPageForAudit", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getExpMainPageForAudit(@RequestBody Map<String, Object> param) throws Exception {
        param.put("companyId", this.currentCompanyId);
        if (StringUtil.isNullOrEmpty(param.get("approveStatus"))) {
            param.put("defaultApproveStatus", "('0','1','2','5')");
        }
        param.put("isHave", "have");
//		param.put("companyId", this.getFromSession("companyId", String.class));
//		param.put("userId", this.getFromSession("userId", String.class));
        List<ExpMainDTO> data = expMainService.getExpMainListPage(param);
        int totalNumber = expMainService.getExpMainListPageCount(param);
        param.clear();
        param.put("data", data);
        param.put("total", totalNumber);
        return this.ajaxResponseSuccess().setData(param);
    }

    /**
     * 方法描述：待我审核列表总数
     * 作   者：LY
     * 日   期：2016/7/29 10:53
     *
     * @param
     */
    @RequestMapping(value = "/getExpMainPageForAuditCount", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getExpMainPageForAuditCount() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("auditPerson", this.currentUserId);
        param.put("defaultApproveStatus", "('0','5')");
        return this.ajaxResponseSuccess().setData(expMainService.getExpMainPageCount(param));
    }

    /**
     * 方法描述：退回报销
     * 作   者：LY
     * 日   期：2016/7/29 11:01
     *
     * @param dto -- mainId--报销单id  approveStatus--状态(2.退回) auditMessage审批意见
     */
    @RequestMapping(value = "/recallExpMain", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage recallExpMain(@RequestBody ExpAuditDTO dto) throws Exception {
        if (dto.getAuditPerson() == null) {
            dto.setAuditPerson(currentUserId);
        }
        expMainService.recallExpMain(dto);
        return ajaxResponseSuccess().setInfo("退回成功");
    }

    /**
     * 方法描述：报销详情
     * 作   者：LY
     * 日   期：2016/7/29 10:53
     *
     * @param id--报销单id
     */
    @RequestMapping(value = "/getExpMainPage/{id}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getExpMainPageDetail(@PathVariable("id") String id) throws Exception {
        ExpMainDTO dto = expMainService.getExpMainPageDetail(id);
        return this.ajaxResponseSuccess().setData(dto);
    }

    /**
     * 方法描述：删除报销
     * 作   者：LY
     * 日   期：2016/7/29 10:53
     *
     * @param id--报销单id
     */
    @RequestMapping(value = "/deleteExpMain/{id}/{versionNum}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage deleteExpMain(@PathVariable("id") String id, @PathVariable("versionNum") String versionNum) throws Exception {
        int result = expMainService.deleteExpMain(id, versionNum);
        if (result == 0) {
            return this.ajaxResponseSuccess().setCode("1").setInfo("删除失败");
        }
        return this.ajaxResponseSuccess().setInfo("删除成功");
    }

    /**
     * 方法描述：同意报销
     * 作   者：LY
     * 日   期：2016/8/1 15:08
     *
     * @param id--报销单id
     */
    @RequestMapping(value = "/agreeExpMain/{id}/{versionNum}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage agreeExpMain(@PathVariable("id") String id, @PathVariable("versionNum") String versionNum) throws Exception {
        int i = expMainService.agreeExpMain(id, versionNum, this.currentCompanyId, currentUserId);
        if (i == 1) {
            return AjaxMessage.succeed("审批成功");
        } else {
            return AjaxMessage.failed("审批失败");
        }
    }

    /**
     * 方法描述：同意报销并转移审批人
     * 作   者：LY
     * 日   期：2016/8/1 15:08
     *
     * @param id--报销单id auditPerson--新审批人
     */
    @RequestMapping(value = "/agreeAndTransAuditPerExpMain/{id}/{auditPerson}/{versionNum}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage agreeAndTransAuditPerExpMain(@PathVariable("id") String id, @PathVariable("auditPerson") String auditPerson, @PathVariable("versionNum") String versionNum) throws Exception {
        int result = expMainService.agreeAndTransAuditPerExpMain(id, this.currentUserId, auditPerson, versionNum, this.currentCompanyId);
        if (result == 0) {
            return this.ajaxResponseSuccess().setCode("1").setInfo("该记录失效,审批失败");
        }
        return this.ajaxResponseSuccess().setInfo("审批成功");
    }

    /**
     * 方法描述：同意报销
     * 作   者：LY
     * 日   期：2016/8/1 15:08
     *
     * @param id--报销单id
     */
    @RequestMapping(value = "/financialAllocation/{id}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage financialAllocation(@PathVariable("id") String id) throws Exception {
        int i = expMainService.financialAllocation(id, currentCompanyUserId, this.currentCompanyId, this.currentUserId);
        if (i == 1) {
            return AjaxMessage.succeed("操作成功");
        } else {
            return AjaxMessage.failed("操作失败");
        }
    }

    /**
     * 方法描述：财务拨款
     * 作   者：ZCL
     * 日   期：2018/3/22
     */
    @RequestMapping(value = "/financialAllocation", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage financialAllocationWithDate(@RequestBody FinancialAllocationDTO dto) throws Exception {
        String id = dto.getId();
        Date financialDate = dto.getFinancialDate();
        int i = expMainService.financialAllocation(id, currentCompanyUserId, this.currentCompanyId, this.currentUserId,financialDate);
        if (i == 1) {
            return AjaxMessage.succeed("操作成功");
        } else {
            return AjaxMessage.failed("操作失败");
        }
    }

    /**
     * 方法描述：财务退回
     * 作   者：MaoSF
     * 日   期：2018/5/23
     */
    @RequestMapping(value = "/financialRecallExpMain", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage financialRecallExpMain(@RequestBody FinancialAllocationDTO dto) throws Exception {
        dto.setAccountId(this.currentUserId);
        dto.setCurrentCompanyId(this.currentCompanyId);
        int i = expMainService.financialRecallExpMain2(dto);
        if (i == 1) {
            return AjaxMessage.succeed("操作成功");
        } else {
            return AjaxMessage.failed("操作失败");
        }
    }

    /**
     * 方法描述：报销详情与审批记录
     * 作   者：LY
     * 日   期：2016/8/2 14:13
     *
     * @param id--报销单id
     * @return
     */
    @RequestMapping(value = "/getExpMainDetail/{id}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getExpMainDetail(@PathVariable("id") String id) throws Exception {
        Map<String, Object> map = expMainService.getExpMainDetail(id);
        return this.ajaxResponseSuccess().setData(map);
    }

    /**
     * 方法描述：报销汇总
     * 作   者：LY
     * 日   期：2016/8/2 10:53
     *
     * @param
     */
    @RequestMapping(value = "/getExpMainPageForSummary", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.REPORT_EXP, "org_manager"}, logical = Logical.OR)
    public AjaxMessage getExpMainPageForSummary(@RequestBody Map<String, Object> param) throws Exception {
        String currentCompanyId = this.currentCompanyId;
        param.put("currentCompanyId", currentCompanyId);
        param.put("accountId", currentUserId);
        List<CompanyRelationDTO> companyList = null;
        try {
            companyList = companyService.getExpAmountCompanyAndChildren(currentCompanyId);
            if (!CollectionUtils.isEmpty(companyList)) {
                List<String> companyIdList = new ArrayList<>();
                for (CompanyRelationDTO c : companyList) {
                    companyIdList.add(c.getOrgId());
                }
                param.put("companyIdList", companyIdList);
                param.remove("companyId");
            }else {
                param.put("companyId",currentCompanyId);
            }
        } catch (Exception e) {
            param.put("companyId",currentCompanyId);
        }
        List<ExpMainDTO> data = expMainService.getExpMainListPageForSummary(param);
        int totalNumber = expMainService.getExpMainPageForSummaryCount(param);

        ExpSummaryDTO expSummary = expMainService.getExpMainSummary(param);
        if(expSummary==null){
            expSummary = new ExpSummaryDTO();
        }
        param.clear();
        param.put("expSumAmount", expSummary.getExpSumAmount());
        param.put("financialAllocationSumAmount", expSummary.getFinancialAllocationSumAmount());
        param.put("waitingAllocationSumAmount", expSummary.getWaitingAllocationSumAmount());
        param.put("repulseAllocationSumAmount", expSummary.getRepulseAllocationSumAmount());
        param.put("data", data);
        param.put("total", totalNumber);
        param.put("companyList",expSummary.getCompanyList());
        return this.ajaxResponseSuccess().setData(param);
    }

    /**
     *作者：FYT
     * 日期：2018/9/5
     * 描述：审批报表，报销统计详情
     */
    @RequestMapping(value = "/getAuditDetailForExp", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getAuditDetailForExp(@RequestBody QueryAuditDTO query) throws Exception {
        query.setAppOrgId(currentCompanyId);
        return this.ajaxResponseSuccess().setData(expMainService.getAuditDetailForExp(query));
    }


    /**
     * 方法描述：获取最大组织expNo + 1
     * 作   者：ZhujieChen
     * 日   期：2016/12/22
     */
    @RequestMapping(value = "/getMaxExpNo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMaxExpNo(@RequestBody ExpMainDTO dto) throws Exception {
        try {
            Map<String, Object> map = expMainService.getMaxExpNo(objectMap("companyId", dto.getCompanyId()));
            return this.ajaxResponseSuccess().setData(map);
        } catch (Exception e) {
            log.error("=======V2FinancialController.getMaxExpNo()方法出现异常=========", e);
            return this.ajaxResponseError("查询失败");
        }
    }


    /**
     * 获取固定支出月账数据
     */
    @RequestMapping(value = "/getExpFixedByExpDate/{expDate}/{companyId}", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_FIXED, "org_manager"}, logical = Logical.OR)
    public AjaxMessage getExpFixedByExpDate(@PathVariable("expDate") String expDate, @PathVariable("companyId") String companyId) throws Exception {
        if (StringUtil.isNullOrEmpty(expDate)) {
            return AjaxMessage.error("参数错误");
        }
        ExpFixedMainDTO data = expFixedService.getExpFixedByExpDate(companyId, expDate);
        return AjaxMessage.succeed(data);
    }

    /**
     * 获取固定支出月账数据
     */
    @RequestMapping(value = "/saveExpFixedByExpDate", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.FINANCE_FIXED, "org_manager"}, logical = Logical.OR)
    public AjaxMessage saveExpFixedByExpDate(@RequestBody ExpFixedMainDTO dto) throws Exception {
        if (StringUtil.isNullOrEmpty(dto.getExpDate())) {
            return AjaxMessage.error("参数错误");
        }
        dto.setCompanyId(dto.getCompanyId());
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setUserId(this.currentUserId);
        return expFixedService.saveExpFixedByExpDate(dto);
    }

    /**
     * 获取某年份的固定支出的总合计（按月份分组)
     */
    @RequestMapping(value = "/getExpAmountByYear/{year}/{companyId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getExpAmountByYear(@PathVariable("year") String year, @PathVariable("companyId") String companyId) throws Exception {
        return AjaxMessage.succeed(expFixedService.getExpFixedByMonth(companyId, year));

    }

    /**
     * 方法描述：请假--3、出差--4
     * 作   者：DongLiu
     * 日   期：2017/12/22 15:36
     */
    @RequestMapping(value = "/getLeaveDetailList", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.SUMMARY_LEAVE}, logical = Logical.OR)
    public AjaxMessage getLeaveDetailList(@RequestBody LeaveDetailQueryDTO queryDTO) throws Exception {
        if (null == queryDTO.getType()) {
            return AjaxMessage.failed(null).setInfo("没有选择类别");
        }
        Map<String, Object> para = new HashMap<String, Object>();
        queryDTO.setCompanyId(this.currentCompanyId);
        List<LeaveDetailDTO> detailDTOS = null;
        Integer total = 0;
        try {
            detailDTOS = expMainService.getLeaveDetailList(queryDTO);
            total = expMainService.getLeaveDetailCount(queryDTO);
        } catch (Exception e) {
            log.error("getLeaveDetailList" + e.getMessage());
            return AjaxMessage.failed("数据异常");
        }
        para.put("data", detailDTOS);
        para.put("total", total);
        return this.ajaxResponseSuccess().setData(para);
    }

    /**
     * 方法描述：请假、出差获取详情
     * 作   者：DongLiu
     * 日   期：2017/12/25 15:36
     */
    @RequestMapping(value = "/getLeaveDetail", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getLeaveDetail(@RequestBody LeaveDetailQueryDTO queryDTO) throws Exception {
        if (null == queryDTO.getId()) {
            return AjaxMessage.failed(null).setInfo("没有指定组织");
        }
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> flow = new HashMap<String, Object>();
        List<LeaveDetailDTO> detailDTOS = null;
        try {
            detailDTOS = expMainService.getLeaveDetailList(queryDTO);
            flow = expMainService.getLeaveDetail(queryDTO);
        } catch (Exception e) {
            log.error("getLeaveDetail" + e.getMessage());
            return AjaxMessage.failed("数据异常");
        }
        para.put("data", detailDTOS);
        para.put("flow", flow);
        return this.ajaxResponseSuccess().setData(para);
    }

    /**
     * 方法描述：在新增审批单据的时候，请求审批人接口
     * 作   者：MaoSF
     * 日   期：2016/12/22
     */
    @RequestMapping(value = "/getProcessType", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProcessType(@RequestBody AuditEditDTO dto) throws Exception{
        updateCurrentUserInfo(dto);
        return AjaxMessage.succeed(processService.getCurrentProcess(dto));
    }

    /**
     * 作者：FYT
     * 日期：2018/9/3
     * 方法描述：审批：我的申请：报销/费用
     * 我申请的（type=1），待我审批（type=2），我已审批（type=3），抄送我的（type=4）
     * @return
     */
    @RequestMapping(value = "/getAuditData")
    @ResponseBody
    public AjaxMessage getAuditData(@RequestBody QueryAuditDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setCurrentCompanyUserId(this.currentCompanyUserId);
        return this.ajaxResponseSuccess().setData(expMainService.getAuditDataForWeb(dto));

    }

    /**
     * 作者：FYT
     * 日期：2018/9/5
     * 方法描述：我的申请：详情
     *
     * @return
     */
    @RequestMapping(value = "/getAuditDataDetail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getAuditDataDetail(@PathVariable("id") String id) throws Exception {
        QueryAuditDTO dto = new QueryAuditDTO();
        dto.setId(id);
        return this.ajaxResponseSuccess().setData(expMainService.getAuditDataDetail(dto));

    }
}
