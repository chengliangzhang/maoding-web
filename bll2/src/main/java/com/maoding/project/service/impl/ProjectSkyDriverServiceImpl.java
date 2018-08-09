package com.maoding.project.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.dto.BaseShowDTO;
import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.NetFileType;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.HttpUtils;
import com.maoding.core.util.JsonUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.StringUtils;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.dto.CompanyDataDTO;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dao.ProjectSkyDriverDao;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectSkyDriveEntity;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.projectcost.dto.ProjectCostEditDTO;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.projectmember.service.ProjectMemberService;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.dao.ProjectTaskRelationDao;
import com.maoding.task.dto.ProjectIssueTaskDTO;
import com.maoding.task.dto.QueryProjectTaskDTO;
import com.maoding.task.dto.SaveProjectTaskDTO;
import com.maoding.task.entity.ProjectTaskEntity;
import com.maoding.task.service.ProjectTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectSkyDriverServiceImpl
 * 类描述：项目文件磁盘
 * 作    者：MaoSF
 * 日    期：2016/12/18 16:50
 */
@Service("projectSkyDriverService")
public class ProjectSkyDriverServiceImpl extends GenericService<ProjectSkyDriveEntity> implements ProjectSkyDriverService {

    protected final Logger log= LoggerFactory.getLogger(getClass());

    @Autowired
    private ProjectSkyDriverDao projectSkyDriverDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private ProjectTaskDao projectTaskDao;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ProjectTaskRelationDao projectTaskRelationDao;


    @Autowired
    private ProjectMemberService projectMemberService;

    @Value("${company}")
    private String companyUrl;

    @Value("${server.url}")
    protected String serverUrl;

    @Value("${fastdfs.fileCenterUrl}")
    protected String fileCenterUrl;

    @Value("${fastdfs.url}")
    protected String fastdfsUrl;


    @Override
    public AjaxMessage saveOrUpdateFileMaster(ProjectSkyDriveDTO dto) throws Exception {

        //判断是否存在
        ProjectSkyDriveEntity isExist = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(dto.getPid(), dto.getFileName(), dto.getProjectId(), dto.getCompanyId());

        if (StringUtil.isNullOrEmpty(dto.getId())) {
            if (isExist != null) {
                return AjaxMessage.failed(null).setInfo(dto.getFileName() + "已经存在");
            }
            dto.setId(StringUtil.buildUUID());
            if (StringUtil.isNullOrEmpty(dto.getPid())) {
                dto.setSkyDrivePath(dto.getId());
            } else {
                ProjectSkyDriveEntity parent = projectSkyDriverDao.selectById(dto.getPid());
                if (parent != null) {
                    dto.setSkyDrivePath(parent.getSkyDrivePath() + "-" + dto.getId());
                }
            }
            ProjectSkyDriveEntity entity = new ProjectSkyDriveEntity();
            entity.setCreateBy(dto.getAccountId());
            entity.setUpdateBy(dto.getAccountId());
            entity.setStatus("0");
            BaseDTO.copyFields(dto, entity);
            if (isExist == null) {
                projectSkyDriverDao.insert(entity);
            }
        } else {
            if (isExist != null && !isExist.getId().equals(dto.getId())) {
                return AjaxMessage.failed(null).setInfo(dto.getFileName() + "已经存在");
            }
            ProjectSkyDriveEntity entity = new ProjectSkyDriveEntity();
            BaseDTO.copyFields(dto, entity);
            entity.setUpdateBy(dto.getAccountId());
            this.projectSkyDriverDao.updateById(entity);
        }

        return AjaxMessage.succeed(null).setInfo("保存成功");
    }

    /**
     * 方法描述：删除文件或文件夹（单个删除、若文件夹下有其他的文件夹或文件，不可删除）
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param id
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage deleteSysDrive(String id, String accountId) throws Exception {
        ProjectSkyDriveEntity entity = this.projectSkyDriverDao.selectById(id);
        if (!accountId.equals(entity.getCreateBy())) {
            return AjaxMessage.error(null).setInfo("您无权限删除");
        }
        Map<String, Object> paraMap = new HashMap<String, Object>();
        if (entity != null) {
            if (1 == entity.getType()) {
                //删除原有的文件
                paraMap.put("group", entity.getFileGroup());
                paraMap.put("path", entity.getFilePath());
                String result = HttpUtils.sendPost(fileCenterUrl + "/fastDelete", paraMap, false);
                Map<String, Object> resultMap = JsonUtils.json2map(result);
                if ("0".equals(resultMap.get("code"))) {
                    projectSkyDriverDao.deleteById(entity.getId());
                } else {
                    return new AjaxMessage().setCode("1").setInfo("删除失败");
                }
            } else {
                //查询是否有子文件
                List<ProjectSkyDriveEntity> list = this.projectSkyDriverDao.getSkyDriveByPid(entity.getId(), entity.getProjectId());
                if (CollectionUtils.isEmpty(list)) {
                    this.projectSkyDriverDao.deleteById(id);
                } else {
                    return AjaxMessage.failed(null).setInfo("请删除子文件、文件夹再删除");
                }
            }
        }

        return AjaxMessage.succeed(null).setInfo("删除成功");
    }

    @Override
    public int deleteSysDriveByIds(List<String> ids) throws Exception {
        return projectSkyDriverDao.deleteSysDriveByIds(ids);
    }

    /**
     * 方法描述：查询文件
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param map
     * @param:
     * @return:
     */
    public AjaxMessage getSkyDriveByParam(Map<String, Object> map) throws Exception {
        Map<String, Object> returnData = new HashMap<>();
        String accountId = (String) map.get("accountId");
        String companyId = (String) map.get("companyId");
        String pid = (String) map.get("pid");
        if (null == pid) {
            List<String> fileNames = new ArrayList<>();
            fileNames.add("设计依据");
            fileNames.add("设计文件");
            fileNames.add("交付文件");
            map.put("fileNames", fileNames);
        }
        List<ProjectSkyDriveEntity> list = this.projectSkyDriverDao.getSkyDriveByParam(map);
        List<ProjectSkyDriveData> resultList = new ArrayList<>();
        for (ProjectSkyDriveEntity entity : list) {
            setResultList(accountId, companyId, resultList, entity);
        }
        Map<String, Object> para = new HashMap<>();
        para.put("projectId", 0 < list.size() ? list.get(0).getProjectId() : null);
        para.put("fileName", "设计文件");
        para.put("companyId", companyId);
        List<ProjectSkyDriveEntity> entities = projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndFileName(para);
        for (ProjectSkyDriveData projectSkyDriveData : resultList) {
            if ("设计依据".equals(projectSkyDriveData.getFileName())) {
                projectSkyDriveData.setChildId(true);
            }
        }
        if (0 < entities.size()) {
            for (ProjectSkyDriveData projectSkyDriveData : resultList) {
                if ("设计文件".equals(projectSkyDriveData.getFileName()) || "交付文件".equals(projectSkyDriveData.getFileName())) {
                    projectSkyDriveData.setChildId(true);
                }
            }
        }
        //设计文件查看是否有子类
        for (ProjectSkyDriveData entity : resultList) {
            Map<String, Object> param = new HashMap<>();
            param.put("projectId", entity.getProjectId());
            param.put("fileName", entity.getFileName());
            param.put("companyId", companyId);
            List<ProjectSkyDriveEntity> driveEntities = projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndFileName(param);
            if (0 < driveEntities.size()) {
                for (ProjectSkyDriveEntity driveEntity : driveEntities) {
                    if ((null != driveEntity.getPid() && null != entity.getId()) && NetFileType.FILE != driveEntity.getType() && driveEntity.getPid().equals(entity.getId())) {
                        entity.setChildId(true);
                    }
                }
            }
        }

        returnData.put("list", resultList);
        returnData.put("uploadFlag", 0);
        if (null != map.get("pid")) {
            ProjectSkyDriveEntity parent = this.selectById(map.get("pid"));
            if (!StringUtil.isNullOrEmpty(parent.getSkyDrivePath())) {
                String[] path = parent.getSkyDrivePath().split("-");
                if (path.length > 1) {
                    String rootId = path[0];
                    ProjectSkyDriveEntity root = this.selectById(rootId);
                    if ("交付文件".equals(root.getFileName()) || "设计文件".equals(root.getFileName())) {
                        if (isWritable(parent,accountId,companyId)) {
                            returnData.put("uploadFlag", 1);
                        }
                    } else {
                        returnData.put("uploadFlag", 1);
                    }
                }else {
                    if (parent!=null && "设计依据".equals(parent.getFileName())){
                        returnData.put("uploadFlag", 1);
                    }
                }
            }
        }
        return AjaxMessage.succeed(returnData);
    }

