package com.maoding.commonModule.service;

import com.maoding.commonModule.dto.AuditCopyDTO;
import com.maoding.commonModule.dto.AuditCopyDataDTO;
import com.maoding.commonModule.dto.SaveAuditCopyDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.org.dto.CompanyUserDataDTO;

import java.util.List;

public interface AuditCopyService {

    /**
     * 动态表单：保存知会人
     */
    int saveAuditCopy(SaveAuditCopyDTO dto) throws Exception;

    List<AuditCopyDataDTO> listAuditCopy(String targetId);

    List<CompanyUserDataDTO> listAuditCopyUser(String companyId,String formId);
}
