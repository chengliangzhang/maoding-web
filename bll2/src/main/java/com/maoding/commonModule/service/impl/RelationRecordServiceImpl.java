package com.maoding.commonModule.service.impl;

import com.maoding.commonModule.dao.RelationRecordDao;
import com.maoding.commonModule.dto.RelationTypeDTO;
import com.maoding.commonModule.dto.SaveRelationRecordDTO;
import com.maoding.commonModule.entity.RelationRecordEntity;
import com.maoding.commonModule.service.RelationRecordService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.financial.dao.ExpMainDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("relationRecordService")
public class RelationRecordServiceImpl extends NewBaseService implements RelationRecordService {

    @Autowired
    private RelationRecordDao relationRecordDao;

    @Autowired
    private ExpMainDao expMainDao;

    @Override
    public void saveRelationRecord(SaveRelationRecordDTO dto) throws Exception {
        RelationRecordEntity relationRecord =(RelationRecordEntity)BaseDTO.copyFields(dto,RelationRecordEntity.class);
        //先删除原有的记录
        relationRecordDao.deleteRelationRecord(relationRecord);
        //再新增传递过来的记录
        for(RelationTypeDTO relation:dto.getRelationList()){
            relationRecord.initEntity();
            relationRecord.setDeleted(0);
            relationRecord.setRecordType(relation.getRecordType());
            relationRecord.setRelationId(relation.getRelationId());
            relationRecord.setOperateRecordId(relation.getOperateRecordId());
            relationRecord.setCreateBy(dto.getAccountId());
            relationRecordDao.insert(relationRecord);
        }
    }

//    @Override
//    public AuditDataDTO getRelationList(QueryRelationRecordDTO query) throws Exception {
//        List<AuditDataDTO> list = new ArrayList<>();
//        List<RelationRecordEntity> relationList = this.relationRecordDao.getRelationRecord(query);
//        for(RelationRecordEntity relation:relationList){
//            QueryAuditDTO queryAudit = new QueryAuditDTO();
//            queryAudit.setMainId(relation.getRelationId());
//            list.addAll(expMainDao.getAuditData(queryAudit));
//        }
//        return CollectionUtils.isEmpty(list)?null:list.get(0);
//    }
}
