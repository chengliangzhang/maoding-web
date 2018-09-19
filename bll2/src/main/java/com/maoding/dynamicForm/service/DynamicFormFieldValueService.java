package com.maoding.dynamicForm.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.dto.SaveDynamicAuditDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;

import java.util.Map;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段- 可选择提供
 */
public interface DynamicFormFieldValueService  {

    int saveAuditDetail(SaveDynamicAuditDTO dto) throws Exception;


    /**
     * 新增表单，编辑表单，处理初始化数据
     */
    Map<String,Object> initDynamicAudit(FormFieldQueryDTO dto) throws Exception;
}
