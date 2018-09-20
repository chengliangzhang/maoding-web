package com.maoding.process.dao.impl;

import com.maoding.activiti.dto.ProcessDefineDTO;
import com.maoding.activiti.dto.ProcessDefineGroupDTO;
import com.maoding.activiti.dto.ProcessDefineQueryDTO;
import com.maoding.core.base.dao.GenericDao;
import com.maoding.core.util.ObjectUtils;
import com.maoding.core.util.TraceUtils;
import com.maoding.dynamicForm.dto.FormGroupDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
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
        List<ProcessTypeEntity> list = sqlSession.selectList("ProcessTypeEntityMapper.getCurrentProcessType",map);
        TraceUtils.check(ObjectUtils.isNotEmpty(list) && list.size() == 1,"~查询流程信息时产生错误，流程个数不对");
        return ObjectUtils.getFirst(list);
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

    /**
     * 描述       获取流程列表
     * 日期       2018/9/13
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<ProcessDefineDTO> listProcessDefine(ProcessDefineQueryDTO query) {
        return sqlSession.selectList("ProcessTypeEntityMapper.listProcessDefine",query);
    }

    /**
     * 作者：FYT
     * 日期：2018/9/17
     * 描述：审批表 启用/停用。根据dto中的id，和当前组织去查询ProcessTypeEntity（form_id = dto.id,companyId = dto.currentCompanyId)
     */
    @Override
    public ProcessTypeEntity selectByTargetType(SaveDynamicFormDTO query) {
        return sqlSession.selectOne("ProcessTypeEntityMapper.selectByTargetType",query);
    }

    /**
     * 作者：FYT
     * 日期：2018/9/17
     * 描述：根据公司查询排序值(Seq如最大为10，返回+1，结果11)
     */
    @Override
    public Integer selectMaxSeq(String currentCompanyId) {
        return sqlSession.selectOne("ProcessTypeEntityMapper.selectMaxSeq",currentCompanyId);
    }

    @Override
    public List<ProcessTypeEntity> selectByCompanyIdFormType(FormGroupDTO formGroupDTO) {
        return sqlSession.selectList("ProcessTypeEntityMapper.selectByCompanyIdFormType",formGroupDTO);
    }

    @Override
    public int updateDynamicFormType(String oldFormType, String newFormType) {
        Map<String,Object> map = new HashMap<>();
        map.put("oldFormType",oldFormType);
        map.put("newFormType",newFormType);
        return this.sqlSession.update("ProcessTypeEntityMapper.updateDynamicFormType",map);
    }

}
