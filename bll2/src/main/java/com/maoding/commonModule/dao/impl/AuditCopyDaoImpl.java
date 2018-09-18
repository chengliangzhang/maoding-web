package com.maoding.commonModule.dao.impl;

import com.maoding.commonModule.dao.AuditCopyDao;
import com.maoding.commonModule.dto.AuditCopyDataDTO;
import com.maoding.commonModule.entity.AuditCopyEntity;
import com.maoding.core.base.dao.GenericDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("auditCopyDao")
public class AuditCopyDaoImpl extends GenericDao<AuditCopyEntity> implements AuditCopyDao {

    @Override
    public int deleteAuditCopyByTargetId(String targetId) {
        return this.sqlSession.update("AuditCopyEntityMapper.deleteAuditCopyByTargetId",targetId);
    }

    @Override
    public List<AuditCopyDataDTO> listAuditCopy(String targetId) {
        return this.sqlSession.selectList("AuditCopyEntityMapper.listAuditCopy",targetId);
    }
}
