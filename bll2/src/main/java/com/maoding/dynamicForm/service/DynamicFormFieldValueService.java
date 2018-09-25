package com.maoding.dynamicForm.service;

import com.maoding.dynamicForm.dto.DynamicFormFieldValueDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.dto.SaveDynamicAuditDTO;

import java.util.List;
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

    /*
     * 查询数据
     */
    List<DynamicFormFieldValueDTO> listFormFieldValueByFormId(FormFieldQueryDTO dto) throws Exception;
}
