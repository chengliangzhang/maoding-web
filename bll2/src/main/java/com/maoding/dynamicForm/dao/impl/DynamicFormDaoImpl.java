package com.maoding.dynamicForm.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.dynamicForm.dao.DynamicFormDao;
import com.maoding.dynamicForm.dto.FormDTO;
import com.maoding.dynamicForm.dto.FormQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态表单自定义的数据层接口
 */
@Service("dynamicFormDao")
public class DynamicFormDaoImpl extends GenericDao<DynamicFormEntity> implements DynamicFormDao{

    /**
     * 描述       查找动态窗口模板
     * 日期       2018/9/13
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<FormDTO> listForm(FormQueryDTO query) {
        return sqlSession.selectList("DynamicFormEntityMapper.listForm",query);
    }
}
