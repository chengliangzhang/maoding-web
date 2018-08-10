package com.maoding.projectcost.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.projectcost.dao.ProjectCostDao;
import com.maoding.projectcost.dto.ProjectCostDTO;
import com.maoding.projectcost.dto.ProjectCostSummaryDTO;
import com.maoding.projectcost.dto.ProjectCostSummaryQueryDTO;
import com.maoding.projectcost.entity.ProjectCostEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostDao
 * 类描述：项目费用Dao
 * 作    者：MaoSF
 * 日    期：2015年8月10日-下午4:28:32
 */
@Service("projectCostDao")
public class ProjectCostDaoImpl extends GenericDao<ProjectCostEntity> implements ProjectCostDao {

    @Override
    public List<ProjectCostDTO> selectByParam(Map<String, Object> map) {
        return this.sqlSession.selectList("GetProjectCostMapper.selectByParam", map);
    }

    @Override
    public ProjectCostEntity getProjectCostByPointId(String pointId) {
        return this.sqlSession.selectOne("ProjectCostEntityMapper.getProjectCostByPointId",pointId);
    }

    /**
     * 描述     获取组织内各项目的收付款汇总列表
     * 日期     2018/8/10
     *
     * @param query 查询条件
     *              startDate 起始日期
     * @return ProjectCostSummaryDTO列表
     * @author 张成亮
     **/
    @Override
    public List<ProjectCostSummaryDTO> listProjectCostSummary(ProjectCostSummaryQueryDTO query) {
        return sqlSession.selectList("GetProjectCostMapper.listProjectCostSummary", query);
    }
}
