package com.maoding.projectcost.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.projectcost.dto.ProjectCostDTO;
import com.maoding.projectcost.dto.ProjectCostSingleSummaryDTO;
import com.maoding.projectcost.dto.ProjectCostSummaryQueryDTO;
import com.maoding.projectcost.entity.ProjectCostEntity;

import java.util.List;
import java.util.Map;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostDao
 * 类描述：项目费用Dao
 * 作    者：MaoSF
 * 日    期：2015年8月10日-下午4:28:32
 */
public interface ProjectCostDao extends BaseDao<ProjectCostEntity> {

    List<ProjectCostDTO> selectByParam(Map<String, Object> map);

    /**
     * @param pointId (费用节点的id)
     */
    ProjectCostEntity getProjectCostByPointId(String pointId);

    /**
     * 描述     获取组织内各项目的收付款汇总列表
     * 日期     2018/8/10
     *
     * @param query 查询条件
     *              startDate 起始日期
     * @return ProjectCostSummaryDTO列表
     * @author 张成亮
     **/
    List<ProjectCostSingleSummaryDTO> listProjectCostSummary(ProjectCostSummaryQueryDTO query);
}
