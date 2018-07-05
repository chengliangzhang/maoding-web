package com.maoding.project.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.entity.ProjectDesignContentEntity;

import java.util.List;

/**
 * Created by Dong_Liu
 * date：2017/9/22
 */
public interface ProjectDesignContentDao extends BaseDao<ProjectDesignContentEntity> {
            List<ProjectDesignContentEntity> getProjectDesignContentByProjectId(String projectId);

            Integer getProjectContentMaxSeq(String projectId);
}
