package com.maoding.project.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectConstructDetailDao;
import com.maoding.project.dto.ProjectConstructDetailGroupByProjectDTO;
import com.maoding.project.entity.ProjectConstructDetailEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectConstructDetailDaoImpl
 * 类描述：建设单位联系人DaoImpl
 * 作    者：LY
 * 日    期：2016年7月7日-下午3:44:06
 */

@Service("projectConstructDetailDao")
public class ProjectConstructDetailDaoImpl extends GenericDao<ProjectConstructDetailEntity> implements ProjectConstructDetailDao {

    /**
     * 方法描述：根据constructId查询详情
     * 作者：MaoSF
     * 日期：2016/7/28
     *
     * @param constructId
     * @param:
     * @return:
     */
    @Override
    public List<ProjectConstructDetailEntity> getDetailByConstructId(String constructId) {
        return this.sqlSession.selectList("ProjectConstructDetailEntityMapper.getDetailByConstructId", constructId);
    }

    /**
     * 方法描述：根据projectId查询详情（查询当前项目的联系人）
     * 作者：MaoSF
     * 日期：2016/7/28
     *
     * @param projectId
     * @param:
     * @return:
     */
    @Override
    public List<ProjectConstructDetailEntity> getDetailByProjectId(String constructId,String projectId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("constructId",constructId);
        map.put("projectId",projectId);
        return this.sqlSession.selectList("ProjectConstructDetailEntityMapper.getDetailByProjectId", map);
    }

    /**
     * 方法描述：获取当前公司，当前项目的其他联系人（如果当前项目为新增，则projectId为空，则获取所有联系人）（新版本，根据项目分组）
     * 作者：MaoSF
     * 日期：2016/7/28
     *
     * @param map
     * @param: map（companyId必传），projectId可以为空
     * @return:
     */
    @Override
    public List<ProjectConstructDetailGroupByProjectDTO> getOtherConstructDetailGroupByProject(Map<String, Object> map) {
        return this.sqlSession.selectList("GetProjectConstructDetailGroupByProjectMapper.getOtherConstructDetailGroupByProject", map);
    }

}
