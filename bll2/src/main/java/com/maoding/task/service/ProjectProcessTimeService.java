package com.maoding.task.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.task.dto.ProjectProcessTimeDTO;
import com.maoding.task.entity.ProjectProcessTimeEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskRelationService
 * 类描述：项目任务签发关系
 * 作    者：MaoSF
 * 日    期：2016年12月28日-下午5:28:54
 */
public interface ProjectProcessTimeService extends BaseService<ProjectProcessTimeEntity>{

    /**
     * 方法描述：保存变更信息
     * 作        者：ChenZJ
     * 日        期：2016年7月21日-上午11:28:56
     */
    AjaxMessage saveOrUpdateProjectProcessTime(ProjectProcessTimeDTO processTimeDTO)throws Exception;

    /**
     * 方法描述：保存变更信息
     * 作        者：MaoSF
     * 日        期：2016年7月21日-上午11:28:56
     */
    ProjectProcessTimeEntity saveChangeTime(ProjectProcessTimeDTO processTimeDTO,String taskId) throws Exception;

    /**
     * 方法描述：根据tagetId获取变更信息
     * 作        者：TangY
     * 日        期：2016年7月21日-上午11:28:56
     *
     * @param {targetId，type}
     */
    List<ProjectProcessTimeDTO> getProjectProcessTimeList(Map<String,Object> param)throws Exception;

    /**
     * 方法描述：根据id删除变更记录
     * 作        者：TangY
     * 日        期：2016年7月21日-上午11:28:56
     *
     */
    int delProjectProcessTime(String id)throws Exception;
}
