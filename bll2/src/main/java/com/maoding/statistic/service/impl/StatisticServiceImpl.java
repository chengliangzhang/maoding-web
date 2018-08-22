package com.maoding.statistic.service.impl;

import com.maoding.companybill.entity.CompanyBalanceEntity;
import com.maoding.companybill.service.CompanyBalanceService;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.ObjectUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.StringUtils;
import com.maoding.exception.CustomException;
import com.maoding.financial.dto.ExpCategoryDataDTO;
import com.maoding.financial.dto.QueryExpCategoryDTO;
import com.maoding.financial.service.ExpCategoryService;
import com.maoding.org.dao.CompanyDao;
import com.maoding.org.service.CompanyService;
import com.maoding.projectcost.dao.ProjectCostDao;
import com.maoding.statistic.dao.StatisticDao;
import com.maoding.statistic.dto.*;
import com.maoding.statistic.service.StatisticService;
import com.maoding.system.dao.DataDictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service("statisticService")
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    protected StatisticDao statisticDao;

    @Autowired
    protected ProjectCostDao projectCostDao;

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    @Autowired
    private ExpCategoryService expCategoryService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private CompanyBalanceService companyBalanceService;

    private static final String GDFY_SALESTAX = "gdfy_salestax";//主营业务税金及附加
    private static final String GDFY_CWFY = "gdfy_cwfy";//财务费用
    private static final String GDFY_SDSFY = "gdfy_sdsfy";//税务费用
    private static final String GDFY_EXECUTIVESALARY = "gdfy_executivesalary";//管理人员工资
    private static final String GDFY_FWSALARY = "gdfy_fwsalary";//房屋物业费用
    private static final String GDFY_ASSETSAMORTIZATION = "gdfy_assetsamortization";//分摊费用
    private static final String GDFY_ZCJZZB = "gdfy_zcjzzb";//资产减值准备
    private static final String GDFY_DIRECTCOSTS = "gdfy_directcosts";//直接人工成本
    private static final String BX_YWFY = "bx_ywfy";//直接项目成本
    private static final String BX_JYFY = "bx_jyfy";//经营费用
    private static final String BX_XZFY = "bx_xzfy";//行政费用
    private static final String BX_QTFY = "bx_qtfy";//其他费用


    /**
     * 方法描述：公司人数统计
     * 作者：MaoSF
     * 日期：2016/10/17
     *
     * @param:
     * @return:
     */
    public List<CompanyStatisticDTO> getCompanyStatisticList(Map<String, Object> param) {
        if (null != param.get("pageNumber")) {
            int page = (Integer) param.get("pageNumber");
            int pageSize = (Integer) param.get("pageSize");
            param.put("startPage", page * pageSize);
            param.put("endPage", pageSize);
        }
        return statisticDao.getCompanyStatisticList(param);
    }

    /**
     * 方法描述：公司人数统计 总条数
     * 作者：MaoSF
     * 日期：2016/10/17
     *
     * @param param
     * @param:
     * @return:
     */
    @Override
    public int getCompanyStatisticCount(Map<String, Object> param) {
        return statisticDao.getCompanyStatisticCount(param);
    }

    //=========================================新接口2.0=============================================================


    /**
     * 方法描述：公司人数统计
     * 作者：MaoSF
     * 日期：2016/10/17
     *
     * @param:
     * @return:
     */
    public List<CompanyStatisticDTO> selectCompanyStatisticList(Map<String, Object> param) {
        if (null != param.get("pageIndex")) {
            int page = (Integer) param.get("pageIndex");
            int pageSize = (Integer) param.get("pageSize");
            param.put("startPage", page * pageSize);
            param.put("endPage", pageSize);
        }
        return statisticDao.getCompanyStatisticList(param);
    }

    /**
     * 方法描述：公司人数统计 总条数
     * 作者：MaoSF
     * 日期：2016/10/17
     *
     * @param param
     * @param:
     * @return:
     */
    @Override
    public int selectCompanyStatisticCount(Map<String, Object> param) {
        return statisticDao.getCompanyStatisticCount(param);
    }


    /**
     * 统计项目费用
     */
    @Override
    public CompanyCostStatisticDTO getStatProjectCost(ProjectCostQueryDTO param) {
        CompanyCostStatisticDTO ccs;
        //查列表
        AtomicInteger count = new AtomicInteger();
        List<ProjectCostStatisticDTO> pcsList = statisticDao.selectStatisticByQuery(param, count);
        if (pcsList == null || pcsList.size() == 0) {
            ccs = new CompanyCostStatisticDTO();
        } else {
            //查总合计
            ccs = statisticDao.selectStatisticSummaryByQuery(param);
        }
        ccs.setDetailList(pcsList);
        ccs.setTotal(count.get());
        ccs.setCompanyId(param.getCompanyId());
        ccs.setType(param.getType());
        return ccs;
    }

    /**
     * 统计总览（列表）
     */
    @Override
    public StatisticDetailSummaryDTO getStatisticDetailSummary(StatisticDetailQueryDTO param) {
        StatisticDetailSummaryDTO  data = statisticDao.getCompanyStandingBookSum(param);
        if(data==null){
            data = new StatisticDetailSummaryDTO();
            data.setPay(new BigDecimal("0"));
            data.setGain(new BigDecimal("0"));
        }
        //合计金额（该段时间内的总盈利）
        data.setAmount(data.getGain().subtract(data.getPay()));
        //获取余额
        data.setSumBalance(this.getBalanceSum(param));
        return data;
    }


    @Override
    public StatisticDetailSummaryDTO getStatisticDetailSummaryByCompanyId(String companyId) {
        //查找该公司的最低余额设置
        CompanyBalanceEntity balance = companyBalanceService.getCompanyBalanceByCompanyId(companyId);
        //查询收益
        StatisticDetailQueryDTO param = new StatisticDetailQueryDTO();
        StatisticCompanyDTO companyDTO = new StatisticCompanyDTO();
        companyDTO.setCompanyId(companyId);
        if(balance!=null && balance.getSetBalanceDate()!=null){
            companyDTO.setSetBalanceDate(balance.getSetBalanceDate());
        }
        param.getStatisticCompanyList().add(companyDTO);
        StatisticDetailSummaryDTO  data = statisticDao.getBalanceSum(param);
        if(data==null){
            data = new StatisticDetailSummaryDTO();
            data.setPay(new BigDecimal("0"));
            data.setGain(new BigDecimal("0"));
        }
        data.setAmount(data.getGain().subtract(data.getPay()));
        if(balance!=null){
            if(!StringUtil.isNullOrEmpty(balance.getInitialBalance())){
                data.setAmount(data.getAmount().add(new BigDecimal(balance.getInitialBalance())));
            }
            data.setBalance(balance);
        }
        return data;
    }

    @Override
    public BigDecimal getBalanceSum(StatisticDetailQueryDTO param) {
        BigDecimal balanceSum = new BigDecimal("0");
        if(!CollectionUtils.isEmpty(param.getStatisticCompanyList())){
            //获取余额
            for(StatisticCompanyDTO dto:param.getStatisticCompanyList()){
                if (dto.getInitBalanceData()!=null){
                    balanceSum = balanceSum.add(new BigDecimal(dto.getInitBalanceData()));
                }
            }
            StatisticDetailSummaryDTO  balance = statisticDao.getBalanceSum(param);
            if(balance!=null){
                if(balance.getGain()!=null){
                    balanceSum = balanceSum.add(balance.getGain());
                }
                if(balance.getPay()!=null){
                    balanceSum = balanceSum.subtract(balance.getPay());
                }
            }
        }
        return balanceSum;
    }

    private void setExpensesDetailLedgerParam(StatisticDetailQueryDTO query) throws Exception{
        List<String> companyIds = this.getCompanyList(query.getCurrentCompanyId(),query.getCombineCompanyId());
        query.setCombineCompanyId(StringUtils.join(companyIds,","));
        for(String companyId:companyIds){
            query.getStatisticCompanyList().add(this.getStatisticDetailCompanyIdParam(query,companyId));
        }
    }

    private StatisticCompanyDTO getStatisticDetailCompanyIdParam(StatisticDetailQueryDTO param,String companyId) {
        StatisticCompanyDTO query = new StatisticCompanyDTO();
        query.setCompanyId(companyId);
        query.setEndDate(param.getEndDate());
        query.setStartDate(param.getStartDate());//先设置界面上传递过来的值，
        //todo 下面是根据余额初始值设置的时间重新设置查询的开始日期
        //查找该公司的最低余额设置
        CompanyBalanceEntity balance = companyBalanceService.getCompanyBalanceByCompanyId(companyId);
        if(balance!=null && balance.getSetBalanceDate()!=null){
            //如果前端传递了结束时间，并且结束时间小于设置的时间，则不处理,如果前端传递的开始时间>设置的时间，则不处理，其他都处理
            if(!(param.getEndDate()!=null && DateUtils.datecompareDate(param.getEndDate(),balance.getSetBalanceDate())<0
                    || param.getStartDate()!=null && DateUtils.datecompareDate(param.getStartDate(),balance.getSetBalanceDate())>0 )){
                query.setStartDate(balance.getSetBalanceDate());
            }
            if(param.getEndDate()==null || !StringUtil.isNullOrEmpty(param.getEndDate()) && DateUtils.datecompareDate(param.getEndDate(),balance.getSetBalanceDate())>0){
                query.setInitBalanceData(balance.getInitialBalance());
            }
            query.setSetBalanceDate(balance.getSetBalanceDate());
        }

        if(param.getStartDate()==null){
            if(param.getStartDateStr()==null || query.getStartDate()!=null && DateUtils.datecompareDate(param.getStartDateStr(),DateUtils.date2Str(query.getStartDate(),DateUtils.date_sdf))>0){
                param.setStartDateStr(DateUtils.date2Str(query.getStartDate(),DateUtils.date_sdf));
            }
        }
        return query;
    }

    /**
     * 收支总览-台账
     */
    @Override
    public List<StatisticDetailDTO> getExpensesDetailLedger(StatisticDetailQueryDTO param) throws Exception{
        //重新组装参数
        setExpensesDetailLedgerParam(param);
        param.getFeeType();
        return statisticDao.getExpensesDetailLedger(param);
    }

    @Override
    public List<StatisticDetailDTO> getExpensesDetailLedgerSum(StatisticDetailQueryDTO param) {
        return statisticDao.getExpensesDetailLedgerSum(param);
    }

    @Override
    public StatisticDetailSummaryDTO getCompanyStandingBookSum(StatisticDetailQueryDTO param) {
        return statisticDao.getCompanyStandingBookSum(param);
    }

    @Override
    public Integer getExpensesDetailLedgerCount(StatisticDetailQueryDTO param) {
        return statisticDao.getExpensesDetailLedgerCount(param);
    }

    /**
     * 收支总览-应收
     */
    @Override
    public List<StatisticDetailDTO> getReceivable(StatisticDetailQueryDTO dto) throws Exception{
        //重新设置参数
        dto.setCompanyIdList(this.getCompanyList(dto.getCurrentCompanyId(),dto.getReceivableId()));
        dto.setPayType(1);
        return statisticDao.getReceivable(dto);
    }

    @Override
    public Integer getReceivableCount(StatisticDetailQueryDTO param) {
        return statisticDao.getReceivableCount(param);
    }

    @Override
    public BigDecimal getReceivableSum(StatisticDetailQueryDTO param) {
        return statisticDao.getReceivableSum(param);
    }

    /**
     * 收支总览-应付
     */
    @Override
    public List<StatisticDetailDTO> getPayment(StatisticDetailQueryDTO dto) throws Exception{
        //重新设置参数
        dto.setCompanyIdList(this.getCompanyList(dto.getCurrentCompanyId(),dto.getPaymentId()));
        dto.setPayType(2);
        return statisticDao.getReceivable(dto);//和已收使用同一接口
    }

    @Override
    public Integer getPaymentCount(StatisticDetailQueryDTO param) {
        return statisticDao.getReceivableCount(param);
    }

    @Override
    public BigDecimal getPaymentSum(StatisticDetailQueryDTO param) {
        return statisticDao.getReceivableSum(param);
    }

    /**
     * 应收明细
     */
    @Override
    public ReceivableDetailDTO getReceivableDetail(String projectId) {
        return statisticDao.getReceivableDetail(projectId);
    }

    /**
     * 到账列表
     */
    @Override
    public List<PaymentDetailDTO> getReceivableDetailList(String projectId) {
        return statisticDao.getReceivableDetailList(projectId);
    }

    @Override
    public BigDecimal getAccountFee(String costPointId) {
        return statisticDao.getAccountFee(costPointId);
    }

    @Override
    public ReceivableDetailDTO getPaymentDetail(String projectId) {
        return statisticDao.getPaymentDetail(projectId);
    }

    @Override
    public List<PaymentDetailDTO> getPaymentDetailList(String projectId) {
        return statisticDao.getPaymentDetailList(projectId);
    }

    @Override
    public BigDecimal getPaymentAccountFee(String costPointId) {
        return statisticDao.getPaymentAccountFee(costPointId);
    }


    @Override
    public List<StatisticDetailDTO> getGeneralIncome(StatisticDetailQueryDTO dto) {
        return statisticDao.getGeneralIncome(dto);
    }

    @Override
    public List<StatisticDetailDTO> getTotalExpenditure(StatisticDetailQueryDTO dto) {
        return statisticDao.getTotalExpenditure(dto);
    }

    @Override
    public ExpensesStatisticsDTO setGeneralIncome(StatisticDetailQueryDTO queryDTO) {
        ExpensesStatisticsDTO expensesStatisticsDTO = this.statisticDao.getCompanyBillClassStatistics(queryDTO);
        if(expensesStatisticsDTO==null){
            expensesStatisticsDTO = new ExpensesStatisticsDTO();
        }
        //第一个饼图
        // foundGeneralIncome(queryDTO, expensesStatisticsDTO);
        //第二个饼图
        //foundExpensesStatostics(queryDTO, expensesStatisticsDTO);
        return expensesStatisticsDTO;
    }


    /**
     * 主营业务成本，直接项目成本中（合作设计分、技术审查费、其他支出）
     */
    @Override
    public List<StatisticDetailDTO> getProjectExpenditure(StatisticDetailQueryDTO dto) {
        return this.statisticDao.getProjectExpenditure(dto);
    }

    /**
     * 报销汇总
     */
    @Override
    public List<StatisticDetailDTO> getPaymentCollect(StatisticDetailQueryDTO dto) {
        return this.statisticDao.getPaymentCollect(dto);
    }

    @Override
    public List<StatisticDetailDTO> getCurveIncomeData(StatisticDetailQueryDTO dto) {
        return statisticDao.getCurveIncomeData(dto);
    }

    @Override
    public List<StatisticDetailDTO> getCurveExpenditureData1(StatisticDetailQueryDTO dto) {
        return statisticDao.getCurveExpenditureData1(dto);
    }

    @Override
    public List<StatisticDetailDTO> getCurveExpenditureData2(StatisticDetailQueryDTO dto) {
        return statisticDao.getCurveExpenditureData2(dto);
    }

    @Override
    public List<StatisticDetailDTO> getCurveExpenditureData3(StatisticDetailQueryDTO dto) {
        return statisticDao.getCurveExpenditureData3(dto);
    }

    /**
     * 拼装总支出
     */
    @Override
    public Map<String, BigDecimal> getCurveExpenditureData(StatisticDetailQueryDTO queryDTO) {
        List<StatisticDetailDTO> statisticDetailDTOS = new ArrayList<StatisticDetailDTO>();
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        List<StatisticDetailDTO> statisticDetailDTO = new ArrayList<StatisticDetailDTO>();
        StatisticDetailDTO detailDTO = new StatisticDetailDTO();
        List<StatisticDetailDTO> getCurveExpenditureData1 = this.getCurveExpenditureData1(queryDTO);
        List<StatisticDetailDTO> getCurveExpenditureData2 = this.getCurveExpenditureData2(queryDTO);
        List<StatisticDetailDTO> getCurveExpenditureData3 = this.getCurveExpenditureData3(queryDTO);
        getCurveExpenditureData1.addAll(getCurveExpenditureData2);
        getCurveExpenditureData1.addAll(getCurveExpenditureData3);
        statisticDetailDTO.addAll(getCurveExpenditureData1);
        for (int i = 0; i < getCurveExpenditureData1.size(); i++) {
            BigDecimal fee = new BigDecimal("0");
            StatisticDetailDTO detailDTO1 = getCurveExpenditureData1.get(i);
            for (int j = 0; j < statisticDetailDTO.size(); j++) {
                BigDecimal fee1 = new BigDecimal("0");
                StatisticDetailDTO detailDTO2 = statisticDetailDTO.get(j);
                if (null != detailDTO1.getProfitFee() && null != detailDTO2.getProfitFee()
                        && null != detailDTO1.getCreateDate() && null != detailDTO2.getCreateDate()
                        && detailDTO1.getCreateDate().equals(detailDTO2.getCreateDate())) {
                    fee1 = fee1.add(detailDTO2.getProfitFee());
                    detailDTO2.setProfitFee(fee);
                    if (null == map.get(detailDTO2.getCreateDate())) {
                        map.put(detailDTO2.getCreateDate(), fee1);
                    } else if (null != map.get(detailDTO2.getCreateDate())) {
                        fee1 = fee1.add(map.get(detailDTO2.getCreateDate()));
                        map.put(detailDTO2.getCreateDate(), fee1);
                    }
                    getCurveExpenditureData1.remove(detailDTO2);
                }
            }
        }
        return map;
    }

    @Override
    public Object[] getCompanyBillForLineData(StatisticDetailQueryDTO dto) {
        Object[] data = new Object[4];
        List<ClassifiedStatisticDataDTO> list = statisticDao.getCompanyBillForLineData(dto);
        if(!CollectionUtils.isEmpty(list)){
            List dateList = null;
            if(dto.getStartDate()==null && dto.getEndDate()==null){
                dateList = DateUtils.getMonthBetween(DateUtils.str2Date(list.get(0).getTimeData()+"-01",DateUtils.date_sdf), DateUtils.str2Date(list.get(list.size()-1).getTimeData()+"-01",DateUtils.date_sdf));
            }
            if(dto.getStartDate()!=null && dto.getEndDate()!=null){
                dateList = DateUtils.getMonthBetween(dto.getStartDate(), dto.getEndDate());
            }
            if(dto.getStartDate()!=null && dto.getEndDate()==null){
                dateList = DateUtils.getMonthBetween(dto.getStartDate(), DateUtils.str2Date(list.get(list.size()-1).getTimeData()+"-01",DateUtils.date_sdf));
            }
            if(dto.getStartDate()==null && dto.getEndDate()!=null){
                dateList = DateUtils.getMonthBetween(DateUtils.str2Date(list.get(0).getTimeData()+"-01",DateUtils.date_sdf),dto.getEndDate());
            }
            String[] timeList = new String[dateList.size()];
            data[0] = dateList.toArray(timeList);
            data[1] =  new BigDecimal[dateList.size()];
            data[2] =  new BigDecimal[dateList.size()];
            data[3] =  new BigDecimal[dateList.size()];
            BigDecimal[] countRevenue =  new BigDecimal[dateList.size()];
            BigDecimal[] countExpenditure =  new BigDecimal[dateList.size()];
            for(int i = 0;i<timeList.length;i++){
                countRevenue[i] = new BigDecimal("0");
                countExpenditure[i] = new BigDecimal("0");
                for(ClassifiedStatisticDataDTO dataDTO:list){
                    if(timeList[i].equals(dataDTO.getTimeData())){
                        countRevenue[i] =dataDTO.getCountRevenue();
                        countExpenditure[i] = dataDTO.getCountExpenditure();
                    }
                }
            }
            data[1] = countRevenue;
            data[2] = countExpenditure;
        }
        return data;
    }

    @Override
    public List<StatisticDetailDTO> getContractSumMoney(StatisticDetailQueryDTO dto) {
        return statisticDao.getContractSumMoney(dto);
    }


    /**
     * 收支总览（总计）
     */
    @Override
    public StatisticSummaryDTO getStatisticSummaryDTO(StatisticSummaryQueryDTO dto) {
        return statisticDao.getStatisticSummaryDTO(dto);
    }

    /**
     * 方法描述：人员任务统计
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    @Override
    public List<UserTaskStaticDTO> listUserTaskStatistic(Map<String, Object> param) throws Exception {
        if (null != param.get("pageIndex")) {
            int page = (Integer) param.get("pageIndex");
            int pageSize = (Integer) param.get("pageSize");
            param.put("startPage", page * pageSize);
            param.put("endPage", pageSize);
        }
        return statisticDao.listUserTaskStatistic(param);
    }

    /**
     * 方法描述：人员任务统计
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    @Override
    public AjaxMessage listUserTaskStatisticPage(Map<String, Object> param) throws Exception {
        List<UserTaskStaticDTO> list = this.listUserTaskStatistic(param);
        int total = this.statisticDao.getUserTaskStatisticCount(param);
        param.clear();
        param.put("data", list);
        param.put("total", total);
        return AjaxMessage.succeed(null).setData(param);
    }

    @Override
    public Map<String,List<Map<String, String>>> getRelationCompany(StatisticDetailQueryDTO dto) {
        Map<String,List<Map<String, String>>> result = new HashMap<>();
        result.put("toCompany",new ArrayList<>());
        result.put("fromCompany",new ArrayList<>());
        //添加收款组织
        dto.setPayType(1);
        List<Map<String, String>> toCompany =  statisticDao.getRelationCompany(dto);
        result.get("toCompany").addAll(toCompany);
        //添加付款组织
        dto.setPayType(2);
        List<Map<String, String>> fromCompany =  statisticDao.getRelationCompany(dto);
        result.get("fromCompany").addAll(fromCompany);
        //添加关联组织
//        dto.setPayType(3);
//        List<Map<String, String>> relationCompany =  statisticDao.getRelationCompany(dto);
//        result.get("relationCompany").addAll(relationCompany);
        return result;
    }

    @Override
    public List<StatisticDetailSummaryDTO> getCompanyColumnarData(StatisticDetailQueryDTO dto) {
        return statisticDao.getCompanyColumnarData(dto);
    }

    @Override
    public List<StatisticClassicSummaryDTO> getColumnarDataForOrgGroup(StatisticDetailQueryDTO dto) {
        return statisticDao.getColumnarDataForOrgGroup(dto);
    }

    @Override
    public List<StatisticClassicSummaryDTO> getColumnarDataForTimeGroup(StatisticDetailQueryDTO dto) {
        return statisticDao.getColumnarDataForTimeGroup(dto);
    }


    private List<String> getCompanyList(String currentCompanyId,String companyFlag) throws Exception{
        if( companyFlag.length()!=32){//以下情况都是合并报表
            if("root".equals(companyFlag) || companyFlag.indexOf("subCompanyId")>-1 || companyFlag.indexOf("partnerId")>-1){//查询所有
                List<String> companyIds = companyService.getExpensesDetailLedgerCompanyList(currentCompanyId,companyFlag);
                return companyIds;
            }
        }else {
            List<String> companyList = new ArrayList<>();
            companyList.add(companyFlag);
            return companyList;
        }
        return null;
    }


    @Override
    public Map<String, Object> getStatisticClassicData(StatisticDetailQueryDTO query) throws Exception{
        /** todo 设置参数 **/
        //获取所有的组织
        if(StringUtil.isNullOrEmpty(query.getCombineCompanyId())){
            query.setCombineCompanyId("root");
        }
        boolean isGetDataByOrgGroup = false;//按组织分组，只要在多组织的情况下才按组织分组
        if( query.getCombineCompanyId().length()!=32){//以下情况都是合并报表
            query.setCompanyIdList(this.getCompanyList(query.getCurrentCompanyId(),query.getCombineCompanyId()));
            query.setCombineCompanyId(null);
            if(query.getCompanyIdList().size()>1){
                isGetDataByOrgGroup = true;
            }
        }
        //如果前端没有传递费用类型过来，则查询按所有一级类型分组查询
        if(CollectionUtils.isEmpty(query.getFeeTypeList())){
            //如果当前值查询一个组织，并且存在分摊，则类型中包含分摊
            if(query.getCombineCompanyId()!=null && query.getCombineCompanyId().length()==32 && isExistShareType(query.getCombineCompanyId())) {
                query.setColFeeTypeList(SystemParameters.CATEGORY_ROOT);
            }else {//不包含分摊
                query.setColFeeTypeList(SystemParameters.CATEGORY_ROOT_NOT_SHARE);
            }
        }else {
            for(String type:query.getFeeTypeList()){
                if(SystemParameters.CATEGORY_ROOT.contains(type)){//表示一级类型
                    query.getColFeeTypeList().add(type);
                } else  if(!query.getFeeTypeList().contains(SystemParameters.CATEGORY_RELATION.get(type))){//表示的是二级类型，并且不包含在列表内，则添加到getColFeeTypeList中
                    query.getColFeeTypeList().add(type);
                }
            }
        }
        if(StringUtil.isNullOrEmpty(query.getStartDateStr()) && StringUtil.isNullOrEmpty(query.getEndDateStr())){
            query.setStartDateStr(DateUtils.getNewYearsDay(null));
            query.setEndDateStr(DateUtils.date2Str(DateUtils.date_sdf));
            query.setGroupByTime("1");
            query.setColMonthList(DateUtils.getNMonthByYear(null));
        }else {
            if(StringUtil.isNullOrEmpty(query.getStartDateStr())|| StringUtil.isNullOrEmpty(query.getEndDateStr())){
                throw new CustomException("请选择开始时间及结束时间");
            }
            if("1".equals(query.getGroupByTime())){//如果是按月
                query.setColMonthList(DateUtils.getMonthBetween(query.getStartDateStr(),query.getEndDateStr()));
            }else {
                query.setColMonthList(DateUtils.getYearBetween(query.getStartDateStr(),query.getEndDateStr()));
            }
        }

        Map<String, Object> result = new HashMap<>();
        if(isGetDataByOrgGroup){
            result.put("columnarDataForOrgGroup",this.getColumnarDataForOrgGroup2(query));
        }
        result.put("columnarDataForTimeGroup",this.getColumnarDataForTimeGroup2(query));
        result.put("startDateStr",query.getStartDateStr());
        result.put("endDateStr",query.getEndDateStr());
        return result;
    }

    /**
     * 按组织分组
     */
    private ColumnarDTO getColumnarDataForOrgGroup2(StatisticDetailQueryDTO query){
        ColumnarDTO data = new ColumnarDTO();
        int size = query.getCompanyIdList().size();
        ColumnarDataDTO datasets[] = initColumnarDataDTO(size, query.getColFeeTypeList());
        List<String> companyList = new ArrayList<>();
        for(String companyId:query.getCompanyIdList()){
            companyList.add(companyDao.getCompanyName(companyId));
        }
        List<StatisticClassicSummaryDTO> list = this.getColumnarDataForOrgGroup(query);
        for(StatisticClassicSummaryDTO dto:list){
            if(dto.getAmount()!=null){
                String type = getColFeeTypeByExpParentType(query.getColFeeTypeList(),dto.getExpTypeParentName());
                if(type!=null){//理论上不会为null
                    int x = getTypeIndex(query.getColFeeTypeList(),type);
                    int y = getCompanyIndex(query.getCompanyIdList(),dto.getCompanyId());
                    if(x>-1 && y>-1){
                        double amount = datasets[x].getData()[y] +dto.getAmount().doubleValue();
                        datasets[x].getData()[y] = amount;
                    }
                }
            }
        }
        String labels[] = new String[size];
        data.setLabels(companyList.toArray(labels));
        data.setDatasets(datasets);
        return data;
    }

    /**
     * 按时间分组
     */
    private ColumnarDTO getColumnarDataForTimeGroup2(StatisticDetailQueryDTO query){
        List<String> timeList = query.getColMonthList();
        ColumnarDTO data = new ColumnarDTO();
        if(CollectionUtils.isEmpty(timeList)){
            return data;
        }
        int size = timeList.size();
        ColumnarDataDTO datasets[] = initColumnarDataDTO(size, query.getColFeeTypeList());
        List<StatisticClassicSummaryDTO> list = this.getColumnarDataForTimeGroup(query);
        for(StatisticClassicSummaryDTO dto:list){
            if(dto.getAmount()!=null){
                String type = getColFeeTypeByExpParentType(query.getColFeeTypeList(),dto.getExpTypeParentName());
                if(type!=null && dto.getPaymentDate()!=null){//理论上不会为null
                    int x = getTypeIndex(query.getColFeeTypeList(),type);
                    int y = getCompanyIndex(timeList,dto.getPaymentDate());
                    if(x>-1 && y>-1){
                        double amount = datasets[x].getData()[y] +dto.getAmount().doubleValue();
                        datasets[x].getData()[y] = amount;
                    }
                }
            }
        }
        String labels[] = new String[size];
        data.setLabels(timeList.toArray(labels));
        data.setDatasets(datasets);
        return data;
    }

    String getColFeeTypeByExpParentType(List<String>colTypeList,String parentType){
        if(parentType==null){
            return null;
        }
        if(colTypeList.contains(parentType)) {//表示一级类型
            return parentType;
        }
        //如果在colTypeList中没有包含，则需要找到他的存在的type组中
        String type = SystemParameters.CATEGORY_RELATION.get(parentType);
        if(type!=null && colTypeList.contains(type)){
            return type;
        }
        return null;
    }
    int getCompanyIndex(List<String> companyIds,String companyId ){
        for(int i = 0;i<companyIds.size();i++){
            if(companyId.equals(companyIds.get(i))){
                return i;
            }
        }
        return -1;
    }


    int getTypeIndex(List<String> typeList,String type ){
        for(int i = 0;i<typeList.size();i++){
            if(type.equals(typeList.get(i))){
                return i;
            }
        }
        return -1;
    }

    private ColumnarDataDTO[] initColumnarDataDTO(int colSize,List<String> typeList){
        ColumnarDataDTO datasets[] = new ColumnarDataDTO[typeList.size()];
        for(int i = 0;i<typeList.size();i++){
            ColumnarDataDTO data = new ColumnarDataDTO(colSize,this.getColor(i));
            data.setLabel(typeList.get(i));
            datasets[i] = data;
        }
        return datasets;
    }

    private String getColor(int i){
        if(i<SystemParameters.COLORS.size()){
            return SystemParameters.COLORS.get(i);
        }else {
            return getColor();
        }
    }
    private String getColor(){
        //红色
        String red;
        //绿色
        String green;
        //蓝色
        String blue;
        //生成随机对象
        Random random = new Random();
        //生成红色颜色代码
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成绿色颜色代码
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //生成蓝色颜色代码
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //判断红色代码的位数
        red = red.length()==1 ? "0" + red : red ;
        //判断绿色代码的位数
        green = green.length()==1 ? "0" + green : green ;
        //判断蓝色代码的位数
        blue = blue.length()==1 ? "0" + blue : blue ;
        //生成十六进制颜色值
        String color = "#"+red+green+blue;
        return color;
    }

    @Override
    public List<CostTypeDTO> getFeeTypeList(StatisticDetailQueryDTO dto) throws Exception{
        List<CostTypeDTO> list = new ArrayList<>();
        //初始化利润报表
        List<ProfitStatementTableDTO> dataList = new ArrayList<>();
        Map<String,ProfitStatementDataDTO> mapData = new HashMap<>();
        initProfitStatement2(dataList,mapData,Arrays.asList(dto.getCombineCompanyId()));
        /**统计合计**/
        //主营业务收入
        list.add(getParentType(dataList,mapData.get("业务收入"), SystemParameters.CATEGORY_CHILD.get("业务收入")));
      //  list.add(new CostTypeDTO("主营业务税金及附加","主营业务税金及附加",1));
        //主营业务成本
        list.add(getParentType(dataList,mapData.get("主营业务成本"), SystemParameters.CATEGORY_CHILD.get("主营业务成本")));
        //管理费用
        list.add(getParentType(dataList,mapData.get("管理费用"), SystemParameters.CATEGORY_CHILD.get("管理费用")));
        //财务费用
        list.add(getParentType(dataList,mapData.get("财务费用合计"), Arrays.asList("财务费用")));
        list.add(getParentType(dataList,mapData.get("税务费用合计"), Arrays.asList("税务费用")));
        return list;
    }

    private boolean isExistShareType(String companyId){
        return !CollectionUtils.isEmpty(this.expCategoryService.getExpCategoryListByType(companyId,3));
    }

    private boolean isExistShareType(List<String> companyIdList){
        return !CollectionUtils.isEmpty(this.expCategoryService.getExpCategoryListByType(companyIdList,3));
    }
    @Override
    public List<List<CostTypeDTO>> getCategoryTypeList(StatisticDetailQueryDTO dto) throws Exception {
        boolean isExistShareType = false;
        String companyId = dto.getCompanyId();
        if(StringUtil.isNullOrEmpty(companyId)){
            companyId = "root";
        }
        isExistShareType = isExistShareType(this.getCompanyList(dto.getCurrentCompanyId(),companyId));
        //此方法内的数据写死
        List<List<CostTypeDTO>> list = new ArrayList<>();
        List<CostTypeDTO> list1 = new ArrayList<>();
        list1.add( new CostTypeDTO("业务收入","业务收入",2,"1",null));
        list1.add( new CostTypeDTO("主营业务成本","主营业务成本",2,"2",null));
        list1.add( new CostTypeDTO("管理费用","管理费用",7,"3",null));
        if(isExistShareType){
            list1.add( new CostTypeDTO("费用分摊","费用分摊",3,"17",null));
        }
        list1.add( new CostTypeDTO("财务费用","财务费用",1,"4",null));
        list1.add( new CostTypeDTO("税务费用","税务费用",1,"5",null));
        List<CostTypeDTO> list2 = new ArrayList<>();
        list2.add( new CostTypeDTO("主营业务收入","主营业务收入",1,"6","1"));
        list2.add( new CostTypeDTO("其他业务收入","其他业务收入",1,"7","1"));

        list2.add( new CostTypeDTO("直接项目成本","直接项目成本",1,"8","2"));
        list2.add( new CostTypeDTO("直接人工成本","直接人工成本",1,"9","2"));

        list2.add( new CostTypeDTO("管理人员工资","管理人员工资",1,"10","3"));
        list2.add( new CostTypeDTO("房屋物业费用","房屋物业费用",1,"11","3"));
        list2.add( new CostTypeDTO("行政费用","行政费用",1,"12","3"));
        list2.add( new CostTypeDTO("经营费用","经营费用",1,"13","3"));
        list2.add( new CostTypeDTO("其他费用","其他费用",1,"14","3"));
        list2.add( new CostTypeDTO("软件、不动产购置","软件、不动产购置",1,"15","3"));
        list2.add( new CostTypeDTO("资产减值准备","资产减值准备",1,"16","3"));

        if(isExistShareType){
            list2.add( new CostTypeDTO("房屋物业费用分摊","房屋物业费用分摊",1,"19","17"));
            list2.add( new CostTypeDTO("资产购置费分摊","资产购置费分摊",1,"20","17"));
            list2.add( new CostTypeDTO("其他费用分摊","其他费用分摊",1,"21","17"));
        }

        list2.add( new CostTypeDTO("","",1,"17","4"));
        list2.add( new CostTypeDTO("","",1,"18","5"));
        list.add(list1);
        list.add(list2);
        return list;
    }

    public List<String> getExpTypeParentList(List<CompanyBillDetailDTO> list){
        List<String> typeList = new ArrayList<>();
        list.stream().forEach(b->{
            if(!typeList.contains(b.getExpTypeParentName())){
                typeList.add(b.getExpTypeParentName());
            }
        });
        return typeList;
    }

    @Override
    public List<ProfitStatementTableDTO> getProfitStatement(StatisticDetailQueryDTO dto) throws Exception{
        //组织参数
        String companyId = dto.getCombineCompanyId();
        if(StringUtil.isNullOrEmpty(companyId)){
            companyId = "root";
        }
        dto.setCompanyIdList(this.getCompanyList(dto.getCurrentCompanyId(),companyId));
        List<CompanyBillDetailDTO> list = statisticDao.getCompanyBillDetailByYear(dto);
        //初始化利润报表
        List<ProfitStatementTableDTO> dataList = new ArrayList<>();
        Map<String,ProfitStatementDataDTO> mapData = new HashMap<>();
        initProfitStatement2(dataList,mapData,dto.getCompanyIdList(),getExpTypeParentList(list));
        //
        for(CompanyBillDetailDTO data:list){
            setProfitStatementData(dataList,mapData,data);
        }
        /**统计合计**/
        //主营业务收入
        statisticTotal(dataList,mapData.get("业务收入"), SystemParameters.CATEGORY_CHILD.get("业务收入"),"code1",true);
        //主营业务成本
        statisticTotal(dataList,mapData.get("主营业务成本"), SystemParameters.CATEGORY_CHILD.get("主营业务成本"),"code2",true);
        //主营业务利润
        statisticTotal(dataList,mapData.get("主营业务利润"), Arrays.asList("主营业务收入"),null,false);
        statisticTotal2(dataList,mapData.get("主营业务利润"), Arrays.asList("主营业务成本"));
        //管理费用
        statisticTotal(dataList,mapData.get("管理费用"), SystemParameters.CATEGORY_CHILD.get("管理费用"),"code3",true);
        //财务费用
        statisticTotal(dataList,mapData.get("财务费用合计"), Arrays.asList("财务费用"),"code4",true);
        //财务费用
        statisticTotal(dataList,mapData.get("税务费用合计"), Arrays.asList("税务费用"),"code5",true);
        //利润总额
        statisticTotal(dataList,mapData.get("利润总额"), Arrays.asList("主营业务收入"),null,false);
        statisticTotal2(dataList,mapData.get("利润总额"), Arrays.asList("主营业务成本","管理费用","财务费用"));
        //净利润
        statisticTotal(dataList,mapData.get("净利润"), Arrays.asList("利润总额"),null,false);
        statisticTotal2(dataList,mapData.get("净利润"), Arrays.asList("税务费用"));

        //去掉多余的字段
//        getGroupStatementTable("主营业务收入",dataList).setKey("");
        ProfitStatementTableDTO sw = getGroupStatementTable("税务费用",dataList);
        if(sw!=null){
            sw.setKey("");
        }
        ProfitStatementTableDTO cw = getGroupStatementTable("财务费用",dataList);
        if(cw!=null){
            cw.setKey("");
        }
        ProfitStatementTableDTO cwhj = getGroupStatementTable("财务费用合计",dataList);
        if(cwhj!=null){
            cwhj.setKey("财务费用");
        }
        ProfitStatementTableDTO swhj = getGroupStatementTable("税务费用合计",dataList);
        if(swhj!=null){
            swhj.setKey("税务费用");
        }
        return dataList;
    }

    private void setProfitStatementData(List<ProfitStatementTableDTO> list,Map<String,ProfitStatementDataDTO> mapData,CompanyBillDetailDTO data){
        int month = DateUtils.getMonth(data.getPaymentDate());
        ProfitStatementDataDTO dto = null;
        String key = data.getExpTypeParentName()+"-"+data.getExpTypeName();
        if(data.getFeeType()==4){
            if(data.getPayType()==1){
                key = "主营业务收入-其他收入";
            }else {
                key = "直接项目成本-其他支出";
            }
        }
        if(mapData.containsKey(key)){
            dto = mapData.get(key);
        }else {
            //找到父级
            ProfitStatementTableDTO table = getGroupStatementTable(data.getExpTypeParentName(),list);
            dto = new ProfitStatementDataDTO(data.getExpTypeParentName(),data.getExpTypeName());
            mapData.put(key,dto);
            if(table==null){
                //统计到其他报销里面
                table = getGroupStatementTable("其他费用",list);
            }
            if(table!=null){
                table.getList().add(dto);
            }
        }
        if(dto==null){
            return;
        }
        switch (month){
            case 1:
                dto.setJanuaryData(dto.getJanuaryData().add(data.getPaymentFee()));
                break;
            case 2:
                dto.setFebruaryData(dto.getFebruaryData().add(data.getPaymentFee()));
                break;
            case 3:
                dto.setMarchData(dto.getMarchData().add(data.getPaymentFee()));
                break;
            case 4:
                dto.setAprilData(dto.getAprilData().add(data.getPaymentFee()));
                break;
            case 5:
                dto.setMayData(dto.getMayData().add(data.getPaymentFee()));
                break;
            case 6:
                dto.setJuneData(dto.getJuneData().add(data.getPaymentFee()));
                break;
            case 7:
                dto.setJulyData(dto.getJulyData().add(data.getPaymentFee()));
                break;
            case 8:
                dto.setAugustData(dto.getAugustData().add(data.getPaymentFee()));
                break;
            case 9:
                dto.setSeptemberData(dto.getSeptemberData().add(data.getPaymentFee()));
                break;
            case 10:
                dto.setOctoberData(dto.getOctoberData().add(data.getPaymentFee()));
                break;
            case 11:
                dto.setNovemberData(dto.getNovemberData().add(data.getPaymentFee()));
                break;
            case 12:
                dto.setDecemberData(dto.getDecemberData().add(data.getPaymentFee()));
                break;
        }
    }

    /**
     * 通过key值
     */
    private ProfitStatementTableDTO getGroupStatementTable(String key,List<ProfitStatementTableDTO> list){
        for(ProfitStatementTableDTO dto:list){
            if(key.equals(dto.getKey())){
                return dto;
            }
        }
        return null;
    }

    private void statisticTotal(List<ProfitStatementTableDTO> list,ProfitStatementDataDTO totalDTO,List<String> keys,String parentCode,boolean isSetGroupCode){
        for(String key:keys){
            ProfitStatementTableDTO dto = getGroupStatementTable(key,list);
            if(dto!=null){
                if(isSetGroupCode){
                    dto.setCode(parentCode);
                }
                for(ProfitStatementDataDTO data:dto.getList()){
                    totalDTO.addObject(data);
                }
            }
        }
    }

    private void statisticTotal2(List<ProfitStatementTableDTO> list,ProfitStatementDataDTO totalDTO,List<String> keys){
        for(String key:keys){
            ProfitStatementTableDTO dto = getGroupStatementTable(key,list);
            if(dto!=null){
                for(ProfitStatementDataDTO data:dto.getList()){
                    totalDTO.subtractObject(data);
                }
            }
        }
    }

    private CostTypeDTO getParentType(List<ProfitStatementTableDTO> list,ProfitStatementDataDTO totalDTO,List<String> keys){
        CostTypeDTO type = new CostTypeDTO(totalDTO.getExpTypeParentName(),totalDTO.getExpTypeParentName(),1);
        type.setExpTypeKey(type.getExpTypeKey().replaceAll("合计",""));
        type.setExpTypeValue(type.getExpTypeValue().replaceAll("合计",""));
        for(String key:keys){
            ProfitStatementTableDTO dto = getGroupStatementTable(key,list);
            if(dto!=null){
                CostTypeDTO child1 = new CostTypeDTO(dto.getKey(),dto.getKey(),2);
                if(key.equals("财务费用")){
                    child1 = new CostTypeDTO("","",2);
                }
                type.getChildList().add(child1);

                for(ProfitStatementDataDTO data:dto.getList()){
                    String keyCode = data.getExpTypeName();
                    if(key.equals("主营业务收入")){
                        keyCode = keyCode+"_1";
                    }else {
                        keyCode = keyCode+"_2";
                    }
                    CostTypeDTO child2 = new CostTypeDTO(keyCode,data.getExpTypeName(),3);
                    child1.getChildList().add(child2);
                }
            }
        }
        return type;
    }

    private void initProfitStatement2(List<ProfitStatementTableDTO> list ,Map<String,ProfitStatementDataDTO> mapData,List<String> companyIdList) throws Exception {
        initProfitStatement2(list,mapData,companyIdList,null);
    }

    private void initProfitStatement2(List<ProfitStatementTableDTO> list ,Map<String,ProfitStatementDataDTO> mapData,List<String> companyIdList,List<String> parentTypeList) throws Exception {
        //todo 1.查询所有的费用收支项
        QueryExpCategoryDTO query = new QueryExpCategoryDTO();
       // query.setCompanyId(companyId);
        query.setCompanyIdList(companyIdList);
        if(!CollectionUtils.isEmpty(parentTypeList)){
            query.setParentTypeList(parentTypeList);
        }
        List<ExpCategoryDataDTO> typeList = expCategoryService.getExpTypeListForProfitReport(query);
        //todo 2.把mapList重新整理成 ProfitStatementTableDTO
        ProfitStatementTableDTO table1 = new ProfitStatementTableDTO("业务收入","code1");
        table1.setFlag(1);
        table1.setArrowsFlag(1);
        ProfitStatementDataDTO data1 = new ProfitStatementDataDTO("业务收入","合计");
        mapData.put("业务收入",data1);
        table1.getList().add(data1);

        //第四个表格 主营业务成本合计
        ProfitStatementTableDTO table4 = new ProfitStatementTableDTO("主营业务成本","code2");
        table4.setFlag(1);
        table4.setArrowsFlag(1);
        ProfitStatementDataDTO data41 = new ProfitStatementDataDTO("主营业务成本","合计");
        table4.getList().add(data41);
        mapData.put("主营业务成本",data41);

        //第7个表格
        ProfitStatementTableDTO table7 = new ProfitStatementTableDTO("主营业务利润","");
        table7.setFlag(1);
        ProfitStatementDataDTO data71 = new ProfitStatementDataDTO("主营业务利润","合计");
        mapData.put("主营业务利润",data71);
        table7.getList().add(data71);

        //第8个表格
        ProfitStatementTableDTO table8 = new ProfitStatementTableDTO("管理费用","code3");
        table8.setFlag(1);
        table8.setArrowsFlag(1);
        ProfitStatementDataDTO data81 = new ProfitStatementDataDTO("管理费用","合计");
        mapData.put("管理费用",data81);
        table8.getList().add(data81);

        //财务费用
        ProfitStatementTableDTO table9 = new ProfitStatementTableDTO("财务费用合计","code4");
        table9.setFlag(1);
        table9.setArrowsFlag(1);
        ProfitStatementDataDTO data91 = new ProfitStatementDataDTO("财务费用合计","合计");
        mapData.put("财务费用合计",data91);
        table9.getList().add(data91);

        //税务费用
        ProfitStatementTableDTO table10 = new ProfitStatementTableDTO("税务费用合计","code5");
        table10.setFlag(1);
        table10.setArrowsFlag(1);
        ProfitStatementDataDTO data10 = new ProfitStatementDataDTO("税务费用合计","合计");
        mapData.put("税务费用合计",data10);
        table10.getList().add(data10);

        //利润总额
        ProfitStatementTableDTO table11 = new ProfitStatementTableDTO("利润总额","");
        table11.setFlag(1);
        ProfitStatementDataDTO data11 = new ProfitStatementDataDTO("利润总额","合计");
        mapData.put("利润总额",data11);
        table11.getList().add(data11);

        //净利润
        ProfitStatementTableDTO table12 = new ProfitStatementTableDTO("净利润","");
        table12.setFlag(1);
        ProfitStatementDataDTO data12 = new ProfitStatementDataDTO("净利润","合计");
        mapData.put("净利润",data12);
        table12.getList().add(data12);
        boolean isAddTable = false;
        /********************/
        for(ExpCategoryDataDTO parent:typeList){
            String parentName = parent.getName();
            if("主营业务收入".equals(parentName)){
                list.add(table1);
            }
            if("直接项目成本".equals(parentName)){
                list.add(table4);
            }
            if("管理人员工资".equals(parentName)){
                list.add(table7);
                list.add(table8);
            }
            if("财务费用".equals(parentName)){
                list.add(table9);
            }
            if("税务费用".equals(parentName)){
                list.add(table10);
            }

            ProfitStatementTableDTO dataTable = new ProfitStatementTableDTO(parentName,"");
            for(ExpCategoryDataDTO child:parent.getChildList()){
                String childName = child.getName();
                ProfitStatementDataDTO data = new ProfitStatementDataDTO("",childName);
                mapData.put(parentName+"-"+childName,data);
                dataTable.getList().add(data);
            }
            list.add(dataTable);
            if("财务费用".equals(parentName)){
                list.add(table11);
               // list.add(table10);
                isAddTable = true;
            }
        }
        //如果系统中不存在财务费用，则在最后添加table11，table10，否则会漏掉利润总额 此行数据
        if(!isAddTable){
            list.add(table11);
//            list.add(table10);
        }
        list.add(table12);
    }

    /**
     * 描述     获取收支明细标题栏过滤条件列表
     * 日期     2018/8/15
     * @author  张成亮
     * @return  标题栏过滤条件
     * @param   query 收支明细查询条件
     **/
    @Override
    public StatisticTitleFilterDTO getTitleFilter(StatisticDetailQueryDTO query) {
        //查找相关的收支分类和收支分类子项过滤条件
        //保存并清理用于过滤的值
        String feeType = query.getFeeType();
        query.setFeeType(null);
        List<String> feeTypeList = query.getFeeTypeList();
        query.setFeeTypeList(null);
        List<String> feeTypeParentList = query.getFeeTypeParentList();
        query.setFeeTypeParentList(null);

        List<CostTypeDTO> feeTypeAllList = statisticDao.listFeeTypeFilter(query);
        //转换相关的收支分类和收支分类子项过滤条件到相应过滤条件
        List<CostTypeDTO> feeTypeParentFilterList = new ArrayList<>();
        List<CostTypeDTO> feeTypeFilterList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(feeTypeAllList)){
            for (CostTypeDTO type : feeTypeAllList){
                //设置收支分类过滤项
                CostTypeDTO parent = new CostTypeDTO(null,type.getExpTypeValue(),1);
                if (inList(parent.getExpTypeValue(),feeTypeParentList)) {
                    parent.setSelected(1);
                } else {
                    parent.setSelected(0);
                }
                feeTypeParentFilterList.add(parent);

                //设置收支分类子项过滤项，如果feeTypeParentList不为空，需要联动
                if (ObjectUtils.isNotEmpty(feeTypeParentList)){
                    if (inList(type.getExpTypeValue(),feeTypeParentList)){
                        if (inList(type.getExpTypeValue(),feeTypeParentList)) {
                            type.setSelected(1);
                        }
                        feeTypeFilterList.add(type);
                        if (ObjectUtils.isNotEmpty(type.getChildList())){
                            type.getChildList().forEach(child->{
                                if (inList(child.getExpTypeValue(),feeTypeList)){
                                    child.setSelected(1);
                                }
                            });
                        }
                    }
                } else {
                    feeTypeFilterList.add(type);
                }
            }
        }

        //还原过滤值
        query.setFeeType(feeType);
        query.setFeeTypeList(feeTypeList);
        query.setColFeeTypeList(feeTypeParentList);

        //生成返回值
        StatisticTitleFilterDTO titleFilter = new StatisticTitleFilterDTO();
        titleFilter.setFeeTypeParentNameList(feeTypeParentFilterList);
        titleFilter.setFeeTypeNameList(feeTypeFilterList);
        titleFilter.setFeeList(getFeeList());
        return titleFilter;
    }

    //获取金额过滤条件
    private List<CoreShowDTO> getFeeList(){
        return new ArrayList<CoreShowDTO>(){
            {
                add(new CoreShowDTO("3","项目收支"));
                add(new CoreShowDTO("4","非项目收支"));
            }
        };
    }

    //判断字符串是否在列表里
    private boolean inList(String str,List<String> list){
        boolean found = false;
        if (ObjectUtils.isNotEmpty(list)) {
            for (String s : list) {
                if (StringUtils.isSame(str, s)) {
                    found = true;
                    break;
                }
            }
        }

        return found;
    }
}
