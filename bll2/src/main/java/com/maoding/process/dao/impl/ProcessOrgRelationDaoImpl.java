package com.maoding.process.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.process.dao.ProcessDao;
import com.maoding.process.dao.ProcessOrgRelationDao;
import com.maoding.process.dto.ProcessCountDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.entity.ProcessEntity;
import com.maoding.process.entity.ProcessOrgRelationEntity;
import org.springframework.stereotype.Service;

@Service("processOrgRelationDao")
public class ProcessOrgRelationDaoImpl extends GenericDao<ProcessOrgRelationEntity> implements ProcessOrgRelationDao {

    @Override
    public ProcessCountDTO countProcess(QueryProcessDTO query) {
        return this.sqlSession.selectOne("ProcessOrgRelationEntityMapper.countProcess");
    }
}
