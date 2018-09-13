package com.maoding.dynamicForm.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldSelectedValueDTO;
import com.maoding.dynamicForm.dto.FormFieldOptionalQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;

import java.util.List;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段- 可选择提供
 */
public interface DynamicFormFieldSelectableValueDao extends BaseDao<DynamicFormFieldSelectableValueEntity> {

    /**
     * 描述       查找可选项
     * 日期       2018/9/13
     * @author   张成亮
     **/
    List<DynamicFormFieldSelectedValueDTO> listOptional(FormFieldOptionalQueryDTO query);
}
