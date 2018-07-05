package com.maoding.dynamic.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.dynamic.dto.DynamicDTO;
import com.maoding.dynamic.dto.QueryDynamicDTO;
import com.maoding.dynamic.entity.DynamicDO;
import com.maoding.project.entity.ProjectDynamicsEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/6/7.
 */
public interface DynamicDAO{
    /**
     * 查找动态信息
     */
    List<DynamicDTO> listDynamic(QueryDynamicDTO query);
    /**
     * 使用兼容格式返回动态信息
     */
    List<ProjectDynamicsEntity> listProjectDynamics(QueryDynamicDTO query);
    /**
     * 获取最后一次查询找到的记录数
     */
    Integer getLastQueryCount();

    int insert(DynamicDO dynamic);
}
