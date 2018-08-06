package com.maoding.process.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.process.entity.ProcessTypeEntity;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface ProcessTypeDao extends BaseDao<ProcessTypeEntity> {

    /**
     * 获取当前正在使用的流程类型
     */
    ProcessTypeEntity getCurrentProcessType(String companyId,String targetType);
}
