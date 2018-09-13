package com.maoding.dynamicForm.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.dynamicForm.dto.SaveDynamicAuditDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段- 可选择提供
 */
public interface DynamicFormFieldValueService extends BaseService<DynamicFormFieldSelectableValueEntity> {

    int saveAuditDetail(SaveDynamicAuditDTO dto) throws Exception;
}
