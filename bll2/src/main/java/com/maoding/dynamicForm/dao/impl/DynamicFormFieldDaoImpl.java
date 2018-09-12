package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldDao;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldEntity;
import org.springframework.stereotype.Service;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段
 */

@Service("dynamicFormFieldDao")
public class DynamicFormFieldDaoImpl extends GenericDao<DynamicFormFieldEntity> implements DynamicFormFieldDao{


}
