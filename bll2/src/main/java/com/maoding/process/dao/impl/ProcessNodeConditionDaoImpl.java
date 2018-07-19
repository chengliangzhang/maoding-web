package com.maoding.process.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.process.dao.ProcessDao;
import com.maoding.process.dao.ProcessNodeConditionDao;
import com.maoding.process.dto.ProcessDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.entity.ProcessEntity;
import com.maoding.process.entity.ProcessNodeConditionEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("processNodeConditionDao")
public class ProcessNodeConditionDaoImpl extends GenericDao<ProcessNodeConditionEntity> implements ProcessNodeConditionDao {


    @Override
    public List<ProcessNodeConditionEntity> listProcessNodeCondition(String processId) {
        QueryProcessDTO dto = new QueryProcessDTO();
        dto.setProcessId(processId);
        return getProcessNodeCondition(dto);
    }

    @Override
    public List<ProcessNodeConditionEntity> getProcessNodeCondition(QueryProcessDTO dto) {
        return this.sqlSession.selectList("ProcessNodeConditionEntityMapper.listProcessNodeCondition",dto);
    }

    @Override
    public ProcessNodeConditionEntity getProcessNodeConditionByDataType(String nodeId, String dataType) {
        QueryProcessDTO dto = new QueryProcessDTO();
        dto.setNodeId(nodeId);
        dto.setDataType(dataType);
        List<ProcessNodeConditionEntity> dataList = getProcessNodeCondition(dto);
        if(!CollectionUtils.isEmpty(dataList)){
            return dataList.get(0);
        }
        return null;
    }
}
