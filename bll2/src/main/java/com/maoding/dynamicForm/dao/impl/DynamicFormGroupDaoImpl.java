package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormGroupDao;
import com.maoding.dynamicForm.dto.FormGroupDTO;
import com.maoding.dynamicForm.dto.FormGroupQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormGroupEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态表单自定义的数据层接口
 */
@Service("dynamicFormGroupDao")
public class DynamicFormGroupDaoImpl extends GenericDao<DynamicFormGroupEntity> implements DynamicFormGroupDao {

    @Override
    public List<DynamicFormGroupEntity> listDefaultFormGroup() {
        return this.sqlSession.selectList("DynamicFormGroupEntityMapper.listDefaultFormGroup");
    }

    @Override
    public List<DynamicFormGroupEntity> listFormGroupByCompanyId(String companyId) {
        return this.sqlSession.selectList("DynamicFormGroupEntityMapper.listFormGroupByCompanyId",companyId);
    }

    @Override
    public boolean isInitFormGroup(String companyId) {
        return this.sqlSession.selectOne("DynamicFormGroupEntityMapper.isInitFormGroup",companyId);
    }

    @Override
    public Integer selectMaxSeq(String currentCompanyId) {
        return this.sqlSession.selectOne("DynamicFormGroupEntityMapper.selectMaxSeq",currentCompanyId);
    }

    /**
     * 描述       查询动态表单群组
     * 日期       2018/9/19
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<FormGroupDTO> listFormGroup(FormGroupQueryDTO query) {
        return sqlSession.selectList("DynamicFormGroupEntityMapper.listFormGroup",query);
    }
}
