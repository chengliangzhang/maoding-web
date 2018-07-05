package com.maoding.project.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.project.dao.ProjectDesignRangeDao;
import com.maoding.project.entity.ProjectDesignRangeEntity;
import com.maoding.project.service.ProjectDesignRangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：designRangeService
 * 类描述：设计范围
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:28:54
 */
@Service("projectDesignRangeService")
public class ProjectDesignRangeServiceImpl extends GenericService<ProjectDesignRangeEntity> implements ProjectDesignRangeService {

    @Autowired
    private ProjectDesignRangeDao projectDesignRangeDao;

    @Override
    public List<ProjectDesignRangeEntity> getDesignRangeByProjectId(String projectId) throws Exception {
        return projectDesignRangeDao.getDesignRangeByProjectId(projectId);
    }
}
