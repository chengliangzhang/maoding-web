package com.maoding.project.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.project.entity.ProjectDesignRangeEntity;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DesignRangeService
 * 类描述：设计范围
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:28:54
 */
public interface ProjectDesignRangeService extends BaseService<ProjectDesignRangeEntity>{


    public List<ProjectDesignRangeEntity> getDesignRangeByProjectId(String projectId) throws Exception;
}
