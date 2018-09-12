package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldSelectableValueDao;
import com.maoding.dynamicForm.entity.DynamicFormFieldEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;
import org.springframework.stereotype.Service;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段- 可选择提供
 */

@Service("dynamicFormFieldSelectableValueDao")
public class DynamicFormFieldSelectableValueDaoImpl extends GenericDao<DynamicFormFieldSelectableValueEntity> implements DynamicFormFieldSelectableValueDao {


}
