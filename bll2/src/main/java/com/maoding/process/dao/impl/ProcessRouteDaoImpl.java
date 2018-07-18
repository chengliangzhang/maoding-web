package com.maoding.process.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.process.dao.ProcessRouteDao;
import com.maoding.process.entity.ProcessRouteEntity;
import org.springframework.stereotype.Service;

@Service("processRouteDao")
public class ProcessRouteDaoImpl extends GenericDao<ProcessRouteEntity> implements ProcessRouteDao {
}
