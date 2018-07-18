package com.maoding.process.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.process.dao.ProcessDao;
import com.maoding.process.dto.ProcessDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.entity.ProcessEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("processDao")
public class ProcessDaoImpl extends GenericDao<ProcessEntity> implements ProcessDao {

    @Override
    public List<ProcessDTO> getProcessByCompany(QueryProcessDTO query) {
        return this.sqlSession.selectList("GetProcessMapper.getProcessByCompany",query);
    }

    @Override
    public ProcessEntity getDefaultProcessByType(Integer processType) {
        return this.sqlSession.selectOne("ProcessEntityMapper.getDefaultProcessByType",processType);
    }
}
