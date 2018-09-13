package com.maoding.dynamicForm.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.dynamicForm.dto.FormDTO;
import com.maoding.dynamicForm.dto.FormQueryDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.dynamicForm.entity.DynamicFormEntity;

import java.util.List;

/**
 * 动态表单自定义的数据层接口
 */
public interface DynamicFormService extends BaseService<DynamicFormEntity> {
/*
    保存审核表样式
*/
    int insertDynamicForm (SaveDynamicFormDTO dto) throws Exception;

    /**
     * 描述       查找动态窗口模板
     * 日期       2018/9/13
     * @author   张成亮
     **/
    List<FormDTO> listForm(FormQueryDTO query);

    /**
     * 描述       查找动态窗口模板，并获得控件的位置、标题、类型等信息
     * 日期       2018/9/13
     * @author   张成亮
     **/
    FormDTO getFormDetail(FormQueryDTO query);

    /**
     * 描述       补充动态窗口模板的控件的位置、标题、类型等信息
     * 日期       2018/9/13
     * @author   张成亮
     **/
    FormDTO getFormDetail(FormDTO form);
}
