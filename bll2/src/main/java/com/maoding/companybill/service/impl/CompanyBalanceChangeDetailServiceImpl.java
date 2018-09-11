package com.maoding.companybill.service.impl;

import com.maoding.companybill.dao.CompanyBalanceChangeDetailDao;
import com.maoding.companybill.dao.CompanyBalanceDao;
import com.maoding.companybill.dto.CompanyBalanceChangeDetailDTO;
import com.maoding.companybill.dto.QueryCompanyBalanceDTO;
import com.maoding.companybill.dto.SaveCompanyBalanceChangeDetailDTO;
import com.maoding.companybill.dto.SaveCompanyBalanceDTO;
import com.maoding.companybill.entity.CompanyBalanceChangeDetailEntity;
import com.maoding.companybill.entity.CompanyBalanceEntity;
import com.maoding.companybill.service.CompanyBalanceChangeDetailService;
import com.maoding.companybill.service.CompanyBalanceService;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;
import com.maoding.statistic.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("companyBalanceChangeDetailService")
public class CompanyBalanceChangeDetailServiceImpl extends NewBaseService implements CompanyBalanceChangeDetailService{

    @Autowired
    private CompanyBalanceChangeDetailDao companyBalanceChangeDetailDao;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    CompanyBalanceDao companyBalanceDao;
    @Autowired
    CompanyBalanceService companyBalanceService;

    public int SaveCompanyBalanceChangeDetail(SaveCompanyBalanceChangeDetailDTO dto) throws Exception{
        String companyId = dto.getCompanyId();
        CompanyBalanceChangeDetailEntity entity = new CompanyBalanceChangeDetailEntity();
        BeanUtils.copyProperties(dto,entity);
        //如果公司。没有设置，录入余额初始值 or 录入余额初始日期 or 设置最低余额。当前余额没设置的，添加记录
        //获取当前公司的BanlanceId
        //todo 1.如果balanceId==null，就新增一条空的记录，因为 CompanyBalanceChangeDetailEntity 的balanceId是关联CompanyBalanceEntity 中的id
        String balanceId = dto.getBalanceId();
        if(StringUtil.isNullOrEmpty(balanceId)){
            balanceId = companyBalanceService.saveCompanyBalance(companyId);
        }

        //TODO 2.查询变更前的金额(当前金额/余额)，用来设置 改变前的余额（beforeChangeAmount） 和 改变后的余额（afterChangeAmount）
        StatisticDetailSummaryDTO data = statisticService.getStatisticDetailSummaryByCompanyId(companyId);
        BigDecimal currentBalance = new BigDecimal(data.getAmount()==null?null:data.getAmount().toString());
        //变更金额
        BigDecimal currentAmount = new BigDecimal(dto.getChangeAmount());
        BigDecimal afterChangeAmount = new BigDecimal("0");
        if(1 == dto.getChangeType()){//如果 变更类型：1=增加余额值，2=减少余额值
            //变更后的金额（当前金额+变更金额）
            afterChangeAmount =currentBalance.add(currentAmount);
        }
        if(2 == dto.getChangeType()){//如果 变更类型：1=增加余额值，2=减少余额值
            //变更后的金额（当前金额-变更金额）
            afterChangeAmount =currentBalance.subtract(currentAmount);
        }
        entity.setBeforeChangeAmount(currentBalance.toString());
        entity.setAfterChangeAmount(afterChangeAmount.toString());
        //TODO 3.保存
        if (StringUtil.isNullOrEmpty(dto.getId())){
            entity.initEntity();
            entity.setDeleted(0);
            entity.setChangeDate(DateUtils.getDate());
            entity.setBalanceId(balanceId);
            entity.setOperatorId(dto.getCurrentCompanyUserId());
            return companyBalanceChangeDetailDao.insert(entity);
        }else{
            return companyBalanceChangeDetailDao.updateById(entity);
        }
    }

    @Override
    public List<CompanyBalanceChangeDetailDTO> listCompanyBalanceChangeDetail(QueryCompanyBalanceDTO dto) throws Exception {
        List<CompanyBalanceChangeDetailEntity> list = companyBalanceChangeDetailDao.listCompanyBalanceChangeDetail(dto.getId());
        return BeanUtils.createListFrom(list,CompanyBalanceChangeDetailDTO.class);
    }
}
