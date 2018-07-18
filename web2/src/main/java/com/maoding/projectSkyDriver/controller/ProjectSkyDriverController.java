package com.maoding.projectSkyDriver.controller;

import com.maoding.core.base.controller.BaseController;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.NetFileType;
import com.maoding.core.constant.RoleConst;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.message.entity.MessageEntity;
import com.maoding.message.service.MessageService;
import com.maoding.mytask.service.MyTaskService;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import com.maoding.project.service.ProjectService;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.projectmember.dto.ProjectMemberDTO;
import com.maoding.task.dto.ProjectDesignTaskShow;
import com.maoding.task.entity.ProjectTaskEntity;
import com.maoding.task.service.ProjectTaskService;
import com.mysql.jdbc.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ProjectConstructController
 * 描    述 : 建设单位Controller
 * 作    者 : LY
 * 日    期 : 2016/7/22-9:52
 */
@Controller
@RequestMapping("iWork/projectSkyDriver")
public class ProjectSkyDriverController extends BaseController {
    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MyTaskService myTaskService;

    @ModelAttribute
    public void before() {
        this.currentUserId = this.getFromSession("userId", String.class);
        this.currentCompanyId = this.getFromSession("companyId", String.class);
        this.currentCompanyUserId = this.getFromSession("companyUserId", String.class);
    }

