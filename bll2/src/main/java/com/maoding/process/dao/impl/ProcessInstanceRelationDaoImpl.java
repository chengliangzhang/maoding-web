package com.maoding.process.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.process.dao.ProcessDao;
import com.maoding.process.dao.ProcessInstanceRelationDao;
import com.maoding.process.dto.ProcessDTO;
import com.maoding.process.dto.QueryProcessDTO;
import com.maoding.process.entity.ProcessEntity;
import com.maoding.process.entity.ProcessInstanceRelationEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("processInstanceRelationDao")
public class ProcessInstanceRelationDaoImpl extends GenericDao<ProcessInstanceRelationEntity> implements ProcessInstanceRelationDao {


}