    private boolean haveSendArchiveNoticeRight(String projectId, String companyId, String accountId) {
        ProjectMemberEntity designerManager = projectMemberService.getDesignManager(projectId, companyId);
        if ((designerManager != null) && (StringUtil.isSame(accountId, designerManager.getAccountId()))) return true;
        ProjectMemberEntity designerAssistant = projectMemberService.getDesignManagerAssistant(projectId, companyId);
        if ((designerAssistant != null) && (StringUtil.isSame(accountId, designerAssistant.getAccountId())))
            return true;
        return false;
    }

    @Override
    public AjaxMessage getSkyDriveByParamList(Map<String, Object> map) throws Exception {
        Map<String, Object> returnData = new HashMap<>();
        String accountId = (String) map.get("accountId");
        String companyId = (String) map.get("companyId");
        String currentCompanyUserId = (String) map.get("currentCompanyUserId");
        String pid = (String) map.get("pid");
        if (null == pid) {
            List<String> fileNames = new ArrayList<>();
            fileNames.add("设计依据");
            fileNames.add("设计文件");
            fileNames.add("交付文件");
            map.put("fileNames", fileNames);
        }
        List<ProjectSkyDriveEntity> list = this.projectSkyDriverDao.getSkyDriveByParamList(map);
        List<ProjectSkyDriveData> resultList = new ArrayList<>();
        for (ProjectSkyDriveEntity entity : list) {
            setResultList(accountId, companyId, resultList, entity);
        }
        Map<String, Object> para = new HashMap<>();
        String projectId = (String) map.get("projectId");
        para.put("projectId", 0 < list.size() ? list.get(0).getProjectId() : null);
        para.put("fileName", "设计文件");
        para.put("companyId", companyId);
        List<ProjectSkyDriveEntity> entities = projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndFileName(para);
        for (ProjectSkyDriveData projectSkyDriveData : resultList) {
            if ("设计依据".equals(projectSkyDriveData.getFileName())) {
                projectSkyDriveData.setChildId(true);
            }
        }
        if (0 < entities.size()) {
            for (ProjectSkyDriveData projectSkyDriveData : resultList) {
                if ("设计文件".equals(projectSkyDriveData.getFileName()) || "交付文件".equals(projectSkyDriveData.getFileName())) {
                    projectSkyDriveData.setChildId(true);
                }
            }
        }
        //设计文件查看是否有子类
        for (ProjectSkyDriveData entity : resultList) {
            Map<String, Object> param = new HashMap<>();
            param.put("projectId", entity.getProjectId());
            param.put("fileName", entity.getFileName());
            param.put("companyId", companyId);
            List<ProjectSkyDriveEntity> driveEntities = projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndFileName(param);
            if (0 < driveEntities.size()) {
                for (ProjectSkyDriveEntity driveEntity : driveEntities) {
                    if ((null != driveEntity.getPid() && null != entity.getId()) && NetFileType.FILE != driveEntity.getType() && driveEntity.getPid().equals(entity.getId())) {
                        entity.setChildId(true);
                    }
                }
            }
        }

        returnData.put("list", resultList);
        returnData.put("uploadFlag", 0);
        if (null != map.get("pid")) {
            ProjectSkyDriveEntity parent = this.selectById(map.get("pid"));
            if (!StringUtil.isNullOrEmpty(parent.getSkyDrivePath())) {
                String[] path = parent.getSkyDrivePath().split("-");
                if (path.length > 1) {
                    String rootId = path[0];
                    ProjectSkyDriveEntity root = this.selectById(rootId);
                    if ("交付文件".equals(root.getFileName()) || "设计文件".equals(root.getFileName())) {
                        if (isWritable(parent,accountId,companyId)) {
                            returnData.put("uploadFlag", 1);
                        }
                    } else {
                        returnData.put("uploadFlag", 1);
                    }
                }else {
                    if (parent!=null && "设计依据".equals(parent.getFileName())){
                        returnData.put("uploadFlag", 1);
                    }
                }
            }
        }

        //设置是否有发送归档通知的权限
        returnData.put("haveNoticeRight", haveSendArchiveNoticeRight(projectId, companyId, accountId));
        return AjaxMessage.succeed(returnData);
    }


    //判断文件是否可写，如果操作者和文件创建者相同，并且选择了创建文件时的组织，则可写，否则不可写
    private boolean isWritable(ProjectSkyDriveEntity file, String accountId, String companyId){
        return (StringUtils.isNotEmpty(accountId) && StringUtils.isNotEmpty(companyId)
                && ((accountId.equals(file.getCreateBy())) || StringUtils.isEmpty(file.getCreateBy()))
                && ((companyId.equals(file.getCompanyId())) || StringUtils.isEmpty(file.getCompanyId())));
    }

    //转换布尔值为字符串，0-false,1-true
    private String toString(boolean b){
        return b ? "1" : "0";
    }

    private void setResultList(String accountId, String companyId, List<ProjectSkyDriveData> resultList, ProjectSkyDriveEntity entity) throws Exception {
        long fileSize = 0;
        //判断是否为签发过来的目录
        Map<String, Object> param = new HashMap<>();
        List<String> ids = new ArrayList<String>();
        param.put("projectId", entity.getProjectId());
        param.put("companyId", entity.getCompanyId());
        ids.add(entity.getId());
        param.put("id", ids);
        param.put("pid", entity.getPid());
        ProjectSkyDriveEntity owner = projectSkyDriverDao.getOwnerProject(param);
        List<ProjectSkyDriveEntity> entities = projectSkyDriverDao.getDirectoryDTOList(param);
        List<String> newIds = new ArrayList<>();
        for (ProjectSkyDriveEntity driveEntity : entities) {
            if (NetFileType.FILE == driveEntity.getType()) {
                fileSize += driveEntity.getFileSize();
            } else if (NetFileType.DIRECTORY == driveEntity.getType() ||
                    NetFileType.DIRECTORY_ACHIEVEMENT == driveEntity.getType() || NetFileType.DIRECTORY_ARCHIVE_NOTICE == driveEntity.getType() ||
                    NetFileType.DIRECTORY_SEND_ARCHIVE_NOTICE == driveEntity.getType() || NetFileType.DIRECTORY_ARCHIVE_NOTICE_PARTYB == driveEntity.getType()) {
                newIds.add(driveEntity.getId());
            }
        }
        if (0 < newIds.size()) {
            param.put("id", newIds);
            fileSize = setFileSize(fileSize, param, newIds);
        }
        ProjectSkyDriveData driveData = new ProjectSkyDriveData();
        BaseDTO.copyFields(entity, driveData);
        if (0 == driveData.getFileSize()) {
            driveData.setFileSize(fileSize);
        }
        if (isWritable(entity,accountId,companyId)) {
            driveData.setEditFlag(1);
        }

        //设置companyName
        CompanyEntity companyEntity = this.companyDao.selectById(entity.getCompanyId());
        if (companyEntity != null) {
            driveData.setCompanyName(companyEntity.getAliasName());
        }
        //设置是否可提交成果文件
        if (null != owner && !entity.getCompanyId().equals(owner.getFromCompanyId())) {
            driveData.setSendResults("1");
        }
        resultList.add(driveData);
    }

