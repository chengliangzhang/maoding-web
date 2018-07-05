package com.maoding.commonModule.dao.impl;

import com.maoding.commonModule.dao.CopyRecordDao;
import com.maoding.commonModule.dto.QueryCopyRecordDTO;
import com.maoding.commonModule.entity.CopyRecordEntity;
import com.maoding.core.base.dao.GenericDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("copyRecordDao")
public class CopyRecordDaoImpl extends GenericDao<CopyRecordEntity> implements CopyRecordDao {

    @Override
    public int deleteRelationRecord(CopyRecordEntity entity) {
        return this.sqlSession.update("CopyRecordEntityMapper.updateRelationRecord",entity);
    }

    @Override
    public List<CopyRecordEntity> selectCopyByCompanyUserId(QueryCopyRecordDTO query) {
        return this.sqlSession.selectList("CopyRecordEntityMapper.selectCopyByCompanyUserId",query);
    }
}
