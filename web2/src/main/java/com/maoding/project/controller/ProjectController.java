package com.maoding.project.controller;

import com.maoding.commonModule.dto.ContentDTO;
import com.maoding.commonModule.dto.TemplateQueryDTO;
import com.maoding.commonModule.service.ConstService;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.RoleConst;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.ObjectUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.mytask.entity.MyTaskEntity;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dto.*;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.org.service.DepartService;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectDesignContentEntity;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.service.ProjectConditionService;
import com.maoding.project.service.ProjectConstructService;
import com.maoding.project.service.ProjectDesignContentService;
import com.maoding.project.service.ProjectService;
import com.maoding.projectmember.dto.ProjectMemberDTO;
import com.maoding.projectmember.service.ProjectMemberService;
import com.maoding.role.dto.PermissionDTO;
import com.maoding.role.dto.ProjectUserPermissionEnum;
import com.maoding.role.service.PermissionService;
import com.maoding.system.dto.DataDictionaryDTO;
import com.maoding.system.service.DataDictionaryService;
import com.maoding.system.service.SystemService;
import com.maoding.task.dto.ProjectProcessTimeDTO;
import com.maoding.task.service.ProjectProcessTimeService;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：OrgController
 * 类描述：组织controller
 * 作    者：MaoSF
 * 日    期：2016年7月8日-下午3:12:45
 */
