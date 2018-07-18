package com.maoding.process.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.process.dao.ProcessDao;
import com.maoding.process.dao.ProcessNodeDao;
import com.maoding.process.entity.ProcessEntity;
import com.maoding.process.entity.ProcessNodeEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("processNodeDao")
public class ProcessNodeDaoImpl extends GenericDao<ProcessNodeEntity> implements ProcessNodeDao {

    @Override
    public List<ProcessNodeEntity> listProcessNode(String processId) {
        return this.sqlSession.selectList("ProcessNodeEntityMapper.listProcessNode",processId);
    }
}
