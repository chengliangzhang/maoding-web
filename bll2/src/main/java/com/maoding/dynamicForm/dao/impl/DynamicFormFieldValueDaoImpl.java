package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldValueDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldValueDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormFieldValueEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态表单自定义的数据层接口- 具体业务产生的审核表单数据值
 */
@Service("dynamicFormFieldValueDao")
public class DynamicFormFieldValueDaoImpl extends GenericDao<DynamicFormFieldValueEntity> implements DynamicFormFieldValueDao {


    @Override
    public List<DynamicFormFieldValueEntity> listDynamicFormFieldValue(String mainId) {
        return this.sqlSession.selectList("DynamicFormFieldValueEntityMapper.listDynamicFormFieldValue",mainId);
    }

    @Override
    public List<DynamicFormFieldValueDTO> listFormFieldValueByFormId(FormFieldQueryDTO query) {
        return sqlSession.selectList("DynamicFormFieldValueEntityMapper.listFormFieldValueByFormId",query);
    }

    @Override
    public double getConditionValue(String mainId, String conditionFieldId) {
        Map<String,Object> map = new HashMap<>();
        map.put("mainId",mainId);
        map.put("conditionFieldId",conditionFieldId);
        return sqlSession.selectOne("DynamicFormFieldValueEntityMapper.getConditionValue",map);
    }
}
