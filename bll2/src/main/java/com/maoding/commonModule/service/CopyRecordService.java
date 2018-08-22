package com.maoding.commonModule.service;

import com.maoding.commonModule.dto.QueryCopyRecordDTO;
import com.maoding.commonModule.dto.SaveCopyRecordDTO;
import com.maoding.commonModule.entity.CopyRecordEntity;
import com.maoding.org.dto.CompanyUserDataDTO;

import java.util.List;

public interface CopyRecordService {

    /**
     * 保存抄送记录
     */
    void saveCopyRecode(SaveCopyRecordDTO dto) throws Exception;

    List<CompanyUserDataDTO> getCopyRecode(QueryCopyRecordDTO dto) throws Exception;

    boolean isExitCopyRecord(QueryCopyRecordDTO dto);

    List<CopyRecordEntity> selectCopyByCompanyUserId(QueryCopyRecordDTO dto);
}
