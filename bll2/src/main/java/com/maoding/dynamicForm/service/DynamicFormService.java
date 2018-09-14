package com.maoding.dynamicForm.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.dynamicForm.dto.*;
import com.maoding.dynamicForm.entity.DynamicFormEntity;

import java.util.List;

/**
 * 动态表单自定义的数据层接口
 */
public interface DynamicFormService extends BaseService<DynamicFormEntity> {

    //保存审批表模板
    int insertDynamicForm (SaveDynamicFormDTO dto) throws Exception;

    //审批表 启用/停用
    int startOrStopDynamicForm (SaveDynamicFormDTO dto) throws  Exception;

    //审批表 删除
    int deleteDynamicForm (SaveDynamicFormDTO dto) throws  Exception;

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
    
    /**
     * 描述       添加及更改动态窗口模板信息
     * 日期       2018/9/14
     * @author   张成亮
     **/
    FormDTO changeForm(SaveDynamicFormDTO request);

    /**
     * 描述       添加及更改动态窗口控件信息
     * 日期       2018/9/14
     * @author   张成亮
     **/
    List<DynamicFormFieldDTO> changeFormDetail(SaveDynamicFormDTO request);

    /**
     * 描述       添加及更改动态窗口群组
     * 日期       2018/9/14
     * @author   张成亮
     **/
    FormGroupDTO changeFormGroup(FormGroupEditDTO request);
}
