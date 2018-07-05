package com.maoding.project.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectPropertyDao;
import com.maoding.project.dto.CustomProjectPropertyDTO;
import com.maoding.project.entity.ProjectPropertyEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/8/16.
 * 某个项目的自定义属性操作Dao
 */
@Service("projectPropertyDao")
public class ProjectPropertyDaoImpl extends GenericDao<ProjectPropertyEntity> implements ProjectPropertyDao {
    /**
     * 查找某个项目的自定义属性
     *
     * @param projectId 项目id
     */
    @Override
    public List<CustomProjectPropertyDTO> listProperty(String projectId) {
        return sqlSession.selectList("ProjectPropertyEntityMapper.listProperty",projectId);
    }

    /**
     * 逻辑删除记录
     *
     * @param id
     */
    @Override
    public int fakeDeleteById(String id) {
        return sqlSession.update("ProjectPropertyEntityMapper.fakeDeleteById",id);
    }

    /**
     * 添加公司默认自定义属性
     *
     * @param projectId
     * @param propertyList
     */
    @Override
    public int insertDefaultProperty(@Param("propertyList") List<CustomProjectPropertyDTO> propertyList,@Param("projectId") String projectId,@Param("createBy") String createBy) {
        Map<String,Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("createBy", createBy);
        params.put("propertyList",propertyList);
        return sqlSession.insert("ProjectPropertyEntityMapper.insertDefaultProperty",params);
    }
}
