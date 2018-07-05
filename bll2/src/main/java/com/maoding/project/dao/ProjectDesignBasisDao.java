package com.maoding.project.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.dto.ProjectDesignBasisDTO;
import com.maoding.project.entity.ProjectDesignBasisEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DesignBasisDao
 * 类描述：设计依据dao
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:20:47
 */
public interface ProjectDesignBasisDao extends BaseDao<ProjectDesignBasisEntity> {

    /**
     * 方法描述：根据项目ID查询
     * 作        者：wangrb
     * 日        期：2015年12月26日-下午7:26:03
     */
    List<ProjectDesignBasisDTO> getDesignBasisByProjectId(String projectId);

    /**
     * 根据项目Id删除设计依据
     */
    int deleteDBasisByProjectId(String projectId);

}