    /**
     * 方法描述：获取成果文件目录
     * 作   者：DongLiu
     * 日   期：2018/1/19 11:08
     */
    @Override
    public AjaxMessage getMyProjectSkyDriveByParam(Map<String, Object> map) throws Exception {
        Map<String, Object> returnData = new HashMap<>();
        String accountId = (String) map.get("accountId");
        String companyId = (String) map.get("companyId");
        String pid = (String) map.get("pid");
        if (null == pid) {
            List<String> fileNames = new ArrayList<>();
            fileNames.add("设计依据");
            fileNames.add("设计文件");
            fileNames.add("交付文件");
            map.put("fileNames", fileNames);
        }
        List<ProjectSkyDriveEntity> list = this.projectSkyDriverDao.getSkyDriveByParam(map);
        List<ProjectSkyDriveData> resulList = new ArrayList<>();
        for (ProjectSkyDriveEntity entity : list) {
            setResultList(accountId, companyId, resulList, entity);
        }
        Map<String, Object> para = new HashMap<>();
        para.put("projectId", 0 < list.size() ? list.get(0).getProjectId() : null);
        para.put("fileName", "交付文件");
        para.put("companyId", companyId);
        List<ProjectSkyDriveEntity> entities = projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndFileName(para);
        for (ProjectSkyDriveData projectSkyDriveData : resulList) {
            if ("设计依据".equals(projectSkyDriveData.getFileName())) {
                projectSkyDriveData.setChildId(true);
            }
        }
        if (0 < entities.size()) {
            for (ProjectSkyDriveData projectSkyDriveData : resulList) {
                if ("交付文件".equals(projectSkyDriveData.getFileName())) {
                    projectSkyDriveData.setChildId(true);
                }
            }
        }
        //设计文件查看是否有子类
        for (ProjectSkyDriveEntity entity : list) {
            Map<String, Object> param = new HashMap<>();
            param.put("projectId", entity.getProjectId());
            param.put("fileName", entity.getFileName());
            para.put("companyId", companyId);
            List<ProjectSkyDriveEntity> driveEntities = projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndFileName(param);
            if (0 < driveEntities.size()) {
                for (ProjectSkyDriveData projectSkyDriveData : resulList) {
                    if (entity.getFileName().equals(projectSkyDriveData.getFileName())) {
                        projectSkyDriveData.setChildId(true);
                    }
                }
            }
        }
        returnData.put("list", resulList);
        returnData.put("uploadFlag", 0);
        if (null != map.get("pid")) {
            ProjectSkyDriveEntity parent = this.selectById(map.get("pid"));
            if (!StringUtil.isNullOrEmpty(parent.getSkyDrivePath())) {
                String[] path = parent.getSkyDrivePath().split("-");
                if (path.length > 1) {
                    String rootId = path[0];
                    ProjectSkyDriveEntity root = this.selectById(rootId);
                    if ("交付文件".equals(root.getFileName()) || "设计文件".equals(root.getFileName())) {
                        if (isWritable(parent,accountId,companyId)) {
                            returnData.put("uploadFlag", 1);
                        }
                    } else {
                        returnData.put("uploadFlag", 1);
                    }
                }
            }
        }
        return AjaxMessage.succeed(returnData);
    }

    public long setFileSize(long fileSize, Map<String, Object> param, List<String> newIds) {
        List<ProjectSkyDriveEntity> entities = null;
        List<String> ids = new ArrayList<>();
        if (0 < newIds.size()) {
            param.put("ids", newIds);
            entities = projectSkyDriverDao.getDirectoryDTOList(param);
            for (ProjectSkyDriveEntity driveEntity : entities) {
                if (NetFileType.FILE == driveEntity.getType()) {
                    fileSize += driveEntity.getFileSize();
                } else if (NetFileType.DIRECTORY == driveEntity.getType() ||
                        NetFileType.DIRECTORY_ACHIEVEMENT == driveEntity.getType() || NetFileType.DIRECTORY_ARCHIVE_NOTICE == driveEntity.getType() ||
                        NetFileType.DIRECTORY_SEND_ARCHIVE_NOTICE == driveEntity.getType() || NetFileType.DIRECTORY_ARCHIVE_NOTICE_PARTYB == driveEntity.getType()) {
                    ids.add(driveEntity.getId());
                }
            }
        }
        if (0 < ids.size() && null != ids) {
            param.put("id", ids);
            fileSize = setFileSize(fileSize, param, ids);
        }
        return fileSize;
    }

    /**
     * 方法描述：查询文件
     * 作者：MaoSF
     * 日期：2016/12/18
     *
     * @param map
     * @param:
     * @return:
     */
    @Override
    public List<ProjectSkyDriveEntity> getNetFileByParam(Map<String, Object> map) throws Exception {
        return this.projectSkyDriverDao.getNetFileByParam(map);
    }

    public void initProjectFile() {
        List<ProjectEntity> projectEntityList = projectDao.selectAll();

        for (ProjectEntity projectEntity : projectEntityList) {
            this.createProjectFile(projectEntity);
        }
    }

    //创建公司的默认文件夹
    public void createProjectFile(ProjectEntity projectEntity) {
        List<ProjectSkyDriveEntity> parent;
        //创建本公司项目文档目录
        Map<String, Object> param = new HashMap<>();
        param.put("fileName", "设计文件");
        param.put("projectId", projectEntity.getId());
        param.put("companyId", projectEntity.getCompanyId());
        param.put("status", "0");
        parent = this.projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndCompanyId(param);
        if (parent == null || parent.size() == 0) {
            createSkyRoot(projectEntity.getId(),projectEntity.getCompanyId());
        }
        //创建设计组织公司的项目文档目录
        if (!StringUtils.isEmpty(projectEntity.getCompanyBid()) && !projectEntity.getCompanyBid().equals(projectEntity.getCompanyId())) {
            param.put("companyId", projectEntity.getCompanyBid());
            parent = projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndCompanyId(param);
            if (parent == null || parent.size() == 0) {
                createSkyRoot(projectEntity.getId(),projectEntity.getCompanyBid());
            }
        }
    }

    int createProjectFile(String projectId,String companyId){
        List<ProjectSkyDriveEntity> parent;
        //创建本公司项目文档目录
        return createSkyRoot(projectId,companyId);
    }

    private int createSkyRoot(String projectId, String companyId) {
        int result = 0;
        //添加项目文档目录
        int seq0 = 1;
        List<String> rootDirList = SystemParameters.rootList;
        for (String fileName : rootDirList) {
            Map<String, Object> param = new HashMap<>();
            param.put("fileName", fileName);
            param.put("projectId", projectId);
            param.put("companyId", companyId);
            param.put("status", "0");
            List<ProjectSkyDriveEntity> fileList = this.projectSkyDriverDao.getProjectSkyDriveEntityByProjectIdAndCompanyId(param);
            if (CollectionUtils.isEmpty(fileList)) {
                if(!fileName.equals("设计依据")){
                    result = 1;
                }
                ProjectSkyDriveEntity projectSkyDriveEntity = new ProjectSkyDriveEntity();
                projectSkyDriveEntity.setId(StringUtil.buildUUID());
                projectSkyDriveEntity.setCompanyId(companyId);
                projectSkyDriveEntity.setProjectId(projectId);
                projectSkyDriveEntity.setSkyDrivePath(projectSkyDriveEntity.getId());
                projectSkyDriveEntity.setIsCustomize(1);
                projectSkyDriveEntity.setType(0);
                projectSkyDriveEntity.setFileName(fileName);
                projectSkyDriveEntity.setParam4(seq0++);
                projectSkyDriveEntity.setStatus("0");
                projectSkyDriveEntity.setCreateBy("系统创建");
                projectSkyDriverDao.insert(projectSkyDriveEntity);

                //添加项目依据目录下的子目录
                List<String> inputDirList = new ArrayList<>();
                if (!"交付文件".equals(fileName) && !"设计文件".equals(fileName)) {
                    inputDirList = SystemParameters.nodeList2;
                }
                int seq = 1;
                for (String fileName2 : inputDirList) {
                    ProjectSkyDriveEntity projectSkyDrivey = new ProjectSkyDriveEntity();
                    projectSkyDrivey.setId(StringUtil.buildUUID());
                    projectSkyDrivey.setCompanyId(companyId);
                    projectSkyDrivey.setProjectId(projectId);
                    projectSkyDrivey.setPid(projectSkyDriveEntity.getId());
                    projectSkyDrivey.setSkyDrivePath(projectSkyDriveEntity.getId() + "-" + projectSkyDrivey.getId());
                    projectSkyDrivey.setIsCustomize(1);
                    projectSkyDrivey.setType(0);
                    projectSkyDrivey.setFileName(fileName2);
                    projectSkyDrivey.setParam4(seq++);
                    projectSkyDrivey.setStatus("0");
                    projectSkyDrivey.setCreateBy("系统创建");
                    projectSkyDriverDao.insert(projectSkyDrivey);
                }
            }

        }
        return result;
    }

