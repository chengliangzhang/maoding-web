package com.maoding.process.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.process.entity.ProcessNodeEntity;

import java.util.List;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface ProcessNodeDao extends BaseDao<ProcessNodeEntity> {

    List<ProcessNodeEntity> listProcessNode(String processId);

}
