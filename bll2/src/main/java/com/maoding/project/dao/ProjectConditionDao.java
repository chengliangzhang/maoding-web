package com.maoding.project.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.dto.ProjectConditionDTO;
import com.maoding.project.entity.ProjectConditionEntity;

import java.util.List;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/25 17:11
 * 描    述 :
 */
public interface ProjectConditionDao extends BaseDao<ProjectConditionEntity> {
    /**
     * 查询
     */
    List<ProjectConditionDTO> selProjectConditionList(Map<String, Object> param);
}
