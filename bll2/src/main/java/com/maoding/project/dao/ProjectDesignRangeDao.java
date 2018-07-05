package com.maoding.project.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.dto.ProjectDesignRangeDTO;
import com.maoding.project.entity.ProjectDesignBasisEntity;
import com.maoding.project.entity.ProjectDesignRangeEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DesignRangeDao
 * 类描述：设计范围（dao）
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:20:47
 */
public interface ProjectDesignRangeDao extends BaseDao<ProjectDesignRangeEntity> {
    /**
     * 方法描述：根据项目ID查询
     * 作        者：wangrb
     * 日        期：2015年12月26日-下午7:26:03
     * @param projectId
     * @return
     */
    public List<ProjectDesignRangeEntity> getDesignRangeByProjectId(String projectId);

    /**
     * 方法描述：根据项目Id删除设计范围
     * 作   者：LY
     * 日   期：2016/7/22 16:12
     * @param  projectId --- 项目Id
     * @return
     *
    */
    public int deleteDRangeByProjectId(String projectId);
}
