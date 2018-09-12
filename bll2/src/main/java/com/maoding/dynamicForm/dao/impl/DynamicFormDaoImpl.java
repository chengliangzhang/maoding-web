package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormDao;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import org.springframework.stereotype.Service;

/**
 * 动态表单自定义的数据层接口
 */
@Service("dynamicFormDao")
public class DynamicFormDaoImpl extends GenericDao<DynamicFormEntity> implements DynamicFormDao{


}
