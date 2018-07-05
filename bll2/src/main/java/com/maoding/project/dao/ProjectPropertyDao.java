package com.maoding.project.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.dto.CustomProjectPropertyDTO;
import com.maoding.project.entity.ProjectPropertyEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/8/16.
 */
public interface ProjectPropertyDao extends BaseDao<ProjectPropertyEntity> {
    /** 查找某个项目的自定义属性 */
    List<CustomProjectPropertyDTO> listProperty(String projectId);
    /** 逻辑删除记录 */
    int fakeDeleteById(String id);
    /** 添加公司默认自定义属性 */
    int insertDefaultProperty(@Param("propertyList") List<CustomProjectPropertyDTO> propertyList,@Param("projectId") String projectId,@Param("createBy") String createBy);
}
