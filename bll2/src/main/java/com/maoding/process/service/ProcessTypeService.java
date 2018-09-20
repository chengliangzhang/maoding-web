package com.maoding.process.service;

import com.maoding.commonModule.dto.SaveAuditCopyDTO;
import com.maoding.dynamicForm.dto.FormGroupDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.process.entity.ProcessTypeEntity;

import java.util.List;

public interface ProcessTypeService {

    int saveAuditCopy(SaveAuditCopyDTO dto) throws Exception;

    int saveProcessType(ProcessTypeEntity processTypeEntity) throws Exception;

    //动态审批表 启用/停用
    int updateStatusDynamicForm(SaveDynamicFormDTO dto) throws Exception;

    //动态表单模板 删除
    int deleteDynamicForm(SaveDynamicFormDTO dto) throws Exception;

    //查询所有属于该分组的动态审批表
    List<ProcessTypeEntity> selectByCompanyIdFormType(FormGroupDTO formGroupDTO) throws Exception;

    //将没有分组的动态审批表，设置FormType = 4
    int updateDynamicFormType(String oldFormTypeId,String newFormTypeId) throws Exception;

    //动态表移动到其他分组
    int updateProcessTypeFormType(SaveDynamicFormDTO dto) throws Exception;

    //入参为“其他模板”的ID，删除所有属于“其他模板”下的动态表单，批量将动态表单delete设置为1；
    int deleteDynamicForm(String formType) throws Exception;
}
