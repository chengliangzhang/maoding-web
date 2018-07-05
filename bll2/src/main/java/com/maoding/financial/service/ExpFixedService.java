package com.maoding.financial.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.financial.dto.ExpFixedAmountDTO;
import com.maoding.financial.dto.ExpFixedMainDTO;
import com.maoding.financial.entity.ExpFixedEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpFixedService
 * 描    述 : 固定支出Service
 * 作    者 : LY
 * 日    期 : 2016/8/4-11:07
 */
public interface ExpFixedService extends BaseService<ExpFixedEntity> {

    /**
     * 方法描述：查询选择月的固定支出
     * 作   者：MaoSF
     * 日   期：2017/12/1 15:25
     * @param  expDate 月份
     */
    ExpFixedMainDTO getExpFixedByExpDate(String companyId, String expDate) throws Exception;

    /**
     * 方法描述：查询选择月的固定支出
     */
    AjaxMessage saveExpFixedByExpDate(ExpFixedMainDTO dto) throws Exception;


    List<ExpFixedAmountDTO> getExpFixedByMonth(String companyId, String year);

    ExpFixedAmountDTO getExpFixedByMonth2(String companyId, String month);

}
