package com.maoding.project.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectDesignRangeDao;
import com.maoding.project.dto.ProjectDesignRangeDTO;
import com.maoding.project.entity.ProjectDesignBasisEntity;
import com.maoding.project.entity.ProjectDesignRangeEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DesignRangeDaoImpl
 * 类描述：DesignRangeDao设计范围
 * 作    者：ChenZJ
 * 日    期：2016年7月7日-上午5:21:29
 */
@Service("projectDesignRangeDao")
public class ProjectDesignRangeDaoImpl extends GenericDao<ProjectDesignRangeEntity> implements ProjectDesignRangeDao {


    /**
     * 方法描述：根据参数查询设计范围
     * 作        者：ChenZJ
     * 日        期：2016年7月21日-上午11:08:56
     */
    @Override
    public List<ProjectDesignRangeEntity> getDesignRangeByProjectId(String projectId) {
        return  this.sqlSession.selectList("ProjectDesignRangeEntityMapper.getDesignRangeByProjectId", projectId);
    }


    /**
     * 方法描述：根据项目Id删除设计范围
     * 作   者：LY
     * 日   期：2016/7/22 16:12
     * @param  projectId --- 项目Id
     * @return
     *
     */
    @Override
    public int deleteDRangeByProjectId(String projectId){
        return this.sqlSession.delete("ProjectDesignRangeEntityMapper.deleteDRangeByProjectId", projectId);
    }
}


