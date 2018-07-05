package com.maoding.financial.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.financial.dao.ExpDetailDao;
import com.maoding.financial.dto.ExpAuditDTO;
import com.maoding.financial.dto.ExpDetailDTO;
import com.maoding.financial.dto.ExpMainDTO;
import com.maoding.financial.entity.ExpDetailEntity;
import com.maoding.financial.service.ExpDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpDetailServiceImpl
 * 描    述 : 报销明细ServiceImpl
 * 作    者 : LY
 * 日    期 : 2016/7/26-16:05
 */
@Service("expDetailService")
public class ExpDetailServiceImpl extends GenericService<ExpDetailEntity> implements ExpDetailService{
    @Autowired
    private ExpDetailDao expDetailDao;

    public List<ExpDetailDTO> getExpDetailDTO(String mainId){

        List<ExpDetailDTO> list = expDetailDao.selectDetailDTOByMainId(mainId);
        return list;
    }

}
