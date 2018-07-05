package com.maoding.project.service.impl;

import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.SystemParameters;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dao.ProjectDesignContentDao;
import com.maoding.project.dao.ProjectProcessNodeDao;
import com.maoding.project.dto.ProjectDesignContentDTO;
import com.maoding.project.entity.ProjectDesignContentEntity;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectProcessNodeEntity;
import com.maoding.project.service.ProjectDesignContentService;
import com.maoding.task.dao.ProjectProcessTimeDao;
import com.maoding.task.dao.ProjectTaskDao;
import com.maoding.task.dao.ProjectTaskRelationDao;
import com.maoding.task.dto.ProjectTaskDataDTO;
import com.maoding.task.entity.ProjectProcessTimeEntity;
import com.maoding.task.entity.ProjectTaskEntity;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名： ProjectDesignContentServiceImpl
 * 类描述：设计阶段
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:28:54
 */
@Service(" projectDesignContentService")
public class ProjectDesignContentServiceImpl  implements ProjectDesignContentService {

    @Autowired
    private ProjectProcessTimeDao projectProcessTimeDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ProjectTaskDao projectTaskDao;

    @Autowired
    private ProjectDesignContentDao projectDesignContentDao;

    /**
     * 方法描述：根据参数查询设计阶段
     * 作        者：ChenZJ
     * 日        期：2016年7月21日-上午11:28:56
     *
     * @param projectId
     * @return
     */
    @Override
    public List<ProjectDesignContentDTO> getProjectDesignContentByProjectId(String projectId,Map<String,Object> param) throws Exception{
        String companyId = (String)param.get("companyId");
        ProjectEntity projectEntity = this.projectDao.selectById(projectId);
//        List<ProjectTaskDataDTO> rootTaskList = this.projectTaskDao.getProjectTaskRootData(projectId,(String)param.get("projectCompanyId"));
        List<ProjectDesignContentEntity> designContentEntities=projectDesignContentDao.getProjectDesignContentByProjectId(projectId);
        List<ProjectDesignContentDTO> projectDesignContentDTOList=new ArrayList<>();
        int seq = 1;
        for(ProjectDesignContentEntity dto1:designContentEntities){
            ProjectDesignContentDTO dto = new ProjectDesignContentDTO();
            dto.setId(dto1.getId());
            dto.setCompanyId(dto1.getCompanyId());
            dto.setProjectId(dto1.getProjectId());
            dto.setContentName(dto1.getContentName());
//            dto.setStartTime(dto1.getStartTime());
//            dto.setEndTime(dto1.getEndTime());
            dto.setSeq((seq++)+"");
//            if(companyId.equals(projectEntity.getCompanyId()) && SystemParameters.TASK_STATUS_MODIFIED.equals(dto1.getTaskStatus())){
//                dto.setIsHas("1");
//            }else {
//                dto.setIsHas("0");
//            }

            Map<String,Object> map = new HashMap<>();
            map.put("targetId",dto.getId());
            map.put("type",1);
            if(companyId.equals(projectEntity.getCompanyId()) || companyId.equals(projectEntity.getCompanyBid())){
                List<ProjectProcessTimeEntity> projectProcessTimeEntityList = projectProcessTimeDao.getProjectProcessTime(map);
                dto.setProjectProcessTimeEntityList(projectProcessTimeEntityList);
            }
            projectDesignContentDTOList.add(dto);
        }
        return projectDesignContentDTOList;
    }


    //========================新接口2.0===================================================================================


    /**
     * 方法描述：根据参数查询设计阶段
     * 作        者：TangY
     * 日        期：2016年7月21日-上午11:28:56
     * @param projectId
     * @return
     */
    @Override
    public  List<ProjectDesignContentEntity> getProjectDesignContentByProjectId(String projectId) throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("projectId",projectId);
        map.put("taskType",1);
        List<ProjectTaskEntity> list = this.projectTaskDao.selectByParam(map);
        List<ProjectDesignContentEntity> projectDesignContentEntityList = new ArrayList<>();
        for(ProjectTaskEntity task:list){
            ProjectDesignContentEntity contentEntity = new ProjectDesignContentEntity();
            contentEntity.setId(task.getId());
            contentEntity.setProjectId(task.getProjectId());
            contentEntity.setCompanyId(task.getCompanyId());
            contentEntity.setSeq(task.getSeq()+"");
            contentEntity.setContentName(task.getTaskName());
        }
        return  projectDesignContentEntityList;
    }

    @Override
    public AjaxMessage deleteProjectDesignContent(String id) throws Exception {
        int i = this.projectDesignContentDao.deleteById(id);
        return i==1?AjaxMessage.succeed("删除成功"):AjaxMessage.failed("参数错误");
    }
}
