package com.maoding.dynamicForm.service;

import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.dynamicForm.dto.*;
import com.maoding.financial.dto.QueryAuditDTO;

import java.util.List;

/**
 * 动态表单自定义的数据层接口
 */
public interface DynamicFormService {

    //保存审批表模板
    int insertDynamicForm(SaveDynamicFormDTO dto) throws Exception;

    /**
     * 描述       查找动态窗口模板
     * 日期       2018/9/13
     *
     * @author 张成亮
     **/
    List<FormDTO> listForm(FormQueryDTO query);

    /**
     * 描述       查找动态窗口模板，并获得控件的位置、标题、类型等信息
     * 日期       2018/9/13
     *
     * @author 张成亮
     **/
    FormDTO getFormDetail(FormQueryDTO query);

    /**
     * 描述       补充动态窗口模板的控件的位置、标题、类型等信息
     * 日期       2018/9/13
     *
     * @author 张成亮
     **/
    FormDTO getFormDetail(FormDTO form);

    /**
     * 描述       添加及更改动态窗口模板信息
     * 日期       2018/9/14
     *
     * @author 张成亮
     **/
    FormDTO createForm(SaveDynamicFormDTO request);

    /**
     * 查询候选项
     */
    List<DynamicFormFieldSelectedValueDTO> listOptional(String fieldId);

    /**
     * 描述       添加及更改动态窗口控件信息
     * 日期       2018/9/14
     *
     * @author 张成亮
     **/
    List<DynamicFormFieldDTO> changeFormDetail(SaveDynamicFormDTO request);

    /**
     * 描述       准备用于编辑的动态窗口
     *              要编辑的动态表单模板编号为空则返回空白动态表单信息，加载指定表单信息
     * 日期       2018/9/18
     * @author   张成亮
     **/
    FormWithOptionalDTO prepareFormToEdit(FormEditDTO request);

    /**
     * 作者：FYT
     * 日期：2018/9/17
     * 描述：后台管理-审批管理-操作，seq排序对调(交换seq值)
     */
    int setDynamicFormSeq (FormGroupEditDTO dto) throws  Exception;

    /**
     * 作者：FYT
     * 日期：2018/9/18
     * 描述：后台管理-审批管理-操作，对调分组seq排序对调(交换seq值) （如：行政审批 与 财务审批 位置对调）
     */
    int updateDynamicFormSeq (FormGroupEditDTO dto) throws  Exception;

    String getFormName(String id) ;

    /**
     * 动态表单编辑时，下拉框，选择系统默认的数据，点击按钮查询系统中默认的数据的接口
     * selectType = 1，报销， 2 = 费用，3 = 请假类型
     */
    Object listSystemDefaultSelect(FormFieldQueryDTO query) throws Exception;

    List<CoreShowDTO> listAuditType(QueryAuditDTO query);
}