package com.maoding.project.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectAuditDao;
import com.maoding.project.entity.ProjectAuditEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectAuditDaoImpl
 * 类描述：项目审核DaoImpl
 * 作    者：LY
 * 日    期：2016年7月7日-下午3:44:06
 */

@Service("projectAuditDao")
public class ProjectAuditDaoImpl extends GenericDao<ProjectAuditEntity> implements ProjectAuditDao {

    /**
     * 方法描述：根据项目Id和审核类型删除项目审核相关信息
     * 作   者：LY
     * 日   期：2016/7/25 10:01
     * @param
     * @return
     *
     */
    public int delAuditByProjectAndType(String projectId,String type){
        Map<String,String> map = new HashMap<String, String>();
        map.put("projectId", projectId);
        map.put("auditType", type);
        return this.sqlSession.delete("ProjectAuditEntityMapper.deleteByProjectAndType", map);
    }

    /**
     * 方法描述：根据项目Id和审核类型查询项目审核相关信息
     * 作者：MaoSF
     * 日期：2016/8/3
     *
     * @param projectId
     * @param type
     * @param: projectId
     * @param: type
     * @return:
     */
    @Override
    public List<ProjectAuditEntity> getProjectAuditEntityByProjectAndType(String projectId, String type) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("projectId", projectId);
        map.put("auditType", type);
        return this.sqlSession.selectList("ProjectAuditEntityMapper.getProjectAuditEntityByProjectAndType", map);
    }
}
