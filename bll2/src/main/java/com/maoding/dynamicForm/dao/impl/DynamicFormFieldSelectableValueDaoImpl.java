package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldSelectableValueDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldSelectedValueDTO;
import com.maoding.dynamicForm.dto.FormFieldOptionalQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段- 可选择提供
 */

@Service("dynamicFormFieldSelectableValueDao")
public class DynamicFormFieldSelectableValueDaoImpl extends GenericDao<DynamicFormFieldSelectableValueEntity> implements DynamicFormFieldSelectableValueDao {

    /**
     * 描述       查找可选项
     * 日期       2018/9/13
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<DynamicFormFieldSelectedValueDTO> listOptional(FormFieldOptionalQueryDTO query) {
        return sqlSession.selectList("DynamicFormFieldSelectableValueEntityMapper.listOptional",query);
    }
}
