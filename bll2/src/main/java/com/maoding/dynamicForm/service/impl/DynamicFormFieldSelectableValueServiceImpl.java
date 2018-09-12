package com.maoding.dynamicForm.service.impl;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.service.GenericService;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;
import com.maoding.dynamicForm.service.DynamicFormFieldSelectableValueService;
import org.springframework.stereotype.Service;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段- 可选择提供
 */
@Service("dynamicFormFieldSelectableValueService")
public class DynamicFormFieldSelectableValueServiceImpl extends GenericService<DynamicFormFieldSelectableValueEntity> implements DynamicFormFieldSelectableValueService {
}