    /**
     * 方法描述：文件信息保存
     * 作   者：LY
     * 日   期：2016/8/11 19:37
     */
    @Override
    public AjaxMessage saveFileMessage(ProjectSkyDriveDTO dto) throws Exception {
        //判断是否存在
        ProjectSkyDriveEntity isExist = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(dto.getPid(), dto.getFileName(), dto.getProjectId(), dto.getCompanyId());
        Map<String, Object> paraMap = new HashMap<String, Object>();
        if (null != isExist) {
            paraMap.put("group", isExist.getFileGroup());
            paraMap.put("path", isExist.getFilePath());
            String result = HttpUtils.sendPost(fileCenterUrl + "/fastDelete", paraMap, false);
            Map<String, Object> resultMap = JsonUtils.json2map(result);
            if ("0".equals(resultMap.get("code"))) {
                projectSkyDriverDao.deleteById(isExist.getId());
            } else {
                return new AjaxMessage().setCode("1").setInfo("上传失败");
            }
        }

        if (StringUtil.isNullOrEmpty(dto.getId())) {
            ProjectSkyDriveEntity entity = new ProjectSkyDriveEntity();
            BaseDTO.copyFields(dto, entity);
            entity.setId(StringUtil.buildUUID());
            entity.setCreateBy(dto.getAccountId());
            entity.setUpdateBy(dto.getAccountId());
            entity.setStatus("0");
            if (StringUtil.isNullOrEmpty(entity.getPid())) {
                entity.setSkyDrivePath(entity.getId());
            } else {
                ProjectSkyDriveEntity parent = projectSkyDriverDao.selectById(entity.getPid());
                if (parent != null) {
                    entity.setSkyDrivePath(parent.getSkyDrivePath() + "-" + entity.getId());
                }
            }
            projectSkyDriverDao.insert(entity);

            return new AjaxMessage().setCode("0").setInfo("上传成功");
        } else {
            ProjectSkyDriveEntity entity = new ProjectSkyDriveEntity();
            BaseDTO.copyFields(dto, entity);
            entity.setUpdateBy(dto.getAccountId());
            this.projectSkyDriverDao.updateById(entity);
            return new AjaxMessage().setCode("0").setInfo("上传成功");
        }
    }

    @Override
    public AjaxMessage rename(ProjectSkyDriveRenameDTO dto) {
        if (this.projectSkyDriverDao.rename(dto) > 0) {
            return new AjaxMessage().setCode("0").setInfo("重命名成功");
        }
        return new AjaxMessage().setCode("0").setInfo("重命名失败");
    }

    /**
     * 方法描述：签发的时候，给该公司创建默认的文件（交付文件中的文件）
     * 作者：MaoSF
     * 日期：2017/4/12
     *
     * @param projectId
     * @param companyId
     * @param:
     * @return:
     */
    @Override
    public AjaxMessage createFileMasterForIssueTask(String projectId, String companyId) {
        List<ProjectSkyDriveEntity> list = this.projectSkyDriverDao.getProjectSkyByCompanyId(projectId, companyId);
        if (CollectionUtils.isEmpty(list)) {
            ProjectSkyDriveEntity parent = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(null, "交付文件", projectId, companyId);
            CompanyEntity companyEntity = this.companyDao.selectById(companyId);
            if (parent != null && companyEntity != null) {
                ProjectSkyDriveEntity projectSkyDrivey = new ProjectSkyDriveEntity();
                projectSkyDrivey.setId(StringUtil.buildUUID());
                projectSkyDrivey.setCompanyId(companyId);
                projectSkyDrivey.setProjectId(projectId);
                projectSkyDrivey.setPid(parent.getId());
                projectSkyDrivey.setSkyDrivePath(parent.getId() + "-" + projectSkyDrivey.getId());
                projectSkyDrivey.setIsCustomize(1);
                projectSkyDrivey.setType(0);
                projectSkyDrivey.setFileName(companyEntity.getCompanyName());
                projectSkyDrivey.setStatus("0");
                projectSkyDriverDao.insert(projectSkyDrivey);
            }
        }
        return null;
    }

    /**
     * 方法描述：经营签发的任务名称默认到文档库中（交付文件中的文件）
     * 作者：MaoSF
     * 日期：2017/4/12
     */
    @Override
    public void createFileMasterForTask(ProjectTaskEntity taskEntity) {
        ProjectSkyDriveEntity parent = null;
        Map<String, Object> map = new HashMap<>();
        String projectId = taskEntity.getProjectId();
        if (StringUtil.isNullOrEmpty(taskEntity.getTaskPid())) {
            parent = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(null, "交付文件", projectId, taskEntity.getCompanyId());
        } else {
            map.put("projectId", taskEntity.getProjectId());
            map.put("fileName", "交付文件");
            map.put("taskId", taskEntity.getTaskPid());
            map.put("companyId", taskEntity.getCompanyId());
            parent = this.projectSkyDriverDao.getSkyDriveByProjectIdAndFileNameAndTaskId(map);
            if (null == parent) {
                parent = this.projectSkyDriverDao.getSkyDriveByFileAndProIdAndComId(map);
            }
            //更新父文件夹
            if (null != parent) {
                map = new HashMap<>();
                map.put("status", 0);
                map.put("id", parent.getId());
                map.put("accountId", taskEntity.getCreateBy());
                projectSkyDriverDao.updateSkyDriveForStatus(map);
            }
        }
        if (parent != null) {
            ProjectSkyDriveEntity projectSkyDrivey = new ProjectSkyDriveEntity();
            projectSkyDrivey.setId(StringUtil.buildUUID());
            projectSkyDrivey.setCompanyId(taskEntity.getCompanyId());
            projectSkyDrivey.setProjectId(projectId);
            projectSkyDrivey.setPid(parent.getId());
            projectSkyDrivey.setSkyDrivePath(parent.getSkyDrivePath() + "-" + projectSkyDrivey.getId());
            projectSkyDrivey.setIsCustomize(1);
            if (null != taskEntity.getType()) {
                projectSkyDrivey.setType(taskEntity.getType());
            } else {
                projectSkyDrivey.setType(NetFileType.DIRECTORY_ACHIEVEMENT);
            }
            projectSkyDrivey.setFileName(taskEntity.getTaskName());
            projectSkyDrivey.setTaskId(taskEntity.getId());
            projectSkyDrivey.setParam4(taskEntity.getSeq());//排序字段
            if (0 == taskEntity.getIsOperaterTask()) {
                projectSkyDrivey.setStatus("0");
            } else {
                projectSkyDrivey.setStatus("1");
            }
            //判断是否存在
            ProjectSkyDriveEntity isExist = this.projectSkyDriverDao.getSkyDriveByPidAndFileName
                    (projectSkyDrivey.getPid(), projectSkyDrivey.getFileName(), projectSkyDrivey.getProjectId(), projectSkyDrivey.getCompanyId());
            if (isExist == null) {
                this.projectSkyDriverDao.insert(projectSkyDrivey);
            } else {
                map = new HashMap<>();
                map.put("status", 0);
                map.put("id", isExist.getId());
                map.put("accountId", taskEntity.getCreateBy());
                projectSkyDriverDao.updateSkyDriveForStatus(map);
            }
        }
    }

