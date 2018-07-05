package com.maoding.project.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.dto.ProjectConstructDetailGroupByProjectDTO;
import com.maoding.project.entity.ProjectConstructDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectConstructDetailDao
 * 类描述：建设单位联系人Dao
 * 作    者：LY
 * 日    期：2016年7月10日-下午2:19:05
 */
public interface ProjectConstructDetailDao extends BaseDao<ProjectConstructDetailEntity> {

    /**
     * 方法描述：根据constructId查询详情
     * 作者：MaoSF
     * 日期：2016/7/28
     * @param:
     * @return:
     */
    public List<ProjectConstructDetailEntity> getDetailByConstructId(String constructId);

    /**
     * 方法描述：根据projectId查询详情（查询当前项目的联系人）
     * 作者：MaoSF
     * 日期：2016/7/28
     * @param:
     * @return:
     */
    public List<ProjectConstructDetailEntity> getDetailByProjectId(String constructId, String projectId);


    /**
     * 方法描述：获取当前公司，当前项目的其他联系人（如果当前项目为新增，则projectId为空，则获取所有联系人）（新版本，根据项目分组）
     * 作者：MaoSF
     * 日期：2016/7/28
     * @param: map（companyId必传），projectId可以为空
     * @return:
     */
    public List<ProjectConstructDetailGroupByProjectDTO> getOtherConstructDetailGroupByProject(Map<String,Object> map);


}