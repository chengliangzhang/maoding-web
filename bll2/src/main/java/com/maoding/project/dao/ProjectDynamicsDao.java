package com.maoding.project.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.dto.ProjectConstructDTO;
import com.maoding.project.dto.ProjectDynamicsDTO;
import com.maoding.project.entity.ProjectConstructEntity;
import com.maoding.project.entity.ProjectDynamicsEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectConstructDao
 * 类描述：建设单位Dao
 * 作    者：LY
 * 日    期：2016年7月10日-下午2:19:05
 */
public interface ProjectDynamicsDao extends BaseDao<ProjectDynamicsEntity> {
    /**
     * 方法描述：项目动态列表
     * 作者：TangY
     * 日期：2016/7/29
     * @param:int
     * @return:
     */

    public List<ProjectDynamicsEntity> getProjectDynamicsPage(Map<String, Object> param);

    /**
     * 方法描述：项目动态个数
     * 作者：TangY
     * 日期：2016/7/29
     * @param:int
     * @return:
     */
    public int getProjectDynamicsPageCount(Map<String, Object> param);
}