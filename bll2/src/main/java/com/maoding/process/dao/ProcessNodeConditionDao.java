package com.maoding.process.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.process.dto.ProcessDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.entity.ProcessEntity;
import com.maoding.process.entity.ProcessNodeConditionEntity;

import java.util.List;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface ProcessNodeConditionDao extends BaseDao<ProcessNodeConditionEntity> {

    /**
     * 查询所有流程的条件信息
     */
    List<ProcessNodeConditionEntity> listProcessNodeCondition(String processId);

    List<ProcessNodeConditionEntity> getProcessNodeCondition(QueryProcessDTO dto);

    ProcessNodeConditionEntity getProcessNodeConditionByDataType(String nodeId,String dataType);
   // List<ProcessNodeConditionEntity> listProcessNodeConditionByNode(String nodeId);
}
