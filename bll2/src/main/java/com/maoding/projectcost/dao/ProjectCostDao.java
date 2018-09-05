package com.maoding.projectcost.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.projectcost.dto.*;
import com.maoding.projectcost.entity.ProjectCostEntity;
import com.maoding.projectcost.dto.ProjectCostQueryDTO;

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
     * @param query 查询条件
     *              projectId 关联项目编号
     *              currentCompanyId 关联公司编号
     *              startDate 起始日期
     *              endDate 终止日期
     * @return ProjectCostSummaryDTO列表
     * @author 张成亮
     **/
    List<ProjectCostSingleSummaryDTO> listProjectCostSummary(ProjectCostSummaryQueryDTO query);

    /**
     * 描述     获取组织内各项目的收付款汇总列表
     * 日期     2018/8/10
     * @param query 查询条件
     *              projectId 关联项目编号
     *              currentCompanyId 关联公司编号
     *              startDate 起始日期
     *              endDate 终止日期
     * @return ProjectExpSummaryDTO列表
     * @author 张成亮
     **/
    List<ProjectExpSingleSummaryDTO> listProjectExpSummary(ProjectCostSummaryQueryDTO query);

    /**
     * 根据costId 查询应收，应付，已收，已付
     */
    ProjectCooperatorCostDTO getProjectAmountFeeByCostId(ProjectCostQueryDTO queryDTO);

    /**
     * 描述       获取计划款项汇总数值
     * 日期       2018/9/5
     * @author   张成亮
     **/
    CostAmountDTO getCostAmountPlan(ProjectCostQueryDTO query);

    /**
     * 描述       获取实际款项汇总数值
     * 日期       2018/9/5
     * @author   张成亮
     **/
    CostAmountDTO getCostAmountReal(ProjectCostQueryDTO query);
}
