package com.maoding.message.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.message.entity.NotifyEntity;

/**
 * Created by Chengliang.zhang on 2017/4/14.
 */
public interface NotifyDao extends BaseDao<NotifyEntity> {
    NotifyEntity getNotifyByCompanyId(String companyId);
    int deleteByCompanyId(String companyId);
}
