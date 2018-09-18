package com.maoding.commonModule.service;

import com.maoding.commonModule.dto.AuditCopyDTO;
import com.maoding.commonModule.dto.AuditCopyDataDTO;
import com.maoding.commonModule.dto.SaveAuditCopyDTO;

import java.util.List;

public interface AuditCopyService {

    /**
     * 动态表单：保存知会人
     */
    int saveAuditCopy(SaveAuditCopyDTO dto) throws Exception;

    List<AuditCopyDataDTO> listAuditCopy(String targetId);
}
