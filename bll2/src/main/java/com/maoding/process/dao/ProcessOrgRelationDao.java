package com.maoding.process.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.process.dto.ProcessCountDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.entity.ProcessEntity;
import com.maoding.process.entity.ProcessOrgRelationEntity;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface ProcessOrgRelationDao extends BaseDao<ProcessOrgRelationEntity> {

    ProcessCountDTO countProcess(QueryProcessDTO query);
}
