package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormGroupDao;
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
}
