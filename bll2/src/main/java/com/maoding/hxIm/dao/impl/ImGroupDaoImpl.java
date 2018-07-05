package com.maoding.hxIm.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.hxIm.dao.ImGroupDao;
import com.maoding.hxIm.dao.ImQueueDao;
import com.maoding.hxIm.entity.ImGroupEntity;
import com.maoding.hxIm.entity.ImQueueEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by sandy on 2017/8/7.
 */
@Service("imGroupDao")
public class ImGroupDaoImpl extends GenericDao<ImGroupEntity> implements ImGroupDao {

    @Override
    public List<ImGroupEntity> getImGroupsByParam(Map<String, Object> map) {
        return sqlSession.selectList("ImGroupEntityMapper.selectByParam", map);
    }
}
