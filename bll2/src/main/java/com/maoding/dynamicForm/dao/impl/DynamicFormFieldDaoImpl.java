package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormFieldEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段
 */

@Service("dynamicFormFieldDao")
public class DynamicFormFieldDaoImpl extends GenericDao<DynamicFormFieldEntity> implements DynamicFormFieldDao{

    /**
     * 描述       查询控件信息
     * 日期       2018/9/13
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<DynamicFormFieldDTO> listFormField(FormFieldQueryDTO query) {
        return sqlSession.selectList("DynamicFormFieldEntityMapper.listFormField",query);
    }


}
