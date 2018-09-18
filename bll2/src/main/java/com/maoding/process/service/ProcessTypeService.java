package com.maoding.process.service;

import com.maoding.commonModule.dto.SaveAuditCopyDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.process.entity.ProcessTypeEntity;

public interface ProcessTypeService {

    int saveAuditCopy(SaveAuditCopyDTO dto) throws Exception;

    int saveProcessType(ProcessTypeEntity processTypeEntity) throws Exception;

    //动态审批表 启用/停用
    int updateStatusDynamicForm(SaveDynamicFormDTO dto) throws Exception;

    //动态表单模板 删除
    int deleteDynamicForm(SaveDynamicFormDTO dto) throws Exception;
}
