package com.maoding.project.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectDesignBasisDao;
import com.maoding.project.dto.ProjectDesignBasisDTO;
import com.maoding.project.entity.ProjectDesignBasisEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DesignBasisDaoImpl
 * 类描述：设计依据
 * 作    者：ChenZJ
 * 日    期：2016年7月7日-上午5:21:29
 */
@Service("projectDesignBasisDao")
public class ProjectDesignBasisDaoImpl extends GenericDao<ProjectDesignBasisEntity> implements ProjectDesignBasisDao {

    @Override
    public List<ProjectDesignBasisDTO> getDesignBasisByProjectId(String projectId) {
        return this.sqlSession.selectList("GetProjectDesignBasisMapper.getDesignBasisByProjectId", projectId);
    }


    /**
     * 根据项目Id删除设计依据
     * @param projectId
     * @return
     */
    @Override
    public int deleteDBasisByProjectId(String projectId){
        return this.sqlSession.delete("ProjectDesignBasisEntityMapper.deleteDBasisByProjectId", projectId);
    }


}


