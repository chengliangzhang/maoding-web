package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormGroupDao;
import com.maoding.dynamicForm.entity.DynamicFormGroupEntity;
import org.springframework.stereotype.Service;

/**
 * 动态表单自定义的数据层接口
 */
@Service("dynamicFormGroupDao")
public class DynamicFormGroupDaoImpl extends GenericDao<DynamicFormGroupEntity> implements DynamicFormGroupDao {
}