    /**
     * 方法描述：经营签发的任务名称默认到文档库中（设计文件）
     * 作   者：DongLiu
     * 日   期：2018/1/8 17:25
     *
     * @param
     * @return
     */
    @Override
    public void createFileMasterForArchivedFile(ProjectTaskEntity taskEntity) {
        ProjectSkyDriveEntity parent = null;
        String projectId = taskEntity.getProjectId();
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNullOrEmpty(taskEntity.getTaskPid())) {
            parent = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(null, "设计文件", projectId, taskEntity.getCompanyId());
        } else {
            map.put("projectId", taskEntity.getProjectId());
            map.put("fileName", "设计文件");
            map.put("taskId", taskEntity.getTaskPid());
            map.put("companyId", taskEntity.getCompanyId());
            parent = this.projectSkyDriverDao.getSkyDriveByProjectIdAndFileNameAndTaskId(map);
            if (null == parent) {
                parent = this.projectSkyDriverDao.getSkyDriveByFileAndProIdAndComId(map);
            }
            //更新父文件夹
            if (null != parent) {
                map = new HashMap<>();
                map.put("status", 0);
                map.put("id", parent.getId());
                map.put("accountId", taskEntity.getCreateBy());
                projectSkyDriverDao.updateSkyDriveForStatus(map);
            }
        }
        if (parent != null) {
            ProjectSkyDriveEntity projectSkyDrivey = new ProjectSkyDriveEntity();
            projectSkyDrivey.setId(StringUtil.buildUUID());
            projectSkyDrivey.setCompanyId(taskEntity.getCompanyId());
            projectSkyDrivey.setProjectId(projectId);
            projectSkyDrivey.setPid(parent.getId());
            projectSkyDrivey.setSkyDrivePath(parent.getSkyDrivePath() + "-" + projectSkyDrivey.getId());
            projectSkyDrivey.setIsCustomize(1);
            if (null != taskEntity.getType()) {
                projectSkyDrivey.setType(taskEntity.getType());
            } else {
                projectSkyDrivey.setType(NetFileType.DIRECTORY_ARCHIVE_NOTICE);
            }
            projectSkyDrivey.setFileName(taskEntity.getTaskName());
            projectSkyDrivey.setTaskId(taskEntity.getId());
            projectSkyDrivey.setParam4(taskEntity.getSeq());//排序字段
            if (0 == taskEntity.getIsOperaterTask()) {
                projectSkyDrivey.setStatus("0");
            } else {
                projectSkyDrivey.setStatus("1");
            }
            //判断是否存在
            ProjectSkyDriveEntity isExist = this.projectSkyDriverDao.getSkyDriveByPidAndFileName
                    (projectSkyDrivey.getPid(), projectSkyDrivey.getFileName(), projectSkyDrivey.getProjectId(), projectSkyDrivey.getCompanyId());
            if (isExist == null) {
                this.projectSkyDriverDao.insert(projectSkyDrivey);
            } else {
                map = new HashMap<>();
                map.put("status", 0);
                map.put("id", isExist.getId());
                map.put("accountId", taskEntity.getCreateBy());
                projectSkyDriverDao.updateSkyDriveForStatus(map);
            }
        }
    }

    /**
     * 方法描述：生产安排创建文件夹（设计文件）
     * 作   者：DongLiu
     * 日   期：2018/1/8 17:25
     *
     * @param
     * @return
     */
    @Override
    public void createFileMasterForProductionFile(ProjectTaskEntity taskEntity) {
        ProjectSkyDriveEntity parent = null;
        String projectId = taskEntity.getProjectId();
        Map<String, Object> map = new HashMap<>();
        map.put("projectId", taskEntity.getProjectId());
        map.put("taskId", taskEntity.getTaskPid());
        map.put("companyId", taskEntity.getCompanyId());
        parent = this.projectSkyDriverDao.getProjectSkyDriveEntityByProductionFile(map);
        if (parent != null) {
            ProjectSkyDriveEntity projectSkyDrivey = new ProjectSkyDriveEntity();
            projectSkyDrivey.setId(StringUtil.buildUUID());
            projectSkyDrivey.setCompanyId(taskEntity.getCompanyId());
            projectSkyDrivey.setProjectId(projectId);
            projectSkyDrivey.setPid(parent.getId());
            projectSkyDrivey.setSkyDrivePath(parent.getSkyDrivePath() + "-" + projectSkyDrivey.getId());
            projectSkyDrivey.setIsCustomize(1);
            projectSkyDrivey.setType(0);
            projectSkyDrivey.setFileName(taskEntity.getTaskName());
            projectSkyDrivey.setTaskId(taskEntity.getId());
            projectSkyDrivey.setParam4(taskEntity.getSeq());//排序字段
            projectSkyDrivey.setStatus("0");
            //判断是否存在
            ProjectSkyDriveEntity isExist = this.projectSkyDriverDao.getSkyDriveByPidAndFileName
                    (projectSkyDrivey.getPid(), projectSkyDrivey.getFileName(), projectSkyDrivey.getProjectId(), projectSkyDrivey.getCompanyId());
            if (isExist == null) {
                this.projectSkyDriverDao.insert(projectSkyDrivey);
            }
        }
    }

    /**
     * 方法描述：建立设计文件夹，type：40
     * 作   者：DongLiu
     * 日   期：2018/1/17 15:27
     */
    @Override
    public String createSendarchivedFileNotifier(ProjectTaskEntity taskEntity) {
        ProjectSkyDriveEntity isExist = this.projectSkyDriverDao.getSkyDriveByPidAndFileName
                (taskEntity.getTaskPid(), taskEntity.getTaskName(), taskEntity.getProjectId(), taskEntity.getCompanyId());
        ProjectSkyDriveEntity projectSkyDrivey = new ProjectSkyDriveEntity();
        String pid = "";
        if (null == isExist) {
            projectSkyDrivey.setId(StringUtil.buildUUID());
            projectSkyDrivey.setCompanyId(taskEntity.getCompanyId());
            projectSkyDrivey.setProjectId(taskEntity.getProjectId());
            projectSkyDrivey.setPid(taskEntity.getTaskPid());
            projectSkyDrivey.setSkyDrivePath(taskEntity.getTaskPid() + "-" + projectSkyDrivey.getId());
            projectSkyDrivey.setIsCustomize(1);
            projectSkyDrivey.setType(taskEntity.getType());
            projectSkyDrivey.setFileName(taskEntity.getTaskName());
            projectSkyDrivey.setTaskId(taskEntity.getId());
            projectSkyDrivey.setParam4(taskEntity.getSeq());//排序字段
            projectSkyDrivey.setStatus("0");
            projectSkyDrivey.setFilePath(taskEntity.getFilePath());
            projectSkyDrivey.setFileSize(0 != taskEntity.getFileSize() ? taskEntity.getFileSize() : 0);
            this.projectSkyDriverDao.insert(projectSkyDrivey);
            pid = projectSkyDrivey.getId();
        }
        if ((null == pid || "".equals(pid)) && null != isExist) {
            pid = isExist.getId();
        }
        return pid;
    }

    /**
     * @param request 交付申请
     * @return 创建或更改交付文件夹
     * @author 张成亮
     * @date 2018/7/16
     * @description 创建的文档的编号
     */
    @Override
    public String createDeliverDir(DeliverEditDTO request) {
        String nodeId = null;

        if (request != null) {
            ProjectTaskEntity issue = projectTaskDao.selectById(request.getIssueId());
            if (issue != null) {
                //查找任务目录
                ProjectSkyDriveEntity rootDir = getDeliverRoot(request);
                ProjectSkyDriveEntity taskDir = getChildByName(rootDir,issue.getTaskName());
                //如果任务目录不存在，创建任务目录
                if (taskDir == null) {
                    taskDir = createTaskDirFrom(rootDir, issue, request.getCreateBy());
                }
                nodeId = taskDir.getId();

                //如果交付名称不为空，则需要创建或修改交付目录
                if (!StringUtils.isEmpty(request.getTaskName())){
                    ProjectSkyDriveEntity deliverDir = getChildByName(taskDir,request.getTaskName());
                    if (deliverDir == null){
                        //创建交付目录
                        deliverDir = createDeliverDirFrom(taskDir,request);
                        projectSkyDriverDao.insert(deliverDir);
                    } else {
                        //更新交付目录
                        projectSkyDriverDao.updateById(deliverDir);
                    }
                    nodeId = deliverDir.getId();
                }
            }
        }

        return nodeId;
    }

    //根据名称查找子目录
    private ProjectSkyDriveEntity getChildByName(ProjectSkyDriveEntity rootDir,String childName) {
        return this.projectSkyDriverDao.getSkyDriveByPidAndFileName
                (rootDir.getId(), childName, rootDir.getProjectId(), rootDir.getCompanyId());
    }


    //查找某公司，某项目的交付文件根目录，如果没有，则创建一个
    private ProjectSkyDriveEntity getDeliverRoot(DeliverEditDTO request){
        final String deliverRoot = "交付文件";
        final int deliverRootOrder = 2;
        ProjectSkyDriveEntity rootDir = getRoot(request.getProjectId(),request.getCompanyId(),deliverRoot);
        if (!isValid(rootDir)){
            rootDir = createRootDir(request.getProjectId(),request.getCompanyId(),deliverRoot,request.getCreateBy(),deliverRootOrder);
        }
        return rootDir;
    }

    //获取名字为fileName的项目根目录
    private ProjectSkyDriveEntity getRoot(String projectId, String companyId, String fileName){
        Map<String,Object> queryRoot = new HashMap<>();
        queryRoot.put("projectId",projectId);
        queryRoot.put("companyId",companyId);
        queryRoot.put("fileName",fileName);
        List<ProjectSkyDriveEntity> rootDirList = projectSkyDriverDao.getProjectSkyDriveEntityById(queryRoot);
        if (!isValid(rootDirList)){
            return null;
        } else {
            ProjectSkyDriveEntity rootDir = rootDirList.get(0);
            rootDir.setProjectId(projectId);
            rootDir.setCompanyId(companyId);
            return rootDir;
        }
    }

    //创建名字为fileName的项目根目录
    private ProjectSkyDriveEntity createRootDir(String projectId, String companyId, String fileName, String createBy, int order){
        ProjectSkyDriveEntity rootDir = new ProjectSkyDriveEntity();
        rootDir.initEntity();
        rootDir.setSkyDrivePath(rootDir.getId());
        rootDir.setCompanyId(companyId);
        rootDir.setProjectId(projectId);
        rootDir.setIsCustomize(1);
        rootDir.setType(0);
        rootDir.setFileName(fileName);
        rootDir.setStatus("0");
        rootDir.setCreateBy(createBy);
        rootDir.setParam4(order);
        projectSkyDriverDao.insert(rootDir);
        return rootDir;
    }

    //判断查询列表的返回值是否有效
    private boolean isValid(List<?> list){
        return (list != null) && (list.size() > 0);
    }

    //判断存储对象是否有效
    private boolean isValid(BaseEntity entity){
        return (entity != null) && (!StringUtils.isEmpty(entity.getId()));
    }

    //使用交付信息创建相应的交付目录信息
    private ProjectSkyDriveEntity createDeliverDirFrom(ProjectSkyDriveEntity parent, DeliverEditDTO request){
        ProjectSkyDriveEntity dir = new ProjectSkyDriveEntity();
        if (StringUtils.isEmpty(request.getId())) {
            dir.initEntity();
        } else {
            dir.setId(request.getId());
            dir.resetUpdateDate();
        }
        dir.setPid(parent.getId());
        dir.setSkyDrivePath(parent.getSkyDrivePath() + "-" + dir.getId());
        dir.setCompanyId(request.getCompanyId());
        dir.setProjectId(request.getProjectId());
        dir.setIsCustomize(1);
        dir.setType(request.getType());
        dir.setFileName(request.getName());
        dir.setTaskId(request.getIssueId());
        dir.setTargetId(request.getIssueId());
        dir.setParam4(1);
        dir.setStatus("0");
        dir.setCreateBy(request.getCreateBy());
        return dir;
    }


    //使用任务信息创建相应的文档目录信息
    private ProjectSkyDriveEntity createTaskDirFrom(ProjectSkyDriveEntity parent, ProjectTaskEntity task, String createBy){
        ProjectSkyDriveEntity dir = new ProjectSkyDriveEntity();
        dir.initEntity();
        dir.setPid(parent.getId());
        dir.setSkyDrivePath(parent.getSkyDrivePath() + "-" + dir.getId());
        dir.setProjectId(parent.getProjectId());
        dir.setCompanyId(parent.getCompanyId());
        dir.setIsCustomize(0);
        dir.setType(0);
        dir.setFileName(task.getTaskName());
        dir.setTaskId(task.getId());
        dir.setTargetId(task.getId());
        dir.setParam4(task.getSeq());
        dir.setStatus("0");
        dir.setFileSize(0);
        dir.setCreateBy(createBy);
        projectSkyDriverDao.insert(dir);
        return dir;
    }

    @Override
    public List<ProjectSkyDriveEntity> getProjectFileFirst(Map<String, Object> map) {
        return projectSkyDriverDao.getProjectFileFirst(map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getProjectFileSecond(Map<String, Object> map) {
        return projectSkyDriverDao.getProjectFileSecond(map);
    }

    /**
     * 方法描述：在交付文件目录中查找与设计文件夹相对应的文件夹
     * 设计方案：先找到本项目的成果目录，再根据归档路径递归查找成果目录下的相应文件夹，直到无法找到对应文件夹为止
     * 作   者：ZhengChengliang
     * 日   期：2018/5/7
     */
    @Override
    public ProjectSkyDriveEntity getResultsFolder(Map<String, Object> map) {
        ProjectSkyDriveEntity result = null;
        assert (map != null);
        String pid = (String)map.get("pid");
        String projectId = (String)map.get("projectId");
        String companyId = (String)map.get("companyId");

        Map<String, Object> query = new HashMap<>();
        query.put("projectId",projectId);
        query.put("fileName","交付文件");
        query.put("pid","-");
        query.put("companyId",companyId);
        List<ProjectSkyDriveEntity> resultList = projectSkyDriverDao.getNetFileByParam(query);
        if ((resultList != null) && (resultList.size() > 0)) {
            result = resultList.get(0);
            List<ProjectSkyDriveEntity> archList = new ArrayList<>();
            for (int i=0; i<100 && !StringUtils.isEmpty(pid); i++) {
                ProjectSkyDriveEntity archEntity = projectSkyDriverDao.selectById(pid);
                if ((archEntity != null) && (!StringUtils.isEmpty(archEntity.getPid()))) {
                    archList.add(archEntity);
                    pid = archEntity.getPid();
                }
            }
            if (archList.size() > 0) {
                ProjectSkyDriveEntity[] archArray = archList.toArray(new ProjectSkyDriveEntity[archList.size()]);
                for (int i = archArray.length-1; i>=0; i--) {
                    query.put("fileName",archArray[i].getFileName());
                    query.put("pid",result.getId());
                    resultList = projectSkyDriverDao.getNetFileByParam(query);
                    if ((resultList != null) && (resultList.size() > 0)) {
                        result = resultList.get(0);
                    } else {
                        break;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public ProjectSkyDriveEntity getOwnerProject(Map<String, Object> map) {
        return this.projectSkyDriverDao.getOwnerProject(map);
    }

    @Override
    public ProjectSkyDriveEntity getSendResults(Map<String, Object> map) {
        return this.projectSkyDriverDao.getSendResults(map);
    }

    @Override
    public ProjectSkyDriveEntity getOwnerProjectFile(Map<String, Object> map) {
        return this.projectSkyDriverDao.getOwnerProjectFile(map);
    }

    @Override
    public ProjectSkyDriveEntity getOwnerProjectFileByPid(Map<String, Object> map) {
        return this.projectSkyDriverDao.getOwnerProjectFileByPid(map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getOwnerProjectFileFirst(Map<String, Object> param) {

        return this.projectSkyDriverDao.getOwnerProjectFileFirst(param);
    }

    @Override
    public List<ProjectSkyDriveEntity> getDirectoryDTOList(Map<String, Object> map) {
        return this.projectSkyDriverDao.getDirectoryDTOList(map);
    }

    /**
     * 项目文档全文搜索
     */
    @Override
    public List<ProjectSkyDriveEntity> getProjectFileByFileName(Map<String, Object> param) {
        if (null != param.get("pageIndex")) {
            int page = (Integer) param.get("pageIndex");
            int pageSize = (Integer) param.get("pageSize");
            param.put("startPage", page * pageSize);
            param.put("endPage", pageSize);
        }
        return this.projectSkyDriverDao.getProjectFileByFileName(param);
    }

    @Override
    public Integer getProjectFileTotil(Map<String, Object> map) {
        return this.projectSkyDriverDao.getProjectFileTotil(map);
    }


    /**
     * 方法描述：根据项目id，父级id，文件名查询
     * 作者：MaoSF
     * 日期：2016/12/18
     */
    @Override
    public ProjectSkyDriveEntity getSkyDriveByTaskId(String taskId) {
        return this.projectSkyDriverDao.getSkyDriveByTaskId(taskId);
    }

    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2017/4/21
     */
    @Override
    public AjaxMessage updateSkyDriveStatus(String taskId, String accountId) throws Exception {

        ProjectSkyDriveEntity driveEntity = this.getSkyDriveByTaskId(taskId);
        if (driveEntity != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("accountId", accountId);
            map.put("skyDrivePath", driveEntity.getId());
            this.projectSkyDriverDao.updateSkyDriveStatus(map);
        }

        return AjaxMessage.succeed(null);
    }

    /**
     * 方法描述：数据迁移使用
     * 作者：MaoSF
     * 日期：2017/5/1
     */
    @Override
    public AjaxMessage initProjectSkyDriver() throws Exception {
        List<ProjectEntity> list = projectDao.selectAll();
        for (ProjectEntity project : list) {
            //处理合作放内容
            List<CompanyDataDTO> companyList = projectTaskRelationDao.getTakePartCompany(project.getId());
            CompanyDataDTO createCompany = new CompanyDataDTO();
            createCompany.setId(project.getCompanyId());
            companyList.add(0,createCompany);
            for(CompanyDataDTO c:companyList){
                //首先创建默认文件
                //1.创建文件夹 (处理立项方内容)
                int i = this.createProjectFile(project.getId(),c.getId());
                if(i==1){//如果系统中有默认文件了，则不处理后面的内容
                    createDefaultForDeliveryFile(project,c.getId(),NetFileType.DIRECTORY_ACHIEVEMENT);
                    createDefaultForDeliveryFile(project,c.getId(),NetFileType.DIRECTORY_ARCHIVE_NOTICE);
                }
            }
        }
        return null;
    }

    private void createDefaultForDeliveryFile(ProjectEntity project,String companyId,Integer fileType) throws Exception{
        String fileName = null;
        if(fileType == NetFileType.DIRECTORY_ACHIEVEMENT){
            fileName = "交付文件";
        }else {
            fileName = "设计文件";
        }
        //查询 父文件 文件
        ProjectSkyDriveEntity parent = this.projectSkyDriverDao.getSkyDriveByPidAndFileName(null, fileName, project.getId(), companyId);
        Map<String,String> filePath = new HashMap<>();//保存文件的path,以免每次都去查找
        //查询签发数据
        QueryProjectTaskDTO query = new QueryProjectTaskDTO();
        query.setProjectId(project.getId());
        query.setCompanyId(companyId);
        List<ProjectIssueTaskDTO> list = projectTaskService.getProjectIssueTaskListForInitFile(query);
        for(ProjectIssueTaskDTO design:list){
            if("0".equals(design.getTaskStatus())){
                String skyDrivePath  = null;
                if(StringUtil.isNullOrEmpty(design.getTaskPid())) {
                    skyDrivePath = parent.getSkyDrivePath();
                }else {
                    skyDrivePath = filePath.get(design.getTaskPid());
                }
                if (skyDrivePath==null){
                    log.error("项目："+project.getId()+"  --"+project.getProjectName()+"  任务名："+design.getBeModifyId());
                    continue;
                }
                ProjectSkyDriveEntity projectSkyDrivey = this.projectSkyDriverDao.getSkyDriveByPidAndFileName
                        (parent.getId(), parent.getFileName(), project.getId(), companyId);
                //如果该文件没有存在，则插入记录
                if(projectSkyDrivey==null){
                    projectSkyDrivey = new ProjectSkyDriveEntity();
                    projectSkyDrivey.setId(StringUtil.buildUUID());
                    projectSkyDrivey.setCompanyId(companyId);
                    projectSkyDrivey.setProjectId(project.getId());
                    projectSkyDrivey.setPid(parent.getId());
                    projectSkyDrivey.setSkyDrivePath(skyDrivePath + "-" + projectSkyDrivey.getId());
                    projectSkyDrivey.setIsCustomize(1);
                    projectSkyDrivey.setType(fileType);
                    projectSkyDrivey.setFileName(design.getTaskName());
                    projectSkyDrivey.setTaskId(design.getBeModifyId());
                    projectSkyDrivey.setParam4(design.getSeq());//排序字段
                    projectSkyDrivey.setStatus("0");
                    projectSkyDrivey.setCreateBy("系统创建");
                    this.projectSkyDriverDao.insert(projectSkyDrivey);
                }
                filePath.put(design.getBeModifyId(),projectSkyDrivey.getSkyDrivePath());
            }
        }
    }

    private void setSkyDriverForTask(String projectId, String taskPid, String pid, String skyDriverPath,Integer fileType) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("taskPid",taskPid);
        List<ProjectTaskEntity> taskList = this.projectTaskDao.getProjectTaskByPid(taskPid);
        for (ProjectTaskEntity taskEntity : taskList) {
            if(taskEntity.getTaskType()==0){
                continue;
            }
            ProjectSkyDriveEntity projectSkyDrivey = new ProjectSkyDriveEntity();
            projectSkyDrivey.setId(StringUtil.buildUUID());
            projectSkyDrivey.setCompanyId(taskEntity.getCompanyId());
            projectSkyDrivey.setProjectId(projectId);
            projectSkyDrivey.setPid(pid);
            projectSkyDrivey.setSkyDrivePath(skyDriverPath + "-" + projectSkyDrivey.getId());
            projectSkyDrivey.setIsCustomize(1);
            projectSkyDrivey.setType(fileType);
            projectSkyDrivey.setFileName(taskEntity.getTaskName());
            projectSkyDrivey.setTaskId(taskEntity.getId());
            projectSkyDrivey.setParam4(taskEntity.getSeq());//排序字段
            //判断是否存在
            projectSkyDrivey.setStatus("0");
            ProjectSkyDriveEntity isExist = this.projectSkyDriverDao.getSkyDriveByPidAndFileName
                    (projectSkyDrivey.getPid(), projectSkyDrivey.getFileName(), projectSkyDrivey.getProjectId(), projectSkyDrivey.getCompanyId());
            if (isExist == null) {
                this.projectSkyDriverDao.insert(projectSkyDrivey);
                isExist = projectSkyDrivey;
            }
            setSkyDriverForTask(projectId, taskEntity.getId(), isExist.getId(), isExist.getSkyDrivePath(),fileType);
        }

    }


    /**
     * 方法描述：获取组织logo地址
     * 作者：MaoSF
     * 日期：2017/6/2
     */
    @Override
    public String getCompanyLogo(String companyId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
        map.put("type", NetFileType.COMPANY_LOGO_ATTACH);
        return this.getFileUrl(map, true);
    }

    /**
     * 方法描述：获取组织logo地址（不包含轮播图，不带fastdfsUrl）
     * 作者：MaoSF
     * 日期：2017/6/2
     */
    @Override
    public String getCompanyFileByType(String companyId, Integer type) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
        map.put("type", type);
        return this.getFileUrl(map, false);
    }


    /**
     * 方法描述：生成公司二维码
     * 作者：MaoSF
     * 日期：2016/11/25
     */
    @Override
    public String createCompanyQrcode(String companyId) throws Exception {
//        String url = this.serverUrl + "iAdmin/sys/shareInvitation/" + companyId;
//        String result = this.uploadService.createQrcode(url, this.companyUrl);
//
//        //上传成功后，数据保存到数据库
//        if (null != result && !"".equals(result)) {
//            //先删除原有的二维码数据
////			if(null!=companyId&&!"".equals(companyId)) {
////				Map<String, Object> paraMap = new HashMap<String, Object>();
////				paraMap.put("companyId", companyId);
////				paraMap.put("fileType", "7");
////				companyAttachDao.delCompanyAttachByParamer(paraMap);
////			}
//            ProjectSkyDriveEntity projectSkyDrive = new ProjectSkyDriveEntity();
//            projectSkyDrive.setId(StringUtil.buildUUID());
//            projectSkyDrive.setFileName("");
//            projectSkyDrive.setType(7);
//            projectSkyDrive.setFileGroup(result.substring(0, 6));
//            projectSkyDrive.setFilePath(result.substring(7));
//            projectSkyDrive.setCompanyId(companyId);
//            projectSkyDrive.setIsCustomize(0);
//            projectSkyDrive.setStatus("0");
//            projectSkyDrive.setCreateDate(new Date());
//            projectSkyDriverDao.insert(projectSkyDrive);
//
//            return result;
//        }

        return null;
    }

    /**
     * 方法描述：获取项目合同
     * 作者：MaoSF
     * 日期：2017/6/2
     */
    @Override
    public ProjectSkyDriveEntity getProjectContractAttach(String projectId) throws Exception {
        List<ProjectSkyDriveEntity> attachEntityList = listProjectContractAttach(projectId);
        if (!CollectionUtils.isEmpty(attachEntityList)) {
            return attachEntityList.get(0);
        }
        return null;
    }

    /**
     * 方法描述：获取项目合同附件列表
     * 作者：ZCL
     * 日期：2017/9/12
     */
    @Override
    public List<ProjectSkyDriveEntity> listProjectContractAttach(String projectId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("projectId", projectId);
        map.put("type", NetFileType.PROJECT_CONTRACT_ATTACH);
        return this.projectSkyDriverDao.getNetFileByParam(map);
    }

    @Override
    public List<ProjectSkyDriveEntity> getProjectSkyDriveEntityById(Map<String, Object> map) {
        return this.projectSkyDriverDao.getProjectSkyDriveEntityById(map);
    }

    /**
     * 方法描述：获取单个文件的路径
     * 作者：MaoSF
     * 日期：2017/6/2
     */
    private String getFileUrl(Map<String, Object> map, boolean isHasFastdfsUrl) {
        List<ProjectSkyDriveEntity> attachEntityList = this.projectSkyDriverDao.getNetFileByParam(map);
        if (!CollectionUtils.isEmpty(attachEntityList)) {
            ProjectSkyDriveEntity logoAttach = attachEntityList.get(0);
            String filePath = logoAttach.getFileGroup() + "/" + logoAttach.getFilePath();
            if (isHasFastdfsUrl) {
                filePath = this.fastdfsUrl + filePath;
            }
            return filePath;
        }
        return null;
    }

    /**
     * @param request      更改任务申请
     * @param taskList     关联任务
     * @param designerList 用户列表
     * @author 张成亮
     * @date 2018/7/17
     * @description 在交付文件目录下创建参与者用户目录
     **/
    @Override
    public void createDesignerDir(SaveProjectTaskDTO request, List<ProjectTaskEntity> taskList, List<BaseShowDTO> designerList) {
        ProjectSkyDriveEntity rootDir = getDesignRoot(request);
        ProjectSkyDriveEntity parent = rootDir;
        if (!ObjectUtils.isEmpty(taskList)){
            for (ProjectTaskEntity task : taskList) {
                ProjectSkyDriveEntity taskDir = getChildByName(parent,task.getTaskName());
                if (taskDir == null){
                    taskDir = createTaskDirFrom(parent,task,request.getAccountId());
                }
                parent = taskDir;
            }
        }

        if (!ObjectUtils.isEmpty(designerList)){
            ProjectSkyDriveEntity lastParent = parent;
            designerList.forEach(designer->{
                ProjectSkyDriveEntity designerDir = getChildByName(lastParent,designer.getName());
                if (designerDir == null){
                    createDesignerDirFrom(lastParent,designer,lastParent.getTaskId(),designer.getId());
                }
            });
        }
    }

    //查找某公司，某项目的设计文件根目录，如果没有，则创建一个
    private ProjectSkyDriveEntity getDesignRoot(SaveProjectTaskDTO request){
        final String designRoot = "设计文件";
        final int designRootOrder = 1;
        ProjectSkyDriveEntity rootDir = getRoot(request.getProjectId(),request.getCompanyId(),designRoot);
        if (!isValid(rootDir)){
            rootDir = createRootDir(request.getProjectId(),request.getCompanyId(),designRoot,request.getAccountId(),designRootOrder);
        }
        return rootDir;
    }

    //使用人员信息创建相应的设计文件子目录信息
    private ProjectSkyDriveEntity createDesignerDirFrom(ProjectSkyDriveEntity parent, BaseShowDTO request, String taskId, String createBy){
        final int SKY_DRIVE_TYPE_PERSONAL_DIR = 40;

        ProjectSkyDriveEntity dir = new ProjectSkyDriveEntity();
        dir.initEntity();
        dir.setPid(parent.getId());
        dir.setSkyDrivePath(parent.getSkyDrivePath() + "-" + dir.getId());
        dir.setCompanyId(parent.getCompanyId());
        dir.setProjectId(parent.getProjectId());
        dir.setIsCustomize(1);
        dir.setType(SKY_DRIVE_TYPE_PERSONAL_DIR);
        dir.setFileName(request.getName());
        dir.setTaskId(taskId);
        dir.setTargetId(taskId);
        dir.setParam4(1);
        dir.setStatus("0");
        dir.setCreateBy(createBy);
        projectSkyDriverDao.insert(dir);
        return dir;
    }

    /**
     * @param query 查询条件
     * @return 文件列表
     * @author 张成亮
     * @date 2018/7/19
     * @description 通用查询文件方法
     **/
    @Override
    public List<ProjectSkyDriveEntity> listEntityByQuery(ProjectSkyDriverQueryDTO query) {
        return projectSkyDriverDao.listEntityByQuery(query);
    }

    /**
     * @param query 查询条件
     * @return 文件
     * @author 张成亮
     * @date 2018/7/19
     * @description 查询文件，预期只有一个
     **/
    @Override
    public ProjectSkyDriveEntity getEntityByQuery(ProjectSkyDriverQueryDTO query) {
        List<ProjectSkyDriveEntity> list = listEntityByQuery(query);
        return (ObjectUtils.isEmpty(list)) ? null : list.get(0);
    }

    @Override
    public void saveProjectFeeContractAttach(ProjectCostEditDTO dto) throws Exception {
        //先删除原来的，删除在做添加修改处理
        Map<String,Object> deleteParam = new HashMap<>();
        deleteParam.put("targetId",dto.getId());
        deleteParam.put("accountId",dto.getAccountId());
        projectSkyDriverDao.updateSkyDriveStatus(deleteParam);
        dto.getContactAttachList().stream().forEach(attach->{
            String id = attach.get("id");
            ProjectSkyDriveEntity file = this.projectSkyDriverDao.selectById(id);
            file.setTargetId(dto.getId());
            file.setProjectId(dto.getProjectId());
            file.setType(NetFileType.PROJECT_FEE_ARCHIVE);
            if(!(attach.get("isUploadFile")!=null && "1".equals(attach.get("isUploadFile")))){
                file.setId(StringUtil.buildUUID());
                projectSkyDriverDao.insert(file);
            }else {
                file.setId(attach.get("id"));
                file.setStatus("0");
                projectSkyDriverDao.updateById(file);
            }
        });

    }
}
