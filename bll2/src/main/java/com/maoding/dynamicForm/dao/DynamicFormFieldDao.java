package com.maoding.dynamicForm.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldDTO;
import com.maoding.dynamicForm.dto.DynamicFormFieldValueDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormFieldEntity;

import java.util.List;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段
 */
public interface DynamicFormFieldDao extends BaseDao<DynamicFormFieldEntity> {

    /**
     * 描述       查询控件信息
     * 日期       2018/9/13
     * @author   张成亮
     **/
    List<DynamicFormFieldDTO> listFormField(FormFieldQueryDTO query);


}
