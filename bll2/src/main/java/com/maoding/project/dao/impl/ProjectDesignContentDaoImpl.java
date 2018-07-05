package com.maoding.project.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectDesignContentDao;
import com.maoding.project.entity.ProjectDesignContentEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dong_Liu
 * date：2017/9/22
 */
@Service("projectDesignContentDao")
public class ProjectDesignContentDaoImpl extends GenericDao<ProjectDesignContentEntity> implements ProjectDesignContentDao{

    @Override
    public List<ProjectDesignContentEntity> getProjectDesignContentByProjectId(String projectId) {
        return this.sqlSession.selectList("ProjectDesignContentEntityMapper.getProjectDesignContentByProjectId",projectId);
    }

    @Override
    public Integer getProjectContentMaxSeq(String projectId) {
        return this.sqlSession.selectOne("ProjectDesignContentEntityMapper.getProjectContentMaxSeq",projectId);
    }
}
