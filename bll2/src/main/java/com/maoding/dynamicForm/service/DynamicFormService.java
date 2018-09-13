package com.maoding.dynamicForm.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.dynamicForm.dto.DynamicFormFieldDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.dynamicForm.entity.DynamicFormEntity;

import java.util.List;

/**
 * 动态表单自定义的数据层接口
 */
public interface DynamicFormService extends BaseService<DynamicFormEntity> {
/*
    保存审核表样式
*/
    int insertDynamicForm (SaveDynamicFormDTO dto) throws Exception;
}
