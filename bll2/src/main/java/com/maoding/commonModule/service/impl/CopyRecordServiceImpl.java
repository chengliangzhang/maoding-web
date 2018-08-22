package com.maoding.commonModule.service.impl;

import com.maoding.commonModule.dao.CopyRecordDao;
import com.maoding.commonModule.dto.QueryCopyRecordDTO;
import com.maoding.commonModule.dto.SaveCopyRecordDTO;
import com.maoding.commonModule.entity.CopyRecordEntity;
import com.maoding.commonModule.service.CopyRecordService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.org.dto.CompanyUserDataDTO;
import com.maoding.org.service.CompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("copyRecordService")
public class CopyRecordServiceImpl extends NewBaseService implements CopyRecordService {

    @Autowired
    private CopyRecordDao copyRecordDao;

    @Autowired
    private CompanyUserService companyUserService;

    @Override
    public void saveCopyRecode(SaveCopyRecordDTO dto) throws Exception {
        CopyRecordEntity copy = (CopyRecordEntity)BaseDTO.copyFields(dto,CopyRecordEntity.class);
        //先删除原有的
        copyRecordDao.deleteRelationRecord(copy);
        for(String companyUserId:dto.getCompanyUserList()){
            copy.initEntity();
            copy.setCompanyUserId(companyUserId);
            copy.setDeleted(0);
            copyRecordDao.insert(copy);
        }
    }

    @Override
    public List<CompanyUserDataDTO> getCopyRecode(QueryCopyRecordDTO dto) throws Exception {
        return companyUserService.getCopyUser(dto);
    }

    @Override
    public boolean isExitCopyRecord(QueryCopyRecordDTO dto) {
        List<CopyRecordEntity> copyList = this.selectCopyByCompanyUserId(dto);
        if(!CollectionUtils.isEmpty(copyList)){
            return true;
        }
        return false;
    }

    @Override
    public List<CopyRecordEntity> selectCopyByCompanyUserId(QueryCopyRecordDTO dto) {
        return this.copyRecordDao.selectCopyByCompanyUserId(dto);
    }
}
