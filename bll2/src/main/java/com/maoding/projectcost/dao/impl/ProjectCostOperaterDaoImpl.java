package com.maoding.projectcost.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.projectcost.dao.ProjectCostOperaterDao;
import com.maoding.projectcost.dto.ProjectCostOperaterDTO;
import com.maoding.projectcost.entity.ProjectCostOperaterEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostOperaterDao
 * 类描述：项目费用操作详情表
 * 作    者：MaoSF
 * 日    期：2015年8月10日-下午4:28:32
 */
@Service("projectCostOperaterDao")
public class ProjectCostOperaterDaoImpl extends GenericDao<ProjectCostOperaterEntity> implements ProjectCostOperaterDao {

    /**
     * 方法描述：查询费用详情处理人
     * 作者：MaoSF
     * 日期：2017/3/6
     */
    @Override
    public List<ProjectCostOperaterDTO> getCostOperator(Map<String, Object> map) {
        return this.sqlSession.selectList("GetProjectCostOperaterMapper.getCostOperator",map);
    }

    @Override
    public List<ProjectCostOperaterEntity> selectByType(ProjectCostOperaterEntity param) {
        return this.sqlSession.selectList("ProjectCostOperaterEntityMapper.selectByType",param);
    }
}
