package com.maoding.project.service;

import com.maoding.core.bean.AjaxMessage;
import com.maoding.project.dto.ProjectDesignContentDTO;
import com.maoding.project.entity.ProjectDesignContentEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ojectDesignContentService
 * 类描述：设计阶段
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:28:54
 */
public interface ProjectDesignContentService {

    /**
     * 方法描述：根据参数查询设计阶段
     * 作        者：ChenZJ
     * 日        期：2016年7月21日-上午11:28:56
     * @param projectId
     * @param param(其他参数)
     * @return
     */
    List<ProjectDesignContentDTO> getProjectDesignContentByProjectId(String projectId, Map<String, Object> param) throws Exception;




    //========================新接口2.0===================================================================================


    /**
     * 方法描述：根据参数查询设计阶段
     * 作        者：TangY
     * 日        期：2016年7月21日-上午11:28:56
     * @param projectId
     * @return
     */
    List<ProjectDesignContentEntity> getProjectDesignContentByProjectId(String projectId) throws Exception;


    AjaxMessage deleteProjectDesignContent(String id) throws Exception;
}
