package com.maoding.process.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.process.dto.ProcessDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.entity.ProcessEntity;
import com.maoding.process.entity.ProcessNodeMemberEntity;

import java.util.List;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface ProcessNodeMemberDao extends BaseDao<ProcessNodeMemberEntity> {

    /**
     * 查询流程所有操作人
     */
    List<ProcessNodeMemberEntity> listProcessNodeMember(String processId);

    /**
     *
     *查询流程节点所有操作人
     */
    List<ProcessNodeMemberEntity> listMemberByNodeId(String nodeId);
}
