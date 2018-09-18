package com.maoding.dynamicForm.service;

import com.maoding.dynamicForm.dto.*;

import java.util.List;

/**
 * 动态表单自定义的数据层接口
 */
public interface DynamicFormService {

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
     * 作者：FYT
     * 日期：2018/9/17
     * 描述：后台管理-审批管理-操作，seq排序对调(交换seq值)
     */
    int setDynamicFormSeq (SaveDynamicFormDTO dto,SaveDynamicFormDTO dto2) throws  Exception;

    /**
     * 描述       准备用于编辑的动态窗口
     *              要编辑的动态表单模板编号为空则返回空白动态表单信息，加载指定表单信息
     * 日期       2018/9/18
     * @author   张成亮
     **/
    FormWithOptionalDTO prepareFormDetail(FormEditDTO request);

}
