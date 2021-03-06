package com.maoding.process.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.process.dto.ProcessDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.entity.ProcessEntity;

import java.util.List;

/**
 * Created by Idccapp21 on 2016/10/18.
 */
public interface ProcessDao extends BaseDao<ProcessEntity> {

    List<ProcessDTO> getProcessByCompany(QueryProcessDTO query);

    ProcessEntity getDefaultProcessByType(Integer processType);
}