@Controller
@RequestMapping("iWork/project")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private DepartService departService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProjectConstructService projectConstructService;

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private ProjectProcessTimeService projectProcessTimeService;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ProjectDesignContentService projectDesignContentService;

    @Autowired
    private ProjectConditionService projectConditionService;

    @Autowired
    private ConstService constService;

    @Autowired
    private CompanyDao companyDao;

    @Value("${project}")
    private String projectUrl;

    //  经营列表
    @RequestMapping("/workProjectManage")
    public String workProjectManage(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        List<DataDictionaryDTO> projectTypeList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_Type);//项目类别
        model.put("projectTypeList", projectTypeList);
        return "work/views/project/workProjectManage";
    }

    //  经营列表-项目立项
    @RequestMapping("/workAddProject/{projectType}")
    //@RequiresPermissions(value = {"GeneralManager","ProjectManager","OperateManager"},logical=Logical.OR )
    @RequiresPermissions(value = {RoleConst.PROJECT_EDIT}, logical = Logical.OR)
    public String workAddProject(ModelMap model, @PathVariable String projectType) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        model.put("projectType", projectType);
        return "work/views/project/workAddProject";
    }
    //  经营列表-项目详情

    @RequestMapping(value = "/workProjectInformation/{id}", method = RequestMethod.GET)
    public String workProjectInformation(ModelMap model, @PathVariable String id) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        model.addAttribute("projectId", id);
        return "work/views/project/workProjectInformation";
    }

    //	任务列表
    @RequestMapping("/workManufacture")
    public String workManufacture() {
        return "work/views/project/workManufacture";
    }


    /**
     * 跳转到项目信息（v2.0）
     */
    @RequestMapping(value = "/projectInformation/{id}", method = RequestMethod.GET)
    public String toProInformation(ModelMap model, @PathVariable String id) throws Exception {
        ProjectEntity project = this.projectService.selectById(id);
        if (project == null || "1".equals(project.getPstatus())) {
            return "views/project/projectRemoved";
        }
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        model.addAttribute("projectId", id);
        ProjectEntity entity = projectService.selectById(id);
        model.addAttribute("projectName", entity.getProjectName());
        model.addAttribute("forwardType", model.getOrDefault("forwardType", null));
        model.addAllAttributes(this.projectService.projectNavigationRole(id, this.currentCompanyId, this.currentUserId, this.currentCompanyUserId));
        return "views/project/projectInformation";
    }

    /**
     * 获取项目菜单权限值(控制展示与否)
     */
    @RequestMapping(value = "/getProjectNavigationRole/{id}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getProjectNavigationRole(@PathVariable("id") String id) throws Exception {
        Map<String, Object> obj = this.projectService.projectNavigationRole(id, this.currentCompanyId, this.currentUserId, this.currentCompanyUserId);
        return (obj != null) ? this.ajaxResponseSuccess().setData(obj) : ajaxResponseError("未找到数据");
    }

    /**
     * 根据项目ID、组织ID判官当前组织最高权限的所有费控查看权限
     */
    @RequestMapping(value = "/getCostRoleByCompanyId/{id}/{companyId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getCostRoleByCompanyId(@PathVariable("id") String id, @PathVariable("companyId") String companyId) throws Exception {
        Map<String, Object> obj = this.projectService.getCostRoleByCompanyId(id, companyId);
        return this.ajaxResponseSuccess().setData(obj);
    }

    /**
     * 跳转到项目信息（v2.0）
     * @param id    forwordType({1＝基本信息，2＝任务签发，3＝生产安排，4＝合同回款，5＝技术审查费，6=合作设计费，7=其他费用，8=项目文档库，9=项目成员，10=项目动态})
     */
    @RequestMapping(value = "/projectInformation/{id}/{forwordType}", method = RequestMethod.GET)
    public String toProInformation(RedirectAttributes model, @PathVariable String id, @PathVariable String forwordType) throws Exception {
        String dataAction;
        switch (forwordType) {
            case "1":
                dataAction = "basicInfo";
                break;
            case "2":
                dataAction = "taskIssue";
                break;
            case "3":
                dataAction = "productionArrangement";
                break;
            case "4":
                dataAction = "contractPayment";
                break;
            case "5":
                dataAction = "technicalReviewFee";
                break;
            case "6":
                dataAction = "cooperativeDesignFee";
                break;
            case "7":
                dataAction = "otherFee";
                break;
            case "8":
                dataAction = "projectDocumentLib";
                break;
            case "9":
                dataAction = "projectMember";
                break;
            case "10":
                dataAction = "projectDynamic";
                break;
            default:
                dataAction = "basicInfo";
                break;
        }

        model.addFlashAttribute("forwardType", dataAction);
        return "redirect:/iWork/project/projectInformation/" + id;
    }

    /**
     * 跳转到项目信息（v2.0）
     * @param forwordType({1＝基本信息，2＝任务签发，3＝生产安排，4＝合同回款，5＝技术审查费，6=合作设计费，7=其他费用，8=项目文档库，9=项目成员，10=项目动态})
     */
    @RequestMapping(value = "/companyProjectInformation/{companyId}/{projectId}/{forwordType}", method = RequestMethod.GET)
    public String toCompanyProInformation(HttpServletRequest request, HttpServletResponse response, RedirectAttributes model, @PathVariable String companyId, @PathVariable String projectId, @PathVariable String forwordType) throws Exception {

        String userId = this.currentUserId;
        AjaxMessage ajaxMessage = systemService.switchCompany(request, response, companyId, userId);
        if ("1".equals(ajaxMessage.getCode())) {
            throw new AccountException("切换团队失败");
        }
        String dataAction;
        switch (forwordType) {
            case "1":
                dataAction = "basicInfo";
                break;
            case "2":
                dataAction = "taskIssue";
                break;
            case "3":
                dataAction = "productionArrangement";
                break;
            case "4":
                dataAction = "contractPayment";
                break;
            case "5":
                dataAction = "technicalReviewFee";
                break;
            case "6":
                dataAction = "cooperativeDesignFee";
                break;
            case "7":
                dataAction = "otherFee";
                break;
            case "8":
                dataAction = "projectDocumentLib";
                break;
            case "9":
                dataAction = "projectMember";
                break;
            case "10":
                dataAction = "projectDynamic";
                break;
            default:
                dataAction = "basicInfo";
                break;
        }

        model.addFlashAttribute("forwardType", dataAction);
        return "redirect:/iWork/project/projectInformation/" + projectId;
    }

    /**
     * 跳转到项目动态（v2.0）
     */
    @RequestMapping(value = "/projectDynamicList", method = RequestMethod.GET)
    public String projectDynamicList(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        return "views/project/projectDynamicList";
    }

    /**
     * 跳转到我的任务（v2.0）
     */
    @RequestMapping(value = "/myTaskListByPage", method = RequestMethod.GET)
    public String myTaskListByPage(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        return "views/task/myTaskListByPage";
    }

    /**
     * 跳转到我的任务（v2.0）
     */
    @RequestMapping(value = "/projectListByPage", method = RequestMethod.GET)
    public String projectListByPage(ModelMap model) throws Exception {
        systemService.getCurrUserInfoOfWork(model, this.getSession());
        return "views/project/projectListByPage";
    }

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }


    /**
     * 方法描述：项目详情（包含基础数据）
     * 作者：MaoSF
     * 日期：2016/7/28
     */
    @RequestMapping(value = {"/initProject", "/initProject/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage initProject(@RequestBody Map<String, Object> map) throws Exception {
        map.put("companyId", this.currentCompanyId);
        String id = (String) map.get("id");
        ProjectShowDTO projectShowDTO = projectService.getProjectShow(map);
        projectShowDTO.setFastdfsUrl(this.fastdfsUrl);
        if (!StringUtil.isNullOrEmpty(id)) {
            ProjectDTO projectDTO = projectService.getProjectById(id, this.currentCompanyId, this.currentUserId);
            projectShowDTO.setProjectDTO(projectDTO);

            //取数据的时候，把建设单位缓存到session，以便保持项目的时候，把建设单位的联系人和项目关联起来
            if (projectDTO.getConstructDTO() != null) {
                ProjectConstructDTO dto1 = projectDTO.getConstructDTO();
                Map<String, ProjectConstructDTO> projectConstructMap = null;
                if (this.getSession().getAttribute("projectConstruct") == null) {
                    projectConstructMap = new HashMap<String, ProjectConstructDTO>();
                } else {
                    projectConstructMap = (Map) this.getSession().getAttribute("projectConstruct");
                }
                projectConstructMap.put(dto1.getId(), dto1);
                this.getSession().setAttribute("projectConstruct", projectConstructMap);
            }
            //查询所有角色（所在公司，及部门下的所有角色）
            map.clear();
            map.put("companyId", this.currentCompanyId);
            map.put("userId", this.currentUserId);
            List<DepartRoleDTO> departRoleDTOs = departService.getOrgRole(map);
            projectShowDTO.setDepartRoleList(departRoleDTOs);
        }
        return this.ajaxResponseSuccess().setData(projectShowDTO);
    }


    /**
     * 方法描述：项目详情（包含基础数据）
     * 作者：MaoSF
     * 日期：2016/7/28
     */
    @RequestMapping(value = {"/projectType"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectTypeList(@RequestBody Map<String, Object> map) throws Exception {
        map.put("companyId", this.currentCompanyId);
        List<DataDictionaryDTO> projectTypeList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_Type);//项目类别
        return this.ajaxResponseSuccess().setData(projectTypeList);
    }


    /**
     * 方法描述：变更设计阶段信息修改
     * 作者：MaoSF
     * 日期：2016/8/4
     */
    @RequestMapping(value = {"/projectDesignContentDetail", "/projectDesignContentDetail/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    //@RequiresPermissions(value = {"GeneralManager","ProjectManager","OperateManager"},logical=Logical.OR )
    //  @RequiresPermissions(value = {"project_eidt"}, logical = Logical.OR)
    public AjaxMessage projectDesignContentDetail(@RequestBody ProjectProcessTimeDTO projectProcessTimeDTO) throws Exception {
        projectProcessTimeDTO.setAccountId(this.currentUserId);
        projectProcessTimeDTO.setCompanyId(this.currentCompanyId);
        projectProcessTimeService.saveChangeTime(projectProcessTimeDTO, projectProcessTimeDTO.getTargetId());
        return AjaxMessage.succeed("保存成功");
    }


    /**
     * 方法描述：根据设计阶段id查询变更详情
     * 作者：MaoSF
     * 日期：2016/8/4
     */
    @RequestMapping(value = "/projectDesignContent/{id}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getProjectDesignContentDetail(@PathVariable("id") String id) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("targetId", id);
        map.put("type", 1);
        List<ProjectProcessTimeDTO> list = projectProcessTimeService.getProjectProcessTimeList(map);
        return this.ajaxResponseSuccess().setData(list);
    }


    /*************常用合作伙伴*****************/

    /**
     * 方法描述：常用合作伙伴
     * 作者：MaoSF
     * 日期：2016/8/26
     */
    @RequestMapping(value = "/getUsedCooperationPartners", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getUsedCooperationPartners() throws Exception {
        List<CompanyDTO> list = companyService.getUsedCooperationPartners(this.currentCompanyId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("companyId", this.currentCompanyId);
        List<DepartDTO> departList = departService.getDepartByCompanyId(map);
        CompanyDTO company = companyService.getCompanyById(this.currentCompanyId);
        company.setDepartList(departList);
        list.add(0, company);
        return this.ajaxResponseSuccess().setData(list);
    }


    /**
     * 方法描述：项目签发--不常用公司
     * 作者：MaoSF
     * 日期：2016/8/6
     */
    @RequestMapping(value = {"/getLikedCooperationPartners", "/getLikedCooperationPartners/{keyword}"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getDesignOrg(@PathVariable Map<String, Object> map) throws Exception {
        String companyId = this.currentCompanyId;
        if (map == null || CollectionUtils.isEmpty(map)) {
            map = new HashMap<String, Object>();
        }
        map.put("orgId", companyId);
        List<CompanyDTO> list = companyService.getCompanyFilterbyParam(map);
        return this.ajaxResponseSuccess("操作成功").setData(list);
    }

    /****************常用乙方***********************/
    /**
     * 方法描述：常用乙方
     * 作者：MaoSF
     * 日期：2016/8/26
     */
    @RequestMapping(value = "/getUsedPartB", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getUsedPartB() throws Exception {
        String companyId = this.currentCompanyId;
        String parentId = companyService.getRootCompanyId(companyId);
        List<CompanyEntity> companyEntityList = new ArrayList<CompanyEntity>();
        //List<CompanyEntity> companyEntityList= companyService.getAllParentCompanyList(companyId);

        CompanyEntity companyEntity1 = companyService.selectById(companyId);
        if (!StringUtil.isNullOrEmpty(parentId) && !parentId.equals(companyId)) {
            CompanyEntity companyEntity = companyService.selectById(parentId);
            companyEntityList.add(companyEntity);
        }
        companyEntityList.add(companyEntity1);
        List<Object> companyList = new ArrayList<Object>();
        for (CompanyEntity entity : companyEntityList) {
            Map<String, String> company = new HashMap<String, String>();
            company.put("id", entity.getId());
            company.put("companyName", entity.getCompanyName());
            companyList.add(company);
        }
        return this.ajaxResponseSuccess().setData(companyList);
    }

    /**
     * 方法描述：模糊查询乙方
     * 作者：MaoSF
     * 日期：2016/8/26
     */
    @RequestMapping(value = {"/getLikedPartB", "/getLikedPartB/{keyword}"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getLikedPartB(@RequestBody Map<String, Object> map) throws Exception {
        //乙方
        if (map == null || CollectionUtils.isEmpty(map)) {
            map = new HashMap<String, Object>();
        }
        map.put("orgId", this.currentCompanyId);
        List<CompanyDTO> partBList = companyService.getCompanyFilterbyParam(map);
        return this.ajaxResponseSuccess().setData(partBList);
    }


    /***********常用甲方*****************/
    /**
     * 方法描述：常用甲方
     * 作者：MaoSF
     * 日期：2016/8/26
     */
    @RequestMapping(value = "/getUsedPartA", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getUsedPartA() throws Exception {
        return this.ajaxResponseSuccess().setData(this.projectConstructService.getUsedConstructByCompanyId(this.currentCompanyId));
    }


    /**
     * 方法描述：模糊查询甲方
     * 作者：MaoSF
     * 日期：2016/8/26
     */
    @RequestMapping(value = {"/getLikedPartA", "/getLikedPartA/{keyword}"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getLikedPartA(@RequestBody Map<String, Object> map) throws Exception {
        //甲方
        if (map == null || CollectionUtils.isEmpty(map)) {
            map = new HashMap<String, Object>();
        }
        map.put("companyId", this.currentCompanyId);
        //甲方：建设单位
        List<ProjectConstructDTO> partBList = projectConstructService.getConstructByParam(map);
        return this.ajaxResponseSuccess().setData(partBList);
    }


    /**
     * 方法描述：修改项目设计依据
     * 作        者：MaoSF
     * 日        期：2016年4月20日-下午3:05:14
     */
    @RequestMapping(value = "/saveProjectDesignBasic/{id}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveProjectDesignBasic(@RequestBody ProjectDTO dto) throws Exception {

        dto.setAccountId(this.currentUserId);
        dto.setCurrentCompanyId(this.currentCompanyId);
        return this.projectService.saveProjectDesignBasic(dto);
    }


    //=========================================================新接口=====================================================================

    /**
     * 方法描述：项目列表（进行中的项目）
     * 作者：MaoSF
     * 日期：2016/7/29
     */
    @RequestMapping(value = "/getProjects", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectList(@RequestBody Map<String, Object> param) throws Exception {
        String companyId = this.currentCompanyId;
        String companyUserId = (String) param.get("companyUserId");
        Map<String, Object> condition = new HashMap<>();
        //查询条件
        if (null == companyId || "".equals(companyId)) {
            return this.ajaxResponseSuccess();
        }
        setMapConditions(param, companyId, companyUserId, condition);
        //数据
        Map<String, Object> result = projectService.getProcessingProjectsByPage(param);
        //显示列查询
        Map<String, Object> para = setProjectUserPermissionParam();
        List<PermissionDTO> permissionDTOS = permissionService.getProjectUserPermission(para);
        if (0 < permissionDTOS.size()) {
            result.put("flag", 1);
        } else {
            result.put("flag", 0);
        }
        String columnCodes = "";
        Map<String, Object> proCondition = getProConditionMap(param, companyId);
        List<ProjectConditionDTO> conditionDTOS = projectConditionService.selProjectConditionList(proCondition);
        if (0 < conditionDTOS.size()) {
            columnCodes = conditionDTOS.get(0).getCode();
        }
        result.put("columnCodes", columnCodes);
        return this.ajaxResponseSuccess().setData(result);
    }

    /**
     * 方法描述：项目列表条件数据查询
     * 作者：MaoSF
     * 日期：2016/7/29
     */
    @RequestMapping(value = "/getProjectConditions", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    public AjaxMessage getProjectConditions(@RequestBody Map<String, Object> param) throws Exception {
        String companyId = this.currentCompanyId;
        String companyUserId = (String) param.get("companyUserId");
        Map<String, Object> condition = new HashMap<>();
        //查询条件
        if (null == companyId || "".equals(companyId)) {
            return this.ajaxResponseSuccess();
        }
        setMapConditions(param, companyId, companyUserId, condition);

        param.remove("pageIndex");
        param.remove("pageSize");//此处不分页处理，查询所有项目信息
        List<ProjectTableDTO> dataList = projectService.getProjectsByPage(projectService.getProjectParam(condition));
        // 遍历查询条件
        Map<String, String> companyNames = new HashMap<>();
        Map<String, String> partyANames = new HashMap<>();
        Map<String, String> partyBNames = new HashMap<>();
        Map<String, String> busPersonInChargeMap = new HashMap<>();//经营负责人
        Map<String, String> designPersonInChargeMap = new HashMap<>();//设计负责人
        Map<String, String> busPersonInChargeAssistantMap = new HashMap<>();//经营负责人助理
        Map<String, String> designPersonInChargeAssistantMap = new HashMap<>();//设计负责人助理
        LinkedHashMap<String, String> buildTypeNames = new LinkedHashMap<>();
        StringBuffer buildTypeIds = new StringBuffer();
        setSelConditions(dataList, companyNames, partyANames, partyBNames, buildTypeNames, buildTypeIds,
                busPersonInChargeMap, designPersonInChargeMap, busPersonInChargeAssistantMap, designPersonInChargeAssistantMap);
        Map<String, Object> para = setProjectUserPermissionParam();
        List<PermissionDTO> permissionDTOS = permissionService.getProjectUserPermission(para);
        if (0 < permissionDTOS.size()) {
            param.put("flag", 1);
        } else {
            param.put("flag", 0);
        }

        //为项目列表添加合作组织信息过滤选择列表，即当前组织发布了签发任务给到的组织，不包含自己
        //这里需要查询所有项目的合作组织
        CompanyQueryDTO cooperatorCompanyQuery = new CompanyQueryDTO();
        cooperatorCompanyQuery.setCurrentCompanyId(companyId);
        //只查询外发的组织
        cooperatorCompanyQuery.setIsPay("1");
        List<CompanyDTO> cooperatorCompanyList = companyDao.listCompanyCooperate(cooperatorCompanyQuery);
        //生成查询组织列表
        Map<String, String> designCompanyNames = new HashMap<>();
        if (ObjectUtils.isNotEmpty(cooperatorCompanyList)){
            cooperatorCompanyList.forEach(company->
                    designCompanyNames.put(company.getId(),company.getCompanyName())
            );
        }

        param.put("designCompanyNames",designCompanyNames);
        param.put("companyNames", companyNames);
        param.put("partyANames", partyANames);
        param.put("partyBNames", partyBNames);
        param.put("buildTypeNames", buildTypeNames);
        param.put("busPersonInChargeMap", busPersonInChargeMap);
        param.put("designPersonInChargeMap", designPersonInChargeMap);
        param.put("busPersonInChargeAssistantMap", busPersonInChargeAssistantMap);
        param.put("designPersonInChargeAssistantMap", designPersonInChargeAssistantMap);
        return this.ajaxResponseSuccess().setData(param);
    }

    private void setMapConditions(@RequestBody Map<String, Object> param, String companyId, String companyUserId, Map<String, Object> condition) {
        String type;
        if (null == companyUserId || "".equals(companyUserId)) {
            param.put("companyUserId", this.currentCompanyUserId);
            condition.put("companyUserId", this.currentCompanyUserId);
        }
        param.put("relationCompany", companyId);
        condition.put("relationCompany", companyId);

        if (StringUtil.isNullOrEmpty(param.get("type"))) {//如果type为空，则设置为0
            type = "0";
        } else {
            type = param.get("type").toString();
        }
        if (!StringUtil.isNullOrEmpty(param.get("orgId"))) {
            param.put("companyId", param.get("orgId").toString());
            condition.put("companyId", param.get("orgId").toString());
        }
        param.put("type", type);
        condition.put("type", type);
        param.put("companyMainId",companyId);
        condition.put("companyMainId", companyId);
    }

    private void setSelConditions(List<ProjectTableDTO>  dataList,
                                  Map<String, String> companyNames, Map<String, String> partyANames,
                                  Map<String, String> partyBNames, LinkedHashMap<String, String> buildTypeNames,
                                  StringBuffer buildTypeIds, Map<String, String> busPersonInChargeMap, Map<String, String> designPersonInCharge,
                                  Map<String, String> busPersonInChargeAssistantMap, Map<String, String> designPersonInChargeAssistantMap) {

        List<String> projectIdList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ProjectTableDTO dto = dataList.get(i);
            projectIdList.add(dto.getId());
            if (null != dto && null != dto.getCompanyId() && null != dto.getCompanyName()) {
                companyNames.put(dto.getCompanyId(), dto.getCompanyName());
            }
            if (null != dto && null != dto.getConstructCompany() && null != dto.getPartyA()) {
                partyANames.put(dto.getConstructCompany(), dto.getPartyA());
            }
            if (null != dto && null != dto.getCompanyBid() && null != dto.getPartyB()) {
                partyBNames.put(dto.getCompanyBid(), dto.getPartyB());
            }
            if (null != dto && null != dto.getBuildType()) {
                buildTypeIds.append(dto.getBuildType());
            }
            if (null != dto && null != dto.getBusPersonInChargeUserId() && null != dto.getBusPersonInCharge()) {
                busPersonInChargeMap.put(dto.getBusPersonInChargeUserId(), dto.getBusPersonInCharge());
            }
            if (null != dto && null != dto.getBusPersonInChargeAssistantUserId() && null != dto.getBusPersonInChargeAssistant()) {
                busPersonInChargeAssistantMap.put(dto.getBusPersonInChargeAssistantUserId(), dto.getBusPersonInChargeAssistant());
            }
            if (null != dto && null != dto.getDesignPersonInChargeUserId() && null != dto.getDesignPersonInCharge()) {
                designPersonInCharge.put(dto.getDesignPersonInChargeUserId(), dto.getDesignPersonInCharge());
            }
            if (null != dto && null != dto.getDesignPersonInChargeAssistantUserId() && null != dto.getDesignPersonInChargeAssistant()) {
                designPersonInChargeAssistantMap.put(dto.getDesignPersonInChargeAssistantUserId(), dto.getDesignPersonInChargeAssistant());
            }
        }

//        List<String> idList = Arrays.asList(buildTypeIds.toString().split(","));
//        Map<String, Object> para1 = new HashMap<>();
//        para1.put("idList", idList);
//        List<DataDictionaryEntity> entities = dataDictionaryService.getDataByParemeter(para1);

        if(!CollectionUtils.isEmpty(projectIdList)){
            List<ContentDTO> buildTypeList = projectService.getProjectBuildType(projectIdList);
            buildTypeList.stream().forEach(b->{
                buildTypeNames.put(b.getName(), b.getName());//后台用name查找
            });
        }
    }

    private Map<String, Object> getProConditionMap(@RequestBody Map<String, Object> param, String companyId) {
        Map<String, Object> proCondition = new HashMap<>();
        proCondition.put("companyId", companyId);
        proCondition.put("userId", this.currentUserId);
        if ("1".equals(param.get("type"))) {
            proCondition.put("type", 0);
        } else {
            proCondition.put("type", 1);
        }
        proCondition.put("status", 0);
        return proCondition;
    }

    private Map<String, Object> setProjectUserPermissionParam() {
        Map<String, Object> para = new HashMap<>();
        para.put("companyUserId", this.currentCompanyUserId);
        para.put("companyId", this.currentCompanyId);
        List<String> codes = new ArrayList<String>();
        codes.add(ProjectUserPermissionEnum.ORG_MANAGER.getName());
        codes.add(ProjectUserPermissionEnum.PROJECT_MANAGER.getName());
        codes.add(ProjectUserPermissionEnum.DESIGN_MANAGER.getName());
        codes.add(ProjectUserPermissionEnum.SUPER_PROJECT_EDIT.getName());
        codes.add(ProjectUserPermissionEnum.PROJECT_CHARGE_MANAGER.getName());
        codes.add(ProjectUserPermissionEnum.FINANCE_BACK_FEE.getName());
        codes.add(ProjectUserPermissionEnum.PROJECT_EDIT.getName());
        codes.add(ProjectUserPermissionEnum.PROJECT_OVERVIEW.getName());
        para.put("codes", codes);
        return para;
    }


    /**
     * 方法描述：新增（项目立项）、修改项目
     * 作者：MaoSF
     * 日期：2016/7/29
     */
    @RequestMapping(value = {"/project"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveProject(@RequestBody ProjectDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setCurrentCompanyUserId(this.currentCompanyUserId);
        if (StringUtil.isNullOrEmpty(dto.getCompanyId())) {
            dto.setCompanyId(this.currentCompanyId);
        }
        dto.setAccountId(this.currentUserId);
        AjaxMessage ajaxMessage = projectService.saveOrUpdateProjectNew(dto);
        return ajaxMessage;
    }

    @RequestMapping(value = {"/project/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    // @RequiresPermissions(value = {RoleConst.PROJECT_EDIT}, logical = Logical.OR) //此处用代码控制
    public AjaxMessage updateProject(@RequestBody ProjectDTO dto) throws Exception {
        if (!projectService.isEditProject(dto.getId(), this.currentCompanyId, this.currentUserId)) {
            throw new UnauthorizedException();
        }
        return saveProject(dto);
    }


    /**
     * 方法描述：根据名称模糊检索甲方（包含基础数据）
     * 作者：TangY
     * 日期：2016/7/28
     */
    @RequestMapping(value = {"/constructList"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getConstructDTOList(@RequestBody Map<String, Object> map) throws Exception {
        map.put("companyId", this.currentCompanyId);
        List<ProjectConstructDTO> projectConstructDTOList = projectService.getProjectConstructListByName(map);
        return this.ajaxResponseSuccess().setData(projectConstructDTOList);
    }

    /**
     * 方法描述：立项基础数据(查询设计依据，设计范围)
     * 作者：TangY （2.0）
     * 日期：2016/7/28
     */
    @RequestMapping(value = {"/addProjectBasicData"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage addProjectInstall() throws Exception {
        List<DataDictionaryDTO> rangeList = projectService.getDesignRangeList();
        List<DataDictionaryDTO> contentList = projectService.getDesignContentList();
        List<CompanyUserEntity> companyUserEntityList = companyUserService.getCompanyUserByCompanyId(this.currentCompanyId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("permissionId", "25");//任务签发权限id
        map.put("companyId", this.currentCompanyId);
        List<CompanyUserTableDTO> companyUserList = companyUserService.getCompanyUserByPermissionId(map);
        String hasManager = "";
        if (CollectionUtils.isEmpty(companyUserList)) {
            hasManager = "1";
        } else {
            hasManager = "0";
        }
        List<ProjectConstructDTO> projectConstructList = projectService.getProjectConstructList(this.currentCompanyId);
        Map<String, Object> returnMap = new <String, Object>HashMap();
        returnMap.put("rangeList", rangeList);
        returnMap.put("contentList", contentList);
        //hasManager （是否有经营权限0,是有，1，没有）
        returnMap.put("hasManager", hasManager);
        returnMap.put("projectConstructList", projectConstructList);
        returnMap.put("companyUserEntityList", companyUserEntityList);
        return this.ajaxResponseSuccess().setData(returnMap);
    }

    /**
     * 方法描述：立项基础数据(查询设计范围)
     * 作者：TangY （2.0）
     * 日期：2016/7/28
     */
    @RequestMapping(value = {"/getDesignRangeList"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getDesignRangeList() throws Exception {
        List<DataDictionaryDTO> rangeList = projectService.getDesignRangeList();
        return this.ajaxResponseSuccess().setData(rangeList);
    }

    /**
     * 方法描述：立项基础数据(查询设计依据)
     * 作者：TangY （2.0）
     * 日期：2016/7/28
     */
    @RequestMapping(value = {"/getDesignBasicList"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getDesignBasicList() throws Exception {
        List<DataDictionaryDTO> designBasicList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_DESIGNBASIC);//设计依据
        return this.ajaxResponseSuccess().setData(designBasicList);
    }

    /**
     * 方法描述：立项基础数据(查询设计阶段)
     * 作者：TangY （2.0）
     * 日期：2016/7/28
     */
    @RequestMapping(value = {"/getDesignContentList"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getDesignContentList() throws Exception {
        List<DataDictionaryDTO> designContentList = dataDictionaryService.getSubDataByCodeToDTO(SystemParameters.PRO_DESIGNCONTENT);
        ;//设计阶段
        return this.ajaxResponseSuccess().setData(designContentList);
    }


    /**
     * 方法描述：获取项目的基础信息（2.0）
     * 作者：TangY
     * 日期：2016/7/29
     */
    @RequestMapping(value = "/getProjectDetails/{projectId}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectDetails(@PathVariable("projectId") String projectId) throws Exception {
        ProjectDTO projectDTO = projectService.getProjectById(projectId, this.currentCompanyId, this.currentUserId);
        return ajaxResponseSuccess().setData(projectDTO);
    }

    /**
     * 方法：获取项目的基础信息（3.0）
     * 作者：Zhangchengliang
     * 日期：2017/8/15
     */
    @RequestMapping(value = "/loadProjectDetails/{projectId}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage loadProjectDetails(@PathVariable("projectId") String projectId) throws Exception {
        ProjectDetailsDTO result = projectService.loadProjectDetails(projectId, currentCompanyId, currentUserId);
        if ((result != null) && (result.getPartyBCompany() != null)) {
            result.setCompanyBidName(result.getPartyBCompany().getCompanyName());
        }
        return ajaxResponseSuccess().setData(result);
    }

    /**
     * 方法：同时保存项目的全部基础信息
     * 作者：Zhangchengliang
     * 日期：2017/8/15
     */
    @RequestMapping(value = "/saveProjectDetails", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.PROJECT_EDIT}, logical = Logical.OR)
    public AjaxMessage saveProjectDetails(@RequestBody ProjectDetailsDTO project) throws Exception {
        return ajaxResponseSuccess();
    }

    /**
     * 方法：保存项目的某一项自定义信息
     * 作者：Zhangchengliang
     * 日期：2017/8/15
     */
    @RequestMapping(value = "/saveProjectField", method = RequestMethod.POST)
    @ResponseBody
    // @RequiresPermissions(value = {RoleConst.PROJECT_EDIT}, logical = Logical.OR),此处用代码控制
    public AjaxMessage saveProjectField(@RequestBody ProjectFieldChangeDTO changedField) throws Exception {
        if (!projectService.isEditProject(changedField.getProjectId(), this.currentCompanyId, this.currentUserId)) {
            throw new UnauthorizedException();
        }
        if (changedField.getOperatorId() == null) {
            changedField.setOperatorId(currentUserId);
        }
        projectService.saveCustomProperty(changedField);
        return ajaxResponseSuccess();
    }

    /**
     * 方法：获取项目基础信息的自定义字段
     * 作者：Zhangchengliang
     * 日期：2017/8/15
     */
    @RequestMapping(value = "/loadProjectCustomFields", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage loadProjectCustomFields(@RequestBody ProjectCustomFieldQueryDTO query) throws Exception {
        if (query.getCompanyId() == null) {
            query.setCompanyId(currentCompanyId);
        }
        CustomProjectPropertyEditDTO result = projectService.loadProjectCustomFields(query);
        result.setCompanyId(query.getCompanyId());
        result.setProjectId(query.getProjectId());
        result.setOperatorId(currentUserId);
        return ajaxResponseSuccess().setData(result);
    }

    /**
     * 方法：保存项目基础信息的自定义字段
     * 作者：Zhangchengliang
     * 日期：2017/8/15
     */
    @RequestMapping(value = "/saveProjectCustomFields", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveProjectCustomFields(@RequestBody CustomProjectPropertyEditDTO properties) throws Exception {
        if (properties.getCompanyId() == null) {
            properties.setCompanyId(currentCompanyId);
        }
        if (properties.getOperatorId() == null) {
            properties.setCompanyId(currentUserId);
        }
        projectService.saveProjectCustomFields(properties);
        return ajaxResponseSuccess();
    }

    /**
     * @author  张成亮
     * @date    2018/7/4
     * @description     查询功能分类
     * @param
     * @return
     **/
    @RequestMapping(value = "/listFunction", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage listFunction(@RequestBody TemplateQueryDTO query) throws Exception {
        query.setFunction(true);
        List<ContentDTO> list = constService.listTemplateContent(query);
        return AjaxMessage.succeed(list);
    }

    /**
     * 方法描述：获取项目的设计范围（2.0）
     * 作者：TangY
     * 日期：2016/7/29
     */
    @RequestMapping(value = "/getProjectDesignRange/{projectId}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectDesignRange(@PathVariable("projectId") String projectId) throws Exception {
        List<ProjectDesignRangeDTO> projectDesignRangeDTOList = projectService.getProjectDesignRangeById(projectId);
        return ajaxResponseSuccess().setData(projectDesignRangeDTOList);
    }

    /**
     * 方法描述：获取项目搜索条件（2.0）
     * 作者：TangY
     * 日期：2016/7/29
     */
    @RequestMapping(value = "/getProjectSearchBaseData", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectSearchBaseData() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("companyId", this.currentCompanyId);
        return projectService.getSearchBaseData(param);
    }

    /**
     * 方法描述：获取我的项目项目搜索条件（2.0）
     * 作者：TangY
     * 日期：2016/7/29
     */
    @RequestMapping(value = "/getMyProjectSearchBaseData", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMyProjectSearchBaseData() throws Exception {
        CompanyUserEntity companyUserEntity = companyUserService.getCompanyUserByUserIdAndCompanyId(this.currentUserId, this.currentCompanyId);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("companyUserId", companyUserEntity.getId());
        return projectService.getSearchBaseData(param);
    }

    /**
     * 方法描述：获取项目的设计阶段（2.0）
     * 作者：TangY
     * 日期：2016/7/29
     */
    @RequestMapping(value = "/getDesignContentList/{projectId}", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getDesignContentList(@PathVariable("projectId") String projectId) throws Exception {
        List<ProjectDesignContentEntity> projectDesignContentEntityList = projectService.getDesignContentListByProjectId(projectId);
        return ajaxResponseSuccess().setData(projectDesignContentEntityList);
    }

    /**
     * 方法描述：获取团队中我的任务
     * 作者：TangY
     * 日期：2016/7/29
     */
    @RequestMapping(value = "/getTaskList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getTaskList(@RequestBody Map<String, Object> param) throws Exception {
        param.put("companyId", this.currentCompanyId);
        if (StringUtil.isNullOrEmpty(param.get("handlerId"))) {
            param.put("handlerId", this.currentCompanyUserId);
        }
        Map<String, Object> resultMap = myTaskService.getMyTaskByParam(param);
        return ajaxResponseSuccess().setData(resultMap);
    }


    /**
     * 方法描述：获取项目中我的任务
     * 作者：TangY
     * 日期：2016/7/29
     */
    @RequestMapping(value = "/getMyTaskListByProjectId", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMyTaskListByProjectId(@RequestBody Map<String, Object> param) throws Exception {
        if (StringUtil.isNullOrEmpty(param.get("handlerId"))) {
            param.put("handlerId", this.currentCompanyUserId);
        }
        if (null == param.get("projectId")) {
            return this.ajaxResponseError("项目Id不能为空");
        }
        String companyUserId = this.currentCompanyUserId;
        String projectId = param.get("projectId").toString();
        List<MyTaskEntity> myTaskEntityList = myTaskService.getMyTaskByProjectId(projectId, this.currentCompanyId, companyUserId);
        return ajaxResponseSuccess().setData(myTaskEntityList);
    }

    /**
     * 方法描述：删除项目动态
     * 作者：TangY
     * 日期：2016/7/29
     *
     * @param: param dynamicsId
     */
    @RequestMapping(value = "/projectDynamics/{dynamicsId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getProjectDynamicsList(@PathVariable("dynamicsId") String dynamicsId) throws Exception {
        int i = projectService.deleteProjectDynamics(dynamicsId);
        if (i > 0) {
            return this.ajaxResponseSuccess();
        } else {
            return this.ajaxResponseError("删除失败！");
        }
    }

    /**
     * 方法描述：查询项目参与人
     * 作者：TangY
     * 日期：2016/7/29
     */
    @RequestMapping(value = "/getProjectParticipants/{projectId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getProjectParticipants(@PathVariable("projectId") String projectId) throws Exception {
        List<ProjectMemberDTO> projectParticipantsList = this.projectMemberService.listProjectMember(projectId, currentCompanyId, currentUserId);
        return ajaxResponseSuccess().setData(projectParticipantsList);
    }


    /**
     * 方法描述：删除项目
     * 作者：MaoSF
     * 日期：2016/7/29
     */
    @RequestMapping(value = {"/deleteProject/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage deleteProject(@PathVariable("id") String id) throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(id);
        return projectService.deleteProjectById(id, currentCompanyUserId);
    }

    /**
     * 方法描述：获取项目功能
     * 作者：MaoSF
     * 日期：2016/7/29
     */
    @RequestMapping(value = {"/getProjectBulidType"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectBulidType() throws Exception {
        return projectService.getBuildType(this.currentCompanyId);
    }

    /**
     * 方法描述：保存计划进度时间
     * 作者：MaoSF
     * 日期：2016/7/29
     */
    @RequestMapping(value = {"/saveOrUpdateProjectProcessTime"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateProjectProcessTime(@RequestBody ProjectProcessTimeDTO dto) throws Exception {
        if (!this.projectService.isEditProject(dto.getProjectId(), this.currentCompanyId, this.currentUserId)) {
            throw new UnauthorizedException();
        }
        dto.setAccountId(this.currentUserId);
        dto.setCompanyId(this.currentCompanyId);
        projectProcessTimeService.saveChangeTime(dto, dto.getTargetId());
        return AjaxMessage.succeed("保存成功");
    }


    /**
     * 方法描述：保存设计阶段
     * 作者：MaoSF
     * 日期：2016/7/29
     */
    @RequestMapping(value = {"/saveOrUpdateProjectDesign"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateProjectDesign(@RequestBody ProjectDesignContentDTO dto) throws Exception {
        if (!this.projectService.isEditProject(dto.getProjectId(), this.currentCompanyId, this.currentUserId)) {
            throw new UnauthorizedException();
        }
        dto.setCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        return this.projectService.saveOrUpdateProjectDesign(dto);
    }

    /**
     * 方法描述：
     * 作   者：MaoSF
     * 日   期：2017/6/7 17:39
     * @param: param{projectName}
     */
    @RequestMapping(value = {"/listProjectByProjectName"}, method = RequestMethod.POST)
    @ResponseBody
    public List<ProjectDTO> listProject(@RequestBody Map<String, Object> param) {
        ProjectDTO dto = new ProjectDTO();
        String projectName = (String) param.get("projectName");
        dto.setProjectName(projectName);
        dto.setCompanyId(this.currentCompanyId);
        return projectService.getProjectListByCompanyId(dto);
    }

    /**
     * 方法描述：获取工时统计
     * 作   者：DongLiu
     * 日   期：2018/1/3 15:57
     */
    @RequestMapping(value = "/getProjectWorkingHours", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectWorkingHours(@RequestBody ProjectWorkingHoursDTO hoursDTO) throws Exception {
        if (null == hoursDTO.getProjectId()) {
            return AjaxMessage.failed(null).setInfo("没有指定组织");
        }
//        hoursDTO.setCurrentCompanyId(this.currentCompanyUserId);
        Map<String, Object> para = new HashMap<String, Object>();
        List<ProjectMemberDTO> projectMemberEntities = null;
        try {
            projectMemberEntities = this.projectMemberService.getProjectWorkingHours(hoursDTO);
            Integer total = this.projectMemberService.getProjectWorkingHoursCount(hoursDTO);
            String sum = this.projectMemberService.getProjectWorkingHoursSum(hoursDTO);
            para.put("data", projectMemberEntities);
            para.put("total", total == null ? 0 : total);
            para.put("sum", sum);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getProjectParticipants" + e.getMessage());
            return this.ajaxResponseError("数据异常");
        }
        return ajaxResponseSuccess().setData(para);
    }

    /**
     * 方法描述：工时汇总
     * 作   者：DongLiu
     * 日   期：2018/1/3 16:36
     */
    @RequestMapping(value = "/getProjectWorking", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.SUMMARY_LEAVE}, logical = Logical.OR)
    public AjaxMessage getProjectWorking(@RequestBody ProjectWorkingHoursDTO hoursDTO) throws Exception {
        Map<String, Object> param = new HashMap<>();
        List<ProjectWorkingHourTableDTO> projectWorkingHourTableDTOS = null;
        try {
            Integer total = projectService.getProjectWorkingCount(hoursDTO);
            projectWorkingHourTableDTOS = projectService.getProjectWorking(hoursDTO);
            param.put("data", projectWorkingHourTableDTOS);
            param.put("total", total);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getProjectWorking" + e.getMessage());
            return this.ajaxResponseError("数据异常");
        }
        return this.ajaxResponseSuccess().setData(param);
    }

    /**
     * 方法描述：删除设计阶段
     */
    @RequestMapping(value = "/deleteProjectDesign", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage deleteProjectDesign(@RequestBody ProjectDesignContentDTO dto) throws Exception {
        return projectDesignContentService.deleteProjectDesignContent(dto.getId());
    }

    /**
     * 方法描述：保存我的项目过滤条件
     * 作   者：DongLiu
     * 日   期：2018/1/25 17:46
     */
    @RequestMapping(value = "/insertProCondition", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage insertProCondition(@RequestBody Map<String, Object> param) throws Exception {
        //判断是否存在
        param.put("userId", this.currentUserId);
        int status = projectConditionService.insertProjectCondition(param);
        if (0 < status) {
            return this.ajaxResponseSuccess("操作成功");
        } else {
            return this.ajaxResponseError("数据异常");
        }
    }

    @RequestMapping(value ={"/builtType/{projectId}"} , method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage changeCustomBuiltType(@RequestBody ContentDTO dto, @PathVariable("projectId") String projectId) throws Exception {
        return null;
    }
}



