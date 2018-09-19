package com.maoding.dynamicForm.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldValueDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldValueEntity;

import java.util.List;

/**
 * 动态表单自定义的数据层接口- 具体业务产生的审核表单数据值
 */
public interface DynamicFormFieldValueDao extends BaseDao<DynamicFormFieldValueEntity> {


    List<DynamicFormFieldValueEntity> listDynamicFormFieldValue(String mainId);

    List<DynamicFormFieldValueDTO> listFormFieldValueByFormId(FormFieldQueryDTO query);
}
