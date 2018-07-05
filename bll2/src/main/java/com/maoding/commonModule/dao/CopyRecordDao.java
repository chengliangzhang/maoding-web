package com.maoding.commonModule.dao;

import com.maoding.commonModule.dto.QueryCopyRecordDTO;
import com.maoding.commonModule.entity.CopyRecordEntity;
import com.maoding.core.base.dao.BaseDao;

import java.util.List;

public interface CopyRecordDao extends BaseDao<CopyRecordEntity> {

    int deleteRelationRecord(CopyRecordEntity entity);

    List<CopyRecordEntity> selectCopyByCompanyUserId(QueryCopyRecordDTO query);
}
