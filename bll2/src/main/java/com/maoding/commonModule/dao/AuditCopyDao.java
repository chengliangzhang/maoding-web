package com.maoding.commonModule.dao;

import com.maoding.commonModule.dto.AuditCopyDataDTO;
import com.maoding.commonModule.dto.QueryAuditCopyDTO;
import com.maoding.commonModule.entity.AuditCopyEntity;
import com.maoding.core.base.dao.BaseDao;
import com.maoding.org.dto.CompanyUserDataDTO;

import java.util.List;

public interface AuditCopyDao extends BaseDao<AuditCopyEntity> {

    int deleteAuditCopyByTargetId(String targetId);

    List<AuditCopyDataDTO> listAuditCopy(String targetId);

    List<CompanyUserDataDTO> listAuditCopyUser(QueryAuditCopyDTO query);
}
