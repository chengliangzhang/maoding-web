package com.maoding.dynamicForm.service;

import com.maoding.dynamicForm.dto.FormGroupDTO;
import com.maoding.dynamicForm.dto.FormGroupEditDTO;
import com.maoding.dynamicForm.dto.FormGroupQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormGroupEntity;

import java.util.List;

public interface DynamicFormGroupService {

    /**
     * 描述       添加及更改动态窗口群组
     * 日期       2018/9/14
     * @author   张成亮
     **/
    FormGroupDTO saveDynamicFormGroup(FormGroupEditDTO request) throws Exception;

    /**
     * 删除分组
     */
    int deleteDynamicFormGroup(FormGroupEditDTO request) throws Exception;


    /**
     * 获取 groupName = 其他模板
     */
    DynamicFormGroupEntity getOtherDynamicFormGroup(String companyId);

    /**
     * 初始化话分组
     */
    void initDynamicFormGroup(String companyId) throws Exception;

    //查询当前公司的分组
    List<DynamicFormGroupEntity> listFormGroupByCompanyId(FormGroupEditDTO dto) throws Exception;

    /**
     * 描述       查询公司分组
     * 日期       2018/9/19
     * @author   张成亮
     **/
    List<FormGroupDTO> listFormGroup(FormGroupQueryDTO query);
}
