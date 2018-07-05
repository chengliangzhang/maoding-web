package com.maoding.projectcost.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.projectcost.dao.ProjectCostPointDetailDao;
import com.maoding.projectcost.entity.ProjectCostPointDetailEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostDao
 * 类描述：项目费用Dao
 * 作    者：MaoSF
 * 日    期：2015年8月10日-下午4:28:32
 */
@Service("projectCostPointDetailDao")
public class ProjectCostPointDetailDaoImpl extends GenericDao<ProjectCostPointDetailEntity> implements ProjectCostPointDetailDao {


    /**
     * 方法描述：根据pointId查询
     * 作者：MaoSF
     * 日期：2017/4/25
     *
     * @param pointId
     * @param:
     * @return:
     */
    @Override
    public List<ProjectCostPointDetailEntity> getCostPointDetailByPointId(String pointId) {
        return this.sqlSession.selectList("ProjectCostPointDetailEntityMapper.getCostPointDetailByPointId",pointId);
    }

    /**
     * 方法描述：获取发起收款的总金额
     * 作者：MaoSF
     * 日期：2017/4/26
     *
     * @param pointId
     * @param:
     * @return:
     */
    @Override
    public double getSumFee(String pointId) {
        return this.sqlSession.selectOne("ProjectCostPointDetailEntityMapper.getSumFee",pointId);
    }
}
