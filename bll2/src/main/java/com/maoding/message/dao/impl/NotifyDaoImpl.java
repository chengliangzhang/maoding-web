package com.maoding.message.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.message.dao.NotifyDao;
import com.maoding.message.entity.NotifyEntity;
import org.springframework.stereotype.Service;

/**
 * Created by Chengliang.zhang on 2017/4/14.
 */
@Service("notifyDao")
public class NotifyDaoImpl extends GenericDao<NotifyEntity> implements NotifyDao {
    @Override
    public NotifyEntity getNotifyByCompanyId(String companyId) {
        return this.sqlSession.selectOne("NotifyEntityMapper.selectByCompanyId",companyId);
    }

    @Override
    public int deleteByCompanyId(String companyId) {
        return this.sqlSession.delete("NotifyEntityMapper.deleteByCompanyId",companyId);
    }
}
