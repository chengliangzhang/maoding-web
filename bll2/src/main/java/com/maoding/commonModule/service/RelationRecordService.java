package com.maoding.commonModule.service;

import com.maoding.commonModule.dto.QueryRelationRecordDTO;
import com.maoding.commonModule.dto.SaveRelationRecordDTO;

import java.util.List;

public interface RelationRecordService {

    void saveRelationRecord(SaveRelationRecordDTO dto) throws Exception;

//    AuditDataDTO getRelationList(QueryRelationRecordDTO query) throws Exception;

}
