package com.maoding.financial.service.impl;

import com.beust.jcommander.internal.Maps;
import com.maoding.companybill.dto.SaveCompanyBillDTO;
import com.maoding.companybill.service.CompanyBalanceService;
import com.maoding.companybill.service.CompanyBillService;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.CompanyBillType;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.exception.CustomException;
import com.maoding.financial.dao.ExpFixedDao;
import com.maoding.financial.dto.ExpFixedAmountDTO;
import com.maoding.financial.dto.ExpFixedDTO;
import com.maoding.financial.dto.ExpFixedMainDTO;
import com.maoding.financial.dto.QueryExpCategoryDTO;
import com.maoding.financial.entity.ExpFixedEntity;
import com.maoding.financial.service.ExpCategoryService;
import com.maoding.financial.service.ExpFixedService;
import com.maoding.org.dao.CompanyUserDao;
import com.maoding.org.entity.CompanyUserEntity;
import com.maoding.org.service.CompanyService;
import com.maoding.system.dao.DataDictionaryDao;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpFixedServiceImpl
 * 描    述 : 固定支出ServiceImpl
 * 作    者 : LY
 * 日    期 : 2016/8/4-11:11
 */
@Service("expFixedService")
public class ExpFixedServiceImpl extends GenericService<ExpFixedEntity> implements ExpFixedService {

    @Autowired
    private ExpFixedDao expFixedDao;

    @Autowired
    private CompanyBillService companyBillService;

    @Autowired
    private CompanyUserDao companyUserDao;

    @Autowired
    private ExpCategoryService expCategoryService;

    @Autowired
    private CompanyBalanceService companyBalanceService;

    @Autowired
    private CompanyService companyService;

    private String otherIncome = "其他业务收入";

    /**
     * 方法描述：查询选择月的固定支出
     * 作   者：LY
     * 日   期：2016/8/4 15:25
     * @param  expDate 月份
     */
    @Override
    public ExpFixedMainDTO getExpFixedByExpDate(String companyId, String expDate) throws Exception{
        String rootCompanyId = companyService.getRootCompanyId(companyId);
        ExpFixedMainDTO dto = expFixedDao.getExpFixed(companyId,rootCompanyId,expDate);
        if(dto==null){//理论上是不会为null的，因为不管数据库中是否有数据，都返回固定格式的数据
            //如果为空，则获取默认的数据
            dto = getExpFixedDefault(companyId,expDate);
        }
        BigDecimal expMount = new BigDecimal("0");
        BigDecimal incomeMount = new BigDecimal("0");
        if(!StringUtil.isNullOrEmpty(dto.getUserId())){
            for(ExpFixedDTO fixed:dto.getFixedList()){
                for(ExpFixedDTO fixedDTO:fixed.getDetailList()){
                    if(fixedDTO.getExpAmount()!=null ){
                        if(otherIncome.equals(fixedDTO.getExpTypeParentName())){
                            incomeMount = incomeMount.add(fixedDTO.getExpAmount());
                        }else {
                            expMount = expMount.add(fixedDTO.getExpAmount());
                        }
                    }
                }
            }
        }
        dto.setExpAmount(expMount);
        dto.setIncomeAmount(incomeMount);
        return dto;
    }

    private ExpFixedMainDTO getExpFixedDefault(String companyId, String expDate) throws Exception{
        String rootCompanyId = companyService.getRootCompanyId(companyId);
        this.expCategoryService.setDefaultExp(rootCompanyId);
        QueryExpCategoryDTO query = new QueryExpCategoryDTO();
        query.setCompanyId(companyId);
        query.setRootCompanyId(rootCompanyId);
        expCategoryService.insertCategoryFromRootCompany(query);
        return this.expFixedDao.getExpFixedDefault(companyId,rootCompanyId,expDate);
    }

