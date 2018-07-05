package com.maoding.projectcost.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.projectcost.dao.ProjectCostPointDao;
import com.maoding.projectcost.dto.ProjectCostPointDTO;
import com.maoding.projectcost.dto.ProjectCostPointDataForMyTaskDTO;
import com.maoding.projectcost.entity.ProjectCostPointEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostPointDao
 * 类描述：项目费用收款节点表
 * 作    者：MaoSF
 * 日    期：2015年8月10日-下午4:28:32
 */
@Service("projectCostPointDao")
public class ProjectCostPointDaoImpl extends GenericDao<ProjectCostPointEntity> implements ProjectCostPointDao {

    @Override
    public List<ProjectCostPointDTO> selectByParam(Map<String, Object> map) {
        return this.sqlSession.selectList("GetProjectCostPointMapper.selectByParam", map);
    }

    /**
     * 方法描述：根据任务id删除费用的节点（用于设计阶段取消，任务签发删除，任务签发改签给其他组织，删除对应的收款节点）
     * 作者：MaoSF
     * 日期：2017/3/2
     */
    @Override
    public int updateByPid(String pid) {
        return this.sqlSession.update("ProjectCostPointEntityMapper.updateByPid",pid);
    }

    @Override
    public List<ProjectCostPointEntity> selectByType(Map<String, Object> map) {
        return this.sqlSession.selectList("ProjectCostPointEntityMapper.selectByType", map);
    }

    @Override
    public ProjectCostPointEntity getCostPointByTaskId(String taskId) {
        return this.sqlSession.selectOne("ProjectCostPointEntityMapper.getCostPointByTaskId", taskId);
    }

    /**
     * 方法描述：获取同一级节点的总金额
     * 作者：MaoSF
     * 日期：2017/3/12
     */
    @Override
    public double getTotalFee(Map<String, Object> map) {
        return this.sqlSession.selectOne("ProjectCostPointEntityMapper.getTotalFee", map);
    }


    /**
     * 方法描述：pointDetailId,paymentDetailId必须存在其中一个值，获取节点信息
     * 作者：MaoSF
     * 日期：2017/6/29
     */
    @Override
    public ProjectCostPointDataForMyTaskDTO getCostPointData(String pointDetailId, String paymentDetailId, String companyId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pointDetailId",pointDetailId);
        map.put("paymentDetailId",paymentDetailId);
        map.put("companyId",companyId);
        return this.sqlSession.selectOne("GetProjectCostPointMapper.getCostPointData", map);
    }

    public String getCostFeeCompanyByTaskId(String taskId){
        return this.sqlSession.selectOne("GetProjectCostPointMapper.getCostFeeCompanyByTaskId", taskId);
    }
}
