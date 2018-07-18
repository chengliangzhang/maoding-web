package com.maoding.process.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.process.dao.ProcessDao;
import com.maoding.process.dao.ProcessNodeMemberDao;
import com.maoding.process.dto.ProcessDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.entity.ProcessEntity;
import com.maoding.process.entity.ProcessNodeMemberEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("processNodeMemberDao")
public class ProcessNodeMemberDaoImpl extends GenericDao<ProcessNodeMemberEntity> implements ProcessNodeMemberDao {


    @Override
    public List<ProcessNodeMemberEntity> listProcessNodeMember(String processId) {
        return this.sqlSession.selectList("ProcessNodeMemberEntityMapper.listProcessNodeMember",processId);
    }
}
