package com.maoding.projectcost.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.projectcost.dto.ProjectCostPointDTO;
import com.maoding.projectcost.dto.ProjectCostPointDataForMyTaskDTO;
import com.maoding.projectcost.dto.ProjectCostPointDetailInfoDTO;
import com.maoding.projectcost.dto.ProjectCostPointInfoDTO;
import com.maoding.projectcost.entity.ProjectCostPointEntity;
import com.maoding.projectcost.dto.ProjectCostQueryDTO;

import java.util.List;
import java.util.Map;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostPointDao
 * 类描述：项目费用收款节点表
 * 作    者：MaoSF
 * 日    期：2015年8月10日-下午4:28:32
 */
public interface ProjectCostPointDao extends BaseDao<ProjectCostPointEntity> {

    List<ProjectCostPointDTO> selectByParam(Map<String, Object> map);

    /**
     * 方法描述：根据任务id删除费用的节点（用于设计阶段取消，任务签发删除，任务签发改签给其他组织，删除对应的收款节点）
     * 作者：MaoSF
     * 日期：2017/3/2
     */
    int updateByPid(String pid);

    List<ProjectCostPointEntity> selectByType(Map<String,Object> map);

    ProjectCostPointEntity getCostPointByTaskId(String taskId);

    /**
     * 方法描述：获取同一级节点的总金额
     * 作者：MaoSF
     * 日期：2017/3/12
     * @param:
     * @return:
     */
    double getTotalFee(Map<String,Object> map);

    /**
     * 方法描述：pointDetailId,paymentDetailId必须存在其中一个值，获取节点信息
     * 作者：MaoSF
     * 日期：2017/6/29
     */
    ProjectCostPointDataForMyTaskDTO getCostPointData(String pointDetailId,String paymentDetailId,String companyId);

    /**
     * 获取当前数据
     */
    String getCostFeeCompanyByTaskId(String taskId);


    /**
     * 收付款节点详情
     */
    List<ProjectCostPointInfoDTO> listProjectAmountFeeByCostId(ProjectCostQueryDTO query);


    /**
     * 发起收款，付款详情
     */
    List<ProjectCostPointDetailInfoDTO> listProjectAmountFeeByPointId(ProjectCostQueryDTO query);
}
