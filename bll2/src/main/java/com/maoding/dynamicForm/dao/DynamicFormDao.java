package com.maoding.dynamicForm.dao;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.dynamicForm.dto.FormDTO;
import com.maoding.dynamicForm.dto.FormQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormEntity;

import java.util.List;

/**
 * 动态表单自定义的数据层接口
 */
public interface DynamicFormDao extends BaseDao<DynamicFormEntity> {
    /**
     * 描述       查询动态表单模板列表
     * 日期       2018/9/13
     * @author   张成亮
     **/
    List<FormDTO> listForm(FormQueryDTO query);

    /**
     * 描述       仅获取一个表单模板
     * 日期       2018/9/20
     * @author   张成亮
     **/
    FormDTO getForm(FormQueryDTO query);

    List<DynamicFormEntity> listDynamicFormByType(String formType);

}