    /**
     * 根据保存文件夹信息
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/saveOrUpdateFileMaster"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateFileMaster(@RequestBody ProjectSkyDriveDTO projectSkyDriveDTO) throws Exception {
        projectSkyDriveDTO.setAccountId(this.currentUserId);
        return projectSkyDriverService.saveOrUpdateFileMaster(projectSkyDriveDTO);
    }


    /**
     * 根据保存文件信息
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/saveOrUpdateFile"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage saveOrUpdateFile(@RequestBody ProjectSkyDriveDTO projectSkyDriveDTO) throws Exception {
        projectSkyDriveDTO.setAccountId(this.currentUserId);
        projectSkyDriveDTO.setCurrentCompanyId(this.currentCompanyId);
        return projectSkyDriverService.saveFileMessage(projectSkyDriveDTO);
    }

    /**
     * 重命名
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/rename"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage rename(@RequestBody ProjectSkyDriveRenameDTO dto) throws Exception {
        dto.setAccountId(this.currentUserId);
        return projectSkyDriverService.rename(dto);
    }

    /**
     * 根据projectId获取文件夹和文件
     *
     * @param paraMap(projectId,pid)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/getSkyDriverByProject"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getSkyDriverByProject(@RequestBody Map<String, Object> paraMap) throws Exception {
        paraMap.put("accountId", this.currentUserId);
        paraMap.put("companyId", this.currentCompanyId);
        paraMap.put("currentCompanyUserId", this.currentCompanyUserId);
        //前端传递的文件类型，先保留，查询时不需要传递
        paraMap.put("type", null);
        return projectSkyDriverService.getSkyDriveByParam(paraMap);
    }

    /**
     * 根据projectId获取文件夹和文件
     *
     * @param paraMap(projectId,pid)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/getSkyDriverByProjectList"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getSkyDriverByProjectList(@RequestBody Map<String, Object> paraMap) throws Exception {
        paraMap.put("accountId", this.currentUserId);
        paraMap.put("companyId", this.currentCompanyId);
        paraMap.put("currentCompanyUserId", this.currentCompanyUserId);
        //前端传递的文件类型，先保留，查询时不需要传递
        paraMap.put("type", null);
        return projectSkyDriverService.getSkyDriveByParamList(paraMap);
    }


    /**
     * 我们的项目-项目文档
     *
     * @param paraMap(projectId,pid)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/getMyProjectSkyDriveByParam"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMyProjectSkyDriveByParam(@RequestBody Map<String, Object> paraMap) throws Exception {
        paraMap.put("accountId", this.currentUserId);
        paraMap.put("companyId", this.currentCompanyId);
        paraMap.put("currentCompanyUserId", this.currentCompanyUserId);
        //前端传递的文件类型，先保留，查询时不需要传递
        paraMap.put("type", null);
        return projectSkyDriverService.getMyProjectSkyDriveByParam(paraMap);
    }

    /**
     * 根据projectId获取文件夹和文件
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/initProjectFile"}, method = RequestMethod.GET)
    @ResponseBody
    public void initProjectFile() throws Exception {
        projectSkyDriverService.initProjectFile();
    }


    /**
     * 根据删除文件或者文件夹
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/deleteSysDrive/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.PROJECT_EDIT}, logical = Logical.OR)
    public AjaxMessage deleteSysDrive(@PathVariable("id") String id) throws Exception {
        ProjectSkyDriveEntity projectSkyDriveEntity = projectSkyDriverService.selectById(id);
        if ("0".equals(projectSkyDriveEntity.getIsCustomize())) {
            return this.ajaxResponseError("非自定义目录不能删除！");
        }
        return projectSkyDriverService.deleteSysDrive(id, this.currentUserId);
    }

    /**
     * 方法描述：批量删除文件或者文件夹
     * 作   者：DongLiu
     * 日   期：2018/1/16 18:22
     *
     * @param
     * @return
     */
    @RequestMapping(value = {"/deleteSysDriveByIds"}, method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.PROJECT_EDIT}, logical = Logical.OR)
    public AjaxMessage deleteSysDriveByIds(@RequestBody Map<String, Object> param) throws Exception {
        List<String> ids = (List) param.get("ids");
        int status = this.projectSkyDriverService.deleteSysDriveByIds(ids);
        if (status > 0) {
            return AjaxMessage.succeed("删除成功");
        } else {
            return AjaxMessage.succeed("删除失败");
        }
    }

    /**
     * 方法描述：项目文档列表第一层项目名称
     * 作   者：DongLiu
     * 日   期：2018/1/6 16:12
     *
     * @param ：
     * @return
     */
    @RequestMapping(value = "/getProjectsDocuments", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.PROJECT_EDIT}, logical = Logical.OR)
    public AjaxMessage getProjectsDocuments(@RequestBody Map<String, Object> param) throws Exception {
        String companyId = this.currentCompanyId;
//        String companyId = "4d0431400f3e41668ececd918ffe2cd9";//测试
        if (null == companyId || "".equals(companyId)) {
            return this.ajaxResponseSuccess();
        }
        String companyUserId = (String) param.get("companyUserId");
        if (null == companyUserId || "".equals(companyUserId)) {
            param.put("companyUserId", this.currentUserId);
        }
        param.put("relationCompany", companyId);

        String type = "";
        if (StringUtil.isNullOrEmpty(param.get("type"))) {//如果type为空，则设置为0
            type = "0";
        } else {
            type = param.get("type").toString();
        }
        if (!StringUtil.isNullOrEmpty(param.get("orgId"))) {
            param.put("companyId", param.get("orgId").toString());
        }
        param.put("type", type);
        param = projectService.getProcessingProjectsByPage(param);
        Map<String, Object> returnData = new HashMap<>();
        if (null != param) {
            List<Object> obj = (List<Object>) param.get("data");
            List<ProjectTableDTO> tableDTOS = new ArrayList<ProjectTableDTO>();
            for (int i = 0; i < obj.size(); i++) {
                ProjectTableDTO dto = (ProjectTableDTO) obj.get(i);
                dto.setChildId(true);
                tableDTOS.add(dto);
            }
            returnData.put("tableDTOS", tableDTOS);
            returnData.put("uploadFlag", 0);
        }
        return this.ajaxResponseSuccess().setData(returnData);
    }

    /**
     * 方法描述：我的项目-项目文档目录结构接口
     * 作   者：DongLiu
     * 日   期：2018/1/11 17:46
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getMyProjectsDocuments", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getMyProjectsDocuments(@RequestBody Map<String, Object> param) throws Exception {
        ProjectEntity projectEntity = projectService.selectById(param.get("id"));
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", projectEntity);
        returnData.put("uploadFlag", 0);
        returnData.put("childId", true);
        return this.ajaxResponseSuccess().setData(returnData);
    }

    /**
     * 方法描述：项目文档列表第二层
     * 作   者：DongLiu
     * 日   期：2018/1/6 17:01
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getProjectsDocumentsForTwo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.PROJECT_EDIT}, logical = Logical.OR)
    public AjaxMessage getProjectsDocumentsForTwo(@RequestBody Map<String, Object> param) throws Exception {
        if (null == param.get("companyId") || null == param.get("projectId")) {
            return AjaxMessage.failed(null).setInfo("没有指定组织");
        }
        List<ProjectSkyDriveEntity> entities = null;
        try {
            entities = projectSkyDriverService.getProjectSkyDriveEntityById(param);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getProjectsDocumentsForTwo" + e.getMessage());
        }
        return this.ajaxResponseSuccess().setData(entities);
    }

    /**
     * 方法描述：发送归档文件，获取通知人
     * 作   者：DongLiu
     * 日   期：2018/1/12 16:46
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getArchivedFileNotifier", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.PROJECT_EDIT}, logical = Logical.OR)
    public AjaxMessage getArchivedFileNotifier(@RequestBody Map<String, Object> param) throws Exception {

        String companyId = param.get("companyId").toString();
        String projectId = param.get("projectId").toString();
        String taskId = param.get("taskId").toString();

        Map<String, Object> data = new HashMap<>();
        List<ProjectDesignUser> defaultName = new ArrayList<ProjectDesignUser>();
        List<ProjectDesignUser> selectName = new ArrayList<ProjectDesignUser>();
        List<ProjectDesignUser> selAllName = new ArrayList<ProjectDesignUser>();
        List<ProjectDesignUser> allNames = new ArrayList<ProjectDesignUser>();
        List<String> ids = new ArrayList<String>();
        try {
            List<ProjectDesignTaskShow> taskShows = projectTaskService.getProjectDesignTaskList(companyId, projectId, this.currentCompanyUserId);
            getAllNames(taskId,defaultName,selectName,ids,taskShows);
            allNames.addAll(selectName);
            allNames.addAll(defaultName);
            //去重复
            List<ProjectDesignUser> newList = new ArrayList<ProjectDesignUser>();
            List<ProjectDesignUser> newSelAllName = new ArrayList<ProjectDesignUser>();
            Set<ProjectDesignUser> personSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
            Set<ProjectDesignUser> personSet1 = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
            personSet.addAll(allNames);
            newList.addAll(personSet);
            newSelAllName.addAll(personSet1);
            data.put("defaultName", newList);
            data.put("selectName", newList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.ajaxResponseSuccess().setData(data);
    }

    /**
     * 方法描述：从taskShows内取出任务编号为taskId及其下属任务的所有参与人员，包括任务负责人和设校审人员，放入defaultName和selectName队列内
     *          其中任务编号为taskId的参与人员放入defaultName列表中，下属任务参与人员放入selectName列表中
     *          并且，把下属任务id放入ids队列
     *          任务负责人在ProjectDesignTaskShow.design属性内
     *          设校审在ProjectDesignTaskShow.designerList.userList属性内
     *          输出队列的id对应参与人员的companyUserId,userName对应参与人员的userName
     * 作者：Zhangchengliang
     * 日期：2018/5/5
     */
    private void getAllNames(String taskId, List<ProjectDesignUser> defaultName, List<ProjectDesignUser> selectName, List<String> ids, List<ProjectDesignTaskShow> taskShows) {
        for (ProjectDesignTaskShow taskShow : taskShows) {
            //只在任务是taskId或是taskId的下属任务才添加
            if (taskShow.getTaskPath().contains(taskId)) {
                //添加任务负责人
                ProjectMemberDTO taskLeader = taskShow.getDesigner();
                ProjectDesignUser leader = new ProjectDesignUser();
                leader.setId(taskLeader.getCompanyUserId());
                leader.setUserName(taskLeader.getCompanyUserName());
                if (taskShow.getId().equals(taskId)) {
                    defaultName.add(leader);
                } else {
                    selectName.add(leader);
                    ids.add(taskShow.getId());
                }

                //添加设校审人员
                List<ProjectTaskProcessNodeDTO> taskRoleList = taskShow.getDesignersList();
                for (ProjectTaskProcessNodeDTO taskRole : taskRoleList) {
                    List<ProjectDesignUser> taskMemberList = taskRole.getUserList();
                    for (ProjectDesignUser taskMember : taskMemberList) {
                        ProjectDesignUser member = new ProjectDesignUser();
                        member.setId(taskMember.getCompanyUserId());
                        member.setUserName(taskMember.getUserName());
                        if (taskShow.getId().equals(taskId)) {
                            defaultName.add(member);
                        } else {
                            selectName.add(member);
                            ids.add(taskShow.getId());
                        }
                    }
                }
            }
        }
    }

    /**
     * 方法描述：发送归档通知
     * 作   者：DongLiu
     * 日   期：2018/1/13 15:44
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/sendarchivedFileNotifier", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = {RoleConst.PROJECT_EDIT}, logical = Logical.OR)
    public AjaxMessage sendarchivedFileNotifier(@RequestBody Map<String, Object> param) throws Exception {
        try {
            //原来的代码使用taskEntity和修改后的map存储传入的参数，现在改为DeliverEditDTO
            DeliverEditDTO request = new DeliverEditDTO();
            request.setId((String) param.get("id"));
            request.setProjectId((String) param.get("projectId"));
            request.setCompanyId((String) param.get("companyId"));
            request.setIssueId((String) param.get("targetId"));
            request.setCreateBy((String) param.get("userId"));
            request.setName((String) param.get("taskName"));
            request.setType(NetFileType.DIRECTORY_SEND_ARCHIVE_NOTICE);
            request.setDescription((String) param.get("remarks"));
            if (!ObjectUtils.isEmpty(param.get("deadline"))) {
                request.setEndTime(DateUtils.parseDate((String) param.get("deadline"), "yyyy-MM-dd"));
            }
            request.setChangedResponseList(createResponseEditListFrom((List<Map<String,Object>>) param.get("userArr")));

            //***********原来的代码*****************/
            //创建归档通知文件夹
            ProjectTaskEntity taskEntity = new ProjectTaskEntity();
            //原有代码必须要有id，为了和以前代码兼容，在id为空时生成一个id
            String id = (String) param.get("id");
            if (StringUtils.isNullOrEmpty(id)){
                id = StringUtil.buildUUID();
            }
            taskEntity.setTaskPid(id);
            //保存原有id
            param.put("oldId", id);
            List<String> ids = new ArrayList<>();
            ids.add(id);
            param.put("id", ids);
            List<ProjectSkyDriveEntity> entities = projectSkyDriverService.getDirectoryDTOList(param);
            taskEntity.setTaskType(SystemParameters.TASK_TYPE_ISSUE);
            taskEntity.setProjectId((String) param.get("projectId"));
            taskEntity.setCompanyId((String) param.get("companyId"));
            taskEntity.setTaskName((String) param.get("taskName"));
            taskEntity.setSeq(null != entities ? entities.size() + 1 : 0);
            taskEntity.setIsOperaterTask(0);
            taskEntity.setType(NetFileType.DIRECTORY_SEND_ARCHIVE_NOTICE);
            taskEntity.setFileSize(0);
            //***********原来的代码*****************/

            //创建交付目录
            String nodeId = this.projectSkyDriverService.createDeliverDir(taskEntity,request);
            //创建并保存交付任务，包括发起的交付任务，各个负责人的交付任务和上传任务
            request.setNodeId(nodeId);
            myTaskService.saveDeliverTask(request);

            //为每个负责人发送一条消息
            List<MessageEntity> messageList = messageService.createDeliverChangedMessageListFrom(request);
            messageService.sendMessage(messageList);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("sendarchivedFileNotifier is fail" + e.getMessage());
            return AjaxMessage.failed("数据异常");
        }
        return AjaxMessage.succeed("发送成功");
    }

    private List<ResponseEditDTO> createResponseEditListFrom(List<Map<String,Object>> userArr){
        List<ResponseEditDTO> responseEditList = new ArrayList<>();
        userArr.forEach(user->{
            ResponseEditDTO responseEditRequest = new ResponseEditDTO();
            responseEditRequest.setId((String) user.get("id"));
            responseEditRequest.setName((String) user.get("name"));
            responseEditRequest.setIsSelected(
                    getWithDefault((String) user.get("isSelected"),"1"));
            responseEditList.add(responseEditRequest);
        });
        return responseEditList;
    }

    private String getWithDefault(String value,String defaultValue){
        return (StringUtils.isNullOrEmpty(value)) ? defaultValue : value;
    }

    private MessageEntity sendMessageEntity(Map<String, Object> param) {
        MessageEntity m = new MessageEntity();
        try {
            m.setTargetId((String) param.get("targetId"));
            m.setProjectId((String) param.get("projectId"));
            m.setCompanyId((String) param.get("companyId"));
            m.setUserId((String) param.get("newUserId"));
            m.setSendCompanyId((String) param.get("companyId"));
            m.setCreateBy(this.currentUserId);
            m.setTaskName((String) param.get("taskName"));
            m.setDeadline((String) param.get("deadline"));
            m.setRemarks((String) param.get("remarks"));
            m.setUserName((String) param.get("userName"));
            m.setMessageType(SystemParameters.MESSAGE_TYPE_FILING_NOTICE);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("sendMessageEntity is fail:" + e.getMessage());
        }
        return m;
    }

    /**
     * 方法描述：本组织发送归档通知确认按钮接口
     * 作   者：DongLiu
     * 日   期：2018/1/17 15:32
     *
     * @return
     * @param:projectId,companyId,id,pid
     */
    @RequestMapping(value = "/notarizeArchivedFileNotifier", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage notarizeArchivedFileNotifier(@RequestBody Map<String, Object> param) throws Exception {
        List<ProjectSkyDriveEntity> entities = null;
        ProjectSkyDriveEntity entity = null;
        ProjectSkyDriveEntity entity1 = null;
        String pid = "";
        try {
            //获取第一层的归档文件夹
            entity1 = projectSkyDriverService.selectById(param.get("id"));
            // 获取对应的成果目录
            entity = projectSkyDriverService.getResultsFolder(param);
            pid = sendArchivedFile(entity, entity1);
            entities = this.projectSkyDriverService.getProjectFileFirst(param);
            //复制文件
            //如果有文件夹的数据，取出id
            List<String> ids = new ArrayList<>();
            Map<String, String> para = new HashMap<>();
            for (ProjectSkyDriveEntity driveEntity : entities) {
                if (1 != driveEntity.getType()) {
                    driveEntity.setType(30);//成果文件
                }
                String id = createFileNotifier(pid, driveEntity);
                if (0 == driveEntity.getType() || 30 == driveEntity.getType()) {
                    ids.add(driveEntity.getId());
                    para.put(driveEntity.getId(), id);//原始id，新建id
                }
            }
            //判断是否有文件夹，如果有文件夹继续查询文件夹下的内容
            if (ids.size() > 0) {
                createFileNotifierEvery(ids, param.get("projectId").toString(), param.get("companyId").toString(), para);
            }
        } catch (Exception e) {
            return AjaxMessage.failed("数据异常");
        }
        return AjaxMessage.succeed("发送成功");
    }

    /**
     * 方法描述：乙方发送成果文件给甲方确认按钮
     * 作   者：DongLiu
     * 日   期：2018/1/19 14:28
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/sendOwner", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage sendOwner(@RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> data = new HashMap<>();
        try {
            //甲方组织id
            ProjectSkyDriveEntity owner = projectSkyDriverService.getOwnerProject(param);
            if (null != owner) {
                data.put("projectId", param.get("projectId"));
                data.put("companyId", param.get("companyId"));
                data.put("fromCompanyId", owner.getFromCompanyId());
                data.put("id", param.get("id"));
                data.put("pid", param.get("pid"));
                data.put("companyName", owner.getCompanyName());
                data.put("fileName", owner.getFileName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.ajaxResponseSuccess().setData(data);
    }

    /**
     * 方法描述：乙方发送成果文件给甲方接口
     * 作   者：DongLiu
     * 日   期：2018/1/18 11:05
     *
     * @return
     * @param:projectId,companyId,id,pid
     */
    @RequestMapping(value = "/sendOwnerProjectFile", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage sendOwnerProjectFile(@RequestBody Map<String, Object> param) throws Exception {
        List<ProjectSkyDriveEntity> entities = null;
        try {
            //获取甲方文件夹
            Map<String, Object> paramOwner = new HashMap<>();
            paramOwner.put("fileName", param.get("fileName"));
            paramOwner.put("companyId", param.get("fromCompanyId"));
            paramOwner.put("projectId", param.get("projectId"));
            ProjectSkyDriveEntity ownerFile = null;
            ownerFile = projectSkyDriverService.getOwnerProjectFile(paramOwner);
            if (null == ownerFile) {
                ownerFile = projectSkyDriverService.getOwnerProjectFileByPid(paramOwner);
            }
            if (null != ownerFile) {
                entities = this.projectSkyDriverService.getOwnerProjectFileFirst(param);
                List<String> ids = new ArrayList<>();
                Map<String, String> para = new HashMap<>();
                for (ProjectSkyDriveEntity driveEntity : entities) {
                    driveEntity.setCompanyId(param.get("fromCompanyId").toString());
                    if (1 != driveEntity.getType()) {
                        driveEntity.setType(NetFileType.DIRECTORY_SEND_ARCHIVE_NOTICE);//发送给甲方的归档文件夹中type=50
                    }
                    String id = createFileNotifier(ownerFile.getId(), driveEntity);
                    if (0 == driveEntity.getType() || 40 == driveEntity.getType() || 50 == driveEntity.getType()) {
                        ids.add(driveEntity.getId());
                        //原始id，新建id
                        para.put(driveEntity.getId(), id);
                    }
                }
                if (ids.size() > 0) {
                    createOwnerProjectFile(ids, param.get("projectId").toString(), param.get("companyId").toString(), para, param.get("fromCompanyId").toString());
                }
            }
        } catch (Exception e) {
            return AjaxMessage.failed("数据异常");
        }
        return AjaxMessage.succeed("发送成功");
    }

    /**
     * 方法描述：发送成果文件
     * 作   者：DongLiu
     * 日   期：2018/1/18 16:03
     *
     * @param
     * @return
     */
    private void createOwnerProjectFile(List<String> ids, String projectId, String companyId, Map<String, String> para, String fromCompanyId) {
        //根据ids查询多目录，进行创建
        Map<String, Object> param = new HashMap<>();
        param.put("ids", ids);
        param.put("projectId", projectId);
        param.put("companyId", companyId);
        List<ProjectSkyDriveEntity> entities = projectSkyDriverService.getProjectFileSecond(param);
        List<String> newIds = new ArrayList<>();
        Map<String, String> newPara = new HashMap<>();
        for (ProjectSkyDriveEntity entity : entities) {
            ProjectTaskEntity taskEntity = new ProjectTaskEntity();
            taskEntity.setTaskType(SystemParameters.TASK_TYPE_ISSUE);
            taskEntity.setProjectId(entity.getProjectId());
            taskEntity.setCompanyId(fromCompanyId);
            taskEntity.setTaskName(entity.getFileName());
            taskEntity.setSeq(entity.getParam4());
            taskEntity.setIsOperaterTask(0);
            taskEntity.setTaskPid(para.get(entity.getPid()));
            taskEntity.setType(entity.getType());
            taskEntity.setId(entity.getTaskId());
            taskEntity.setFilePath(entity.getFilePath());
            taskEntity.setFileSize(0 != entity.getFileSize() ? entity.getFileSize() : 0);
            String id = this.projectSkyDriverService.createSendarchivedFileNotifier(taskEntity);
            if (0 == entity.getType() || 30 == entity.getType() || 40 == entity.getType() || 50 == entity.getType()) {
                newIds.add(entity.getId());
                newPara.put(entity.getId(), id);
            }
        }
        if (newIds.size() > 0) {
            createOwnerProjectFile(newIds, projectId, companyId, newPara, fromCompanyId);
        }
    }

    /**
     * 方法描述：归档通知
     * 作   者：DongLiu
     * 日   期：2018/1/18 16:02
     *
     * @param
     * @return
     */
    private void createFileNotifierEvery(List<String> ids, String projectId, String companyId, Map<String, String> para) {
        //根据ids查询多目录，进行创建
        Map<String, Object> param = new HashMap<>();
        param.put("ids", ids);
        param.put("projectId", projectId);
        param.put("companyId", companyId);
        List<ProjectSkyDriveEntity> entities = projectSkyDriverService.getProjectFileSecond(param);
        List<String> newIds = new ArrayList<>();
        Map<String, String> newPara = new HashMap<>();
        for (ProjectSkyDriveEntity entity : entities) {
            ProjectTaskEntity taskEntity = new ProjectTaskEntity();
            taskEntity.setTaskType(SystemParameters.TASK_TYPE_ISSUE);
            taskEntity.setProjectId(entity.getProjectId());
            taskEntity.setCompanyId(entity.getCompanyId());
            taskEntity.setTaskName(entity.getFileName());
            taskEntity.setSeq(entity.getParam4());
            taskEntity.setIsOperaterTask(0);
            taskEntity.setTaskPid(para.get(entity.getPid()));
            taskEntity.setType(entity.getType());
            taskEntity.setId(entity.getTaskId());
            taskEntity.setFilePath(entity.getFilePath());
            taskEntity.setFileSize(0 != entity.getFileSize() ? entity.getFileSize() : 0);
            String id = this.projectSkyDriverService.createSendarchivedFileNotifier(taskEntity);
            if (0 == entity.getType() || 30 == entity.getType() || 40 == entity.getType() || 50 == entity.getType()) {
                newIds.add(entity.getId());
                newPara.put(entity.getId(), id);
            }
        }
        if (newIds.size() > 0) {
            createFileNotifierEvery(newIds, projectId, companyId, newPara);
        }
    }

    private String createFileNotifier(String pid, ProjectSkyDriveEntity driveEntity) {
        ProjectTaskEntity taskEntity = new ProjectTaskEntity();
        taskEntity.setTaskType(SystemParameters.TASK_TYPE_ISSUE);
        taskEntity.setProjectId(driveEntity.getProjectId());
        taskEntity.setCompanyId(driveEntity.getCompanyId());
        taskEntity.setTaskName(driveEntity.getFileName());
        taskEntity.setSeq(driveEntity.getParam4());
        taskEntity.setIsOperaterTask(0);
        taskEntity.setTaskPid(pid);
        taskEntity.setType(driveEntity.getType());
        taskEntity.setId(driveEntity.getTaskId());
        taskEntity.setFilePath(driveEntity.getFilePath());
        taskEntity.setFileSize(0 != driveEntity.getFileSize() ? driveEntity.getFileSize() : 0);
        String id = this.projectSkyDriverService.createSendarchivedFileNotifier(taskEntity);
        return id;
    }

    /**
     * 方法描述：本组织发送归档通知到成果文件，type=30
     * 作   者：DongLiu
     * 日   期：2018/1/31 14:53
     *
     * @param
     * @return
     */
    private String sendArchivedFile(ProjectSkyDriveEntity entity, ProjectSkyDriveEntity entity1) {
        ProjectTaskEntity taskEntity1 = new ProjectTaskEntity();
        taskEntity1.setTaskType(SystemParameters.TASK_TYPE_ISSUE);
        taskEntity1.setProjectId(entity1.getProjectId());
        taskEntity1.setCompanyId(entity1.getCompanyId());
        taskEntity1.setTaskName(entity1.getFileName());
        taskEntity1.setSeq(entity1.getParam4());
        taskEntity1.setIsOperaterTask(0);
        taskEntity1.setTaskPid(entity.getId());
        taskEntity1.setType(30);
        taskEntity1.setFileSize(0 != entity.getFileSize() ? entity.getFileSize() : 0);
        return this.projectSkyDriverService.createSendarchivedFileNotifier(taskEntity1);
    }

    /**
     * 方法描述：项目文档，全文搜索功能
     * 作   者：DongLiu
     * 日   期：2018/3/28 9:39
     *
     * @return
     * @param： projectId 或 fileName
     */
    @RequestMapping(value = "/getProjectFileByFileName", method = RequestMethod.POST)
    @ResponseBody
    public AjaxMessage getProjectFileByFileName(@RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> data = new HashMap<>();
        List<ProjectSkyDriveEntity> entities = null;
        param.put("companyId", this.currentCompanyId);
        Integer total = 0;
        try {
            entities = projectSkyDriverService.getProjectFileByFileName(param);
            total = projectSkyDriverService.getProjectFileTotil(param);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getProjectFileByFileName is fail->" + e.getMessage());
        }
        data.put("data", entities);
        data.put("total", total);
        return this.ajaxResponseSuccess().setData(data);
    }

}
