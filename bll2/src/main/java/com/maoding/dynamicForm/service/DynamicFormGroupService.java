package com.maoding.dynamicForm.service;

import com.maoding.dynamicForm.dto.FormGroupDTO;
import com.maoding.dynamicForm.dto.FormGroupEditDTO;

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
     * 初始化话分组
     */
    void initDynamicFormGroup(String companyId) throws Exception;


}
