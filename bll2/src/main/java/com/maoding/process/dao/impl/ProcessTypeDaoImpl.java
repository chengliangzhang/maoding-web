package com.maoding.process.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.process.dao.ProcessTypeDao;
import com.maoding.process.entity.ProcessTypeEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("processTypeDao")
public class ProcessTypeDaoImpl extends GenericDao<ProcessTypeEntity> implements ProcessTypeDao {


    @Override
    public ProcessTypeEntity getCurrentProcessType(String companyId, String targetType) {
        Map<String,Object> map = new HashMap<>();
        map.put("companyId",companyId);
        map.put("targetType",targetType);
        return this.sqlSession.selectOne("ProcessTypeEntityMapper.getCurrentProcessType",map);
    }
}
