package com.maoding.project.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectDynamicsDao;
import com.maoding.project.dto.ProjectDynamicsDTO;
import com.maoding.project.entity.ProjectDynamicsEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectConstructDao
 * 类描述：建设单位Dao
 * 作    者：LY
 * 日    期：2016年7月10日-下午2:19:05
 */
@Service("projectDynamicsDao")
public class ProjectDynamicsDaoImpl extends GenericDao<ProjectDynamicsEntity> implements ProjectDynamicsDao {
    /**
     * 方法描述：项目动态列表
     * 作者：TangY
     * 日期：2016/7/29
     * @param:int
     * @return:
     */
    @Override
    public List<ProjectDynamicsEntity> getProjectDynamicsPage(Map<String, Object> param){
        return this.sqlSession.selectList("ProjectDynamicsEntityMapper.getProjectDynamicsPage",param);
    }

    /**
     * 方法描述：项目动态个数
     * 作者：TangY
     * 日期：2016/7/29
     * @param:int
     * @return:
     */
    @Override
    public int getProjectDynamicsPageCount(Map<String, Object> param){
        return this.sqlSession.selectOne("ProjectDynamicsEntityMapper.getProjectDynamicsPageCount", param);
    }

}