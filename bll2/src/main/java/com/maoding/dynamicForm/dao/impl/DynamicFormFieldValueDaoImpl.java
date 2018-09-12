package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldValueDao;
import com.maoding.dynamicForm.entity.DynamicFormFieldValueEntity;
import org.springframework.stereotype.Service;

/**
 * 动态表单自定义的数据层接口- 具体业务产生的审核表单数据值
 */
@Service("dynamicFormFieldValueDao")
public class DynamicFormFieldValueDaoImpl extends GenericDao<DynamicFormFieldValueEntity> implements DynamicFormFieldValueDao {


}
