package com.maoding.commonModule.service;

import com.maoding.commonModule.dto.QueryRelationRecordDTO;
import com.maoding.commonModule.dto.SaveRelationRecordDTO;
import com.maoding.commonModule.entity.RelationRecordEntity;
import com.maoding.financial.dto.AuditDataDTO;

public interface RelationRecordService {

    void saveRelationRecord(SaveRelationRecordDTO dto) throws Exception;

    AuditDataDTO getRelationList(QueryRelationRecordDTO query) throws Exception;

    RelationRecordEntity getRelationRecord(String mainId);

}
