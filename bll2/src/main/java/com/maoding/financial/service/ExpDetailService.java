package com.maoding.financial.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.financial.dto.ExpDetailDTO;
import com.maoding.financial.dto.ExpMainDTO;
import com.maoding.financial.entity.ExpDetailEntity;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpDetailService
 * 描    述 : 报销明细Service
 * 作    者 : LY
 * 日    期 : 2016/7/26-15:58
 */
public interface ExpDetailService extends BaseService<ExpDetailEntity>{
    /*

     */
    public List<ExpDetailDTO> getExpDetailDTO(String id);
}
