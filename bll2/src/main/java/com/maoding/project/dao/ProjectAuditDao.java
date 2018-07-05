package com.maoding.project.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.entity.ProjectAuditEntity;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectAuditDao
 * 类描述：项目审核Dao
 * 作    者：LY
 * 日    期：2016年7月10日-下午2:19:05
 */
public interface ProjectAuditDao extends BaseDao<ProjectAuditEntity> {

    /**
     * 方法描述：根据项目Id和审核类型删除项目审核相关信息
     * 作   者：LY
     * 日   期：2016/7/25 10:01
     * @param  projectId
     * @param: type
     * @return 
     *
    */
    public int delAuditByProjectAndType(String projectId,String type);


    /**
     * 方法描述：根据项目Id和审核类型查询项目审核相关信息
     * 作者：MaoSF
     * 日期：2016/8/3
     * @param: projectId
     * @param: type
     * @return:
     */
    public List<ProjectAuditEntity> getProjectAuditEntityByProjectAndType(String projectId, String type);
}