package com.maoding.dynamic.dao.impl;

import com.maoding.dynamic.dao.DynamicDAO;
import com.maoding.dynamic.dto.DynamicDTO;
import com.maoding.dynamic.dto.QueryDynamicDTO;
import com.maoding.dynamic.entity.DynamicDO;
import com.maoding.project.entity.ProjectDynamicsEntity;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/6/8.
 */
@Service("dynamicDAO")
public class DynamicDAOImpl implements DynamicDAO {
    @Autowired
    protected SqlSessionTemplate sqlSession;

    private DynamicDAO dao = null;

    @PostConstruct
    public void init(){
        dao = sqlSession.getMapper(DynamicDAO.class);
    }

    /**
     * 查找动态信息
     *
     * @param query
     */
    @Override
    public List<DynamicDTO> listDynamic(QueryDynamicDTO query) {
        return dao.listDynamic(query);
    }

    /**
     * 从兼容数据表内查找动态信息 (maoding_web_project_dynamics)
     *
     * @param query
     */
    @Override
    public List<ProjectDynamicsEntity> listProjectDynamics(QueryDynamicDTO query) {
        return dao.listProjectDynamics(query);
    }

    /**
     * 获取最后一次查询找到的记录数
     */
    @Override
    public Integer getLastQueryCount() {
        return dao.getLastQueryCount();
    }

    @Override
    public int insert(DynamicDO dynamic) {
        return dao.insert(dynamic);
    }
}
