package com.maoding.hxIm.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.hxIm.entity.ImGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by sandy on 2017/8/7.
 */
public interface ImGroupDao extends BaseDao<ImGroupEntity>{
    /**
     * 根据参数查找群组（暂时）
     */
    List<ImGroupEntity> getImGroupsByParam(Map<String,Object> map);
}
