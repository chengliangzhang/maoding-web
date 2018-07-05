package com.maoding.projectcost.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.projectcost.dto.ProjectCostOperaterDTO;
import com.maoding.projectcost.entity.ProjectCostOperaterEntity;

import java.util.List;
import java.util.Map;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectCostOperaterDao
 * 类描述：项目费用操作详情表
 * 作    者：MaoSF
 * 日    期：2015年8月10日-下午4:28:32
 */
public interface ProjectCostOperaterDao extends BaseDao<ProjectCostOperaterEntity> {

    /**
     * 方法描述：查询费用详情处理人
     * 作者：MaoSF
     * 日期：2017/3/6
     * @param:
     * @return:
     */
	List<ProjectCostOperaterDTO> getCostOperator(Map<String,Object> map);


	List<ProjectCostOperaterEntity> selectByType(ProjectCostOperaterEntity param);
}
