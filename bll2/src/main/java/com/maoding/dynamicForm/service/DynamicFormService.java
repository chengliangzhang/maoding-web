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

    /**
     * 描述       获取动态窗口组件位置、标题等信息
     * 日期       2018/9/13
     * @author   张成亮
     **/
    FormDetailDTO getFormDetail(FormDetailQueryDTO query);
}
