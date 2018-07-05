package com.maoding.project.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.project.dto.ProjectConditionDTO;
import com.maoding.project.entity.ProjectConditionEntity;

import java.util.List;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/25 17:20
 * 描    述 :
 */
public interface ProjectConditionService extends BaseService<ProjectConditionEntity> {
    /**
     * 查询
     */
    List<ProjectConditionDTO> selProjectConditionList(Map<String, Object> param);

    /**
     * 新增
     * */
    int insertProjectCondition(Map<String, Object> param);
}
