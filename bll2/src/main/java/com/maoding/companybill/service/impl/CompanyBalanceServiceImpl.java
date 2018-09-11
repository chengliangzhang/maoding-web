package com.maoding.companybill.service.impl;

import com.maoding.companybill.dao.CompanyBalanceDao;
import com.maoding.companybill.dao.CompanyBillDao;
import com.maoding.companybill.dto.CompanyBalanceDTO;
import com.maoding.companybill.dto.QueryCompanyBalanceDTO;
import com.maoding.companybill.dto.SaveCompanyBalanceDTO;
import com.maoding.companybill.dto.SaveCompanyBillDTO;
import com.maoding.companybill.entity.CompanyBalanceEntity;
import com.maoding.companybill.entity.CompanyBillEntity;
import com.maoding.companybill.service.CompanyBalanceService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.CompanyBillType;
import com.maoding.core.util.BeanUtilsEx;
import com.maoding.core.util.DateUtils;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.entity.CompanyEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.statistic.dto.StatisticDetailSummaryDTO;
import com.maoding.statistic.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("companyBalanceService")
public class CompanyBalanceServiceImpl extends NewBaseService implements CompanyBalanceService {

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyBillDao companyBillDao;

    @Autowired
    private CompanyBalanceDao companyBalanceDao;

    @Autowired
    private StatisticService statisticService;

    @Override
    public int saveCompanyBalance(SaveCompanyBalanceDTO dto) throws Exception {
        CompanyBalanceEntity balanceDatabase = companyBalanceDao.getCompanyBalanceByCompanyId(dto.getCompanyId());
        CompanyBalanceEntity balance = (CompanyBalanceEntity)BaseDTO.copyFields(dto,CompanyBalanceEntity.class);
        if(balanceDatabase==null){
            balance.initEntity();
            balance.setCreateBy(dto.getAccountId());
            return companyBalanceDao.insert(balance);
        }else {
            if("setBalanceDate".equals(dto.getType())){
                balanceDatabase.setSetBalanceDate(dto.getSetBalanceDate());
            }else {
                BeanUtilsEx.setPropertyValue(balanceDatabase,dto.getType(), (String)BeanUtilsEx.getProperty(dto,dto.getType()));
            }

            balanceDatabase.setUpdateBy(dto.getAccountId());
            balanceDatabase.setUpdateDate(DateUtils.getDate());
            return companyBalanceDao.update(balanceDatabase);
        }
    }

    @Override
    public String saveCompanyBalance(String companyId) throws Exception {
        CompanyBalanceEntity balance = new CompanyBalanceEntity();
        balance.initEntity();
        balance.setCompanyId(companyId);
        companyBalanceDao.insert(balance);
        return balance.getId();
    }

    @Override
    public List<CompanyBalanceDTO> getCompanyBalance(QueryCompanyBalanceDTO query) throws Exception {
        List<CompanyBalanceDTO> result = new ArrayList<>();
        List<CompanyEntity> list  = companyService.getAllOrg(query.getCurrentCompanyId());
        for(CompanyEntity c:list){
            result.add(getBalanceByCompanyId(c.getId(),c.getCompanyName()));
//            if(c.getId().equals(query.getCurrentCompanyId()) || !"1".equals(c.getRelationType())){
//
//            }
        }
        return result;
    }

    private CompanyBalanceDTO getBalanceByCompanyId(String companyId,String companyName){
        CompanyBalanceDTO balance = new CompanyBalanceDTO();
        balance.setCompanyId(companyId);
        balance.setCompanyName(companyName);
        // todo 查询余额
        StatisticDetailSummaryDTO data = statisticService.getStatisticDetailSummaryByCompanyId(companyId);
        if(data.getBalance()!=null){
            balance.setInitialBalance(data.getBalance().getInitialBalance());
            balance.setLowBalance(data.getBalance().getLowBalance());
            balance.setSetBalanceDate(data.getBalance().getSetBalanceDate());
            balance.setId(data.getBalance().getId());
        }
        balance.setCurrentIncome(data.getGain().toString());
        balance.setCurrentBalance(data.getAmount().toString());
        //todo 上月支出
        String lastMonth = DateUtils.getLastMonth();
        SaveCompanyBillDTO queryBill = new SaveCompanyBillDTO();
        queryBill.setCompanyId(companyId);
        queryBill.setPaymentDate(lastMonth);
        queryBill.setFeeType(CompanyBillType.FEE_TYPE_EXP_FIX);
        List<CompanyBillEntity> billList = companyBillDao.getCompanyBill(queryBill);
        if(!CollectionUtils.isEmpty(billList)){
            balance.setLastMonthFixFee(billList.get(0).getFee().toString());
        }
        return balance;
    }

    @Override
    public CompanyBalanceEntity getCompanyBalanceByCompanyId(String companyId) {
        return companyBalanceDao.getCompanyBalanceByCompanyId(companyId);
    }

    @Override
    public boolean isCanBeAllocate(String companyId, String amount, String paymentDate) throws Exception {
        //查询组织的余额信息
        double currentBalance = 0;
        double currentPayAmount = Double.parseDouble(amount);
        CompanyBalanceDTO balance = this.getBalanceByCompanyId(companyId, null);
        if(balance==null || balance.getLowBalance()==null
                || (balance.getSetBalanceDate()!=null && DateUtils.datecompareDate(paymentDate,DateUtils.formatDate(balance.getSetBalanceDate()))<1)){//如果没有设置最低余额，则
            return true;
        }else if(balance.getCurrentBalance()!=null){
            currentBalance =  Double.parseDouble(balance.getCurrentBalance());
            double balanceAmount = currentBalance-currentPayAmount- Double.parseDouble(balance.getLowBalance());
            BigDecimal   b   =   new   BigDecimal(balanceAmount);
            if(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()>0){
                return true;
            }
        }
        return false;
    }
}
