package com.maoding.process.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.process.dao.ProcessInstanceRelationDao;
import com.maoding.process.entity.ProcessInstanceRelationEntity;
import org.springframework.stereotype.Service;

@Service("processInstanceRelationDao")
public class ProcessInstanceRelationDaoImpl extends GenericDao<ProcessInstanceRelationEntity> implements ProcessInstanceRelationDao {

    @Override
    public ProcessInstanceRelationEntity getProcessInstanceRelation(String targetId) {
        return sqlSession.selectOne("ProcessInstanceRelationEntityMapper.getProcessInstanceRelation",targetId);
    }
}
