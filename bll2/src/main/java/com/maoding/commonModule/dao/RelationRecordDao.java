package com.maoding.commonModule.dao;

import com.maoding.commonModule.dto.QueryRelationRecordDTO;
import com.maoding.commonModule.entity.RelationRecordEntity;
import com.maoding.core.base.dao.BaseDao;

import java.util.List;

public interface RelationRecordDao extends BaseDao<RelationRecordEntity> {

    int deleteRelationRecord(RelationRecordEntity entity);

    List<RelationRecordEntity> getRelationRecord(QueryRelationRecordDTO query);
}