    @Override
    public AjaxMessage saveExpFixedByExpDate(ExpFixedMainDTO dto) throws Exception{
        CompanyUserEntity user = companyUserDao.getCompanyUserByUserIdAndCompanyId(dto.getUserId(),dto.getCurrentCompanyId());
        if(user == null){
            return AjaxMessage.failed("参数错误");
        }
        //判断余额是否允许该次出账
        validateLowBalance(dto);
        //处理保存
        for(ExpFixedDTO fixed:dto.getFixedList()){
            ExpFixedEntity entity = new ExpFixedEntity();
            BaseDTO.copyFields(fixed,entity);
            entity.setUserId(user.getId());
            entity.setExpDate(dto.getExpDate());
            if(StringUtil.isNullOrEmpty(fixed.getId())){
                entity.setCompanyId(dto.getCompanyId());
                entity.initEntity();
                entity.setCreateBy(dto.getUserId());
                expFixedDao.insert(entity);
            }else {
                //更新
                entity.setUpdateBy(dto.getUserId());
                expFixedDao.updateById(entity);
            }
        }


        //财务记账
        financialAccount(dto,user.getId());
        return AjaxMessage.succeed(null);
    }

    private void validateLowBalance(ExpFixedMainDTO dto) throws Exception{
        //判断最低余额
        ExpFixedAmountDTO amountDTO = getExpFixedByMonth2(dto.getCompanyId(),dto.getExpDate());
        String amount ="0";
        if(amountDTO!=null && !StringUtil.isNullOrEmpty(amountDTO.getExpAmount())){
             amount = amountDTO.getExpAmount();
        }
        double currentAmount = 0;
        for(ExpFixedDTO fixed:dto.getFixedList()){
            if(!"其他业务收入".equals(fixed.getExpTypeParentName()) && fixed.getExpAmount()!=null){
                currentAmount = currentAmount + fixed.getExpAmount().doubleValue();
            }
        }
        double amount2 = currentAmount - Double.parseDouble(amount);
        if(amount2>0 && !companyBalanceService.isCanBeAllocate(dto.getCompanyId(),amount2+"",dto.getExpDate()+"-01")){
            //抛异常
            throw new CustomException("当前支出的金额不能大于账目余额与最低余额的差值");
        }
    }

    private void financialAccount(ExpFixedMainDTO dto,String companyUserId) throws Exception{
        SaveCompanyBillDTO billDTO = new SaveCompanyBillDTO();
        billDTO.setCompanyId(dto.getCompanyId());
        billDTO.setFromCompanyId(dto.getCompanyId());
        billDTO.setFeeType(CompanyBillType.FEE_TYPE_EXP_FIX);
        billDTO.setPayType(CompanyBillType.DIRECTION_PAYER);
        billDTO.setOperatorId(companyUserId);
        billDTO.setPaymentDate(dto.getExpDate());
        companyBillService.saveCompanyBillForFix(billDTO);
    }

    @Override
    public List<ExpFixedAmountDTO> getExpFixedByMonth(String companyId, String year) {
        List<String> monthList = DateUtils.getNMonthByYear(year);
        String[] months = new String[monthList.size()];
        List<ExpFixedAmountDTO> dataList = expFixedDao.getExpFixedByMonth(companyId, monthList.toArray(months));
        List<ExpFixedAmountDTO> returnData = new ArrayList<>();
        for(String month:months){
            boolean isContain = false;
            for(ExpFixedAmountDTO data:dataList){
                if(month.equals(data.getExpDate())){
                    returnData.add(data);
                    isContain = true;
                    break;
                }
            }
            if(!isContain){
                returnData.add(new ExpFixedAmountDTO(month));
            }
        }
        return returnData;
    }

    @Override
    public ExpFixedAmountDTO getExpFixedByMonth2(String companyId, String month) {
        String[] months = new String[1];
        months[0] = month;
        List<ExpFixedAmountDTO> dataList = expFixedDao.getExpFixedByMonth(companyId,months);
        if(CollectionUtils.isEmpty(dataList)){
            return new ExpFixedAmountDTO(month);
        }
        return dataList.get(0);
    }

}
