package com.maoding.user.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.user.entity.AttentionEntity;

import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：AttentionDaoImpl
 * 类描述：关注Dao
 * 作    者：wrb
 * 日    期：2017年01月06日-下午16:38:05
 */
public interface AttentionDao extends BaseDao<AttentionEntity> {


    public AttentionEntity getAttentionEntity(Map<String,Object> paraMap);
}
