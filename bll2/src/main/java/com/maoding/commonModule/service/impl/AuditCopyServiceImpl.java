package com.maoding.commonModule.service.impl;

import com.maoding.commonModule.dao.AuditCopyDao;
import com.maoding.commonModule.dto.AuditCopyDataDTO;
import com.maoding.commonModule.dto.SaveAuditCopyDTO;
import com.maoding.commonModule.entity.AuditCopyEntity;
import com.maoding.commonModule.service.AuditCopyService;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("auditCopyService")
public class AuditCopyServiceImpl extends NewBaseService implements AuditCopyService {

    @Autowired
    private AuditCopyDao auditCopyDao;

    @Override
    public int saveAuditCopy(SaveAuditCopyDTO dto) throws Exception {
        //1.删除原有的记录
        auditCopyDao.deleteAuditCopyByTargetId(dto.getTargetId());
        //2.添加现有的记录
        dto.getCopyList().stream().forEach(copy->{
            AuditCopyEntity auditCopy = BeanUtils.createFrom(copy,AuditCopyEntity.class);
            auditCopy.initEntity();
            auditCopy.setTargetId(dto.getTargetId());
            auditCopy.setTargetType(dto.getTargetType());
            auditCopyDao.insert(auditCopy);
        });
        return 1;
    }

    @Override
    public List<AuditCopyDataDTO> listAuditCopy(String targetId) {
        return auditCopyDao.listAuditCopy(targetId);
    }
}
