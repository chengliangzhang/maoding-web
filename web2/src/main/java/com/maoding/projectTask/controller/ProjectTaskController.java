package com.maoding.projectTask.controller;

import com.beust.jcommander.internal.Maps;
import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.ProjectMemberType;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.TxtFileUtil;
import com.maoding.mytask.dto.HandleMyTaskDTO;
import com.maoding.mytask.dto.MyTaskActiveRequestDTO;
import com.maoding.mytask.entity.MyTaskEntity;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.org.service.CompanyUserService;
import com.maoding.project.dto.ProjectProcessDTO;
import com.maoding.project.dto.ProjectTaskResponsibleDTO;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.service.ProjectProcessService;
import com.maoding.project.service.ProjectService;
import com.maoding.project.service.ProjectTaskResponsibleService;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.projectmember.service.ProjectMemberService;
import com.maoding.task.dto.*;
import com.maoding.task.entity.ProjectTaskEntity;
import com.maoding.task.service.ProjectManagerService;
import com.maoding.task.service.ProjectProcessTimeService;
import com.maoding.task.service.ProjectTaskExportService;
import com.maoding.task.service.ProjectTaskService;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp21 on 2016/12/29.
 */

@Controller
@RequestMapping("iWork/projectTask")
public class ProjectTaskController extends BaseController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ProjectTaskResponsibleService projectTaskResponsibleService;

    @Autowired
    private ProjectProcessTimeService projectProcessTimeService;

    @Autowired
    private ProjectProcessService projectProcessService;

    @Autowired
    private ProjectManagerService projectManagerService;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private MyTaskService myTaskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectTaskExportService projectTaskExportService;

    @Autowired
    private ProjectMemberService projectMemberService;

    private static final Logger log = LoggerFactory.getLogger(ProjectTaskController.class);

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }


    /**
     * `
     * 方法描述： 保存任务时间(约定合同进间，计划进度时间 )
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/saveProjectProcessTime", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveProjectProcessTime(@RequestBody ProjectProcessTimeDTO processTimeDTO) throws Exception {
        processTimeDTO.setAccountId(this.currentUserId);
        processTimeDTO.setCompanyId(this.currentCompanyId);
        processTimeDTO.setCurrentCompanyId(this.currentCompanyId);
        return projectProcessTimeService.saveOrUpdateProjectProcessTime(processTimeDTO);


    }

    /**
     * 方法描述： 删除任务时间(约定合同进间，计划进度时间 )
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/saveProjectProcessTime/{id}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage delProjectProcessTime(@PathVariable String id) throws Exception {
        int res = projectProcessTimeService.delProjectProcessTime(id);
        if (res > 0) {
            return this.ajaxResponseSuccess();
        } else {
            return this.ajaxResponseError("查询异常，请重新刷新！");
        }

    }


    /**
     * 方法描述： 获取变更记录(参数{targetId，type}）
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/getChangeTimeList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getChangeTimeList(@RequestBody Map<String, Object> param) throws Exception {
        param.put("companyId",this.currentCompanyId);
        return this.ajaxResponseSuccess().setData(projectProcessTimeService.getProjectProcessTimeList(param));
    }


    /**
     * 方法描述： 判断我的人是否存在
     * param  （taskId.)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/checkMyTask/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage checkMyTask(@PathVariable("taskId") String taskId) throws Exception {
        MyTaskEntity myTaskEntity = myTaskService.selectById(taskId);
        if (!"0".equals(myTaskEntity.getStatus())) {
            return this.ajaxResponseError("该任务不存在！");
        }
        return this.ajaxResponseSuccess();
    }

    /**
     * 方法描述： 移交经营负责人或者项目负责人
     * param  （projectId,companyId,type，companyUserId)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/updateProjectManager", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage updateProjectManager(@RequestBody Map<String, Object> param) throws Exception {
        param.put("currentCompanyId", this.currentCompanyId);
        param.put("accountId", this.currentUserId);
        return projectManagerService.updateProjectManager(param);
    }


    /**
     * 方法描述： 移交项目负责人
     * param  （dto）
     * 作   者： MaoSF
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/transferTaskDesigner", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage transferTaskDesigner(@RequestBody TransferTaskDesignerDTO dto) throws Exception {
        dto.setCurrentCompanyId(this.currentCompanyId);
        dto.setAccountId(this.currentUserId);
        return projectManagerService.transferTaskDesinger(dto);
    }

    /**
     * 方法描述： 设置助理（type=1：经营助理，type=2：设计助理）
     * param  （projectId,companyId,type，companyUserId)
     * 作   者： MaoSF
     * 日   期：2017/11/22 16:18
     */
    @RequestMapping(value = "/updateProjectAssistant", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage updateProjectAssistant(@RequestBody Map<String, Object> param) throws Exception {
        param.put("currentCompanyId", this.currentCompanyId);
        param.put("accountId", this.currentUserId);
        return projectManagerService.updateProjectAssistant(param);
    }

    /**
     * 方法描述： 获取公司list
     * param  （projectId,companyId,type，companyUserId)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/getProjectTaskCoopateCompany", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectTaskCoopateCompany(@RequestBody Map<String, Object> param) throws Exception {
        param.put("companyId", this.currentCompanyId);
        param.put("accountId", this.currentUserId);
        //查询乙方
        ProjectManagerDataDTO partB = projectTaskService.getProjectTaskPartBCompany(param);
        List<ProjectManagerDataDTO> projectDesignContentShowList = projectTaskService.getProjectTaskCoopateCompany(param);
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("projectDesignContentShowList", projectDesignContentShowList);
        resultMap.put("partB", partB);
        return this.ajaxResponseSuccess().setData(resultMap);
    }

    /**
     * 方法描述：获取经营界面数据
     * param  （projectId)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/getOperatorList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getOperatorList(@RequestBody Map<String, Object> param) throws Exception {
        String companyId = this.currentCompanyId;
        String projectId = param.get("projectId").toString();
        List<ProjectDesignTaskShow> projectDesignContentShowList = projectTaskService.getOperatingTaskShowList(companyId, projectId, this.currentCompanyUserId);
        param.put("companyId", this.currentCompanyId);
        param.put("accountId", this.currentUserId);
        param.put("type", "1");
        List<ProjectManagerDataDTO> projectManagerDataDTOList = projectTaskService.getProjectTaskCoopateCompany(param);
        //查询乙方
        ProjectManagerDataDTO partB = projectTaskService.getProjectTaskPartBCompany(param);
        ProjectEntity projectEntity = projectService.selectById(projectId);
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("projectManagerDataDTOList", projectManagerDataDTOList);
        resultMap.put("projectDesignContentShowList", projectDesignContentShowList);
        resultMap.put("partB", partB);
        resultMap.put("projectCreateBy", projectEntity.getCreateBy());
        resultMap.put("projectCompanyId", projectEntity.getCompanyId());

        return this.ajaxResponseSuccess().setData(resultMap);
    }


    /**
     * 方法描述：获取生产界面数据
     * param  （projectId)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/getDesignTaskList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getDesignTaskList(@RequestBody QueryProjectTaskDTO query) throws Exception {
        //
        if(StringUtil.isNullOrEmpty(query.getCompanyId())){
            query.setCompanyId(this.currentCompanyId);
        }
        query.setCurrentCompanyId(this.currentCompanyId);
        query.setCurrentCompanyUserId(this.currentCompanyUserId);
        query.setAccountId(this.currentUserId);
        ProductTaskInfoDTO data = projectTaskService.getProductTaskInfo(query);
        return this.ajaxResponseSuccess().setData(data);
    }

    /**
     * @author  张成亮
     * @date    2018/7/12
     * @description     获取生产安排标签列表
     **/
    @RequestMapping(value = "/listDesignTaskTab", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage listDesignTaskTab(@RequestBody QueryProjectTaskDTO query) throws Exception {
        ProjectProductTaskGroupInfoDTO data = projectTaskService.listDesignTaskTab(query);
        return AjaxMessage.succeed(data);
    }


    /**
     * 方法描述：保存经营签发，或生产分解
     * 更改签发任务
     * 如果任务已发布，需更改任务类型为未发布，并且不能影响已发布的签发任务信息
     * param  （dto)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/saveTaskIssuing", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveTaskIssuing(@RequestBody SaveProjectTaskDTO dto) throws Exception {
        if (!(dto.getTaskType() == 0 || dto.getTaskType() == 5)) {
            if(!projectTaskService.isEditIssueTask(dto.getProjectId(),this.currentCompanyId,this.currentUserId)){
                throw new UnauthorizedException();
            }
        }
        dto.setAccountId(this.currentUserId);
        dto.setCurrentCompanyId(this.currentCompanyId);
        return projectTaskService.saveProjectTask2(dto);
    }


    /**
     * 方法描述：任务签发查询公司
     * param  （dto)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/getIssueTaskCompany", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getIssueTaskCompany(@RequestBody Map<String, Object> param) throws Exception {
        return this.projectTaskService.getIssueTaskCompany((String) param.get("taskId"), (String) param.get("projectId"), this.currentCompanyId);
    }


    /**
     * 方法描述：验证是否已经签发给该组织
     * param  （dto)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/validateIssueTaskCompany", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage validateIssueTaskCompany(@RequestBody Map<String, Object> param) throws Exception {
        param.put("currentCompanyId", this.currentCompanyId);
        return this.projectTaskService.validateIssueTaskCompany(param);
    }


    /**
     * 方法描述：移交任务负责人
     * param  （dto)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/transferTaskResponse", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage transferTaskResponse(@RequestBody ProjectTaskResponsibleDTO projectTaskResponsibleDTO) throws Exception {
        projectTaskResponsibleDTO.setAccountId(this.currentUserId);
        projectTaskResponsibleDTO.setCurrentCompanyId(this.currentCompanyId);
        return projectTaskResponsibleService.transferTaskResponse(projectTaskResponsibleDTO);
    }

    /**
     * 方法描述：编辑任务名称
     * param  （dto)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/updateTaskInfo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage updateTaskInfo(@RequestBody ProjectTaskEntity projectTaskEntity) throws Exception {
        projectTaskEntity.setUpdateBy(currentUserId);
        projectTaskEntity.setUpdateDate(new Date());
        return projectTaskService.updateByTaskId(projectTaskEntity);
    }


    /**
     * 方法描述：根据拼音匹配人员信息
     * param  （dto)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/getUserByKeyWord", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getUserByKeyWord(@RequestBody Map<String, Object> param) throws Exception {
        return this.ajaxResponseSuccess().setData(companyUserService.getUserByKeyWord(param));
    }


    /**
     * 方法描述：保存流程节点
     * param  （dto)
     * 作   者： TangY
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/saveOrUpdateProcess", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateProcess(@RequestBody ProjectProcessDTO dto) throws Exception {
        return projectProcessService.saveOrUpdateProcess(dto);
    }


    /**
     * 方法描述：删除任务
     * param  （dto)
     * 作   者： MaoSF
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/deleteProjectTask", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    //@RequiresPermissions(value = {"project_manager","design_manager"}, logical = Logical.OR)
    public AjaxMessage deleteProjectTask(@RequestBody Map<String, Object> param) throws Exception {
        AjaxMessage msg = new AjaxMessage();
        try {
            if (null != param.get("idList")) {
                List<String> paramId = (List<String>) param.get("idList");
                for (String id : paramId) {
                    msg = this.projectTaskService.deleteProjectTaskById(id, this.currentUserId, this.currentCompanyId);
                }
                return msg;
            } else if (!StringUtil.isNullOrEmpty(param.get("id"))) {
                return this.projectTaskService.deleteProjectTaskById((String) param.get("id"), this.currentUserId, this.currentCompanyId);
            } else {
                return AjaxMessage.failed("无效的任务");
            }
        } catch (Exception e) {
            //添加事物回滚
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxMessage.failed("无效的任务");
        }
    }

    /**
     * 方法描述：移交设计负责人请求数据
     * param  （projectId)
     * 作   者： MaoSF
     * 日   期：2016/8/9 16:18
     */
    @RequestMapping(value = "/getProjectTaskForChangeDesigner", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectTaskForChangeDesigner(@RequestBody Map<String, Object> param) throws Exception {

        String designPersonId = (String) param.get("designPersonId");//之前是当前人移交，现在是由经营人移交，应获取原设计负责人的任务
        if (null != designPersonId && !"".equals(designPersonId)) {
            param.put("companyUserId", designPersonId);
        }
        return this.projectTaskService.getProjectTaskForChangeDesigner(param);
    }

    /**
     * 方法描述：导出任务列表
     * 作   者： ZhangChengliang
     * 日   期：2017/4/24
     * param  （projectId)
     */
    @RequestMapping(value = "/exportTaskList/{projectId}/{type}", method = RequestMethod.GET)
    public void exportMyTaskList(HttpServletResponse response,
                                 @PathVariable("projectId") String projectId, @PathVariable("type") Integer type) throws Exception {
        ProjectTaskExportDTO dto = new ProjectTaskExportDTO();
        dto.setProjectId(projectId);
        dto.setType(type);
        dto.setCompanyId(currentCompanyId);
        dto.setCompanyUserId(currentCompanyUserId);
        dto.setDestFileName("export_" + DateUtils.date2Str(DateUtils.yyyyMMdd) + ".xls");
        dto.setTemplateFileName(new TxtFileUtil().getWebRoot() + "assets/template/task/template.xls");

        projectTaskExportService.exportDownloadResource(response, dto);
    }

    /**
     * 获取签发任务列表
     */
    @RequestMapping(value = "getProjectIssueTaskList/{projectId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getProjectIssueTaskList(@PathVariable("projectId") String projectId) throws Exception {
        QueryProjectTaskDTO query = new QueryProjectTaskDTO();
        query.setCompanyId(this.currentCompanyId);
        query.setCurrentCompanyId(this.currentCompanyId);
        query.setProjectId(projectId);
        return AjaxMessage.succeed(this.projectTaskService.getProjectIssueTaskList(query));
    }

    /**
     * 方法描述：获取签发任务所有界面元素信息
     * 作   者： ZCL
     * 日   期：2017/5/15
     * param  （projectId) 项目ID
     */
    @RequestMapping(value = "getIssueInfo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getIssueInfo(@RequestBody QueryProjectTaskDTO query) throws Exception {
        if(StringUtil.isNullOrEmpty(query.getCompanyId())){
            query.setCompanyId(this.currentCompanyId);
        }
        query.setCurrentCompanyId(this.currentCompanyId);
        query.setAccountId(this.currentUserId);
        return AjaxMessage.succeed(this.projectTaskService.getIssueInfo(query));
    }

    /**
     * 方法描述：发布签发任务
     * 复制任务到正式签发任务，同时保存相应的日期更改历史、项目经营人等信息，并把状态改为已发布
     * 作   者： ZCL
     * 日   期：2017/5/15
     * param  dto  要更改的任务信息，包括任务起止时间
     */
    @RequestMapping(value = "publishIssueTask", method = RequestMethod.POST)
    @ResponseBody
    //@RequiresPermissions(value = {"project_manager"}, logical = Logical.OR)
    public AjaxMessage publishIssueTask(@RequestBody Map<String, Object> map) throws Exception {
        if(!this.projectTaskService.isEditIssueTask((String)map.get("projectId"),currentCompanyId,currentUserId)){
            throw new UnauthorizedException();
        }
        return projectTaskService.publishIssueTask((List<String>) map.get("idList"), currentUserId, currentCompanyId);
    }

    /**
     * 方法描述：发布生产任务任务
     * 设计人员，计划时间等信息，并把状态改为已发布
     * 作   者： MaoSF
     * 日   期：2017/6/23
     * param  dto  要更改的任务信息，包括任务起止时间
     */
    @RequestMapping(value = "publishProductTask", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage publishProductTask(@RequestBody Map<String, Object> map) throws Exception {
        return projectTaskService.publishProductTask((List<String>) map.get("idList"), currentUserId, currentCompanyId);
    }

    /**
     * 方法描述：获取生产总览列表
     * 作   者： wrb
     * 日   期：2017/5/17
     * param  {projectId:项目ID}
     */
    @RequestMapping(value = "getProductTaskOverview/{projectId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getProductTaskOverview(@PathVariable("projectId") String projectId) throws Exception {
        QueryProjectTaskDTO query = new QueryProjectTaskDTO();
        query.setProjectId(projectId);
        query.setCurrentCompanyUserId(this.currentCompanyUserId);
        //l老的业务接口
//        List<ProjectProductTaskDTO> list = this.projectTaskService.getProductTaskOverview(query);
        //修改新业务，展示生产安排
        List<ProjectProducttaskViewDTO> list = this.projectTaskService.getProductTaskOverviewNew(query);
        return AjaxMessage.succeed(list);
    }

    /**
     * 方法描述：获取项目立项人、经营负责人、项目负责人
     * 作   者： wrb
     * 日   期：2017/5/18
     * param  {projectId:项目ID}
     */
    @RequestMapping(value = "getProjectInfoForTask/{projectId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getProjectInfoForTask(@PathVariable("projectId") String projectId) throws Exception {
        return AjaxMessage.succeed(this.projectTaskService.getProjectInfoForTask(projectId));
    }

    /**
     * 方法描述：签发总览（版本1.0）
     * 作   者： ZCL
     * 日   期：2017/5/17
     * param  {projectId:项目ID}
     */
    @RequestMapping(value = "getIssueTaskOverview/{projectId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage getIssueTaskOverview(@PathVariable("projectId") String projectId) throws Exception {
        // List<ProjectIssueTaskOverviewDTO> lst = this.projectTaskService.getProjectTaskIssueOverview(projectId);
        List<ProjectIssueTaskOverviewDTO> lst = this.projectTaskService.listProjectTaskIssueOverview(projectId, currentCompanyId);
        return AjaxMessage.succeed(lst);
    }

    /**
     * 方法描述：签发总览(版本2.0）
     * 作   者： ZCL
     * 日   期：2017/8/28
     * param  {projectId:项目ID}
     */
    @RequestMapping(value = "listIssueTaskOverview/{projectId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage listIssueTaskOverview(@PathVariable("projectId") String projectId) throws Exception {
        List<ProjectIssueTaskOverviewDTO> lst = this.projectTaskService.listProjectTaskIssueOverview(projectId, currentCompanyId);
        return AjaxMessage.succeed(lst);
    }

    /**
     * 方法描述：重新激活一个任务
     * 作   者： ZCL
     * 日   期：2017/5/20
     * param  {taskId:任务ID}
     */
    @RequestMapping(value = "activeTask/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage activeTask(@PathVariable("taskId") String taskId) throws Exception {
        MyTaskActiveRequestDTO request = new MyTaskActiveRequestDTO();
        request.setMyTaskId(taskId);
        request.setCompanyId(currentCompanyId);
        request.setUserId(currentUserId);
        request.setCurrentCompanyUserId(currentCompanyUserId);
        String fail = myTaskService.activeMyTask(request);
        return (fail.equals("激活成功")) ? AjaxMessage.succeed("操作成功") : AjaxMessage.failed(fail);
    }

    /**
     * 方法：交换两个任务排序位置
     * 作者：ZCL
     * 日期：2017/8/29
     * 参数：申请交换的任务的编号和排序编号
     */
    @RequestMapping(value = "exchangeTask", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage exchangeTask(@RequestBody List<TaskSequencingDTO> taskList) throws Exception {
        assert ((taskList != null) && (taskList.size() >= 2)) : "exchangeTask 参数错误";
        projectTaskService.exchangeTask(taskList, currentUserId);
        return AjaxMessage.succeed(null);
    }

    /**
     * 方法：任务负责人确认完成任务
     * 作者：ZCL
     * 日期：2017/8/29
     * 参数：申请完成的任务的编号
     */
    @RequestMapping(value = "completeTask", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage completeTask(@RequestBody HandleMyTaskDTO completeTaskDTO) throws Exception {
        String taskId = completeTaskDTO.getTaskId();
        String status = completeTaskDTO.getStatus();
        completeTaskDTO.setCurrentCompanyId(currentCompanyId);
        completeTaskDTO.setCurrentCompanyUserId(currentCompanyUserId);
        completeTaskDTO.setAccountId(currentUserId);
        assert (taskId != null) : "completeTask 参数错误";
        //判断是否需要先确认
        if("0".equals(status)){
            //taskId
            MyTaskActiveRequestDTO dto = new MyTaskActiveRequestDTO();
            dto.setCompanyId(this.currentCompanyId);
            dto.setCurrentCompanyUserId(this.currentCompanyUserId);
            dto.setTaskId(taskId);
            dto.setUserId(this.currentUserId);
            String msg = myTaskService.activeMyTask2(dto);
            if("激活成功".equals(msg)){
                return AjaxMessage.succeed("激活成功");
            }
          return   AjaxMessage.failed("操作失败");
        }
        AjaxMessage msg = projectTaskService.completeTask(completeTaskDTO);
        return (msg != null) ? msg : AjaxMessage.failed("操作失败");
    }

    /**
     * 生产页面，点击设校审按钮，单独完成接口
     */
    @RequestMapping(value = "/updateCompleteTask/{id}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage updateCompleteTask(@PathVariable("id") String id) {
        assert (id != null) : "completeTask 参数错误";
        AjaxMessage msg = new AjaxMessage();
        //获取task_id
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("id", id);//此值为projectMember中的id
            param.put("status", "0");
            String myTaskId = this.myTaskService.getCompleteTaskId(param);
            if (null != myTaskId) {
                msg = myTaskService.handleMyTask(new HandleMyTaskDTO(myTaskId, "", "", this.currentUserId));
            }else {
                //如果是设计分解的任务,由于前端传递的参数为projectMember的id，为共用此接口，添加一下代码
                ProjectMemberEntity member = this.projectMemberService.getProjectMemberById(id);
                if(member!=null && member.getMemberType() == ProjectMemberType.PROJECT_DESIGNER){
                    return projectProcessService.completeProjectProcessNode(member.getProjectId(),member.getCompanyId(),member.getTargetId(),member.getNodeId(),currentUserId);
                }
                //msg = projectTaskService.completeTask(id, currentUserId,"1",currentCompanyUserId);
            }
        } catch (Exception e) {
            AjaxMessage.failed("操作失败");
        }
        return (msg != null) ? msg : AjaxMessage.failed("操作失败");
    }

    /**
     * 生产页面，单点激活按钮
     */
    @RequestMapping(value = "/activeProjectTask/{id}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxMessage activeProjectTask(@PathVariable("id") String id) {
        assert (id != null) : "completeTask 参数错误";
        MyTaskActiveRequestDTO request = new MyTaskActiveRequestDTO();
        String fail = "";
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("id", id);
            param.put("status", "1");
            String myTaskId = this.myTaskService.getCompleteTaskId(param);
            if (null != myTaskId) {
                request.setMyTaskId(myTaskId);
                request.setCompanyId(currentCompanyId);
                request.setUserId(currentUserId);
                request.setCurrentCompanyUserId(currentCompanyUserId);
                fail = myTaskService.activeMyTask(request);
            }
        } catch (Exception e) {
            AjaxMessage.failed("操作失败");
        }
        return (fail.equals("激活成功")) ? AjaxMessage.succeed("操作成功") : AjaxMessage.failed(fail);
    }
}
