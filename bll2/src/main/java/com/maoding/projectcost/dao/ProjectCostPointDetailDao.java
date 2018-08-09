package com.maoding.projectcost.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.projectcost.dto.ProjectCostPointDetailDataDTO;
import com.maoding.projectcost.entity.ProjectCostPointDetailEntity;

import java.util.List;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostDetailDao
 * 类描述：项目费用详情
 * 作    者：MaoSF
 * 日    期：2015年8月10日-下午4:28:32
 */
public interface ProjectCostPointDetailDao extends BaseDao<ProjectCostPointDetailEntity> {

    /**
     * 方法描述：根据pointId查询
     * 作者：MaoSF
     * 日期：2017/4/25
     */
    List<ProjectCostPointDetailEntity> getCostPointDetailByPointId(String pointId);

    /**
     * 根据pointId查询（带有发票信息）
     */
    List<ProjectCostPointDetailDataDTO> getCostPointDetail(String pointId,String companyId);

    /**
     * 方法描述：获取发起收款的总金额
     * 作者：MaoSF
     * 日期：2017/4/26
     * @param:
     * @return:
     */
    double getSumFee(String pointId);
}
