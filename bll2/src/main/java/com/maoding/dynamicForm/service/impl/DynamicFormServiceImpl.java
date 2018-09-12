package com.maoding.dynamicForm.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import com.maoding.dynamicForm.service.DynamicFormService;
import org.springframework.stereotype.Service;

/**
 * 动态表单自定义的数据层接口
 */
@Service("dynamicFormService")
public class DynamicFormServiceImpl extends GenericService<DynamicFormEntity> implements DynamicFormService {
}
