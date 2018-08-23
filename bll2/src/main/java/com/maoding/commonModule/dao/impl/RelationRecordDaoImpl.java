package com.maoding.commonModule.dao.impl;

import com.maoding.commonModule.dao.RelationRecordDao;
import com.maoding.commonModule.dto.QueryRelationRecordDTO;
import com.maoding.commonModule.entity.RelationRecordEntity;
import com.maoding.core.base.dao.GenericDao;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("relationRecordDao")
public class RelationRecordDaoImpl extends GenericDao<RelationRecordEntity> implements RelationRecordDao {

    @Override
    public int deleteRelationRecord(RelationRecordEntity entity) {
        return this.sqlSession.update("RelationRecordEntityMapper.updateRelationRecord",entity);
    }

    @Override
    public List<RelationRecordEntity> getRelationRecord(QueryRelationRecordDTO query) {
        return this.sqlSession.selectList("RelationRecordEntityMapper.getRelationRecord",query);
    }

    @Override
    public RelationRecordEntity getRelationRecodeByTargetId(String targetId) {
        QueryRelationRecordDTO query = new QueryRelationRecordDTO();
        query.setTargetId(targetId);
        List<RelationRecordEntity> list = this.getRelationRecord(query);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }
}
