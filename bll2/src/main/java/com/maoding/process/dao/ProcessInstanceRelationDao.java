package com.maoding.process.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.process.entity.ProcessInstanceRelationEntity;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface ProcessInstanceRelationDao extends BaseDao<ProcessInstanceRelationEntity> {

    ProcessInstanceRelationEntity getProcessInstanceRelation(String targetId);
}
