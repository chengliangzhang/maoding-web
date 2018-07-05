package com.maoding.projectcost.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.projectcost.dto.ProjectCostDTO;
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

}
