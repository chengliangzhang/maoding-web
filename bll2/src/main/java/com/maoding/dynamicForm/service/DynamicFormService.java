package com.maoding.dynamicForm.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.dynamicForm.dto.FormDetailDTO;
import com.maoding.dynamicForm.dto.FormDetailQueryDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.dynamicForm.entity.DynamicFormEntity;

/**
 * 动态表单自定义的数据层接口
 */
public interface DynamicFormService extends BaseService<DynamicFormEntity> {
/*
    保存审核表样式
*/
    int insertDynamicForm (SaveDynamicFormDTO dto) throws Exception;

    FormDetailDTO getFormDetail(FormDetailQueryDTO query);
}
