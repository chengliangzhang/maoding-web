package com.maoding.statistic.dao;

import com.maoding.statistic.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Idccapp22 on 2016/10/17.
 */
public interface StatisticDao {

    /**
     * 方法描述：公司人数统计
     * 作者：MaoSF
     * 日期：2016/10/17
     *
     * @param:
     * @return:
     */
    public List<CompanyStatisticDTO> getCompanyStatisticList(Map<String, Object> param);

    /**
     * 方法描述：公司人数统计 总条数
     * 作者：MaoSF
     * 日期：2016/10/17
     *
     * @param:
     * @return:
     */
    public int getCompanyStatisticCount(Map<String, Object> param);

    /**
     * 方法描述：获取费用统计数据
     * 作者：ZCL
     * 日期：2017/4/27
     */
    List<ProjectCostStatisticDTO> selectStatisticByQuery(ProjectCostQueryDTO dto);

    List<ProjectCostStatisticDTO> selectStatisticByQuery(ProjectCostQueryDTO dto, AtomicInteger total);

    CompanyCostStatisticDTO selectStatisticSummaryByQuery(ProjectCostQueryDTO dto);


    StatisticDetailSummaryDTO getStatisticDetailSummary(StatisticDetailQueryDTO dto, Boolean isIncludeAll);

    List<StatisticDetailDTO> getStatisticDetailList(StatisticDetailQueryDTO dto);

    /**
     * 获取总收入
     */
    StatisticSummaryDTO getStatisticSummaryDTO(StatisticSummaryQueryDTO dto);

    /**
     * 方法描述：人员任务统计
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    List<UserTaskStaticDTO> listUserTaskStatistic(Map<String, Object> param) throws Exception;

    /**
     * 方法描述：获取 人员任务统计 总条数
     * 作者：MaoSF
     * 日期：2017/6/7
     */
    int getUserTaskStatisticCount(Map<String, Object> param) throws Exception;

    /**
     * 台账
     */
    List<StatisticDetailDTO> getExpensesDetailLedger(StatisticDetailQueryDTO dto);
    /**
     * 计算总收入、总支出
     * */
    List<StatisticDetailDTO> getExpensesDetailLedgerSum(StatisticDetailQueryDTO param);
    StatisticDetailSummaryDTO getCompanyStandingBookSum(StatisticDetailQueryDTO param);

    /**
     * 查询余额接口
     */
    StatisticDetailSummaryDTO getBalanceSum(StatisticDetailQueryDTO param);

    Integer getExpensesDetailLedgerCount(StatisticDetailQueryDTO param);

    /**
     * 应收
     */
    List<StatisticDetailDTO> getReceivable(StatisticDetailQueryDTO dto);

    Integer getReceivableCount(StatisticDetailQueryDTO param);

    /**
     * 应收总额
     */
    BigDecimal getReceivableSum(StatisticDetailQueryDTO param);

    /**
     * 应付
     */
    List<StatisticDetailDTO> getPayment(StatisticDetailQueryDTO dto);

    Integer getPaymentCount(StatisticDetailQueryDTO param);

    /**
     * 应付总额
     */
    BigDecimal getPaymentSum(StatisticDetailQueryDTO param);

    /**
     * 应收明细
     */
    ReceivableDetailDTO getReceivableDetail(String projectId);

    /**
     * 到账列表
     */
    List<PaymentDetailDTO> getReceivableDetailList(String projectId);

    /**
     * 获取到账金额
     */
    BigDecimal getAccountFee(String costPointId);

    /**
     * 应付明细
     */
    ReceivableDetailDTO getPaymentDetail(String projectId);

    /**
     * 应付到账列表
     */
    List<PaymentDetailDTO> getPaymentDetailList(String projectId);

    /**
     * 获取到账金额
     */
    BigDecimal getPaymentAccountFee(String costPointId);

    /**
     * 总收入
     */
    List<StatisticDetailDTO> getGeneralIncome(StatisticDetailQueryDTO dto);

    /**
     * 总支出
     */
    List<StatisticDetailDTO> getTotalExpenditure(StatisticDetailQueryDTO dto);

    //分类统计
    ExpensesStatisticsDTO getCompanyBillClassStatistics(StatisticDetailQueryDTO dto);

    /**
     * 主营业务成本，直接项目成本中（合作设计分、技术审查费、其他支出）
     */
    List<StatisticDetailDTO> getProjectExpenditure(StatisticDetailQueryDTO dto);

    /**
     * 报销汇总
     */
    List<StatisticDetailDTO> getPaymentCollect(StatisticDetailQueryDTO dto);

    /**
     * 曲线总收入
     */
    List<StatisticDetailDTO> getCurveIncomeData(StatisticDetailQueryDTO dto);

    /**
     * 曲线总支出
     */
    List<StatisticDetailDTO> getCurveExpenditureData1(StatisticDetailQueryDTO dto);

    /**
     * 曲线主营业务成本，直接项目成本中（合作设计分、技术审查费、其他支出）
     */
    List<StatisticDetailDTO> getCurveExpenditureData2(StatisticDetailQueryDTO dto);

    /**
     * 曲线报销汇总
     */
    List<StatisticDetailDTO> getCurveExpenditureData3(StatisticDetailQueryDTO dto);

    List<ClassifiedStatisticDataDTO> getCompanyBillForLineData(StatisticDetailQueryDTO dto);
    /***
     * 获取合同总金额
     * */
    List<StatisticDetailDTO> getContractSumMoney(StatisticDetailQueryDTO dto);


    List<CompanyBillDetailDTO> getCompanyBillDetailByYear(StatisticDetailQueryDTO dto);

    List<Map<String, String>>  getRelationCompany(StatisticDetailQueryDTO dto);

    /**
     * 柱状图数据
     */
    List<StatisticDetailSummaryDTO> getCompanyColumnarData(StatisticDetailQueryDTO dto);

    /**
     * 按组织分组的柱状图数据
     */
    List<StatisticClassicSummaryDTO> getColumnarDataForOrgGroup(StatisticDetailQueryDTO dto);

    /**
     * 按时间分组的柱状图数据
     */
    List<StatisticClassicSummaryDTO> getColumnarDataForTimeGroup(StatisticDetailQueryDTO dto);
}
