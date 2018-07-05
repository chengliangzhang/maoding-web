package com.maoding.project.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectConditionDao;
import com.maoding.project.dto.ProjectConditionDTO;
import com.maoding.project.entity.ProjectConditionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/25 17:20
 * 描    述 :
 */
@Service("projectConditionDao")
public class ProjectConditionDaoImpl extends GenericDao<ProjectConditionEntity> implements ProjectConditionDao {
    @Override
    public List<ProjectConditionDTO> selProjectConditionList(Map<String, Object> param) {
        return this.sqlSession.selectList("ProjectConditionEntityMapper.selProjectConditionList", param);
    }
}
