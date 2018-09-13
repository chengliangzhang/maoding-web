package com.maoding.process.dao.impl;

import com.maoding.activiti.dto.ProcessDefineGroupDTO;
import com.maoding.activiti.dto.ProcessDefineQueryDTO;
import com.maoding.core.base.dao.GenericDao;
import com.maoding.process.dao.ProcessTypeDao;
import com.maoding.process.entity.ProcessTypeEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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

    /**
     * 描述       获取流程列表
     * 日期       2018/9/13
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<ProcessDefineGroupDTO> listProcessDefineWithGroup(ProcessDefineQueryDTO query) {
        return sqlSession.selectList("ProcessTypeEntityMapper.listProcessDefineWithGroup",query);
    }
}
