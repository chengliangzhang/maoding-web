package com.maoding.task.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.dto.CompanyDataDTO;
import com.maoding.task.entity.ProjectTaskRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskRelationDao
 * 类描述：项目任务（任务签发组织之间的关系）
 * 作    者：MaoSF
 * 日    期：2016年12月28日-上午10:13:28
 */
public interface ProjectTaskRelationDao extends BaseDao<ProjectTaskRelationEntity> {

    /**
     * 方法描述：根据参数查询
     * 作者：MaoSF
     * 日期：2017/5/3
     * @param:
     * @return:
     */
    public List<ProjectTaskRelationEntity> getTaskRelationParam(Map<String,Object> param);
    /**
     * 方法描述：查询任务的toCompany
     * 作者：MaoSF
     * 日期：2016/12/30
     *
     * @param param
     * @param:
     * @return:
     */
    public ProjectTaskRelationEntity selectTaskRelationByTaskId(Map<String,Object> param);

    /**
     * 方法描述：根据projectId获取关系
     * 作者：MaoSF
     * 日期：2017/1/4
     * @param:
     * @return:
     */
    List<ProjectTaskRelationEntity> getProjectTaskRelationByProjectId(String projectId);

    /**
     * 方法描述：根据projectId,fromCompanyId获取关系
     * 作者：MaoSF
     * 日期：2017/1/4
     * @param:
     * @return:
     */
    List<ProjectTaskRelationEntity> getProjectTaskRelationByFromCompanyId(String projectId,String fromCompanyId);

    /**
     * 方法描述：根据projectId,CompanyId获取关系
     * 作者：MaoSF
     * 日期：2017/1/4
     * @param:
     * @return:
     */
    List<ProjectTaskRelationEntity> getProjectTaskRelationByCompanyId(String projectId,String companyId);

    /**
     * 方法描述：根据taskId.list集合，更新relationStatus
     * 作者：MaoSF
     * 日期：2017/1/5
     *
     * @param map
     * @param:
     * @return:
     */
    public int updateTaskRelationStatus(Map<String, Object> map);

    /**
     * 方法描述：用于合作设计费查询界面使用
     * 作者：MaoSF
     * 日期：2017/3/6
     * @param:
     * @return:
     */
    List<Map<String,String>> getProjectRelation(Map<String, Object> map);

    /**
     * 方法描述：查询某任务整条线被签发的次数
     * 作者：MaoSF
     * 日期：2017/1/5
     * @param:
     * @return:
     */
    int getTaskIssueCount(Map<String, Object> map);

    /**
     * 获取参与项目的组织
     */
    List<CompanyDataDTO> getTakePartCompany(String projectId);
}